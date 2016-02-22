package com.clockworks.android.tablet.bigture.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.NotificationClickListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationGroup;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationRow;
import com.clockworks.android.tablet.bigture.views.NotificationItemCell;
import com.clockworks.android.tablet.bigture.views.NotificationSectionCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NotificationAdapter extends BaseAdapter {
	private Context context;
	private String today;

	private ArrayList<NotificationRow> rows;
	private DateFormat fm = new SimpleDateFormat("MM-dd-yyyy");
	private NotificationClickListener listener;
	
	public NotificationAdapter(Context context,NotificationClickListener listener){
		this.context = context;
		this.rows = new ArrayList<NotificationRow>();
		this.today = fm.format(new Date());
		this.listener = listener;
	}
	
	public void setNotifications(ArrayList<NotificationGroup> datas){
		this.rows.clear();
		for(NotificationGroup group : datas){
			NotificationRow row = new NotificationRow();
			row.setSection(true);
			row.setCreateDate(group.getCreateDate());
			rows.add(row);
			for(NotificationEntity noti : group.getNotifications()){
				NotificationRow sub = new NotificationRow();
				sub.setSection(false);
				sub.setNotification(noti);
				rows.add(sub);
			}
		}
		
	}
	
	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Object getItem(int position) {
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		NotificationRow item = this.rows.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NotificationRow item = (NotificationRow)getItem(position);
		if(item.isSection()){
			NotificationSectionCell layout = (NotificationSectionCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (NotificationSectionCell)inflater.inflate(R.layout.cell_notification_section, null);
			}
			String date = fm.format(item.getCreateDate());
			if(date.equals(today)){
				date = "Today";
			}
			
			layout.updateView(item.getCreateDate(),date, listener);
			return layout;
		}else{
			NotificationItemCell layout = (NotificationItemCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (NotificationItemCell)inflater.inflate(R.layout.cell_notification_item, null);
			}
			
			
			layout.updateView(item.getNotification(), listener);
			return layout;
			
		}
		
	}

	
	
}
