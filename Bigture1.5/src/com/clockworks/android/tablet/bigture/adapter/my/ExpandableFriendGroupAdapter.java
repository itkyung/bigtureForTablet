package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;

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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpandableFriendGroupAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<FriendGroupEntity> groups;
	private GroupSelectListener listener;
	private BitmapFactory.Options options;
	
	
	public ExpandableFriendGroupAdapter(Context context,
			ArrayList<FriendGroupEntity> groups,GroupSelectListener listener) {
		super();
		this.context = context;
		this.groups = groups;
		this.listener = listener;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
	}
	
	

	public ArrayList<FriendGroupEntity> getGroups() {
		return groups;
	}



	public void setGroups(ArrayList<FriendGroupEntity> groups) {
		this.groups = groups;
	}



	@Override
	public Object getChild(int groupPosition, int childPosition) {
		FriendGroupEntity group = (FriendGroupEntity)getGroup(groupPosition);
		if(group != null && group.members != null){
			return group.members.get(childPosition);
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.cell_friend_selectable , null);
			UserViewHolder viewHolder = new UserViewHolder(convertView,this);
			convertView.setTag(viewHolder);
		}
		
		FriendEntity entity = (FriendEntity)getChild(groupPosition, childPosition);

		UserViewHolder viewHolder = (UserViewHolder)convertView.getTag();
		viewHolder.updateView(entity,convertView);
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		FriendGroupEntity group = (FriendGroupEntity)getGroup(groupPosition);
		if(group != null && group.members != null){
			return group.members.size();
		}else{
			return 0;
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this.groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		if(convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.cell_group_selectable , null);
			GroupViewHolder viewHolder = new GroupViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		FriendGroupEntity entity = groups.get(groupPosition);

		GroupViewHolder viewHolder = (GroupViewHolder)convertView.getTag();
		viewHolder.updateView(entity,isExpanded,groupPosition);
		
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	class GroupViewHolder{
		FriendGroupEntity entity;
		Button btnSelect;
		ImageView imgFold;
		TextView groupTitleLabel;
		int position=-1;
		
		public GroupViewHolder(View parent){
			btnSelect = (Button)parent.findViewById(R.id.btnGroupSelect);
				
			btnSelect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(btnSelect.isSelected()){
						btnSelect.setSelected(false);
						entity.selected = false;
						listener.selectGroup(entity, false);
					}else{
						btnSelect.setSelected(true);
						entity.selected = true;
						listener.selectGroup(entity, true);
					}
				}
			});
			
			
			groupTitleLabel = (TextView)parent.findViewById(R.id.groupTitleLabel);
			groupTitleLabel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position != -1)
						listener.clickGroup(position);
				}
			});
			imgFold = (ImageView)parent.findViewById(R.id.imgFold);
			
		}
		
		public void updateView(FriendGroupEntity entity,boolean isExpanded,int position){
			this.entity = entity;
			this.position = position;
			
			groupTitleLabel.setText(entity.groupName + "(" + entity.friendCount + ")");
			
			if(entity.index.equals("-1")){
				btnSelect.setVisibility(View.GONE);
			}else{
				btnSelect.setVisibility(View.VISIBLE);
				btnSelect.setSelected(entity.selected);
			}
			if(isExpanded){
				imgFold.setImageResource(R.drawable.common_fold_arrow_open);
			}else{
				imgFold.setImageResource(R.drawable.common_fold_arrow_close);
			}
			
		
		}
		
	}

	class UserViewHolder{
		public Button btnFSelect;
		public FriendEntity entity;
		public TextView nameLabel;
		public TextView countryLabel;
		public ImageView imageProfile;
		public TextView jobLabel;
		public RelativeLayout wrapper;
		
		public UserViewHolder(View parent,final ExpandableFriendGroupAdapter adapter){
			wrapper = (RelativeLayout)parent.findViewById(R.id.cellWrapper);
			btnFSelect = (Button)parent.findViewById(R.id.btnFriendSelect);
			
			btnFSelect.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(btnFSelect.isSelected()){
						btnFSelect.setSelected(false);
						entity.selected = false;
						listener.selectFriend(entity, false);
						wrapper.setBackgroundResource(R.color.transparent);
					}else{
						boolean duplicated = listener.selectFriend(entity, true);
						if(!duplicated){
							btnFSelect.setSelected(true);
							entity.selected = true;
							wrapper.setBackgroundResource(R.color.popup_list_selected);
						}
					}
					adapter.notifyDataSetChanged();
				}
			});
			
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			
			
			countryLabel = (TextView)parent.findViewById(R.id.countryLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			jobLabel = (TextView)parent.findViewById(R.id.jobTitleLabel);
			
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
				btnFSelect.setSelected(true);
				wrapper.setBackgroundResource(R.color.popup_list_selected);
			}else{
				btnFSelect.setSelected(false);
				wrapper.setBackgroundResource(R.color.transparent);
			}
		}
	}
	
	
	public interface GroupSelectListener{
		public void selectGroup(FriendGroupEntity group,boolean selected);
		public boolean selectFriend(FriendEntity friend,boolean selected);
		public void clickGroup(int groupPosition);
	}
	
}
