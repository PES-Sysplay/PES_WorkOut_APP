package com.example.workoutapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserActivityController {

    public static final String URL = "https://dev-pes-workout.herokuapp.com";
    Context ctx;


    public UserActivityController(Context context) {
        this.ctx = context;
    }

    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String message);

        void onResponseFavorites(ArrayList<Activitat> ret);

        void onResponseFav();
        
        void onResponseJoinedActivites(ArrayList<Activitat> ret);

        void onResponseReviewList(ArrayList<Review> ret);

        void onResponseOrganizationList(ArrayList<Organizer> ret);

        void onResponseChat(ArrayList<Chat> ret);

        void onResponseReportReview();
    }

    public void favorite(Integer activityId, UserActivityController.VolleyResponseListener vrl) {
        String favURL = URL + "/api/favorite/";
        Map<String, Integer> params = new HashMap<>();
        params.put("activity_id", activityId);
        JSONObject jsonBody = new JSONObject(params);
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, favURL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                vrl.onResponseFav();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                vrl.onError("Error al dar a favoritos");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        RequestSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }

    public void unfavorite(Integer activityID, UserActivityController.VolleyResponseListener vrl) {
        String unfavURL = URL + "/api/favorite/"+activityID+"/";
        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, unfavURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response.toString());
                vrl.onResponseFav();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                vrl.onError("Error al dejar de tener en favoritos");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

    public void getFavorites(UserActivityController.VolleyResponseListener vrl) {
        String favsURL = URL + "/api/activity/?favorite=true";
        ArrayList<Activitat> ret = new ArrayList<Activitat>();

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, favsURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonact = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Activitat act = gson.fromJson(jsonact.toString(), Activitat.class);
                                ret.add(act);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        vrl.onResponseFavorites(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "No se han encontrado actividades favoritas", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se han encontrado actividades favoritas");
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void getJoinedActivities(UserActivityController.VolleyResponseListener vrl) {
        String favsURL = URL + "/api/activity/?joined=true";
        ArrayList<Activitat> ret = new ArrayList<Activitat>();

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, favsURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonact = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Activitat act = gson.fromJson(jsonact.toString(), Activitat.class);
                                ret.add(act);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        vrl.onResponseJoinedActivites(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "No se han encontrado actividades favoritas", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se han encontrado actividades favoritas");
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void sendReport(Integer activityID, String comment, UserActivityController.VolleyResponseListener vrl) {
        String reportURL = URL + "/api/report/";
        Map<String, String> params = new HashMap<>();
        params.put("activity_id", activityID.toString());
        params.put("comment", comment);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, reportURL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                vrl.onResponse("Reporte enviado");
            }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    vrl.onError("Error al enviar el reporte");
                }
            }) {
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String userToken = UserSingleton.getInstance().getId();
                    Log.d("", "");
                    headers.put("Authorization", "Token " + userToken);
                    return headers;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

            };

        RequestSingleton.getInstance(ctx).addToRequestQueue(req);

    }

    public void sendReview(Integer activityID, String comment, Float stars, UserActivityController.VolleyResponseListener vrl) {
        String reviewURL = URL + "/api/review/";
        Map<String, Object> params = new HashMap<>();
        params.put("activity_id", activityID.toString());
        params.put("comment", comment);
        params.put("stars", stars);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, reviewURL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                vrl.onResponse("Review enviada");
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                vrl.onError("Error al enviar la review");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        RequestSingleton.getInstance(ctx).addToRequestQueue(req);

    }

    public void getReviews(String organization, UserActivityController.VolleyResponseListener vrl) {
        String reviewURL = URL + "/api/review/?organization=" + organization;
        ArrayList<Review> ret = new ArrayList<Review>();

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, reviewURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonact = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Review review = gson.fromJson(jsonact.toString(), Review.class);
                                //review.setRating((float)jsonact.get("stars"));
                                ret.add(review);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        vrl.onResponseReviewList(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "No se han encontrado reseñas", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se han encontrado reseñas");
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void getOrganizers(UserActivityController.VolleyResponseListener vrl) {
        String reviewURL = URL + "/api/organization/";
        ArrayList<Organizer> ret = new ArrayList<Organizer>();

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, reviewURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("JSON", String.valueOf(response.length()));

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonact = response.getJSONObject(i);

                                Log.d("JSON", jsonact.toString());

                                Gson gson = new Gson();
                                Organizer org = gson.fromJson(jsonact.toString(), Organizer.class);
                                ret.add(org);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        vrl.onResponseOrganizationList(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "No se han encontrado organizers", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se han encontrado organizers");
                        Log.d("ERROR", error.toString());
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);

    }

    public void sendMessage(Integer activityID, String text, UserActivityController.VolleyResponseListener vrl) {
        String reviewURL = URL + "/api/message/";
        Map<String, Object> params = new HashMap<>();
        params.put("activity_id", activityID.toString());
        params.put("text", text);
        JSONObject jsonBody = new JSONObject(params);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, reviewURL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response.toString());
                vrl.onResponse("Mensaje enviado");
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
                vrl.onError("Error al enviar la mensaje");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };

        RequestSingleton.getInstance(ctx).addToRequestQueue(req);

    }

    public void getChats(UserActivityController.VolleyResponseListener vrl) {
        String chatURL = URL + "/api/chat";
        ArrayList<Chat> ret = new ArrayList<Chat>();

        JsonArrayRequest req = new JsonArrayRequest
                (Request.Method.GET, chatURL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonact = response.getJSONObject(i);
                                Gson gson = new Gson();
                                Chat chat = gson.fromJson(jsonact.toString(), Chat.class);
                                ret.add(chat);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        vrl.onResponseChat(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ctx, "No se ha encontrado el chat", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se ha encontrado el chat");
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void getChatMessages(int chat_id, UserActivityController.VolleyResponseListener vrl) {
        String messagesURL = URL + "/api/chat/" + chat_id + "/";
        ArrayList<Chat> ret = new ArrayList<Chat>();

        JsonObjectRequest req = new JsonObjectRequest
                (Request.Method.GET, messagesURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        Chat chat = gson.fromJson(response.toString(), Chat.class);
                        ret.add(chat);

                        vrl.onResponseChat(ret);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print(error.getMessage());
                        Toast.makeText(ctx, "No se han encontrado los mensajes", Toast.LENGTH_SHORT).show();
                        vrl.onError("No se han encontrado los mensajes");
                    }
                }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(req);
    }

    public void reportReview(Integer reviewID, UserActivityController.VolleyResponseListener vrl) {
        String joinActURL = URL + "/api/report_review/";
        Map<String, Integer> params = new HashMap<>();
        params.put("review_id", reviewID);
        JSONObject jsonBody = new JSONObject(params);
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, joinActURL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                vrl.onResponseReportReview();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());

                if(error.networkResponse.statusCode == 400){
                    String errorMsg = new String(error.networkResponse.data);
                    errorMsg = errorMsg.replace("[\"", "");
                    errorMsg = errorMsg.replace("\"]", "");

                    if(errorMsg.contains("Already reported")) vrl.onError("La reseña ya esta reportada");
                    else vrl.onError(errorMsg);
                }
                else vrl.onError("Error al reportar la reseña");
            }
        }) {
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userToken = UserSingleton.getInstance().getId();
                Log.d("", "");
                headers.put("Authorization", "Token " + userToken);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

        };
        RequestSingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest);
    }
}
