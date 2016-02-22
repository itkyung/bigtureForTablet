package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.TempImgEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


public class ProfileHandler{
	public static ProfileEntity readProfile(String memberIndex){
		ProfileEntity entity = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("userId",memberIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetProfileURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			
			if (errorNo == 0){
				entity = new ProfileEntity();
				
				JSONObject profileObject = (JSONObject)object.get("data");
				entity.importData(profileObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return entity;
	}

	public static TempImgEntity updateProfileImage(File bitmapFile,String localPath){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("anyFile", bitmapFile);
		params.put("localPath",localPath);
		HttpUtil util = new HttpUtil();
		
		String resultString = util.executeByMultipart(ServerStaticVariable.UploadProfileImageURL, null, params);
		
		try{
			TempImgEntity entity = new TempImgEntity();
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject imgInfo = object.getJSONObject("data");
				entity.id = imgInfo.getString("id");
				entity.localImagePath = imgInfo.getString("localImagePath");
				entity.thumbnailPath = imgInfo.getString("thumbnail");
				return entity;
				
			}else{
				return null;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean deleteProfileImage(String userIndex)
	{
		ProfileEntity entity = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
	//	params.add("userId",memberIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetProfileURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			
			if (errorNo == 0){
				entity = new ProfileEntity();
				
				JSONObject profileObject = (JSONObject)object.get("profile");
				entity.importData(profileObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean updateProfile(String userIndex, String firstName, String lastName, String comment, String job)
	{
		ProfileEntity entity = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
	//	params.add("userId",memberIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetProfileURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			
			if (errorNo == 0){
				entity = new ProfileEntity();
				
				JSONObject profileObject = (JSONObject)object.get("profile");
				entity.importData(profileObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static List<ProfileEntity> findUser(String userIdx, String keyword)
	{
		ProfileEntity entity = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
	//	params.add("userId",memberIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetProfileURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			
			if (errorNo == 0){
				entity = new ProfileEntity();
				
				JSONObject profileObject = (JSONObject)object.get("profile");
				entity.importData(profileObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
