package com.jayurewards.tablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.models.GivePointsRequest;
import com.jayurewards.tablet.models.GivePointsResponse;
import com.jayurewards.tablet.models.MerchantModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserKeypadActivity extends AppCompatActivity {
    private static final String TAG = "KeypadScreen";
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
    private TextView keypadInput;


//    private static final String TAG = "GivePointsFrag";
//    private static final String TEAM_ID = "team_id";
//    private static final String MERCHANT_SHOPS = "merchantShops";
//    private static final String PASSED_COMPANY = "passedCompany";
//    private static final String SELECTED_STORE_ID = "selectedStoreId";
//    private static final String POINT_METHOD = "pointMethod";
//    private static final String ADMIN_LEVEL = "adminLevel";
//
//    public interface GivePointsFragmentInterface {
//        void onSuccessfulPointsGiven(int storeId, String companyName);
//    }
//
//    private GivePointsFragmentInterface listener;
//
//    private EditText countryCode;
//    private EditText phoneNumber;
//    private EditText pointAmount;
//    private TextView header;
//    private TextView company;
//    private MaterialButton submitButton;
//    private ConstraintLayout resultsContainer;
//    private ImageView userImage;
//    private TextView givePointsResult;
//    private MaterialButton dismissButton;
//    private ConstraintLayout spinner;
//
//    // Passed data
//    private int teamId;
//    private ArrayList<ShopAdminModel> merchantShops = new ArrayList<>();
//    private String passedCompany;
//    private int selectedStoreId;
//    private String pointMethod;
//    private int adminLevel;
//
//    private boolean givePointsSuccess = false;
//    private String usaCountryCode = "1";
//
//    private long lastClickTime = 0;
//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_keypad);
        keypadInput = findViewById(R.id.keypadInput);
        key1 = findViewById(R.id.key1);
        key2 = findViewById(R.id.key2);
        key3 = findViewById(R.id.key3);
        key4 = findViewById(R.id.key4);
        key5 = findViewById(R.id.key5);
        key6 = findViewById(R.id.key6);
        key7 = findViewById(R.id.key7);
        key8 = findViewById(R.id.key8);
        key9 = findViewById(R.id.key9);
        key0 = findViewById(R.id.key0);
        deleteButton = findViewById(R.id.deleteButton);
        enterButton = findViewById(R.id.enterButton);
        keypadInput.addTextChangedListener(textWatcher);
        setUpClickListeners();
        enableDeleteButton(false);


        //        View view = inflater.inflate(R.layout.fragment_give_points, container, false);
//
//        header = view.findViewById(R.id.textGivePointsTitle);
//        countryCode = view.findViewById(R.id.editTextGivePointsCountryCode);
//        phoneNumber = view.findViewById(R.id.editTextGivePointsPhone);
//        pointAmount = view.findViewById(R.id.editTextGivePointsAmount);
//        company = view.findViewById(R.id.textGivePointsCompany);
//        submitButton = view.findViewById(R.id.buttonGivePointsSubmit);
//        resultsContainer = view.findViewById(R.id.layoutGivePointsResult);
//        userImage = view.findViewById(R.id.imageGivePoints);
//        givePointsResult = view.findViewById(R.id.textGivePointsResult);
//        dismissButton = view.findViewById(R.id.buttonGivePointsDismiss);
//        spinner = view.findViewById(R.id.layoutGivePointsSpinner);
//
//        countryCode.addTextChangedListener(textWatcher);
//        phoneNumber.addTextChangedListener(textWatcher);
//        pointAmount.addTextChangedListener(textWatcher);
//
//        // Phone number formatting based off user's device Locale (Country)
//        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
//
//        if (getActivity() != null) {
//            InputMethodManager imm = (InputMethodManager) phoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        }
//
//        if (merchantShops.size() <= 1) {
//            company.setText(passedCompany);
//            company.setEnabled(false);
//            countryCode.setText(merchantShops.get(0).getCountryCode() != null ? merchantShops.get(0).getCountryCode() : usaCountryCode);
//
//            String point = String.valueOf(merchantShops.get(0).getStandardPoints() != 0 ? merchantShops.get(0).getStandardPoints() : "1");
//            pointAmount.setText(point);
//
//            company.setTextColor(Color.GRAY);
//            LayerDrawable border = UIDesignService.getBorders(
//                    Color.WHITE, // Background color
//                    Color.BLACK, // Border color
//                    0, // Left border in pixels
//                    0, // Top border in pixels
//                    0, // Right border in pixels
//                    1 // Bottom border in pixels
//            );
//            company.setBackground(border);
//
//        } else {
//            company.setText(passedCompany);
//
//            pointAmount.setText(usaCountryCode);
//            for (ShopAdminModel item: merchantShops) {
//                if (passedCompany.equals(item.getCompany())) {
//                    countryCode.setText(item.getCountryCode() != null ? item.getCountryCode() : usaCountryCode);
//                    String point = String.valueOf(item.getStandardPoints() != 0 ? item.getStandardPoints() : "1");
//                    pointAmount.setText(point);
//                    break;
//                }
//            }
//
//            LayerDrawable border = UIDesignService.getBorders(
//                    Color.WHITE, // Background color
//                    Color.BLACK, // Border color
//                    0, // Left border in pixels
//                    0, // Top border in pixels
//                    0, // Right border in pixels
//                    1 // Bottom border in pixels
//            );
//            company.setBackground(border);
//        }
//
//        if (adminLevel != 0 && adminLevel <= 2) {
//            pointAmount.setEnabled(false);
//        }
//
//        submitButton.setBackgroundResource(R.drawable.rounded_button);
//        enablePostSubmit(false);
//        setClickListeners();
//
//        return view;
    }

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
            String str = keypadInput.getText().toString();
            String strNew = str.substring(0, str.length() - 1);
            keypadInput.setText(strNew);
        });

        enterButton.setOnClickListener(v -> givePoints());
    }

    private void keypadButtonInput(String number) {
        keypadInput.setText(keypadInput.getText() + number);
    }

    private void enableDeleteButton(boolean enabled) {
        if (!enabled) {
            deleteButton.setEnabled(false);
        } else {
            deleteButton.setEnabled(true);
        }
    }

    private void checkForEmptyField() {
        String keypad = keypadInput.getText().toString();

        Log.i(TAG, "PHONE NUMBER: " + keypad);
        boolean goEnableButton = keypad.length() >= 1;
        enableDeleteButton(goEnableButton);
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
//
//    /**
//     * Set Click Listeners
//     */
//    private void setClickListeners() {
//        submitButton.setOnClickListener(v -> {
//            if (getActivity() != null) {
//                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
//                    return;
//                }
//                lastClickTime = SystemClock.elapsedRealtime();
//
//                spinner.setVisibility(View.VISIBLE);
//                enablePostSubmit(false);
//                hideKeyboard();
//
//                int timeout = 14400; // Initially set default time out
//                for (ShopAdminModel item: merchantShops) {
//                    if (passedCompany.equals(item.getCompany())) {
//                        timeout = item.getStandardPtTimeout();
//                        break;
//                    }
//                }
//
//                String countryCodeInput = countryCode.getText().toString();
//                String phoneFormatted = phoneNumber.getText().toString();
//                String phone = phoneFormatted.replaceAll("[^0-9]", "");
//                String formattedPoints = stripNumberFormatting(pointAmount.getText().toString());
//                int amount;
//                try {
//                    amount = Integer.parseInt(formattedPoints);
//                } catch (Throwable t) {
//                    String errorMessage = "Give points Int Parse error";
//                    LogService.errorReport(TAG, errorMessage, t, LogService.ErrorReportType.PARSING);
//                    AlertService.showAlert(getActivity(), "com.jayurewards.tablet.models.Points Error", "Please make sure the amount of points are all numbers.");
//                    return;
//                }
//
//                String shop = company.getText().toString();
//                String type = GlobalConstants.POINT_TYPE_GENERAL;
//
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
//                String userPhone = sharedPreferences.getString(GlobalConstants.SHARED_PREF_PHONE, null);
//
//                if (userPhone != null && userPhone.equals(phone)) {
//                    AlertService.showAlert(getActivity(), "Not Allowed", "You cannot give yourself reward points.");
//                    spinner.setVisibility(View.GONE);
//                    return;
//                }
//
//                String day = DateFormatService.getDayString(new Date());
//                String time = DateFormatService.getTimeString(new Date());
//
//                GivePointsRequest params = new GivePointsRequest(countryCodeInput, phone, selectedStoreId, shop, amount, pointMethod, type,
//                        teamId, adminLevel, timeout, day, time);
//
//                Call<GivePointsResponse> call = RetrofitClient.getInstance().getRestDashboardMerchant().merchantGivePoints(params);
//                call.enqueue(new Callback<GivePointsResponse>() {
//                    @Override
//                    public void onResponse(@NonNull Call<GivePointsResponse> call, @NonNull Response<GivePointsResponse> response) {
//                        GivePointsResponse result = response.body();
//                        if (getActivity() != null) {
//                            if (result != null) {
//                                if (result.getThumbnail() != null && !"".equals(result.getThumbnail())) {
//                                    GlideApp.with(getActivity())
//                                            .load(result.getThumbnail())
//                                            .placeholder(R.drawable.placeholder)
//                                            .fallback(R.drawable.default_profile_image)
//                                            .into(userImage);
//                                } else {
//                                    GlideApp.with(getActivity())
//                                            .load(R.drawable.default_profile_image)
//                                            .into(userImage);
//                                }
//
//                                if (result.getTimeLeft() != 0) {
//                                    String timeLeftString = DateDifferenceService.dateDifferenceString(result.getTimeLeft());
//                                    String message = result.getName() + " must wait " + timeLeftString + " to get more points.";
//
//                                    header.setText(getString(R.string.unsuccessful));
//                                    header.setTextColor(getActivity().getColor(R.color.colorDanger));
//                                    givePointsResult.setText(message);
//
//                                    spinner.setVisibility(View.GONE);
//                                    resultsContainer.setVisibility(View.VISIBLE);
//
//                                    return;
//                                }
//
//                                givePointsSuccess = true;
//                                header.setText(getString(R.string.success));
//
//                                int pointTally = amount; // Set just in case a null response is received
//                                if (result.getPointTally() != 0) {
//                                    pointTally = result.getPointTally();
//                                }
//
//                                String pointTallyString;
//                                if (pointTally == 1) {
//                                    pointTallyString = "1 point for rewards.";
//                                } else {
//                                    pointTallyString = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointTally) + " points for rewards.";
//                                }
//
//                                int pointsGiven = amount;
//                                if (result.getPoints() != 0) {
//                                    pointsGiven = result.getPoints();
//                                }
//
//                                String points;
//                                String success;
//                                if (pointsGiven == 1) {
//                                    points = "1";
//                                    success = result.getName() + " was given " + points + " point and now has " + pointTallyString;
//                                } else {
//                                    points = NumberFormat.getNumberInstance(Locale.getDefault()).format(pointsGiven);
//                                    success = result.getName() + " was given " + points + " points and now has " + pointTallyString;
//                                }
//
//                                givePointsResult.setText(success);
//
//                            } else {
//                                GlideApp.with(getActivity())
//                                        .load(R.drawable.default_profile_image)
//                                        .into(userImage);
//
//                                header.setText(getString(R.string.no_match));
//                                givePointsResult.setText(getString(R.string.give_points_incorrect_phone_result));
//                                dismissButton.setText(getString(R.string.dismiss));
//                            }
//                        }
//
//                        spinner.setVisibility(View.GONE);
//                        resultsContainer.setVisibility(View.VISIBLE);
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<GivePointsResponse> call, @NonNull Throwable t) {
//                        Log.e(TAG, "Merchant manually give com.jayurewards.tablet.models.Points Error: " + t.getMessage());
//                        FlurryAgent.onError(TAG, "Merchant manually give com.jayurewards.tablet.models.Points"  + t.getMessage(), t);
//
//                        spinner.setVisibility(View.GONE);
//                        AlertService.showNetworkIssueAlert(getActivity());
//                    }
//                });
//            }
//        });
//
//        company.setOnClickListener(view -> {
//            if (getActivity() != null && merchantShops.size() > 1) {
//                ArrayList<String> shops = new ArrayList<>();
//                ArrayList<Integer> shopStdPts = new ArrayList<>();
//                ArrayList<String> shopCountryCodes = new ArrayList<>();
//
//                for (ShopAdminModel shop : merchantShops) {
//                    shops.add(shop.getCompany());
//                    shopStdPts.add(shop.getStandardPoints());
//                    shopCountryCodes.add(shop.getCountryCode() != null ? shop.getCountryCode() : usaCountryCode);
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("Select Company");
//                builder.setItems(shops.toArray(new String[0]), (dialog, which) -> {
//
//                    if (shops.get(which).equals(company.getText().toString())) {
//                        return;
//                    }
//
//                    countryCodeChangePhoneEditTextFormatting();
//                    pointAmount.setText(String.valueOf(shopStdPts.get(which)));
//                    company.setText(shops.get(which));
//                    countryCode.setText(shopCountryCodes.get(which));
//                    selectedStoreId = merchantShops.get(which).getStoreId();
//                });
//
//                builder.show();
//            }
//        });
//
//        dismissButton.setOnClickListener(view -> {
//            if (givePointsSuccess) {
//                if (listener != null) {
//                    listener.onSuccessfulPointsGiven(selectedStoreId, company.getText().toString());
//                    dismiss();
//                }
//            } else {
//                enablePostSubmit(true);
//                resultsContainer.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    /**
//     * Edit Text Functions
//     */
//    private TextWatcher textWatcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//            // Reset phone number and check phone number format if country code changed
//            if (countryCode.getText().hashCode() == s.hashCode()) {
//                phoneNumber.setText(null);
//                countryCodeChangePhoneEditTextFormatting();
//            } else if (pointAmount.getText().hashCode() == s.hashCode()) {
//                pointAmount.removeTextChangedListener(this);
//                String numberString = pointAmount.getText().toString();
//
//                // Format number based on location (e.g add commas)
//                if (!numberString.equals("")) {
//                    String numberStripped = stripNumberFormatting(numberString);
//
//                    double numberDouble;
//                    try {
//                        numberDouble = Double.parseDouble(numberStripped);
//                        String formattedNumber = NumberFormat.getNumberInstance(Locale.getDefault()).format(numberDouble);
//                        pointAmount.setText(formattedNumber);
//                        pointAmount.setSelection(pointAmount.getText().toString().length());
//
//                    } catch (Throwable t) {
//                        String message = "Give points text listener parse points error";
//                        LogService.errorReport(TAG, message, t, LogService.ErrorReportType.PARSING);
//                    }
//                }
//
//                pointAmount.addTextChangedListener(this);
//            }
//
//            checkForEmptyField();
//        }
//    };
//
//    // Change state for submit button based on text entered
//    private void checkForEmptyField() {
//        String amount = pointAmount.getText().toString();
//        String countryCallingCode = countryCode.getText().toString();
//        String phone = phoneNumber.getText().toString();
//
//        if ((pointAmount.isEnabled() && amount.isEmpty()) || phone.isEmpty() || countryCallingCode.isEmpty()) {
//            enablePostSubmit(false);
//
//        } else if (usaCountryCode.equals(countryCallingCode) &&  phone.length() <= 13) {
//            enablePostSubmit(false);
//
//        } else if (phone.length() < 6) {
//            enablePostSubmit(false);
//
//        } else {
//            enablePostSubmit(true);
//        }
//    }
//
//    // Change phone number length and format based on country code. Android's phone number format only works with phone input type
//    private void countryCodeChangePhoneEditTextFormatting() {
//        if (!usaCountryCode.equals(countryCode.getText().toString())) {
//            phoneNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
//            phoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
//        } else {
//            phoneNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(14) });
//            phoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
//        }
//    }
//
//    // Stripping commas, periods, and spaces to support international number formatting
//    private String stripNumberFormatting(String number) {
//        String numberStripped1 = number.replaceAll(",", "");
//        String numberStripped2 = numberStripped1.replaceAll("/.", "");
//        return numberStripped2.replaceAll(" ", "");
//    }
//
//    private void enablePostSubmit(Boolean enabled) {
//        if (getActivity() != null) {
//            if (!enabled) {
//                submitButton.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimaryLight)));
//                submitButton.setEnabled(false);
//
//            } else {
//                submitButton.setBackgroundTintList(ColorStateList.valueOf(getActivity().getColor(R.color.colorPrimary)));
//                submitButton.setEnabled(true);
//            }
//        }
//    }
//
//    private void hideKeyboard() {
//        InputMethodManager imm =
//                (InputMethodManager)phoneNumber.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive())
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//
//        imm.hideSoftInputFromWindow(phoneNumber.getWindowToken(), 0);
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        if (context instanceof GivePointsFragmentInterface) {
//            listener = (GivePointsFragmentInterface) context;
//
//        } else {
//            throw new RuntimeException(context.toString() + " must implement GivePointsFragmentInterface");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }

}