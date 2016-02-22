package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

import android.os.Parcel;
import android.os.Parcelable;

public class StoryEntity implements Parcelable{
	public String storyId;
	public String title;
	public String desc;
	public String coverImage;
	public boolean hot;
	public String status;
	public boolean afterReading;
	public int pageCount;
	public Date startDate;
	public Date endDate;
	public int memberCount;
	public boolean didItAll;
	public boolean joined;
	public boolean collected;
	public int loveCount;
	public int awesomeCount;
	public int wowCount;
	public int funCount;
	public int fantasticCount;
	
	public ArrayList<StoryPageEntity> pages;
	
	public String ownerId;
	public String ownerName;
	public String ownerPhoto;
	public String ownerJob;
	public String ownerCountry;
	
	public String localCoverImagePath;

	public CommonCoverEntity coverEntity;

	public StoryEntity(){
		pageCount = 0;
		loveCount = 0;
		awesomeCount = 0;
		wowCount = 0;
		funCount = 0;
		fantasticCount = 0;
		
	}
	
	public StoryEntity(Parcel parcel){
		readFromParcel(parcel);
	}

	public void importData(JSONObject obj) throws Exception{
		this.storyId    = obj.getString("id");
		this.title      = obj.getString("title");
		if(obj.has("description"))
			this.desc = obj.getString("description");
		if(obj.has("startDate"))
			this.startDate  = new Date(obj.getLong("startDate"));
		if(obj.has("endDate"))
			this.endDate    = new Date(obj.getLong("endDate"));
		if(obj.has("coverImage"))
			this.coverImage = obj.getString("coverImage");
		this.afterReading = obj.getBoolean("afterReading");
		
		this.memberCount = obj.getInt("memberCount");
		this.status = obj.getString("status");
		this.joined = obj.getBoolean("joined");
		this.collected = obj.getBoolean("collected");
		this.didItAll = obj.getBoolean("didItAll");
		this.hot = obj.getBoolean("hot");
		this.loveCount = obj.getInt("loveCount");
		this.awesomeCount = obj.getInt("awesomeCount");
		this.wowCount = obj.getInt("wowCount");
		this.funCount = obj.getInt("funCount");
		this.fantasticCount = obj.getInt("fantasticCount");
		this.pageCount = obj.getInt("pageCount");
		
		this.ownerId        = obj.getString("ownerId");
		this.ownerName = obj.getString("ownerName");
		if(obj.has("ownerPhoto"))
			this.ownerPhoto       = obj.getString("ownerPhoto");
		if(obj.has("ownerJob"))
			this.ownerJob = obj.getString("ownerJob");
		this.ownerCountry = obj.getString("ownerCountry");
	}
	
	public String getCoverImageURL(){
		if (this.coverImage != null && this.coverImage.length() > 0)
			return ServerStaticVariable.StoryCoverURL + coverImage;
		else
			return null;
	}
	
	public String getProfileImageURL(){
		if (ownerPhoto != null && ownerPhoto.length() > 0)
			return ServerStaticVariable.ProfileURL + ownerPhoto;
		else
			return null;
	}	
	
	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(storyId);
		out.writeString(title);
		out.writeString(desc);
		out.writeString(coverImage);
		out.writeInt(afterReading ? 1 : 0);
		out.writeInt((hot ? 1 : 0));
		out.writeInt(pageCount);
		out.writeLong(startDate == null ? 0 :startDate.getTime());
		out.writeLong(endDate == null ? 0 : endDate.getTime());
		out.writeString(ownerId);
		out.writeString(ownerName);
		out.writeString(ownerPhoto);
		out.writeInt(loveCount);
		out.writeInt(awesomeCount);
		out.writeInt(wowCount);
		out.writeInt(funCount);
		out.writeInt(fantasticCount);
		out.writeInt(didItAll ? 1 : 0);
		out.writeInt(joined ? 1 : 0);
		out.writeInt(collected ? 1 : 0);
		out.writeString(ownerJob);
		out.writeString(ownerCountry);
		out.writeInt(memberCount);
	}
	
	protected void readFromParcel(Parcel in){
		storyId = in.readString();
		title   = in.readString();
		desc    = in.readString();
		coverImage = in.readString();
		afterReading = in.readInt() == 1 ? true : false;
		hot   = in.readInt() == 1 ? true : false;
	
		pageCount = in.readInt();
		long s = in.readLong();
		startDate = s == 0 ? null :  new Date(s);
		long e = in.readLong();
		endDate   = e == 0 ? null : new Date(e);
		ownerId   = in.readString();
		ownerName = in.readString();
		ownerPhoto  = in.readString();
		loveCount  = in.readInt();
		awesomeCount = in.readInt();
		wowCount = in.readInt();
		funCount = in.readInt();
		fantasticCount = in.readInt();
		
		didItAll = in.readInt() == 1 ? true : false;
		joined = in.readInt() == 1 ? true : false;
		collected = in.readInt() == 1 ? true : false;
		ownerJob = in.readString();
		ownerCountry = in.readString();
		memberCount = in.readInt();
	}
	
	public static final Parcelable.Creator<StoryEntity> CREATOR = new Parcelable.Creator<StoryEntity>()
	{
		public StoryEntity createFromParcel(Parcel in)
		{
			return new StoryEntity(in);
		}
		
		public StoryEntity[] newArray(int size)
		{
			return new StoryEntity[size];
		}
	};
}
