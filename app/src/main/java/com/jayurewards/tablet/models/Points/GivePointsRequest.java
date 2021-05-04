package com.jayurewards.tablet.models.Points;

public class GivePointsRequest {

    private final String countryCode;
    private final String phone;
    private final int storeId;
    private final String company;
    private final long points;
    private final String method;
    private final String type;
    private final int teamId;
    private final int adminLevel;
    private final int pointTimeout;
    private final String day;
    private final String time;

    public GivePointsRequest(String countryCode, String phone, int storeId, String company, long points, String method, String type, int teamId, int adminLevel, int pointTimeout, String day, String time) {
        this.countryCode = countryCode;
        this.phone = phone;
        this.storeId = storeId;
        this.company = company;
        this.points = points;
        this.method = method;
        this.type = type;
        this.teamId = teamId;
        this.adminLevel = adminLevel;
        this.pointTimeout = pointTimeout;
        this.day = day;
        this.time = time;
    }

    @Override
    public String toString() {
        return "GivePointsRequest{" +
                "countryCode='" + countryCode + '\'' +
                ", phone='" + phone + '\'' +
                ", storeId=" + storeId +
                ", company='" + company + '\'' +
                ", points=" + points +
                ", method='" + method + '\'' +
                ", type='" + type + '\'' +
                ", teamId=" + teamId +
                ", adminLevel=" + adminLevel +
                ", pointTimeout=" + pointTimeout +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
