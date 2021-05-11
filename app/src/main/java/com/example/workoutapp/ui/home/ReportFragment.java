package com.example.workoutapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workoutapp.Activitat;
import com.example.workoutapp.R;
import com.example.workoutapp.UserActivityController;

import java.util.ArrayList;
import java.util.List;


public class ReportFragment extends Fragment {

    Spinner reportSpinner;
    Button reportBtn;
    TextView descriptionTextView;
    List<Activitat> activity_list = new ArrayList<>();
    int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity_list = ActivityListAdapter.getInstance(view.getContext(), new ArrayList<>()).copyInfo();

        pos = getActivity().getIntent().getIntExtra("Position recycler",0);

        reportSpinner = view.findViewById(R.id.ReportType);

        ArrayList<String> elements = new ArrayList<>();

        elements.add("Incumplimiento normativa COVID-19");
        elements.add("Publicidad engañosa");
        elements.add("Problemas con la organización");
        elements.add("Otro motivo");

        ArrayAdapter adp = new ArrayAdapter(view.getContext(), android.R.layout.simple_spinner_dropdown_item, elements);

        reportSpinner.setAdapter(adp);

        reportBtn = view.findViewById(R.id.sendReportBt);
        descriptionTextView = view.findViewById(R.id.descriptionEditText);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = descriptionTextView.getText().toString();
                String text = reportSpinner.getSelectedItem().toString();

                if (description.matches("")) {
                    Toast.makeText(view.getContext(),  "Añade una descripción detallando el problema", Toast.LENGTH_SHORT).show();
                }
                else {
                    String comment = text + ": " + description;
                    UserActivityController UAController = new UserActivityController(view.getContext());
                    UAController.sendReport(activity_list.get(pos).getId(), comment, new UserActivityController.VolleyResponseListener() {

                        @Override
                        public void onError(String message) {
                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String message) {
                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                            activity_list.get(pos).toggleReported();
                            ((ReportActivity)getActivity()).goToSecondFragment();
                        }

                        @Override
                        public void onResponseFavorites(ArrayList<Activitat> ret) {

                        }

                        @Override
                        public void onResponseJoinedActivites(ArrayList<Activitat> ret) {

                        }
                    });

                }
            }
        });

    }
}