package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.List;

import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ContestHandler;

import android.os.AsyncTask;

public class ContestHandleTask{
	public ContestEntity contestEntity;
	public List<ContestEntity> contestList;
	Callback callback;
	
	public ContestHandleTask(Callback callback)
	{
		this.callback = callback;
	}
	
	public void getContest(String contestId){
		InterfaceTask task = new InterfaceTask();
		task.execute("getContest", contestId);		
	}

	
	protected boolean runMethod(Object[] params)
	{
		String methodName = (String)params[0];
		
		if (methodName.equals("getContest")){
			String contestId = (String)params[1];
			contestEntity = ContestHandler.getContest(contestId);
			
			if (contestEntity != null)
				return true;
		}
		
		return false;
	}
	
	class InterfaceTask extends AsyncTask<Object,Void,Boolean>
	{
		@Override
		protected Boolean doInBackground(Object... params)
		{
			return runMethod(params);
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (callback != null)
				callback.onComplete(ContestHandleTask.this, result);
				
			super.onPostExecute(result);
		}		
	}

	public interface Callback
	{
		public void onComplete(ContestHandleTask task, boolean result);
	}
}
