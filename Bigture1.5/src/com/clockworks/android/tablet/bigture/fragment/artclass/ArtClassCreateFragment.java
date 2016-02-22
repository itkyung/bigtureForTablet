package com.clockworks.android.tablet.bigture.fragment.artclass;

import java.io.File;
import java.util.Date;


import com.clockworks.android.tablet.bigture.CropActivity;
import com.clockworks.android.tablet.bigture.PopupArtworkListActivity;
import com.clockworks.android.tablet.bigture.PopupCreateClassActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.common.ClassType;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ArtClassCreateFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
	private Context context;
	private ChangeShareOptionListener listener;
	private ImageView coverImg;
	private EditText editClassName;
	private EditText editClassDesc;
	private View classTypeNormal;
	private View classTypePuzzle;
	private View classPublic;
	private View classPrivate;
	int activeDateField;
	PopupWindow editMenu;
	private ArtClassEntity classEntity;
	private CommonCoverEntity currentCoverEntity;
	
	Date startDate = new Date();
	Date endDate = null;//new Date();
	
	TextView startDateLabel;
	TextView endDateLabel;
	
	private ClassShare currentShare;
	private ClassType currentType;
	
	
	@Override
	public void onAttach(Activity activity) {
	
		super.onAttach(activity);
		this.context = activity;
		this.listener = (ChangeShareOptionListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.currentShare = ClassShare.OPEN;
		this.currentType = ClassType.NORMAL;
		
	}
	


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_artclass, container,false);
		
		this.coverImg = (ImageView)view.findViewById(R.id.coverImg);
		this.editClassName = (EditText)view.findViewById(R.id.editClassName);
		this.editClassDesc = (EditText)view.findViewById(R.id.editClassDesc);
		this.classTypeNormal = view.findViewById(R.id.classTypeNormal);
		this.classTypePuzzle = view.findViewById(R.id.classTypePuzzle);
		this.classPublic = view.findViewById(R.id.classPublic);
		this.classPrivate = view.findViewById(R.id.classPrivate);
		
		this.classTypeNormal.setSelected(true);
		this.classPublic.setSelected(true);
		
		this.classTypeNormal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				classTypeNormal.setSelected(true);
				classTypePuzzle.setSelected(false);
				currentType = ClassType.NORMAL;
			}
		});
		
		this.classTypePuzzle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				classTypeNormal.setSelected(false);
				classTypePuzzle.setSelected(true);
				currentType = ClassType.PUZZLE;
			}
		});
		
		this.classPublic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				classPublic.setSelected(true);
				classPrivate.setSelected(false);
				currentShare = ClassShare.OPEN;
				listener.onChangeShareOption(currentShare);
			}
		});
		
		this.classPrivate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				classPublic.setSelected(false);
				classPrivate.setSelected(true);
				currentShare = ClassShare.EXCLUSIVE;
				listener.onChangeShareOption(currentShare);
			}
		});
		
		startDateLabel = (TextView)view.findViewById(R.id.startDateLabel);
		startDateLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activeDateField = 1;
				int year  = DateUtil.getYear(startDate);
				int month = DateUtil.getMonth(startDate)-1;
				int day   = DateUtil.getDay(startDate);
				
				DatePickerDialog dlg = new DatePickerDialog(context, ArtClassCreateFragment.this, year, month, day);
				dlg.show();
			}
		});
		startDateLabel.setText(DateUtil.getDateFormatString(startDate, "yyyy.MM.dd"));
		
		endDateLabel = (TextView)view.findViewById(R.id.endDateLabel);
		endDateLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activeDateField = 2;
				int year  = DateUtil.getCurrentYear();
				int month = DateUtil.getCurrentMonth() - 1;
				int day   = DateUtil.getCurrentDay();
				
				if (endDate != null){
					year  = DateUtil.getYear(endDate);
					month = DateUtil.getMonth(endDate)-1;
					day   = DateUtil.getDay(endDate);
				}

				DatePickerDialog dlg = new DatePickerDialog(context, ArtClassCreateFragment.this, year, month, day);
				dlg.show();
				
			}
		});
		
		View coverImgWrapper = view.findViewById(R.id.coverImgWrapper);
		coverImgWrapper.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//이미지를 선택하는 팝업 메뉴를 띄운다. 
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
						Intent intent = new Intent(context,PopupArtworkListActivity.class);
						startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
					}
				});
				
				Button menuRecentArtworks = (Button)mView.findViewById(R.id.menuRecentArtworks);
				menuRecentArtworks.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						
					}
				});
				
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				editMenu.setAnimationStyle(-1);
				editMenu.showAsDropDown(v, 0, -70);
			}
		});
		
		PopupCreateClassActivity activity = (PopupCreateClassActivity)getActivity();
		
		updateView(activity.getClassEntity());
		
		return view;
	}
	
	public void updateView(ArtClassEntity entity){
		this.classEntity = entity;
		
		if(entity != null){
			this.editClassName.setText(entity.className);
			this.editClassDesc.setText(entity.description);
			if(entity.classType.equals(ClassType.NORMAL.name())){
				classTypeNormal.setSelected(true);
				classTypePuzzle.setSelected(false);
			}else{
				classTypeNormal.setSelected(false);
				classTypePuzzle.setSelected(true);
			}
			this.currentType = ClassType.valueOf(entity.classType);
			
			if(entity.shareType.equals(ClassShare.OPEN.name())){
				classPublic.setSelected(true);
				classPrivate.setSelected(false);
				
			}else{
				classPublic.setSelected(false);
				classPrivate.setSelected(true);
			}
			this.currentShare = ClassShare.valueOf(entity.shareType);
			
			this.currentCoverEntity = entity.coverEntity;
			if(entity.coverEntity != null){
				showCoverImage(entity.coverEntity);
			}
			
			this.startDate = DateUtil.createDateFromString(entity.startDate,"yyyy-MM-dd");
			startDateLabel.setText(DateUtil.getDateFormatString(startDate, "yyyy.MM.dd"));
			
			this.endDate = DateUtil.createDateFromString(entity.endDate, "yyyy-MM-dd");
			endDateLabel.setText(DateUtil.getDateFormatString(endDate, "yyyy.MM.dd"));
			
			
		}
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
				showCoverImage(cover);
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
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void uploadCoverImage(String filePath){
		WaitDialog.showWailtDialog(context, false);
		
		UploadCoverImgTask task = new UploadCoverImgTask();
		task.execute(filePath);
		
	}
	
	private void showCoverImage(CommonCoverEntity coverEntity){
		this.currentCoverEntity = coverEntity;
		if(coverEntity.localImagePath != null){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bitmap = ImageUtil.loadFromFile(coverEntity.localImagePath, options);
			coverImg.setImageBitmap(bitmap);
		}else{
			BitmapDownloader.getInstance().displayImage(coverEntity.getCoverThumbPath(), null, coverImg, null);
		}
	}
	
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		if (activeDateField == 1){
			startDate = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(startDate, "yyyy.MM.dd");
			
			startDateLabel.setText(dateString);
		}
		else if (activeDateField == 2){
			endDate = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(endDate, "yyyy.MM.dd");
			
			endDateLabel.setText(dateString);
		}
	}
	
/**
 * 입력된 항목을 기초로해서 ArtClassEntity를 생성한다.
 * 입력항목을 검증해서 필수값을 체크한다.
 * 문제없으면 entity를 만들어서 리턴한다. 
 * @return
 */
	public ArtClassEntity makeClassEntity(){
		ArtClassEntity entity = this.classEntity == null ? new ArtClassEntity() : this.classEntity;
		entity.className = editClassName.getText().toString();
		
		if(entity.className == null || entity.className.length() == 0){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Notice	");
			builder.setMessage(R.string.story_ph_story_title);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			builder.create().show();
			
			return null;
		}
		
		entity.description = editClassDesc.getText().toString();
		entity.description = entity.description.replaceAll("\n", "\\\\n");
		
		if(endDate == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Notice	");
			builder.setMessage(R.string.message_enter_enddate);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			builder.create().show();
			
			return null;
		}
		String date1 = DateUtil.getDateFormatString(startDate, "yyyy-MM-dd");
		String date2 = DateUtil.getDateFormatString(endDate, "yyyy-MM-dd");
		entity.startDate = date1;
		entity.endDate = date2;
		
		entity.classType = currentType.name();
		entity.shareType = currentShare.name();
		entity.coverEntity = this.currentCoverEntity;
		
		return entity;
	}


	public interface ChangeShareOptionListener{
		public void onChangeShareOption(ClassShare option);
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
	
}
