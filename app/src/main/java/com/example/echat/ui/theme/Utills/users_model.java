package com.example.echat.ui.theme.Utills;

public class users_model {
    String city,country,fullName,username,phoneNumber,profession,profileImage;

    public users_model() {
    }

    public users_model(String city, String country, String fullName, String username, String phoneNumber, String profession, String profileImage) {
        this.city = city;
        this.country = country;
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
        this.profileImage = profileImage;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
