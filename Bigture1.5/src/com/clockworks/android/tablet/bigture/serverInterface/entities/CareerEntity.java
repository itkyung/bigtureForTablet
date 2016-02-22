package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class CareerEntity implements Parcelable{
	public String index;
	public String career;
	public String startTime;
	public String endTime;
	
	public CareerEntity(){
		
	}
	
	public CareerEntity(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject obj) throws Exception
	{
		index      = obj.getString("id");
		career     = obj.getString("career");
		career = career.replaceAll("\\\\n", "\\\n");
		startTime  = obj.getString("startTime");
		if(obj.isNull("endTime"))
			endTime = null;
		else
			endTime    = obj.getString("endTime");
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(index);
		dest.writeString(career);
		dest.writeString(startTime);
		dest.writeString(endTime);
	}
	
	protected void readFromParcel(Parcel in){
		index = in.readString();
		career = in.readString();
		startTime = in.readString();
		endTime = in.readString();
	}
	
	public static final Parcelable.Creator<CareerEntity> CREATOR = new Parcelable.Creator<CareerEntity>()
			{
				public CareerEntity createFromParcel(Parcel in)
				{
					return new CareerEntity(in);
				}
				
				public CareerEntity[] newArray(int size)
				{
					return new CareerEntity[size];
				}
			};
}
