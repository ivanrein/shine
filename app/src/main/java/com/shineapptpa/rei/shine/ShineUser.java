package com.shineapptpa.rei.shine;

/**
 * Created by rei on 12/22/2015.
 */
public class ShineUser {
    private static String currentUserEmail;
    private static String currentUserSchool;
    private static String currentUserGender;
    private static String currentUserFullname;
    private String username;
    private String school;
    private String gender;
    private String fullname;

    public String getFullname() {
        return fullname;
    }

    public static String getCurrentUserFullname() {
        return currentUserFullname;
    }

    public ShineUser(String username, String school, String gender, String fullname) {
        this.username = username;
        this.school = school;
        this.gender = gender;
        this.fullname = fullname;
    }

    public void resetCurrent(){
        this.username = null;
        this.school = null;
        this.gender = null;
        this.fullname = null;
    }

    public static String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public static void setCurrentUserGender(String currentUserGender) {
        ShineUser.currentUserGender = currentUserGender;
    }

    public static String getCurrentUserGender() {

        return currentUserGender;
    }

    public static void setCurrentUserSchool(String currentUserSchool) {
        ShineUser.currentUserSchool = currentUserSchool;
    }

    public static void setCurrentUser(String currentEmail,  String uschool,String cgender,  String fname) {
        ShineUser.currentUserEmail = currentEmail;
        ShineUser.currentUserSchool = uschool;
        ShineUser.currentUserGender = cgender;
        ShineUser.currentUserFullname = fname;
    }

    public static String getCurrentUserSchool() {
        return currentUserSchool;
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
}
