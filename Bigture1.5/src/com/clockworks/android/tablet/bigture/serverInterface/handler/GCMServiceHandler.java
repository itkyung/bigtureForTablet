package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.HashMap;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


public class GCMServiceHandler
{
	public static boolean updateGCMRegistrationId(String userId, String regId)
	{
		boolean result = false;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("osType","Android");
		
		if (regId == null)
			params.add("userId",userId);
		else{
			params.add("userId", userId);
			params.add("regId",regId);
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GcmRegistURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");

			result = (errorNo == 0);
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}

}
