package com.dashboard.telekrip.model;


import com.google.gson.annotations.SerializedName;


public class Message{

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("sender")
	private String sender;

	@SerializedName("text")
	private String text;

	@SerializedName("senderNameSurname")
	private String senderNameSurname;

	public void setDateTime(String dateTime){
		this.dateTime = dateTime;
	}

	public String getDateTime(){
		return dateTime;
	}

	public void setSender(String sender){
		this.sender = sender;
	}

	public String getSender(){
		return sender;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setSenderNameSurname(String senderNameSurname){
		this.senderNameSurname = senderNameSurname;
	}

	public String getSenderNameSurname(){
		return senderNameSurname;
	}

	@Override
 	public String toString(){
		return 
			"Message{" + 
			"dateTime = '" + dateTime + '\'' + 
			",sender = '" + sender + '\'' + 
			",text = '" + text + '\'' + 
			",senderNameSurname = '" + senderNameSurname + '\'' + 
			"}";
		}
}