package com.dashboard.telekrip.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class OldMessage implements Serializable{

	@SerializedName("sender_phone")
	private String senderPhone;

	@SerializedName("lastMessage")
	private String lastMessage;

	@SerializedName("receiver_name")
	private String receiverName;

	@SerializedName("sender_name")
	private String senderName;

	@SerializedName("avatar")
	private String avatar;

	@SerializedName("receiver_phone")
	private String receiverPhone;

	@SerializedName("key")
	private String key;

	public void setSenderPhone(String senderPhone){
		this.senderPhone = senderPhone;
	}

	public String getSenderPhone(){
		return senderPhone;
	}


	public String getLastMessage(){
		return lastMessage;
	}

	public void setReceiverName(String receiverName){
		this.receiverName = receiverName;
	}

	public String getReceiverName(){
		return receiverName;
	}

	public void setSenderName(String senderName){
		this.senderName = senderName;
	}

	public String getSenderName(){
		return senderName;
	}

	public void setLastMessage(String lastMessage){
		this.lastMessage = lastMessage;
	}



	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setReceiverPhone(String receiverPhone){
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverPhone(){
		return receiverPhone;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	@Override
 	public String toString(){
		return 
			"OldMessage{" + 
			"sender_phone = '" + senderPhone + '\'' + 
			",lastMessage = '" + lastMessage + '\'' + 
			",receiver_name = '" + receiverName + '\'' + 
			",sender_name = '" + senderName + '\'' + 
			",last_message = '" + lastMessage + '\'' + 
			",avatar = '" + avatar + '\'' + 
			",receiver_phone = '" + receiverPhone + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}