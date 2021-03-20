package com.jayurewards.tablet.models;

public class UpdatePushMessageOpenModel {

    private final int pushId;
    private final int count;

    @Override
    public String toString() {
        return "UpdatePushMessageOpenModel{" +
                "pushId=" + pushId +
                ", count=" + count +
                '}';
    }

    public UpdatePushMessageOpenModel(int pushId, int count) {
        this.pushId = pushId;
        this.count = count;
    }
}
