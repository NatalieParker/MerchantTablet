package com.jayurewards.tablet;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import com.jayurewards.tablet.helpers.FCMHelper;
import com.jayurewards.tablet.helpers.GlobalConstants;

public class JayuApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

}
