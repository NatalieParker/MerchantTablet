package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jayurewards.tablet.R;

public class LoginTeam extends AppCompatActivity {

    EditText phoneNumberInput;
    Button buttonCancel;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_team);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSend = findViewById(R.id.buttonSend);

        setUpClickListeners();
    }

    private void setUpClickListeners () {
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginTeam.this, RegistrationTeam.class);
                startActivity(intent);
            }
        });
    }
}