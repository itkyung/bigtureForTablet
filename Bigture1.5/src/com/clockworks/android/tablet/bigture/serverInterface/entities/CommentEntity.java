package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class CommentEntity {
	
	public String id;
	public String comment;
	public String sticker;
	public String ownerId;
	public String ownerName;
	public String ownerCountry;
	public String ownerPhoto;
	public String ownerJob;
	public Date created;
	public boolean friendComment;
	public String ownerCountryKr;
	public CommentEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		if(json.has("comment"))
			this.comment = json.getString("comment");
		if(json.has("sticker"))
			this.sticker = json.getString("sticker");
		this.ownerId = json.getString("ownerId");
		this.ownerName = json.getString("ownerName");
		this.ownerCountry = json.getString("ownerCountry");
		if(json.has("ownerCountryKr"))
			this.ownerCountryKr = json.getString("ownerCountryKr");
		if(json.has("ownerPhoto"))
			this.ownerPhoto = json.getString("ownerPhoto");
		if(json.has("ownerJob"))
			this.ownerJob = json.getString("ownerJob");
		
		this.created = new Date(json.getLong("created"));
		if(json.has("friendComment"))
			this.friendComment = json.getBoolean("friendComment");
		else
			this.friendComment = false;
	}
	
	public String getProfileImageURL(){
		if (ownerPhoto != null && ownerPhoto.length() > 0)
			return ServerStaticVariable.ProfileURL + ownerPhoto;
		else
			return null;
	}
}
