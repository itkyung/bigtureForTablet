package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class StoryPageEntity implements Parcelable{
	public String pageId;
	public String storyId;
	public int pageNo;	//Start with 1
	public String description;
	public ArrayList<StoryArtworkEntity> artworkList;
	
	public StoryPageEntity(){
	}
	
	public StoryPageEntity(Parcel parcel){
		readFromParcel(parcel);
	}

	public void importData(JSONObject obj) throws Exception{
		this.pageId = obj.getString("id");
		this.storyId = obj.getString("storyId");
		this.pageNo    = obj.getInt("pageNo");
		this.description    = obj.getString("description");
	}
	
	public void addArtwork(StoryArtworkEntity artworkEntitity){
		if (artworkList == null)
			artworkList = new ArrayList<StoryArtworkEntity>();

		artworkList.add(artworkEntitity);
	}
	
	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(pageId);
		out.writeString(storyId);
		out.writeInt(pageNo);
		out.writeString(description);
	}
	
	protected void readFromParcel(Parcel in){
		pageId = in.readString();
		storyId = in.readString();
		pageNo    = in.readInt();
		description    = in.readString();
	}
	
	public static final Parcelable.Creator<StoryPageEntity> CREATOR = new Parcelable.Creator<StoryPageEntity>(){
		public StoryPageEntity createFromParcel(Parcel in)
		{
			return new StoryPageEntity(in);
		}
		
		public StoryPageEntity[] newArray(int size)
		{
			return new StoryPageEntity[size];
		}
	};
}
