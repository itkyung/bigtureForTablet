package com.clockworks.android.tablet.bigture.adapter.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassCell;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;
import com.clockworks.android.tablet.bigture.views.story.StorySectionItem;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.clockworks.android.tablet.bigture.views.story.listcell.NormalCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserArtClassAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ArtClassSectionItem> listItem;
	private boolean myPage;
	private boolean isPro;
	private ClassThumbnail.Callback callback;
	
	public UserArtClassAdapter(Context context, ClassThumbnail.Callback callback,boolean myPage,boolean isPro){
		this.context = context;
		
		this.callback = callback;
		this.listItem = new ArrayList<ArtClassSectionItem>();
		this.myPage = myPage;
		this.isPro = isPro;
	}


	public void setClassItem(ArrayList<ArtClassEntity> classes) {
		this.listItem.clear();
		
		ArrayList<ArtClassEntity> realClasses = new ArrayList<ArtClassEntity>();
//		if(myPage && isPro){
//			ArtClassEntity dummy = new ArtClassEntity();
//			dummy.index = null;
//			realClasses.add(dummy);
//		}
		
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
		layout.updateView(callback, item.getClasses(), position,myPage);
		return layout;
	}

}
