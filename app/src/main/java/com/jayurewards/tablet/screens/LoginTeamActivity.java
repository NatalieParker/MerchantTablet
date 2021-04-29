package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;

public class LoginTeamActivity extends AppCompatActivity {
    private static final String TAG = "LoginTmActivity";

    private EditText phoneNumberInput;
    private MaterialButton buttonBack;
    private MaterialButton buttonSend;

    // Passed data
    private int storeId;

    private CountryCodePicker ccp;
    private Boolean isPhoneValid = false;
    private long lastClickTime = 0;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_team);

        phoneNumberInput = findViewById(R.id.editTextLoginTeamEnterPhone);
        buttonBack = findViewById(R.id.buttonLoginTeamCancel);
        buttonSend = findViewById(R.id.buttonLoginTeamSend);
        ccp = findViewById(R.id.ccpLoginTeamEnterPhone);
        ScrollView background = findViewById(R.id.scrollViewLoginTeamBg);

        Intent intent = getIntent();
        storeId = intent.getIntExtra(GlobalConstants.STORE_ID, 0);

        AnimationDrawable animationDrawable = (AnimationDrawable) background.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        phoneNumberInput.addTextChangedListener(textWatcher);
        phoneNumberInput.requestFocus();

        ccp.registerCarrierNumberEditText(phoneNumberInput);
        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> isPhoneValid = isValidNumber);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        buttonBack.setPaintFlags(buttonBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        setUpClickListeners();
    }

    private void setUpClickListeners() {
        buttonBack.setOnClickListener(v -> finish());

        buttonSend.setOnClickListener(v -> {
            preventDuplicateClicks();
            hideKeyboard();

            if (!ccp.isValidFullNumber()) {
                String title = "Invalid Phone Number";
                String message = "Please check your phone number and make sure it is correct.";
                AlertHelper.showAlert(LoginTeamActivity.this, title, message);
                return;
            }

            String countryCode = ccp.getSelectedCountryCode();
            String phone = phoneNumberInput.getText().toString().replaceAll("[^0-9]", "");
            String phoneFormatted = ccp.getFormattedFullNumber(); // Get formatted number with country code from ccp


            AlertDialog.Builder builder = new AlertDialog.Builder(LoginTeamActivity.this);
            builder.setTitle("Phone Number").setMessage("Send the code to " + phoneFormatted + "?");

            builder.setPositiveButton("Yes", (dialog, id) -> {
                if (!countryCode.isEmpty() && !phone.isEmpty()) {
                    openFragment(phone, countryCode, phoneFormatted);
                }
            });

            builder.setNegativeButton("Cancel", (dialog, id) -> {
                // User cancelled the dialog
            });

            builder.show();
        });
    }

    private void preventDuplicateClicks() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
    }

    /**
     * Navigate to fragment
     *
     * @param phoneNumber Entered phone number
     * @param countryCode Entered country code
     */
    private void openFragment(String phoneNumber, String countryCode, String phoneFormatted) {

        // Disable submit button or the user can click through the fragment
        enablePostSubmit(false);

        phoneNumberInput.getText().clear();
        phoneNumberInput.requestFocus();

        // Prep fragment with passed data
        LoginTeamVerifyFragment fragment = LoginTeamVerifyFragment.newInstance(phoneNumber, countryCode, phoneFormatted, storeId);
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.animation_fragment_enter_right, R.anim.animation_fragment_exit_right, R.anim.animation_fragment_enter_right, R.anim.animation_fragment_exit_right);

//         Back button returns to this activity
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frameLoginTeamVerifyPhoneFragment, fragment, "Verify_Fragment").commit();
    }


    // Track user's text input
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            enablePostSubmit(isPhoneValid);
        }
    };

    private void enablePostSubmit(Boolean enabled) {
        if (!enabled) {
            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
            buttonSend.setEnabled(false);

        } else {
            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            buttonSend.setEnabled(true);
        }
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(phoneNumberInput.getWindowToken(), 0);
    }


    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }
}