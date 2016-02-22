package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestWinnerEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class ContestHandler {
	
	public static ArrayList<ContestEntity> listActiveContest(int page){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListActiveContestURL, params);
		ArrayList<ContestEntity> contests = new ArrayList<ContestEntity>();
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ContestEntity entity = new ContestEntity();
					entity.importData(data);
					contests.add(entity);
				}	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return contests;
	}

	public static ContestPageWrapper listAllContest(int page){
		ContestPageWrapper wrapper = new ContestPageWrapper();
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("page",""+page);
		

		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListAllContestURL, params);
		ArrayList<ContestEntity> contests = new ArrayList<ContestEntity>();
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ContestEntity entity = new ContestEntity();
					entity.importData(data);
					contests.add(entity);
				}	
			}
			wrapper.totalPage = object.getInt("totalPage");
			wrapper.contests = contests;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return wrapper;
	}
	
	public static ArtworkPageWrapper listContestArtwork(String contestId,String sortOption,int page){
		
		ArtworkPageWrapper pageWrapper = new ArtworkPageWrapper();
		
		ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("contestId",contestId);
		
		if(sortOption != null){
			params.add("sortOption",sortOption);
		}
		params.add("page",""+page);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.ListContestArtworkURL, params);
		
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
	
	public static ArrayList<ContestWinnerEntity> listWinner(String contestId){
		ArrayList<ContestWinnerEntity> winners = new ArrayList<ContestWinnerEntity>();
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("contestId",contestId);
		
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.listWinnerURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONArray datas = object.getJSONArray("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					ContestWinnerEntity entity = new ContestWinnerEntity();
					entity.importData(data);
					winners.add(entity);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return winners;
	}
	
	public static ContestEntity getContest(String contestId){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("contestId",contestId);
		
		HttpUtil util = new HttpUtil();
		String resultStr = util.execute(ServerStaticVariable.GetContestURL, params);
		
		try{
			JSONObject object = new JSONObject(resultStr);
			boolean success = object.getBoolean("success");
			if(success){
				JSONObject json = object.getJSONObject("data");
				ContestEntity entity = new ContestEntity();
				entity.importData(json);
				return entity;
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
}
