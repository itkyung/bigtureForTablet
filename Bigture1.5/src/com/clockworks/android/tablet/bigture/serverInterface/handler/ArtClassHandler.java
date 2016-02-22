package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PuzzleArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PuzzleEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class ArtClassHandler {

	public static ArrayList<ArtClassEntity> listUserArtclass(String userId,int page){
		ArrayList<ArtClassEntity> artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		if(userId != null)
			params.add("userId",userId);
		params.add("page",page+"");
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListUserArtClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					artClasses.add(artClass);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return artClasses;
	}
	
	
	public static ArtClassPageWrapper findOwnClass(String userId,int page,String status){
		ArtClassPageWrapper wrapper = new ArtClassPageWrapper();
		wrapper.artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
	
		params.add("userId",userId);
		params.add("page",page+"");
		if(status != null)
			params.add("status",status);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListOwnArtClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					wrapper.artClasses.add(artClass);
				}
				wrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static ArtClassPageWrapper findJoinedClass(String userId,int page,String status){
		ArtClassPageWrapper wrapper = new ArtClassPageWrapper();
		wrapper.artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
	
		params.add("userId",userId);
		params.add("page",page+"");
		if(status != null)
			params.add("status",status);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListJoinedClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					wrapper.artClasses.add(artClass);
				}
				wrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static ArtClassPageWrapper findCollectedClass(String userId,int page,String status){
		ArtClassPageWrapper wrapper = new ArtClassPageWrapper();
		wrapper.artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
	
		params.add("userId",userId);
		params.add("page",page+"");
		if(status != null)
			params.add("status",status);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListCollectedClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					wrapper.artClasses.add(artClass);
				}
				wrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static ArtClassPageWrapper findAllClass(String userId,int page,String sortOption){
		ArtClassPageWrapper wrapper = new ArtClassPageWrapper();
		wrapper.artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
	
		params.add("userId",userId);
		params.add("page",page+"");
		if(sortOption != null)
			params.add("sortOption",sortOption);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListAllClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					wrapper.artClasses.add(artClass);
				}
				wrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static ArtClassPageWrapper findClassByExperts(String targetId,int page,String sortOption){
		ArtClassPageWrapper wrapper = new ArtClassPageWrapper();
		wrapper.artClasses = new ArrayList<ArtClassEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
	
		params.add("userId",targetId);
		params.add("page",page+"");
		if(sortOption != null)
			params.add("sortOption",sortOption);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListClassByExpertURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ArtClassEntity artClass = new ArtClassEntity();
					artClass.importData(data);
					wrapper.artClasses.add(artClass);
				}
				wrapper.totalPage = object.getInt("totalPage");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static CommonCoverEntity uploadCover(File image,String localPath){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("anyFile", image);
		params.put("localPath",localPath);
		HttpUtil util = new HttpUtil();
		
		String resultString = util.executeByMultipart(ServerStaticVariable.UploadClassCoverURL, null, params);
		
		
		try{
			CommonCoverEntity entity = new CommonCoverEntity();
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject imgInfo = object.getJSONObject("data");
				entity.coverImageId = imgInfo.getString("id");
				entity.coverImagePath = imgInfo.getString("photo");
				entity.coverThumbPath = imgInfo.getString("thumbnail");
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
	
	public static boolean createArtClass(String userId,ArtClassEntity entity){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("className",entity.className);
		params.add("description",entity.description);
		params.add("classType",entity.classType);
		params.add("shareType",entity.shareType);
		params.add("startDate",entity.startDate);
		params.add("endDate",entity.endDate);
		if(entity.coverEntity != null){
			params.add("coverImageId",entity.coverEntity.coverImageId);
			params.add("coverFromArtwork", (entity.coverEntity.coverFromArtworks == true) ? "true" : "false");
		}
		
		if(entity.members != null){
			int i = 0;
			for(FriendEntity friend : entity.members){
				params.add("sharedFriends[" + i + "]",friend.index);
				i++;
			}
		}
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.CreateClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public static ArtClassEntity getArtClassInfo(String classId){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetClassInfoURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			JSONObject data = object.getJSONObject("data");
			
			ArtClassEntity entity = new ArtClassEntity();
			entity.importData(data);
			
			return entity;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean addClassCollection(String classId){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.AddClassCollectionURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean removeClassCollection(String classId){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.RemoveClassCollectionURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static ArrayList<SimpleUserEntity> getJoinedMembers(String classId){
		ArrayList<SimpleUserEntity> members = new ArrayList<SimpleUserEntity>();
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.GetJoinedMembersURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			JSONArray datas = object.getJSONArray("datas");
			
			for(int i=0; i < datas.length(); i++){
				JSONObject json = datas.getJSONObject(i);
				SimpleUserEntity entity = new SimpleUserEntity();
				entity.importData(json);
				members.add(entity);
			}
			
			return members;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return members;
	}
	
	public static String changeCoverImage(String classId,String coverImageId,boolean coverFromArtwork){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
		params.add("coverImageId",coverImageId);
		params.add("coverFromArtwork",coverFromArtwork?"true":"false");
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ChangeCoverImageURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				String imagePath = object.getString("data");
				return imagePath;
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static ArtworkPageWrapper listClassArtworks(String classId,int page,String sortOption){
		ArtworkPageWrapper wrapper = new ArtworkPageWrapper();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("page",""+page);
		params.add("sortOption",sortOption);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ListClassArtworksURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				wrapper.totalPage = object.getInt("totalPage");
				wrapper.artworks = new ArrayList<ArtworkEntity>();
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject json = datas.getJSONObject(i);
					ArtworkEntity entity = new ArtworkEntity();
					entity.importData(json);
					wrapper.artworks.add(entity);
				}
			}
			return wrapper;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	

	public static boolean deleteArtClass(String classId){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
	
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.DeleteArtClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean editArtClass(String classId,String startDate,String endDate,String description){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("classId",classId);
		params.add("startDate",startDate);
		params.add("endDate",endDate);
		params.add("description",description);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.EditArtClassURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean addClassMembers(String classId,ArrayList<FriendEntity> addedMembers){
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("id",classId);
		
		int i = 0;
		for(FriendEntity friend : addedMembers){
			params.add("addedMemberIds[" + i + "]",friend.index);
			i++;
		}
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.AddClassMembersURL, params);
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean registArtwork(String classId,String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("artworkId",artworkId);
	
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.RegistClassArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registerArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean registPuzzleArtwork(String classId,String puzzleId,String part,String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("artworkId",artworkId);
		params.add("puzzleId",puzzleId);
		params.add("part",part);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.RegistPuzzleArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registPuzzleArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean reservePuzzleArtwork(String classId,String puzzleId,String part,String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("artworkId",artworkId);
		params.add("puzzleId",puzzleId);
		params.add("part",part);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ReservePuzzleArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registPuzzleArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean cancelPuzzleArtwork(String classId,String puzzleId,String part,String artworkId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("artworkId",artworkId);
		params.add("puzzleId",puzzleId);
		params.add("part",part);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.CancelPuzzleArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			
			return success;
		}catch(Exception e){
			Log.e("registPuzzleArtwork", e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static ArrayList<PuzzleEntity> getPuzzleList(String classId){
		ArrayList<PuzzleEntity> results = new ArrayList<PuzzleEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.GetPuzzleListURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject json = datas.getJSONObject(i);
					PuzzleEntity puzzle = new PuzzleEntity();
					puzzle.importData(json);
					results.add(puzzle);
				}
			}
			return results;
		}catch(Exception e){
			Log.e("getPuzzleList", e.getMessage());
			e.printStackTrace();
		}
		return results;
	}
	
	public static ArrayList<PuzzleArtworkEntity> getPuzzleArtworkList(String classId,String puzzleId){
		ArrayList<PuzzleArtworkEntity> results = new ArrayList<PuzzleArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("classId",classId);
		params.add("puzzleId",puzzleId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.ListPuzzleArtworksURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject json = datas.getJSONObject(i);
					PuzzleArtworkEntity puzzle = new PuzzleArtworkEntity();
					puzzle.importData(json);
					results.add(puzzle);
				}
			}
			return results;
		}catch(Exception e){
			Log.e("getPuzzleArtworkList", e.getMessage());
			e.printStackTrace();
		}
		return results;
		
	}
	
	
	public static PuzzleEntity getPuzzle(String puzzleId){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("puzzleId",puzzleId);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.GetPuzzleInfoURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject json = object.getJSONObject("data");
				PuzzleEntity entity = new PuzzleEntity();
				entity.importData(json);
				return entity;
			}
			return null;
		}catch(Exception e){
			Log.e("getPuzzle", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static PuzzleArtworkEntity getPuzzleArtwork(String puzzleId,String part){
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("puzzleId",puzzleId);
		params.add("part",part);
		
		try{
			HttpUtil util = new HttpUtil();
			String resultString = util.execute(ServerStaticVariable.GetPuzzleArtworkURL, params);
	
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject json = object.getJSONObject("data");
				PuzzleArtworkEntity entity = new PuzzleArtworkEntity();
				entity.importData(json);
				return entity;
			}
			return null;
		}catch(Exception e){
			Log.e("getPuzzle", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
