package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;

public class InactiveAccountActivity extends AppCompatActivity {
    private static final String TAG = "Inactive Merchant Act";

    private MaterialButton portalLogin;
    private MaterialButton portalSignUp;
    private Button backButton;

    private Boolean shouldLogoutMerchant = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_account);

        portalLogin = findViewById(R.id.buttonInactiveMerchantLoginSite);
        portalSignUp = findViewById(R.id.buttonInactiveMerchantSignUpSite);
        backButton = findViewById(R.id.buttonInactiveMerchantBack);

        Intent intent = getIntent();
        shouldLogoutMerchant = intent.getBooleanExtra("shouldLogoutMerchant", false);

        setupClickListeners();
    }

    @Override
    protected void onDestroy() {
        AuthHelper.logOut(this);
        super.onDestroy();
    }

    private void setupClickListeners() {
        portalLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(GlobalConstants.INTENT_ENDING_URL, GlobalConstants.URL_PORTAL_LOGIN_TAIL);
            startActivity(intent);
        });

        portalSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(GlobalConstants.INTENT_ENDING_URL, GlobalConstants.URL_PORTAL_SIGNUP_TAIL);
            startActivity(intent);
        });

        backButton.setOnClickListener(view -> {
            if (!shouldLogoutMerchant) {
                finish();
            } else {
                AuthHelper.logOut(this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AuthHelper.logOut(this);
    }
}