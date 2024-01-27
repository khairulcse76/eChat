package com.example.echat.ui.theme;

public class ChatModel {
    String sms,userID,status;

    public ChatModel() {
    }

    public ChatModel(String sms, String userID, String status) {
        this.sms = sms;
        this.userID = userID;
        this.status = status;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
