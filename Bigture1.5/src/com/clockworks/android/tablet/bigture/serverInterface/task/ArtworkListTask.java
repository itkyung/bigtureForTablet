package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;

import android.os.AsyncTask;

public class ArtworkListTask extends AsyncTask<String, Void, ArtworkPageWrapper> {
	public static final int USER_ARTWORKS = 1;
	public static final int ALL_ARTWORS = 2;
	
	private int action;
	private ArtworkListListener listener;
	
	
	
	public ArtworkListTask(int action, ArtworkListListener listener) {
		super();
		this.action = action;
		this.listener = listener;
	}

	@Override
	protected ArtworkPageWrapper doInBackground(String... params) {
		if(action == USER_ARTWORKS){
			String userId = params[0];
			String type = params[1];
			String sortOption = params[2];
			return ArtworkHandler.listUserArtworks(userId, type, sortOption);
		}else{
			String type = params[0];
			String sortOption = params[1];
			int page = Integer.parseInt(params[2]);
			
			return ArtworkHandler.listAllArtworks(type, sortOption, page);
		}
	}

	@Override
	protected void onCancelled(ArtworkPageWrapper result) {
		// TODO Auto-generated method stub
		super.onCancelled(result);
	}

	@Override
	protected void onPostExecute(ArtworkPageWrapper result) {
		this.listener.onComplete(result);
		super.onPostExecute(result);
	}

	public interface ArtworkListListener{
		public void onComplete(ArtworkPageWrapper pageWrapper);
	}
	
	
}
