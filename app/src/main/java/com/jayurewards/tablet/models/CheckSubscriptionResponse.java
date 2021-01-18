package com.jayurewards.tablet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckSubscriptionResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    @Override
    public String toString() {
        return "CheckSubscriptionResponse{" +
                "status='" + status + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                '}';
    }

}
