package com.dashboard.telekrip.model;

public class Message {

    private String message;
    private Integer sender;
    private String datetime;

    public Message(String message, Integer sender, String datetime) {
        this.message = message;
        this.sender=sender;
        this.datetime=datetime;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

}
