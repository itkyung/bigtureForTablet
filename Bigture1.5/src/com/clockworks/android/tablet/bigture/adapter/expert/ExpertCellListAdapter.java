package com.clockworks.android.tablet.bigture.adapter.expert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.utils.StringUtils;
import com.clockworks.android.tablet.bigture.views.expert.ExpertSectionItem;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpertCellListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ExpertSectionItem> listItem;
	private ExpertClickListener listener;
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	private BitmapFactory.Options options;
	
	public ExpertCellListAdapter(Context context,ExpertClickListener listener){
		this.context = context;
		this.listItem = new ArrayList<ExpertSectionItem>();
		this.listener = listener;
		
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
	}
	
	public void setExperts(ArrayList<SimpleUserEntity> experts){
		this.listItem.clear();
		
		LinkedHashMap<String, ArrayList<SimpleUserEntity>> sectionDataMap = convertToSectionList(experts);
		
		Iterator<String> iter = sectionDataMap.keySet().iterator();
		int i=0;
		while(iter.hasNext()){
			String key = iter.next();
			if(key.equals("#"))
				continue;
			ExpertSectionItem item = new ExpertSectionItem();
			item.setSection(true);
			item.setSectionTitle(key);
			sectionMap.put(key.toLowerCase(), i);
			i++;
			this.listItem.add(item);
			
			ArrayList<SimpleUserEntity> subList = sectionDataMap.get(key.toLowerCase());
			for(SimpleUserEntity expert : subList){
				ExpertSectionItem item2 = new ExpertSectionItem();
				item2.setSection(false);
				item2.setEntity(expert);
				this.listItem.add(item2);
			}	
		}
		
		if(sectionDataMap.containsKey("#")){
			String key = "#";
			ExpertSectionItem item = new ExpertSectionItem();
			item.setSection(true);
			item.setSectionTitle(key);
			sectionMap.put(key.toLowerCase(), i);
			i++;
			this.listItem.add(item);
			
			ArrayList<SimpleUserEntity> subList = sectionDataMap.get(key.toLowerCase());
			for(SimpleUserEntity expert : subList){
				ExpertSectionItem item2 = new ExpertSectionItem();
				item2.setSection(false);
				item2.setEntity(expert);
				this.listItem.add(item2);
			}	
			
		}
		
	}
	
	private LinkedHashMap<String, ArrayList<SimpleUserEntity>> convertToSectionList(ArrayList<SimpleUserEntity> experts){
		LinkedHashMap<String, ArrayList<SimpleUserEntity>> map = new LinkedHashMap<String, ArrayList<SimpleUserEntity>>();
	
		for(SimpleUserEntity expert : experts){
			String title = expert.name;
			char firstChar = StringUtils.getFirstElement(title);
			if( StringUtils.checkHangul(firstChar) || Character.isDigit(firstChar)){
				firstChar = '#';
			}
			ArrayList<SimpleUserEntity> subList = null;
			String key = (firstChar+"").toLowerCase();
			if(map.containsKey(key)){
				subList = map.get(key);
			}else{
				subList = new ArrayList<SimpleUserEntity>();
				map.put(key,subList);
			}
			subList.add(expert);
		}
		
		
		return map;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpertSectionItem item = (ExpertSectionItem)getItem(position);
		if(item.isSection()){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_job_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getSectionTitle());
			return convertView;
			
		}else{
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(this.context);
				convertView = inflater.inflate(R.layout.cell_expert_small, null);
				ViewHolder viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}
			
			SimpleUserEntity entity = item.getEntity();

			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			viewHolder.updateView(entity);
			
			return convertView;
		}
		
	}

	public interface ExpertClickListener{
		public void onSelectExpert(SimpleUserEntity entity);
	}
	
	class ViewHolder{
		SimpleUserEntity entity;
		public TextView nameLabel;
		public TextView countryLabel;
		public ImageView imageProfile;
		public TextView jobLabel;
		public Button btnLike;
		public Button btnUnlike;
		public Button btnWait;
		
		public ViewHolder(View parent){
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			countryLabel = (TextView)parent.findViewById(R.id.countryLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			
			jobLabel = (TextView)parent.findViewById(R.id.jobLabel);
			
			imageProfile.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){

				}
			});
		
			parent.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.onSelectExpert(entity);
				}
			});
			
		}
		
		public void updateView(SimpleUserEntity entity){
			this.entity = entity;
			nameLabel.setText(entity.name);
			countryLabel.setText(entity.country);
			countryLabel.setVisibility(View.GONE);
			
			jobLabel.setVisibility(View.VISIBLE);
			jobLabel.setText(entity.jobName);
			
			String imageURL = entity.getProfileImageURL();
			if (imageURL != null)
				BitmapDownloader.getInstance().displayImage(imageURL, options, imageProfile, null);
			else
				imageProfile.setImageResource(R.drawable.common_pofile_60x60);
			
		}
	}
}
