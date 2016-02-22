package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.List;

import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.CareerHandler;


import android.os.AsyncTask;

public class CareerReadTask extends AsyncTask<String,Void,List<CareerEntity>>
{
	private final CareerReadTaskListener listener;
	
	public CareerReadTask(CareerReadTaskListener listener){
		this.listener = listener;
	}
	
	@Override
	protected List<CareerEntity> doInBackground(String... params){
		return CareerHandler.getCareerList(params[0]);
	}

	@Override
	protected void onPostExecute(List<CareerEntity> result)
	{
		if (listener != null)
			listener.onComplete(this, result);
		
		super.onPostExecute(result);
	}

	public interface CareerReadTaskListener
	{
		void onComplete(CareerReadTask task, List<CareerEntity> careerList);
	}
	
}
