package com.jayurewards.tablet.models.TeamMembers;

public class TeamMemberRequest {
    String countryCode;
    String phone;
    int storeId;

    public TeamMemberRequest(String countryCode, String phone, int storeId) {
        this.countryCode = countryCode;
        this.phone = phone;
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "TeamMemberRequest{" +
                "countryCode='" + countryCode + '\'' +
                ", phone='" + phone + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
