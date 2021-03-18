package com.jayurewards.tablet.screens;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.models.UserModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginTeamVerifyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginTeamVerifyFragment extends Fragment {
    private static final String TAG = "LoginTeamVerifyFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String COUNTRY_CODE = "countryCode";
    private static final String PHONE_FORMATTED = "phoneFormatted";

    // TODO: Rename and change types of parameters
    private String phoneNumber;
    private String countryCode;
    private String phoneFormatted;

    private FirebaseAuth auth;

    private MaterialButton buttonCancel;
    private MaterialButton buttonSubmit;
    private TextView textViewUserPhoneNumber;
    private EditText editTextVerificationInput;
//    private EditText codeField_1;
//    private EditText codeField_2;
//    private EditText codeField_3;
//    private EditText codeField_4;
//    private EditText codeField_5;
//    private EditText codeField_6;

    // Properties
//    private RelativeLayout spinner;
//    private final ImageService imageService = new ImageService();

    // Reference to keyboard manager
    private InputMethodManager imm;

    private String verificationId;

    // Variable to track event time to prevent mis-clicks
    private long lastClickTime = 0;

    public LoginTeamVerifyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
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
//        spinner = view.findViewById(R.id.loadingVerifyPhone);

        buttonCancel = view.findViewById(R.id.buttonVerifyFragmentCancel);
        buttonSubmit = view.findViewById(R.id.buttonVerifyFragmentSubmit);
        textViewUserPhoneNumber = view.findViewById(R.id.textViewVerifyFragmentUserPhoneNumber);
        editTextVerificationInput = view.findViewById(R.id.editTextVerifyFragmentVerificationInput);
//        codeField_1 = view.findViewById(R.id.editTextVerifyCode1);
//        codeField_2 = view.findViewById(R.id.editTextVerifyCode2);
//        codeField_3 = view.findViewById(R.id.editTextVerifyCode3);
//        codeField_4 = view.findViewById(R.id.editTextVerifyCode4);
//        codeField_5 = view.findViewById(R.id.editTextVerifyCode5);
//        codeField_6 = view.findViewById(R.id.editTextVerifyCode6);

//        codeField_1.requestFocus();

        setUpTextListeners();

        // Format phone number and send to Firebase Auth
        if (phoneNumber != null) {
            String phoneFirebase = "+" + countryCode + phoneNumber;
            sendVerificationCode(phoneFirebase);
        }

        editTextVerificationInput.setText(phoneFormatted);

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
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,      // Phone number to verify
                    60,              // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    getActivity(),       // Activity (for callback binding)
                    mCallbacks
            );
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
            for(int id : ids){
                EditText t = getView().findViewById(id);
                code.append(t.getText().toString());
            }

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
//        spinner.setVisibility(View.VISIBLE);

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
                    Call<UserModel> call = RetrofitClient.getInstance().getRest().getUser(firebaseUid);
                    call.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            if (!response.isSuccessful()) {
                                String errorMessage = "Check if user exists REST Error: ";
                                LogService.serverError(TAG, errorMessage, response.code(), response.message());
                                AlertHelper.showNetworkAlert(getActivity());
                                return;
                            }

                            UserModel user = response.body();

                            // User already exists
                            if (getActivity() != null && user != null && user.getFirebaseUID() != null) {
                                SharedPreferences sharedPref = getActivity().getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putInt(GlobalConstants.SHARED_PREF_USER_ID, user.getUserId());
                                editor.putString(GlobalConstants.SHARED_PREF_FIREBASE_UID, user.getFirebaseUID());
                                editor.putString(GlobalConstants.SHARED_PREF_NAME, user.getName());
                                editor.putString(GlobalConstants.SHARED_PREF_COUNTRY_CODE, user.getCountryCode());
                                editor.putString(GlobalConstants.SHARED_PREF_PHONE, user.getPhone());
                                editor.putString(GlobalConstants.SHARED_PREF_BIRTHDATE, user.getBirthdate());

//                                editor.putBoolean(GlobalConstantsMerchant.SHARED_PREF_IS_MERCHANT_ACTIVE, false);
                                editor.apply();

                                FCMService.updateUserFcmToken(user.getUserId(), user.getFirebaseUID());
//                                AuthHelper.mapUserIdToMerchant(getActivity());

                                // Save user profile photo to local device - Download image as bitmap
                                if (getActivity() != null && user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                                    GlideApp.with(getActivity())
                                            .asBitmap()
                                            .load(user.getPhotoUrl())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    imageService.saveProfileImageInternalStorage(getActivity(), resource);

                                                    editor.putString(GlobalConstants.SHARED_PREF_PHOTO_URL, user.getPhotoUrl());
                                                    editor.putString(GlobalConstants.SHARED_PREF_THUMBNAIL_URL, user.getThumbnailUrl());
                                                    editor.apply();

//                                                    spinner.setVisibility(View.GONE);
                                                    hideKeyboard();
                                                    navigateToNearby();
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) { }
                                            });
                                } else {
                                    navigateToNearby();
                                }

                                // User doesn't exist in database.
                            } else {
//                                spinner.setVisibility(View.GONE);
                                hideKeyboard();

                                if (getActivity() != null) {
                                    Intent intent = new Intent(getActivity(), OnboardIntroActivity.class);

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
                            LogService.errorReport(TAG, errorMessage, t, LogService.ErrorReportType.NETWORK);
                            spinner.setVisibility(View.GONE);
                            AlertService.showNetworkIssueAlert(getActivity());
                        }
                    });
                }
            } else {
                String errorMessage = "Phone verification Sign in Error";
                if (task.getException() != null) {
                    errorMessage = errorMessage + "\n" + task.getException().getMessage();
                    LogService.errorReport(TAG, errorMessage, task.getException(), LogService.ErrorReportType.NETWORK);
                }

                // The verification code entered was invalid
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    spinner.setVisibility(View.GONE);
                    AlertService.showAlert(getActivity(),"Validation Error", "Please try again and check the verification code sent in a text message.");

                } else {
                    spinner.setVisibility(View.GONE);
                    AlertService.showNetworkIssueAlert(getActivity());
                }
            }
        });
    }

}