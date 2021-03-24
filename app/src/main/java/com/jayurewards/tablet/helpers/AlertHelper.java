package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.jayurewards.tablet.screens.LoginMerchantActivity;
import com.jayurewards.tablet.screens.SplashScreenActivity;

import static android.content.ContentValues.TAG;

public class AlertHelper {

    public static void showAlert(Context context, String setTitle, String setMessage) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(setTitle);
        builder1.setMessage(setMessage);

        builder1.setPositiveButton(
                "OK",
                (dialog, id) -> Log.i(TAG, "OK button pressed"));

        builder1.show();
    }

    public static void showNetworkAlert(Context context) {
        showAlert(context, "Network Error", "Unable to connect to the internet. Please check your internet connection.");
    }
}
