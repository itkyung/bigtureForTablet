package com.clockworks.android.tablet.bigture.adapter.expert;

import java.util.ArrayList;
import java.util.HashMap;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ExpertWordWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.views.expert.ExpertListCell;
import com.clockworks.android.tablet.bigture.views.expert.ExpertSectionItem;
import com.clockworks.android.tablet.bigture.views.expert.ExpertThumbnail;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ExpertListAdapter extends BaseAdapter implements SectionIndexer {
	private Context context;
	private ArrayList<ExpertWordWrapper> expertWords;
	private ArrayList<ExpertSectionItem> listItem;
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	private ExpertThumbnail.Callback callback;
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
	
	
	public ExpertListAdapter(Context context,ExpertThumbnail.Callback callback){
		this.context = context;
		this.expertWords = new ArrayList<ExpertWordWrapper>();
		this.listItem = new ArrayList<ExpertSectionItem>();
		this.callback = callback;
		
		
	}
	
	public void setExpertList(ArrayList<ExpertWordWrapper> experts){
		this.expertWords = experts;
		this.listItem.clear();
	
		convertToSection();
		
	}
	
	
	private void convertToSection(){
		int j=0;
		
		for(ExpertWordWrapper wrapper : expertWords){
			ExpertSectionItem section = new ExpertSectionItem();
			section.setSection(true);
			section.setSectionTitle(wrapper.word);
			sectionMap.put(wrapper.word.toLowerCase(),j);
			
			j++;
			this.listItem.add(section);
			
			ExpertSectionItem item = null;
			for(int i=0; i < wrapper.experts.size(); i++){
				if(i == 0 || i % 7 == 0){
					item = new ExpertSectionItem();
					item.setExperts(new ArrayList<SimpleUserEntity>());
					j++;
					this.listItem.add(item);
				}
				SimpleUserEntity entity = wrapper.experts.get(i);
				item.getExperts().add(entity);		
			}
		}
	}
	
	
	public HashMap<String, Integer> getSectionMap() {
		return sectionMap;
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
		ExpertSectionItem item = this.listItem.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpertSectionItem item = (ExpertSectionItem)getItem(position);
		if(item.isSection()){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_expert_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getSectionTitle());
			return convertView;
			
		}else{
			ExpertListCell layout = (ExpertListCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (ExpertListCell)inflater.inflate(R.layout.cell_expert_list, null);
			}
			
			layout.updateView(this.callback, item.getExperts());
			return layout;
			
		}
		
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		String letter = sections[sectionIndex];
		if(letter == null){
			return 0;
		}
		return sectionMap.get(letter.toLowerCase());
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 1;
	}

	public int getPositionForSection(String section){
		String key = section.toLowerCase();
		if(sectionMap.containsKey(key)){
			return sectionMap.get(key);
		}else{
			return -1;
		}
	}
	
	@Override
	public Object[] getSections() {
		return sections;
	}



	
	
}
