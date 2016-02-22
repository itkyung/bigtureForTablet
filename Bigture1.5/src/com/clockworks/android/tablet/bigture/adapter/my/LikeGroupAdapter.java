package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class LikeGroupAdapter extends BaseAdapter {
	private ArrayList<FriendGroupEntity> groups;
	
	private Context context;
	private LikeGroupListListener listener;
	private boolean editMode=false;
	
	
	public LikeGroupAdapter(Context context, LikeGroupListListener listener) {
		super();
		this.context = context;
		this.listener = listener;
		
		this.groups = new ArrayList<FriendGroupEntity>();
	}

	@Override
	public int getCount() {
		return this.groups.size();
	}

	@Override
	public Object getItem(int position) {
		return this.groups.get(position);
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}
	
	
	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		final FriendGroupEntity group = this.groups.get(position);
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_group, null);
		}
		
		TextView groupNameLabel = (TextView)convertView.findViewById(R.id.groupTitleLabel);
		groupNameLabel.setText(group.groupName + "(" + group.friendCount + ")");
		
		if(!group.index.endsWith("-1")){
			groupNameLabel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(isEditMode()){
						listener.editGroupName(group);
					}else{
						listener.goDetailGroup(group);
					}
				}
			});
		}
		
		Button btnDetailGroup = (Button)convertView.findViewById(R.id.btnGroupSelect);
		btnDetailGroup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isEditMode()){
					listener.editGroupName(group);
				}else{
					listener.goDetailGroup(group);
				}
			}
		});
		
		final Button btnGroupRemoveStep1 = (Button)convertView.findViewById(R.id.btnGroupRemoveStep1);
		final Button btnGroupRemoveStep2 = (Button)convertView.findViewById(R.id.btnGroupRemoveStep2);
		
		if(!group.index.endsWith("-1")){
			
			//TODO 스와이프 이벤트를 잡아야한다.
			Button btnRemoveGroup = (Button)convertView.findViewById(R.id.btnGroupRemove);
			btnRemoveGroup.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.deleteGroup(group);
				}
			});
			
			
			btnGroupRemoveStep1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btnGroupRemoveStep1.setVisibility(View.GONE);
					btnGroupRemoveStep2.setVisibility(View.VISIBLE);
				}
			});
			
			btnGroupRemoveStep2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.deleteGroup(group);
				}
			});

			if(editMode){
				btnGroupRemoveStep1.setVisibility(View.VISIBLE);
				btnGroupRemoveStep2.setVisibility(View.GONE);
			}else{
				btnGroupRemoveStep1.setVisibility(View.GONE);
				btnGroupRemoveStep2.setVisibility(View.GONE);
			}
			
			
		}else{
			btnDetailGroup.setVisibility(View.GONE);
			btnGroupRemoveStep1.setVisibility(View.GONE);
			btnGroupRemoveStep2.setVisibility(View.GONE);
		}
		
		
		return convertView;
	}

	
	
	
	public ArrayList<FriendGroupEntity> getGroups() {
		return groups;
	}

	public void setGroups(ArrayList<FriendGroupEntity> groups) {
		this.groups = groups;
	}


	public interface LikeGroupListListener{
		public void goDetailGroup(FriendGroupEntity group);
		public void deleteGroup(FriendGroupEntity group);
		public void editGroupName(FriendGroupEntity group);
	}
}
