package com.clockworks.android.tablet.bigture.serverInterface.task;



import com.clockworks.android.tablet.bigture.serverInterface.handler.CareerHandler;

import android.os.AsyncTask;

public class CareerHandleTask extends AsyncTask<String,Void,Boolean>
{
	public static int ACTION_SAVE = 1;
	public static int ACTION_DELETE = 2;
	
	int action;
	CareerHandleTaskListener listener;
	
	public CareerHandleTask(int action, CareerHandleTaskListener listener)
	{
		this.action = action;
		this.listener = listener;
	}
	
	@Override
	protected Boolean doInBackground(String... params)
	{
		boolean result = false;
		
		if (action == ACTION_SAVE){
			result = CareerHandler.saveCareer(params[0], params[1], params[2], params[3]);
		}else if (action == ACTION_DELETE){
			result = CareerHandler.deleteCareer(params[0]);
		}
		

		return result;
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		if (this.listener != null)
			this.listener.onComplete(this, result);
		
		super.onPostExecute(result);
	}

	public interface CareerHandleTaskListener
	{
		void onComplete(CareerHandleTask task, boolean result);
	}
}
