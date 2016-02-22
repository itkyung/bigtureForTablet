package com.clockworks.android.tablet.bigture.adapter.story;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassShortCell;
import com.clockworks.android.tablet.bigture.views.story.StorySectionItem;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.clockworks.android.tablet.bigture.views.story.listcell.ShortCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class StoryShortAdapter extends BaseAdapter {
	private Context context;
	private BookThumbnail.Callback callback;
	private ArrayList<StorySectionItem> listItem;
	
	public StoryShortAdapter(Context context,BookThumbnail.Callback callback){
		this.context = context;
		this.callback = callback;
		this.listItem = new ArrayList<StorySectionItem>();
	}
	
	public void setStoryItem(ArrayList<StoryEntity> stories) {
		this.listItem.clear();
		
		StorySectionItem item = null;
		for(int i=0; i < stories.size(); i++){
			//3개마다 하나의 item을 만든다.
			if(i == 0 || i % 3 == 0){
				item = new StorySectionItem();
				item.setStories(new ArrayList<StoryEntity>());
				this.listItem.add(item);
			}
			StoryEntity entity = stories.get(i);
			item.getStories().add(entity);
		}
	}

	
	
	@Override
	public int getCount() {
		return this.listItem.size();
	}

	@Override
	public Object getItem(int position) {
		StorySectionItem item = this.listItem.get(position);
		return item;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShortCell layout = (ShortCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ShortCell)inflater.inflate(R.layout.cell_story_short_list, null);
		}
		StorySectionItem item = (StorySectionItem)getItem(position);
		layout.updateView(callback, item.getStories(), position, false);
		return layout;
	}


}
