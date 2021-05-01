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

    @SerializedName("is_anonymous")
    @Expose
    private int isAnonymous;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPointTally() {
        return pointTally;
    }

    public void setPointTally(int pointTally) {
        this.pointTally = pointTally;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    @Override
    public String toString() {
        return "GivePointsResponse{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", points=" + points +
                ", pointTally=" + pointTally +
                ", timeLeft=" + timeLeft +
                ", isAnonymous=" + isAnonymous +
                '}';
    }
}
