package com.jayurewards.tablet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.button.MaterialButton;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.GoogleAuthProvider;
//import com.jayurewards.jayu.BuildConfig;
//import com.jayurewards.jayu.R;
//import com.jayurewards.jayu.helpers.AlertService;
//import com.jayurewards.jayu.helpers.AuthService;
//import com.jayurewards.jayu.helpers.GlobalConstants;
//import com.jayurewards.jayu.merchant_app.models.stripe.UpdateSubscriptionStatus;
//import com.jayurewards.jayu.merchant_app.screens_merchant.WebViewActivity;
//import com.jayurewards.jayu.networking.RetrofitClient;
//import com.jayurewards.jayu.merchant_app.screens_merchant.dashboard.DashboardMerchantActivity;
//import com.jayurewards.jayu.merchant_app.helpers.GlobalConstantsMerchant;
//import com.jayurewards.jayu.merchant_app.models.stripe.CheckSubscriptionParams;
//import com.jayurewards.jayu.merchant_app.models.stripe.CheckSubscriptionResponse;
//import com.jayurewards.jayu.merchant_app.models.MerchantModel;
//import com.jayurewards.jayu.services.FCMService;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;

public class LoginMerchantActivity extends AppCompatActivity {
//    private static final String TAG = "Merchant Login Activity";
//
//    private final int RC_SIGN_IN = 111;
//
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private ImageButton backButton;
//    private MaterialButton emailLoginButton;
//    private SignInButton googleLoginButton;
//    private MaterialButton signUpSiteButton;
//    private MaterialButton forgotPasswordButton;
//
//    private RelativeLayout spinner;
//
//    private FirebaseAuth auth;
//    private GoogleSignInClient googleSignInClient;
//
//    // Reference to keyboard manager
//    InputMethodManager imm;
//
//    // Variable to track event time to prevent mis-clicks
//    private long lastClickTime = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_merchant);
//
//        // Setup Firebase Auth and Google Sign In
//        auth = FirebaseAuth.getInstance();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(BuildConfig.googleSigninWebClientId)
//                .requestEmail().build();
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        emailEditText = findViewById(R.id.editTextMerchantLoginEmail);
//        passwordEditText = findViewById(R.id.editTextMerchantLoginPassword);
//        backButton = findViewById(R.id.buttonMerchantLoginBack);
//        emailLoginButton = findViewById(R.id.buttonMerchantLoginEmailSubmit);
//        googleLoginButton = findViewById(R.id.buttonMerchantLoginGoogleSignin);
//        signUpSiteButton = findViewById(R.id.buttonMerchantLoginSignUp);
//        forgotPasswordButton = findViewById(R.id.buttonMerchantLoginForgotPassword);
//        spinner = findViewById(R.id.loadingMerchantLogin);
//
//        emailEditText.addTextChangedListener(textWatcher);
//        passwordEditText.addTextChangedListener(textWatcher);
//
//        emailLoginButton.setBackgroundResource(R.drawable.rounded_button);
//
//        googleLoginButton.setSize(SignInButton.SIZE_WIDE);
//        googleLoginButton.setColorScheme(SignInButton.COLOR_DARK);
//
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        setupClickListeners();
//        enableEmailSubmit(false);
//    }
//
//    /**
//     * Set up click listeners and form submits
//     */
//    private void setupClickListeners() {
//        emailLoginButton.setOnClickListener( view -> {
//            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
//                return;
//            }
//            lastClickTime = SystemClock.elapsedRealtime();
//
//            // Hide keyboard
//            imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
//
//            spinner.setVisibility(View.VISIBLE);
//
//            String email = emailEditText.getText().toString();
//            String password = passwordEditText.getText().toString();
//
//            if (!email.isEmpty()) {
//                if (!isValidEmail(email)) {
//                    emailEditText.setError("Please enter a valid email");
//                    emailEditText.requestFocus();
//                    spinner.setVisibility(View.GONE);
//                    return;
//                }
//            }
//
//
//            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
//                if (task.isSuccessful() && auth.getCurrentUser() != null) {
//                    String firebaseUid = auth.getCurrentUser().getUid();
//                    getMerchant(firebaseUid);
//
//                } else {
//                    Log.e(TAG, "Merchant Email Login Error: " + task.getException().getMessage());
//                    Log.e(TAG, "Merchant Email Login Error Code: " + ((FirebaseAuthException)task.getException()).getErrorCode());
//
//                    spinner.setVisibility(View.GONE);
//
//                    try {
//                        throw task.getException();
//                    } catch(Exception e) {
//                        AlertService.showAlert(this, "Email Login Error",
//                                "This email does not exist, or the password is incorrect. Please check and try again.");
//                    }
//                }
//            });
//        });
//
//        // Open Google Login Popup
//        googleLoginButton.setOnClickListener( view -> {
//            // Hide keyboard
//            imm.hideSoftInputFromWindow(emailEditText.getWindowToken(), 0);
//
//            Intent signInIntent = googleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        });
//
//        signUpSiteButton.setOnClickListener( view -> {
//            Intent intent = new Intent(this, WebViewActivity.class);
//            intent.putExtra(GlobalConstants.INTENT_ENDING_URL, GlobalConstantsMerchant.PORTAL_URL_SIGNUP);
//            startActivity(intent);
//        });
//
//        forgotPasswordButton.setOnClickListener(view -> {
//            Intent intent = new Intent(this, WebViewActivity.class);
//            intent.putExtra(GlobalConstants.INTENT_ENDING_URL, GlobalConstantsMerchant.PORTAL_URL_FORGOT_PASSWORD);
//            startActivity(intent);
//        });
//
//        backButton.setOnClickListener( view -> {
//            finish();
//        });
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                spinner.setVisibility(View.VISIBLE);
//                if (account != null) {
//                    firebaseAuthWithGoogle(account);
//                }
//
//            } catch (ApiException e) {
//                Log.e(TAG, "Merchant Google Login in failed: " + e);
//
//                switch (e.getStatusCode()) {
//                    case 12501:
//                        Log.w(TAG, "User canceled the Google Signin");
//                        break;
//
//                    case 12502:
//                        AlertService.showAlert(this, "Please Wait", "Google is currently loggin you in.");
//                        break;
//
//                    case 12500:
//                        AlertService.showAlert(this, "Unable To Login",
//                                "Google was not able to log you in. Please check your account and try again.");
//                        break;
//
//                    case 5:
//                        AlertService.showAlert(this, "Invalid Account",
//                                "Please check your account for accuracy, and try again.");
//                        break;
//
//                    default:
//                        AlertService.showNetworkIssueAlert(this);
//                        break;
//                }
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        auth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
//            if (task.isSuccessful()) {
//                String firebaseUid = auth.getCurrentUser().getUid();
//                getMerchant(firebaseUid);
//
//            } else {
//                Log.e(TAG, "Merchant Google Login Firebase Auth Error: ", task.getException());
//                spinner.setVisibility(View.GONE);
//                AlertService.showNetworkIssueAlert(this);
//            }
//        });
//    }
//
//    /**
//     * Get merchant data, and then subscription status
//     * @param firebaseUid Merchant firebase Uid
//     */
//    private void getMerchant(String firebaseUid) {
//        Call<MerchantModel> call = RetrofitClient.getInstance().getRestMerchant().getMerchant(firebaseUid);
//        call.enqueue(new Callback<MerchantModel>() {
//            @Override
//            public void onResponse(@NonNull Call<MerchantModel> call, @NonNull Response<MerchantModel> response) {
//                MerchantModel merchant = response.body();
//
//                if (merchant != null && merchant.getStripeId() != null) {
//                    CheckSubscriptionParams params = new CheckSubscriptionParams(merchant.getStripeId(), merchant.getSubscriptionId());
//                    Call<CheckSubscriptionResponse> callStatus = RetrofitClient.getInstance().getRestMerchant().checkSubscription(params);
//                    callStatus.enqueue(new Callback<CheckSubscriptionResponse>() {
//                        @SuppressLint("ApplySharedPref")
//                        @Override
//                        public void onResponse(@NonNull Call<CheckSubscriptionResponse> call, @NonNull Response<CheckSubscriptionResponse> response) {
//                            CheckSubscriptionResponse status = response.body();
//
//                            if (status != null && (status.getStatus().equals(GlobalConstantsMerchant.ACTIVE_STRIPE)
//                                    || status.getStatus().equals(GlobalConstantsMerchant.PAST_DUE_STRIPE)
//                                    || status.getStatus().equals(GlobalConstantsMerchant.TRIAL_STRIPE))) {
//
//                                // If user is currently logged in the consumer app, remove all consumer data
//                                // Merchant must login into consumer app again to access consumer features
//                                // Necessary to make Firebase Messaging (chat) work with user's (consumer) Firebase credentials
//                                SharedPreferences sharedPref = LoginMerchantActivity.this.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedPref.edit();
//                                editor.clear().commit();
//
//                                String subscriptionId;
//                                if (status.getSubscriptionId() != null) {
//                                    subscriptionId = status.getSubscriptionId();
//                                } else {
//                                    subscriptionId = merchant.getSubscriptionId();
//                                }
//
//                                String name = "";
//                                if (merchant.getFirstName() != null && merchant.getLastName() != null) {
//                                    name = merchant.getFirstName() + " " + merchant.getLastName();
//                                }
//
//                                editor.putInt(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_ID, merchant.getMerchantId());
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_FIREBASE_UID, merchant.getFirebaseUID());
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_NAME, name);
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_EMAIL, merchant.getEmail());
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_STRIPE_ID, merchant.getStripeId());
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_SUBSCRIPTION_ID, subscriptionId);
//                                editor.putString(GlobalConstantsMerchant.SHARED_PREF_MERCHANT_STRIPE_STATUS, status.getStatus());
//
//                                editor.putBoolean(GlobalConstantsMerchant.SHARED_PREF_IS_MERCHANT_ACTIVE, true);
//                                editor.apply();
//
//                                FCMService.updateMerchantFcmToken(merchant.getFirebaseUID(), getApplicationContext());
//
//                                spinner.setVisibility(View.GONE);
//
//                                Intent intent = new Intent(LoginMerchantActivity.this, DashboardMerchantActivity.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//
//                            } else {
//                                Log.w(TAG, "Merchant login - Inactive subscription: " + status);
//
//                                String subscriptionStatus = "inactive";
//                                if (status != null && status.getStatus() != null) {
//                                    subscriptionStatus = status.getStatus();
//                                }
//
//                                UpdateSubscriptionStatus params = new UpdateSubscriptionStatus(merchant.getStripeId(), subscriptionStatus);
//                                Call<String> callUpdate = RetrofitClient.getInstance().getRestMerchant().updateSubscriptionStatus(params);
//                                callUpdate.enqueue(new Callback<String>() {
//                                    @Override
//                                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                                        // Successfully updated
//                                    }
//
//                                    @Override
//                                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                                        Log.e(TAG, "Update subscription status error: " + t.getLocalizedMessage());
//                                    }
//                                });
//
//                                AuthService.firebaseSilentLogout(LoginMerchantActivity.this);
//                                spinner.setVisibility(View.GONE);
//                                Intent intent = new Intent(LoginMerchantActivity.this, InactiveMerchantActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(@NonNull Call<CheckSubscriptionResponse> call, @NonNull Throwable t) {
//                            Log.e(TAG, "Merchant Login - Check subscription error: " + t.getMessage());
//                            spinner.setVisibility(View.GONE);
//                            AuthService.firebaseSilentLogout(LoginMerchantActivity.this);
//                            AlertService.showNetworkIssueAlert(LoginMerchantActivity.this);
//                        }
//                    });
//
//
//                } else if (merchant != null && merchant.getMessage() != null) {
//                    Log.w(TAG, "Merchant Login Merchant doesn't exist: " + merchant.getMessage());
//                    AuthService.firebaseSilentLogout(LoginMerchantActivity.this);
//                    spinner.setVisibility(View.GONE);
//                    Intent intent = new Intent(LoginMerchantActivity.this, InactiveMerchantActivity.class);
//                    startActivity(intent);
//
//                } else {
//                    Log.w(TAG, "Merchant Login Merchant or Stripe ID doesn't exist.");
//                    AuthService.firebaseSilentLogout(LoginMerchantActivity.this);
//                    spinner.setVisibility(View.GONE);
//                    Intent intent = new Intent(LoginMerchantActivity.this, InactiveMerchantActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<MerchantModel> call, @NonNull Throwable t) {
//                Log.e(TAG, "Merchant Login Get Merchant network ERROR:\n" + t.getMessage());
//                spinner.setVisibility(View.GONE);
//                AlertService.showNetworkIssueAlert(LoginMerchantActivity.this);
//            }
//        });
//    }
//
//
//    /**
//     * EDIT TEXT METHODS
//     */
//    private final TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            checkForEmptyField();
//        }
//    };
//
//    private void checkForEmptyField(){
//        String email = emailEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
//        enableEmailSubmit(email.length() >= 1 && password.length() >= 1);
//    }
//
//    private void enableEmailSubmit(Boolean enabled) {
//        if (!enabled) {
//            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
//            emailLoginButton.setEnabled(false);
//
//        } else {
//            emailLoginButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
//            emailLoginButton.setEnabled(true);
//        }
//    }
//
//    private boolean isValidEmail(CharSequence target) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
//    }


}
