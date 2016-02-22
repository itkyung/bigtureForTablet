package com.clockworks.android.tablet.bigture.views.artclass;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClassThumbnail extends RelativeLayout {
	Context context;
	Callback callback;

	public ImageView ivCover;
	public ImageView ivNowOn;
	public ImageView ivLock;
	public TextView tvTitle;
	public TextView tvName;
	public TextView tvJob;
	public ImageButton ibFavorite;
	


	public ArtClassEntity entity;
	BitmapFactory.Options bitmapOptions;

	public ClassThumbnail(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_artclass_cell, this);
	}
	

	@Override
	protected void onFinishInflate(){
		tvTitle = (TextView)findViewById(R.id.titleLabel);
		tvName  = (TextView)findViewById(R.id.expertName);
		tvJob   = (TextView)findViewById(R.id.expertJob);
		ivCover = (ImageView)findViewById(R.id.imageArtwork);
		ivCover.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onShowArtClass(ClassThumbnail.this, entity);
			}
		});
		
		ibFavorite = (ImageButton)findViewById(R.id.btnFavorite);
		ibFavorite.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if (!AccountManager.isLogin()){
					BigtureEnvironment.showAlertMessage(getContext(), 16);
				}else{
					callback.onToggleFavorite(ClassThumbnail.this, entity);
				}
			}
		});
		
		ivNowOn = (ImageView)findViewById(R.id.iconNowOn);
		ivLock = (ImageView)findViewById(R.id.iconLock);

		super.onFinishInflate();
	}
	
	public void updateCell(Callback callback, ArtClassEntity entity){
		this.callback = callback;
		this.entity  = entity;
		
		tvTitle.setText(entity.className);
		tvName.setText(entity.ownerName);
		tvJob.setText(entity.ownerJob);
		
		if (AccountManager.isMyUserIndex(entity.ownerId)){
			ibFavorite.setVisibility(View.GONE);
		}else{
			ibFavorite.setVisibility(View.VISIBLE);
			ibFavorite.setSelected(entity.collected);
		}
		
		if(entity.status.equals("DOING")){
			ivNowOn.setVisibility(View.VISIBLE);
		}else{
			ivNowOn.setVisibility(View.GONE);
		}
		
		String imageURL = entity.getCoverImageURL();
		
		
		if(entity.opened){
			ivLock.setVisibility(View.GONE);
			if (imageURL != null)
				BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, ivCover, null);
		}else{
			if(entity.joined){
				ivLock.setVisibility(View.VISIBLE);
				if (imageURL != null)
					BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, ivCover, null);
			}else{
				ivLock.setVisibility(View.GONE);
				ivCover.setImageResource(R.drawable.artclass_list_canvas_thumbnail_lock);
			}
		}
		
		
		
	}
	
	public void updateCollection(boolean collected){
		ibFavorite.setSelected(collected);
	}
	
	public interface Callback{
		void onShowArtClass(ClassThumbnail thumb, ArtClassEntity entity);
		void onToggleFavorite(ClassThumbnail thumb, ArtClassEntity entity);
		void onAddArtClass();
		
	}
}
