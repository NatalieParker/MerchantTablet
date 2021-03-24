package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jayurewards.tablet.screens.LoginMerchantActivity;
import com.jayurewards.tablet.screens.UserKeypadActivity;

import java.util.Date;

public class AuthHelper {

    private static final String TAG = "log out";


    public static void logInCheck(Context currentScreen) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) logOut(currentScreen);
    }

    // TODO: Test if this works with user logged in our out
    public static boolean isUserLoggedIn(Context currentScreen) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.i(TAG, "USER: " + user);
        if (user != null) {
            return true;
        } else {
            logOut(currentScreen);
            return false;
        }
    }


    public static void checkMerchantSubscription(Context context) {

        Intent intent = new Intent(context, UserKeypadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

    }

    public static void logOut(Context currentScreen) {

        AlertDialog.Builder builder = new AlertDialog.Builder(currentScreen);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");

        builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(currentScreen);
                sharedPref.edit().remove(GlobalConstants.MERCHANT_ID).apply();
                sharedPref.edit().remove(GlobalConstants.MERCHANT_FIREBASE_UID).apply();

                //TODO: Manually remove all team member values from shared preferences

                sharedPref.edit().clear().apply();

                FirebaseAuth.getInstance().signOut();
                Date date = new Date();
                Intent intent = new Intent(currentScreen, LoginMerchantActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                currentScreen.startActivity(intent);

                Log.i(TAG, "USER SIGNED OUT AT: " + date);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }





}
