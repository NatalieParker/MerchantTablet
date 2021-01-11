package com.jayurewards.tablet.models;

public class UpdateSubscriptionStatus {

    private String stripeId;
    private String status;

    @Override
    public String toString() {
        return "UpdateSubscriptionStatus{" +
                "stripeId='" + stripeId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public UpdateSubscriptionStatus(String stripeId, String status) {
        this.stripeId = stripeId;
        this.status = status;

    }
}
