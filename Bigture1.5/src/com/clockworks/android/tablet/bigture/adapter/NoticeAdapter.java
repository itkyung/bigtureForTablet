package com.clockworks.android.tablet.bigture.adapter;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NoticeEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoticeAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<NoticeEntity> noticeList;
	
	public NoticeAdapter(Context context){
		this.context = context;
		this.noticeList = new ArrayList<NoticeEntity>();
	}
	
	public void setNotice(ArrayList<NoticeEntity> datas){
		noticeList.clear();
		noticeList.addAll(datas);
	}
	
	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public Object getItem(int position) {
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final NoticeEntity entity = (NoticeEntity)getItem(position);

		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_notice, null);
			
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.titleLabel = (TextView)convertView.findViewById(R.id.titleLabel);
			viewHolder.timeLabel  = (TextView)convertView.findViewById(R.id.timeLabel);
			viewHolder.contentLabel = (TextView)convertView.findViewById(R.id.contentLabel);
			viewHolder.imageArr = (ImageView)convertView.findViewById(R.id.imageArr);
			viewHolder.contentContainer = (LinearLayout)convertView.findViewById(R.id.contentContainer);
			
			viewHolder.imageArr.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if (entity.expand)
						entity.expand = false;
					else
						entity.expand = true;
					
					NoticeAdapter.this.notifyDataSetChanged();
				}
				
			});
			
			viewHolder.titleLabel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (entity.expand)
						entity.expand = false;
					else
						entity.expand = true;
					
					NoticeAdapter.this.notifyDataSetChanged();
					
				}
			});
			
			convertView.setTag(viewHolder);
		}
		
		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		
		if (entity.expand){
			viewHolder.imageArr.setImageResource(R.drawable.infocenter_notice_list_fold_p);
			viewHolder.contentContainer.setVisibility(View.VISIBLE);
		}else{
			viewHolder.imageArr.setImageResource(R.drawable.infocenter_notice_list_fold_n);
			viewHolder.contentContainer.setVisibility(View.GONE);
		}
		
		viewHolder.titleLabel.setText(entity.title);
		viewHolder.contentLabel.setText(entity.content);
		viewHolder.timeLabel.setText(DateUtil.getTimeString(entity.created));
		
		
		return convertView;
	}

	static class ViewHolder
	{
		public LinearLayout contentContainer;
		
		public TextView titleLabel;
		public TextView contentLabel;
		public ImageView imageArr;
		public TextView timeLabel;
	}
}
