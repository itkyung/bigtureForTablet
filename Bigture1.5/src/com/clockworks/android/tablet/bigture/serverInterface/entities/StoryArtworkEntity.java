package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class StoryArtworkEntity extends ArtworkEntity {
	public String pageId;
	public int pageNo;
	
	public StoryArtworkEntity(){
		
	}
	
	public StoryArtworkEntity(Parcel parcel){
		this.readFromParcel(parcel);
	}
	
	public void importData(JSONObject object) throws Exception{
		super.importData(object);

		this.pageId  = object.getString("pageId");
		if(object.has("pageNo")){
			this.pageNo = object.getInt("pageNo");
		}
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		super.writeToParcel(out, flags);
		
		out.writeString(pageId);
		out.writeInt(pageNo);
	}
	
	protected void readFromParcel(Parcel in){
		super.readFromParcel(in);
		
		pageId = in.readString();
		pageNo = in.readInt();
	}

	public static final Parcelable.Creator<StoryArtworkEntity> CREATOR = new Parcelable.Creator<StoryArtworkEntity>()
	{
		public StoryArtworkEntity createFromParcel(Parcel in)
		{
			return new StoryArtworkEntity(in);
		}
		
		public StoryArtworkEntity[] newArray(int size)
		{
			return new StoryArtworkEntity[size];
		}
	};
}
