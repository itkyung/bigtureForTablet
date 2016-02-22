package com.clockworks.android.tablet.bigture.adapter.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.views.artclass.ArtworkCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail.Callback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ArtworkAdapter extends BaseAdapter {
	private Context context;
	private ArtworkThumbnail.Callback callback;
	private ArrayList<ArtworkSectionItem> listItem;
	private ArrayList<ArtworkEntity> artworkList;
	private int currentPage;
	private int totalPage;
	private boolean canWrite;
	private boolean joined;
	
	public ArtworkAdapter(Context context, Callback callback) {
		super();
		this.context = context;
		this.callback = callback;
		this.listItem = new ArrayList<ArtworkSectionItem>();
	}

	public void setArtworkList(ArrayList<ArtworkEntity> artworks,int totalPage,int currentPage,boolean canWrite){
		this.listItem.clear();
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		this.canWrite = canWrite;
	
		if(artworks != null){
			this.artworkList = artworks;
			convertGroupItems(artworks);
		}
	}
	
	private void convertGroupItems(ArrayList<ArtworkEntity> artworkList){
		
		ArrayList<ArtworkEntity> realArtworks = new ArrayList<ArtworkEntity>();
		if(canWrite){
			ArtworkEntity dummy = new ArtworkEntity();
			dummy.artworkId = null;
			realArtworks.add(dummy);
		}
		
		realArtworks.addAll(artworkList);
		
		ArtworkSectionItem item = null;
		for(int i=0; i < realArtworks.size(); i++){
			//3개마다 하나의 item을 만든다.
			if(i == 0 || i % 3 == 0){
				item = new ArtworkSectionItem();
				item.setArtworks(new ArrayList<ArtworkEntity>());
				item.setFooterView(false);
				this.listItem.add(item);
			}
			ArtworkEntity entity = realArtworks.get(i);
			item.getArtworks().add(entity);
		}

		if(totalPage > 1){
		
		}
		
	}
	
	public ArrayList<ArtworkEntity> getArtworkList() {
		return artworkList;
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
	public int getViewTypeCount(){
		return 1;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtworkSectionItem item = this.listItem.get(position);
		
		ArtworkCell layout = (ArtworkCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ArtworkCell)inflater.inflate(R.layout.cell_artclass_artworks, null);
		}
		
		layout.updateView(callback, item.getArtworks(), position,canWrite);
		return layout;
	}

}
