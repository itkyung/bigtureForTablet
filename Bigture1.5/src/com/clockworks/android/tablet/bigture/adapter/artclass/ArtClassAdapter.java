package com.clockworks.android.tablet.bigture.adapter.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassCell;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ArtClassAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ArtClassSectionItem> listItem;
	private boolean isOwn;
	private boolean isPro;
	private ClassThumbnail.Callback callback;
	
	public ArtClassAdapter(Context context,ClassThumbnail.Callback callback,boolean isPro){
		this.context = context;
		this.callback = callback;
		this.isPro = isPro;
		this.isOwn = false;
		this.listItem = new ArrayList<ArtClassSectionItem>();
	}
	
	public void setClassItem(ArrayList<ArtClassEntity> classes,boolean isOwn) {
		this.listItem.clear();
		this.isOwn = isOwn;
		
		ArrayList<ArtClassEntity> realClasses = new ArrayList<ArtClassEntity>();
		if(isOwn && isPro){
			ArtClassEntity dummy = new ArtClassEntity();
			dummy.index = null;
			realClasses.add(dummy);
		}
		
		realClasses.addAll(classes);
		
		ArtClassSectionItem item = null;
		for(int i=0; i < realClasses.size(); i++){
			//4개마다 하나의 item을 만든다.
			if(i == 0 || i % 4 == 0){
				item = new ArtClassSectionItem();
				item.setClasses(new ArrayList<ArtClassEntity>());
				this.listItem.add(item);
			}
			ArtClassEntity entity = realClasses.get(i);
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
		ClassCell layout = (ClassCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ClassCell)inflater.inflate(R.layout.cell_artclass_list, null);
		}
		ArtClassSectionItem item = (ArtClassSectionItem)getItem(position);
		layout.updateView(callback, item.getClasses(), position,isOwn);
		return layout;
	}

}
