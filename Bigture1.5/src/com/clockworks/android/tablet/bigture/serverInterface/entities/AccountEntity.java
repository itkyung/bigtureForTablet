package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.utils.ObjectSerializer;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AccountEntity extends ProfileEntity
{
	public String loginToken;
	public String loginId;

	
	public String email;
	public String facebook;
	public String twitter;
	public String gender;
	public String birthday;
	
	public boolean keepLogin;
	public int resultCode;
	public boolean success;
	public String socialType;
	public String socialId;
	
	//친구정보는 myBigture에서 likes 탭을 볼때에 가져다가 놓는다. 그리고 친구를 추가하면 다시 refresh한다.
	public ArrayList<FriendEntity> likeMes;
	public ArrayList<FriendEntity> likeYous;
	public ArrayList<FriendGroupEntity> friendGroups;
	
	public AccountEntity()
	{
		
	}
	
	public AccountEntity(Parcel in)
	{
		readFromParcel(in);
	}
	
	public void importData(JSONObject data) throws Exception
	{
		super.importData(data);

		if (data.has("loginToken") && !data.isNull("loginToken"))
			this.loginToken  = data.getString("loginToken");
		
		if (data.has("email") && !data.isNull("email"))
			this.email     = data.getString("email");

		if (data.has("loginId") && !data.isNull("loginId"))
			this.loginId   = data.getString("loginId");
		else
			this.loginId = null;

		if (data.has("facebook") && !data.isNull("facebook"))
			this.facebook  = data.getString("facebook");
		else
			this.facebook = null;

		if (data.has("twitter") && !data.isNull("twitter"))
			this.twitter   = data.getString("twitter");
		else
			this.twitter = null;

		if (data.has("gender") && !data.isNull("gender"))
			this.gender   = data.getString("gender");
		else
			this.gender = null;

		if (data.has("birthDay") && !data.isNull("birthDay"))
			this.birthday   = data.getString("birthDay");
		else
			this.birthday = null;
		
		if (data.has("socialType"))
			this.socialType = data.getString("socialType");
		else
			this.socialType = null;
		
		if (data.has("socialId"))
			this.socialId = data.getString("socialId");
		else
			this.socialId = null;
		
	}
	
	public void load(Context context){
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		
		try{
			this.loginToken = preferences.getString("loginToken", null);
			this.loginId  = preferences.getString("loginId", null);
			this.index = preferences.getString("id", null);
			this.isPro = preferences.getBoolean("isPro", false);
			this.email = preferences.getString("email", null);
			this.nickName = preferences.getString("nickName", null);
			this.comment   = preferences.getString("comment", null);
			this.companyName   = preferences.getString("companyName", null);
			this.facebook  = preferences.getString("facebook", null);
			this.twitter   = preferences.getString("twitter", null);
			this.job       = preferences.getString("job", null);
			this.photoPath     = preferences.getString("photoPath", null);
			this.countryName   = preferences.getString("countryName", null);
			this.countryNameKr = preferences.getString("countryNameKr", null);
			this.countryCode = preferences.getString("countryCode", null);
			this.regionId = preferences.getInt("regionId", 0);
			this.regionName    = preferences.getString("regionName", null);
			this.keepLogin = preferences.getBoolean("keepLogin", false);
			
			this.gender    = preferences.getString("gender", null);
			this.birthday  = preferences.getString("birthday", null);
			
			this.socialId = preferences.getString("socialId", null);
			this.socialType = preferences.getString("socialType", null);
			
			try{
				this.likeYous = (ArrayList<FriendEntity>)ObjectSerializer.deserialize(
						preferences.getString("likeYous", ObjectSerializer.serialize(new ArrayList<FriendEntity>())));
				this.friendGroups = (ArrayList<FriendGroupEntity>)ObjectSerializer.deserialize(
						preferences.getString("groups", ObjectSerializer.serialize(new ArrayList<FriendGroupEntity>())));
						
			}catch(Exception e){
				Log.e("Account",e.getMessage());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void store(Context context){
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putString("loginToken", this.loginToken);
		editor.putString("loginId", this.loginId);
		editor.putString("id", this.index);
		editor.putBoolean("isPro", this.isPro);
		editor.putString("email", this.email);
		editor.putString("nickName", this.nickName);
		editor.putString("comment", this.comment);
		editor.putString("companyName", this.companyName);
		editor.putString("facebook", this.facebook);
		editor.putString("twitter", this.twitter);
		editor.putString("job", this.job);
		editor.putString("photoPath", this.photoPath);
		editor.putString("countryName", this.countryName);
		editor.putString("countryNameKr", this.countryNameKr);
		editor.putString("countryCode", this.countryCode);
		editor.putInt("regionId", this.regionId);
		editor.putString("regionName", this.regionName);
		editor.putBoolean("keepLogin", this.keepLogin);
		//if(this.socialType != null)
			editor.putString("socialType", this.socialType);
		
		//if(this.socialId != null)
			editor.putString("socialId", this.socialId);
		
		//if (this.gender != null)
			editor.putString("gender", this.gender);
	//	if (this.birthday != null)
			editor.putString("birthday", this.birthday);
		
	
		
		try{
			if(this.likeYous != null)
				editor.putString("likeYous", ObjectSerializer.serialize(this.likeYous));
			if(this.friendGroups != null)
				editor.putString("groups", ObjectSerializer.serialize(this.friendGroups));
		
		}catch(IOException e){
			Log.e("ACCOUNT", e.getMessage());
		}
		
		editor.commit();
		
	}
	
	public void storePhotoURL(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString("photo", this.photoPath);

		editor.commit();
	}

	public String getProfileImageURL()
	{
		if (photoPath != null && photoPath.length() > 0)
			return ServerStaticVariable.ProfileURL + photoPath;
		else
			return null;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		super.writeToParcel(out, flags);
		
		out.writeString(loginToken);
		out.writeString(loginId);
		out.writeString(email);
		out.writeString(twitter);
		out.writeString(facebook);
		out.writeString(birthday);
		out.writeString(gender);
		out.writeString(socialType);
		out.writeString(socialId);
		out.writeString(nickName);
	}
	
	protected void readFromParcel(Parcel in)
	{
		super.readFromParcel(in);

		loginToken   = in.readString();
		loginId    = in.readString();
		email      = in.readString();
		twitter    = in.readString();
		facebook   = in.readString();
		birthday   = in.readString();
		gender     = in.readString();
		socialType = in.readString();
		socialId = in.readString();
		nickName = in.readString();
	}

	public boolean useSNS(){
		if(socialType == null || "".equals(socialType)){
			return false;
		}
		return true;
	}
	
	public static final Parcelable.Creator<AccountEntity> CREATOR = new Parcelable.Creator<AccountEntity>()
	{
		public AccountEntity createFromParcel(Parcel in)
		{
			return new AccountEntity(in);
		}
		
		public AccountEntity[] newArray(int size)
		{
			return new AccountEntity[size];
		}
	};
}
