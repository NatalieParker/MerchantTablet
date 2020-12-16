package com.example.merchanttablet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginScreen";

    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton emailLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextLoginEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        emailLoginButton = findViewById(R.id.buttonLoginSubmit);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        enableEmailSubmit(false);
        setUpClickListeners();
    }


    private void setUpClickListeners() {
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                Log.i(TAG, "LOGIN SUBMITTED AT: " + date);

                Intent intent = new Intent(MainActivity.this, UserKeypadActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * EDIT TEXT METHODS
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.i(TAG, "EMAIL: " + email);
        Log.i(TAG, "PASSWORD: " + password);
        boolean goEnableButton = email.length() >= 1 && password.length() >= 1;
        enableEmailSubmit(goEnableButton);
    }

    private void enableEmailSubmit(boolean enabled) {
        if (!enabled) {
            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.black)));
            emailLoginButton.setEnabled(false);

        } else {
            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
            emailLoginButton.setEnabled(true);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}