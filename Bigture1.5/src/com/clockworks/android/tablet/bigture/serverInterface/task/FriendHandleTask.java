package com.clockworks.android.tablet.bigture.serverInterface.task;


import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;

import android.os.AsyncTask;

public class FriendHandleTask extends AsyncTask<String, Void, Boolean> {
	Callback callback;
	
	
	public FriendHandleTask(Callback callback) {
		super();
		this.callback = callback;
	}

	public void likeYou(String friendId){
		execute("likeYou",friendId);
	}
	
	public void unlike(String friendId){
		execute("unlike",friendId);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		String method = params[0];
		String friendId = params[1];
		
		String url = null;
		if(method.equals("likeYou")){
			return FriendHandler.likeYou(friendId);
		}else{
			return FriendHandler.unlike(friendId);
		}
		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		
		super.onPostExecute(result);
		this.callback.onComplete(this, result);
	}

	
	public interface Callback{
		public void onComplete(FriendHandleTask task, boolean result);
	}
}
