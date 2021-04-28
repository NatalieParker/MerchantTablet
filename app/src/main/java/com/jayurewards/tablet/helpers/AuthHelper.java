package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jayurewards.tablet.BuildConfig;
import com.jayurewards.tablet.models.CheckSubscriptionParams;
import com.jayurewards.tablet.models.CheckSubscriptionResponse;
import com.jayurewards.tablet.models.UpdateSubscriptionStatus;
import com.jayurewards.tablet.networking.RetrofitClient;
import com.jayurewards.tablet.screens.InactiveAccountActivity;
import com.jayurewards.tablet.screens.LoginMerchantActivity;
import com.jayurewards.tablet.screens.UserKeypadActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {
    private static final String TAG = "Auth Helper";

    public static boolean isUserLoggedIn(Context currentScreen) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return true;
        } else {
            logOut(currentScreen);
            return false;
        }
    }

    public static void checkMerchantSubscription(Context context, ConstraintLayout spinner) {
        SharedPreferences sharedPref = context.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        String stripeId = sharedPref.getString(GlobalConstants.STRIPE_ID, null);
        String subscriptionId = sharedPref.getString(GlobalConstants.SUBSCRIPTION_ID, null);

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

                    Intent intent = new Intent(context, UserKeypadActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    if (spinner != null) spinner.setVisibility(View.GONE);
                    context.startActivity(intent);

                } else {
                    String subStatus = "inactive";
                    AuthHelper.logOut(context);

                    if (status != null && status.getStatus() != null) {
                        status.setStatus(subStatus);

                        UpdateSubscriptionStatus uss = new UpdateSubscriptionStatus(stripeId, subStatus);
                        Call<String> callUpdate = RetrofitClient.getInstance().getRestAuth().updateSubscriptionStatus(uss);
                        callUpdate.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                                // Success
                            }

                            @Override
                            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                                String errorMessage = "Update subscription status error: ";
                                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                            }
                        });
                    }

                    if (spinner != null) spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(context, InactiveAccountActivity.class);
                    context.startActivity(intent);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CheckSubscriptionResponse> call, @NonNull Throwable t) {
                String errorMessage = "Check subscription network error: ";
                LogHelper.errorReport(TAG, errorMessage, t, LogHelper.ErrorReportType.NETWORK);
                if (spinner != null) spinner.setVisibility(View.GONE);

                AlertHelper.showNetworkAlert(context);

                if (t.getMessage() != null && t.getMessage().equals("timeout")) {
                    AuthHelper.logOut(context);
                }
            }
        });
    }

    public static void logOut(Context currentScreen) {
        SharedPreferences sharedPref = currentScreen.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        sharedPref.edit().remove(GlobalConstants.MERCHANT_ID).apply();
        sharedPref.edit().remove(GlobalConstants.MERCHANT_FIREBASE_UID).apply();
        sharedPref.edit().remove(GlobalConstants.PIN_CODE).apply();

        //TODO: Manually remove all team member values from shared preferences

        sharedPref.edit().clear().apply();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.googleSigninWebClientId)
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(currentScreen, gso);

        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();

        Intent intent = new Intent(currentScreen, LoginMerchantActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentScreen.startActivity(intent);
    }
}
