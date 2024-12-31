package com.example.myapplication.model.request;

import com.google.gson.annotations.SerializedName;

public class SubmitRideLogRequest {

    // 模式，1是标准骑，2是时间骑，3是距离骑，其他为比赛id
    private String mode;

    @SerializedName("start_time")
    private Long startTime;

    @SerializedName("end_time")
    private Long endTime;

    private Float distance;

    @SerializedName("user_id")
    private String userId;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
