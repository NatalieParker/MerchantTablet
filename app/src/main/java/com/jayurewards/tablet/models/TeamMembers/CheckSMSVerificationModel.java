package com.jayurewards.tablet.models.TeamMembers;

public class CheckSMSVerificationModel {
    private final String phone;
    private final String code;

    public CheckSMSVerificationModel(String phone, String code) {
        this.phone = phone;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CheckSMSVerificationModel{" +
                "phone='" + phone + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
