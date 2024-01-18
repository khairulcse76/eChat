package com.example.echat.ui.theme.Utills;

public class Post_Model {

    String fullName,userName,postDate,postDescription,post_img_url,user_profile_img;

    public Post_Model() {
    }

    public Post_Model(String fullName, String userName, String postDate, String postDescription, String post_img_url, String user_profile_imgUrl) {
        this.fullName = fullName;
        this.userName = userName;
        this.postDate = postDate;
        this.postDescription = postDescription;
        this.post_img_url = post_img_url;
        this.user_profile_img = user_profile_imgUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPost_img_url() {
        return post_img_url;
    }

    public void setPost_img_url(String post_img_url) {
        this.post_img_url = post_img_url;
    }

    public String getUser_profile_img() {
        return user_profile_img;
    }

    public void setUser_profile_img(String user_profile_img) {
        this.user_profile_img = user_profile_img;
    }
}
