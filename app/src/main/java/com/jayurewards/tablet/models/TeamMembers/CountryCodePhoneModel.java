package com.jayurewards.tablet.models.TeamMembers;

public class CountryCodePhoneModel {
    String countryCode;
    String phone;

    public CountryCodePhoneModel(String countryCode, String phone) {
        this.countryCode = countryCode;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CountryCodePhoneModel{" +
                "countryCode='" + countryCode + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
