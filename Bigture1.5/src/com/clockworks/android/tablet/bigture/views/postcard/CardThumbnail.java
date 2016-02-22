package com.clockworks.android.tablet.bigture.views.postcard;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail.Callback;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CardThumbnail extends RelativeLayout {
	Context context;
	BitmapFactory.Options bitmapOptions;
	PostCardEntity entity;
	Callback callback;
	
	ImageView imageView;
	TextView cardOwnerText;
	TextView commentText;
	TextView dateText;
	ImageView newIcon;
	
	boolean receivedCell;
	
	
	public CardThumbnail(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_postcard_cell, this);
	}

	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		
		setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.onShowPostCard(CardThumbnail.this, entity);
			}
		});
		
		imageView = (ImageView)findViewById(R.id.imageArtwork);
//		imageView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				callback.onShowPostCard(CardThumbnail.this, entity);
//			}
//		});
		
		cardOwnerText = (TextView)findViewById(R.id.cardOwner);
		commentText = (TextView)findViewById(R.id.cardComment);
		dateText = (TextView)findViewById(R.id.cardDate);
		newIcon = (ImageView)findViewById(R.id.iconNew);
		
	}

	public void updateCell(Callback callback, PostCardEntity entity, boolean receivedCell){
		this.callback = callback;
		this.entity  = entity;
		this.receivedCell = receivedCell;
		
		String imageURL = entity.getThumbnailURL();
		if (imageURL != null)
			BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, imageView, null);
		
		commentText.setText(entity.comment);
		dateText.setText(DateUtil.getDateFormatString(entity.created, "MM.dd.yyyy"));
		if(receivedCell){
			cardOwnerText.setText(entity.ownerName);
			
		}else{
			if(entity.receiverCount > 1){
				cardOwnerText.setText(entity.firstReceiverName + "외 " + (entity.receiverCount-1));
			}else{
				cardOwnerText.setText(entity.firstReceiverName);
			}
		}
		
		if(entity.viewed){
			newIcon.setVisibility(View.GONE);
		}else{
			newIcon.setVisibility(View.VISIBLE);
		}
	}
	
	public interface Callback{
		void onShowPostCard(CardThumbnail thumb, PostCardEntity cardEntity);
		void onSelectArtwork();
		void onDrawSketchbook();
		
	}
}
