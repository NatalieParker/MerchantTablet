package com.jayurewards.tablet.models.TeamMembers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TeamMemberModel {

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("firebase_uid")
    @Expose
    private String firebaseUID;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("dialing_code")
    @Expose
    private String dialingCode;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("dob")
    @Expose
    private String birthdate;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("photo")
    @Expose
    private String photoUrl;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnailUrl;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("type")
    @Expose
    private  String type;

    @SerializedName("admin_lvl")
    @Expose
    private int adminLvl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirebaseUID() {
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDialingCode() {
        return dialingCode;
    }

    public void setDialingCode(String dialingCode) {
        this.dialingCode = dialingCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAdminLvl() {
        return adminLvl;
    }

    public void setAdminLvl(int adminLvl) {
        this.adminLvl = adminLvl;
    }

    @Override
    public String toString() {
        return "TeamMemberModel{" +
                "userId=" + userId +
                ", firebaseUID='" + firebaseUID + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", dialingCode='" + dialingCode + '\'' +
                ", phone='" + phone + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", gender='" + gender + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", adminLvl=" + adminLvl +
                '}';
    }
}
