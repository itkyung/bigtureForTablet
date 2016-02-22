package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;

import org.json.JSONObject;

public class NoticeEntity {
	public String id;
	public String title;
	public String content;
	public Date created;
	public boolean expand;
	
	public NoticeEntity(){
		expand = false;
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.title = json.getString("title");
		this.content = json.getString("content");
		this.created = new Date(json.getLong("created"));
		
	}
}
