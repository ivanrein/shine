package com.shineapptpa.rei.shine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rei on 12/22/2015.
 */
public class ShineUser {
    private static String currentUserEmail;
    private static String currentUserSchool;
    private static String currentUserGender;
    private static String currentUserFullname;
    private static String currentUserBio;
    private static HashMap<String, String> currentUser;
    private String username;
    private String school;
    private String gender;
    private String fullname;
    private String bio;

    public String getFullname() {
        return fullname;
    }


    public static HashMap<String, String> getCurrentUser(){
        return currentUser;
    }
    public ShineUser(String username, String school, String gender, String fullname) {
        this.username = username;
        this.school = school;
        this.gender = gender;
        this.fullname = fullname;
    }

    public static void resetCurrent(){
        currentUserEmail = null;
        currentUserSchool= null;
        currentUserGender= null;
        currentUserFullname = null;
        currentUser = null;
    }






    public static void setCurrentUser(String currentEmail,  String uschool,String cgender,
                                      String fname, String bio) {
        ShineUser.currentUserEmail = currentEmail;
        ShineUser.currentUserSchool = uschool;
        ShineUser.currentUserGender = cgender;
        ShineUser.currentUserFullname = fname;
        ShineUser.currentUserBio = bio;
        if(currentUser == null)
            currentUser = new HashMap<>(6);
        currentUser.put("email", currentEmail);
        currentUser.put("school", uschool);
        currentUser.put("gender", cgender);
        currentUser.put("name", fname);
        currentUser.put("bio", bio);
    }

    public static void setCurrentUser(JSONObject userInfos, JSONObject schoolInfo){
        try {
            ShineUser.currentUserEmail = userInfos.getString("email");
            ShineUser.currentUserFullname = userInfos.getString("name");
            ShineUser.currentUserBio = userInfos.getString("bio");
            ShineUser.currentUserGender = userInfos.getString("gender");
            ShineUser.currentUserSchool = schoolInfo.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(currentUser == null)
            currentUser = new HashMap<>(6);
        currentUser.put("email", ShineUser.currentUserEmail);
        currentUser.put("school", ShineUser.currentUserSchool);
        currentUser.put("gender", ShineUser.currentUserGender);
        currentUser.put("name", ShineUser.currentUserFullname);
        currentUser.put("bio", ShineUser.currentUserBio);
    }




    public String getUsername() {
        return username;
    }

    public String getSchool() {
        return school;
    }

    public String getGender() {
        return gender;
    }

    public String getBio(){
        return bio;
    }
}
