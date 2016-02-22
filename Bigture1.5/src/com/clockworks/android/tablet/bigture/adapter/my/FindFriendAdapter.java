package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;


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

public class FindFriendAdapter extends BaseAdapter {
	List<FriendEntity> users;
	Context context;
	private BitmapFactory.Options options;
	private FriendSelectListener listener;
	
	public FindFriendAdapter(Context context,FriendSelectListener listener){
		this.context = context;
		this.users = new ArrayList<FriendEntity>();
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		this.listener = listener;
	}
	
	public List<FriendEntity> getUsers() {
		return users;
	}

	public void setUsers(List<FriendEntity> users) {
		this.users = users;
	}


	@Override
	public int getCount(){
		return users.size();
	}

	@Override
	public Object getItem(int position){
		return users.get(position);
	}

	@Override
	public long getItemId(int arg0){
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2){
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.cell_friend_selectable, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		FriendEntity entity = users.get(position);

		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.updateView(entity,convertView);
		
		return convertView;
	}	

	public interface FriendSelectListener{
		public boolean selectFriend(FriendEntity friend,boolean selected);
	}
	
	class ViewHolder{
		FriendEntity entity;
		public TextView nameLabel;
		public TextView countryLabel;
		public ImageView imageProfile;
		public TextView jobLabel;
		public Button btnSelect;
		
		public ViewHolder(final View parent){
			
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			countryLabel = (TextView)parent.findViewById(R.id.countryLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			
			jobLabel = (TextView)parent.findViewById(R.id.jobTitleLabel);
			
			imageProfile.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){

				}
			});
		
			this.btnSelect = (Button)parent.findViewById(R.id.btnFriendSelect);
			btnSelect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(btnSelect.isSelected()){
						btnSelect.setSelected(false);
						entity.selected = false;
						listener.selectFriend(entity, false);
						parent.setBackgroundResource(R.color.transparent);
					}else{
						
						boolean duplicated = listener.selectFriend(entity, true);
						if(!duplicated){
							btnSelect.setSelected(true);
							entity.selected = true;
							parent.setBackgroundResource(R.color.popup_list_selected);
						}
					}
				}
			});
		}
		
		public void updateView(FriendEntity entity,View parent){
			this.entity = entity;
			nameLabel.setText(entity.nickName);
			countryLabel.setText(entity.country);
			
			if(entity.pro){
				jobLabel.setVisibility(View.VISIBLE);
				jobLabel.setText(entity.job);
			}else{
				jobLabel.setVisibility(View.GONE);
			}
			
			String imageURL = entity.photoPath;
			if (imageURL != null)
				BitmapDownloader.getInstance().displayImage(imageURL, options, imageProfile, null);
			else
				imageProfile.setImageResource(R.drawable.common_pofile_60x60);
			
			if(entity.selected){
				btnSelect.setSelected(true);
				parent.setBackgroundResource(R.color.popup_list_selected);
			}else{
				btnSelect.setSelected(false);
				parent.setBackgroundResource(R.color.transparent);
			}
			
		}
	}
	
	
}
