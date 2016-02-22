package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AfterReadEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageWrapper;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


import android.util.Log;

public class StoryHandler{
	public static boolean saveStory(StoryEntity entity){
		HttpRequestParameters params = new HttpRequestParameters();
		if(entity.storyId != null)
			params.add("id",entity.storyId);
		params.add("title",entity.title);
		
		if(entity.coverEntity != null){
			params.add("coverImageId",entity.coverEntity.coverImageId);
			params.add("coverFromArtwork", (entity.coverEntity.coverFromArtworks == true) ? "true" : "false");
		}
		
		if(entity.pages != null){
			int i = 0;
			for(StoryPageEntity page : entity.pages){
				if(page.pageId != null){
					params.add("pages[" + i + "].pageId",page.pageId);
				}
				params.add("pages[" + i + "].pageNo",page.pageNo+"");
				params.add("pages[" + i + "].description",page.description);
				i++;
			}
		}
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.SaveStoryURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
		
		
	}
		
	public static boolean deleteStory(String storyId){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.DeleteStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("deleteStory", e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean shareStory(StoryEntity entity){
		HttpRequestParameters params = new HttpRequestParameters();
		if(entity.storyId != null)
			params.add("id",entity.storyId);
		params.add("title",entity.title);
		
		if(entity.coverEntity != null){
			params.add("coverImageId",entity.coverEntity.coverImageId);
			params.add("coverFromArtwork", (entity.coverEntity.coverFromArtworks == true) ? "true" : "false");
		}
		
		if(entity.pages != null){
			int i = 0;
			for(StoryPageEntity page : entity.pages){
				if(page.pageId != null){
					params.add("pages[" + i + "].pageId",page.pageId);
				}
				params.add("pages[" + i + "].pageNo",page.pageNo+"");
				params.add("pages[" + i + "].description",page.description);
				i++;
			}
		}
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ShareStoryURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
//	
//	public static boolean addStoryPage(StoryPageEntity pageEntity)
//	{
//		boolean result = false;
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		JSONArray array = new JSONArray();
//		array.put(pageEntity.storyId);
//		array.put(pageEntity.page);
//		array.put(pageEntity.desc);
//		
//		String paramString = array.toString();
//		paramString = paramString.replace("\\", "\\\\")
//				.replace("\"", "\\\"")
//				.replace("\r", "\\r")
//				.replace("\n", "\\n");
//
//		params.put("class", InterfaceProtocol.BigtureStory.ClassName);
//		params.put("function", InterfaceProtocol.BigtureStory.FunctionAddStoryPage);
//		params.put("jsondata", paramString);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			result = (object.getInt("errno") == 0);
//		}
//		catch(Exception e)
//		{
//			Log.e("ClassHandler::addClass", resultString);
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	public static boolean updateStoryPage(StoryPageEntity pageEntity)
//	{
//		boolean result = false;
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		JSONArray array = new JSONArray();
//		array.put(pageEntity.storyId);
//		array.put(pageEntity.page);
//		array.put(pageEntity.desc);
//		
//		String paramString = array.toString();
//		paramString = paramString.replace("\\", "\\\\")
//				.replace("\"", "\\\"")
//				.replace("\r", "\\r")
//				.replace("\n", "\\n");
//
//		params.put("class", InterfaceProtocol.BigtureStory.ClassName);
//		params.put("function", InterfaceProtocol.BigtureStory.FunctionUpdateStoryPage);
//		params.put("jsondata", paramString);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			result = (object.getInt("errno") == 0);
//		}
//		catch(Exception e)
//		{
//			Log.e("ClassHandler::addClass", resultString);
//			e.printStackTrace();
//		}
//
//		return result;
//
//	}

//	public static StoryEntity getStory(String storyId)
//	{
//		StoryEntity entity = null;
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", InterfaceProtocol.BigtureStory.ClassName);
//		params.put("function", InterfaceProtocol.BigtureStory.FunctionGetStory);
//		params.put("jsondata", "[" + storyId + "]");
//		
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			if (errorNo == 0)
//			{
//				entity = new StoryEntity();
//				
//				JSONObject obj = (JSONObject)object.get("data");
//				entity.importData(obj);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return entity;
//	}

	public static ArrayList<StoryEntity> getUserStoryList(String userId, String status, String sortOption){
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("userId",userId);
		if(status != null){
			params.add("status",status);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListUserStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		

		return storyList;
	}
	
	public static StoryPageWrapper findOwnStory(String status,String sortOption,int page){
		StoryPageWrapper wrapper = new StoryPageWrapper();
		
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		wrapper.stories = storyList;
				
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		if(status != null){
			params.add("status",status);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListOwnStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				wrapper.totalPage = object.getInt("totalPage");
			
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static StoryPageWrapper findJoinedStory(String status,String sortOption,int page){
		StoryPageWrapper wrapper = new StoryPageWrapper();
		
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		wrapper.stories = storyList;
				
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		if(status != null){
			params.add("status",status);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListJoinedStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				wrapper.totalPage = object.getInt("totalPage");
			
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static StoryPageWrapper findCollectedStory(String status,String sortOption,int page){
		StoryPageWrapper wrapper = new StoryPageWrapper();
		
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		wrapper.stories = storyList;
				
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		if(status != null){
			params.add("status",status);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListCollectedStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				wrapper.totalPage = object.getInt("totalPage");
			
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static StoryPageWrapper findAllStory(String sortOption,int page){
		StoryPageWrapper wrapper = new StoryPageWrapper();
		
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		wrapper.stories = storyList;
				
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListAllStoryURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				wrapper.totalPage = object.getInt("totalPage");
			
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static StoryPageWrapper findStoryByExperts(String targetId,String sortOption,int page){
		StoryPageWrapper wrapper = new StoryPageWrapper();
		
		ArrayList<StoryEntity> storyList = new ArrayList<StoryEntity>();
		wrapper.stories = storyList;
				
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		params.add("targetId",targetId);
		
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListStoryByExpertsURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				wrapper.totalPage = object.getInt("totalPage");
			
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryEntity entity = new StoryEntity();
					entity.importData(obj);
					storyList.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
//	public static StoryPageEntity getStoryPage(String storyId, String page)
//	{
//		StoryPageEntity entity = null;
//		
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctionGetPage;
//		String jsondata = "[" + storyId + "," + page + "]";
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			if (errorNo == 0)
//			{
//				entity = new StoryPageEntity();
//				
//				JSONObject obj = (JSONObject)object.get("data");
//				entity.importData(obj);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return entity;
//	}
//	
//	public static StoryPageEntity getStoryPageByArtworkId(String artworkId)
//	{
//		StoryPageEntity pageEntity = null;
//		
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctionGetStoryPageByArtworkId;
//		String jsondata = "[" + artworkId + "]";
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			if (errorNo == 0)
//			{
//				pageEntity = new StoryPageEntity();
//				
//				JSONObject obj = (JSONObject)object.getJSONObject("data");
//				pageEntity.importData(obj);
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return pageEntity;		
//	}
//	
	public static ArrayList<StoryPageEntity> getPageList(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		ArrayList<StoryPageEntity> pages = new ArrayList<StoryPageEntity>();
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListStoryPageURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryPageEntity entity = new StoryPageEntity();
					entity.importData(obj);
					pages.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getUserStory", e.getMessage());
			e.printStackTrace();
		}
		return pages;
	}
	
	public static ArrayList<StoryArtworkEntity> getStoryArtworkList(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		ArrayList<StoryArtworkEntity> artworks = new ArrayList<StoryArtworkEntity>();
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListStoryArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryArtworkEntity entity = new StoryArtworkEntity();
					entity.importData(obj);
					artworks.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getStoryArtworks", e.getMessage());
			e.printStackTrace();
		}
		return artworks;
	}
	
	public static ArrayList<StoryArtworkEntity> getStoryArtworkListByPage(String pageId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("pageId",pageId);
		
		ArrayList<StoryArtworkEntity> artworks = new ArrayList<StoryArtworkEntity>();
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListStoryArtworkByPageURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					StoryArtworkEntity entity = new StoryArtworkEntity();
					entity.importData(obj);
					artworks.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("getStoryArtworkListByPage", e.getMessage());
			e.printStackTrace();
		}
		return artworks;
	}
	
//
//	public static List<StoryArtworkEntity> getPersonalStoryArtworkList(String storyId, String ownerUserIdx, String readUserIdx)
//	{
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctionGetPersonalStoryArtworks;
//		String jsondata = null;
//		
//		if (readUserIdx == null)
//			jsondata = "[" + storyId + "," + ownerUserIdx + "]";
//		else
//			jsondata = "[" + storyId + "," + ownerUserIdx + "," + readUserIdx + "]";
//		
//		List<StoryArtworkEntity> pageArtworkList = null;
//
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			if (errorNo == 0)
//			{
//				pageArtworkList = new ArrayList<StoryArtworkEntity>();
//				
//				JSONArray arr = (JSONArray)object.get("artworks");
//				
//				for (int i = 0; i < arr.length(); i++)
//				{
//					JSONObject obj = arr.getJSONObject(i);
//					
//					StoryArtworkEntity entity = new StoryArtworkEntity();
//					entity.importData(obj);
//					
//					pageArtworkList.add(entity);
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		return pageArtworkList;
//		
//	}
//
//	public static List<StoryArtworkEntity> getPageArtworkList(String storyId, int page)
//	{
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctionGetPageArtworks;
//		String jsondata = "[" + storyId + "," + page + "]";
//		
//		List<StoryArtworkEntity> pageArtworkList = null;
//
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			if (errorNo == 0)
//			{
//				pageArtworkList = new ArrayList<StoryArtworkEntity>();
//				
//				JSONArray arr = (JSONArray)object.get("artworks");
//				
//				for (int i = 0; i < arr.length(); i++)
//				{
//					JSONObject obj = arr.getJSONObject(i);
//					
//					StoryArtworkEntity entity = new StoryArtworkEntity();
//					entity.importData(obj);
//					
//					pageArtworkList.add(entity);
//				}
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		return pageArtworkList;
//		
//	}
//	
//	public static List<StoryArtworkEntity> getAfterReadingList(String storyId)
//	{
//		return getPageArtworkList(storyId, 0);
//	}
//	
	public static ArrayList<AfterReadEntity> listAfterReading(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		ArrayList<AfterReadEntity> artworks = new ArrayList<AfterReadEntity>();
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListAfterReadingURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray objArray = (JSONArray)object.get("datas");
				
				for (int i = 0; i < objArray.length(); i++){
					JSONObject obj = objArray.getJSONObject(i);
					
					AfterReadEntity entity = new AfterReadEntity();
					entity.importData(obj);
					artworks.add(entity);
				}
			}
		}catch(Exception e){
			Log.e("listAfterReading", e.getMessage());
			e.printStackTrace();
		}
		return artworks;
		
	}
	
	public static boolean registerArtwork(String userIdx, String storyId, String pageId, String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		params.add("artworkId",artworkId);
		params.add("pageId",pageId);
		
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.RegistStoryArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registerArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean registerAfterReading(String userIdx, String storyId, String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		params.add("artworkId",artworkId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.RegistAfterReadingURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registerArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean addCollection(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.AddStoryCollectionURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("deleteStory", e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean deleteCollection(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.RemoveStoryCollectionURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("deleteStory", e.getMessage());
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static StoryEntity getStory(String storyId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("storyId",storyId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.GetStoryInfoURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject json = object.getJSONObject("data");
				StoryEntity entity = new StoryEntity();
				entity.importData(json);
				return entity;
			}else{
				return null;
			}
		}catch(Exception e){
			Log.e("getStory", e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
//
//	public static boolean deleteAllStoryPages(String storyIdx)
//	{
//		boolean result = false;
//
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctiondDleteAllStoryPages;
//		String jsondata = "[" + storyIdx + "]";
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			result = (errorNo == 0);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//	

//	
//	public static boolean removeArtwork(String userIdx, String artworkIdx)
//	{
//		boolean result = false;
//
//		String className = InterfaceProtocol.BigtureStory.ClassName;
//		String function  = InterfaceProtocol.BigtureStory.FunctionRemoveArtwork;
//		String jsondata = "[" + userIdx + "," + artworkIdx + "]";
//		
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("class", className);
//		params.put("function", function);
//		params.put("jsondata", jsondata);
//
//		HttpUtil util = new HttpUtil();
//		String resultString = util.loadHTML(InterfaceProtocol.GatewayURL, null, params);
//		
//		try
//		{
//			JSONObject object = new JSONObject(resultString);
//			int errorNo = object.getInt("errno");
//			
//			result = (errorNo == 0);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//
//		return result;		
//	}
}
