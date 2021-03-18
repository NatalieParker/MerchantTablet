package com.jayurewards.tablet.networking;

import com.jayurewards.tablet.models.FCMTokenUpdateModel;
import com.jayurewards.tablet.models.UpdatePushMessageOpenModel;
import com.jayurewards.tablet.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestUser {

    // Retrieve a user's profile
    @GET("user/{userUID}")
    Call<UserModel> getUser(@Path("userUID") String userUID);

    // Update User FCM token
    @Headers("Content-Type: application/json")
    @POST("/notification/user/fcmtoken-update")
    Call<String> userUpdateFcmToken(@Body FCMTokenUpdateModel params);

    // Remove User FCM token
    @GET("/notification/user/fcmtoken-remove/{firebaseUid}")
    Call<String>userRemoveFcmToken(@Path("firebaseUid") String firebaseUid);

    // Clear total notifications badge count (not needed for Android phones)
    @GET("notification/android-users/badge-count/clear/{firebaseUid}")
    Call<String>clearNotificationsTotalBadgeCount(@Path("firebaseUid") String firebaseUid);

//    // Get user notification mute settings
//    @GET("notification/get/notification-settings/{userId}")
//    Call<NotificationSettingsModel>getUserNotificationSettings(@Path("userId") int userId);
//
//    // Update user notification mute setting
//    @Headers("Content-Type: application/json")
//    @POST("/notification/update/notification-settings")
//    Call<String>updateUserNotificationSettings(@Body UpdateNotificationSetting params);

    // Update push message open count
    @Headers("Content-Type: application/json")
    @POST("/notification/update/mobile-opens")
    Call<String>updatePushMessageOpenModel(@Body UpdatePushMessageOpenModel params);

}
