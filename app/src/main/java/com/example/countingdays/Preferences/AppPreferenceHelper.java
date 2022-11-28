package com.example.countingdays.Preferences;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppPreferenceHelper {

    @Nullable
    private static AppPreferenceHelper instance = null;

    public final static String USERNAME = "username";
    public final static String Password = "Password";
    public final static String LastInteraction = "LastInteraction";
    public final static String Profile = "Profile";
    public final static String LastPWD = "LastPwd";
    public final static String Screen = "Screen";

    private Context context;


    public AppPreferenceHelper(Context context) {
        this.context = context;
    }


    public static AppPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new AppPreferenceHelper(context);

        }
        return instance;
    }




}
