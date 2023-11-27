package com.example.lost_found_app;

// com.example.lost_found_app.ClaimedReport.java
public class ClaimedReport {
    private String userId;
    private String reportId;

    public ClaimedReport() {
        // Default constructor required for Firebase Realtime Database
    }

    public ClaimedReport(String userId, String reportId) {
        this.userId = userId;
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReportId() {
        return reportId;
    }
}

