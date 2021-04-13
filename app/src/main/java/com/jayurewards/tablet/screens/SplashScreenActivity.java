package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AuthHelper;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        boolean userLoggedIn = AuthHelper.isUserLoggedIn(this);

        if (userLoggedIn) {
            AuthHelper.checkMerchantSubscription(SplashScreenActivity.this);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, LoginMerchantActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}