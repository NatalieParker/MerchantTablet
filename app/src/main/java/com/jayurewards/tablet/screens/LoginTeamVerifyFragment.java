package com.jayurewards.tablet.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.FCMHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.ImageHelper;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.UserModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginTeamVerifyFragment extends Fragment {
    private static final String TAG = "LoginTeamVerifyFragment";

    // Passed data
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String PHONE_FORMATTED = "phoneFormatted";

    private String phoneNumber;
    private String countryCode;
    private String phoneFormatted;

    private FirebaseAuth auth;

    private MaterialButton buttonCancel;
    private MaterialButton buttonSubmit;
    private TextView textViewUserPhoneNumber;
    private EditText editTextVerificationInput;

    private ConstraintLayout spinner;
    private final ImageHelper imageHelper = new ImageHelper();

    private InputMethodManager imm;

    private String verificationId;

    private long lastClickTime = 0;

    public LoginTeamVerifyFragment() {
        // Required empty public constructor
    }

    public static LoginTeamVerifyFragment newInstance(String phoneNumber, String countryCode, String phoneFormatted) {
        LoginTeamVerifyFragment fragment = new LoginTeamVerifyFragment();
        Bundle args = new Bundle();
        args.putString(PHONE_NUMBER, phoneNumber);
        args.putString(COUNTRY_CODE, countryCode);
        args.putString(PHONE_FORMATTED, phoneFormatted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            phoneNumber = getArguments().getString(PHONE_NUMBER);
            countryCode = getArguments().getString(COUNTRY_CODE);
            phoneFormatted = getArguments().getString(PHONE_FORMATTED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_team_verify, container, false);

        auth = FirebaseAuth.getInstance();

        buttonCancel = view.findViewById(R.id.buttonVerifyFragmentCancel);
        buttonSubmit = view.findViewById(R.id.buttonVerifyFragmentSubmit);
        textViewUserPhoneNumber = view.findViewById(R.id.textViewVerifyFragmentUserPhoneNumber);
        editTextVerificationInput = view.findViewById(R.id.editTextVerifyFragmentVerificationInput);
        spinner = view.findViewById(R.id.spinnerLoginTeamVerifyFragment);

        textViewUserPhoneNumber.setText(phoneFormatted);
        editTextVerificationInput.addTextChangedListener(textWatcher);
        editTextVerificationInput.requestFocus();
//        spinner.setVisibility(View.VISIBLE);

        setUpTextListeners();

        // Format phone number and send to Firebase Auth
//        sendVerificationCode(phoneNumber);

        // Initialize keyboard and open keyboard through window manager
        if (getContext() != null) {
            imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        enablePostSubmit(false);
        setUpClickListeners();


        // Inflate the layout for this fragment
        Log.i(TAG, "COUNTRY CODE: " + countryCode);
        Log.i(TAG, "PHONE NUMBER: " + phoneNumber);
        Log.i(TAG, "PHONE FORMATTED: " + phoneFormatted);

        return view;

    }

    @Override
    public void onDestroy() {
        hideKeyboard();
        super.onDestroy();
    }

    private void sendVerificationCode(String phone) {
        if (getActivity() != null) {
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(getActivity())                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();

            PhoneAuthProvider.verifyPhoneNumber(options);
        }
    }

    private void setUpClickListeners() {
        buttonCancel.setOnClickListener(v -> {
            if (getFragmentManager() == null) return;
            getFragmentManager().popBackStackImmediate();
        });

        buttonSubmit.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            if (getView() == null) return;

            StringBuilder code = new StringBuilder();
//                int[] ids = new int[]{ R.id.editTextVerifyCode1,R.id.editTextVerifyCode2,R.id.editTextVerifyCode3,R.id.editTextVerifyCode4,R.id.editTextVerifyCode5,R.id.editTextVerifyCode6 };
//            for(int id : ids){
//                EditText t = getView().findViewById(id);
//                code.append(t.getText().toString());
//            }

            // In case the code is invalid (protected by disabled button)
            if ((code.length() == 0) || code.length() != 6) {
//                    codeField_1.setError("Please enter a valid code.");
//                    codeField_1.requestFocus();
                return;
            }

            verifyCode(code.toString());
        });
    }

    private void verifyCode(String code) {
        spinner.setVisibility(View.VISIBLE);

        if (verificationId != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = task.getResult().getUser();

                if (user != null) {
                    String firebaseUid = user.getUid();

                    // Retrieve user's info from Database
                    Call<UserModel> call = RetrofitClient.getInstance().getRestUser().getUser(firebaseUid);
                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            if (!response.isSuccessful()) {
                                String errorMessage = "Check if user exists REST Error: ";
                                LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                                AlertHelper.showNetworkAlert(getActivity());
                                return;
                            }

                            UserModel user = response.body();

                            // User already exists
                            if (getActivity() != null && user != null && user.getFirebaseUID() != null) {
                                SharedPreferences sharedPref = getActivity().getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putInt(GlobalConstants.USER_ID, user.getUserId());
                                editor.putString(GlobalConstants.USER_FIREBASE_UID, user.getFirebaseUID());
                                editor.putString(GlobalConstants.NAME, user.getName());
                                editor.putString(GlobalConstants.COUNTRY_CODE, user.getCountryCode());
                                editor.putString(GlobalConstants.PHONE, user.getPhone());
                                editor.putString(GlobalConstants.BIRTHDATE, user.getBirthdate());

//                                editor.putBoolean(GlobalConstantsMerchant.SHARED_PREF_IS_MERCHANT_ACTIVE, false);
                                editor.apply();

                                FCMHelper.updateUserFcmToken(user.getUserId(), user.getFirebaseUID());
//                                AuthHelper.mapUserIdToMerchant(getActivity());

                                // Save user profile photo to local device - Download image as bitmap
                                if (getActivity() != null && user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                                    GlideApp.with(getActivity())
                                            .asBitmap()
                                            .load(user.getPhotoUrl())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    imageHelper.saveProfileImageInternalStorage(getActivity(), resource);

                                                    editor.putString(GlobalConstants.PHOTO_URL, user.getPhotoUrl());
                                                    editor.putString(GlobalConstants.THUMBNAIL_URL, user.getThumbnailUrl());
                                                    editor.apply();

                                                    spinner.setVisibility(View.GONE);
                                                    hideKeyboard();
                                                    navigateToUserKeypad();
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });
                                } else {
                                    navigateToUserKeypad();
                                }

                                // User doesn't exist in database.
                            } else {
                                spinner.setVisibility(View.GONE);
                                hideKeyboard();

                                if (getActivity() != null) {
                                    Intent intent = new Intent(getActivity(), RegistrationTeam.class);

                                    if (countryCode != null && phoneNumber != null) {
                                        intent.putExtra(GlobalConstants.COUNTRY_CODE, countryCode);
                                        intent.putExtra(GlobalConstants.PHONE, phoneNumber);
                                        intent.putExtra(GlobalConstants.USER_FIREBASE_UID, firebaseUid);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                            String errorMessage = "Check if user exists network ERROR:\n" + t.getMessage();
                            LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                            spinner.setVisibility(View.GONE);
                            AlertHelper.showNetworkAlert(getActivity());
                        }
                    });
                }
            } else {
                String errorMessage = "Phone verification Sign in Error";
                if (task.getException() != null) {
                    errorMessage = errorMessage + "\n" + task.getException().getMessage();
                    LogHelper.errorReport(TAG, errorMessage, task.getException(), LogHelper.ErrorReportType.NETWORK);
                }

                // The verification code entered was invalid
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    spinner.setVisibility(View.GONE);
                    AlertHelper.showAlert(getActivity(), "Validation Error", "Please try again and check the verification code sent in a text message.");

                } else {
                    spinner.setVisibility(View.GONE);
                    AlertHelper.showNetworkAlert(getActivity());
                }
            }
        });
    }

    private void navigateToUserKeypad() {
        Intent intent = new Intent(getActivity(), UserKeypadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Handle the sending and receiving of the verification code
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // Called if the verification happens automatically
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential.getSignInMethod());

            // Flurry log instant verifications
            Map<String, String> params = new HashMap<>();
            if (credential.getSmsCode() != null) {
                params.put("Phone Code", credential.getSmsCode());
            } else {
                params.put("Phone Code", "None");
            }
//            FlurryAgent.logEvent("Instant Phone Verification", params);

            signInWithCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e(TAG, "Verification Error: " + e.getMessage());

            // Invalid request
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.d(TAG, "Invalid credentials: " + e.getLocalizedMessage());

                spinner.setVisibility(View.GONE);
                AlertHelper.showAlert(getActivity(), "Validation Error", "Please double check your phone number or the verification code if you received a text message.");

                // SMS quota exceeded
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.d(TAG, "SMS Quota exceeded.");

                spinner.setVisibility(View.GONE);
                AlertHelper.showAlert(getActivity(), "SMS Quota exceeded", "Too many attempts made with this phone number! Please try again later.");
            }
        }

        // Called when the verification Id is received from Firebase
        @Override
        public void onCodeSent(@NonNull String verificationIDIn, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(verificationIDIn, forceResendingToken);
            verificationId = verificationIDIn;
        }
    };

    /**
     * Edit Text methods
     */
    private void setUpTextListeners() {
//        codeField_1.addTextChangedListener(textWatcher);
//        codeField_2.addTextChangedListener(textWatcher);
//        codeField_3.addTextChangedListener(textWatcher);
//        codeField_4.addTextChangedListener(textWatcher);
//        codeField_5.addTextChangedListener(textWatcher);
//        codeField_6.addTextChangedListener(textWatcher);
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            // Identify the code field. After a change if it's populated move to the next, else move back one.
//            if (codeField_1.getText().hashCode() == s.hashCode()) {
//                if (!codeField_1.getText().toString().isEmpty()) {
//                    codeField_2.requestFocus();
//                }
//            }
//
//            if (codeField_2.getText().hashCode() == s.hashCode()) {
//                if (!codeField_2.getText().toString().isEmpty()) {
//                    codeField_3.requestFocus();
//                } else {
//                    codeField_1.requestFocus();
//                }
//            }
//
//            if (codeField_3.getText().hashCode() == s.hashCode()) {
//                if (!codeField_3.getText().toString().isEmpty()) {
//                    codeField_4.requestFocus();
//                } else {
//                    codeField_2.requestFocus();
//                }
//            }
//
//            if (codeField_4.getText().hashCode() == s.hashCode()) {
//                if (!codeField_4.getText().toString().isEmpty()) {
//                    codeField_5.requestFocus();
//                } else {
//                    codeField_3.requestFocus();
//                }
//            }
//
//            if (codeField_5.getText().hashCode() == s.hashCode()) {
//                if (!codeField_5.getText().toString().isEmpty()) {
//                    codeField_6.requestFocus();
//                } else {
//                    codeField_4.requestFocus();
//                }
//            }
//
//            if (codeField_6.getText().hashCode() == s.hashCode()) {
//                if (codeField_6.getText().toString().isEmpty()) {
//                    codeField_5.requestFocus();
//                }
//            }

            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
//        boolean codeFieldsFilled =
//                !codeField_1.getText().toString().isEmpty()
//                        && !codeField_2.getText().toString().isEmpty()
//                        && !codeField_3.getText().toString().isEmpty()
//                        && !codeField_4.getText().toString().isEmpty()
//                        && !codeField_5.getText().toString().isEmpty()
//                        && !codeField_6.getText().toString().isEmpty();
//        enablePostSubmit(codeFieldsFilled);
        boolean codeFieldsFilled = editTextVerificationInput.getText().length() == 6;
        Log.i(TAG, "checkForEmptyField: \n EDIT TEXT FIELD: " + editTextVerificationInput.getText().toString());
        enablePostSubmit(codeFieldsFilled);
    }

    private void enablePostSubmit(Boolean enabled) {
        if (getActivity() != null) {
            if (!enabled) {
                buttonSubmit.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
                buttonSubmit.setEnabled(false);

            } else {
                buttonSubmit.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
                buttonSubmit.setEnabled(true);
            }
        }
    }

    private void hideKeyboard() {
//        imm.hideSoftInputFromWindow(codeField_6.getWindowToken(), 0);
    }

}