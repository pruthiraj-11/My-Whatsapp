package com.example.whatsapp.Models;

public class StatusModel {

     String statusthumbnail,username,timestamp;

    public StatusModel(String statusthumbnail, String username, String timestamp) {
        this.statusthumbnail = statusthumbnail;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getStatusthumbnail() {
        return statusthumbnail;
    }

    public void setStatusthumbnail(String statusthumbnail) {
        this.statusthumbnail = statusthumbnail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
