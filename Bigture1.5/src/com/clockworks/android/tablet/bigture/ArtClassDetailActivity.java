package com.clockworks.android.tablet.bigture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.user.FindSimpleUserAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.common.ClassType;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtworksListFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ArtClassDetailActivity extends AbstractBigtureActivity implements TitleBarListener{
	private Context context;
	private ArtClassEntity classEntity;

	private boolean isOwner;
	
	private TitleBar titleBar;
	private Button collectBtn;
	private Button joinedBtn;
	private ImageView coverImg;
	private ImageView imageProfile;
	private TextView jobLabel;
	private TextView nameLabel;
	private TextView countryLabel;
	private TextView startDate;
	private TextView endDate;
	private Button btnEdit;
	private TextView txtDesc;
	private ArtworksListFragment listFragment;
	private PopupWindow editMenu;
	private PopupWindow joinedPopup;
	private ListView listArtists;
	private Button btnEditMember;
	private TextView popupTitle;
	BitmapFactory.Options bitmapOptions;
	
	private final SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd");
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_artclass_detail);
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		
		Intent intent = getIntent();
		this.classEntity = (ArtClassEntity)intent.getParcelableExtra("artClass");
		this.context = this;
	
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideHomeButton();
		titleBar.hideDrawButton();
		titleBar.setTitle(classEntity.className);
		String userId = AccountManager.getUserIdx();
		this.isOwner = userId.equals(classEntity.ownerId) ? true : false;
		
		this.collectBtn = (Button)findViewById(R.id.collectBtn);
		this.collectBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(classEntity.collected){
					ArtClassUnCollectTask task = new ArtClassUnCollectTask();
					task.execute(classEntity.index);
				}else{
					ArtClassCollectTask task = new ArtClassCollectTask();
					task.execute(classEntity.index);
					
				}
			}
		});
		
		this.joinedBtn = (Button)findViewById(R.id.joinedBtn);
		this.joinedBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showJoinedArtist(v);
			}
		});
		
		this.coverImg = (ImageView)findViewById(R.id.coverImg);
		this.imageProfile = (ImageView)findViewById(R.id.imageProfile);
		this.jobLabel = (TextView)findViewById(R.id.jobLabel);
		this.nameLabel = (TextView)findViewById(R.id.nameLabel);
		this.countryLabel = (TextView)findViewById(R.id.countryLabel);
		this.startDate = (TextView)findViewById(R.id.startDate);
		this.endDate = (TextView)findViewById(R.id.endDate);
		this.btnEdit = (Button)findViewById(R.id.btnEdit);
		this.btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,PopupEditArtClassActivity.class);
				intent.putExtra("classEntity", classEntity);
				startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_EDIT_CLASS);
			}
		});
		
		this.txtDesc = (TextView)findViewById(R.id.txtDesc);
		
		updateView();
		
		this.listFragment = ArtworksListFragment.newInstance(classEntity.index,isOwner,classEntity.joined,
				ClassType.valueOf(classEntity.getClassType()),
				classEntity.opened ? ClassShare.OPEN : ClassShare.EXCLUSIVE);
		
		getSupportFragmentManager().beginTransaction().add(R.id.listContainer,listFragment).commitAllowingStateLoss();
		
		
	}
	
	private void updateView(){
		this.collectBtn.setSelected(this.classEntity.collected);
		String coverImageUrl = this.classEntity.getCoverImageURL();
		if(coverImageUrl != null){
			BitmapDownloader.getInstance().displayImage(coverImageUrl, bitmapOptions, coverImg, null);
		}
		String profileUrl = this.classEntity.getProfileImageURL();
		if(profileUrl != null){
			BitmapDownloader.getInstance().displayImage(profileUrl, bitmapOptions, imageProfile, null);
		}
		this.jobLabel.setText(classEntity.ownerJob);
		this.nameLabel.setText(classEntity.ownerName);
		this.countryLabel.setText(classEntity.ownerCountry);
		if(classEntity.dStartDate != null){
			this.startDate.setText(fm.format(classEntity.dStartDate));
		}
		if(classEntity.dEndDate != null){
			this.endDate.setText(fm.format(classEntity.dEndDate));
		}
		this.txtDesc.setText(classEntity.description);
		
		if(isOwner){
			this.btnEdit.setVisibility(View.VISIBLE);
			this.coverImg.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(editMenu == null){
						LayoutInflater inflater = LayoutInflater.from(context);
						View menuView = inflater.inflate(R.layout.menu_select_picture_artclass, null);
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
							
							String tempPath = FileUtil.getTempPath(context) + "/taken.image";
							File file = new File(tempPath);
							
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_TAKE_PICTURE);
						}
					});
					
					Button menuSelectGallery = (Button)mView.findViewById(R.id.menuSelectGallery);
					menuSelectGallery.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							editMenu.dismiss();
							Intent intent = new Intent(Intent.ACTION_PICK);
							intent.setType("image/*");
							startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
						}
					});
					
					
					Button menuMyArtworks = (Button)mView.findViewById(R.id.menuMyArtworks);
					menuMyArtworks.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							editMenu.dismiss();
							Intent intent = new Intent(context,PopupArtworkListActivity.class);
							startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
						}
					});
					
					Button menuRecentArtworks = (Button)mView.findViewById(R.id.menuRecentArtworks);
					menuRecentArtworks.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							editMenu.dismiss();
							
						}
					});
					
					
					mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
					
					editMenu.setAnimationStyle(-1);
					editMenu.showAsDropDown(v, 30, -100);
					
				}
			});
			
		}else{
			this.btnEdit.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}
	
	private void uploadCoverImage(String filePath){
		WaitDialog.showWailtDialog(context, false);
		
		UploadCoverImgTask task = new UploadCoverImgTask();
		task.execute(filePath);
	}
	
	private void changeCoverImage(CommonCoverEntity coverEntity){
		//서버에 해당 coverEntity를 적용시킨다.
		this.classEntity.coverEntity = coverEntity;
		ChangeCoverImageTask task = new ChangeCoverImageTask();
		task.execute(coverEntity);
	}
	
	private void showCoverImage(String imagePath){
		this.classEntity.coverImage = imagePath;
		BitmapDownloader.getInstance().displayImage(classEntity.getCoverImageURL(), null, coverImg, null);
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri imageUri = null;
		Intent intent = null;
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK:
				ArtworkEntity entity = data.getParcelableExtra("selectedArtwork");
				CommonCoverEntity cover = new CommonCoverEntity();
				cover.coverFromArtworks = true;
				cover.coverImageId = entity.artworkId;
				cover.coverImagePath = entity.imagePath;
				cover.coverThumbPath = entity.thumbPath;
				changeCoverImage(cover);
				break;
			case BigtureEnvironment.REQ_CODE_TAKE_PICTURE:
				String tempPath = FileUtil.getTempPath(context) + "/taken.image";
				File file = new File(tempPath);
				imageUri = Uri.fromFile(file);

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				break;
			case BigtureEnvironment.REQ_CODE_CROP_PICTURE:
				final Bundle bundle = data.getExtras();
				
				String filePath = bundle.getString("imagePath");
				String newFilePath = FileUtil.getTempPath(context) + File.separator + System.currentTimeMillis() + ".image";
				try{
					FileUtil.moveFile(filePath, newFilePath);
					uploadCoverImage(newFilePath);
				}catch (Exception e){
					e.printStackTrace();
				}		
				
				break;
			case BigtureEnvironment.REQ_CODE_PICKUP_PICTURE:
				imageUri = data.getData();

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				
				break;
			case BigtureEnvironment.EVENT_CODE_EDIT_CLASS:
				boolean deleted = data.getBooleanExtra("deleted", false);
				this.classEntity = data.getParcelableExtra("classEntity");
				if(deleted){
					finish();
				}else{
					updateView();
				}
				
				break;
			case BigtureEnvironment.EVENT_CODE_ADD_CLASS_MEMBER:
				
				
				break;
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void showJoinedArtist(View v){
		if(joinedPopup == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			View menuView = inflater.inflate(R.layout.menu_joined_artist, null);
			joinedPopup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			joinedPopup.setOutsideTouchable(true);
			joinedPopup.setBackgroundDrawable(new BitmapDrawable()) ;
		}
		
		joinedPopup.dismiss();
		View mView = joinedPopup.getContentView();
		
		this.popupTitle = (TextView)mView.findViewById(R.id.titleLabel);
		this.listArtists = (ListView)mView.findViewById(R.id.listArtists);
		this.btnEditMember = (Button)mView.findViewById(R.id.btnEditMember);
		
		this.btnEditMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				FindSimpleUserAdapter adapter = (FindSimpleUserAdapter)listArtists.getAdapter();
				
				Intent intent = new Intent(context,PopupSelectMemberActivity.class);
				intent.putExtra("classId", classEntity.index);
				intent.putParcelableArrayListExtra("members", adapter.getUsers());
				
				startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_CLASS_MEMBER);
				joinedPopup.dismiss();
			}
		});
		
		if(!isOwner || !classEntity.shareType.equals(ClassShare.EXCLUSIVE.name())){
			this.btnEditMember.setVisibility(View.GONE);
		}else{
			this.btnEditMember.setVisibility(View.VISIBLE);
		}
		
		FindSimpleUserAdapter adapter = new FindSimpleUserAdapter(this);
		this.listArtists.setAdapter(adapter);
		
		mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		joinedPopup.setAnimationStyle(-1);
		joinedPopup.showAsDropDown(v, 0,-100);
		
		FindJoinedArtistTask task = new FindJoinedArtistTask();
		task.execute(classEntity.index);
	}
	

	
	class FindJoinedArtistTask extends AsyncTask<String, Void, ArrayList<SimpleUserEntity>>{

		@Override
		protected ArrayList<SimpleUserEntity> doInBackground(String... params) {
			return ArtClassHandler.getJoinedMembers(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<SimpleUserEntity> result) {
			super.onPostExecute(result);
			FindSimpleUserAdapter adapter = (FindSimpleUserAdapter)listArtists.getAdapter();
			adapter.setUsers(result);
			adapter.notifyDataSetChanged();
		}
		
		
	}
	
	
	
	class ArtClassCollectTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtClassHandler.addClassCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			collectBtn.setSelected(true);
			classEntity.collected = true;
		}
	}
	
	class ArtClassUnCollectTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtClassHandler.removeClassCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
		
			super.onPostExecute(result);
			collectBtn.setSelected(false);
			classEntity.collected = false;
		}
		
		
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
				changeCoverImage(result);
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.msg_upload_error);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}
		
	}
	
	class ChangeCoverImageTask extends AsyncTask<CommonCoverEntity, Void, String>{

		@Override
		protected String doInBackground(CommonCoverEntity... params) {
			CommonCoverEntity entity = params[0];
			return ArtClassHandler.changeCoverImage(classEntity.index, entity.coverImageId, entity.coverFromArtworks);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			showCoverImage(result);
		}
		
	}
	
	
	
}
