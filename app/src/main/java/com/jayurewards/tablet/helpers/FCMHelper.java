package com.jayurewards.tablet.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jayurewards.tablet.models.FCMTokenUpdateModel;
import com.jayurewards.tablet.models.UpdatePushMessageOpenModel;
import com.jayurewards.tablet.networking.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FCMHelper extends FirebaseMessagingService {
    private static final String TAG = "FCMHelper";

    /**
     * Update tokens when there is a change identified by Firebase
     *
     * @param token Updated FCM token
     */
    @Override
    public void onNewToken(@NonNull String token) {
        SharedPreferences sharedPref = this.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(GlobalConstants.USER_ID, 0);
        String userUid = sharedPref.getString(GlobalConstants.USER_FIREBASE_UID, null);
        String merchantUid = sharedPref.getString((GlobalConstants.MERCHANT_FIREBASE_UID), null);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            if (merchantUid != null) {
//                FCMTokenUpdateModel paramsMerchant = new FCMTokenUpdateModel(userId, merchantUid, token);
//                Call<String> call = RetrofitClient.getInstance().getRestMerchant().merchantUpdateFcmToken(paramsMerchant);
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
////                        Log.i(TAG, "Merchant update token result: " + response.body());
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                        String message = "On new token Merchant update FCM token error";
//                        LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
//                    }
//                });
//            }

            if (userUid != null) {
                FCMTokenUpdateModel params = new FCMTokenUpdateModel(userId, userUid, token);
                Call<String> call = RetrofitClient.getInstance().getRestUser().userUpdateFcmToken(params);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                        Log.i(TAG, "User update token result: " + response.body());
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        String message = "On New token User update FCM token error";
                        LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
                    }
                });
            }
        }
    }

    /**
     * Handled notifications - only called if App is in the foreground. (Remote notifications handled in splash screen)
     * Can handle Android's notification messages and data messages
     * Should send all notifications as data messages (data messages treated as notification message in background)
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "FCM Received message: " + remoteMessage.getNotification().getTitle() + "\n" + remoteMessage.getNotification().getBody());

            /** TODO(developer): Configure this screen to handle sound notifications for messages
             * No sounds when the app is open, and no notification in a message room
             * Utilize static variables to track message room, and if app is in foreground/background
             * Use different channels for different notifications: https://stackoverflow.com/questions/48453189/disable-notification-sound-on-android-o
             * --- App lifecycle: https://stackoverflow.com/questions/3667022/checking-if-an-android-application-is-running-in-the-background/48767617#48767617
             */

//            if (remoteMessage.getData().size() > 0) {
//                String recipient = remoteMessage.getData().get(GlobalConstants.FCM_RECIPIENT);
//
//                if (GlobalConstants.FCM_RECIPIENT_MERCHANT.equals(recipient)) {
//                    String notificationIdMerchant = remoteMessage.getData().get(GlobalConstants.FCM_NOTIFICATION_ID_MERCHANT);
//                    if (notificationIdMerchant != null && !notificationIdMerchant.equals("")) {
//                        MerchantForegroundFCMService merchantFCM = new MerchantForegroundFCMService();
//                        merchantFCM.generateForegroundNotification(remoteMessage, this);
//                    }
//
//                } else {
//                    String notificationId = remoteMessage.getData().get(GlobalConstants.FCM_NOTIFICATION_ID);
//                    if (notificationId != null && !notificationId.equals("")) {
//                        SharedPreferences sp = getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
//                        int appUserId = sp.getInt(GlobalConstants.USER_ID, 0);
//
//                        UserForegroundFCMService userFCM = new UserForegroundFCMService();
//                        userFCM.generateForegroundNotification(remoteMessage, this, appUserId);
//                    }
//                }
//
//            } else {
//                String title = remoteMessage.getNotification().getTitle();
//                String message = remoteMessage.getNotification().getBody();
//                new LocalNotificationService().showDefaultNotification(title, message, this);
//            }
        }


    }

    /**
     * Public Notification Helper Functions
     */
    public static void updateUserFcmToken(int userId, String firebaseUid) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String message = "Login User FCM token error";
                LogHelper.errorReport(TAG, message, task.getException(), LogHelper.ErrorReportType.NETWORK);
                return;
            }

            String token = task.getResult().getToken();
            FCMTokenUpdateModel params = new FCMTokenUpdateModel(userId, firebaseUid, token);

            Call<String> callToken = RetrofitClient.getInstance().getRestUser().userUpdateFcmToken(params);
            callToken.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    clearNotificationTotalBadgeCount(firebaseUid);
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    String message = "User update FCM token error";
                    LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
                }
            });
        });
    }

    public static void updateMerchantFcmToken(String firebaseUid, Context context) {
        SharedPreferences sp = context.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        int userId = sp.getInt(GlobalConstants.USER_ID, 0);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                String message = "Login User FCM token error";
                LogHelper.errorReport(TAG, message, task.getException(), LogHelper.ErrorReportType.NETWORK);
                return;
            }

//            String token = task.getResult().getToken();
//            FCMTokenUpdateModel params = new FCMTokenUpdateModel(userId, firebaseUid, token);
//
//            Call<String> callToken = RetrofitClient.getInstance().getRestMerchant().merchantUpdateFcmToken(params);
//            callToken.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                    // Successfully updated merchant's token
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                    String message = "Merchant update FCM token error";
//                    LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
//                }
//            });
        });
    }

    public static void removeFCMToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(GlobalConstants.SHARED_PREF, Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(GlobalConstants.USER_ID, 0);
        String userUid = sharedPref.getString(GlobalConstants.USER_FIREBASE_UID, null);
        String merchantUid = sharedPref.getString(GlobalConstants.MERCHANT_FIREBASE_UID, null);

        // Remove FCM Tokens
        if (userUid != null) {
            Call<String> call = RetrofitClient.getInstance().getRestUser().userRemoveFcmToken(userUid);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    // Successfully removed user's token
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    String message = "User remove FCM token error";
                    LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
                }
            });
        }

        if (merchantUid != null) {
//            Call<String> call = RetrofitClient.getInstance().getRestMerchant().merchantRemoveFcmToken(userId);
//            call.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
//                    // Successfully removed merchant's token
//                }
//
//                @Override
//                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
//                    String message = "Merchant remove FCM token error";
//                    LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
//                }
//            });
        }
    }

    private static void clearNotificationTotalBadgeCount(String firebaseUid) {
        Call<String> callClearBadge = RetrofitClient.getInstance().getRestUser().clearNotificationsTotalBadgeCount(firebaseUid);
        callClearBadge.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                // Successfully cleared notification badge count
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String message = "User clear notification badge count error";
                LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
            }
        });
    }

    /**
     * Public Notification helpers
     */
    public static void UpdatePushMessageOpenModel(UpdatePushMessageOpenModel params) {
        Call<String> call = RetrofitClient.getInstance().getRestUser().updatePushMessageOpenModel(params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                // Successfully updated push message
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String message = "User update push message open count error";
                LogHelper.errorReport(TAG, message, t, LogHelper.ErrorReportType.NETWORK);
            }
        });
    }

    /**
     * Handled notifications deleted by Firebase
     */
    @Override
    public void onDeletedMessages() {
//        /**
//         * In some situations, FCM may not deliver a message. This occurs when there are too many messages (>100) pending for your app on a
//         * particular device at the time it connects or if the device hasn't connected to FCM in more than one month. In these cases, you may receive
//         * a callback to FirebaseMessagingService.onDeletedMessages() When the app instance receives this callback, it should perform a full sync with
//         * your app server. If you haven't sent a message to the app on that device within the last 4 weeks, FCM won't call onDeletedMessages().
//         *
//         * https://firebase.google.com/docs/cloud-messaging/android/receive
//         */
    }

}
