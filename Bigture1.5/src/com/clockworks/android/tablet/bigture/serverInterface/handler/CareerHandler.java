package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


public class CareerHandler{
	
	public static List<CareerEntity> getCareerList(String userIndex)
	{
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("userId",userIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListCareerURL, params);

		List<CareerEntity> careerList = new ArrayList<CareerEntity>();
		
		
		try
		{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			
			if(success){
				
				JSONArray commArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < commArray.length(); i++){
					JSONObject commObject = commArray.getJSONObject(i);
					
					CareerEntity entity = new CareerEntity();
					entity.importData(commObject);
					
					careerList.add(entity);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			careerList = null;
		}

		return careerList;
	}
	
	

	public static boolean deleteCareer(String careerIndex){
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("id",careerIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.DeleteCareerURL, params);
		
		boolean result = false;
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			
			if(success){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

	public static boolean saveCareer(String index, String content, String startTime, String endTime){
		HttpRequestParameters params = new HttpRequestParameters();
		if(index != null)
			params.add("id",index);
		params.add("content",content);
		params.add("startTime",startTime);
		if(endTime != null)
			params.add("endTime",endTime);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.SaveCareerURL, params);
		
		boolean result = false;
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			
			if(success){
				result = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}
}
