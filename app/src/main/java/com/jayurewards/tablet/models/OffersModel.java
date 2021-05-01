package com.jayurewards.tablet.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OffersModel {

    @SerializedName("offer_id")
    @Expose
    private int offerId;

    @SerializedName("store_id")
    @Expose
    private int storeId;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("pts_required")
    @Expose
    private int ptsRequired;

    @SerializedName("pts_give")
    @Expose
    private int ptsGive;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPtsRequired() {
        return ptsRequired;
    }

    public void setPtsRequired(int ptsRequired) {
        this.ptsRequired = ptsRequired;
    }

    public int getPtsGive() {
        return ptsGive;
    }

    public void setPtsGive(int ptsGive) {
        this.ptsGive = ptsGive;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "OffersModel{" +
                "offerId=" + offerId +
                ", storeId=" + storeId +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", ptsRequired=" + ptsRequired +
                ", ptsGive=" + ptsGive +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                '}';
    }
}
