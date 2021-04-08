package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.BaseInputConnection;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.hbb20.CountryCodePicker;
import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.DateTimeHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.Points.GivePointsRequest;
import com.jayurewards.tablet.models.Points.GivePointsResponse;
import com.jayurewards.tablet.models.ShopAdminModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
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

    private MaterialButton key1;
    private MaterialButton key2;
    private MaterialButton key3;
    private MaterialButton key4;
    private MaterialButton key5;
    private MaterialButton key6;
    private MaterialButton key7;
    private MaterialButton key8;
    private MaterialButton key9;
    private MaterialButton key0;
    private MaterialButton deleteButton;
    private MaterialButton enterButton;
    private MaterialButton signOutButton;
    private MaterialButton goToTeamLoginButton;
    private MaterialButton buttonOptionsMenu;
    private MaterialButton buttonLockScreen;
    private MaterialButton buttonUpdatePoints;
    private MaterialButton buttonPointScreenBack;
    private LinearLayout optionsMenuContainer;
    private TextView optionsCompanyName;
    private ConstraintLayout spinner;
    private ConstraintLayout constraintLayoutDarkenScreen;
    private ConstraintLayout constraintLayoutBackgroundAnimation;
    private ConstraintLayout constraintLayoutPointSuccessScreen;
    private ConstraintLayout constraintLayoutKeys;
    private EditText phoneNumber;
    private SharedPreferences preferences;
    private CountryCodePicker ccp;
    private ImageView profilePicture;
    private TextView ptsResponseName;
    private TextView ptsResponseHeader;
    private TextView ptsResponseDesc;
    private TextView ptsResponseMoreInfo;

//    private EditText countryCode;
//    private EditText pointAmount;
//    private TextView header;
//    private TextView company;
//    private ConstraintLayout resultsContainer;
//    private ImageView userImage;
//    private TextView givePointsResult;
//    private MaterialButton dismissButton;

    private ArrayList<ShopAdminModel> shopList = new ArrayList<>();
    private ShopAdminModel shop;
    private String pointMethod;

    private boolean isPhoneValid = false;
    private boolean givePointsSuccess = false;
    private String usaCountryCode = "1";

    private long lastClickTime = 0;

    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_keypad);

        phoneNumber = findViewById(R.id.editTextUserKeypadInput);
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
        buttonPointScreenBack = findViewById(R.id.buttonUserKeypadBackButton);
        buttonLockScreen = findViewById(R.id.buttonUserKeypadLockScreen);
        buttonOptionsMenu = findViewById(R.id.buttonUserKeypadOptionsMenu);
        buttonUpdatePoints = findViewById(R.id.buttonUserKeypadUpdatePoints);
        optionsMenuContainer = findViewById(R.id.linearLayoutUserKeypadOptionsMenu);
        optionsCompanyName = findViewById(R.id.textUserKeypadOptionsHeader);
        spinner = findViewById(R.id.spinnerUserKeypad);
        constraintLayoutDarkenScreen = findViewById(R.id.constraintLayoutUserKeypadDarkenScreen);
        constraintLayoutBackgroundAnimation = findViewById(R.id.layoutUserKeypadLeftContainer);
        constraintLayoutPointSuccessScreen = findViewById(R.id.constraintLayoutUserKeypadPointSuccessScreen);
        constraintLayoutKeys = findViewById(R.id.layoutUserKeypadRightContainer);
        ccp = findViewById(R.id.ccpUserKeypadPhoneNumber);
        profilePicture = findViewById(R.id.imageViewUserKeypadProfilePicture);
        BlurView blurView = findViewById(R.id.layoutUserKeypadContainer);
        ptsResponseName = findViewById(R.id.textUserKeypadPtsResponseName);
        ptsResponseHeader = findViewById(R.id.textUserKeypadPtResponseHeader);
        ptsResponseDesc = findViewById(R.id.textUserKeypadPtsResponseDesc);
        ptsResponseMoreInfo = findViewById(R.id.textUserKeypadPtsResponseMoreInfo);

//        header = view.findViewById(R.id.textGivePointsTitle);
//        countryCode = view.findViewById(R.id.editTextGivePointsCountryCode);
//        pointAmount = view.findViewById(R.id.editTextGivePointsAmount);
//        company = view.findViewById(R.id.textGivePointsCompany);
//        resultsContainer = view.findViewById(R.id.layoutGivePointsResult);
//        userImage = view.findViewById(R.id.imageGivePoints);
//        givePointsResult = view.findViewById(R.id.textGivePointsResult);
//        dismissButton = view.findViewById(R.id.buttonGivePointsDismiss);

        preferences = PreferenceManager.getDefaultSharedPreferences(UserKeypadActivity.this);

//        animationDrawable = (AnimationDrawable) constraintLayoutBackgroundAnimation.getBackground();
//        animationDrawable.setEnterFadeDuration(2000);
//        animationDrawable.setExitFadeDuration(4000);
//        animationDrawable.start();

        spinner.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        optionsMenuContainer.setVisibility(View.GONE);
        constraintLayoutPointSuccessScreen.setVisibility(View.GONE);

        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        signOutButton.setEnabled(false);
        goToTeamLoginButton.setEnabled(false);
        buttonPointScreenBack.setEnabled(false);
        constraintLayoutDarkenScreen.setEnabled(false);

        ccp.registerCarrierNumberEditText(phoneNumber);
        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> isPhoneValid = isValidNumber);

        phoneNumber.addTextChangedListener(textWatcher);
//        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        phoneNumber.setEnabled(false);
//        phoneNumber.setClickable(false);
        enablePostSubmit(false);
        deleteButton.setEnabled(false);
        setUpClickListeners();
        getMerchantShops();

        // Blur keypad background
        float radius = 5f;
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

    /**
     * Network calls
     */
    private void getMerchantShops() {
        int merchantId = preferences.getInt(GlobalConstants.MERCHANT_ID, 0);

        Call<ArrayList<ShopAdminModel>> call = RetrofitClient.getInstance().getRestShops().getMerchantShops(merchantId);
        call.enqueue(new Callback<ArrayList<ShopAdminModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ShopAdminModel>> call, @NonNull Response<ArrayList<ShopAdminModel>> response) {
                shopList = response.body();
                Log.i(TAG, "MERCHANT SHOPS CALL: " + shopList);

                if (shopList != null && shopList.size() >= 1) {
                    shop = shopList.get(0);

                    optionsCompanyName.setText(shop.getCompany());

                } else {
                    String errorMessage = "Get Merchant shops Server Error";
                    LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                    AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                }

                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<ShopAdminModel>> call, @NonNull Throwable t) {
                String errorMessage = "Get Merchant shops Error";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                spinner.setVisibility(View.GONE);
                AlertHelper.showNetworkAlert(UserKeypadActivity.this);
            }
        });
    }


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
            phoneNumber.setEnabled(true);
            phoneNumber.requestFocus();

            // Stimulate delete key pressed
            BaseInputConnection textFieldInputConnection = new BaseInputConnection(phoneNumber, true);
            textFieldInputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));

            new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                        phoneNumber.clearFocus();
                        phoneNumber.setEnabled(false);
                    }, 10);


        });

        enterButton.setOnClickListener(v -> {
            Log.i(TAG, " \n SHOP DATA 2323: " + shop);
            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            Log.i(TAG, "setUpClickListeners: \n SHOP DATA: " + shop);

            if (shop == null) return;

            spinner.setVisibility(View.VISIBLE);
            enablePostSubmit(false);

            int teamId = 0;
            int adminLevel = 1;

            String company = shop.getCompany();
            int storeId = shop.getStoreId();

            String cc = shop.getCountryCode();
            String phoneFormatted = phoneNumber.getText().toString();
            String phone = phoneFormatted.replaceAll("[^0-9]", "");

            int points = shop.getStandardPoints();
            int timeout = shop.getStandardPtTimeout() != 0 ? shop.getStandardPtTimeout() : 14400;

            String method = GlobalConstants.MERCHANT_TABLET_KEYPAD;
            String type = GlobalConstants.POINT_TYPE_GENERAL;

            String day = DateTimeHelper.getDayString(new Date());
            String time = DateTimeHelper.getTimeString(new Date());

            GivePointsRequest params = new GivePointsRequest(cc, phone, storeId, company, points, method,
                    type, teamId, adminLevel, timeout, day, time);

            Log.i(TAG, "GIVE POINTS PARAMS: " + params);

            Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestPoints().merchantGivePoints(params);
            call.enqueue(new Callback<GivePointsResponse>() {
                @Override
                public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {

                    Log.i(TAG, "Merchant data received: " + response.body());

                    GivePointsResponse result = response.body();

                    if (result != null) {
                        GlideApp.with(UserKeypadActivity.this)
                                .load(result.getThumbnail())
                                .fallback(R.drawable.default_profile)
                                .override(profilePicture.getWidth(),profilePicture.getHeight())
                                .into(profilePicture);

                        String greeting = "Hello " + result.getName();
                        ptsResponseName.setText(greeting);

                        // Check point timeout
                        if (result.getTimeLeft() != 0) {
                            String timeLeftString = DateTimeHelper.dateDifferenceString(result.getTimeLeft());
                            String message = result.getName() + " must wait " + timeLeftString + " to get more points.";

                            ptsResponseHeader.setText("Too soon");
                            ptsResponseDesc.setText(message);
                            ptsResponseMoreInfo.setVisibility(View.GONE);

                        } else {

                            int pointTally = result.getPointTally();

                            String pointTallyString;
                            if (pointTally == 1) {
                                pointTallyString = "1 point for rewards.";
                            } else {
                                pointTallyString = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointTally) + " points for rewards.";
                            }

                            int pointsGiven = result.getPoints();

                            String points;
                            if (pointsGiven == 1) {
                                points = "1 point";
                            } else {
                                points = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointsGiven) + " points";
                            }

                            String desc = "You just got " + points + " from " + shop.getCompany() + " and now have " + pointTallyString;

//                        TODO: Determine if user is anonymous and retrieve gift card credits
                            int isAnonymous = 0;

                            String moreInfo;
                            if (isAnonymous == 1) {
                                moreInfo = "Download Jayu to start getting free gift cards!";
                            } else {
                                double credits = 5.50;
                                DecimalFormat numFormat = new DecimalFormat("0.00");
                                moreInfo = "You also have " + numFormat.format(credits) + " available for gift cards you can redeem in the Jayu app.";
                            }


                            ptsResponseHeader.setText(points);
                            ptsResponseDesc.setText(desc);

                            ptsResponseMoreInfo.setVisibility(View.VISIBLE);
                            ptsResponseMoreInfo.setText(moreInfo);
                        }

                    } else {
                        String errorMessage = "Give user points null response";
                        LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                        AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                    }

                    openPointSuccessScreen();
                    phoneNumber.getText().clear();
                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(@NonNull Call<GivePointsResponse> call, @NonNull Throwable t) {
                    String errorMessage = "Get merchant data error: ";
                    LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                    AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                    spinner.setVisibility(View.GONE);
                }
            });
        });

        signOutButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(UserKeypadActivity.this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Log out", (dialog, which) -> AuthHelper.logOut(UserKeypadActivity.this));
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        goToTeamLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserKeypadActivity.this, LoginTeamActivity.class);
            startActivity(intent);
        });

        buttonPointScreenBack.setOnClickListener(v -> closePointSuccessScreen());
        buttonUpdatePoints.setOnClickListener(v -> Log.i(TAG, "UPDATE POINTS BUTTON CLICKED"));
        buttonLockScreen.setOnClickListener(v -> Log.i(TAG, "LOCK SCREEN BUTTON CLICKED"));
        buttonOptionsMenu.setOnClickListener(v -> openKeypadOptionsMenu());
        constraintLayoutDarkenScreen.setOnClickListener(v -> closeKeypadOptionsMenu());
    }

    /**
     * Edit Text Functions
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
            enablePostSubmit(isPhoneValid);
        }
    };

    private void checkForEmptyField() {
        String phone = phoneNumber.getText().toString();

        String countryCode = ccp.getSelectedCountryCode();
        String phoneFormatted = ccp.getFormattedFullNumber(); // Get formatted number with country code from ccp

        Log.i(TAG, "\nCOUNTRY CODE: " + countryCode);
        Log.i(TAG, "CCP PHONE: " + phoneFormatted);
        Log.i(TAG, "CCP PHONE 2: " + ccp.getFullNumber());
        Log.i(TAG, "PHONE: " + phone);
        Log.i(TAG, "SUBMIT ENABLED: " + enterButton.isEnabled());


        deleteButton.setEnabled(phone.length() >= 1);

        boolean enableSubmit = phone.length() > 6;
        enablePostSubmit(enableSubmit);
    }

    private void keypadButtonInput(String number) {
        phoneNumber.append(number);
    }

    private void enablePostSubmit(Boolean enabled) {
        if (!enabled) {
            enterButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
            enterButton.setTextColor(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
            enterButton.setEnabled(false);

        } else {
            enterButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            enterButton.setEnabled(true);
        }
    }

    private void openKeypadOptionsMenu() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_option_menu_open);
        optionsMenuContainer.setVisibility(View.VISIBLE);
        optionsMenuContainer.startAnimation(animation);
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
        optionsMenuContainer.startAnimation(animation);
        optionsMenuContainer.setVisibility(View.GONE);
        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        signOutButton.setEnabled(false);
        goToTeamLoginButton.setEnabled(false);

        buttonOptionsMenu.setEnabled(true);
        buttonOptionsMenu.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        constraintLayoutDarkenScreen.setEnabled(false);
    }

    private void openPointSuccessScreen() {
        Animation animationOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_enter);
        constraintLayoutPointSuccessScreen.setVisibility(View.VISIBLE);
        constraintLayoutPointSuccessScreen.startAnimation(animationOpen);

        buttonPointScreenBack.setEnabled(true);
        buttonPointScreenBack.setVisibility(View.VISIBLE);

        constraintLayoutKeys.setEnabled(false);

        new Handler().postDelayed(() -> {
            if (buttonPointScreenBack.isEnabled()) {
                closePointSuccessScreen();
            }
        }, 30000);
    }

    private void closePointSuccessScreen() {
        Animation animationClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_exit);
        constraintLayoutPointSuccessScreen.setVisibility(View.GONE);
        constraintLayoutPointSuccessScreen.startAnimation(animationClose);

        buttonPointScreenBack.setEnabled(false);
        buttonPointScreenBack.setVisibility(View.GONE);

        constraintLayoutKeys.setEnabled(true);

        Log.i(TAG, "ANIMATION CLOSED");
    }

    // Change phone number length and format based on country code. Android's phone number format only works with phone input type
//    private void countryCodeChangePhoneEditTextFormatting() {
//        if (!usaCountryCode.equals(countryCode.getText().toString())) {
//            phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//            phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
//        } else {
//            phoneNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
//            phoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
//        }
//    }

    // Stripping commas, periods, and spaces to support international number formatting
    private String stripNumberFormatting(String number) {
        String numberStripped1 = number.replaceAll(",", "");
        String numberStripped2 = numberStripped1.replaceAll("/.", "");
        return numberStripped2.replaceAll(" ", "");
    }


}