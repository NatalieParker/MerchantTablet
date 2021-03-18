package com.jayurewards.tablet.models;

public class FCMTokenUpdateModel {

    private int userId;
    private String firebaseUid;
    private String fcmToken;

    public FCMTokenUpdateModel(int userId, String firebaseUid, String fcmToken) {
        this.userId = userId;
        this.firebaseUid = firebaseUid;
        this.fcmToken = fcmToken;
    }

    @Override
    public String toString() {
        return "FCMTokenUpdateModel{" +
                "userId=" + userId +
                ", firebaseUid='" + firebaseUid + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }

}
