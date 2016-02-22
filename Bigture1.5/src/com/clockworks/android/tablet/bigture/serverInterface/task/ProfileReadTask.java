package com.clockworks.android.tablet.bigture.serverInterface.task;

import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ProfileHandler;


import android.os.AsyncTask;

public class ProfileReadTask extends AsyncTask<String,Void,ProfileEntity>
{
	private ProfileReadTaskListener listener;
	
	public ProfileReadTask(ProfileReadTaskListener listener){
		this.listener = listener;
	}
	
	@Override
	protected ProfileEntity doInBackground(String... params){
		ProfileEntity entity = null;
		
		try{
			entity = ProfileHandler.readProfile(params[0]);
			
		}catch(Exception e){
			entity = null;
		}
		
		return entity;
	}

	@Override
	protected void onPostExecute(ProfileEntity result)
	{
		if (listener != null)
		{
			if (result != null)
				listener.onSuccess(result);
			else
				listener.onFail();
		}
		super.onPostExecute(result);
	}
	
	public interface ProfileReadTaskListener
	{
		public void onSuccess(ProfileEntity profileEntity);
		public void onFail();
	}

}
