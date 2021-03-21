package com.jayurewards.tablet.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.jayurewards.tablet.GlideApp;
import com.jayurewards.tablet.R;
import com.jayurewards.tablet.helpers.AlertHelper;
import com.jayurewards.tablet.helpers.AuthHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;
import com.jayurewards.tablet.helpers.ImageHelper;
import com.jayurewards.tablet.helpers.LogHelper;
import com.jayurewards.tablet.models.RegisterUserModel;
import com.jayurewards.tablet.models.UserModel;
import com.jayurewards.tablet.networking.RetrofitClient;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class RegistrationTeamActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    //Views
    private MaterialButton buttonCancelRegistration;
    private MaterialButton buttonSubmitRegistration;
    private ImageView userImage;
    private TextView birthday;
    private EditText usernameEditText;
    private EditText emailEditText;
    private MaterialButton editPhotoButton;
    private TextView disclaimer;

    // Passed data
    private String userFirebaseUID;
    private String countryCode;
    private String phoneNumber;

    private String birthdayInput = "";
    private final String deviceType = android.os.Build.DEVICE;
    private String genderInput = "";

    private String currentPhotoPath;
    private Bitmap bitmap;
    private Bitmap thumbnail;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private RelativeLayout spinner;
    private ImageHelper imageService = new ImageHelper();

    private InputMethodManager imm;
    private long lastClickTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_team);

        // Retrieve data
        Intent intent = getIntent();
        countryCode = intent.getStringExtra(GlobalConstants.COUNTRY_CODE);
        phoneNumber = intent.getStringExtra(GlobalConstants.PHONE);
        userFirebaseUID = intent.getStringExtra(GlobalConstants.USER_FIREBASE_UID);

        // Initialize Views
//        spinner = findViewById(R.id.loadingRegisterUser);
        birthday = findViewById(R.id.textRegistrationDatePicker);
        editPhotoButton = findViewById(R.id.buttonRegistrationEditPhoto);
        usernameEditText = findViewById(R.id.editTextRegisterName);
        emailEditText = findViewById(R.id.editTextRegisterEmail);
        disclaimer = findViewById(R.id.textRegistrationDisclaimer2);
        userImage = findViewById(R.id.imageRegistrationProfile);
        buttonCancelRegistration = findViewById(R.id.buttonRegistrationCancel);
        buttonSubmitRegistration = findViewById(R.id.buttonRegistrationRegister);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Generate bottom border for birthday text
//        LayerDrawable border = UIDesignService.getBorders(
//                0xFFFFFFFF, // Background color
//                Color.LTGRAY, // Border color
//                0, // Left border in pixels
//                0, // Top border in pixels
//                0, // Right border in pixels
//                5 // Bottom border in pixels
//        );
//        birthday.setBackground(border);

        setUpTextListeners();
        initDateListener();
        enablePostSubmit(false);
        setUpOnClickListeners();

        Log.i(TAG, "REGISTRATION SCREEN WORKING");
    }

    /**
     * Setup click listeners
     */

    private void setUpOnClickListeners() {
        buttonCancelRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        birthday.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            // Using old Holo theme to display spinner
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Dialog, mDateSetListener, year, month, day);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            dialog.show();

            // Remove background and shadow from buttons. Must call after showing dialog to avoid NPE
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(Color.TRANSPARENT);
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setStateListAnimator(null);
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.TRANSPARENT);
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setStateListAnimator(null);
        });

        userImage.setOnClickListener(v -> getUserImage());
        editPhotoButton.setOnClickListener(v -> getUserImage());
//        buttonSubmitRegistration.setOnClickListener(view -> onRegisterUser());

        disclaimer.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GlobalConstants.USER_TERMS_AND_CONDITIONS_LINK));
            startActivity(browserIntent);
        });

    }

    /**
     * Submit user registration
     */
    private void onRegisterUser() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        // Hide keyboard
        imm.hideSoftInputFromWindow(usernameEditText.getWindowToken(), 0);

        spinner.setVisibility(View.VISIBLE);

        String imageBase64 = "";
        if (bitmap != null) {
            imageBase64 = imageService.uploadImage(bitmap);
        }

        String thumbnailBase64 = "";
        if (thumbnail != null) {
            thumbnailBase64 = imageService.uploadImage(thumbnail);
        }

        String nameInput = usernameEditText.getText().toString();
        String emailInput = emailEditText.getText().toString();

        if (!emailInput.isEmpty()) {
            if (!isValidEmail(emailInput)) {
                emailEditText.setError("Please enter a valid email");
                emailEditText.requestFocus();
                spinner.setVisibility(View.GONE);
                return;
            }
        }

        RegisterUserModel params = new RegisterUserModel(nameInput, emailInput, countryCode, phoneNumber,
                birthdayInput, genderInput, imageBase64, thumbnailBase64, deviceType, userFirebaseUID);

        Call<String> call = RetrofitClient.getInstance().getRestUser().registerUser(params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (!response.isSuccessful()) {
                    try {
                        String error = response.errorBody() != null ? response.errorBody().string() : null;
                        if (error == null) {
                            String errorMessage = "Register user REST Error: ";
                            LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                            AlertHelper.showNetworkAlert(RegistrationTeamActivity.this);
                            return;
                        }

                        switch (error) {
                            case "Duplicate Phone":
                                spinner.setVisibility(View.GONE);
                                AlertHelper.showAlert(RegistrationTeamActivity.this, "Phone Number in Use",
                                        "This phone number is already in use. Please check your phone number and try again. Contact us if the issue is not resolved.");
                                break;

                            case "Duplicate Firebase UID":
                                spinner.setVisibility(View.GONE);
                                AlertHelper.showAlert(RegistrationTeamActivity.this, "Account Already exists",
                                        "This account already exists. Please try again. Contact us if the issue is not resolved.");
                                break;

                            case "Duplicate Email":
                                spinner.setVisibility(View.GONE);
                                AlertHelper.showAlert(RegistrationTeamActivity.this, "Email Already Exists",
                                        "This email is already in use. Please log into the original account and delete the email, or use another email.");
                                break;
                        }

                    } catch (IOException e) {
                        String errorMessage = "Register user unknown REST Error: ";
                        LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                        AlertHelper.showNetworkAlert(RegistrationTeamActivity.this);
                    }
                } else {
                    String userUID = userFirebaseUID;
                    Call<UserModel> callUser = RetrofitClient.getInstance().getRestUser().getUser(userUID);
                    callUser.enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(@NonNull Call<UserModel> call, @NonNull Response<UserModel> response) {
                            if (!response.isSuccessful()) {
                                String errorMessage = "User registered get user REST Error: ";
                                LogHelper.serverError(TAG, errorMessage, response.code(), response.message());
                                AlertHelper.showNetworkAlert(RegistrationTeamActivity.this);
                                return;
                            }

                            UserModel user = response.body();
                            if (user != null) {
                                SharedPreferences sharedPref = RegistrationTeamActivity.this.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putInt(GlobalConstants.USER_ID, user.getUserId());
                                editor.putString(GlobalConstants.USER_FIREBASE_UID, user.getFirebaseUID());
                                editor.putString(GlobalConstants.NAME, user.getName());
                                editor.putString(GlobalConstants.COUNTRY_CODE, user.getCountryCode());
                                editor.putString(GlobalConstants.PHONE, user.getPhone());
                                editor.putString(GlobalConstants.BIRTHDATE, user.getBirthdate());

//                                editor.putBoolean(GlobalConstantsMerchant.SHARED_PREF_IS_MERCHANT_ACTIVE, false);
                                editor.apply();

//                                AuthHelper.mapUserIdToMerchant(RegistrationTeamActivity.this);

                                // Save user profile photo to local device - Download image as bitmap
                                // Check if activity is null and if photo url is valid
                                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                                    GlideApp.with(RegistrationTeamActivity.this)
                                            .asBitmap()
                                            .load(user.getPhotoUrl())
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    imageService.saveProfileImageInternalStorage(RegistrationTeamActivity.this, resource);

                                                    editor.putString(GlobalConstants.PHOTO_URL, user.getPhotoUrl());
                                                    editor.putString(GlobalConstants.THUMBNAIL_URL, user.getThumbnailUrl());
                                                    editor.apply();

                                                    spinner.setVisibility(View.GONE);

                                                    Intent intent = new Intent(RegistrationTeamActivity.this, UserKeypadActivity.class);

                                                    // Creates a clean new activity and clears everything else
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });

                                    // If photo URL is empty Navigate to nearby screen
                                } else {
                                    spinner.setVisibility(View.GONE);
                                    Intent intent = new Intent(RegistrationTeamActivity.this, UserKeypadActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserModel> call, @NonNull Throwable t) {
                            spinner.setVisibility(View.GONE);
                            String errorMessage = "Retrieve newly registered user network ERROR:\n" + t.getMessage();
                            LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                            AlertHelper.showNetworkAlert(RegistrationTeamActivity.this);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                spinner.setVisibility(View.GONE);
                String errorMessage = "Register user network ERROR:\n" + t.getMessage();
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                AlertHelper.showNetworkAlert(RegistrationTeamActivity.this);
            }
        });
    }

    /**
     * Initialize date listener
     */
    private void initDateListener() {
        mDateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String calDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(cal.getTime());

            // Display readable formatted date
            birthday.setText(calDate);
            birthday.setTextColor(Color.BLACK);

            //Android months start at zero so add one
            month = month + 1;
            birthdayInput = year + "/" + month + "/" + dayOfMonth;
        };
    }

    /**
     * Handle radio button for gender selection
     *
     * @param view - Called from xml layout file
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        if (view.getId() == R.id.radioRegistrationFemale) {
            if (checked) {
                genderInput = "female";
            }
        } else if (view.getId() == R.id.radioRegistrationMale) {
            if (checked) {
                genderInput = "male";
            }
        }
    }

    /**
     * USER IMAGE METHODS
     */
    private void getUserImage() {
        String[] options;
        if (bitmap == null) {
            options = new String[]{"Camera", "Gallery", "Cancel"};
        } else {
            options = new String[]{"Camera", "Gallery", "Delete", "Cancel"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    getCameraImage();
                    break;

                case 1:
                    getGalleryImage();
                    break;

                case 2:
                    if (options[2].equals("Delete")) {
                        userImage.setImageResource(R.drawable.default_profile);
                        bitmap = null;
                        editPhotoButton.setText(getString(R.string.add_photo));
                    }

                    break;

                case 3:
                    // User Cancel (Dialog automatically dismisses)
                    break;

                default: break;
            }
        });

        builder.show();
    }

    private void getGalleryImage() {
        currentPhotoPath = null;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void getCameraImage() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);

        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;

                try {
                    photoFile = createImageFile();

                } catch (IOException e) {
                    Log.e(TAG, " Unable to retrieve image file : " + e.toString());
                }

                if (photoFile != null) {

                    // Prep file path for image
                    // Using "this.getPackageName()" to match the authorization
                    Uri photoURI = FileProvider.getUriForFile(this,
                            this.getPackageName(),
                            photoFile);

                    // Prep picture file
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Saves images publicly for the user
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",   /* suffix */
                    storageDir      /* directory */
            );

            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = image.getAbsolutePath();

        } catch (IOException e) {
            Log.e(TAG, " File saving Error : " + e.toString());
        }

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image from Camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));

            String destinationFileName = "temp_crop_image.jpg";
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), destinationFileName));
            openCropActivity(imageUri, destinationUri);

            // Image from Gallery
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            String destinationFileName = "temp_crop_image.jpg";
            Uri destinationUri = Uri.fromFile(new File(getCacheDir(), destinationFileName));
            openCropActivity(imageUri, destinationUri);

            // Crop Image result
        } else if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            final Uri resultUri = UCrop.getOutput(data);
            handleImageResult(resultUri);

        } else if (resultCode == UCrop.RESULT_ERROR && data != null) {
            handleCropError(data);
        }
    }

    /**
     * Image helpers
     */
    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        int maxWidth = 700;
        int maxHeight = 700;

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(RegistrationTeamActivity.this);
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        String title = "Image Cropping Error";
        String message = "Unexpected image cropping error. Please try again.";

        if (cropError != null) {
            String errorMessage = "Register user Crop error: " + cropError;
            LogHelper.errorReport(TAG, errorMessage, cropError, LogHelper.ErrorReportType.IMAGE);
            AlertHelper.showAlert(RegistrationTeamActivity.this, title, cropError.getMessage());
        } else {
            String errorMessage = "Register User unexpected image cropping error.";
            LogHelper.logReport(TAG, errorMessage, LogHelper.ErrorReportType.IMAGE);
            AlertHelper.showAlert(RegistrationTeamActivity.this, title, message);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void handleImageResult(Uri uri) {
        imageService = new ImageHelper();

        try {
            bitmap = imageService.handleSamplingAndRotationBitmap(this, uri);
            thumbnail = imageService.generateThumbnail(bitmap);
            userImage.setImageBitmap(bitmap);
            editPhotoButton.setText(R.string.change_photo);

            if (currentPhotoPath != null) galleryAddPic();

        } catch (IOException e) {
            String errorMessage = "Register User Sampling bitmap/thumbnail error: " + e.getMessage();
            LogHelper.errorReport(TAG, errorMessage, e, LogHelper.ErrorReportType.IMAGE);

            String title = "Add Photo Error";
            String message = "Unable to add photo. Please try again.";
            AlertHelper.showAlert(RegistrationTeamActivity.this, title, message);
        }
    }

    /**
     * Action after receiving request permissions result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCameraImage();
            } else {
                Toast.makeText(this, "Please give photo and media permissions to post images.", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getGalleryImage();

            } else {
                Toast.makeText(this, "Please give photo and media permissions to post images.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * EDIT TEXT METHODS
     */
    private void setUpTextListeners() {
        usernameEditText.addTextChangedListener(textWatcher);
        emailEditText.addTextChangedListener(textWatcher);
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
        String name = usernameEditText.getText().toString();
        enablePostSubmit(name.length() >= 2);
    }

    private void enablePostSubmit(Boolean enabled) {
        if (!enabled) {
            buttonSubmitRegistration.setTextColor(Color.GRAY);
            buttonSubmitRegistration.setEnabled(false);
        } else {
            buttonSubmitRegistration.setTextColor(getColor(R.color.colorPrimary));
            buttonSubmitRegistration.setEnabled(true);
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();

        // Use below return to invalidate email if it is empty
//        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}