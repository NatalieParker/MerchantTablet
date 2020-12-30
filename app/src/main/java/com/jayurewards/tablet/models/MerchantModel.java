package com.jayurewards.tablet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MerchantModel {

    @SerializedName("merchant_id")
    @Expose
    private int merchantId;

    @SerializedName("firebase_uid")
    @Expose
    private String firebaseUid;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("fname")
    @Expose
    private String firstName;

    @SerializedName("lname")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("ship_address")
    @Expose
    private String shipAddress;

    @SerializedName("ship_city")
    @Expose
    private String shipCity;

    @SerializedName("ship_state")
    @Expose
    private String shipState;

    @SerializedName("ship_postal_code")
    @Expose
    private int shipZipCode;

    @SerializedName("stripe_id")
    @Expose
    private String stripeId;

    @SerializedName("subscription_id")
    @Expose
    private String subscriptionId;

    // Used for empty results
    @SerializedName("message")
    @Expose
    private String message;

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(String shipAddress) {
        this.shipAddress = shipAddress;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public int getShipZipCode() {
        return shipZipCode;
    }

    public void setShipZipCode(int shipZipCode) {
        this.shipZipCode = shipZipCode;
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MerchantModel{" +
                "merchantId=" + merchantId +
                ", firebaseUid='" + firebaseUid + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", shipAddress='" + shipAddress + '\'' +
                ", shipCity='" + shipCity + '\'' +
                ", shipState='" + shipState + '\'' +
                ", shipZipCode=" + shipZipCode +
                ", stripeId='" + stripeId + '\'' +
                ", subscriptionId='" + subscriptionId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
