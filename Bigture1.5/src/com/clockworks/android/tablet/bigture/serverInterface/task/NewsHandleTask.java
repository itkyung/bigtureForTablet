package com.clockworks.android.tablet.bigture.serverInterface.task;

import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;

import android.os.AsyncTask;

public class NewsHandleTask extends AsyncTask<String,Void,Boolean> {
	private NewsHandleTaskListener listener;
	
	
	public NewsHandleTask(NewsHandleTaskListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		return NewsHandler.deleteNews(params[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (this.listener != null)
			this.listener.onComplete(this, result);
		
		super.onPostExecute(result);
	}

	public interface NewsHandleTaskListener{
		void onComplete(NewsHandleTask task, boolean result);
	}

}
