package com.clockworks.android.tablet.bigture.views.artwork;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ArtworkDetailCell extends LinearLayout {
	Context context;
	Callback callback;
	
	ArtworkEntity artworkEntity;
	String myUserId;
	
	TextView titleLabel;
	TextView nameLabel;
	TextView countryLabel;
	TextView descLabel;
	TextView dateLabel;
	TextView refrenceLabel;
	TextView loveCountLabel;
	TextView awesomeCountLabel;
	TextView wowCountLabel;
	TextView fantasticCountLabel;
	TextView funCountLabel;
	
	ImageView artworkImageView;
	ImageView profileImageView;
	
	Button contestBadge;
	ImageView openImage;
	
	ImageView replyMapImageView;
	ImageView moreMenuImageView;
	ImageView contestImageView;
	ImageView collectImageView;
	
	
	
	
	PopupWindow popupMenu;
	
	BitmapFactory.Options artworkBitmapOption;
	BitmapFactory.Options profileBitmapOption;
	
	boolean fromMyBigture;
	
	public ArtworkDetailCell(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		
		artworkBitmapOption = new BitmapFactory.Options();
		artworkBitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		artworkBitmapOption.inSampleSize = 2;

		profileBitmapOption = new BitmapFactory.Options();
		profileBitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		profileBitmapOption.inSampleSize = 3;
		
		if (!this.isInEditMode())
			myUserId = AccountManager.getInstance().getUserIndex();
	}

	@Override
	protected void onFinishInflate(){
		
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		nameLabel  = (TextView)findViewById(R.id.nameLabel);
		countryLabel = (TextView)findViewById(R.id.countryLabel);
		descLabel    = (TextView)findViewById(R.id.descLabel);
		dateLabel    = (TextView)findViewById(R.id.dateLabel);
		refrenceLabel = (TextView)findViewById(R.id.referenceText);
		loveCountLabel = (TextView)findViewById(R.id.loveCountLabel);
		awesomeCountLabel = (TextView)findViewById(R.id.awesomeCountLabel);
		wowCountLabel =  (TextView)findViewById(R.id.wowCountLabel);
		fantasticCountLabel =  (TextView)findViewById(R.id.fantasticCountLabel);
		funCountLabel =  (TextView)findViewById(R.id.funCountLabel);
		
		
		artworkImageView = (ImageView)findViewById(R.id.imageArtwork);
		profileImageView = (ImageView)findViewById(R.id.imageProfile);
		collectImageView = (ImageView)findViewById(R.id.imageCollect);
		
		contestBadge = (Button)findViewById(R.id.imageContestBadge);
		openImage = (ImageView)findViewById(R.id.imageOpen);
		
		profileImageView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onShowUserPage(ArtworkDetailCell.this, artworkEntity);
			}
		});
		
		nameLabel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				callback.onShowUserPage(ArtworkDetailCell.this, artworkEntity);
			}
		});
		
		artworkImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v){
				callback.onShowArtwork(ArtworkDetailCell.this, artworkEntity);
			}
		});
		
		
		
		findViewById(R.id.imageContestIcon).setOnClickListener(new View.OnClickListener(){	
			@Override
			public void onClick(View v){
				if(myUserId == null){
					BigtureEnvironment.showAlertMessage(context, 16);
				}else if(myUserId.equals(artworkEntity.userId)){
					callback.onContestButtonClicked(ArtworkDetailCell.this, v, artworkEntity);
				}else{
					BigtureEnvironment.showAlertMessage(context, 17);
				}
			}
		});

		findViewById(R.id.imageReplyMap).setOnClickListener(new View.OnClickListener(){	
			@Override
			public void onClick(View v){
				callback.onReplyMapButtonClicked(ArtworkDetailCell.this, v, artworkEntity);
			}
		});

		findViewById(R.id.imageMore).setOnClickListener(new View.OnClickListener(){	
			@Override
			public void onClick(View v){
				if(myUserId == null){
					BigtureEnvironment.showAlertMessage(context, 16);
				}else{
					callback.onMoreMenuButtonClicked(ArtworkDetailCell.this, v, artworkEntity);
				}
			}
		});
		
		collectImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				WaitDialog.showWailtDialog(context, false);
				if(!collectImageView.isSelected()){
					AddCollectionTask task = new AddCollectionTask();
					task.execute(artworkEntity.artworkId);
				}else{
					RemoveCollectionTask task = new RemoveCollectionTask();
					task.execute(artworkEntity.artworkId);
				}
			}
		});
		
		moreMenuImageView = (ImageView)findViewById(R.id.imageMore);
		
		
			
		
		super.onFinishInflate();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event){
		if (popupMenu != null && popupMenu.isShowing())
			popupMenu.dismiss();
		
		return super.dispatchTouchEvent(event);
	}

	public void updateCell(ArtworkEntity entity, Callback callback, boolean fromMyBigture){
		this.artworkEntity = entity;
		this.callback = callback;
		this.fromMyBigture = fromMyBigture;
		
		artworkImageView.setImageBitmap(null);
		titleLabel.setText(null);
		nameLabel.setText(null);
		countryLabel.setText(null);
		descLabel.setText(null);
		
		showData(entity);
		
	}
	
	public void refreshData(ArtworkEntity entity){
		titleLabel.setText(entity.title);
		nameLabel.setText(entity.ownerName);
		if(AccountManager.getInstance().isKorean()){
			countryLabel.setText(entity.userCountryKr);
		}else{
			countryLabel.setText(entity.userCountry);
		}
		
		
		descLabel.setText(entity.comment);
		dateLabel.setText(DateUtil.getTimeString(entity.created));
		
		
	}
	
	protected void showData(ArtworkEntity entity){
		titleLabel.setText(entity.title);
		nameLabel.setText(entity.ownerName);
		if(AccountManager.getInstance().isKorean()){
			countryLabel.setText(entity.userCountryKr);
		}else{
			countryLabel.setText(entity.userCountry);
		}
		descLabel.setText(entity.comment);
		dateLabel.setText(DateUtil.getTimeString(entity.created));
		
		Drawable d = artworkImageView.getDrawable();
		if (d != null && d instanceof BitmapDrawable){
			Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			if (bitmap != null){
				bitmap.recycle();
				bitmap = null;
			}
		}
		artworkImageView.setImageBitmap(null);
		
		if(entity.spam){
			collectImageView.setClickable(false);
			//TODO 스팸신고 이미지를 보여줘야한다.
			
			
			
		}else{
			collectImageView.setClickable(true);
			if (entity.imagePath != null && entity.imagePath.length() > 0){
				String imageURL = entity.getArtworkImageURL();
				BitmapDownloader.getInstance().displayImage(imageURL, artworkBitmapOption, artworkImageView, null);
			}
		}
		if (entity.profileImagePath != null && entity.profileImagePath.length() > 0){
			profileImageView.setImageBitmap(null);
			String imageURL = entity.getProfileImageURL();
			BitmapDownloader.getInstance().downloadBitmap(imageURL, profileBitmapOption, new BitmapDownloader.BitmapDownloaderListener(){
				@Override
				public void onComplete(Bitmap bitmap){
					if (bitmap != null && profileImageView != null ){
						try{
							bitmap = ImageUtil.createCircularBitmap(bitmap, 29);
							profileImageView.setImageBitmap(bitmap);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			});			
		}else{
			profileImageView.setImageResource(R.drawable.artworkspage_image_profile_photo_default);
		}
		
		View imageContestIcon = findViewById(R.id.imageContestIcon);
		if(myUserId == null || !myUserId.equals(entity.userId)){
			imageContestIcon.setVisibility(View.GONE);
		}else{
			imageContestIcon.setVisibility(View.VISIBLE);
		}
		
		if(myUserId == null ||  myUserId.equals(entity.userId)){
			collectImageView.setVisibility(View.GONE);
		}else{
			collectImageView.setVisibility(View.VISIBLE);
			collectImageView.setSelected(entity.collected);
		}
		
		if(entity.opend){
			openImage.setVisibility(View.GONE);
		}else{
			openImage.setVisibility(View.VISIBLE);
		}
		
		if (entity.referenceType != null){
			if(entity.referenceType.equals("CONTEST")){
				refrenceLabel.setVisibility(View.VISIBLE);
				refrenceLabel.setText("From " + entity.referenceName);
			}else if(fromMyBigture && (entity.referenceType.equals("CLASS") || entity.referenceType.equals("STORY"))){
				//MyBigture에서 진입했을 경우에만 Contest이외의 출처를 명시한다.
				refrenceLabel.setVisibility(View.VISIBLE);
				refrenceLabel.setText("From " + entity.referenceName);
			}else{
				refrenceLabel.setVisibility(View.GONE);
			}
		}else{
			refrenceLabel.setVisibility(View.GONE);
		}
		
		if (entity.contestRank != 0){
			contestBadge.setVisibility(View.VISIBLE);
			contestBadge.setText(entity.referenceName);
		}else{
			contestBadge.setVisibility(View.GONE);
		}
		
		loveCountLabel.setText(entity.loveCount+"");
		awesomeCountLabel.setText(entity.awesomeCount + "");
		wowCountLabel.setText(entity.wowCount + "");
		fantasticCountLabel.setText(entity.fantasticCount + "");
		funCountLabel.setText(entity.funCount + "");
		
	}
	
	public interface Callback{
		void onShowUserPage(ArtworkDetailCell cell, ArtworkEntity artworkEntity);
		void onShowArtwork(ArtworkDetailCell cell, ArtworkEntity artworkEntity);
		void onContestButtonClicked(ArtworkDetailCell cell, View view, ArtworkEntity artworkEntity);
		void onReplyMapButtonClicked(ArtworkDetailCell cell, View view, ArtworkEntity artworkEntity);
		void onMoreMenuButtonClicked(ArtworkDetailCell cell, View view, ArtworkEntity artworkEntity);
	}
	
	class AddCollectionTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.addCollection(params[0]);
		}

		@Override
		protected void onCancelled() {
		
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
	
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			artworkEntity.collected = true;
			collectImageView.setSelected(true);
		}
	}

	class RemoveCollectionTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.removeCollection(params[0]);
		}

		@Override
		protected void onCancelled() {
		
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
	
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			artworkEntity.collected = false;
			collectImageView.setSelected(false);
		}
	}
}
