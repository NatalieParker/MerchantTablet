package com.jayurewards.tablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreen";
    private static final int RC_SIGN_IN = 1;
    private long lastClickTime = 0;

    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton emailLoginButton;
    private MaterialButton buttonGoogle;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextLoginEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        emailLoginButton = findViewById(R.id.buttonLoginSubmit);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        emailEditText.requestFocus();
        auth = FirebaseAuth.getInstance();
        enableEmailSubmit(false);
        setUpClickListeners();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // updateUI(account);

        if (currentUser != null) {
            Log.i(TAG, "User" + currentUser.toString());
        } else {
            Log.i(TAG, "No Current User");
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
    }

    private void preventDuplicateClicks() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
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

    private void setUpClickListeners() {
        emailLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventDuplicateClicks();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signIn(email, password);
            }
        });
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preventDuplicateClicks();
                switch (v.getId()) {
                    case R.id.buttonGoogle:
                        googleSignIn();
                        break;
                }
            }
        });
    }

    private void goToKeypadPage() {
        Date date = new Date();

        Log.i(TAG, "LOGIN SUBMITTED AT: " + date);

        Intent intent = new Intent(MainActivity.this, UserKeypadActivity.class);
        startActivity(intent);
    }

    private void googleSignIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
        alert("Test title google", "Test message");
    }

    private void signIn(String email, String password) {
        alert("Test title email", "Test message");

        if (!email.isEmpty()) {
            if (!isValidEmail(email)) {
                emailEditText.setError("Please enter a valid email.");
                emailEditText.requestFocus();
                return;
            }
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {
                                Log.i(TAG, "User2" + " " + user.getEmail() + "  " + user.getDisplayName());
                                goToKeypadPage();
                            } else {
                                Log.i(TAG, "No Current User2");
                            }
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Email or password is incorrect.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                            // ...
                        }

                        hideKeyboard();

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            goToKeypadPage();
//            updateUI(account);

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);

        }
        hideKeyboard();
    }

    private void alert(String setTitle, String setMessage) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setTitle(setTitle);
        builder1.setMessage(setMessage);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Yes button pressed");
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();

    }
}