package com.clockworks.android.tablet.bigture.serverInterface.entities;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;



import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author mobility_macpro
 *
 */
public class ProfileEntity implements Parcelable
{
	public String index;
	public String nickName;
	public String comment;
	public String photoPath;
	
	public boolean isPro = false;
	public String companyName;
	public String job;
	
	public int regionId;
	public String regionName;
	public String countryName;
	public String countryCode;
	public String countryNameKr;
	
	public boolean friend;
	public int friendStatus;
	
	public boolean selected;
	public boolean likeYou;
	public int groupCount;
	
	public int loveCount;
	public int awesomeCount;
	public int wowCount;
	public int funCount;
	public int fantasticCount;
	
	
	public ProfileEntity(){
		
	}
	public ProfileEntity(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject profileObject) throws Exception{
		index     = profileObject.getString("id");
		nickName = profileObject.getString("nickName");
		
		if (profileObject.has("isFriend") && !profileObject.isNull("isFriend"))
			friend = profileObject.getInt("isFriend") == 1;
		
		if (profileObject.has("likeYou") && !profileObject.isNull("likeYou"))
			likeYou = profileObject.getBoolean("likeYou");
			
		if (profileObject.has("comment") && !profileObject.isNull("comment"))
			comment   = profileObject.getString("comment");
		
		if (!profileObject.isNull("photoPath"))
			photoPath = profileObject.getString("photoPath");
		
		if (profileObject.has("pro"))
			isPro = profileObject.getBoolean("pro");
		
		if (profileObject.has("companyName") && !profileObject.isNull("companyName"))
			companyName = profileObject.getString("companyName");
		
		if (profileObject.has("job") && !profileObject.isNull("job"))
			job = profileObject.getString("job");

		if (profileObject.has("regionId"))
			regionId = profileObject.getInt("regionId");
		
		if (profileObject.has("regionName"))
			regionName      = profileObject.getString("regionName");
		
		countryName     = profileObject.getString("countryName");
		if (profileObject.has("countryNameKr"))
			countryNameKr = profileObject.getString("countryNameKr");
		
		if (profileObject.has("countryCode"))
			countryCode = profileObject.getString("countryCode");
		if (profileObject.has("groupCount"))
			groupCount = profileObject.getInt("groupCount");
		else
			groupCount = 0;
		
		if (profileObject.has("loveCount"))
			loveCount = profileObject.getInt("loveCount");
		else
			loveCount = 0;
		
		if (profileObject.has("awesomeCount"))
			awesomeCount = profileObject.getInt("awesomeCount");
		else
			awesomeCount = 0;
		
		if (profileObject.has("wowCount"))
			wowCount = profileObject.getInt("wowCount");
		else
			wowCount = 0;
		
		if (profileObject.has("funCount"))
			funCount = profileObject.getInt("funCount");
		else 
			funCount = 0;
		
		if (profileObject.has("fantasticCount"))
			fantasticCount = profileObject.getInt("fantasticCount");
		else
			fantasticCount = 0;
		
	}

	public String getProfileImageURL(){
		if (photoPath != null && photoPath.length() > 0)
			return ServerStaticVariable.ProfileURL + photoPath;
		else
			return null;
	}
	
	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeString(index);
		out.writeString(nickName);
		out.writeString(comment);
		out.writeString(photoPath);
		out.writeInt((isPro ? 1 : 0));
		out.writeInt(friendStatus);
		out.writeString(companyName);
		out.writeString(job);
		out.writeInt(regionId);
		out.writeString(regionName);
		out.writeString(countryName);
		out.writeString(countryNameKr);
		out.writeString(countryCode);
		out.writeInt((likeYou ? 1 : 0));
		out.writeInt(groupCount);
		out.writeInt(loveCount);
		out.writeInt(awesomeCount);
		out.writeInt(wowCount);
		out.writeInt(funCount);
		out.writeInt(fantasticCount);
	}
	
	protected void readFromParcel(Parcel in){
		index       = in.readString();
		nickName   = in.readString();
		comment     = in.readString();
		photoPath       = in.readString();
		isPro       = in.readInt() == 1 ? true : false;
		friendStatus= in.readInt();
		companyName     = in.readString();
		job         = in.readString();
		regionId   = in.readInt();
		regionName      = in.readString();
		countryName     = in.readString();
		countryNameKr = in.readString();
		countryCode = in.readString();
		likeYou = in.readInt() == 1 ? true : false;
		groupCount = in.readInt();
		loveCount = in.readInt();
		awesomeCount = in.readInt();
		wowCount = in.readInt();
		funCount = in.readInt();
		fantasticCount = in.readInt();
	}

	public static final Parcelable.Creator<ProfileEntity> CREATOR = new Parcelable.Creator<ProfileEntity>(){
		public ProfileEntity createFromParcel(Parcel in){
			return new ProfileEntity(in);
		}
		
		public ProfileEntity[] newArray(int size){
			return new ProfileEntity[size];
		}
	};
}
