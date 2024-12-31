package com.example.myapplication.model.request;

import com.google.gson.annotations.SerializedName;

public class SignUpCompetitionRequest {

    private String competitionId;

    private String userId;

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
