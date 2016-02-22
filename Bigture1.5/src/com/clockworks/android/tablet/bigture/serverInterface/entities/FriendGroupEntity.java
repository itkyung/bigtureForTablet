package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendGroupEntity implements Parcelable,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2339850608910127105L;
	
	public String index;
	public String groupName;
	public int friendCount;
	
	public boolean selected;
	
	public ArrayList<FriendEntity> members;
	
	public FriendGroupEntity(){
		selected = false;
	}
	
	public FriendGroupEntity(Parcel parcel){
		selected = false;
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject object) throws Exception{ 
		this.index = object.getString("id");
		this.groupName = object.getString("groupName");
		this.friendCount = object.getInt("friendCount");
		this.selected = false;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(index);
		dest.writeString(groupName);
		dest.writeInt(friendCount);
		
	}
	
	private void readFromParcel(Parcel in){
		index = in.readString();
		groupName = in.readString();
		friendCount = in.readInt();
	}
	
	public static final Parcelable.Creator<FriendGroupEntity> CREATOR = new Parcelable.Creator<FriendGroupEntity>(){
		public FriendGroupEntity createFromParcel(Parcel in){
			return new FriendGroupEntity(in);
		}
		
		public FriendGroupEntity[] newArray(int size)
		{
			return new FriendGroupEntity[size];
		}
	};
}
