package com.clockworks.android.tablet.bigture.views.expert;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpertThumbnail extends RelativeLayout {
	private Context context;
	private BitmapFactory.Options bitmapOptions;
	private TextView jobNameLabel;
	private TextView nameLabel;
	private ImageView profileImageView;
	private SimpleUserEntity expert;
	private Callback callback;
	
	public ExpertThumbnail(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_expert_thumb, this);
		
		jobNameLabel = (TextView)findViewById(R.id.jobLabel);
		nameLabel = (TextView)findViewById(R.id.nameLabel);
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		profileImageView = (ImageView)findViewById(R.id.imageProfile);
		profileImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onShowDetailExpert(expert);
				
			}
		});
		
	}

	public void update(ExpertThumbnail.Callback callback, SimpleUserEntity expert){
		this.expert = expert;
		this.callback = callback;
		
		jobNameLabel.setText(expert.jobName);
		nameLabel.setText(expert.name);
		
		String imagePath = expert.getProfileImageURL();
		if(imagePath == null){
			profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.expert_profile_photo_bg));
			
		}else{
			BitmapDownloader imageDownloader = BitmapDownloader.getInstance();
			imageDownloader.displayImage(imagePath, bitmapOptions, profileImageView	, null);
			
		}
		
		
		
	}
	
	public interface Callback{
		public void onShowDetailExpert(SimpleUserEntity expert);
	}
	
}
