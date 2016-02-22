package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.List;


import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;

import android.os.AsyncTask;

public class NewsReadTask extends AsyncTask<String,Void,List<NewsEntity>> {
	private NewsReadTaskListener listener;
	
	public NewsReadTask(NewsReadTaskListener listener) {
		super();
		this.listener = listener;
	}

	@Override
	protected List<NewsEntity> doInBackground(String... params) {
		return NewsHandler.readNewList(params[0]);
	}

	@Override
	protected void onPostExecute(List<NewsEntity> result) {
		if (listener != null)
			listener.onComplete(this, result);
		super.onPostExecute(result);
	}

	public interface NewsReadTaskListener{
		void onComplete(NewsReadTask task, List<NewsEntity> newsList);
	}

}
