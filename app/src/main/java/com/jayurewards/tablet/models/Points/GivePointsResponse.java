package com.jayurewards.tablet.models.Points;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GivePointsResponse {

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    @SerializedName("points")
    @Expose
    private int points;

    @SerializedName("point_tally")
    @Expose
    private int pointTally;

    @SerializedName("timeleft")
    @Expose
    private int timeLeft;

    @Override
    public String toString() {
        return "GivePointsResponse{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", points=" + points +
                ", pointTally=" + pointTally +
                ", timeLeft=" + timeLeft +
                '}';
    }
}
