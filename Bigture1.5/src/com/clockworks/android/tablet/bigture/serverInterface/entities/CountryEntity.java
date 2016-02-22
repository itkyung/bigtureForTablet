package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryEntity implements Parcelable{

	public String id;
	public String code;
	public String name;
	
	
	public CountryEntity(){
		
	}
	
	public CountryEntity(Parcel in){
		readFromParcel(in);
	}
	
	public void importData(JSONObject data) throws Exception{
		if (data.has("id") && !data.isNull("id"))
			this.id  = data.getString("id");
		if (data.has("code") && !data.isNull("code"))
			this.code  = data.getString("code");
		if (data.has("name") && !data.isNull("name"))
			this.name  = data.getString("name");
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(id);
		out.writeString(code);
		out.writeString(name);
	}
	
	protected void readFromParcel(Parcel in){
		id   = in.readString();
		code    = in.readString();
		name      = in.readString();
	}

	public static final Parcelable.Creator<CountryEntity> CREATOR = new Parcelable.Creator<CountryEntity>()
	{
		public CountryEntity createFromParcel(Parcel in){
			return new CountryEntity(in);
		}
		
		public CountryEntity[] newArray(int size){
			return new CountryEntity[size];
		}
	};
	
	
}
