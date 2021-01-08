package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.jayurewards.tablet.MainActivity;
import com.jayurewards.tablet.UserKeypadActivity;

import java.util.Date;

public class AuthHelper {

    private static final String TAG = "log out";

    public static void logOut(Context currentScreen) {
        FirebaseAuth.getInstance().signOut();
        Date date = new Date();
        Intent intent = new Intent(currentScreen, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentScreen.startActivity(intent);

        Log.i(TAG, "USER SIGNED OUT AT: " + date);
    }

    public static void logInCheck() {
        
    }
}
