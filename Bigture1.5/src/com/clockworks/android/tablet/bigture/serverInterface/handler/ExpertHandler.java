package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ExpertWordWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class ExpertHandler {

	public static ArrayList<ExpertWordWrapper> listExpertsByName(){
		ArrayList<ExpertWordWrapper> results = new ArrayList<ExpertWordWrapper>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListExpertsByNameURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ExpertWordWrapper wrapper = new ExpertWordWrapper();
					wrapper.word = data.getString("initialWord");
					
					JSONArray expertJson = data.getJSONArray("experts");
					ArrayList<SimpleUserEntity> experts = new ArrayList<SimpleUserEntity>();
					wrapper.experts = experts;
					for(int j=0; j < expertJson.length(); j++){
						JSONObject json = expertJson.getJSONObject(j);
						SimpleUserEntity entity = new SimpleUserEntity();
						entity.importData(json);
						experts.add(entity);
						
					}
					results.add(wrapper);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return results;
	}
	
	public static ArrayList<String> listJobs(){
		ArrayList<String> jobs = new ArrayList<String>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListJobsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					String job = data.getString("name");
					jobs.add(job);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return jobs;
	}
	
	public static ArrayList<SimpleUserEntity> findByJob(String jobName){
		ArrayList<SimpleUserEntity> experts = new ArrayList<SimpleUserEntity>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("jobName",jobName);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListExpertsByJobURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					SimpleUserEntity entity = new SimpleUserEntity();
					entity.importData(data);
					experts.add(entity);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return experts;
	}
	
	public static ArrayList<SimpleUserEntity> findByKeyword(String keyword){
		ArrayList<SimpleUserEntity> experts = new ArrayList<SimpleUserEntity>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		if(keyword != null)
			params.add("keyword",keyword);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.FindExpertURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					SimpleUserEntity entity = new SimpleUserEntity();
					entity.importData(data);
					experts.add(entity);
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return experts;
		
		
		
	}
	
	
}
