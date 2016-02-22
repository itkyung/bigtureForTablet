package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.MainContentsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotiSettingEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NoticeEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationGroup;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class MainHandler {

	public static MainContentsEntity getMainContents(){
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.MainURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			if (success){
				JSONObject json = object.getJSONObject("data");
				MainContentsEntity entity = new MainContentsEntity();
				entity.importData(json);
				return entity;
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ArrayList<NotificationGroup> listNotifications(){
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListNotificationURL, params);
		ArrayList<NotificationGroup> groups = new ArrayList<NotificationGroup>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			if (success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i = 0; i < datas.length(); i++){
					JSONObject json = datas.getJSONObject(i);
					NotificationGroup group = new NotificationGroup();
					group.importDate(json);
					groups.add(group);
				}
				
				return groups;
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return groups;
	}
	
	public static boolean deleteNotification(String dates){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("createDate",dates);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.DeleteNotificationURL, params);
		
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			return success;
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static NotiSettingEntity loadSettingInfo(){
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.FindSettingURL, params);
		
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			if(success){
				JSONObject data = object.getJSONObject("data");
				NotiSettingEntity entity = new NotiSettingEntity();
				entity.importData(data);
				return entity;
			}
			return null;
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static ArrayList<NoticeEntity> listNotice(String lang){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("lang", lang);
		ArrayList<NoticeEntity> results = new ArrayList<NoticeEntity>();
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListNoticeURL, params);
		
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject json = datas.getJSONObject(i);
					NoticeEntity entity = new NoticeEntity();
					entity.importData(json);
					results.add(entity);
				}
				
				return results;
			}
			return null;
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return results;
		
	}
	
}
