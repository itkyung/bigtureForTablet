package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class SimpleUserEntity implements Parcelable{
	public String index;
	public String name;
	public String photo;
	public String jobName;
	public String country;
	
	public SimpleUserEntity(){
		
	}
	
	public SimpleUserEntity(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject jsonObject) throws Exception{
		this.index = jsonObject.getString("id");
		if(jsonObject.has("name"))
			this.name = jsonObject.getString("name");
		else if(jsonObject.has("nickName"))
			this.name = jsonObject.getString("nickName");
		
		if(jsonObject.has("photo"))
			this.photo = jsonObject.getString("photo");
		else if(jsonObject.has("photoPath"))
			this.photo = jsonObject.getString("photoPath");
			
		if(jsonObject.has("jobName"))
			this.jobName = jsonObject.getString("jobName");
		else if(jsonObject.has("job"))
			this.jobName = jsonObject.getString("job");
		
		if(jsonObject.has("country"))
			this.country = jsonObject.getString("country");
		else if(jsonObject.has("countryName"))
			this.country = jsonObject.getString("countryName");
	}
	
	public String getProfileImageURL(){
		if (photo != null && photo.length() > 0)
			return ServerStaticVariable.ProfileURL + photo;
		else
			return null;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(index);
		dest.writeString(name);
		dest.writeString(photo);
		dest.writeString(country);
		dest.writeString(jobName);
		
	}
	
	protected void readFromParcel(Parcel in){
		index       = in.readString();
		name   = in.readString();
		photo = in.readString();
		country = in.readString();
		jobName = in.readString();
	}
	
	public static final Parcelable.Creator<SimpleUserEntity> CREATOR = new Parcelable.Creator<SimpleUserEntity>(){
		public SimpleUserEntity createFromParcel(Parcel in)
		{
			return new SimpleUserEntity(in);
		}
		
		public SimpleUserEntity[] newArray(int size)
		{
			return new SimpleUserEntity[size];
		}
	};
}
