package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;
import java.util.Iterator;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.my.FindFriendAdapter.ViewHolder;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedMemberAdapter extends BaseAdapter {
	private Context context;
	private BitmapFactory.Options options;
	private ArrayList<FriendEntity> members;
	private ArrayList<FriendEntity> removedMembers;
	private ArrayList<FriendEntity> addedMembers;
	GroupMemberListener listener;
	private SparseArray<FriendEntity> memberMap = new SparseArray<FriendEntity>();
	private boolean canOnlyRemoveNewAdded = false;	//새로 추가되는 멤버만 지울수 있는지 여부.
	
	public SelectedMemberAdapter(Context context,
			ArrayList<FriendEntity> members,GroupMemberListener listener,boolean canOnlyRemoveNewAdded) {
		super();
		this.context = context;
		this.members = members;
		this.addedMembers = new ArrayList<FriendEntity>();
		this.removedMembers = new ArrayList<FriendEntity>();
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		this.listener = listener;
		
		for(FriendEntity e : members){
			memberMap.put(Integer.parseInt(e.index), e);
		}
		this.canOnlyRemoveNewAdded = canOnlyRemoveNewAdded;
	}

	@Override
	public int getCount() {
		return this.members.size();
	}

	@Override
	public Object getItem(int position) {
		return this.members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(this.context);
			convertView = inflater.inflate(R.layout.cell_member_selectable, null);
			MemberHolder viewHolder = new MemberHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		FriendEntity entity = members.get(position);

		MemberHolder viewHolder = (MemberHolder)convertView.getTag();
		viewHolder.updateView(entity,convertView);
		
		return convertView;
	}
	
	public boolean addMembers(ArrayList<FriendEntity> friends){
		//맨위에 추가시킨다.addedMembers에도 추가한다. 
		boolean duplicated = false;
		ArrayList<FriendEntity> newMembers = new ArrayList<FriendEntity>();
		for(FriendEntity e : friends){
			if(memberMap.get(Integer.parseInt(e.index)) != null){
				duplicated = true;
				continue;
			}else{
				memberMap.put(Integer.parseInt(e.index), e);
			}
			FriendEntity f = new FriendEntity(e);
			f.selected = true;
			f.newAdded = true;
			newMembers.add(f);
		}
		
		this.addedMembers.addAll(newMembers);
		this.members.addAll(0, newMembers);
		
		this.notifyDataSetChanged();
		listener.onChangeGroupMember(getCount());
		
		return duplicated;
	}
	
	public void removeMember(FriendEntity entity,boolean oldMember){
		//oldMember = false이면 members와 addedMembers에서만 지우고
		//oldMember = true이면 false이면  removedMembers에 추가하고 member를 지운다.
		boolean canRemove = false;
		Iterator<FriendEntity> iter1 = this.members.iterator();
		while(iter1.hasNext()){
			FriendEntity e = iter1.next();
			if(e.index.equals(entity.index) && ((e.newAdded && !oldMember) || (!e.newAdded && oldMember))){
				iter1.remove();
				canRemove = true;
			}
		}
		
		if(canRemove){
			if(oldMember){
				this.removedMembers.add(entity);
			}else{
				Iterator<FriendEntity> iter = this.addedMembers.iterator();
				while(iter.hasNext()){
					FriendEntity e = iter.next();
					if(e.index.equals(entity.index))
						iter.remove();
				}
			}
			
			
			memberMap.remove(Integer.parseInt(entity.index));
		}
		this.notifyDataSetChanged();
		listener.onChangeGroupMember(getCount());
	}
	
	
	public ArrayList<FriendEntity> getRemovedMembers() {
		return removedMembers;
	}

	public void setRemovedMembers(ArrayList<FriendEntity> removedMembers) {
		this.removedMembers = removedMembers;
	}

	public ArrayList<FriendEntity> getAddedMembers() {
		return addedMembers;
	}

	public void setAddedMembers(ArrayList<FriendEntity> addedMembers) {
		this.addedMembers = addedMembers;
	}

	public interface GroupMemberListener{
		public void onChangeGroupMember(int memberCount);
	}
	
	public ArrayList<FriendEntity> getSelectedMembers(){
		return this.members;
	}

	class MemberHolder{
		FriendEntity entity;
		public TextView nameLabel;
		public TextView countryLabel;
		public ImageView imageProfile;
		public TextView jobLabel;
		public Button btnSelect;
		public Button btnRemove1;
		public Button btnRemove2;
		
		public MemberHolder(View parent){
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			countryLabel = (TextView)parent.findViewById(R.id.countryLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			
			jobLabel = (TextView)parent.findViewById(R.id.jobTitleLabel);
			
			imageProfile.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){

				}
			});
		
			btnSelect = (Button)parent.findViewById(R.id.btnFriendSelect);
			btnRemove1 = (Button)parent.findViewById(R.id.btnRemoveStep1);
			btnRemove1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					btnRemove2.setVisibility(View.VISIBLE);
					btnRemove1.setVisibility(View.GONE);
				}
			});
			
			btnRemove2 = (Button)parent.findViewById(R.id.btnRemoveStep2);
			btnRemove2.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(canOnlyRemoveNewAdded){
						removeMember(entity,false);
					}else{
						removeMember(entity,true);
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
			
			if(canOnlyRemoveNewAdded){
				if(entity.newAdded){
					btnSelect.setVisibility(View.GONE);
					btnRemove1.setVisibility(View.VISIBLE);
					btnRemove2.setVisibility(View.GONE);
					
					parent.setBackgroundResource(R.color.transparent);
					
				}else{
					//여기는 지울수 없다. 
					btnSelect.setVisibility(View.GONE);
					btnRemove1.setVisibility(View.GONE);
					btnRemove2.setVisibility(View.GONE);
					
					parent.setBackgroundResource(R.color.transparent);
				}
				
			}else{
				if(entity.selected){
					btnSelect.setVisibility(View.VISIBLE);
					btnRemove1.setVisibility(View.GONE);
					btnRemove2.setVisibility(View.GONE);
					
					parent.setBackgroundResource(R.color.popup_list_selected);
				}else{
					btnSelect.setVisibility(View.GONE);
					btnRemove1.setVisibility(View.VISIBLE);
					btnRemove2.setVisibility(View.GONE);
					
					parent.setBackgroundResource(R.color.transparent);
				}
			}
			
			
			
		}
	}

}
