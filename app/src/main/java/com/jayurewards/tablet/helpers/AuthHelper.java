package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jayurewards.tablet.BuildConfig;
import com.jayurewards.tablet.screens.MainActivity;

import java.util.Date;

public class AuthHelper {

    private static final String TAG = "log out";

    public static void logOut(Context currentScreen) {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.googleSigninWebClientId)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(currentScreen, gso);

        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();

        Date date = new Date();
        Intent intent = new Intent(currentScreen, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        currentScreen.startActivity(intent);

        Log.i(TAG, "USER SIGNED OUT AT: " + date);
    }

    public static void logInCheck(Context currentScreen) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) logOut(currentScreen);
    }

}
