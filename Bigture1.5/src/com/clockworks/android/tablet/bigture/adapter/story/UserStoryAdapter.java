package com.clockworks.android.tablet.bigture.adapter.story;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.story.StorySectionItem;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.clockworks.android.tablet.bigture.views.story.listcell.NormalCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class UserStoryAdapter extends BaseAdapter {
	private Context context;
	private BookThumbnail.Callback callback;
	private ArrayList<StorySectionItem> listItem;
	private boolean myPage;
	private boolean isPro;
	
	public UserStoryAdapter(Context context, BookThumbnail.Callback callback,boolean myPage,boolean isPro){
		this.context = context;
		
		this.callback = callback;
		this.listItem = new ArrayList<StorySectionItem>();
		this.myPage = myPage;
		this.isPro = isPro;
	}

	public void setStoryItem(ArrayList<StoryEntity> stories) {
		this.listItem.clear();
		
		//UserStory에서는 dummy가 필요없다.
		ArrayList<StoryEntity> realStories = new ArrayList<StoryEntity>();
//		if(myPage && isPro){
//			StoryEntity dummy = new StoryEntity();
//			dummy.storyId = null;
//			realStories.add(dummy);
//		}
//		
		realStories.addAll(stories);
		
		StorySectionItem item = null;
		for(int i=0; i < realStories.size(); i++){
			//4개마다 하나의 item을 만든다.
			if(i == 0 || i % 4 == 0){
				item = new StorySectionItem();
				item.setStories(new ArrayList<StoryEntity>());
				this.listItem.add(item);
			}
			StoryEntity entity = realStories.get(i);
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
		NormalCell layout = (NormalCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (NormalCell)inflater.inflate(R.layout.cell_story_list, null);
		}
		StorySectionItem item = (StorySectionItem)getItem(position);
		layout.updateView(callback, item.getStories(), position,myPage);
		return layout;
	}

}
