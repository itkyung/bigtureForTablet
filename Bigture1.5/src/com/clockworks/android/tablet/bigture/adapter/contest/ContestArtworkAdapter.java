package com.clockworks.android.tablet.bigture.adapter.contest;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestWinnerEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSmallListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.artwork.ContestArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ContestArtworkThumbnail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContestArtworkAdapter extends BaseAdapter {
	private Context context;
	private ContestArtworkThumbnail.Callback callback;
	private ArrayList<ArtworkSectionItem> listItem;
	
	public ContestArtworkAdapter(Context context,ContestArtworkThumbnail.Callback callback){
		this.context = context;
		this.callback = callback;
		this.listItem = new ArrayList<ArtworkSectionItem>();
	}
	
	public void setArtworks(ArrayList<ArtworkEntity> artworks){
		this.listItem.clear();
		convertGroupItems(artworks);
	}
	
	private void convertGroupItems(ArrayList<ArtworkEntity> artworkList){
		ArtworkSectionItem item = null;
		for(int i=0; i < artworkList.size(); i++){
			//3개마다 하나의 item을 만든다.
			if(i == 0 || i % 3 == 0){
				item = new ArtworkSectionItem();
				item.setArtworks(new ArrayList<ArtworkEntity>());
				this.listItem.add(item);
			}
			ArtworkEntity entity = artworkList.get(i);
			item.getArtworks().add(entity);
		}
	}

	
	@Override
	public int getCount() {
		return this.listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return this.listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtworkSectionItem item = this.listItem.get(position);
		//Item..
		ContestArtworkListCell layout = (ContestArtworkListCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ContestArtworkListCell)inflater.inflate(R.layout.cell_contest_artwork_list, null);
		}
		
		layout.updateView(callback, item.getArtworks());
		return layout;
		
	}

}
