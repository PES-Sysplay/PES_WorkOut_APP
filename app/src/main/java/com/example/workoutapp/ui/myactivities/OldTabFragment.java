package com.example.workoutapp.ui.myactivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.workoutapp.Activitat;
import com.example.workoutapp.Chat;
import com.example.workoutapp.Organizer;
import com.example.workoutapp.R;
import com.example.workoutapp.Review;
import com.example.workoutapp.UserActivityController;
import com.example.workoutapp.ui.home.ActivityListAdapter;
import com.example.workoutapp.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class OldTabFragment extends HomeFragment {

    ViewGroup root;
    ArrayList<Activitat> oldActivities;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        privInflater = inflater;

        root = (ViewGroup) inflater.inflate(R.layout.fragment_act_old, container, false);

        recyclerView = root.findViewById(R.id.recyclerviewold);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setShowHideAnimationEnabled(false);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        super.updateType(root);

        adapter = ActivityListAdapter.getInstance(root.getContext(), new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }

    private void updateList() {
        UserActivityController uc = new UserActivityController(getContext());

        uc.getJoinedActivities(new UserActivityController.VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String message) {

            }

            @Override
            public void onResponseFavorites(ArrayList<Activitat> ret) {

            }

            @Override
            public void onResponseFav() {

            }

            @Override
            public void onResponseJoinedActivites(ArrayList<Activitat> ret) {
                oldActivities = ret;
                displayOldAct();
            }

            @Override
            public void onResponseReviewList(ArrayList<Review> ret) {

            }

            @Override
            public void onResponseOrganizationList(ArrayList<Organizer> ret) {

            }

            @Override
            public void onResponseChat(ArrayList<Chat> ret) {

            }
        });
    }

    private void displayOldAct() {
        ArrayList<Activitat> oldAux = new ArrayList<>();

        Date date = Calendar.getInstance().getTime();
        Calendar currentTime = Calendar.getInstance();
        currentTime.setTime(date);

        currentTime.set(Calendar.HOUR_OF_DAY, 0);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);

        if (oldActivities != null){
            for (Activitat act : oldActivities) {

                Calendar dateAux = Calendar.getInstance();
                dateAux.setTimeInMillis(act.getTimestamp() * 1000L); //time in ms
                dateAux.set(Calendar.HOUR_OF_DAY, 0);
                dateAux.set(Calendar.MINUTE, 0);
                dateAux.set(Calendar.SECOND, 0);
                if (dateAux.before(currentTime)) oldAux.add(act);
                adapter.setList(oldAux);
            }
        }
    }
}
