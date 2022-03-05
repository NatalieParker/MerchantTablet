package com.jayurewards.tablet.models.TeamMembers;

public class TeamMemberRequest {
    String dialingCode;
    String phone;
    int storeId;

    public TeamMemberRequest(String dialingCode, String phone, int storeId) {
        this.dialingCode = dialingCode;
        this.phone = phone;
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "TeamMemberRequest{" +
                "dialingCode='" + dialingCode + '\'' +
                ", phone='" + phone + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
