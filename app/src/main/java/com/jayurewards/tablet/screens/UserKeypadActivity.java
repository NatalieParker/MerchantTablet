package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.BaseInputConnection;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hbb20.CountryCodePicker;
import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.DateTimeHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.helpers.UtilsHelper;
import com.jayurewards.tablet.models.Points.GivePointsRequest;
import com.jayurewards.tablet.models.Points.GivePointsResponse;
import com.jayurewards.tablet.models.ShopAdminModel;
import com.jayurewards.tablet.networking.RetrofitClient;
import com.jayurewards.tablet.screens.popups.EnterAmountPopup;
import com.jayurewards.tablet.screens.popups.LockScreenPopup;
import com.jayurewards.tablet.screens.popups.PointConvertPopup;
import com.jayurewards.tablet.screens.popups.UpdatePointsPopup;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserKeypadActivity extends AppCompatActivity
        implements UpdatePointsPopup.UpdatePtsPopupInterface,
        LockScreenPopup.LockScreenInterface,
        PointConvertPopup.PointConvertInterface,
        EnterAmountPopup.EnterAmountInterface {

    private static final String TAG = "KeypadScreen";

    // Options Menu
    private LinearLayout optionsMenuContainer;
    private TextView optionsPortalBtn;
    private MaterialButton signOutButton;
    private MaterialButton goTeamLoginBtn;
    private MaterialButton goTeamPageBtn;
    private MaterialButton buttonOptionsMenu;
    private MaterialButton buttonLockScreen;
    private MaterialButton buttonUpdatePoints;
    private MaterialButton buttonPointConvert;
    private MaterialButton buttonRefresh;
    private ConstraintLayout optionsMenuBkgDark;

    // Keypad
    private ConstraintLayout containerKeys;
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
    private MaterialButton submitButton;

    // Points response
    private ConstraintLayout containerPointsSuccess;
    private MaterialButton ptsResponseExit;
    private MaterialButton ptsResponseButton;
    private ImageView ptsResponseProfilePic;
    private TextView ptsResponseName;
    private TextView ptsResponsePoints;
    private TextView ptsResponseDesc;
    private TextView ptsResponseDesc2;
    private TextView ptsResponseCompany;
    private TextView ptsResponseMoreInfo;
    private ConstraintLayout ptsResponseAppBottomContainer;
    private ConstraintLayout ptsResponseAnonBottomContainer;
    private ConstraintLayout ptsResponseAnonQRCodeContainer;
    private ConstraintLayout ptsResponseAppQRCodeContainer;
    private ImageView ptsResponseAnonLogo;
    private ImageView ptsResponseAnonQrCode;
    private ImageView ptsResponseAppQrCode;
    private TextView ptsResponseJayuUrl;
    private ConstraintLayout ptsResponsePointIconContainer;
    private TextView ptsResponsePointsText;
    private ImageView ptsResponseLeftConfetti;
    private ImageView ptsResponseRightConfetti;
    private LinearLayout companyNameContainer;
    private LinearLayout teamNameContainer;
    private TextView companyHeader;
    private TextView teamMemberName;
    private EditText phoneNumber;
    private ConstraintLayout spinner;
    private ViewPager2 vp;
    private View ptsResponseTopSpacer;

    // Properties
    private ArrayList<ShopAdminModel> shopList = new ArrayList<>();
    private ShopAdminModel shop;
    private long pointAmount;
    private double spentAmount;
    private int adminLevel;
    private int teamId;

    private CountryCodePicker ccp;
    private SharedPreferences sp;
    private CountDownTimer pointScreenTimer;
    private Timer vpTimer;
    private int timerPosition;
    private boolean screenLocked = false;
    private boolean converterActive;
    private long lastClickTime = 0;

    private Timer refreshTimer;

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
        submitButton = findViewById(R.id.buttonUKSubmit);
        signOutButton = findViewById(R.id.buttonUserKeypadSignOut);
        goTeamLoginBtn = findViewById(R.id.buttonUserKeypadTeamLogin);
        goTeamPageBtn = findViewById(R.id.buttonUserKeypadTeamPage);
        ptsResponseButton = findViewById(R.id.buttonUserKeypadBackButton);
        ptsResponseExit = findViewById(R.id.imageUKPtsResponseExit);
        buttonLockScreen = findViewById(R.id.buttonUserKeypadLockScreen);
        buttonOptionsMenu = findViewById(R.id.buttonUKOptionsMenu);
        buttonUpdatePoints = findViewById(R.id.buttonUserKeypadUpdatePoints);
        buttonRefresh = findViewById(R.id.buttonUserKeypadRefresh);
        buttonPointConvert = findViewById(R.id.buttonUserKeypadPointConverter);
        optionsMenuContainer = findViewById(R.id.linearLayoutUserKeypadOptionsMenu);
        companyNameContainer = findViewById(R.id.layoutUKCompany);
        teamNameContainer = findViewById(R.id.layoutUserKeypadTeamName);
        companyHeader = findViewById(R.id.textUKCompany);
        teamMemberName = findViewById(R.id.textUKTeamMemberName);
        optionsPortalBtn = findViewById(R.id.buttonUserKeypadOptionsPortal);
        spinner = findViewById(R.id.spinnerUserKeypad);
        optionsMenuBkgDark = findViewById(R.id.constraintLayoutUserKeypadDarkenScreen);
        containerPointsSuccess = findViewById(R.id.layoutUKPtsResponse);
        containerKeys = findViewById(R.id.layoutUserKeypadRightContainer);
        ptsResponseProfilePic = findViewById(R.id.imageUKPtsResponseProfile);
        ptsResponseName = findViewById(R.id.textUKPtsResponseName);
        ptsResponseDesc = findViewById(R.id.textUKPtsResponseDesc);
        ptsResponseDesc2 = findViewById(R.id.textUKPtsResponseDesc2);
        ptsResponsePoints = findViewById(R.id.textUKPtsResponsePoints);
        ptsResponseCompany = findViewById(R.id.textUKPtsResponseCompany);
        ptsResponseMoreInfo = findViewById(R.id.textUKPtsResponseMoreInfo);
        ptsResponseAppBottomContainer = findViewById(R.id.layoutUKPtsResponseAppUserContainer);
        ptsResponseAnonBottomContainer = findViewById(R.id.layoutUKPtsResponseAnonBottomContainer);
        ptsResponseAnonQRCodeContainer = findViewById(R.id.layoutUKPtsResponseAnonQRCode);
        ptsResponseAppQRCodeContainer = findViewById(R.id.layoutUKPtsResponseAppQRCode);
        ptsResponseAppQrCode = findViewById(R.id.imageUKPtsResponseAppRCode);
        ptsResponseAnonQrCode = findViewById(R.id.imageUKPtsResponseAnonQRCode);
        ptsResponseAnonLogo = findViewById(R.id.imageUKPtsResponseAnonLogo);
        ptsResponseJayuUrl = findViewById(R.id.textUserKeypadPtsResponseUrl);
        ptsResponsePointIconContainer = findViewById(R.id.layoutUKPtsResponsePointIconContainer);
        ptsResponsePointsText = findViewById(R.id.textUKPtsResponsePointsText);
        ptsResponseLeftConfetti = findViewById(R.id.imagePtsResponseLeftConfetti);
        ptsResponseRightConfetti = findViewById(R.id.imagePtsResponseRightConfetti);
        ptsResponseTopSpacer = findViewById(R.id.viewUKPtsResponseTopSpacer);
        vp = findViewById(R.id.viewPagerUK);

        sp = getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        adminLevel = sp.getInt(GlobalConstants.ADMIN_LEVEL, 1);
        int pin = sp.getInt(GlobalConstants.PIN_CODE, 0);
        onUpdateLockScreen(pin != 0);

        spinner.setVisibility(View.VISIBLE);

        phoneNumber.addTextChangedListener(textWatcher);
        phoneNumber.setEnabled(false);

        ccp = findViewById(R.id.ccpUserKeypadPhoneNumber);
        ccp.registerCarrierNumberEditText(phoneNumber);
        ccp.setPhoneNumberValidityChangeListener(this::enablePostSubmit);

        enablePostSubmit(false);
        deleteButton.setEnabled(false);

        setUpClickListeners();
        getMerchantShops();

        prepareViews();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onResume() {
        super.onResume();
        converterActive = sp.getBoolean(GlobalConstants.PT_CONVERT_ACTIVATED, false);
        if (converterActive) {
            submitButton.setText(R.string.next);
        } else {
            submitButton.setText(R.string.submit);
        }

        checkTeamMember();
        setUpRefreshTimer();
    }

    @Override
    public void onPause() {
        refreshTimer.cancel();
        cancelCountdownTimer();
        super.onPause();
    }

    /**
     * View Manipulation
     */
    private void prepareViews() {
        if (UtilsHelper.isScreenLarge()) {
            companyHeader.setTextSize(25);
            teamMemberName.setTextSize(20);

            submitButton.setTextSize(25);

            ptsResponseTopSpacer.getLayoutParams().height = 40;

            ptsResponseName.setTextSize(35);
            ptsResponseProfilePic.getLayoutParams().height = 100;
            ptsResponseProfilePic.getLayoutParams().width = 100;
            ptsResponseLeftConfetti.getLayoutParams().height = 140;
            ptsResponseRightConfetti.getLayoutParams().height = 140;
            ptsResponsePointIconContainer.getLayoutParams().height = 170;
            ConstraintLayout.LayoutParams pointsLayout = (ConstraintLayout.LayoutParams) ptsResponsePoints.getLayoutParams();
            pointsLayout.setMargins(40, 40, 40, 40);

            ptsResponseAppQRCodeContainer.getLayoutParams().height = 80;
            ptsResponseAppQRCodeContainer.getLayoutParams().width = 80;
            ConstraintLayout.LayoutParams appQRCodeContainerLayout = (ConstraintLayout.LayoutParams) ptsResponseAppQRCodeContainer.getLayoutParams();
            appQRCodeContainerLayout.topMargin = -30;
            ConstraintLayout.LayoutParams appQRCodeLayout = (ConstraintLayout.LayoutParams) ptsResponseAppQrCode.getLayoutParams();
            appQRCodeLayout.setMargins(12, 12, 12, 12);

            ptsResponseMoreInfo.setTextSize(25);
            ptsResponseJayuUrl.setTextSize(14);
            ptsResponseAnonLogo.getLayoutParams().width = 150;
            ptsResponseAnonQRCodeContainer.getLayoutParams().width = 150;
            ConstraintLayout.LayoutParams anonQRCodeLayout = (ConstraintLayout.LayoutParams) ptsResponseAnonQrCode.getLayoutParams();
            anonQRCodeLayout.setMargins(25, 25, 25, 25);
        }

        optionsMenuBkgDark.setVisibility(View.GONE);
        optionsMenuContainer.setVisibility(View.GONE);
        containerPointsSuccess.setVisibility(View.GONE);

        String qrCodeUrl = "https://api.qrserver.com/v1/create-qr-code/?size=250x250&data=https://jayu.me";
        GlideApp.with(UserKeypadActivity.this)
                .load(qrCodeUrl)
                .fallback(R.drawable.qr_code)
                .into(ptsResponseAnonQrCode);

        GlideApp.with(UserKeypadActivity.this)
                .load(qrCodeUrl)
                .fallback(R.drawable.qr_code)
                .into(ptsResponseAppQrCode);

        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        buttonPointConvert.setEnabled(false);
        signOutButton.setEnabled(false);
        ptsResponseButton.setEnabled(false);
        optionsMenuBkgDark.setEnabled(false);

        // Blur keypad background
        float radius = 5f;
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();
        BlurView blurView = findViewById(R.id.layoutUserKeypadContainer);
        blurView.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
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


    private void checkTeamMember() {
        teamId = sp.getInt(GlobalConstants.TEAM_USER_ID, 0);

        if (screenLocked) {
            goTeamLoginBtn.setVisibility(View.GONE);
            goTeamLoginBtn.setEnabled(false);
            goTeamPageBtn.setVisibility(View.GONE);
            goTeamPageBtn.setEnabled(false);
            return;
        }

        if (teamId >= 1) {
            String name = sp.getString(GlobalConstants.TEAM_NAME, null);
            if (name != null) {
                teamMemberName.setText(name);
                teamNameContainer.setVisibility(View.VISIBLE);
                goTeamPageBtn.setVisibility(View.VISIBLE);
                goTeamPageBtn.setEnabled(true);
                goTeamLoginBtn.setVisibility(View.GONE);
                goTeamLoginBtn.setEnabled(false);
            }
        } else {
            teamNameContainer.setVisibility(View.GONE);
            goTeamPageBtn.setVisibility(View.GONE);
            goTeamPageBtn.setEnabled(false);
            goTeamLoginBtn.setVisibility(View.VISIBLE);
            goTeamLoginBtn.setEnabled(true);
        }
    }

    /**
     * Network calls
     */
    private void getMerchantShops() {
        int merchantId = sp.getInt(GlobalConstants.MERCHANT_ID, 0);
        Call<ArrayList<ShopAdminModel>> call = RetrofitClient.getInstance().getRestShops().getMerchantShops(merchantId);
        call.enqueue(new Callback<ArrayList<ShopAdminModel>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<ShopAdminModel>> call, @NonNull Response<ArrayList<ShopAdminModel>> response) {
                shopList = response.body();

                if (shopList != null && shopList.size() >= 1) {
                    shop = shopList.get(0);

                    try {
                        ccp.setCountryForPhoneCode(Integer.parseInt(shop.getDialingCode()));
                    } catch (Throwable t) {
                        String errorMessage = "Convert Country code to int error";
                        LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                        spinner.setVisibility(View.GONE);
                        AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                    }

                    companyHeader.setText(shop.getCompany());
                    pointAmount = shop.getStandardPoints();
                    getTabletFeeds(shop.getStoreId());

                } else {
                    String errorMessage = "Get Merchant shops Server Error";
                    LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                    spinner.setVisibility(View.GONE);
                    AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                }
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

    private void getTabletFeeds(int storeId) {
        Call<String[]> call = RetrofitClient.getInstance().getRestShops().getTabletFeeds(storeId);
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(@NonNull Call<String[]> call, @NonNull Response<String[]> response) {
                String[] imageUrls = response.body();
                startViewPager(shop.getStoreId(), imageUrls);

                int firstImageFrag = 1;
                if (imageUrls != null) {
                    vp.setCurrentItem(firstImageFrag, true);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String[]> call, @NonNull Throwable t) {
                String errorMessage = "Get tablet feeds error";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                spinner.setVisibility(View.GONE);
                AlertHelper.showNetworkAlert(UserKeypadActivity.this);
            }
        });
    }

    private void refreshNetworkCall() {
        String phone = phoneNumber.getText().toString();
        if ("".equals(phone) && buttonOptionsMenu.isEnabled()) {
            runOnUiThread(() -> {
                spinner.setVisibility(View.VISIBLE);
                getMerchantShops();
            });
        }
    }

    private void setUpRefreshTimer() {
        long hoursToMillis = 3600000;
        long hours = 6 * hoursToMillis;

        refreshTimer = new Timer();
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                refreshNetworkCall();
            }
        }, 0, hours);
    }

    /**
     * Set Click Listeners
     */
    @SuppressLint("ApplySharedPref")
    private void setUpClickListeners() {

        //TODO: Too many views in XML file. Create a recycler view in a grid for buttons.
        // Move the options menu and the popup screen into a custom views
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

        submitButton.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1500) {
                return;
            }

            lastClickTime = SystemClock.elapsedRealtime();

            if (converterActive) {
                openConvertAmount();
            } else {
                giveUserPoints();
            }
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

        goTeamLoginBtn.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            Intent intent = new Intent(UserKeypadActivity.this, LoginTeamActivity.class);
            intent.putExtra(GlobalConstants.STORE_ID, shop.getStoreId());
            startActivity(intent);
        });

        goTeamPageBtn.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            String name = sp.getString(GlobalConstants.TEAM_NAME, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(UserKeypadActivity.this);
            builder.setTitle("Logout Team Member?");
            builder.setMessage("Do you want to sign out " + name + "? They will no longer receive credit for customer sign ups or points.");
            builder.setPositiveButton("Log out", (dialog, which) -> {
                SharedPreferences sharedPref = getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                sharedPref.edit().remove(GlobalConstants.TEAM_USER_ID).commit();
                sharedPref.edit().remove(GlobalConstants.TEAM_USER_FIREBASE_UID).commit();
                sharedPref.edit().remove(GlobalConstants.TEAM_NAME).commit();
                sharedPref.edit().remove(GlobalConstants.TEAM_DIALING_CODE).commit();
                sharedPref.edit().remove(GlobalConstants.TEAM_PHONE).commit();

                teamId = 0;
                checkTeamMember();
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        buttonUpdatePoints.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            UpdatePointsPopup popup = new UpdatePointsPopup();
            Bundle args = new Bundle();
            args.putLong(GlobalConstants.POINT_AMOUNT, pointAmount);
            args.putInt(GlobalConstants.ADMIN_LEVEL, adminLevel);
            popup.setArguments(args);
            popup.show(getSupportFragmentManager(), "update_points_popup");
        });

        buttonPointConvert.setOnClickListener(v -> {
            closeKeypadOptionsMenu();

            long points = sp.getLong(GlobalConstants.PT_CONVERT_POINTS, 0);
            long amount = sp.getLong(GlobalConstants.PT_CONVERT_AMOUNT, 0);
            boolean active = sp.getBoolean(GlobalConstants.PT_CONVERT_ACTIVATED, false);

            PointConvertPopup popup = new PointConvertPopup();
            Bundle args = new Bundle();
            args.putString(GlobalConstants.DIALING_CODE, shop.getDialingCode());
            args.putLong(GlobalConstants.PT_CONVERT_POINTS, points);
            args.putLong(GlobalConstants.PT_CONVERT_AMOUNT, amount);
            args.putBoolean(GlobalConstants.PT_CONVERT_ACTIVATED, active);
            popup.setArguments(args);
            popup.show(getSupportFragmentManager(), "point_convert_popup");
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

        buttonRefresh.setOnClickListener(v -> {
            closeKeypadOptionsMenu();
            refreshNetworkCall();
        });

        ptsResponseExit.setOnClickListener(v -> {
            closePointSuccessScreen();
            cancelCountdownTimer();
        });

        ptsResponseButton.setOnClickListener(v -> {
            closePointSuccessScreen();
            cancelCountdownTimer();
        });

        buttonOptionsMenu.setOnClickListener(v -> openKeypadOptionsMenu());
        optionsMenuBkgDark.setOnClickListener(v -> closeKeypadOptionsMenu());
    }

    private void openConvertAmount() {
        String phoneEntered = phoneNumber.getText().toString();
        long points = sp.getLong(GlobalConstants.PT_CONVERT_POINTS, 0);
        long amount = sp.getLong(GlobalConstants.PT_CONVERT_AMOUNT, 0);

        EnterAmountPopup popup = new EnterAmountPopup();
        Bundle args = new Bundle();
        args.putString(GlobalConstants.DIALING_CODE, shop.getDialingCode());
        args.putLong(GlobalConstants.PT_CONVERT_POINTS, points);
        args.putLong(GlobalConstants.PT_CONVERT_AMOUNT, amount);
        args.putString(GlobalConstants.ENTERED_PHONE, phoneEntered);
        popup.setArguments(args);
        popup.show(getSupportFragmentManager(), "enter_amount_popup");
    }

    private void giveUserPoints() {
        if (shop == null) return;

        spinner.setVisibility(View.VISIBLE);
        enablePostSubmit(false);

        String company = shop.getCompany();
        int storeId = shop.getStoreId();

        String cc = shop.getDialingCode();
        String phoneFormatted = phoneNumber.getText().toString();
        String phone = phoneFormatted.replaceAll("[^0-9]", "");

        int timeout = shop.getStandardPtTimeout() != 0 ? shop.getStandardPtTimeout() : 14400;

        String method = GlobalConstants.MERCHANT_TABLET_KEYPAD;
        String type = GlobalConstants.OFFER_TYPE_GENERAL;

        String day = DateTimeHelper.getDayString(new Date());
        String time = DateTimeHelper.getTimeString(new Date());

        GivePointsRequest params = new GivePointsRequest(cc, phone, storeId, company, pointAmount, spentAmount, method,
                type, teamId, adminLevel, timeout, day, time);

        Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestPoints().merchantGivePoints(params);
        call.enqueue(new Callback<GivePointsResponse>() {
            @Override
            public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {
                GivePointsResponse result = response.body();

                if (result != null) {
                    int pointsGiven = result.getPoints();
                    if (pointsGiven == 1) {
                        ptsResponsePointsText.setText(R.string.point);
                    } else {
                        ptsResponsePointsText.setText(R.string.points);
                    }
                    String points = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointsGiven);

                    String desc;
                    if (result.getTimeLeft() != 0) {
                        ptsResponseCompany.setVisibility(View.INVISIBLE);
                        ptsResponseLeftConfetti.setVisibility(View.GONE);
                        ptsResponseRightConfetti.setVisibility(View.GONE);
                        
                        if (result.getThumbnail() == null || "".equals(result.getThumbnail())) {
                            result.setIsAnonymous(1);
                        }

                        String timeLeftString = DateTimeHelper.dateDifferenceString(result.getTimeLeft());
                        desc = result.getName() + " must wait " + timeLeftString + " to get more points.";

                        ptsResponsePoints.setText("0");
                        ptsResponseDesc.setText(desc);
                        ptsResponseDesc2.setText(R.string.too_soon);

                    } else {
                        ptsResponseCompany.setVisibility(View.VISIBLE);
                        ptsResponseLeftConfetti.setVisibility(View.VISIBLE);
                        ptsResponseRightConfetti.setVisibility(View.VISIBLE);

                        int pointTally = result.getPointTally();
                        String pointTallyString;
                        if (pointTally == 1) {
                            pointTallyString = "You have 1 point for rewards.";
                        } else {
                            pointTallyString = "You have " + NumberFormat.getNumberInstance(Locale.getDefault()).format(pointTally) + " points for rewards";
                        }
                        ptsResponseDesc2.setText(pointTallyString);

                        ptsResponsePoints.setText(points);
                        ptsResponseDesc.setText(R.string.from_your_visit_to);
                        ptsResponseCompany.setText(shop.getCompany());
                    }

                    if (result.getIsAnonymous() == 1) {
                        ptsResponseAnonBottomContainer.setVisibility(View.VISIBLE);
                        ptsResponseProfilePic.setVisibility(View.GONE);
                        ptsResponseName.setVisibility(View.GONE);

                    } else {
                        GlideApp.with(UserKeypadActivity.this)
                                .load(result.getThumbnail())
                                .fallback(R.drawable.ribbon_medal_img)
                                .circleCrop()
                                .override(ptsResponseProfilePic.getWidth(), ptsResponseProfilePic.getHeight())
                                .into(ptsResponseProfilePic);

                        ptsResponseAppBottomContainer.setVisibility(View.VISIBLE);
                        ptsResponseProfilePic.setVisibility(View.VISIBLE);
                        ptsResponseName.setVisibility(View.VISIBLE);

                        String name = "Hi " + result.getName() + "!";
                        ptsResponseName.setText(name);
                    }

                } else {
                    String errorMessage = "Give user points null response";
                    LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.NETWORK);
                    AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                }

                openPointSuccessScreen();
                phoneNumber.getText().clear();
                pointAmount = shop.getStandardPoints();
                spentAmount = 0.00;
                companyHeader.setText(shop.getCompany());
                spinner.setVisibility(View.GONE);
                scrollToTop();
            }

            @Override
            public void onFailure(@NonNull Call<GivePointsResponse> call, @NonNull Throwable t) {
                String errorMessage = "Get merchant data error: ";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                AlertHelper.showNetworkAlert(UserKeypadActivity.this);
                spinner.setVisibility(View.GONE);
                scrollToTop();
            }
        });
    }

    private void scrollToTop() {
        Intent intent = new Intent(GlobalConstants.OFFERS_SCROLL_TOP);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
            if (s.length() == 0) {
                if (UtilsHelper.isScreenLarge()) {
                    phoneNumber.setTextSize(30);
                } else {
                    phoneNumber.setTextSize(23);
                }

            } else {
                if (UtilsHelper.isScreenLarge()) {
                    phoneNumber.setTextSize(50);
                } else {
                    phoneNumber.setTextSize(40);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkForEmptyField();
        }
    };

    private void checkForEmptyField() {
        String phone = phoneNumber.getText().toString();
        deleteButton.setEnabled(phone.length() >= 1);
    }

    private void keypadButtonInput(String number) {
        phoneNumber.append(number);
    }

    private void enablePostSubmit(Boolean enabled) {
        if (!enabled) {
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.white)));
            submitButton.setTextColor(ColorStateList.valueOf(getColor(R.color.colorPrimaryLight)));
            submitButton.setEnabled(false);

        } else {
            submitButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
            submitButton.setEnabled(true);
        }
    }

    /**
     * Helper functions
     */
    @SuppressLint("ClickableViewAccessibility")
    private void startViewPager(int storeId, String[] urls) {
        VPA_UserKeypad adapter = new VPA_UserKeypad(this, storeId, urls);
        vp.setAdapter(adapter);
//        vp.setPageTransformer(new MarginPageTransformer(100));

        TabLayout tabLayout = findViewById(R.id.tabLayoutShopActivityImageSlider);
        new TabLayoutMediator(tabLayout, vp, ((tab, position) -> { })).attach();
//        startTimer(vp, strings);

        // Need to get child because Viewpager 2 is a view group
//        vp.getChildAt(0).setOnTouchListener((v, event) -> {
//            stopTimer();
//            Log.i(TAG, "startViewPager: TIMER RESET");
//            timerPosition = vp.getCurrentItem();
//            startTimer(vp, strings);
//            return false;
//        });
    }

    private void openKeypadOptionsMenu() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_option_menu_open);
        optionsMenuContainer.setVisibility(View.VISIBLE);
        optionsMenuContainer.startAnimation(animation);
        buttonLockScreen.setEnabled(true);
        buttonUpdatePoints.setEnabled(true);
        buttonPointConvert.setEnabled(true);
        signOutButton.setEnabled(true);
        checkTeamMember();

        buttonOptionsMenu.setEnabled(false);
        buttonOptionsMenu.setIconTint(ColorStateList.valueOf(getColor(R.color.colorPrimary)));
        companyNameContainer.setVisibility(View.GONE);
        optionsMenuBkgDark.setVisibility(View.VISIBLE);
        optionsMenuBkgDark.setEnabled(true);
    }

    private void closeKeypadOptionsMenu() {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_option_menu_close);
        optionsMenuContainer.startAnimation(animation);
        optionsMenuContainer.setVisibility(View.GONE);
        buttonLockScreen.setEnabled(false);
        buttonUpdatePoints.setEnabled(false);
        buttonPointConvert.setEnabled(false);
        signOutButton.setEnabled(false);
        goTeamLoginBtn.setEnabled(false);
        goTeamPageBtn.setEnabled(false);

        buttonOptionsMenu.setEnabled(true);
        buttonOptionsMenu.setIconTint(ColorStateList.valueOf(getColor(R.color.white)));
        companyNameContainer.setVisibility(View.VISIBLE);
        optionsMenuBkgDark.setVisibility(View.GONE);
        optionsMenuBkgDark.setEnabled(false);
    }

    private void openPointSuccessScreen() {
        Animation animationOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_enter);
        containerPointsSuccess.setVisibility(View.VISIBLE);
        containerPointsSuccess.startAnimation(animationOpen);

        ptsResponseButton.setEnabled(true);
        containerKeys.setEnabled(false);
        countdownTimer();
    }

    private void closePointSuccessScreen() {
        Animation animationClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_user_keypad_point_popup_exit);
        containerPointsSuccess.startAnimation(animationClose);
        ptsResponseAppBottomContainer.setVisibility(View.GONE);
        ptsResponseAnonBottomContainer.setVisibility(View.GONE);

        ptsResponseButton.setEnabled(false);
        containerKeys.setEnabled(true);
    }

    public void hideSpinner() {
        spinner.setVisibility(View.GONE);
    }

    private void countdownTimer() {
        pointScreenTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                closePointSuccessScreen();
            }
        };

        pointScreenTimer.start();
    }

    private void cancelCountdownTimer() {
        if (pointScreenTimer != null) {
            pointScreenTimer.cancel();
        }
    }

    private void startTimer(ViewPager2 vp, String[] imageUrls) {
        if (vpTimer != null) {
            vpTimer.cancel();
        }
        timerPosition = vp.getCurrentItem();
        startSlider(vp, imageUrls);
    }

    private void stopTimer() {
        if (vpTimer != null) {
            vpTimer.cancel();
            vpTimer = null;
        }
    }

    private void startSlider(final ViewPager2 imageViewPager, String[] imageUrls) {
        vpTimer = new Timer();
        vpTimer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                runOnUiThread(() -> {
                    imageViewPager.setCurrentItem(timerPosition);
                    timerPosition = imageViewPager.getCurrentItem() + 1;

                    if (timerPosition == (imageUrls.length + 1)) {
                        timerPosition = 0;
                    }
//                    Log.i(TAG, "run: TIMER POSITION: " + timerPosition);
                });
            }
        }, 0, 2000);
    }


    /**
     * Interfaces
     */
    @Override
    public void onUpdatePoints(int points, int adminLvl) {
        pointAmount = points;
        if (shop.getStandardPoints() != points) {
            String pointsString;
            if (points == 1) {
                pointsString = "1 Pt";
            } else {
                pointsString = points + " Pts";
            }

            String company = shop.getCompany() + " - " + pointsString;
            companyHeader.setText(company);
        } else {
            companyHeader.setText(shop.getCompany());
        }

        adminLevel = adminLvl;
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(GlobalConstants.ADMIN_LEVEL, adminLvl);
        editor.apply();

    }

    @Override
    public void onUpdateLockScreen(boolean isLocked) {
        if (isLocked) {
            screenLocked = true;
            buttonUpdatePoints.setVisibility(View.GONE);
            buttonPointConvert.setVisibility(View.GONE);
            optionsPortalBtn.setVisibility(View.GONE);
            buttonPointConvert.setVisibility(View.GONE);

            buttonLockScreen.setText(R.string.unlock_screen);
            buttonLockScreen.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_unlock));
        } else {
            screenLocked = false;
            buttonUpdatePoints.setVisibility(View.VISIBLE);
            buttonPointConvert.setVisibility(View.VISIBLE);
            optionsPortalBtn.setVisibility(View.VISIBLE);
            buttonPointConvert.setVisibility(View.VISIBLE);

            buttonLockScreen.setText(R.string.lock_screen);
            buttonLockScreen.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_lock));
        }
    }

    @Override
    public void onPointConvertSubmit(boolean activated) {
        converterActive = activated;
        if (converterActive) {
            submitButton.setText(R.string.next);
        } else {
            submitButton.setText(R.string.submit);
        }
    }

    @Override
    public void onEnterAmountSubmit(long points, double spent) {
        spentAmount = spent;
        pointAmount = points;
        giveUserPoints();
    }
}