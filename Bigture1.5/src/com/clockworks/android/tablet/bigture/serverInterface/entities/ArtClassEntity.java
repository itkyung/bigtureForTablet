package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

import android.os.Parcel;
import android.os.Parcelable;


public class ArtClassEntity implements Parcelable {
	public String index;
	public String className;
	public String coverImage;
	public String ownerId;
	public String ownerName;
	public String ownerPhoto;
	public String ownerCountry;
	public String ownerJob;
	public boolean opened;
	public boolean collected;
	public boolean joined;
	public String status;
	public String description;
	public Date dStartDate;
	public Date dEndDate;
	
	
	public String startDate;
	public String endDate;
	public CommonCoverEntity coverEntity;
	public String classType;
	public String shareType;
	public ArrayList<FriendEntity> members;
	
	private final DateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
	
	public ArtClassEntity(){
		
	}
	
	public ArtClassEntity(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject json) throws Exception{
		this.index = json.getString("id");
		this.ownerId = json.getString("ownerId");
		this.ownerName = json.getString("ownerName");
		if(json.has("ownerPhoto"))
			this.ownerPhoto = json.getString("ownerPhoto");
		this.ownerCountry = json.getString("ownerCountry");
		if(json.has("ownerJob"))
			this.ownerJob = json.getString("ownerJob");
		this.opened = json.getBoolean("opened");
		this.collected = json.getBoolean("collected");
		this.status = json.getString("status");
		this.className = json.getString("className");
		if(json.has("coverImage"))
			this.coverImage = json.getString("coverImage");
		this.joined = json.getBoolean("joined");
		this.classType = json.getString("classType");
		if(json.has("shareType")){
			this.shareType = json.getString("shareType");
		}
		
		if(json.has("description"))
			this.description = json.getString("description");
		
		if(json.has("startDate")){
			this.dStartDate = fm.parse(json.getString("startDate"));
		}
		
		if(json.has("endDate")){
			this.dEndDate = fm.parse(json.getString("endDate"));
		}
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		dest.writeString(this.index);
		dest.writeString(this.className);
		dest.writeString(this.coverImage);
		dest.writeString(this.ownerId);
		dest.writeString(this.ownerName);
		dest.writeString(this.ownerCountry);
		dest.writeString(this.ownerPhoto);
		dest.writeString(this.ownerJob);
		dest.writeString(this.status);
		dest.writeInt(this.opened ? 1 : 0);
		dest.writeInt(this.collected ? 1 : 0);
		dest.writeInt(this.joined ? 1 : 0);
		dest.writeString(this.description);
		dest.writeLong(this.dStartDate == null ? 0 : dStartDate.getTime());
		dest.writeLong(this.dEndDate == null ? 0 : dEndDate.getTime());
		dest.writeString(this.classType);
		dest.writeString(this.shareType);
	}
	
	public void readFromParcel(Parcel in){
		this.index = in.readString();
		this.className = in.readString();
		this.coverImage = in.readString();
		this.ownerId = in.readString();
		this.ownerName = in.readString();
		this.ownerCountry = in.readString();
		this.ownerPhoto = in.readString();
		this.ownerJob = in.readString();
		this.status = in.readString();
		this.opened = in.readInt() == 1 ? true : false;
		this.collected = in.readInt() == 1 ? true : false;
		this.joined = in.readInt() == 1 ? true : false;
		this.description = in.readString();
		long s = in.readLong();
		this.dStartDate = s == 0 ? null : new Date(s);
		long e = in.readLong();
		this.dEndDate = e == 0 ? null : new Date(e);
		this.classType = in.readString();
		this.shareType = in.readString();
	}

	public String getCoverImageURL(){
		if (this.coverImage != null && this.coverImage.length() > 0)
			return ServerStaticVariable.ClassCoverURL + coverImage;
		else
			return null;
	}
	
	public String getProfileImageURL(){
		if (ownerPhoto != null && ownerPhoto.length() > 0)
			return ServerStaticVariable.ProfileURL + ownerPhoto;
		else
			return null;
	}
	
	public String getClassType(){
		return classType;
	}
	
	public static final Parcelable.Creator<ArtClassEntity> CREATOR = new Parcelable.Creator<ArtClassEntity>()
	{
		public ArtClassEntity createFromParcel(Parcel in)
		{
			return new ArtClassEntity(in);
		}
		
		public ArtClassEntity[] newArray(int size)
		{
			return new ArtClassEntity[size];
		}
	};
}
