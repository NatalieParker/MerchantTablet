package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jayurewards.tablet.R;

public class RegistrationTeam extends AppCompatActivity {

    Button buttonCancelRegistration;
    Button buttonSubmitRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_team);

        buttonCancelRegistration = findViewById(R.id.buttonCancelRegistration);
        buttonSubmitRegistration = findViewById(R.id.buttonSubmitRegistration);

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        buttonCancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}