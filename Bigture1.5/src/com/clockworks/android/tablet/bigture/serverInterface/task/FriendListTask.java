package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;

import android.os.AsyncTask;

public class FriendListTask extends AsyncTask<String, Void, ArrayList<FriendEntity>> {
	public final static int LIKE_YOU = 0;
	public final static int LIKE_ME = 1;
	private int action;
	private FriendListListener listener;
	
	public FriendListTask(int action,FriendListListener l){
		super();
		this.action = action;
		this.listener = l;
	}
	
	
	@Override
	protected ArrayList<FriendEntity> doInBackground(String... params) {
		if(action == LIKE_YOU){
			return FriendHandler.getLikeYou(params[0], params[1], params[2]);
		}else{
			return FriendHandler.getLikeMe(params[0], params[1]);
		}
		
	}

	@Override
	protected void onPostExecute(ArrayList<FriendEntity> result) {
		super.onPostExecute(result);
		this.listener.onCompleted(result);
	}

	public interface FriendListListener{
		public void onCompleted(ArrayList<FriendEntity> friends);
	}

}
