package com.dashboard.telekrip.model;


import com.google.gson.annotations.SerializedName;


public class Message {

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("sender")
    private String sender;

    @SerializedName("text")
    private String text;

    @SerializedName("senderNameSurname")
    private String senderNameSurname;

    @SerializedName("isSave")
    private boolean isSave;

    public Message() {
        this.isSave=false;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSenderNameSurname(String senderNameSurname) {
        this.senderNameSurname = senderNameSurname;
    }

    public String getSenderNameSurname() {
        return senderNameSurname;
    }

    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    @Override
    public String toString() {
        return
                "Message{" +
                        "dateTime = '" + dateTime + '\'' +
                        ",sender = '" + sender + '\'' +
                        ",text = '" + text + '\'' +
                        ",senderNameSurname = '" + senderNameSurname + '\'' +
                        "}";
    }
}