package com.clockworks.android.tablet.bigture.views.story.page;

import java.io.File;
import java.security.acl.Owner;

import com.clockworks.android.tablet.bigture.PopupArtworkListActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class EditCoverPage extends CoverPage {
	
	private EditText titleLabel;
	private TextView nameLabel;
	private ImageView ivCover;
	private ImageView coverIcon;
	private Button rightArrowBtn;
	private Button addPageBtn;
	private EditCoverPageListener listener;
	private TextView coverText;
	private PopupWindow editMenu;
	private Context context;
	
	public EditCoverPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.titleLabel = (EditText)findViewById(R.id.titleLabel);
		this.nameLabel = (TextView)findViewById(R.id.nameLabel);
		this.ivCover = (ImageView)findViewById(R.id.ivCover);
		this.coverIcon = (ImageView)findViewById(R.id.coverIcon);
		this.rightArrowBtn = (Button)findViewById(R.id.rightArrowBtn);
		this.coverText = (TextView)findViewById(R.id.coverText);
		rightArrowBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClickNextPage();
			}
		});
		
		this.addPageBtn = (Button)findViewById(R.id.addPageBtn);
		addPageBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClickAddPage();
			}
		});
		
		this.ivCover.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(editMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_select_picture_story, null);
					editMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					editMenu.setOutsideTouchable(true);
					editMenu.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				editMenu.dismiss();
				View mView = editMenu.getContentView();
				
				Button menuTakePhoto = (Button)mView.findViewById(R.id.menuTakePhoto);
				menuTakePhoto.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						listener.onTakePhoto();
					}
				});
				
				Button menuSelectGallery = (Button)mView.findViewById(R.id.menuSelectGallery);
				menuSelectGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						listener.onSelectFromGallery();
					}
				});
				
				
				Button menuMyArtworks = (Button)mView.findViewById(R.id.menuMyArtworks);
				menuMyArtworks.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						listener.onSelectFromArtworks();
					}
				});
				
				Button menuFromStory = (Button)mView.findViewById(R.id.menuFromStory);
				menuFromStory.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						listener.onSelectFromStory();
					}
				});
				
				Button menuRecentArtworks = (Button)mView.findViewById(R.id.menuRecentArtworks);
				menuRecentArtworks.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						listener.onSelectRecentArtworks();
					}
				});
				
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				editMenu.setAnimationStyle(-1);
				editMenu.showAsDropDown(v, 300, -300);
				
			}
		});
	}


	public void initView(StoryEntity entity,EditCoverPageListener listener){
		initView(entity);
		this.listener = listener;
	}

	@Override
	public void initView(StoryEntity entity) {
		super.initView(entity);
		AccountEntity account = AccountManager.getInstance().getAccountEntity();
		nameLabel.setText(account.nickName);
		
		if(entity != null){
			titleLabel.setText(entity.title);
			String imagePath = entity.getCoverImageURL();
			if(imagePath != null){
				coverIcon.setVisibility(View.GONE);
				coverText.setVisibility(View.GONE);
				
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
				bitmapOptions.inSampleSize = 3;
				
				BitmapDownloader.getInstance().displayImage(imagePath, bitmapOptions, ivCover, null);
			}
			updateArrow(entity);
			
		}else{
			rightArrowBtn.setVisibility(View.GONE);
		}
		
	}
	
	/*
	 * 입력한 값과 storyEntity를 동기화한다.
	 * 여기서는 단지 title값만 동기화하면됨. 이미지는 이미 동기화 되어잇음.
	 */
	public void syncData(){
		this.storyEntity.title = titleLabel.getText().toString();
	}
	
	
	
	public void updateArrow(StoryEntity storyEntity){
		super.updateArrow(storyEntity);
		if(storyEntity.pageCount > 0){
			rightArrowBtn.setVisibility(View.VISIBLE);
		}else{
			rightArrowBtn.setVisibility(View.GONE);
		}
	}
	
	public void showCoverImage(CommonCoverEntity coverEntity){
		if(coverEntity.localImagePath != null){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bitmap = ImageUtil.loadFromFile(coverEntity.localImagePath, options);
			ivCover.setImageBitmap(bitmap);
		}else{
			BitmapDownloader.getInstance().displayImage(coverEntity.getCoverThumbPath(), null, ivCover, null);
		}
		
		coverIcon.setVisibility(View.GONE);
		coverText.setVisibility(View.GONE);
		
		storyEntity.coverEntity = coverEntity;
	}
	
	public void uploadCoverImage(String filePath){
		WaitDialog.showWailtDialog(context, false);
		
		UploadCoverImgTask task = new UploadCoverImgTask();
		task.execute(filePath);
		
	}
		
	class UploadCoverImgTask extends AsyncTask<String, Void, CommonCoverEntity>{

		@Override
		protected CommonCoverEntity doInBackground(String... params) {
			String filePath = params[0];
			
			File image = new File(filePath);
			return ArtClassHandler.uploadCover(image, filePath);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(CommonCoverEntity result) {
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			if(result != null){
				showCoverImage(result);
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.msg_upload_error);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}		
	}

	
	
	public interface EditCoverPageListener{
		public void onClickAddPage();
		public void onClickNextPage();
		public void onTakePhoto();
		public void onSelectFromGallery();
		public void onSelectFromArtworks();
		public void onSelectRecentArtworks();
		public void onSelectFromStory();
	}
	
}
