package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class CommonCoverEntity {
	public String coverImageId;		//coverFromArtworks가 true이면 이것은 artwork의 id이다.
	public String coverImagePath;
	public String coverThumbPath;
	public boolean coverFromArtworks;
	public String localImagePath;
	
	
	
	//이경우에는 무조건 업로드이후이다. 즉 coverFormArtworks가 false이다. 
	public void importData(JSONObject json) throws Exception{
		this.coverImageId = json.getString("id");
		this.coverImagePath = json.getString("photo");
		this.coverThumbPath = json.getString("thumbnail");
		this.coverFromArtworks = false;
	}
	
	public String getCoverThumbPath(){
		if(coverFromArtworks){
			return ServerStaticVariable.ThumbURL + coverThumbPath;
		}else{
			return ServerStaticVariable.ClassCoverURL + coverThumbPath;
		}
		
	}
	public String getCoverPath(){
		if(coverFromArtworks){
			return ServerStaticVariable.ImageURL + coverImagePath;	
		}else{
			return ServerStaticVariable.ClassCoverURL + coverImagePath;
		}
	}
}
