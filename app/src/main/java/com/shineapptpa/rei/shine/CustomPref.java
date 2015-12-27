package com.shineapptpa.rei.shine;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by rei on 12/27/2015.
 */
public class CustomPref {

    private final static String PREF_ACCESS_TOKEN = "user access token";



    public static String getUserAccessToken(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_ACCESS_TOKEN, null);
    }

    public static void setUserAccessToken(Context context, String acToken){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_ACCESS_TOKEN, acToken)
                .apply();
    }
}
