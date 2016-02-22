package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.List;

import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;

import android.os.AsyncTask;

public class StoryHandleTask{
	Callback callback;

	public List<StoryEntity> storyList;
	public StoryEntity storyEntity;
	
	public StoryHandleTask(Callback callback)
	{
		this.callback = callback;
	}
	
	public void readStory(String storyId){
		storyEntity = null;
		
		InterfaceTask task = new InterfaceTask();
		task.execute("readStory", storyId);
	}
	
	
	public boolean runMethod(Object[] objects){
		String methodName = (String)objects[0];
		
		if (methodName.equals("readStory")){
			String storyId = (String)objects[1];
			storyEntity = StoryHandler.getStory(storyId);

			if (storyEntity != null)
				return true;
		}
		
		return false;
	}
	
	class InterfaceTask extends AsyncTask<Object,Void,Boolean>{
		@Override
		protected Boolean doInBackground(Object... params)
		{
			return runMethod(params);
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (callback != null)
				callback.onComplete(StoryHandleTask.this, result);

			super.onPostExecute(result);
		}
		
	}
	
	public interface Callback
	{
		void onComplete(StoryHandleTask handler, boolean result);
	}
}
