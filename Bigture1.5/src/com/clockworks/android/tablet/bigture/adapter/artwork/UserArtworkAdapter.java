package com.clockworks.android.tablet.bigture.adapter.artwork;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;


import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserArtworkAdapter extends BaseAdapter {
	private Context context;
	private ArtworkThumbnail.Callback callback;
	private ArrayList<ArtworkSectionItem> listItem;
	private boolean myPage;
	private ArrayList<ArtworkEntity> artworkList;
	
	public UserArtworkAdapter(Context context, ArtworkThumbnail.Callback callback,boolean myPage) {
		super();
		this.context = context;
		
		this.callback = callback;
		this.listItem = new ArrayList<ArtworkSectionItem>();
		this.myPage = myPage;
	}
	
	public void setArtworkList(ArrayList<ArtworkEntity> artworkList) {
		this.listItem.clear();
		this.artworkList = artworkList;
		convertGroupItems(artworkList);
	}
	
	public ArrayList<ArtworkEntity> getArtworkList() {
		return artworkList;
	}

	private void convertGroupItems(ArrayList<ArtworkEntity> artworkList){
		ArrayList<Integer> keyList = new ArrayList<Integer>();
		SparseArray<ArrayList<ArtworkEntity>> map = new SparseArray<ArrayList<ArtworkEntity>>();
		
		for(ArtworkEntity entity : artworkList){
			ArrayList<ArtworkEntity> subList = null;
			subList = map.get(entity.createYear);
			if(subList == null){
				subList = new ArrayList<ArtworkEntity>();
				map.put(entity.createYear, subList);
				keyList.add(entity.createYear);
			}
			subList.add(entity);
		}
		
		for(Integer key : keyList){
			ArrayList<ArtworkEntity> artworks = map.get(key);
			ArtworkSectionItem item = null;
			for(int i=0; i < artworks.size(); i++){
				//4개마다 하나의 item을 만든다.
				if(i == 0 || i % 4 == 0){
					item = new ArtworkSectionItem();
					item.setArtworks(new ArrayList<ArtworkEntity>());
					this.listItem.add(item);
				}
				ArtworkEntity entity = artworks.get(i);
				item.getArtworks().add(entity);
			}
			
			ArtworkSectionItem section = new ArtworkSectionItem();
			section.setSectionTitle(key.toString());
			section.setSection(true);
			this.listItem.add(section);
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
	public int getItemViewType(int position) {
		ArtworkSectionItem item = this.listItem.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtworkSectionItem item = this.listItem.get(position);
		if(item.isSection()){
			//Section
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_artwork_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getSectionTitle());
			return convertView;
		}else{
			//Item..
			ArtworkListCell layout = (ArtworkListCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (ArtworkListCell)inflater.inflate(R.layout.cell_artwork_list, null);
			}
			
			layout.updateView(callback, item.getArtworks(), myPage);
			return layout;
		}
		
	}


}
