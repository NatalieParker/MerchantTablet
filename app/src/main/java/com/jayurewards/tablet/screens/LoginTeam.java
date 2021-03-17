package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.hbb20.CountryCodePicker;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;

public class LoginTeam extends AppCompatActivity {

    private EditText phoneNumberInput;
    private Button buttonBack;
    private Button buttonSend;
    private CountryCodePicker ccp;
    private Boolean isPhoneValid = false;
    private long lastClickTime = 0;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_team);

        phoneNumberInput = findViewById(R.id.editTextEnterPhone);
        buttonBack = findViewById(R.id.buttonCancel);
        buttonBack.setPaintFlags(buttonBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        buttonSend = findViewById(R.id.buttonSend);
        ccp = findViewById(R.id.ccpEnterPhone);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setUpClickListeners();
    }

    private void setUpClickListeners () {
        buttonBack.setOnClickListener(new View.OnClickListener() {
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
                preventDuplicateClicks();

            if (!ccp.isValidFullNumber()) {
                String title = "Invalid Phone Number";
                String message = "Please check your phone number and make sure it is correct.";
                AlertHelper.showAlert(LoginTeam.this, title, message);
                return;
            }

            String countryCode = ccp.getSelectedCountryCode();
            String phoneFormatted = ccp.getFormattedFullNumber(); // Get formatted number with country code from ccp
            String phone = phoneFormatted.replaceAll("[^0-9]", "");
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginTeam.this);
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
            }
        });
//        merchantLoginButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LoginMerchantActivity.class);
//            startActivity(intent);
//        });
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
//        PhoneVerifyFragment fragment = PhoneVerifyFragment.newInstance(phoneNumber, countryCode, phoneFormatted);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.setCustomAnimations(R.anim.fragment_enter_right, R.anim.fragment_exit_right, R.anim.fragment_enter_right, R.anim.fragment_exit_right);

        // Back button returns to this activity
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.add(R.id.frameVerifyPhoneFragment, fragment, "Verify_Fragment").commit();
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
//
        } else {
            buttonSend.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            buttonSend.setEnabled(true);
        }
    }

    // Discontinued
//    private void countryCodeChangePhoneEditTextFormatting() {
//        if (!1.equals(editTextCountryCode.getText().toString())) {
//            editTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
//            editTextPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
//        } else {
//            editTextPhone.setFilters(new InputFilter[] { new InputFilter.LengthFilter(14) });
//            editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();

        // Hide keyboard
        imm.hideSoftInputFromWindow(phoneNumberInput.getWindowToken(), 0);
    }
}