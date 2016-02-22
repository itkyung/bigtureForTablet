package com.clockworks.android.tablet.bigture.adapter.artclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AlphabetIdxWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.utils.StringUtils;
import com.clockworks.android.tablet.bigture.views.artclass.ArtClassSectionItem;
import com.clockworks.android.tablet.bigture.views.artclass.ClassCell;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;
import com.clockworks.android.tablet.bigture.views.expert.ExpertSectionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArtClassAllAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ArtClassSectionItem> listItem;
	private boolean useSection;
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
	private ClassThumbnail.Callback callback;
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	
	public ArtClassAllAdapter(Context context,ClassThumbnail.Callback callback,boolean useSection){
		this.context = context;
		this.callback = callback;
		this.useSection = useSection;
		this.listItem = new ArrayList<ArtClassSectionItem>();
	}
	
	public void setClassItem(ArrayList<ArtClassEntity> classes,boolean useSection) {
		this.listItem.clear();
		this.useSection = useSection;
		
		if(this.useSection){
			//알파벳 section을 이용할 경우. 
			LinkedHashMap<String, ArrayList<ArtClassEntity>> sectionDataMap = convertToSectionList(classes);
			
			Iterator<String> iter = sectionDataMap.keySet().iterator();
			int i=0;
			while(iter.hasNext()){
				String key = iter.next();
				if(key.equals("#"))
					continue;
				ArtClassSectionItem item = new ArtClassSectionItem();
				item.setSection(true);
				item.setSectionTitle(key);
				sectionMap.put(key.toLowerCase(), i);
				i++;
				this.listItem.add(item);
				
				ArrayList<ArtClassEntity> subList = sectionDataMap.get(key.toLowerCase());
				for(int j=0; j < subList.size(); j++,i++){
					//4개마다 하나의 item을 만든다.
					if(j == 0 || j % 4 == 0){
						item = new ArtClassSectionItem();
						item.setClasses(new ArrayList<ArtClassEntity>());
						this.listItem.add(item);
					}
					ArtClassEntity entity = subList.get(j);
					item.getClasses().add(entity);
				}
			}
			
			if(sectionDataMap.containsKey("#")){
				String key = "#";
				ArtClassSectionItem item = new ArtClassSectionItem();
				item.setSection(true);
				item.setSectionTitle(key);
				sectionMap.put(key.toLowerCase(), i);
				i++;
				this.listItem.add(item);
				
				ArrayList<ArtClassEntity> subList = sectionDataMap.get(key.toLowerCase());
				for(int j=0; j < subList.size(); j++,i++){
					//4개마다 하나의 item을 만든다.
					if(j == 0 || j % 4 == 0){
						item = new ArtClassSectionItem();
						item.setClasses(new ArrayList<ArtClassEntity>());
						this.listItem.add(item);
					}
					ArtClassEntity entity = subList.get(j);
					item.getClasses().add(entity);
				}
			}
			
		}else{
			ArtClassSectionItem item = null;
			for(int i=0; i < classes.size(); i++){
				//4개마다 하나의 item을 만든다.
				if(i == 0 || i % 4 == 0){
					item = new ArtClassSectionItem();
					item.setClasses(new ArrayList<ArtClassEntity>());
					this.listItem.add(item);
				}
				ArtClassEntity entity = classes.get(i);
				item.getClasses().add(entity);
			}
		}
	}
	
	private LinkedHashMap<String, ArrayList<ArtClassEntity>> convertToSectionList(ArrayList<ArtClassEntity> classes){
		LinkedHashMap<String, ArrayList<ArtClassEntity>> map = new LinkedHashMap<String, ArrayList<ArtClassEntity>>();
	
		for(ArtClassEntity artClass : classes){
			String title = artClass.className;
			char firstChar = StringUtils.getFirstElement(title);
			if( StringUtils.checkHangul(firstChar) || Character.isDigit(firstChar)){
				firstChar = '#';
			}
			ArrayList<ArtClassEntity> subList = null;
			String key = (firstChar+"").toLowerCase();
			if(map.containsKey(key)){
				subList = map.get(key);
			}else{
				subList = new ArrayList<ArtClassEntity>();
				map.put(key,subList);
			}
			subList.add(artClass);
		}
		
		
		return map;
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
	public int getItemViewType(int position) {
		ArtClassSectionItem item = this.listItem.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtClassSectionItem item = (ArtClassSectionItem)getItem(position);
		if(item.isSection()){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_expert_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getSectionTitle());
			return convertView;
			
		}else{
			ClassCell layout = (ClassCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (ClassCell)inflater.inflate(R.layout.cell_artclass_list, null);
			}
		
			layout.updateView(callback, item.getClasses(), position,false);
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
