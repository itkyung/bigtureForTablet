package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommentWrapperEntity {
	
	public boolean alreadySendSticker;
	public String sendedSticker;
	public Date sendedDate;
	
	public ArrayList<CommentEntity> comments;
	
	
	public CommentWrapperEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		this.alreadySendSticker = json.getBoolean("alreadySendSticker");
		if(json.has("sendedSticker")){
			this.sendedSticker = json.getString("sendedSticker");
		}
		if(json.has("sendedDate")){
			this.sendedDate = new Date(json.getLong("sendedDate"));
		}
		
		JSONArray datas = json.getJSONArray("comments");
		
		this.comments = new ArrayList<CommentEntity>();
		
		for(int i = 0;i <  datas.length(); i++){
			JSONObject data = datas.getJSONObject(i);
			CommentEntity comment = new CommentEntity();
			comment.importData(data);
			this.comments.add(comment);
		}
	}
}
