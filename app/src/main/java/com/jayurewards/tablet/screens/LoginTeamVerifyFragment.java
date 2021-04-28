package com.jayurewards.tablet.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.ImageHelper;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.TeamMembers.CheckSMSVerificationModel;
import com.jayurewards.tablet.models.TeamMembers.CountryCodePhoneModel;
import com.jayurewards.tablet.models.TeamMembers.UserModel;
import com.jayurewards.tablet.networking.RetrofitClient;

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

    private MaterialButton buttonCancel;
    private MaterialButton buttonSubmit;
    private TextView textViewUserPhoneNumber;
    private EditText editTextVerificationInput;

    private ConstraintLayout spinner;
    private final ImageHelper imageHelper = new ImageHelper();

    private InputMethodManager imm;

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

        buttonCancel = view.findViewById(R.id.buttonVerifyFragmentCancel);
        buttonSubmit = view.findViewById(R.id.buttonVerifyFragmentSubmit);
        textViewUserPhoneNumber = view.findViewById(R.id.textViewVerifyFragmentUserPhoneNumber);
        editTextVerificationInput = view.findViewById(R.id.editTextVerifyFragmentVerificationInput);
        spinner = view.findViewById(R.id.spinnerLoginTeamVerifyFragment);

        textViewUserPhoneNumber.setText(phoneFormatted);
        editTextVerificationInput.addTextChangedListener(textWatcher);
        editTextVerificationInput.requestFocus();

        setUpTextListeners();

        // Initialize keyboard and open keyboard through window manager
        if (getContext() != null) {
            imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        enablePostSubmit(false);
        setUpClickListeners();

        requestVerification();

        return view;
    }

    @Override
    public void onDestroy() {
        hideKeyboard();
        super.onDestroy();
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

            String code = editTextVerificationInput.getText().toString();
            checkVerification(code);
        });
    }

    /**
     * Network calls
     */
    private void requestVerification() {
        String phone = "+" + countryCode + phoneNumber;
        Call<String> call = RetrofitClient.getInstance().getRestTeam().requestSMSVerification(phone);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i(TAG, "onResponse: \n PHONE RESPONSE: " + response.body());

                if (!response.isSuccessful()) {
                    String errorMessage = "Request SMS verification REST Error: ";
                    LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                    AlertHelper.showNetworkAlert(getActivity());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String errorMessage = "Request SMS Verification Error:" + t.getMessage();
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                spinner.setVisibility(View.GONE);
                AlertHelper.showNetworkAlert(getActivity());
            }
        });
    }

    private void checkVerification(String code) {
        String phone = "+" + countryCode + phoneNumber;
        CheckSMSVerificationModel params = new CheckSMSVerificationModel(phone, code);
        Call<String> call = RetrofitClient.getInstance().getRestTeam().checkSMSVerification(params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.i(TAG, "onResponse: \n PHONE RESPONSE CHECK: " + response.body());

                if (!response.isSuccessful()) {
                    String errorMessage = "Check SMS verification REST Error: ";
                    LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                    AlertHelper.showNetworkAlert(getActivity());
                    return;
                }

                if (GlobalConstants.SMS_VERIFY_APPROVED.equals(response.body())) {
                    getTeamMember();
                } else {
                    spinner.setVisibility(View.GONE);
                    AlertHelper.showAlert(getActivity(), "Validation Error",
                            "Please try again and check the verification code sent in a text message.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String errorMessage = "Check SMS Verification Error:" + t.getMessage();
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                spinner.setVisibility(View.GONE);
                AlertHelper.showNetworkAlert(getActivity());
            }
        });
    }

    private void getTeamMember() {
        CountryCodePhoneModel params = new CountryCodePhoneModel(countryCode, phoneNumber);

        Log.i(TAG, "USER MODEL PARAMS: " + params);
        Call<UserModel> call = RetrofitClient.getInstance().getRestTeam().getTeamMember(params);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                spinner.setVisibility(View.GONE);
                hideKeyboard();

                Log.i(TAG, "onResponse: \n USER MODEL: " + response.body());

                if (!response.isSuccessful()) {
                    String errorMessage = "Get team member REST Error: ";
                    LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                    AlertHelper.showNetworkAlert(getActivity());
                    return;
                }



                UserModel user = response.body();
                Log.i(TAG, "onResponse: \n USER OBJECT; " + user);

                // User already exists
                if (getActivity() != null && user != null && user.getFirebaseUID() != null) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putInt(GlobalConstants.TEAM_USER_ID, user.getUserId());
                    editor.putString(GlobalConstants.TEAM_USER_FIREBASE_UID, user.getFirebaseUID());
                    editor.putString(GlobalConstants.TEAM_NAME, user.getName());
                    editor.putString(GlobalConstants.TEAM_COUNTRY_CODE, user.getCountryCode());
                    editor.putString(GlobalConstants.TEAM_PHONE, user.getPhone());

                    editor.apply();

                    Log.i(TAG, "USER ID: " + user.getUserId());
                    Log.i(TAG, "USER NAME: " + user.getName());

                    // Save user profile photo to local device - Download image as bitmap
//                    if (getActivity() != null && user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
//                        GlideApp.with(getActivity())
//                                .asBitmap()
//                                .load(user.getPhotoUrl())
//                                .into(new CustomTarget<Bitmap>() {
//                                    @Override
//                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                                        imageHelper.saveProfileImageInternalStorage(getActivity(), resource);
//
//                                        editor.putString(GlobalConstants.PHOTO_URL, user.getPhotoUrl());
//                                        editor.putString(GlobalConstants.THUMBNAIL_URL, user.getThumbnailUrl());
//                                        editor.apply();
//
//                                        spinner.setVisibility(View.GONE);
//                                        hideKeyboard();
//                                        navigateToUserKeypad();
//                                    }
//
//                                    @Override
//                                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                                    }
//                                });
//                    } else {
//                        navigateToUserKeypad();
//                    }

                    navigateToUserKeypad();

                    // User doesn't exist in database.
                } else {
                    if (getActivity() != null) {
                        AlertHelper.showAlert(getContext(), "User does not exist",
                                "Please first create your account through the Jayu app.");

//                        if (getFragmentManager() == null) return;
//                        getFragmentManager().popBackStackImmediate();
//                        Intent intent = new Intent(getActivity(), RegistrationTeamActivity.class);
//
//                        if (countryCode != null && phoneNumber != null) {
//                            intent.putExtra(GlobalConstants.COUNTRY_CODE, countryCode);
//                            intent.putExtra(GlobalConstants.PHONE, phoneNumber);
//                            intent.putExtra(GlobalConstants.USER_FIREBASE_UID, firebaseUid);
//                            startActivity(intent);
//                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                String errorMessage = "Get team member network ERROR:\n" + t.getMessage();
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                spinner.setVisibility(View.GONE);
                AlertHelper.showNetworkAlert(getActivity());
            }
        });
    }


    private void navigateToUserKeypad() {
        Intent intent = new Intent(getActivity(), UserKeypadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    /**
     * Edit Text methods
     */
    private void setUpTextListeners() {
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
            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        boolean codeFieldsFilled = editTextVerificationInput.getText().length() == 6;
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
        imm.hideSoftInputFromWindow(editTextVerificationInput.getWindowToken(), 0);
    }

}