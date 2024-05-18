package com.example.lost_found_app;

import java.io.Serializable;

public class Report implements Serializable {
    private String username;
    private String reportType;
    private String objectName;
    private String location;
    private String reportDescription;
    private String reportDate;
    private String contact;
    private String imageUrl;
    private boolean isClaimed;

    private boolean claimed;
    private  String key;
    private String claimedByUserId;

    // ... existing methods ...

    public boolean isClaimed() {
        return claimed;
    }

    public void setClaimed(boolean claimed) {
        this.claimed = claimed;
    }

    public String getClaimedByUserId() {
        return claimedByUserId;
    }

    public void setClaimedByUserId(String claimedByUserId) {
        this.claimedByUserId = claimedByUserId;
    }

    public Report() {
        // Default constructor required for Firebase Realtime Database
    }


    public void setkey(String key){
        this.key = key;
    }
    public Report(String username, String reportType, String objectName, String location, String reportDescription, String contact, String reportDate) {
        this.username = username;
        this.reportType = reportType;
        this.objectName = objectName;
        this.location = location;
        this.reportDescription = reportDescription;
        this.reportDate = reportDate;
        this.contact = contact;
    }

    public Report( String username, String reportType, String objectName, String location, String reportDescription, String contact, String reportDate, String imageUrl) {
        this(username, reportType, objectName, location, reportDescription, contact, reportDate);
        this.imageUrl = imageUrl;
    }

    // Getter and setter methods for imageUrl
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    // Getter methods for all fields

    public String getUsername() {
        return username;
    }
    public String getReportDate() {
        return reportDate;
    }

    public String getReportType() {
        return reportType;
    }

    public String getObjectName() {
        return objectName;
    }
    public String getkey() {
        return key != null ? key : "";
    }

    public String getLocation() {
        return location;
    }
    public String getContact() {
        return contact;
    }

    public String getReportDescription() {
        return reportDescription;
    }

}
