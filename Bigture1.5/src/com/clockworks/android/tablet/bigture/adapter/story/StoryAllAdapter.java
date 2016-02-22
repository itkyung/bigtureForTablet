package com.clockworks.android.tablet.bigture.adapter.story;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.utils.StringUtils;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassCell;
import com.clockworks.android.tablet.bigture.views.story.StorySectionItem;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.clockworks.android.tablet.bigture.views.story.listcell.NormalCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class StoryAllAdapter extends BaseAdapter {
	private boolean useSection;
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	private Context context;
	private BookThumbnail.Callback callback;
	private ArrayList<StorySectionItem> listItem;
	
	public StoryAllAdapter(Context context, BookThumbnail.Callback callback,boolean useSection){
		this.context = context;
		
		this.callback = callback;
		this.listItem = new ArrayList<StorySectionItem>();
		this.useSection = useSection;
	}
	
	public void setStoryItem(ArrayList<StoryEntity> stories,boolean useSection) {
		this.listItem.clear();
		this.useSection = useSection;
		
		if(this.useSection){
			//알파벳 section을 이용할 경우. 
			LinkedHashMap<String, ArrayList<StoryEntity>> sectionDataMap = convertToSectionList(stories);
			
			Iterator<String> iter = sectionDataMap.keySet().iterator();
			int i=0;
			while(iter.hasNext()){
				String key = iter.next();
				if(key.equals("#"))
					continue;
				StorySectionItem item = new StorySectionItem();
				item.setSection(true);
				item.setSectionTitle(key);
				sectionMap.put(key.toLowerCase(), i);
				i++;
				this.listItem.add(item);
				
				ArrayList<StoryEntity> subList = sectionDataMap.get(key.toLowerCase());
				for(int j=0; j < subList.size(); j++,i++){
					//4개마다 하나의 item을 만든다.
					if(j == 0 || j % 4 == 0){
						item = new StorySectionItem();
						item.setStories(new ArrayList<StoryEntity>());
						this.listItem.add(item);
					}
					StoryEntity entity = subList.get(j);
					item.getStories().add(entity);
				}
			}
			
			if(sectionDataMap.containsKey("#")){
				String key = "#";
				StorySectionItem item = new StorySectionItem();
				item.setSection(true);
				item.setSectionTitle(key);
				sectionMap.put(key.toLowerCase(), i);
				i++;
				this.listItem.add(item);
				
				ArrayList<StoryEntity> subList = sectionDataMap.get(key.toLowerCase());
				for(int j=0; j < subList.size(); j++,i++){
					//4개마다 하나의 item을 만든다.
					if(j == 0 || j % 4 == 0){
						item = new StorySectionItem();
						item.setStories(new ArrayList<StoryEntity>());
						this.listItem.add(item);
					}
					StoryEntity entity = subList.get(j);
					item.getStories().add(entity);
				}
			}
			
		}else{
			StorySectionItem item = null;
			for(int i=0; i < stories.size(); i++){
				//4개마다 하나의 item을 만든다.
				if(i == 0 || i % 4 == 0){
					item = new StorySectionItem();
					item.setStories(new ArrayList<StoryEntity>());
					this.listItem.add(item);
				}
				StoryEntity entity = stories.get(i);
				item.getStories().add(entity);
			}
		}
	}
	
	private LinkedHashMap<String, ArrayList<StoryEntity>> convertToSectionList(ArrayList<StoryEntity> stories){
		LinkedHashMap<String, ArrayList<StoryEntity>> map = new LinkedHashMap<String, ArrayList<StoryEntity>>();
	
		for(StoryEntity story : stories){
			String title = story.title;
			char firstChar = StringUtils.getFirstElement(title);
			if( StringUtils.checkHangul(firstChar) || Character.isDigit(firstChar)){
				firstChar = '#';
			}
			ArrayList<StoryEntity> subList = null;
			String key = (firstChar+"").toLowerCase();
			if(map.containsKey(key)){
				subList = map.get(key);
			}else{
				subList = new ArrayList<StoryEntity>();
				map.put(key,subList);
			}
			subList.add(story);
		}
		return map;
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
	public int getItemViewType(int position) {
		StorySectionItem item = this.listItem.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StorySectionItem item = (StorySectionItem)getItem(position);
		if(item.isSection()){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_expert_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getSectionTitle());
			return convertView;
			
		}else{
			NormalCell layout = (NormalCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (NormalCell)inflater.inflate(R.layout.cell_story_list, null);
			}
		
			layout.updateView(callback, item.getStories(), position,false);
			return layout;
		}
	}
	
	public int getPositionForSection(String section){
		String key = section.toLowerCase();
		if(sectionMap.containsKey(key)){
			return sectionMap.get(key);
		}else{
			return -1;
		}
	}
	
	public Object[] getSections() {
		return sections;
	}

}
