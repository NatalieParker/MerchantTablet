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

    private SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();

        updateFCMTokens();
        createNotificationChannel();
    }

    /**
     * Update FCM Token
     */
    private void updateFCMTokens() {
        int userId = sharedPref.getInt(GlobalConstants.USER_ID, 0);
        String userUid = sharedPref.getString(GlobalConstants.USER_FIREBASE_UID, null);
        String merchantUid = sharedPref.getString(GlobalConstants.MERCHANT_FIREBASE_UID, null);

        if (userUid != null) {
            FCMHelper.updateUserFcmToken(userId, userUid);
        }

        if (merchantUid != null) {
            FCMHelper.updateMerchantFcmToken(merchantUid, getApplicationContext());
        }
    }

    /**
     * Create Notification Channels
     */
    private void createNotificationChannel() {
        defaultNotificationChannel();
    }

    private void defaultNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String name = getString(R.string.channel_name_default);
            String description = getString(R.string.channel_description_default);

            // Set Audio
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.light);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(GlobalConstants.CHANNEL_DEFAULT, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setSound(sound, audioAttributes);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
