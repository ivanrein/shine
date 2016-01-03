package com.shineapptpa.rei.shine;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rei on 12/22/2015.
 */
public class ShineUser {

    private static HashMap<String, String> currentUser;
    private static HashMap<String, String> currentSchool;

    private HashMap<String, String> user;
    private HashMap<String, String> school;

    public final static String MAP_USER_ID = "user_id";
    public final static String MAP_USER_BITMAP = "decoded_bitmap";
    public final static String MAP_USER_NAME = "name";
    public final static String MAP_USER_SCHOOL = "school";
    public final static String MAP_USER_BIO = "bio";
    public final static String MAP_USER_GENDER = "gender";
    public final static String MAP_USER_EMAIL = "email";

    public final static String MAP_SCHOOL_NAME = "name";
    public final static String MAP_SCHOOL_ID = "id";
    public final static String MAP_SCHOOL_LAT = "latitude";
    public final static String MAP_SCHOOL_LONG = "longitude";
    public static boolean fetching = false;
    public static HashMap<String, String> getCurrentUser(){
        return currentUser;
    }
    public static HashMap<String, String> getCurrentSchool(){
        return currentSchool;
    }

    public HashMap<String, String> getUser(){
        return this.user;
    }
    public HashMap<String, String> getSchool(){
        return this.school;
    }

    public ShineUser(JSONObject userInfos, JSONObject schoolInfo) {
        this.user = new HashMap<>(6);
        this.school = new HashMap<>(6);
        try {
            String schoolName = schoolInfo.getString("name");
            user.put(MAP_USER_EMAIL,userInfos.getString("email"));
            user.put(MAP_USER_SCHOOL, schoolName);
            user.put(MAP_USER_GENDER, userInfos.getString("gender"));
            user.put(MAP_USER_NAME, userInfos.getString("name"));
            user.put(MAP_USER_BIO, userInfos.getString("bio"));

            school.put(MAP_SCHOOL_NAME, schoolName);
            school.put(MAP_SCHOOL_ID, schoolInfo.getString("id"));
            school.put(MAP_SCHOOL_LAT, schoolInfo.getString("latitude"));
            school.put(MAP_SCHOOL_LONG, schoolInfo.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public ShineUser(String id, String name, String schoolName, String gender, String encodedBitmap)
    {
        user = new HashMap<>();
        user.put(MAP_USER_ID, id);
        user.put(MAP_USER_NAME, name);
        user.put(MAP_USER_GENDER, gender);
        user.put(MAP_USER_SCHOOL, schoolName);
        user.put(MAP_USER_BITMAP, encodedBitmap);
    }

    public static void resetCurrent(){
        currentSchool = null;
        currentUser = null;
    }

    public static void setCurrentUser(){
            currentUser = new HashMap<>(6);
            currentUser.put(MAP_USER_EMAIL,"email");
            currentUser.put(MAP_USER_GENDER, "gender");
            currentUser.put(MAP_USER_NAME, "name");
            currentUser.put(MAP_USER_BIO, "bio");
            currentUser.put(MAP_SCHOOL_NAME, "sekolah");
    }

    public static void setCurrentUser(JSONObject userInfos, JSONObject schoolInfo){
        if(currentUser == null)
            currentUser = new HashMap<>(6);
        if(currentSchool == null){
            currentSchool = new HashMap<>(6);
        }
        try {
            String school = schoolInfo.getString("name");

            currentUser.put(MAP_USER_EMAIL,userInfos.getString("email"));
            currentUser.put(MAP_USER_SCHOOL, school);
            currentUser.put(MAP_USER_GENDER, userInfos.getString("gender"));
            currentUser.put(MAP_USER_NAME, userInfos.getString("name"));
            currentUser.put(MAP_USER_BIO, userInfos.getString("bio"));

            currentSchool.put(MAP_SCHOOL_NAME, school);
            currentSchool.put(MAP_SCHOOL_ID, schoolInfo.getString("id"));
            currentSchool.put(MAP_SCHOOL_LAT, schoolInfo.getString("latitude"));
            currentSchool.put(MAP_SCHOOL_LONG, schoolInfo.getString("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * callback dibikin sama yang manggil fetchCurrentUser, terus dijalanin ama fetchCurrentUser di onresponse.
     * barangkali bisa dipake bwt nge stop
     * animasi muter" gitu di activity yang manggil ato nampilin pesan.. barangkali..
     * @param context
     * @param callback
     */
    public static void fetchCurrentUser(final Context context, final CustomCallback callback){
        fetching = true;
        Log.d("fetch current user", "start");
        RequestQueue mQue = Volley.newRequestQueue(context);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET,
                context.getString(R.string.laravel_API_url) + "fetchCurrentUser", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            JSONObject userInfos = response.getJSONObject("user");
                            JSONObject schoolInfo = response.getJSONObject("school");
                            setCurrentUser(userInfos, schoolInfo);
                            callback.callback();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        fetching = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fetching = false;
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                params.put("utoken", CustomPref.getUserAccessToken(context));
                return params;
            }
        };
        mQue.add(req);
    }




}