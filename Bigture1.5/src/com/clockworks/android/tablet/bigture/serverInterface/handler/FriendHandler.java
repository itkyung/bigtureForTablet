package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.json.JSONArray;
import org.json.JSONObject;


import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;


public class FriendHandler{	
	
	public static ArrayList<FriendEntity> getLikeYou(String userId,String regionId,String keyword){
		boolean result = false;
		
		HttpRequestParameters params = new HttpRequestParameters();
		if(userId != null){
			params.add("userId",userId);
		}
		if(regionId != null){
			params.add("regionId",regionId);
		}
		if(keyword != null){
			params.add("keyword",keyword);
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetLikeYouURL, params);
		
		ArrayList<FriendEntity> friendList = new ArrayList<FriendEntity>();

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			result = (errorNo == 0);
			
			if (success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendEntity entity = new FriendEntity();
					entity.importData(array.getJSONObject(i));
					friendList.add(entity);
				}
			}
		}catch(Exception e){
			//friendList = null;
			e.printStackTrace();
		}
		
		return friendList;
	}

	public static ArrayList<FriendEntity> getLikeMe(String userId,String regionId){
		boolean result = false;
		
		HttpRequestParameters params = new HttpRequestParameters();
		if(userId != null){
			params.add("userId",userId);
		}
		if(regionId != null){
			params.add("regionId",regionId);
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetLikeMeURL, params);
		
		ArrayList<FriendEntity> friendList = new ArrayList<FriendEntity>();

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");
			result = (errorNo == 0);
			
			if (success){
				JSONArray array = object.optJSONArray("datas");
				for (int i = 0; i < array.length(); i++)
				{
					FriendEntity entity = new FriendEntity();
					entity.importData(array.getJSONObject(i));
					friendList.add(entity);
				}
			}
		}catch(Exception e){
			//friendList = null;
			e.printStackTrace();
		}
		
		return friendList;
	}
	


	public static boolean likeYou(String friendId){
		boolean result = false;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("taregetUserId", friendId);
		
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.LikeYouURL, params);
		
		try
		{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			result = (errorNo == 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	public static boolean unlike(String friendId)
	{
		boolean result = false;
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("taregetUserId",friendId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.UnLikeURL, params);
		
		try
		{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			result = (errorNo == 0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	public static ArrayList<FriendGroupEntity> findGroups(){
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetGroupURL, params);
		ArrayList<FriendGroupEntity> groupList = new ArrayList<FriendGroupEntity>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendGroupEntity entity = new FriendGroupEntity();
					entity.importData(array.getJSONObject(i));
					groupList.add(entity);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return groupList;
	}
	
	public static boolean saveGroup(String id,String name){
		HttpRequestParameters params = new HttpRequestParameters();
		
		if(id != null){
			params.add("groupId",id);
		}
		params.add("groupName",name);
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.SaveGroupURL, params);
		boolean success = false;
		try{
			JSONObject object = new JSONObject(resultString);
			success = object.getBoolean("success");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return success;
	}
	
	public static boolean deleteGroup(String id){
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("groupId",id);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.DeleteGroupURL, params);
		boolean success = false;
		try{
			JSONObject object = new JSONObject(resultString);
			success = object.getBoolean("success");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return success;
		
	}
	
	public static ArrayList<FriendEntity> getFriendGroupMembers(String groupId,int page){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("groupId",groupId);
		params.add("page",1+"");
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetFriendMemberURL, params);
		ArrayList<FriendEntity> groupList = new ArrayList<FriendEntity>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendEntity entity = new FriendEntity();
					entity.importData(array.getJSONObject(i));
					groupList.add(entity);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return groupList;
	}
	
	public static ArrayList<FriendEntity> findUsers(String keyword,int page){
		HttpRequestParameters params = new HttpRequestParameters();
		if(keyword != null){
			params.add("keyword",keyword);
		}
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.FindUserURL, params);
		ArrayList<FriendEntity> users = new ArrayList<FriendEntity>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendEntity entity = new FriendEntity();
					entity.importData(array.getJSONObject(i));
					users.add(entity);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	
	public static ArrayList<FriendEntity> findInFriends(String keyword,int page){
		HttpRequestParameters params = new HttpRequestParameters();
		if(keyword != null){
			params.add("keyword",keyword);
		}
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.FindInFriendURL, params);
		ArrayList<FriendEntity> users = new ArrayList<FriendEntity>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendEntity entity = new FriendEntity();
					entity.importData(array.getJSONObject(i));
					users.add(entity);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	
	public static ArrayList<FriendGroupEntity> findGroupAndMembers(){
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetGroupAndMembersURL, params);
		ArrayList<FriendGroupEntity> groupList = new ArrayList<FriendGroupEntity>();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray array = object.getJSONArray("datas");
				for (int i = 0; i < array.length(); i++){
					FriendGroupEntity entity = new FriendGroupEntity();
					JSONObject obj = array.getJSONObject(i);
					entity.importData(obj);
					groupList.add(entity);
					entity.members = new ArrayList<FriendEntity>();
					
					if(obj.has("members")){
						JSONArray members = obj.getJSONArray("members");
						for (int j = 0; j < members.length(); j++){
							FriendEntity fe = new FriendEntity();
							fe.importData(members.getJSONObject(j));
							entity.members.add(fe);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return groupList;
		
	}
	
	public static boolean editGroupMember(String groupId,ArrayList<String> addedMembers,ArrayList<String> deletedMembers){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("id",groupId);

		int i = 0;
		for(String id : addedMembers){
			params.add("addedMemberIds[" + i + "]",id);
			i++;
		}
		
		i = 0;
		for(String id : deletedMembers){
			params.add("deletedMemberIds[" + i + "]",id);
			i++;
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.EditGroupMemberURL, params);
		
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
