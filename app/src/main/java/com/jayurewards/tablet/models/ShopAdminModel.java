package com.jayurewards.tablet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopAdminModel {

    @SerializedName("store_id")
    @Expose
    private int storeId;

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("country_code")
    @Expose
    private String countryCode;

    @SerializedName("std_points")
    @Expose
    private int standardPoints;

    @SerializedName("std_pt_timeout")
    @Expose
    private int standardPtTimeout;

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getStandardPoints() {
        return standardPoints;
    }

    public void setStandardPoints(int standardPoints) {
        this.standardPoints = standardPoints;
    }

    public int getStandardPtTimeout() {
        return standardPtTimeout;
    }

    public void setStandardPtTimeout(int standardPtTimeout) {
        this.standardPtTimeout = standardPtTimeout;
    }

    @Override
    public String toString() {
        return "ShopAdminModel{" +
                "storeId=" + storeId +
                ", company='" + company + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", standardPoints=" + standardPoints +
                ", standardPtTimeout=" + standardPtTimeout +
                '}';
    }
}
