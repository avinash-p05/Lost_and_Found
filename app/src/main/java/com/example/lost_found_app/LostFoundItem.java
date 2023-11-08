package com.example.lost_found_app;

public class LostFoundItem {
    private String status;

    public LostFoundItem() {
        // Default constructor required for Firebase
    }

    public LostFoundItem(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


