package com.clockworks.android.tablet.bigture.fragment.story;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NameCardFragment extends Fragment {
	private StoryEntity story;
	private boolean myBigture;
	private Context context;
	BitmapFactory.Options bitmapOptions;
	
	public static NameCardFragment newInstance(StoryEntity story,boolean myBigture){
		NameCardFragment fragment = new NameCardFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("story", story);
		args.putBoolean("myBigture", myBigture);
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		Bundle args = getArguments();
		this.story = args.getParcelable("story");
		this.myBigture = args.getBoolean("myBigture");
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_namecard, container,false);
		
		View illustratorFrame = view.findViewById(R.id.illustratorFrame);
		if(myBigture){
			illustratorFrame.setVisibility(View.VISIBLE);
			
			AccountEntity entity = AccountManager.getInstance().entity;
			
			TextView name = (TextView)view.findViewById(R.id.illustratorName);
			name.setText(entity.nickName);
			
			TextView country = (TextView)view.findViewById(R.id.illustratorCountry);
			if(AccountManager.getInstance().isKorean()){
				country.setText(entity.countryNameKr);
			}else{
				country.setText(entity.countryName);
			}
			
			TextView job = (TextView)view.findViewById(R.id.illustratorJob);
			if(entity.job != null){
				job.setText(entity.job);
			}else{
				job.setVisibility(View.GONE);
			}
			
			String imageUrl = entity.getProfileImageURL();
			if(imageUrl != null){
				ImageView profile = (ImageView)view.findViewById(R.id.illustratorProfile);
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOptions, profile, null);
			}
			
		}else{
			illustratorFrame.setVisibility(View.GONE);
		}
		
		TextView writerName = (TextView)view.findViewById(R.id.writerName);
		writerName.setText(story.ownerName);
		
		TextView writerJob = (TextView)view.findViewById(R.id.writerJob);
		writerJob.setText(story.ownerJob);
		
		TextView writerCountry = (TextView)view.findViewById(R.id.writerCountry);
		writerCountry.setText(story.ownerCountry);
		
		String ownerImageUrl = story.getProfileImageURL();
		
		if(ownerImageUrl != null){
			ImageView writerProfile = (ImageView)view.findViewById(R.id.writerProfile);
			BitmapDownloader.getInstance().displayImage(ownerImageUrl, bitmapOptions, writerProfile, null);
		}
		
		return view;
	}
	
	
}
