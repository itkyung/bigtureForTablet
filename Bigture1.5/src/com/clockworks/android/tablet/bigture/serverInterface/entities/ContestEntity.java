package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;
import java.util.List;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;



import android.os.Parcel;
import android.os.Parcelable;

public class ContestEntity implements Parcelable{
	public String index;
	public String mainTitle;
	public String subTitle;
	public String contents;
	public String logoPath;

	public Date startTime;
	public Date endTime;
	public String status;
	public String winnerId;
	public String winnerName;
	public String winnerPhoto;
	public String winnerCountry;

	public String artworkImagePath;
	
	public List<ArtworkEntity> artworkList;

	public ContestEntity(){
		
	}
	
	public ContestEntity(Parcel parcel){
		readFromParcel(parcel);
	}

	public void importData(JSONObject fameObject) throws Exception{
		index       = fameObject.getString("id");
		mainTitle   = fameObject.getString("title");
		subTitle    = fameObject.getString("subTitle");
		contents = fameObject.getString("contents");
		startTime   = new Date(fameObject.getLong("startTime"));
		endTime     = new Date(fameObject.getLong("endTime"));
		status      = fameObject.getString("status");
				
		if(fameObject.has("logoPath"))
			logoPath    = fameObject.getString("logoPath");
		
		if(fameObject.has("winnerId"))
			winnerId = fameObject.getString("winnerId");
		
		if(fameObject.has("winnerName"))
			winnerName = fameObject.getString("winnerName");
		
		if(fameObject.has("winnerPhoto"))
			winnerPhoto = fameObject.getString("winnerPhoto");
			
		if(fameObject.has("contestImgPath"))
			artworkImagePath = fameObject.getString("contestImgPath");	
	}

	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + artworkImagePath;
	}
	
	public String getWinnerPhotoURL(){
		return ServerStaticVariable.ImageURL + artworkImagePath;
	}
	
	public String getWinnerProfileURL(){
		return ServerStaticVariable.ProfileURL + winnerPhoto;
	}
	
	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(index);
		out.writeString(mainTitle);
		out.writeString(subTitle);
		out.writeString(contents);
		out.writeLong(startTime.getTime());
		out.writeLong(endTime.getTime());
		out.writeString(status);
		out.writeString(winnerId);
		out.writeString(winnerName);
		out.writeString(winnerPhoto);
		out.writeString(winnerCountry);
	}
	
	protected void readFromParcel(Parcel in){
		index = in.readString();
		mainTitle = in.readString();
		subTitle  = in.readString();
		contents = in.readString();
		startTime = new Date(in.readLong());
		endTime = new Date(in.readLong());
		status = in.readString();
		winnerId = in.readString();
		winnerName = in.readString();
		winnerPhoto = in.readString();
		winnerCountry = in.readString();
		
	}
	
	public static final Parcelable.Creator<ContestEntity> CREATOR = new Parcelable.Creator<ContestEntity>()
	{
		public ContestEntity createFromParcel(Parcel in)
		{
			return new ContestEntity(in);
		}
		
		public ContestEntity[] newArray(int size)
		{
			return new ContestEntity[size];
		}
	};
}
