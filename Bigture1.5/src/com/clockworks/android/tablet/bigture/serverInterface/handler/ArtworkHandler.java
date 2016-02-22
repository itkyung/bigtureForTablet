package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.common.ArtworkType;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentWrapperEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class ArtworkHandler {

	
	public static ArtworkPageWrapper listUserArtworks(String userId,String type,String sortOption){
		ArtworkPageWrapper pageWrapper = new ArtworkPageWrapper();
		ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		if(userId != null)
			params.add("userId",userId);
		if(type != null){
			params.add("type",type);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListUserArtworksURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtworkEntity artwork = new ArtworkEntity();
					artwork.importData(data);
					artworks.add(artwork);
				}
				pageWrapper.artworks = artworks;
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return pageWrapper;
	}
	
	public static ArtworkPageWrapper listAllArtworks(String type,String sortOption,int page){
		ArtworkPageWrapper pageWrapper = new ArtworkPageWrapper();
		
		ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		if(type != null){
			params.add("type",type);
		}
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListAllArtworksURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtworkEntity artwork = new ArtworkEntity();
					artwork.importData(data);
					artworks.add(artwork);
				}
				
				pageWrapper.artworks = artworks;
				pageWrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return pageWrapper;
	}
	
	//서버에서 메인에 보여줄 coverImage를 찾아서 리턴한다.
	public static String getCoverImageURL(){
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.SplashURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			JSONObject data = object.getJSONObject("data");
			if(success){
				return data.getString("photo");
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static CommentWrapperEntity getArtworkComments(String artworkId,int page){
		CommentWrapperEntity wrapper = new CommentWrapperEntity();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.GetArtworkCommentsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				wrapper.importData(object);
			}else{
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return wrapper;
	}
	
	public static ArtworkPageWrapper listFriendsArtworks(String sortOption,int page){
		ArtworkPageWrapper pageWrapper = new ArtworkPageWrapper();
		
		ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListFriendsArtworksURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtworkEntity artwork = new ArtworkEntity();
					artwork.importData(data);
					artworks.add(artwork);
				}
				pageWrapper.artworks = artworks;
				pageWrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return pageWrapper;
	}
	
	public static ArtworkPageWrapper listCollectedArtworks(String sortOption,int page){
		ArtworkPageWrapper pageWrapper = new ArtworkPageWrapper();
		ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListCollectedArtworksURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtworkEntity artwork = new ArtworkEntity();
					artwork.importData(data);
					artworks.add(artwork);
				}
				pageWrapper.artworks = artworks;
				pageWrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return pageWrapper;
	}
	
	public static boolean sendSticker(String artworkId,String sticker){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("sticker",sticker);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.SendStickerURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendComment(String artworkId,String comment){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("comment",comment);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.SendCommentURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean addCollection(String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.AddArtworkCollectionURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	public static boolean removeCollection(String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.RemoveArtworkCollectionURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean editArtworks(String artworkId,String title,String comment,String shareType){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("title",title);
		params.add("comment",comment);
		params.add("shareType",shareType);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.EditArtworkURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean deleteArtwork(String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);

		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.DeleteArtworkURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean deleteCommentOrSticker(String commentId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("id",commentId);

		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.DeleteCommentOrStickerURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean reportSpam(String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);

		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ReportSpamURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean requestToContest(String artworkId,String contestId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("contestId",contestId);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.RequestToContestURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static String registerArtwork(String userIndex, String title, String comment, String refId, 
			ArtworkType type, ShareType share, File bitmapFile, File drawFile){
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		title = title.replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\r", "\\r")
				.replace("\n", "\\n");
		if(comment != null){
			comment = comment.replace("\\", "\\\\")
					.replace("\"", "\\\"")
					.replace("\r", "\\r")
					.replace("\n", "\\n");
		}
		
		
		params.put("title",title);
		params.put("comment",comment);
		params.put("type",type.name());
		params.put("share",share.name());
		params.put("refId",refId);
		params.put("osType","Android");
		//params.put("file",bitmapFile);
		params.put("files[0]",bitmapFile);
		//서가영 선임 요청으로 태블릿버전에서는 drawFile을 보내지 않게 수정함.
		//params.put("files[1]",drawFile);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.executeByMultipart(ServerStaticVariable.RegistArtworkURL, null,params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			JSONObject json = object.getJSONObject("data");
			return json.getString("id");
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static ArtworkEntity loadArtwork(String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("id",artworkId);
	
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.LoadArtworkURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				ArtworkEntity entity = new ArtworkEntity();
				entity.importData(object.getJSONObject("data"));
				return entity;
			}
		}catch(Exception e){
			e.printStackTrace();
			
		}
		return null;
	}
	
}
