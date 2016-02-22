package com.clockworks.android.tablet.bigture.views.common;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CommentCell extends RelativeLayout {
	Context context;
	CommentEntity comment;	
	BitmapFactory.Options profileBitmapOption;
	
	ImageView profileImageView;
	ImageView friendMark;
	TextView jobLabel;
	TextView nameLabel;
	TextView dateLabel1;
	TextView dateLabel2;
	TextView commentLabel;
	TextView countryLabel;
	ImageView stickerIcon;
	
	View commentContainer;
	View stickerContainer;
	
	public CommentCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		profileBitmapOption = new BitmapFactory.Options();
		profileBitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		profileBitmapOption.inSampleSize = 3;
	}

	@Override
	protected void onFinishInflate() {
		profileImageView = (ImageView)findViewById(R.id.imageProfile);
		jobLabel = (TextView)findViewById(R.id.jobLabel);
		nameLabel = (TextView)findViewById(R.id.nameLabel);
		dateLabel1 = (TextView)findViewById(R.id.dateLabel1);
		dateLabel2 = (TextView)findViewById(R.id.dateLabel2);
		commentLabel = (TextView)findViewById(R.id.commentLabel);
		friendMark  = (ImageView)findViewById(R.id.friendMark);
		stickerIcon = (ImageView)findViewById(R.id.stickerIcon);
		countryLabel = (TextView)findViewById(R.id.countryLabel);
		commentContainer = findViewById(R.id.commentContainer);
		stickerContainer = findViewById(R.id.stickerContainer);
		
		
		super.onFinishInflate();
	}

	public void updateCell(CommentEntity entity){
		this.comment = entity;
		
		String imageURL = entity.getProfileImageURL();
		BitmapDownloader.getInstance().displayImage(imageURL, profileBitmapOption, profileImageView, null);			
		
		if (entity.ownerJob == null || entity.ownerJob.length() == 0){
			jobLabel.setVisibility(View.GONE);
		}else{
			jobLabel.setVisibility(View.VISIBLE);
			jobLabel.setText(entity.ownerJob);
		}
		
		nameLabel.setText(entity.ownerName);
		if(AccountManager.getInstance().isKorean()){
			countryLabel.setText(entity.ownerCountryKr);
		}else{
			countryLabel.setText(entity.ownerCountry);
		}
		
		
		if(entity.sticker == null || entity.sticker.equals("NONE")){
			commentContainer.setVisibility(View.VISIBLE);
			stickerContainer.setVisibility(View.GONE);
			
			commentLabel.setText(entity.comment);
			dateLabel1.setText(DateUtil.getTimeString(entity.created));
		}else{
			commentContainer.setVisibility(View.GONE);
			stickerContainer.setVisibility(View.VISIBLE);
			if(entity.sticker.equals("TYPE_LOVE")){
				stickerIcon.setImageResource(R.drawable.artworkspage_talk_list_sticker_icon_love);
			}else if(entity.sticker.equals("TYPE_AWESOME")){
				stickerIcon.setImageResource(R.drawable.artworkspage_talk_list_sticker_icon_awesome);
			}else if(entity.sticker.equals("TYPE_WOW")){
				stickerIcon.setImageResource(R.drawable.artworkspage_talk_list_sticker_icon_wow);
			}else if(entity.sticker.equals("TYPE_FUN")){
				stickerIcon.setImageResource(R.drawable.artworkspage_talk_list_sticker_icon_fun);
			}else if(entity.sticker.equals("TYPE_FANTASTIC")){
				stickerIcon.setImageResource(R.drawable.artworkspage_talk_list_sticker_icon_fantastic);
			}
			dateLabel2.setText(DateUtil.getTimeString(entity.created));
			
		}
		
		if(entity.friendComment){
			friendMark.setVisibility(View.VISIBLE);
		}else{
			friendMark.setVisibility(View.GONE);
		}
		
		
	}
	
	
}
