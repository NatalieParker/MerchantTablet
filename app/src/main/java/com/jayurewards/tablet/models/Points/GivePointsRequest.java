package com.jayurewards.tablet.models.Points;

public class GivePointsRequest {

    private final String dialingCode;
    private final String phone;
    private final int storeId;
    private final String company;
    private final long points;
    private final double spent;
    private final String method;
    private final String type;
    private final int teamId;
    private final int adminLevel;
    private final int pointTimeout;
    private final String day;
    private final String time;

    public GivePointsRequest(String dialingCode, String phone, int storeId, String company, long points, double spent, String method, String type, int teamId, int adminLevel, int pointTimeout, String day, String time) {
        this.dialingCode = dialingCode;
        this.phone = phone;
        this.storeId = storeId;
        this.company = company;
        this.points = points;
        this.spent = spent;
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
                "dialingCode='" + dialingCode + '\'' +
                ", phone='" + phone + '\'' +
                ", storeId=" + storeId +
                ", company='" + company + '\'' +
                ", points=" + points +
                ", spent=" + spent +
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
