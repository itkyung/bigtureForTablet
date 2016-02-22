package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendEntity implements Parcelable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3696248207805388129L;
	
	
	public String index;
	public String nickName;
	public String photoPath;
	public String country;
	public String countryKr;
	public String region;
	public Long regionId;
	public String gender;
	public boolean pro;
	public String job;
	public boolean alreadyLike;
	
	public boolean selected;
	public boolean newAdded;
	
	public FriendEntity(){
		selected = false;
		newAdded = false;
	}
	
	public FriendEntity(FriendEntity f){
		index = f.index;
		nickName = f.nickName;
		photoPath = f.photoPath;
		country = f.country;
		countryKr = f.countryKr;
		region = f.region;
		regionId = f.regionId;
		gender = f.gender;
		pro = f.pro;
		job = f.job;
		alreadyLike = f.alreadyLike;
		selected = f.selected;
		
		newAdded = false;
	}
	
	public FriendEntity(Parcel parcel){
		selected = false;
		readFromParcel(parcel);
		newAdded = false;
	}
	
	public void importData(JSONObject profileObject) throws Exception{
		selected = false;
		index     = profileObject.getString("id");
		nickName = profileObject.getString("nickName");
		if(profileObject.isNull("photoPath"))
			photoPath = null;
		else
			photoPath = profileObject.getString("photoPath");
		
		if(profileObject.has("country"))
			country = profileObject.getString("country");
		if(profileObject.has("countryKr"))
			countryKr = profileObject.getString("countryKr");
		
		region = profileObject.getString("region");
		regionId = profileObject.getLong("regionId");
		gender = profileObject.getString("gender");
		pro = profileObject.getBoolean("pro");
		if(profileObject.isNull("job"))
			job = null;
		else
			job = profileObject.getString("job");
		alreadyLike = profileObject.getBoolean("alreadyLike");
	}



	@Override
	public int describeContents() {
		
		return 0;
	}

	protected void readFromParcel(Parcel in){
		index       = in.readString();
		nickName   = in.readString();
		photoPath = in.readString();
		country = in.readString();
		countryKr = in.readString();
		region = in.readString();
		regionId = in.readLong();
		gender = in.readString();
		pro = in.readInt() == 1 ? true : false;
		job = in.readString();
		alreadyLike = in.readInt() == 1 ? true : false;
	}
	
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(index);
		dest.writeString(nickName);
		dest.writeString(photoPath);
		dest.writeString(country);
		dest.writeString(countryKr);
		dest.writeString(region);
		dest.writeLong(regionId);
		dest.writeString(gender);
		dest.writeInt(pro ? 1 : 0);
		dest.writeString(job);
		dest.writeInt(alreadyLike ? 1 : 0);
	}
	
	public static final Parcelable.Creator<FriendEntity> CREATOR = new Parcelable.Creator<FriendEntity>(){
		public FriendEntity createFromParcel(Parcel in)
		{
			return new FriendEntity(in);
		}
		
		public FriendEntity[] newArray(int size)
		{
			return new FriendEntity[size];
		}
	};
}
