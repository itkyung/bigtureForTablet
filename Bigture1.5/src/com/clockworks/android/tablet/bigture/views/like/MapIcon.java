package com.clockworks.android.tablet.bigture.views.like;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MapIcon extends RelativeLayout
{
	TextView countLabel;
	//ImageView profileImage;
	public int regionId;
	
	Callback callback;
	
	public MapIcon(Context context)
	{
		super(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_icon_friend_map, this);
		
		countLabel = (TextView)findViewById(R.id.countLabel);
		//profileImage = (ImageView)findViewById(R.id.imageProfile);
	}

	public MapIcon(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_icon_friend_map, this);

		countLabel = (TextView)findViewById(R.id.countLabel);
		//profileImage = (ImageView)findViewById(R.id.imageProfile);
	}

	public void setCallback(Callback callback)
	{
		this.callback = callback;
		
		findViewById(R.id.btnAction).setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				if (MapIcon.this.callback != null)
				{
					MapIcon.this.callback.onIconClicked(MapIcon.this);
				}
			}
		});
	}
	public void setCount(int count)
	{
		countLabel.setText("" + count);
		countLabel.setVisibility(View.VISIBLE);
	//	profileImage.setVisibility(View.GONE);
	}
	
//	public void setProfileImage(String imageURL)
//	{
//		countLabel.setVisibility(View.GONE);
//		profileImage.setVisibility(View.VISIBLE);
//		
//		if (imageURL != null){
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inPreferredConfig = Bitmap.Config.RGB_565;
//			options.inSampleSize = 4;
//			
//			BitmapDownloader.getInstance().downloadBitmap(imageURL, options, new BitmapDownloader.BitmapDownloaderListener()
//			{	
//				@Override
//				public void onComplete(Bitmap bitmap)
//				{
//					Bitmap profile = ImageUtil.createCircularBitmap(bitmap, 17);
//					profileImage.setImageBitmap(profile);
//				}
//			});
//		}else{
//			//profileImage.setImageResource(R.drawable.map_bestfriends_icon_imagesize);
//		}
//	}
	
	public interface Callback
	{
		void onIconClicked(MapIcon icon);
	}
}
