package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.MyBigtureActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LikeFriendAdapter extends BaseAdapter {

	private Context context;
	private LikeFriendListListener listener;
	private ArrayList<FriendEntity> friends;

	BitmapFactory.Options profileOptions;
	
	public LikeFriendAdapter(Context context, LikeFriendListListener listener) {
		super();
		this.context = context;
		this.listener = listener;
	
		this.friends = new ArrayList<FriendEntity>();
	
		profileOptions = new BitmapFactory.Options();
		profileOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		profileOptions.inSampleSize = 3;
	}
	
	@Override
	public int getCount() {
		return this.friends.size();
	}

	@Override
	public Object getItem(int position) {
		
		return this.friends.get(position);		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final FriendEntity friend = this.friends.get(position);
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_friend_list, null);
		}
		
		ImageView thumbView = (ImageView)convertView.findViewById(R.id.imageProfile);
		String imageURL = friend.photoPath;
		
		if (imageURL != null)
			BitmapDownloader.getInstance().displayImage(ServerStaticVariable.ProfileURL + imageURL, profileOptions, thumbView, null);
		else{
			BitmapDownloader.getInstance().cancelDisplayImage(thumbView);
			thumbView.setImageResource(R.drawable.common_pofile_48x48);
		}
		
		TextView jobTitleView = (TextView)convertView.findViewById(R.id.jobTitleLabel);
		if(friend.pro){
			jobTitleView.setVisibility(View.VISIBLE);
			jobTitleView.setText(friend.job);
		}else{
			jobTitleView.setVisibility(View.GONE);
		}
		
		TextView nameLabel = (TextView)convertView.findViewById(R.id.nameLabel);
		nameLabel.setText(friend.nickName);
		
		TextView countryLabel = (TextView)convertView.findViewById(R.id.countryLabel);
		if(AccountManager.getInstance().isKorean()){
			countryLabel.setText(friend.countryKr);
		}else{
			countryLabel.setText(friend.country);
		}
		
		//TODO 스와이프 이벤트를 잡아야한다.
		Button exceptBtn = (Button)convertView.findViewById(R.id.btnRemove);
		exceptBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.doUnlike(friend);
			}
		});
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Activity activity = (Activity)context;
				Intent intent = new Intent(context, MyBigtureActivity.class);
				intent.putExtra("userId", friend.index);
				intent.putExtra("viewIndex", 0);
				activity.startActivity(intent);
			}
		});
		
		return convertView;
		
		
	}



	public ArrayList<FriendEntity> getFriends() {
		return friends;
	}



	public void setFriends(ArrayList<FriendEntity> friends) {
		this.friends = friends;
	}




	public interface LikeFriendListListener{
		public void doUnlike(FriendEntity friend);
	
	}
}
