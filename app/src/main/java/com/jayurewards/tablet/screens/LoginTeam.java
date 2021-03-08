package com.jayurewards.tablet.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jayurewards.tablet.R;

public class LoginTeam extends AppCompatActivity {

    EditText phoneNumberInput;
    Button buttonBack;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_team);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        buttonBack = findViewById(R.id.buttonCancel);
        buttonBack.setPaintFlags(buttonBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        buttonSend = findViewById(R.id.buttonSend);

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
            }
        });
    }


    /**
     * Set click listeners for activity
     */
//    private void setClickListeners() {
//        submitButton.setOnClickListener(v -> {
//            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
//                return;
//            }
//            mLastClickTime = SystemClock.elapsedRealtime();
//
//            if (!ccp.isValidFullNumber()) {
//                String title = "Invalid Phone Number";
//                String message = "Please check your phone number and make sure it is correct.";
//                AlertService.showAlert(PhoneLoginActivity.this, title, message);
//                return;
//            }
//
//            String countryCode = ccp.getSelectedCountryCode();
//
////            String phoneFormatted = ccp.getFormattedFullNumber(); // Get formatted number with country code from ccp
//            String phoneFormatted = editTextPhone.getText().toString();
//            String phone = phoneFormatted.replaceAll("[^0-9]", "");
//            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneLoginActivity.this);
//            builder.setTitle("Phone Number").setMessage("Send the code to " + phoneFormatted + "?");
//            builder.setPositiveButton("Yes", (dialog, id) -> {
//                if (!countryCode.isEmpty() && !phone.isEmpty()) {
//                    openFragment(phone, countryCode, phoneFormatted);
//                }
//            });
//
//            builder.setNegativeButton("Cancel", (dialog, id) -> {
//                // User cancelled the dialog
//            });
//
//            builder.show();
//        });
//
//        merchantLoginButton.setOnClickListener(v -> {
//            Intent intent = new Intent(this, LoginMerchantActivity.class);
//            startActivity(intent);
//        });
//
//        backButton.setOnClickListener(view -> finish());
//    }


    /**
     * Navigate to fragment
     *
     * @param phoneNumber Entered phone number
     * @param countryCode Entered country code
     */
    private void openFragment(String phoneNumber, String countryCode, String phoneFormatted) {

        // Disable submit button or the user can click through the fragment
//        enablePostSubmit(false);
//
//        editTextPhone.getText().clear();
//        editTextPhone.requestFocus();

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

//            enablePostSubmit(isPhoneValid);
        }
    };

    private void enablePostSubmit(Boolean enabled) {
//        if (!enabled) {
//            submitButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
//            submitButton.setEnabled(false);
//
//        } else {
//            submitButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
//            submitButton.setEnabled(true);
//        }
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
//        imm.hideSoftInputFromWindow(editTextPhone.getWindowToken(), 0);
    }
}