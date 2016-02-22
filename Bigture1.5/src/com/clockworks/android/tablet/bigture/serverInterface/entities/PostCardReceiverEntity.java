package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;

import org.json.JSONObject;

public class PostCardReceiverEntity {
	public String receiverId;
	public String receiverName;
	public String receiverPhotoPath;
	public String receiverCountry;
	public String receiverJob;
	public boolean viewed;
	public Date viewDate;
	
	public void importData(JSONObject json) throws Exception{
		receiverId = json.getString("receiverId");
		receiverName = json.getString("receiverName");
		if(json.has("receiverPhotoPath"))
			receiverPhotoPath = json.getString("receiverPhotoPath");
		receiverCountry = json.getString("receiverCountry");
		if(json.has("receiverJob"))
			receiverJob = json.getString("receiverJob");
		
		viewed = json.getBoolean("viewed");
		if(json.has("viewDate")){
			viewDate = new Date(json.getLong("viewDate"));
		}
	}
}
