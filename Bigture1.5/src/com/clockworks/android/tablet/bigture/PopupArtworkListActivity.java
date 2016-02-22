package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artwork.SelectArtworkAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSimpleThumbnail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class PopupArtworkListActivity extends Activity implements ArtworkSimpleThumbnail.Callback{
	private Context context;
	private ListView listView;
	private SelectArtworkAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup_select_artwork);
		this.context = this;
		
		this.listView = (ListView)findViewById(R.id.listArtworks);
		this.adapter = new SelectArtworkAdapter(this, this);
		this.listView.setAdapter(adapter);
		
		getArtworkData();
	}

	private void getArtworkData(){
		ArtworkListTask task = new ArtworkListTask();
		task.execute();
	}
	
	class ArtworkListTask extends AsyncTask<Void, Void, ArtworkPageWrapper>{

		@Override
		protected ArtworkPageWrapper doInBackground(Void... params) {
			
			return ArtworkHandler.listUserArtworks(AccountManager.getUserIdx(), null, null);
		}

		@Override
		protected void onPostExecute(ArtworkPageWrapper result) {
			
			super.onPostExecute(result);
			
			adapter.setArtworkList(result.artworks);
			adapter.notifyDataSetChanged();
			
			
			
		}
		
		
	}
	
	
	@Override
	public void onSelectArtwork(ArtworkEntity artwork) {
		Intent intent = new Intent();
		intent.putExtra("selectedArtwork", artwork);
		setResult(RESULT_OK, intent);
		finish();
	}

	
	
	
}
