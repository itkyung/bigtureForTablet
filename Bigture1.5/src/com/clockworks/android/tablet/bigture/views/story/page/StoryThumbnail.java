package com.clockworks.android.tablet.bigture.views.story.page;




import com.clockworks.android.tablet.bigture.ArtworkDetailActivity;
import com.clockworks.android.tablet.bigture.MyBigtureActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StoryThumbnail extends RelativeLayout{
	StoryArtworkEntity artworkEntity;
	
	ImageView imageView1;
	ImageView imageArtwork;
	ImageView imageProfile;
	TextView  nameLabel;
	TextView  pageLabel;

	
	BitmapFactory.Options bitmapOptions;

	public StoryThumbnail(Context context, AttributeSet attrs){
		super(context, attrs);

		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
	}

	@Override
	protected void onFinishInflate(){
		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		imageProfile = (ImageView)findViewById(R.id.imageProfile);
		nameLabel = (TextView)findViewById(R.id.nameLabel);
		pageLabel = (TextView)findViewById(R.id.pageLabel);
		imageView1 = (ImageView)findViewById(R.id.imageView1);
		
		imageProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Activity activity = (Activity)getContext();
				Intent intent = new Intent(getContext(), MyBigtureActivity.class);
				intent.putExtra("userId", artworkEntity.userId);
				intent.putExtra("viewIndex", 0);
				activity.startActivity(intent);
			}
		});
		
		imageArtwork.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
//				ArrayList<ArtworkE>
//				Intent intent = new Intent(getContext(),ArtworkDetailActivity.class);
//				intent.putExtra("fromMyBigture", false);
//				intent.putParcelableArrayListExtra("artworkList", artworkList);
//				intent.putExtra("currentIndex", index);
//				
//				activity.startActivity(intent);
			}
		});
		
		
		
		
		//imageProfile.setOnClickListener(new View.OnCli)
		
		super.onFinishInflate();
	}

	public void updateView(StoryArtworkEntity entity, int currentPage, int totalPageCount, boolean myBigture, boolean showOrgImg){
		this.artworkEntity = entity;
		
		
		String imageURL = null;
		
		if(showOrgImg){
			imageURL = entity.getArtworkImageURL();
		}else{
			imageURL = entity.getThumbnailURL();
		}
		
		BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, imageArtwork, null);


		imageURL = entity.getProfileImageURL();
		if (imageURL != null){
			BitmapDownloader.getInstance().downloadBitmap(imageURL, bitmapOptions, new BitmapDownloader.BitmapDownloaderListener(){
				@Override
				public void onComplete(Bitmap bitmap){
					if (bitmap != null && imageProfile != null ){
						try{
							bitmap = ImageUtil.createCircularBitmap(bitmap, 29);
							imageProfile.setImageBitmap(bitmap);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			});
		}
		
		nameLabel.setText("By " + entity.ownerName);
		pageLabel.setText("" + (currentPage+1) + "/" + totalPageCount);
		if(myBigture){
			nameLabel.setVisibility(View.GONE);
			imageView1.setVisibility(View.GONE);
			imageProfile.setVisibility(View.GONE);
		}
	}
	
	public void resetContent(){
		if(imageArtwork != null){
			Drawable d = imageArtwork.getDrawable();
			if (d != null && d instanceof BitmapDrawable){
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				if (bitmap != null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap = null;
				}
//				imageArtwork.setImageBitmap(null);
//				imageArtwork.setImageDrawable(null);
			}
		}
		if(imageProfile != null && artworkEntity.getProfileImageURL() != null){
			Drawable d = imageProfile.getDrawable();
			if (d != null && d instanceof BitmapDrawable){
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				if (bitmap != null && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap = null;
				}
//				imageProfile.setImageBitmap(null);
//				imageProfile.setImageDrawable(null);
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	@Override
	protected void finalize() throws Throwable {
		//resetContent();
		super.finalize();
	}
	
	
}
