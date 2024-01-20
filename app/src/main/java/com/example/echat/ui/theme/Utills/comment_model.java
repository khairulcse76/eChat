package com.example.echat.ui.theme.Utills;


public class comment_model {
    private String comment, profileImgUrl,userName;

    public comment_model() {
    }

    public comment_model(String comment, String profileImgUrl, String userName) {
        this.comment = comment;
        this.profileImgUrl = profileImgUrl;
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
