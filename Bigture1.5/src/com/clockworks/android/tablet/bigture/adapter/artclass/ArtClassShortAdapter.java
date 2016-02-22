package com.clockworks.android.tablet.bigture.adapter.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassCell;
import com.clockworks.android.tablet.bigture.views.artclass.ClassShortCell;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArtClassShortAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ArtClassSectionItem> listItem;
	private ClassThumbnail.Callback callback;
	
	
	public ArtClassShortAdapter(Context context,ClassThumbnail.Callback callback){
		this.context = context;
		this.callback = callback;
		this.listItem = new ArrayList<ArtClassSectionItem>();
	}
	
	public void setClassItem(ArrayList<ArtClassEntity> classes) {
		this.listItem.clear();
		
		ArtClassSectionItem item = null;
		for(int i=0; i < classes.size(); i++){
			//3개마다 하나의 item을 만든다.
			if(i == 0 || i % 3 == 0){
				item = new ArtClassSectionItem();
				item.setClasses(new ArrayList<ArtClassEntity>());
				this.listItem.add(item);
			}
			ArtClassEntity entity = classes.get(i);
			item.getClasses().add(entity);
		}
	}

	
	
	@Override
	public int getCount() {
		return this.listItem.size();
	}

	@Override
	public Object getItem(int position) {
		ArtClassSectionItem item = this.listItem.get(position);
		return item;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ClassShortCell layout = (ClassShortCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ClassShortCell)inflater.inflate(R.layout.cell_artclass_short_list, null);
		}
		ArtClassSectionItem item = (ArtClassSectionItem)getItem(position);
		layout.updateView(callback, item.getClasses(), position);
		return layout;
	}


}
