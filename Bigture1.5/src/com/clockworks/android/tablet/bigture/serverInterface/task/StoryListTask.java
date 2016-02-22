package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.ArrayList;


import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;

import android.os.AsyncTask;

public class StoryListTask extends AsyncTask<String, Void, ArrayList<StoryEntity>> {
	public static final int USER_STORY = 1;
	public static final int ALL_STORY = 2;
	
	
	private int action;
	private StoryListListener listener;
	private boolean isMy;
	private boolean isPro;
	
	public StoryListTask(int action, StoryListListener listener,boolean isMy,boolean isPro) {
		super();
		this.action = action;
		this.listener = listener;
		this.isMy = isMy;
		this.isPro = isPro;
	}

	@Override
	protected ArrayList<StoryEntity> doInBackground(String... params) {
		if(action == USER_STORY){
			String userId = params[0];
			String status = params[1];
			String sortOption = params[2];
			return StoryHandler.getUserStoryList(userId, status, sortOption);
		}else{
			
			
		}
		
		
		return null;
	}

	@Override
	protected void onPostExecute(ArrayList<StoryEntity> result) {
		
		super.onPostExecute(result);
		
//		if(isMy && isPro){
//			ArrayList<StoryEntity> stories = new ArrayList<StoryEntity>();
//			StoryEntity dummy = new StoryEntity();
//			stories.add(dummy);
//			stories.addAll(result);
//			this.listener.onComplete(stories);
//		}else{
			this.listener.onComplete(result);
		//}
	}
	
	public interface StoryListListener{
		public void onComplete(ArrayList<StoryEntity> stories);
	}

}
