package com.jayurewards.tablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
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
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.models.MerchantModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreen";
    private static final int RC_SIGN_IN = 1;
    private long lastClickTime = 0;

    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton emailLoginButton;
    private MaterialButton buttonGoogle;
    private MaterialButton buttonSignUp;
    private MaterialButton buttonForgotPassword;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences preferences;
    private ConstraintLayout constraintLayoutSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText = findViewById(R.id.editTextLoginEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginPassword);
        emailLoginButton = findViewById(R.id.buttonLoginSubmit);
        buttonGoogle = findViewById(R.id.buttonGoogle);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);
        emailEditText.requestFocus();
        auth = FirebaseAuth.getInstance();
        enableEmailSubmit(false);
        setUpClickListeners();
        constraintLayoutSpinner = findViewById(R.id.constraintLayoutSpinner);
        constraintLayoutSpinner.setVisibility(View.GONE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Log.i(TAG, "Email: " + preferences.getString("email", null));
        Log.i(TAG, "Firebase UID: " + preferences.getString("firebaseUid", null));
        Log.i(TAG, "Merchant ID: " + preferences.getInt("merchantId", 0));

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (currentUser != null) {
            Log.i(TAG, "User" + currentUser.toString());
        } else {
            Log.i(TAG, "No Current User");
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
        }
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
                constraintLayoutSpinner.setVisibility(View.VISIBLE);
                preventDuplicateClicks();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                signIn(email, password);
                constraintLayoutSpinner.setVisibility(View.GONE);
            }
        });
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutSpinner.setVisibility(View.VISIBLE);
                preventDuplicateClicks();
                switch (v.getId()) {
                    case R.id.buttonGoogle:
                        googleSignIn();
                        constraintLayoutSpinner.setVisibility(View.GONE);
                        break;
                }

            }
        });
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutSpinner.setVisibility(View.VISIBLE);
                preventDuplicateClicks();
                website("https://portal.jayu.us/auth/signup");
                constraintLayoutSpinner.setVisibility(View.GONE);
            }
        });
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayoutSpinner.setVisibility(View.VISIBLE);
                preventDuplicateClicks();
                website("https://portal.jayu.us/auth/forgot-password");
                constraintLayoutSpinner.setVisibility(View.GONE);
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
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signIn(String email, String password) {

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
                        if (task.isSuccessful() && auth.getCurrentUser() != null) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            String firebaseUid = auth.getCurrentUser().getUid();
                            Log.i(TAG, "Firebase ID: " + firebaseUid);
                            getMerchantData(firebaseUid);
                        } else {
                            Log.e(TAG, "signInWithEmail:failure", task.getException());

                            AlertHelper.showAlert(MainActivity.this, "Email Login Error", "This email does not exist, or the password is incorrect. Please check and try again.");
                        }

                        hideKeyboard();
                        goToKeypadPage();

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

        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e);
//
            switch (e.getStatusCode()) {
                case 12501:
                    Log.w(TAG, "User canceled the Google Sign in");
                    break;

                case 12502:
                    AlertHelper.showAlert(this, "Please Wait", "Google is currently logging you in.");
                    break;

                case 12500:
                    AlertHelper.showAlert(this, "Unable To Login",
                            "Google was not able to log you in. Please check your account and try again.");
                    break;

                case 5:
                    AlertHelper.showAlert(this, "Invalid Account",
                            "Please check your account for accuracy, and try again.");
                    break;

                default:
                    AlertHelper.showNetworkAlert(this);
                    break;
            }

        }
        hideKeyboard();
    }

    private void website(String setWebsite) {
        Intent openWebsite = new Intent(Intent.ACTION_VIEW);
        try {
            openWebsite.setData(Uri.parse(setWebsite));
            startActivity(openWebsite);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "Website error");
            AlertHelper.showAlert(this, "Error", "Something went wrong, please check your internet connection and try again.");
        }
    }

    private void getMerchantData(String firebaseUid) {
        Call<MerchantModel> call = RetrofitClient.getInstance().getRestAuth().getMerchant(firebaseUid);
        call.enqueue(new Callback<MerchantModel>() {
            @Override
            public void onResponse(@NonNull Call<MerchantModel> call, @NonNull Response<MerchantModel> response) {
                MerchantModel merchant = response.body();
                String firstName = merchant.getFirstName();
                String email = merchant.getEmail();
                String firebaseUid = merchant.getFirebaseUid();
                int merchantId = merchant.getMerchantId();

                Log.i(TAG, "Merchant data response retrieved: " + firstName);
                Log.i(TAG, "Merchant data retrieved: " + merchant);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email);
                editor.putString("firebaseUid", firebaseUid);
                editor.putInt("merchantId", merchantId);
                editor.apply();
            }

            @Override
            public void onFailure(@NonNull Call<MerchantModel> call, @NonNull Throwable t) {

                Log.e(TAG, "Get merchant data error: " + t.getMessage());
            }
        });
    }

}