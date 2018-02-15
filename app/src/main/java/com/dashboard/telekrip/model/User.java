package com.dashboard.telekrip.model;


import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String userName;
    private String avatar;
    private String lastMessage;

    public User(int id, String userName, String avatar, String lastMessage) {
        this.id = id;
        this.userName = userName;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
