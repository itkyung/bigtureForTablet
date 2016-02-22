package com.clockworks.android.tablet.bigture.adapter.user;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;


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

public class FindSimpleUserAdapter extends BaseAdapter {
	private BitmapFactory.Options options;
	ArrayList<SimpleUserEntity> users;
	Context context;
	
	public FindSimpleUserAdapter(Context context){
		this.context = context;
		this.users = new ArrayList<SimpleUserEntity>();
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
	}
	
	public ArrayList<SimpleUserEntity> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<SimpleUserEntity> users) {
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
			convertView = inflater.inflate(R.layout.cell_find_expert, null);
			ViewHolder viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		SimpleUserEntity entity = users.get(position);

		ViewHolder viewHolder = (ViewHolder)convertView.getTag();
		viewHolder.updateView(entity);
		
		return convertView;
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
		
		}
		
		public void updateView(SimpleUserEntity entity){
			this.entity = entity;
			nameLabel.setText(entity.name);
			countryLabel.setText(entity.country);
			
			if(entity.jobName != null){
				jobLabel.setVisibility(View.VISIBLE);
				jobLabel.setText(entity.jobName);
			}else{
				jobLabel.setVisibility(View.GONE);
			}
			
			String imageURL = entity.getProfileImageURL();
			if (imageURL != null)
				BitmapDownloader.getInstance().displayImage(imageURL, options, imageProfile, null);
			else
				imageProfile.setImageResource(R.drawable.common_pofile_60x60);
			
		}
	}
}

