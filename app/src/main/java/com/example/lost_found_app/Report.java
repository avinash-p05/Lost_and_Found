package com.example.lost_found_app;
public class Report {
    private String username; // Change 'userId' to 'username' to store the username
    private String reportType; // "Lost" or "Found"
    private String objectName;
    private String location;
    private String reportDescription;
    private String reportDate;
    private String contact;
    public Report() {
        // Default constructor required for Firebase Realtime Database
    }

    public Report(String username, String reportType, String objectName, String location, String reportDescription,String contact , String reportDate) {
        this.username = username;
        this.reportType = reportType;
        this.objectName = objectName;
        this.location = location;
        this.reportDescription = reportDescription;
        this.reportDate = reportDate;
        this.contact= contact;// Initialize the date field
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
