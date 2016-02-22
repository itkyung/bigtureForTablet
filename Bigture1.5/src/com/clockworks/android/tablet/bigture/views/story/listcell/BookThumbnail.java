package com.clockworks.android.tablet.bigture.views.story.listcell;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BookThumbnail extends RelativeLayout{
	Context context;
	Callback callback;

	public ImageView ivCover;
	public ImageView ivHot;
	public TextView tvTitle;
	public TextView tvName;
	public ImageButton ibFavorite;
	public ImageButton ivDelete;
	public View draftView;

	public StoryEntity entity;
	BitmapFactory.Options bitmapOptions;

	public BookThumbnail(Context context, AttributeSet attrs){
		super(context, attrs);

		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_story_cell, this);
	}

	@Override
	protected void onFinishInflate(){
		tvTitle = (TextView)findViewById(R.id.titleLabel);
		tvName  = (TextView)findViewById(R.id.nameLabel);
		ivCover = (ImageView)findViewById(R.id.imageArtwork);
		ivCover.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				callback.onShowStoryBook(BookThumbnail.this, entity);
			}
		});
		
		ibFavorite = (ImageButton)findViewById(R.id.btnFavorite);
		ibFavorite.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				if (!AccountManager.isLogin())
				{
					BigtureEnvironment.showAlertMessage(getContext(), 16);
				}
				else
				{
					callback.onToggleFavorite(BookThumbnail.this, entity);
				}
				
			}
		});
		
		ivDelete = (ImageButton)findViewById(R.id.imageDelete);
		ivDelete.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onDeleteStoryBook(BookThumbnail.this, entity);
			}
		});

		draftView = findViewById(R.id.draftIcon);
		
		super.onFinishInflate();
	}
	
	public void updateCell(Callback callback, StoryEntity entity){
		this.callback = callback;
		this.entity  = entity;
		
		tvTitle.setText(entity.title);
		tvName.setText(entity.ownerName);
		
		if(entity.status.equals("DRAFT")){
			draftView.setVisibility(View.VISIBLE);

			if (AccountManager.isMyUserIndex(entity.ownerId))
				ivDelete.setVisibility(View.VISIBLE);
		}else{
			//findViewById(R.id.rootView).setBackgroundResource(R.drawable.bigturestory_book);
			ivDelete.setVisibility(View.GONE);
		}
		
		// Is my story?
		if (AccountManager.isMyUserIndex(entity.ownerId)){
			ibFavorite.setVisibility(View.GONE);
		}else{
			ibFavorite.setVisibility(View.VISIBLE);
			ibFavorite.setSelected(entity.collected);
		}
		
		
		
		String imageURL = entity.getCoverImageURL();
		if (imageURL != null)
			BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, ivCover, null);
		else{
			ivCover.setImageDrawable(getResources().getDrawable(R.drawable.bigturestory_list_book_thumbnail_blank));
		}
	}
	
	public void updateCollection(boolean collected){
		ibFavorite.setSelected(collected);
	}
	
	public interface Callback{
		void onShowStoryBook(BookThumbnail thumb, StoryEntity storyEntity);
		void onToggleFavorite(BookThumbnail thumb, StoryEntity storyEntity);
		void onAddStoryBook();
		void onEditStoryBook(BookThumbnail thumb, StoryEntity storyEntity);
		void onDeleteStoryBook(BookThumbnail thumb, StoryEntity storyEntity);
	}
	
	
}
