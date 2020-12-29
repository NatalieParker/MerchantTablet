package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import static android.content.ContentValues.TAG;

public class AlertHelper {

    public static void showAlert(Context context, String setTitle, String setMessage) {
        androidx.appcompat.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(setTitle);
        builder1.setMessage(setMessage);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "OK button pressed");
                    }
                });

        builder1.show();
    }

    public static void showNetworkAlert(Context context) {
        showAlert(context, "Network Error", "Unable to connect to the internet. Please check your internet connection.");
    }
}
