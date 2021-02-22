package com.jayurewards.tablet.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopAdminModel implements Parcelable {

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

    public ShopAdminModel(int storeId, String company, String countryCode, int standardPoints, int standardPtTimeout) {
        this.storeId = storeId;
        this.company = company;
        this.countryCode = countryCode;
        this.standardPoints = standardPoints;
        this.standardPtTimeout = standardPtTimeout;
    }

    protected ShopAdminModel(Parcel in) {
        storeId = in.readInt();
        company = in.readString();
        countryCode = in.readString();
        standardPoints = in.readInt();
        standardPtTimeout = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(storeId);
        dest.writeString(company);
        dest.writeString(countryCode);
        dest.writeInt(standardPoints);
        dest.writeInt(standardPtTimeout);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShopAdminModel> CREATOR = new Creator<ShopAdminModel>() {
        @Override
        public ShopAdminModel createFromParcel(Parcel in) {
            return new ShopAdminModel(in);
        }

        @Override
        public ShopAdminModel[] newArray(int size) {
            return new ShopAdminModel[size];
        }
    };

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


