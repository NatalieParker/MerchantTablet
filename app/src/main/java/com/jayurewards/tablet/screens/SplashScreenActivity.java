package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jayurewards.tablet.R;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(SplashScreenActivity.this, LoginMerchantActivity.class);
        startActivity(intent);

        Log.i(TAG,"SPLASH SCREEN SHOWN");
    }

}