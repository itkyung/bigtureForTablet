package com.clockworks.android.tablet.bigture.adapter.artwork;

import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkDetailCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArtworkDetailAdapter extends BaseAdapter {
	Context context;

	boolean fromMyBigture;
	List<ArtworkEntity> artworkList;
	ArtworkEntity currentArtwork;
	
	public boolean scrolling;
	ArtworkDetailCell.Callback callback;
	
	public ArtworkDetailAdapter(Context context, List<ArtworkEntity> artworkList,  ArtworkDetailCell.Callback callback,boolean fromMyBigture){
		this.context = context;
		this.callback = callback;
		this.artworkList = artworkList;
		this.fromMyBigture = fromMyBigture;
	}
	
	
	@Override
	public int getCount() {
		return artworkList.size();
	}

	@Override
	public Object getItem(int position) {
		return artworkList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtworkDetailCell cell = (ArtworkDetailCell)convertView;
		
		if (cell == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			cell = (ArtworkDetailCell)inflater.inflate(R.layout.cell_artwork_detail, parent, false);
		}
		
		ArtworkEntity entity = (ArtworkEntity)getItem(position);
		cell.updateCell(entity, callback,fromMyBigture);
		
		
		if (position == 0){
			cell.setPadding(0, 20, 0, 0);
		}else if (position == artworkList.size() - 1){
			cell.setPadding(0, 0, 0, 20);
		}
		else
		{
			cell.setPadding(0, 0, 0, 0);
		}

		
		return cell;
	}

}
