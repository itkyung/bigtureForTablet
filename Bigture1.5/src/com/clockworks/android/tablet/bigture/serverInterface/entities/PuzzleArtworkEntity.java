package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.common.PuzzlePart;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.utils.DateUtil;



public class PuzzleArtworkEntity{
	public String partId;
	
	public PuzzlePart part;
	public Date started;
	public Date expiredTime;
	public String status;	//ING,COMPLETED,CANCEL
	public String ownerId;
	public String artworkId;
	public String ownerName;
	public String ownerCountry;
	public String ownerPhoto;
	
	
	public String baseSketch;
	
	public String imagePath;
	public String thumbPath;
	
	public String classId;
	public String puzzleId;
	
	public void importData(JSONObject artworkObject) throws Exception{
		this.partId = artworkObject.getString("partId");
		this.puzzleId = artworkObject.getString("puzzleId");
		this.part = PuzzlePart.valueOf(artworkObject.getString("part"));
		
		if (artworkObject.has("artworkId") && !artworkObject.isNull("artworkId")){
			this.artworkId = artworkObject.getString("artworkId");
			this.imagePath = artworkObject.getString("photo");
			this.thumbPath = artworkObject.getString("thumbnail");
		}
		
		if (artworkObject.has("started") && !artworkObject.isNull("started")){
			long date = artworkObject.getLong("started");
			this.started = new Date(date);
		}
		
		if (artworkObject.has("expiredTime")){
			long date = artworkObject.getLong("expiredTime");
			this.expiredTime = new Date(date);
		}
		
	
		this.baseSketch = artworkObject.getString("baseSketch");
		this.status = artworkObject.getString("status");
		
		if (artworkObject.has("ownerId") && !artworkObject.isNull("ownerId"))
			this.ownerId = artworkObject.getString("ownerId");
		
		if (artworkObject.has("classId") && artworkObject.isNull("classId"))
			this.classId = artworkObject.getString("classId");
		
		this.ownerName = artworkObject.getString("ownerName");
		if(artworkObject.has("ownerPhoto"))
			this.ownerPhoto = artworkObject.getString("ownerPhoto");
		
		if(artworkObject.has("ownerCountry"))
			this.ownerCountry = artworkObject.getString("ownerCountry");
	
		
	}

	public String getSketchImageURL(){
		return ServerStaticVariable.ClassPuzzleSketchURL + baseSketch;
	}
	
	public String getArtworkImageURL(){
		return ServerStaticVariable.ImageURL + imagePath;	
	}
	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + thumbPath;
	}
}
