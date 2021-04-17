package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.jayurewards.tablet.models.OffersModel;
import com.jayurewards.tablet.models.Points.GivePointsRequest;
import com.jayurewards.tablet.models.Points.GivePointsResponse;
import com.jayurewards.tablet.models.ShopAdminModel;
import com.jayurewards.tablet.networking.RetrofitClient;
import com.jayurewards.tablet.screens.popups.LockScreenPopup;
import com.jayurewards.tablet.screens.popups.UpdatePointsPopup;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserKeypadActivity extends AppCompatActivity
        implements UpdatePointsPopup.UpdatePtsPopupInterface, LockScreenPopup.LockScreenInterface {
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
    private TextView companyTextView;
    private TextView optionsPortalBtn;
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
    private ImageView qrCode;

    private CountDownTimer timer;

    private ArrayList<ShopAdminModel> shopList = new ArrayList<>();
    private ShopAdminModel shop;
    private int pointAmount;
    private int adminLevel;

    private ArrayList<OffersModel> offers = new ArrayList<>();

    private boolean isPhoneValid = false;

    private long lastClickTime = 0;

    private AnimationDrawable animationDrawable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_keypad);

        hideSystemUI();

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
        companyTextView = findViewById(R.id.textUserKeypadCompany);
        optionsPortalBtn = findViewById(R.id.buttonUserKeypadOptionsPortal);
        spinner = findViewById(R.id.spinnerUserKeypad);
        constraintLayoutDarkenScreen = findViewById(R.id.constraintLayoutUserKeypadDarkenScreen);
        constraintLayoutBackgroundAnimation = findViewById(R.id.layoutUserKeypadLeftContainer);
        constraintLayoutPointSuccessScreen = findViewById(R.id.constraintLayoutUserKeypadPointSuccessScreen);
        constraintLayoutKeys = findViewById(R.id.layoutUserKeypadRightContainer);
        ccp = findViewById(R.id.ccpUserKeypadPhoneNumber);
        profilePicture = findViewById(R.id.imageUserKeypadPtsResponseProfile);
        BlurView blurView = findViewById(R.id.layoutUserKeypadContainer);
        ptsResponseName = findViewById(R.id.textUserKeypadPtsResponseName);
        ptsResponseHeader = findViewById(R.id.textUserKeypadPtResponseHeader);
        ptsResponseDesc = findViewById(R.id.textUserKeypadPtsResponseDesc);
        ptsResponseMoreInfo = findViewById(R.id.textUserKeypadPtsResponseMoreInfo);
        qrCode = findViewById(R.id.imageViewUserKeypadQRCode);

        preferences = PreferenceManager.getDefaultSharedPreferences(UserKeypadActivity.this);
        adminLevel = preferences.getInt("adminLevel", 1);

        spinner.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        optionsMenuContainer.setVisibility(View.GONE);
        constraintLayoutPointSuccessScreen.setVisibility(View.GONE);

        qrCode.setVisibility(View.GONE);
        String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://jayu.me";
        GlideApp.with(UserKeypadActivity.this)
                .load(qrCodeUrl)
                .fallback(R.drawable.qr_code)
                .into(qrCode);

        ptsResponseMoreInfo.setVisibility(View.GONE);

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

        int pin = preferences.getInt(GlobalConstants.PIN_CODE, 0);
        onUpdate(pin != 0);

        startRecyclerView(offers);
    }

    /**
     * Fullscreen mode
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        int uiOptions =
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Resize screen when out of focus (e.g. popup)
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        // Make full screen
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
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
                    companyTextView.setText(shop.getCompany());
                    pointAmount = shop.getStandardPoints();
                } else {
                    String errorMessage = "Get Merchant shops Server Error";
                    LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                    AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                }

                getBusinessOffers(shop.getStoreId());
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

    private void getBusinessOffers(int storeId) {
        Call<ArrayList<OffersModel>> call = RetrofitClient.getInstance().getRestOffers().getBusinessOffers(storeId);
        call.enqueue(new Callback<ArrayList<OffersModel>>() {
            @Override
            public void onResponse(Call<ArrayList<OffersModel>> call, Response<ArrayList<OffersModel>> response) {
                offers = response.body();
                Log.i(TAG, "BUSINESS OFFERS CALL: " + offers);

                List<String> types = Arrays.asList(GlobalConstants.OFFER_TYPES_ARRAY);
                ArrayList<OffersModel> rewards = new ArrayList<>();
                ArrayList<OffersModel> specials = new ArrayList<>();
                OffersModel signUp = new OffersModel();

                for (int i = 0; i < offers.size(); i++) {

//                    Log.i(TAG, "OFFER NUMBER " + i + " \n OFFER DESCRIPTION: " + offers.get(i).getDescription());
                    OffersModel offer = offers.get(i);

                    Log.i(TAG, "OFFER: " + offer.getType().equals(GlobalConstants.OFFER_TYPE_GENERAL));


                    if (offer.getType().equals(GlobalConstants.OFFER_TYPE_GENERAL)) {
                        rewards.add(offer);
                    } else if (offer.getType().equals(GlobalConstants.OFFER_TYPE_SIGNUP)) {
                        signUp = offer;
                    } else {
                        specials.add(offer);
                    }
                }
                rewards.sort((o1, o2) -> Integer.compare(o1.getPtsRequired(), o2.getPtsRequired()));
                specials.sort((o1, o2) -> types.indexOf(o1.getType()) - types.indexOf(o2.getType()));
                Log.i(TAG, "REWARDS: " + rewards);
                Log.i(TAG, "SPECIALS: " + specials);

                ArrayList<OffersModel> of = new ArrayList<>();
                of.add(signUp);
                of.addAll(rewards);
                of.addAll(specials);

                startRecyclerView(of);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<OffersModel>> call, Throwable t) {
                String errorMessage = "Get Business Offers Error";
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
            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            if (shop == null) return;

            spinner.setVisibility(View.VISIBLE);
            enablePostSubmit(false);

            int teamId = 0;

            String company = shop.getCompany();
            int storeId = shop.getStoreId();

            String cc = shop.getCountryCode();
            String phoneFormatted = phoneNumber.getText().toString();
            String phone = phoneFormatted.replaceAll("[^0-9]", "");

            int timeout = shop.getStandardPtTimeout() != 0 ? shop.getStandardPtTimeout() : 14400;

            String method = GlobalConstants.MERCHANT_TABLET_KEYPAD;
            String type = GlobalConstants.POINT_TYPE_GENERAL;

            String day = DateTimeHelper.getDayString(new Date());
            String time = DateTimeHelper.getTimeString(new Date());

            GivePointsRequest params = new GivePointsRequest(cc, phone, storeId, company, pointAmount, method,
                    type, teamId, adminLevel, timeout, day, time);

            Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestPoints().merchantGivePoints(params);
            call.enqueue(new Callback<GivePointsResponse>() {
                @Override
                public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {
                    GivePointsResponse result = response.body();

                    if (result != null) {
                        GlideApp.with(UserKeypadActivity.this)
                                .load(result.getThumbnail())
                                .fallback(R.drawable.ribbon_medal_img)
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

                            String moreInfo;
                            if (result.getIsAnonymous() == 1) {
                                moreInfo = "Download Jayu to start getting free gift cards!";
                                ptsResponseMoreInfo.setText(moreInfo);
                                ptsResponseMoreInfo.setVisibility(View.VISIBLE);
                                qrCode.setVisibility(View.VISIBLE);
                            } else {
                                ptsResponseMoreInfo.setVisibility(View.GONE);
                                qrCode.setVisibility(View.GONE);
                            }

                            ptsResponseHeader.setText(points);
                            ptsResponseDesc.setText(desc);
                        }

                    } else {
                        String errorMessage = "Give user points null response";
                        LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                        AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                    }

                    openPointSuccessScreen();
                    phoneNumber.getText().clear();

                    pointAmount = shop.getStandardPoints();
                    companyTextView.setText(shop.getCompany());

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
            closeKeypadOptionsMenu();
            AlertDialog.Builder builder = new AlertDialog.Builder(UserKeypadActivity.this);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Log out", (dialog, which) -> AuthHelper.logOut(UserKeypadActivity.this));
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        goToTeamLoginButton.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            Intent intent = new Intent(UserKeypadActivity.this, LoginTeamActivity.class);
            startActivity(intent);
        });

        buttonUpdatePoints.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            UpdatePointsPopup popup = new UpdatePointsPopup();
            Bundle args = new Bundle();
            args.putInt(GlobalConstants.POINT_AMOUNT, pointAmount);
            args.putInt(GlobalConstants.ADMIN_LEVEL, adminLevel);
            popup.setArguments(args);
            popup.show(getSupportFragmentManager(), "update_points_popup");
        });

        optionsPortalBtn.setOnClickListener(v -> {
            closeKeypadOptionsMenu();

            Intent openWebsite = new Intent(Intent.ACTION_VIEW);
            try {
                openWebsite.setData(Uri.parse(GlobalConstants.WEB_URL_PORTAL_LOGIN));
                startActivity(openWebsite);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Open website link error: " + e.getMessage());
                AlertHelper.showNetworkAlert(this);
            }
        });

        buttonLockScreen.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            LockScreenPopup popup = new LockScreenPopup();
            popup.show(getSupportFragmentManager(), "lock_screen_popup");
        });

        buttonPointScreenBack.setOnClickListener(v -> {
            closePointSuccessScreen();
            timer.cancel();
        });

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

           // Changing edit text hint size
            if(s.length() == 0) {
                phoneNumber.setTextSize(23);
            } else {
                phoneNumber.setTextSize(40);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkForEmptyField();
            enablePostSubmit(isPhoneValid);
        }
    };

    private void checkForEmptyField() {
        String phone = phoneNumber.getText().toString();
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

    /**
     * Helper functions
     */
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
        companyTextView.setVisibility(View.GONE);
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
        companyTextView.setVisibility(View.VISIBLE);
        constraintLayoutDarkenScreen.setVisibility(View.GONE);
        constraintLayoutDarkenScreen.setEnabled(false);
    }

    private void countdownTimer () {
        timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long l) {
                Log.i(TAG, "ON TICK: 1 SECOND");
            }

            @Override
            public void onFinish() {
                closePointSuccessScreen();
            }
        };
        timer.start();
    }

    private void openPointSuccessScreen() {
        Animation animationOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_enter);
        constraintLayoutPointSuccessScreen.setVisibility(View.VISIBLE);
        constraintLayoutPointSuccessScreen.startAnimation(animationOpen);

        buttonPointScreenBack.setEnabled(true);
        buttonPointScreenBack.setVisibility(View.VISIBLE);

        constraintLayoutKeys.setEnabled(false);

        countdownTimer();

//        new Handler().postDelayed(() -> {
//            if (buttonPointScreenBack.isEnabled()) {
//                closePointSuccessScreen();
//            }
//        }, 30000);
    }

    private void closePointSuccessScreen() {
        Animation animationClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_exit);
        constraintLayoutPointSuccessScreen.startAnimation(animationClose);

        buttonPointScreenBack.setEnabled(false);
        buttonPointScreenBack.setVisibility(View.GONE);

        constraintLayoutKeys.setEnabled(true);
    }


    /**
     * Interfaces
     */
    @Override
    public void onUpdate(int points, int adminLvl) {
        pointAmount = points;
        if (shop.getStandardPoints() != points) {
            String pointsString;
            if (points == 1) {
                pointsString = "1 Pt";
            } else {
                pointsString = points + " Pts";
            }

            String company = shop.getCompany() + " - " + pointsString;
            companyTextView.setText(company);
        } else {
            companyTextView.setText(shop.getCompany());
        }

        adminLevel = adminLvl;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("adminLevel", adminLvl);
        editor.apply();

    }

    @Override
    public void onUpdate(boolean isLocked) {
        Log.i(TAG, "IS LOCKED: " + isLocked);

        if (isLocked) {
            buttonUpdatePoints.setVisibility(View.GONE);
            goToTeamLoginButton.setVisibility(View.GONE);
            optionsPortalBtn.setVisibility(View.GONE);

            buttonLockScreen.setText("Unlock Screen");
            buttonLockScreen.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_unlock));
        } else if (!isLocked) {
            buttonUpdatePoints.setVisibility(View.VISIBLE);
            goToTeamLoginButton.setVisibility(View.VISIBLE);
            optionsPortalBtn.setVisibility(View.VISIBLE);

            buttonLockScreen.setText("Lock Screen");
            buttonLockScreen.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_lock));
        }
    }

    private void startRecyclerView(ArrayList<OffersModel> offersList) {
        RecyclerView rv = findViewById(R.id.recyclerViewUserKeypadCards);
        RA_UserKeypad adapter = new RA_UserKeypad(offersList, this);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);

    }
}