package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsImageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.TempImgEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


public class NewsHandler
{
	public static List<NewsEntity> readNewList(String userIndex){
		List<NewsEntity> newsList = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("userId",userIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListNewsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray masterArray = (JSONArray)object.get("datas");
				
				newsList = new ArrayList<NewsEntity>();
				
				for (int i = 0; i < masterArray.length(); i++){
					JSONObject masterObject = (JSONObject)masterArray.get(i);
					
					NewsEntity entity = new NewsEntity();
					entity.importData(masterObject);
					newsList.add(entity);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return newsList;
	}
	
	public static List<NewsImageEntity> readNewsImageList(String newsIndex){
		List<NewsImageEntity> imageList = null;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("newsId",newsIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListNewsImagesURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				JSONArray masterArray = (JSONArray)object.get("datas");
				
				imageList = new ArrayList<NewsImageEntity>();
				
				for (int i = 0; i < masterArray.length(); i++)
				{
					JSONObject masterObject = (JSONObject)masterArray.get(i);
					
					NewsImageEntity newsImage = new NewsImageEntity();
					newsImage.importData(masterObject);
					imageList.add(newsImage);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return imageList;
	}
	
	/**
	 * 해당 이미지를 서버에 업로드하고 이미지 tempId를 발급받는다.
	 * @param image
	 * @return
	 */
	public static TempImgEntity uploadNewsImage(File image,String localPath){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("anyFile", image);
		params.put("localPath",localPath);
		HttpUtil util = new HttpUtil();
		
		String resultString = util.executeByMultipart(ServerStaticVariable.UploadNewsImageURL, null, params);
		
		
		try{
			TempImgEntity entity = new TempImgEntity();
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject imgInfo = object.getJSONObject("data");
				entity.id = imgInfo.getString("id");
				entity.localImagePath = imgInfo.getString("localImagePath");
				return entity;
				
			}else{
				return null;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean saveNews(String newsId, String title, String content, List<String> addedImages,
			List<String> deletedImages){
		
		HttpRequestParameters params = new HttpRequestParameters();
		if(newsId != null)
			params.add("id",newsId);
		params.add("title",title);
		params.add("contents",content);
		
		int i = 0;
		for(String id : addedImages){
			params.add("addedImages[" + i + "]",id);
			i++;
		}
		
		i = 0;
		for(String id : deletedImages){
			params.add("deletedImages[" + i + "]",id);
			i++;
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.SaveNewsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static boolean deleteNews(String newsIndex){

		HttpRequestParameters params = new HttpRequestParameters();
		params.add("newsId",newsIndex);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.DeleteNewsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}

}
