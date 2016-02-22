package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

import android.os.Parcel;
import android.os.Parcelable;

public class PostCardEntity implements Parcelable{
	public String id;
	public String ownerId;
	public String ownerName;
	public String ownerPhoto;
	public String ownerCountry;
	public String ownerJob;
	public Date created;
	public String comment;
	public int receiverCount;
	public String firstReceiverName;
	public String photoPath;
	public String photoTitle;
	public String photoComment;
	public String photoOwnerName;
	public Date photoDate;
	public boolean opened;
	public String receiverEmails;
	public boolean viewed;
	public Date viewDate;
	
	
	public ArrayList<PostCardReceiverEntity> receivers;
	
	public PostCardEntity(){
		
	}
	
	public PostCardEntity(Parcel in){
		readFromParcel(in);
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.ownerId = json.getString("ownerId");
		this.ownerName = json.getString("ownerName");
		if(json.has("ownerPhoto"))
			this.ownerPhoto = json.getString("ownerPhoto");
		this.ownerCountry = json.getString("ownerCountry");
		this.created = new Date(json.getLong("created"));
		if(json.has("comment"))
			this.comment = json.getString("comment");
		if(json.has("ownerJob"))
			this.ownerJob = json.getString("ownerJob");
		this.receiverCount = json.getInt("receiverCount");
		this.firstReceiverName = json.getString("firstReceiverName");
		this.photoPath = json.getString("photoPath");
		this.photoTitle = json.getString("photoTitle");
		this.photoComment = json.getString("photoComment");
		this.photoOwnerName = json.getString("photoOwnerName");
		this.photoDate = new Date(json.getLong("photoDate"));
		this.opened = json.getBoolean("opened");
		this.receiverEmails = json.getString("receiverEmails");
		this.viewed = json.getBoolean("viewed");
		if(json.has("viewDate"))
			this.viewDate = new Date(json.getLong("viewDate"));
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void readFromParcel(Parcel in){
		this.id = in.readString();
		this.ownerId = in.readString();
		this.ownerName = in.readString();
		this.ownerPhoto = in.readString();
		this.ownerCountry = in.readString();
		this.ownerJob = in.readString();
		this.created = new Date(in.readLong());
		this.comment = in.readString();
		this.receiverCount = in.readInt();
		this.firstReceiverName = in.readString();
		this.photoPath = in.readString();
		this.photoTitle = in.readString();
		this.photoComment = in.readString();
		this.photoOwnerName = in.readString();
		this.photoDate = new Date(in.readLong());
		this.opened = in.readInt() == 1 ? true : false;
		this.receiverEmails = in.readString();
		this.viewed = in.readInt() == 1 ? true : false;
		this.viewDate = new Date(in.readLong());
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.ownerId);
		dest.writeString(this.ownerName);
		dest.writeString(this.ownerPhoto);
		dest.writeString(this.ownerCountry);
		dest.writeString(this.ownerJob);
		dest.writeLong(this.created.getTime());
		dest.writeString(this.comment);
		dest.writeInt(this.receiverCount);
		dest.writeString(this.firstReceiverName);
		dest.writeString(this.photoPath);
		dest.writeString(this.photoTitle);
		dest.writeString(this.photoComment);
		dest.writeString(this.photoOwnerName);
		dest.writeLong(this.photoDate.getTime());
		dest.writeInt(this.opened ? 1 : 0);
		dest.writeString(this.receiverEmails);
		dest.writeInt(this.viewed ? 1 : 0);
		dest.writeLong(this.viewDate == null ? 0 : this.viewDate.getTime());
	}
	

	public static final Parcelable.Creator<PostCardEntity> CREATOR = new Parcelable.Creator<PostCardEntity>(){
		public PostCardEntity createFromParcel(Parcel in){
			return new PostCardEntity(in);
		}
		
		public PostCardEntity[] newArray(int size){
			return new PostCardEntity[size];
		}
	};
	
	public String getArtworkImageURL(){
		return ServerStaticVariable.ImageURL + photoPath;		
	}
	
	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + photoPath;
	}
	
	public String getOwnerProfileURL(){
		return ServerStaticVariable.ProfileURL + ownerPhoto;
	}
	
}
