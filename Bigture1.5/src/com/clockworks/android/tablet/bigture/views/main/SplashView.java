package com.clockworks.android.tablet.bigture.views.main;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class SplashView extends RelativeLayout
{
	Handler handler;
	ImageView imageArtwork;
	
	public SplashView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate()
	{
		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		
		super.onFinishInflate();
	}

	public void loadImage(Handler handler)
	{
		if (HttpUtil.isNetworkAvailable(getContext()))
		{
			this.handler = handler;
			LoadSplashTask task = new LoadSplashTask();
			task.execute();
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("Network Error");
			builder.setMessage("There is no available network, Please check your network state.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					((Activity)getContext()).finish();
				}
			});
			builder.create().show();
		}
	}
	
	public void clearBitmap()
	{
		imageArtwork.setImageBitmap(null);
	}

	class LoadSplashTask extends AsyncTask<Void,Void,String> implements BitmapDownloader.BitmapDownloaderListener
	{
		@Override
		protected String doInBackground(Void... arg0)
		{
			return ArtworkHandler.getCoverImageURL();
		}

		@Override
		protected void onPostExecute(String result)
		{
			if(result == null){
				imageArtwork.postDelayed(new Runnable() {

					@Override
					public void run()
					{
						Message msg = handler.obtainMessage(BigtureEnvironment.EVENT_CODE_CLOSE_SPLASH);
						handler.sendMessage(msg);
					}
					
				}, 3000);
				return;
			}
			
			String imageURL = ServerStaticVariable.ImageURL + result;
			
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmapOptions.inSampleSize = 2;

			if(imageURL != null)
				BitmapDownloader.getInstance().downloadBitmap(imageURL, bitmapOptions, this);
			super.onPostExecute(result);
		}

		@Override
		public void onComplete(Bitmap bitmap)
		{
			imageArtwork.setImageBitmap(bitmap);
			Message msg = handler.obtainMessage(BigtureEnvironment.EVENT_CODE_SPLASH_LOADED);
			handler.sendMessage(msg);
			
			imageArtwork.postDelayed(new Runnable() {

				@Override
				public void run()
				{
					Message msg = handler.obtainMessage(BigtureEnvironment.EVENT_CODE_CLOSE_SPLASH);
					handler.sendMessage(msg);
				}
				
			}, 3000);
		}		
	}
}
