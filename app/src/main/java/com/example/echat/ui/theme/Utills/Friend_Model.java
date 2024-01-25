package com.example.echat.ui.theme.Utills;

public class Friend_Model {
    String fullName,username,profession,ProfileImgUrl, status;

    public Friend_Model() {
    }

    public Friend_Model(String fullName, String username, String profession, String profileImgUrl, String status) {
        this.fullName = fullName;
        this.username = username;
        this.profession = profession;
        ProfileImgUrl = profileImgUrl;
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfileImgUrl() {
        return ProfileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        ProfileImgUrl = profileImgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
