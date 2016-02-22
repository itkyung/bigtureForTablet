package com.clockworks.android.tablet.bigture.adapter.story;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtworkDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.ShowArtworkInterface;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AfterReadEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.story.listcell.NormalCell;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AfterReadAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<AfterReadEntity> artworks;
	BitmapFactory.Options bitmapOptions;
	ShowArtworkInterface artworkInterface;
	
	public AfterReadAdapter(Context context, ArrayList<AfterReadEntity> artworks) {
		super();
		this.context = context;
		this.artworks = artworks;
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
	}

	public void setArtworks(ArrayList<AfterReadEntity> artworks){
		this.artworks = artworks;
		this.artworkInterface = artworkInterface;
	}
	
	public void setArtworkInterface(ShowArtworkInterface artworkInterface){
		this.artworkInterface = artworkInterface;
	}
	
	@Override
	public int getCount() {
		return artworks.size();
	}

	@Override
	public Object getItem(int position) {
		return artworks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AfterReadEntity entity = (AfterReadEntity)getItem(position);
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_afterread, null);
		}
		
		ImageView imageArtwork = (ImageView)convertView.findViewById(R.id.imageArtwork);
		String imgUrl = entity.getThumbnailURL();
		if(imgUrl != null){
			BitmapDownloader.getInstance().displayImage(imgUrl, bitmapOptions, imageArtwork, null);
		}
		TextView title = (TextView)convertView.findViewById(R.id.afterTitle);
		title.setText(entity.getTitle());
		
		imageArtwork.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ArtworkLoadTask().execute(entity.getArtworkId());
			}
		});
		
		return convertView;
	}

	class ArtworkLoadTask extends AsyncTask<String, Void, ArtworkEntity>{

		@Override
		protected ArtworkEntity doInBackground(String... params) {
			return ArtworkHandler.loadArtwork(params[0]);
		}

		@Override
		protected void onPostExecute(ArtworkEntity result) {
			super.onPostExecute(result);
			artworkInterface.onShowArtworkDetail(result);
		}
		
	}
}
