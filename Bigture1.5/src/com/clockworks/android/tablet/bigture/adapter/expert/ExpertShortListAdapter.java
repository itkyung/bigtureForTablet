package com.clockworks.android.tablet.bigture.adapter.expert;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ExpertWordWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.views.expert.ExpertListCell;
import com.clockworks.android.tablet.bigture.views.expert.ExpertSectionItem;
import com.clockworks.android.tablet.bigture.views.expert.ExpertShortListCell;
import com.clockworks.android.tablet.bigture.views.expert.ExpertThumbnail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ExpertShortListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ExpertSectionItem> listItem;
	private ExpertThumbnail.Callback callback;
	private ArrayList<SimpleUserEntity> experts;
	
	public ExpertShortListAdapter(Context context,ExpertThumbnail.Callback callback){
		this.context = context;
		this.listItem = new ArrayList<ExpertSectionItem>();
		this.callback = callback;
	}
	
	public void setExperts(ArrayList<SimpleUserEntity> experts){
		this.experts = experts;
		this.listItem.clear();
		
		ExpertSectionItem item = null;
		for(int i=0; i < experts.size(); i++){
			if(i == 0 || i % 5 == 0){
				item = new ExpertSectionItem();
				item.setExperts(new ArrayList<SimpleUserEntity>());
				this.listItem.add(item);
			}
			SimpleUserEntity entity = experts.get(i);
			item.getExperts().add(entity);		
		}
	}
	
	
	@Override
	public int getCount() {
		
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ExpertSectionItem item = (ExpertSectionItem)getItem(position);
		ExpertShortListCell layout = (ExpertShortListCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (ExpertShortListCell)inflater.inflate(R.layout.cell_expert_short_list, null);
		}
		
		layout.updateView(this.callback, item.getExperts());
		return layout;
		
	}

}
