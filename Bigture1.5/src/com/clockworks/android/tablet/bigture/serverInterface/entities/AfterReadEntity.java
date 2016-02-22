package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class AfterReadEntity {
	
	private String id;
	
	private String artworkId;
	
	private String photo;
	
	private String thumbnail;
	
	private String title;

	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.artworkId = json.getString("artworkId");
		this.title = json.getString("title");
		this.photo = json.getString("photo");
		this.thumbnail = json.getString("thumbnail");
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getArtworkId() {
		return artworkId;
	}

	public void setArtworkId(String artworkId) {
		this.artworkId = artworkId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getArtworkImageURL(){
		return ServerStaticVariable.ImageURL + photo;		
	}
	
	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + thumbnail;
	}
	
	
}
