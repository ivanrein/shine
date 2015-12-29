package com.shineapptpa.rei.shine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by rei on 12/22/2015.
 */
public class ShineUser {

    private static HashMap<String, String> currentUser;
    private static HashMap<String, String> currentSchool;

    private
    HashMap<String, String> user;
    private HashMap<String, String> school;


    public final static String MAP_USER_NAME = "name";
    public final static String MAP_USER_SCHOOL = "school";
    public final static String MAP_USER_BIO = "bio";
    public final static String MAP_USER_GENDER = "gender";
    public final static String MAP_USER_EMAIL = "email";

    public final static String MAP_SCHOOL_NAME = "name";
    public final static String MAP_SCHOOL_ID = "id";
    public final static String MAP_SCHOOL_LAT = "latitude";
    public final static String MAP_SCHOOL_LONG = "longitude";

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

    public static void resetCurrent(){
        currentSchool = null;
        currentUser = null;
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




}
