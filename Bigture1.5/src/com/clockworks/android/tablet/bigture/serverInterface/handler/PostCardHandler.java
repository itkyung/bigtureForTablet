package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardReceiverEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class PostCardHandler {

	public static ArrayList<PostCardEntity> listSendedPostCards(int page){
		ArrayList<PostCardEntity> cards = new ArrayList<PostCardEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListSendedPostCardsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					PostCardEntity card = new PostCardEntity();
					card.importData(data);
					cards.add(card);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cards;
	}
	
	public static ArrayList<PostCardEntity> listReceivedPostCards(int page){
		ArrayList<PostCardEntity> cards = new ArrayList<PostCardEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListReceivedPostCardsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					PostCardEntity card = new PostCardEntity();
					card.importData(data);
					cards.add(card);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cards;
	}
	
	public static ArrayList<PostCardEntity> listAllPostCards(String ownerId,int page){
		ArrayList<PostCardEntity> cards = new ArrayList<PostCardEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListAllPostCardsURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					PostCardEntity card = new PostCardEntity();
					card.importData(data);
					cards.add(card);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return cards;
	}
	
	public static boolean sendPostCard(String artworkId,String comment,boolean reply,String emails,ArrayList<String> receivers){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("artworkId",artworkId);
		params.add("comment",comment);
		params.add("reply",reply+"");
		params.add("emails",emails);
		
		for(int i=0; i < receivers.size(); i++){
			String receiver = receivers.get(i);
			params.add("receiverIds[" + i + "]",receiver); 
		}
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.SendPostCardURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean changeShareMode(String cardId,boolean open){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("cardId",cardId);
		params.add("open",open+"");
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ChangeCardShareModeURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean removeFromList(String cardId,String listType){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("cardId",cardId);
		params.add("listType",listType);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.RemoveFromListURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			return success;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static PostCardEntity viewPostCard(String cardId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("cardId",cardId);

		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ViewPostCardURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject cardJson = object.getJSONObject("data");
				PostCardEntity entity = new PostCardEntity();
				entity.importData(cardJson);
				if(cardJson.has("receivers")){
					JSONArray receiversData = cardJson.getJSONArray("receivers");
					entity.receivers = new ArrayList<PostCardReceiverEntity>();
					for(int i = 0; i < receiversData.length(); i++){
						JSONObject receiverJson = receiversData.getJSONObject(i);
						PostCardReceiverEntity rEntity = new PostCardReceiverEntity();
						rEntity.importData(receiverJson);
						entity.receivers.add(rEntity);
					}
				}else{
					entity.receivers = new ArrayList<PostCardReceiverEntity>();
				}
				return entity;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
		
	}
}
