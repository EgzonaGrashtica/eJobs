package com.fiek.ejobs.models;

public class FavoriteModel {
    private String UserId;
    private String JobId;

    public FavoriteModel(String userId, String jobId) {
        UserId = userId;
        JobId = jobId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }
}
