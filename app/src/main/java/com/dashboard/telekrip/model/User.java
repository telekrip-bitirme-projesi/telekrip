package com.dashboard.telekrip.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("first_name")
    private String name;
    @SerializedName("last_name")
    private String surname;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("phone")
    private String phoneNumber;

    public User(String name, String surname, String avatar, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
