package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class ContestWinnerEntity {
	public String index;
	public String photo;
	public String artworkId;
	public String artworkTitle;
	public String artworkComment;
	
	public String winnerId;
	public String winnerName;
	public String winnerPhoto;
	public String winnerCountry;
	public int rank;
	
	public String getArtworkImageURL(){
		return ServerStaticVariable.ImageURL + photo;		
	}
	
	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + photo;
	}
	
	public String getProfileImageURL(){
		if (winnerPhoto != null && winnerPhoto.length() > 0)
			return ServerStaticVariable.ProfileURL + winnerPhoto;
		else
			return null;
	}
	
	
	public void importData(JSONObject jsonObject) throws Exception{
		index = jsonObject.getString("id");
		photo = jsonObject.getString("photo");
		winnerId = jsonObject.getString("winnerId");
		winnerName = jsonObject.getString("winnerName");
		if(jsonObject.has("winnerPhoto"))
			winnerPhoto = jsonObject.getString("winnerPhoto");
		if(jsonObject.has("winnerCountry"))
			winnerCountry = jsonObject.getString("winnerCountry");
		rank = jsonObject.getInt("rank");
		
		artworkId = jsonObject.getString("artworkId");
		artworkTitle = jsonObject.getString("artworkTitle");
		artworkComment = jsonObject.getString("artworkComment");
		
	}
	
}
