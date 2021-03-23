package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.CheckSubscriptionParams;
import com.jayurewards.tablet.models.CheckSubscriptionResponse;
import com.jayurewards.tablet.models.Points.GivePointsRequest;
import com.jayurewards.tablet.models.Points.GivePointsResponse;
import com.jayurewards.tablet.models.UpdateSubscriptionStatus;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserKeypadActivity extends AppCompatActivity {
    private static final String TAG = "KeypadScreen";
    private static final String TEAM_ID = "team_id";
    private static final String MERCHANT_SHOPS = "merchantShops";
    private static final String PASSED_COMPANY = "passedCompany";
    private static final String SELECTED_STORE_ID = "selectedStoreId";
    private static final String POINT_METHOD = "pointMethod";
    private static final String ADMIN_LEVEL = "adminLevel";

    public interface GivePointsFragmentInterface {
        void onSuccessfulPointsGiven(int storeId, String companyName);
    }

    private GivePointsFragmentInterface listener;

    private Button key1;
    private Button key2;
    private Button key3;
    private Button key4;
    private Button key5;
    private Button key6;
    private Button key7;
    private Button key8;
    private Button key9;
    private Button key0;
    private Button deleteButton;
    private Button enterButton;
    private MaterialButton signOutButton;
    private MaterialButton goToTeamLoginButton;
    private MaterialButton buttonOptionsMenu;
    private MaterialButton buttonLockScreen;
    private MaterialButton buttonUpdatePoints;
    private LinearLayout linearLayoutOptionsMenu;
    private ConstraintLayout spinner;
    private ConstraintLayout constraintLayoutDarkenScreen;
    private TextView phoneNumber;

    private EditText countryCode;
    private EditText pointAmount;
    private TextView header;
    private TextView company;
    private ConstraintLayout resultsContainer;
    private ImageView userImage;
    private TextView givePointsResult;
    private MaterialButton dismissButton;

    // Passed data
    private int teamId;
    private ArrayList<ShopAdminModel> merchantShops = new ArrayList<>();
    private String passedCompany;
    private int selectedStoreId;
    private String pointMethod;
    private int adminLevel;

    private boolean givePointsSuccess = false;
    private String usaCountryCode = "1";

    private long lastClickTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_keypad);

        View view = inflater.inflate(R.layout.fragment_give_points, container, false);

        phoneNumber = findViewById(R.id.textViewUserKeypadInput);
        key1 = findViewById(R.id.buttonUserKeypadKey1);
        key2 = findViewById(R.id.buttonUserKeypadKey2);
        key3 = findViewById(R.id.buttonUserKeypadKey3);
        key4 = findViewById(R.id.buttonUserKeypadKey4);
        key5 = findViewById(R.id.buttonUserKeypadKey5);
        key6 = findViewById(R.id.buttonUserKeypadKey6);
        key7 = findViewById(R.id.buttonUserKeypadKey7);
        key8 = findViewById(R.id.buttonUserKeypadKey8);
        key9 = findViewById(R.id.buttonUserKeypadKey9);
        key0 = findViewById(R.id.buttonUserKeypadKey0);
        deleteButton = findViewById(R.id.deleteButton);
        enterButton = findViewById(R.id.enterButton);
        signOutButton = findViewById(R.id.buttonUserKeypadSignOut);
        goToTeamLoginButton = findViewById(R.id.buttonUserKeypadSwitchToEmployeeAccount);
        buttonLockScreen = findViewById(R.id.buttonUserKeypadLockScreen);
        buttonOptionsMenu = findViewById(R.id.buttonUserKeypadOptionsMenu);
        buttonUpdatePoints = findViewById(R.id.buttonUserKeypadUpdatePoints);
        linearLayoutOptionsMenu = findViewById(R.id.linearLayoutUserKeypadOptionsMenu);
        spinner = findViewById(R.id.spinnerUserKeypad);
        constraintLayoutDarkenScreen = findViewById(R.id.constraintLayoutUserKeypadDarkenScreen);

        header = view.findViewById(R.id.textGivePointsTitle);
        countryCode = view.findViewById(R.id.editTextGivePointsCountryCode);
        pointAmount = view.findViewById(R.id.editTextGivePointsAmount);
        company = view.findViewById(R.id.textGivePointsCompany);
        resultsContainer = view.findViewById(R.id.layoutGivePointsResult);
        userImage = view.findViewById(R.id.imageGivePoints);
        givePointsResult = view.findViewById(R.id.textGivePointsResult);
        dismissButton = view.findViewById(R.id.buttonGivePointsDismiss);

        spinner.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        linearLayoutOptionsMenu.setVisibility(View.GONE);

        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        signOutButton.setEnabled(false);
        goToTeamLoginButton.setEnabled(false);
        constraintLayoutDarkenScreen.setEnabled(false);


        countryCode.addTextChangedListener(textWatcher);
        phoneNumber.addTextChangedListener(textWatcher);
        pointAmount.addTextChangedListener(textWatcher);

//        // Phone number formatting based off user's device Locale (Country)
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        if (merchantShops.size() <= 1) {
            company.setText(passedCompany);
            company.setEnabled(false);
            countryCode.setText(merchantShops.get(0).getCountryCode() != null ? merchantShops.get(0).getCountryCode() : usaCountryCode);

            String point = String.valueOf(merchantShops.get(0).getStandardPoints() != 0 ? merchantShops.get(0).getStandardPoints() : "1");
            pointAmount.setText(point);

            company.setTextColor(Color.GRAY);
            LayerDrawable border = UIDesignService.getBorders(
                    Color.WHITE, // Background color
                    Color.BLACK, // Border color
                    0, // Left border in pixels
                    0, // Top border in pixels
                    0, // Right border in pixels
                    1 // Bottom border in pixels
            );
            company.setBackground(border);

        } else {
            company.setText(passedCompany);

            pointAmount.setText(usaCountryCode);
            for (ShopAdminModel item : merchantShops) {
                if (passedCompany.equals(item.getCompany())) {
                    countryCode.setText(item.getCountryCode() != null ? item.getCountryCode() : usaCountryCode);
                    String point = String.valueOf(item.getStandardPoints() != 0 ? item.getStandardPoints() : "1");
                    pointAmount.setText(point);
                    break;
                }
            }

            LayerDrawable border = UIDesignService.getBorders(
                    Color.WHITE, // Background color
                    Color.BLACK, // Border color
                    0, // Left border in pixels
                    0, // Top border in pixels
                    0, // Right border in pixels
                    1 // Bottom border in pixels
            );
            company.setBackground(border);
        }

        if (adminLevel != 0 && adminLevel <= 2) {
            pointAmount.setEnabled(false);
        }

        enablePostSubmit(false);
        setUpClickListeners();
        enableDeleteButton(false);
        getMerchantSubscription();

        return view;
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Show Keyboard
//        phoneNumber.post(() -> {
//            phoneNumber.requestFocus();
//            InputMethodManager imm = (InputMethodManager)phoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            if (imm != null)
//                imm.showSoftInput(phoneNumber, InputMethodManager.SHOW_IMPLICIT);
//        });
//    }
//
//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        hideKeyboard();
//        super.onDismiss(dialog);
//    }

    /**
     * Set Click Listeners
     */

    private void setUpClickListeners() {
        key1.setOnClickListener(v -> keypadButtonInput("1"));

        key2.setOnClickListener(v -> keypadButtonInput("2"));

        key3.setOnClickListener(v -> keypadButtonInput("3"));

        key4.setOnClickListener(v -> keypadButtonInput("4"));

        key5.setOnClickListener(v -> keypadButtonInput("5"));

        key6.setOnClickListener(v -> keypadButtonInput("6"));

        key7.setOnClickListener(v -> keypadButtonInput("7"));

        key8.setOnClickListener(v -> keypadButtonInput("8"));

        key9.setOnClickListener(v -> keypadButtonInput("9"));

        key0.setOnClickListener(v -> keypadButtonInput("0"));

        deleteButton.setOnClickListener(v -> {
            String str = phoneNumber.getText().toString();
            String strNew = str.substring(0, str.length() - 1);
            phoneNumber.setText(strNew);
        });

        enterButton.setOnClickListener(v -> {
            if (UserKeypadActivity.this != null) {
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                spinner.setVisibility(View.VISIBLE);
                enablePostSubmit(false);
//                hideKeyboard();

                int timeout = 14400; // Initially set default time out
                for (ShopAdminModel item : merchantShops) {
                    if (passedCompany.equals(item.getCompany())) {
                        timeout = item.getStandardPtTimeout();
                        break;
                    }
                }

                String countryCodeInput = countryCode.getText().toString();
                String phoneFormatted = phoneNumber.getText().toString();
                String phone = phoneFormatted.replaceAll("[^0-9]", "");
                String formattedPoints = stripNumberFormatting(pointAmount.getText().toString());
                int amount;
                try {
                    amount = Integer.parseInt(formattedPoints);
                } catch (Throwable t) {
                    String errorMessage = "Give points Int Parse error";
                    LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.PARSING);
                    AlertHelper.showAlert(UserKeypadActivity.this, "com.jayurewards.tablet.models.Points Error", "Please make sure the amount of points are all numbers.");
                    return;
                }

                String shop = company.getText().toString();
                String type = GlobalConstants.POINT_TYPE_GENERAL;

                SharedPreferences sharedPreferences = UserKeypadActivity.this.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                String userPhone = sharedPreferences.getString(GlobalConstants.PHONE, null);

                if (userPhone != null && userPhone.equals(phone)) {
                    AlertHelper.showAlert(UserKeypadActivity.this, "Not Allowed", "You cannot give yourself reward points.");
                    spinner.setVisibility(View.GONE);
                    return;
                }

                String day = DateFormatService.getDayString(new Date());
                String time = DateFormatService.getTimeString(new Date());

                GivePointsRequest params = new GivePointsRequest(countryCodeInput, phone, selectedStoreId, shop, amount, pointMethod, type,
                        teamId, adminLevel, timeout, day, time);

                Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestDashboardMerchant().merchantGivePoints(params);
                call.enqueue(new Callback<GivePointsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {
                        GivePointsResponse result = response.body();
                        if (UserKeypadActivity.this != null) {
                            if (result != null) {
                                if (result.getThumbnail() != null && !"".equals(result.getThumbnail())) {
                                    GlideApp.with(UserKeypadActivity.this)
                                            .load(result.getThumbnail())
                                            .placeholder(R.drawable.placeholder)
                                            .fallback(R.drawable.default_profile)
                                            .into(userImage);
                                } else {
                                    GlideApp.with(UserKeypadActivity.this)
                                            .load(R.drawable.default_profile)
                                            .into(userImage);
                                }

                                if (result.getTimeLeft() != 0) {
                                    String timeLeftString = DateDifferenceService.dateDifferenceString(result.getTimeLeft());
                                    String message = result.getName() + " must wait " + timeLeftString + " to get more points.";

                                    header.setText(getString(R.string.unsuccessful));
                                    header.setTextColor(UserKeypadActivity.this.getColor(R.color.colorDanger));
                                    givePointsResult.setText(message);

                                    spinner.setVisibility(View.GONE);
                                    resultsContainer.setVisibility(View.VISIBLE);

                                    return;
                                }

                                givePointsSuccess = true;
                                header.setText(getString(R.string.success));

                                int pointTally = amount; // Set just in case a null response is received
                                if (result.getPointTally() != 0) {
                                    pointTally = result.getPointTally();
                                }

                                String pointTallyString;
                                if (pointTally == 1) {
                                    pointTallyString = "1 point for rewards.";
                                } else {
                                    pointTallyString = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointTally) + " points for rewards.";
                                }

                                int pointsGiven = amount;
                                if (result.getPoints() != 0) {
                                    pointsGiven = result.getPoints();
                                }

                                String points;
                                String success;
                                if (pointsGiven == 1) {
                                    points = "1";
                                    success = result.getName() + " was given " + points + " point and now has " + pointTallyString;
                                } else {
                                    points = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointsGiven);
                                    success = result.getName() + " was given " + points + " points and now has " + pointTallyString;
                                }

                                givePointsResult.setText(success);

                            } else {
                                GlideApp.with(UserKeypadActivity.this)
                                        .load(R.drawable.default_profile_image)
                                        .into(userImage);

                                header.setText(getString(R.string.no_match));
                                givePointsResult.setText(getString(R.string.give_points_incorrect_phone_result));
                                dismissButton.setText(getString(R.string.dismiss));
                            }
                        }

                        spinner.setVisibility(View.GONE);
                        resultsContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<GivePointsResponse> call, @NonNull Throwable t) {
                        Log.e(TAG, "Merchant manually give com.jayurewards.tablet.models.Points Error: " + t.getMessage());
                        spinner.setVisibility(View.GONE);
                        AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                    }
                });
            }
        });

        company.setOnClickListener(view -> {
            if (UserKeypadActivity.this != null && merchantShops.size() > 1) {
                ArrayList<String> shops = new ArrayList<>();
                ArrayList<Integer> shopStdPts = new ArrayList<>();
                ArrayList<String> shopCountryCodes = new ArrayList<>();

                for (ShopAdminModel shop : merchantShops) {
                    shops.add(shop.getCompany());
                    shopStdPts.add(shop.getStandardPoints());
                    shopCountryCodes.add(shop.getCountryCode() != null ? shop.getCountryCode() : usaCountryCode);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(UserKeypadActivity.this);
                builder.setTitle("Select Company");
                builder.setItems(shops.toArray(new String[0]), (dialog, which) -> {

                    if (shops.get(which).equals(company.getText().toString())) {
                        return;
                    }

                    countryCodeChangePhoneEditTextFormatting();
                    pointAmount.setText(String.valueOf(shopStdPts.get(which)));
                    company.setText(shops.get(which));
                    countryCode.setText(shopCountryCodes.get(which));
                    selectedStoreId = merchantShops.get(which).getStoreId();
                });

                builder.show();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthHelper.logOut(UserKeypadActivity.this);
                Log.i(TAG, "SIGN OUT BUTTON PRESSED");
            }
        });

        goToTeamLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "SENT TO LOGIN TEAM SCREEN");

                Intent intent = new Intent(UserKeypadActivity.this, LoginTeamActivity.class);
                startActivity(intent);
            }
        });

        buttonUpdatePoints.setOnClickListener(v -> Log.i(TAG, "UPDATE POINTS BUTTON CLICKED"));

        buttonLockScreen.setOnClickListener(v -> Log.i(TAG, "LOCK SCREEN BUTTON CLICKED"));

        buttonOptionsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openKeypadOptionsMenu();
                Log.i(TAG, "OPTIONS MENU BUTTON CLICKED");
            }
        });

        constraintLayoutDarkenScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeypadOptionsMenu();
            }
        });

        dismissButton.setOnClickListener(view -> {
            if (givePointsSuccess) {
                if (listener != null) {
                    listener.onSuccessfulPointsGiven(selectedStoreId, company.getText().toString());
                    dismiss();
                }
            } else {
                enablePostSubmit(true);
                resultsContainer.setVisibility(View.GONE);
            }
        });

    }

    /**
     * Edit Text Functions
     */

    private final TextWatcher textWatcher;

    {
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                Reset phone number and check phone number format if country code changed
                if (countryCode.getText().hashCode() == s.hashCode()) {
                    phoneNumber.setText(null);
                    countryCodeChangePhoneEditTextFormatting();
                } else if (pointAmount.getText().hashCode() == s.hashCode()) {
                    pointAmount.removeTextChangedListener(this);
                    String numberString = pointAmount.getText().toString();

                    // Format number based on location (e.g add commas)
                    if (!numberString.equals("")) {
                        String numberStripped = stripNumberFormatting(numberString);

                        double numberDouble;
                        try {
                            numberDouble = Double.parseDouble(numberStripped);
                            String formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(numberDouble);
                            pointAmount.setText(formattedNumber);
                            pointAmount.setSelection(pointAmount.getText().toString().length());

                        } catch (Throwable t) {
                            String message = "Give points text listener parse points error";
                            LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.PARSING);
                        }
                    }

                    pointAmount.addTextChangedListener(this);
                }
                checkForEmptyField();

            }
        };
    }

    private void givePoints() {
        GivePointsRequest params = new GivePointsRequest("1", "7578765083", 1, "company", 2, "merchant_web", "general", 0, 0, 1000, "Tuesday", "13:00");
        Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestPoints().merchantGivePoints(params);
        call.enqueue(new Callback<GivePointsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {

                Log.i(TAG, "Merchant data recieved: " + response.body());

            }

            @Override
            public void onFailure(@NonNull Call<GivePointsResponse> call, @NonNull Throwable t) {
                Log.i(TAG, "Get merchant data error: " + t.getMessage());
                AlertHelper.showNetworkAlert(UserKeypadActivity.this);
            }
        });

    }

    // Change state for submit button based on text entered
    private void checkForEmptyField() {
        String amount = pointAmount.getText().toString();
        String countryCallingCode = countryCode.getText().toString();
        String phone = phoneNumber.getText().toString();

        Log.i(TAG, "PHONE NUMBER: " + phone);
        boolean goEnableButton = phone.length() >= 1;
        enableDeleteButton(goEnableButton);

        if ((pointAmount.isEnabled() && amount.isEmpty()) || phone.isEmpty() || countryCallingCode.isEmpty()) {
            enablePostSubmit(false);

        } else if (usaCountryCode.equals(countryCallingCode) &&  phone.length() <= 13) {
            enablePostSubmit(false);

        } else if (phone.length() < 6) {
            enablePostSubmit(false);

        } else {
            enablePostSubmit(true);
        }
    }

    /**
     * Network calls
     */
    private void getMerchantSubscription() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(UserKeypadActivity.this);
        String stripeId = sharedPref.getString("stripeId", null);
        String subscriptionId = sharedPref.getString("subscriptionId", null);

        Log.i(TAG, "STRIPE ID: " + sharedPref.getString("stripeId", null));
        Log.i(TAG, "SUBSCRIPTION ID: " + sharedPref.getString("subscriptionId", null));

        CheckSubscriptionParams params = new CheckSubscriptionParams(stripeId, subscriptionId);
        Call<CheckSubscriptionResponse> call = RetrofitClient.getInstance().getRestAuth().checkSubscription(params);

        call.enqueue(new Callback<CheckSubscriptionResponse>() {
            @Override
            public void onResponse(@NonNull Call<CheckSubscriptionResponse> call, @NonNull Response<CheckSubscriptionResponse> response) {
                CheckSubscriptionResponse status = response.body();

                if (status != null &&
                        (status.getStatus().equals(GlobalConstants.ACTIVE_STRIPE)
                                || status.getStatus().equals(GlobalConstants.PAST_DUE_STRIPE)
                                || status.getStatus().equals(GlobalConstants.TRIAL_STRIPE))) {

                    Log.i(TAG, "\n\n SUBSCRIPTION STATUS PASSED: " + status);
                    getMerchantShops();

                } else {
                    String subStatus = "inactive";
                    Log.w(TAG, "STATUS = FALSE");

                    logoutMerchant();

                    if (status != null && status.getStatus() != null) {
                        status.setStatus(subStatus);

                        // TODO: Check and verify this network call works

                        UpdateSubscriptionStatus uss = new UpdateSubscriptionStatus(stripeId, subStatus);
                        Call<String> callUpdate = RetrofitClient.getInstance().getRestAuth().updateSubscriptionStatus(uss);
                        callUpdate.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                Log.i(TAG, "UPDATE STATUS RESPONSE: " + response.body());


                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                Log.e(TAG, "Update status error: " + t.getLocalizedMessage());


                            }
                        });
                    }


                    Intent intent = new Intent(UserKeypadActivity.this, InactiveAccountActivity.class);
                    startActivity(intent);


                }


                Log.i(TAG, "STATUS: " + status);
            }

            @Override
            public void onFailure(@NonNull Call<CheckSubscriptionResponse> call, @NonNull Throwable t) {

                Log.e(TAG, "GET MERCHANT ERROR: " + t.getMessage());
                spinner.setVisibility(View.GONE);

                if (t.getMessage() != null && t.getMessage().equals("timeout")) {
                    logoutMerchant();
                }
            }
        });
    }

    private void getMerchantShops() {
        spinner.setVisibility(View.GONE);

    }


    private void logoutMerchant() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(UserKeypadActivity.this);
        AuthHelper.logOut(UserKeypadActivity.this);
        sharedPref.edit().remove(GlobalConstants.MERCHANT_ID).apply();
        sharedPref.edit().remove(GlobalConstants.MERCHANT_FIREBASE_UID).apply();
        sharedPref.edit().clear().apply();
    }

    private void keypadButtonInput(String number) {
        String digit = phoneNumber.getText() + number;
        phoneNumber.setText(digit);
    }

    private void enableDeleteButton(boolean enabled) {
        if (!enabled) {
            deleteButton.setBackgroundTintList(ColorStateList.valueOf(UserKeypadActivity.this.getColor(R.color.colorPrimaryLight)));
            deleteButton.setEnabled(false);
        } else {
            deleteButton.setBackgroundTintList(ColorStateList.valueOf(UserKeypadActivity.this.getColor(R.color.colorPrimary)));
            deleteButton.setEnabled(true);
        }
    }

    private void enablePostSubmit(Boolean enabled) {
        if (UserKeypadActivity.this != null) {
            if (!enabled) {
                enterButton.setBackgroundTintList(ColorStateList.valueOf(UserKeypadActivity.this.getColor(R.color.colorPrimaryLight)));
                enterButton.setEnabled(false);

            } else {
                enterButton.setBackgroundTintList(ColorStateList.valueOf(UserKeypadActivity.this.getColor(R.color.colorPrimary)));
                enterButton.setEnabled(true);
            }
        }
    }

    private void openKeypadOptionsMenu() {

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_option_menu_open);
        linearLayoutOptionsMenu.setVisibility(View.VISIBLE);
        linearLayoutOptionsMenu.startAnimation(animation);
        buttonLockScreen.setEnabled(true);
        buttonUpdatePoints.setEnabled(true);
        signOutButton.setEnabled(true);
        goToTeamLoginButton.setEnabled(true);

        buttonOptionsMenu.setEnabled(false);
        buttonOptionsMenu.setVisibility(View.GONE);
        constraintLayoutDarkenScreen.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setEnabled(true);
    }

    private void closeKeypadOptionsMenu() {

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_option_menu_close);
        linearLayoutOptionsMenu.startAnimation(animation);
        linearLayoutOptionsMenu.setVisibility(View.GONE);
        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        signOutButton.setEnabled(false);
        goToTeamLoginButton.setEnabled(false);

        buttonOptionsMenu.setEnabled(true);
        buttonOptionsMenu.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        constraintLayoutDarkenScreen.setEnabled(false);
    }

    // Change phone number length and format based on country code. Android's phone number format only works with phone input type
    private void countryCodeChangePhoneEditTextFormatting() {
        if (!usaCountryCode.equals(countryCode.getText().toString())) {
            phoneNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
            phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            phoneNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(14) });
            phoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        }
    }

    // Stripping commas, periods, and spaces to support international number formatting
    private String stripNumberFormatting(String number) {
        String numberStripped1 = number.replaceAll(",", "");
        String numberStripped2 = numberStripped1.replaceAll("/.", "");
        return numberStripped2.replaceAll(" ", "");
    }

//    private void hideKeyboard() {
//        InputMethodManager imm =
//                (InputMethodManager)phoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive())
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//        imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof GivePointsFragmentInterface) {
            listener = (GivePointsFragmentInterface) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement GivePointsFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}