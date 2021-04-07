package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ActivityNotFoundException;
import android.content.Context;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.jayurewards.tablet.BuildConfig;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.models.MerchantModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginMerchantActivity extends AppCompatActivity {
    private static final String TAG = "LoginScreen";
    private static final int RC_SIGN_IN = 1;

    private EditText emailEditText;
    private EditText passwordEditText;
    private MaterialButton emailLoginButton;
    private MaterialButton buttonGoogle;
    private MaterialButton buttonApple;
    private MaterialButton buttonSignUp;
    private MaterialButton buttonForgotPassword;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;
    private SharedPreferences preferences;
    private ConstraintLayout spinner;

    private long lastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_merchant);

        emailEditText = findViewById(R.id.editTextLoginMerchantEmailAddress);
        passwordEditText = findViewById(R.id.editTextLoginMerchantPassword);
        emailLoginButton = findViewById(R.id.buttonLoginMerchantLoginSubmit);
        buttonGoogle = findViewById(R.id.buttonLoginMerchantGoogle);
        buttonApple = findViewById(R.id.buttonLoginMerchantApple);
        buttonSignUp = findViewById(R.id.buttonLoginMerchantSignUp);
        buttonForgotPassword = findViewById(R.id.buttonLoginMerchantForgotPassword);
        spinner = findViewById(R.id.spinnerLoginMerchant);

        auth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.googleSigninWebClientId)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        emailEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginMerchantActivity.this);

        enableEmailSubmit(false);
        setUpClickListeners();
    }

    /**
     * Click Listeners
     */
    private void setUpClickListeners() {
        emailLoginButton.setOnClickListener(v -> {
            preventDuplicateClicks();
            spinner.setVisibility(View.VISIBLE);
            hideKeyboard();

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!email.isEmpty()) {
                if (!isValidEmail(email)) {
                    emailEditText.setError("Please enter a valid email.");
                    emailEditText.requestFocus();
                    return;
                }
            }

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if (task.isSuccessful() && auth.getCurrentUser() != null) {
                    String firebaseUid = auth.getCurrentUser().getUid();
                    getMerchantData(firebaseUid);
                } else {
                    Log.e(TAG, "Firebase email and password auth error: ", task.getException());
                    AlertHelper.showAlert(LoginMerchantActivity.this, "Email Login Error",
                            "This email does not exist, or the password is incorrect. Please check and try again.");
                }
                spinner.setVisibility(View.GONE);
            });
        });

        buttonGoogle.setOnClickListener(v -> {
            preventDuplicateClicks();
            spinner.setVisibility(View.VISIBLE);
            hideKeyboard();

            if (v.getId() == R.id.buttonLoginMerchantGoogle) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        buttonApple.setOnClickListener(v -> {
            preventDuplicateClicks();
            spinner.setVisibility(View.VISIBLE);
            hideKeyboard();

            OAuthProvider.Builder provider = OAuthProvider.newBuilder("apple.com");
            Task<AuthResult> pending = auth.getPendingAuthResult();
            if (pending != null) {
                pending.addOnSuccessListener(authResult -> {
                    String firebaseUid = authResult.getUser().getUid();
                    getMerchantData(firebaseUid);

                }).addOnFailureListener(e -> {
                    Log.e(TAG, "Check Apple pending auth error: ", e);
                    AlertHelper.showNetworkAlert(LoginMerchantActivity.this);
                });

                spinner.setVisibility(View.GONE);

            } else {
                auth.startActivityForSignInWithProvider(LoginMerchantActivity.this, provider.build())
                        .addOnSuccessListener(authResult -> {
                                    String firebaseUid = authResult.getUser().getUid();
                                    getMerchantData(firebaseUid);
                                }
                        )
                        .addOnFailureListener(e -> {
                                    Log.e(TAG, "Sign in with Apple error: ", e);
                                    AlertHelper.showNetworkAlert(LoginMerchantActivity.this);
                                }
                        );

                spinner.setVisibility(View.GONE);
            }
        });

        buttonSignUp.setOnClickListener(v -> {
            preventDuplicateClicks();
            website(GlobalConstants.WEB_URL_PORTAL_SIGNUP);
        });

        buttonForgotPassword.setOnClickListener(v -> {
            preventDuplicateClicks();
            website(GlobalConstants.WEB_URL_PORTAL_FORGOT_PASSWORD);
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

            Log.i(TAG, "handleSignInResult: \n ACCOUNT: " + account.getEmail() + "\n" + account.getId() + "\n " + account.getIdToken());

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
                if (task.isSuccessful() && auth.getCurrentUser() != null) {
                    String firebaseUid = auth.getCurrentUser().getUid();
                    getMerchantData(firebaseUid);
                } else {
                    Log.e(TAG, "Firebase email and password auth error: ", task.getException());
                    AlertHelper.showAlert(LoginMerchantActivity.this, "Email Login Error",
                            "This email does not exist, or the password is incorrect. Please check and try again.");
                }

                spinner.setVisibility(View.GONE);
            });

        } catch (ApiException e) {
            Log.e(TAG, "Google sign in error: " + e);

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

            spinner.setVisibility(View.GONE);
        }
    }

    /**
     * Networking
     */
    private void getMerchantData(String firebaseUid) {
        Call<MerchantModel> call = RetrofitClient.getInstance().getRestAuth().getMerchant(firebaseUid);
        call.enqueue(new Callback<MerchantModel>() {
            @Override
            public void onResponse(@NonNull Call<MerchantModel> call, @NonNull Response<MerchantModel> response) {
                MerchantModel merchant = response.body();
                String firstName = merchant.getFirstName();
                String email = merchant.getEmail();
                String firebaseUid = merchant.getFirebaseUid();
                String stripeId = merchant.getStripeId();
                String subscriptionId = merchant.getSubscriptionId();
                int merchantId = merchant.getMerchantId();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(GlobalConstants.FIRST_NAME, firstName);
                editor.putString(GlobalConstants.EMAIL, email);
                editor.putString(GlobalConstants.MERCHANT_FIREBASE_UID, firebaseUid);
                editor.putInt(GlobalConstants.MERCHANT_ID, merchantId);
                editor.putString(GlobalConstants.STRIPE_ID, stripeId);
                editor.putString(GlobalConstants.SUBSCRIPTION_ID, subscriptionId);
                editor.apply();

                AuthHelper.checkMerchantSubscription(LoginMerchantActivity.this);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<MerchantModel> call, @NonNull Throwable t) {
                Log.e(TAG, "Get merchant data error: " + t.getMessage());
                AlertHelper.showNetworkAlert(LoginMerchantActivity.this);
                spinner.setVisibility(View.GONE);
            }
        });
    }

    private void website(String setWebsite) {
        Intent openWebsite = new Intent(Intent.ACTION_VIEW);
        try {
            openWebsite.setData(Uri.parse(setWebsite));
            startActivity(openWebsite);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, "Open website link error: " + e.getMessage());
            AlertHelper.showNetworkAlert(this);
        }
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
        boolean goEnableButton = email.length() >= 1 && password.length() >= 1;
        enableEmailSubmit(goEnableButton);
    }

    private void enableEmailSubmit(boolean enabled) {
        if (!enabled) {
            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
            emailLoginButton.setEnabled(false);

        } else {
            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            emailLoginButton.setEnabled(true);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

}