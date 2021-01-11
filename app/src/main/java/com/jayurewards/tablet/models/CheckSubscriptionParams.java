package com.jayurewards.tablet.models;

public class CheckSubscriptionParams {
    private String stripeId;
    private String subscriptionId;

    @Override
    public String toString() {
        return "CheckSubscriptionParams{" +
                "stripeId='" + stripeId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                '}';
    }

    public CheckSubscriptionParams(String stripeId, String subscriptionId) {
        this.stripeId = stripeId;
        this.subscriptionId = subscriptionId;

    }
}
