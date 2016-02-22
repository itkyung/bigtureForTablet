package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.io.File;

import com.clockworks.android.tablet.bigture.common.ArtworkType;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;

import android.os.AsyncTask;

public class ArtworkRegisterTask extends AsyncTask<Object, Void, String>
{
	private ArtworkRegisterListener listener;
	
	public ArtworkRegisterTask(ArtworkRegisterListener listener){
		this.listener = listener;
	}
	
	public void register(String userIdx, String title, String comment, String refId, ArtworkType type, ShareType shareType, File imageFile, File drawingFile){
		execute(userIdx, title, comment, refId, type, shareType, imageFile, drawingFile);
	}

	@Override
	protected String doInBackground(Object... params){
		String userIdx = (String)params[0];
		String title     = (String)params[1];
		String comment   = (String)params[2];
		String refId = (String)params[3];
		ArtworkType type = (ArtworkType)params[4];
		ShareType shareType = (ShareType)params[5];
		File imageFile  = (File)params[6];
		File drawingFile  = (File)params[7];
		
		return ArtworkHandler.registerArtwork(userIdx, title, comment, refId, type, shareType, imageFile, drawingFile);
	}

	@Override
	protected void onPostExecute(String result){
		if (this.listener != null)
			this.listener.onComplete(result);
		
		super.onPostExecute(result);
	}
	
	public interface ArtworkRegisterListener{
		void onComplete(String artworkId);
	}
}
