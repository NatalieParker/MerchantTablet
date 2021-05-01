package com.jayurewards.tablet.models.TeamMembers;

public class RegisterUserModel {

    private String name;
    private String email;
    private String countryCode;
    private String phone;
    private String birthDate;
    private String gender;
    private String photo;
    private String thumbnail;
    private String mobileDevice;
    private String firebaseUID;

    public RegisterUserModel(String name, String email, String countryCode, String phone, String birthDate, String gender, String photo, String thumbnail, String mobileDevice, String firebaseUID) {
        this.name = name;
        this.email = email;
        this.countryCode = countryCode;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photo = photo;
        this.thumbnail = thumbnail;
        this.mobileDevice = mobileDevice;
        this.firebaseUID = firebaseUID;
    }

    @Override
    public String toString() {
        return "RegisterUserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", phone='" + phone + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", mobileDevice='" + mobileDevice + '\'' +
                ", firebaseUID='" + firebaseUID + '\'' +
                '}';
    }
}
