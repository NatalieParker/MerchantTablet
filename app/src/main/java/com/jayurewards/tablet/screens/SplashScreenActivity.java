package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AuthHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Log.i(TAG,"SPLASH SCREEN SHOWN");

        boolean userLoggedIn = AuthHelper.isUserLoggedIn(this);

        if (userLoggedIn) {
            Log.i(TAG, "onCreate: \n USER IS LOGGED IN: " + userLoggedIn );
            AuthHelper.checkMerchantSubscription(SplashScreenActivity.this);
        } else {
            Log.i(TAG, "onCreate: \n USER IS NOT LOGGED IN: " + userLoggedIn );
            Intent intent = new Intent(SplashScreenActivity.this, LoginMerchantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



}