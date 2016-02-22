package com.clockworks.android.tablet.bigture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsImageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.TempImgEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NewsEditActivity extends Activity implements View.OnClickListener {
	Context context;
	TextView noImageLabel;
	
	NewsEntity newsEntity;
	List<NewsImageEntity> originImageList;
	List<NewsImageEntity> addedImageList;
	List<NewsImageEntity> deletedImageList;
	PopupWindow editMenu;
	private ProgressDialog progressDlg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_expert_news);
		
		context = this;
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			newsEntity = bundle.getParcelable("newsEntity");

		this.addedImageList = new ArrayList<NewsImageEntity>();
		this.deletedImageList = new ArrayList<NewsImageEntity>();

		Button btnOk = (Button)findViewById(R.id.btnOK);
		btnOk.setOnClickListener(this);
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
		Button btnPicture = (Button)findViewById(R.id.btnPicture);
		btnPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(editMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_select_picture, null);
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
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				editMenu.setAnimationStyle(-1);
				editMenu.showAsDropDown(v, -50, 0);
			}
		});
		

		noImageLabel = (TextView)findViewById(R.id.noimageLabel);
		
		if (newsEntity != null)
			showData(newsEntity);
	}

	@Override
	public void onClick(View v){
		if (v.getId() == R.id.btnCancel){
			setResult(RESULT_CANCELED);
			this.finish();
		}
		else if (v.getId() == R.id.btnOK){
			EditText editTitle = (EditText)findViewById(R.id.editTitle);
			EditText editContent = (EditText)findViewById(R.id.editContent);

			String title = editTitle.getText().toString();
			if (title == null || title.length() == 0)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Notice");
				builder.setMessage(R.string.story_ph_story_title);
				builder.setPositiveButton("OK", null);
				builder.create().show();
				
				return;
			}
			
			String content = editContent.getText().toString();
			if (content == null || content.length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Notice");
				builder.setMessage(R.string.message_enter_content);
				builder.setPositiveButton("OK", null);
				builder.create().show();
				
				return;
			}
			
			progressDlg = ProgressDialog.show(context, "", "Save news .....", true);
			progressDlg.setCancelable(false);
			
			SaveNewsTask task = new SaveNewsTask();
			task.execute(newsEntity == null ? null : newsEntity.index, title, content);
			
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == BigtureEnvironment.REQ_CODE_TAKE_PICTURE){
			if (resultCode == RESULT_OK){
				String tempPath = FileUtil.getTempPath(this) + "/taken.image";
				File file = new File(tempPath);
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(NewsEditActivity.this, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
			}
		}else if (requestCode == BigtureEnvironment.REQ_CODE_CROP_PICTURE){
			if (resultCode == RESULT_OK){
				final Bundle bundle = data.getExtras();
				
				String filePath = bundle.getString("imagePath");
				String newFilePath = FileUtil.getTempPath(NewsEditActivity.this) + File.separator + System.currentTimeMillis() + ".image";
				try{
					FileUtil.moveFile(filePath, newFilePath);
					uploadNewsImage(newFilePath);
				}catch (Exception e){
					e.printStackTrace();
				}		
			}
		}else if (requestCode == BigtureEnvironment.REQ_CODE_PICKUP_PICTURE){
			if (resultCode == RESULT_OK){
				Uri imageUri = data.getData();

				Intent intent = new Intent(NewsEditActivity.this, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void showData(NewsEntity entity){
		EditText editTitle = (EditText)findViewById(R.id.editTitle);
		EditText editContent = (EditText)findViewById(R.id.editContent);

		editTitle.setText(entity.title);
		editContent.setText(entity.contents);
		
		if(entity.imageCount > 0){
			final HorizontalScrollView  hView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
			hView.setVisibility(View.VISIBLE);
			
			ImageLoadTask task = new ImageLoadTask();
			task.execute(entity.index);
			
		}
		
		
	}
	
	/**
	 * 서버에 해당 이미지를 업로드한다.
	 * @param filePath
	 */
	public void uploadNewsImage(String filePath){
		progressDlg = ProgressDialog.show(context, "", "Image Uploading .....", true);
		progressDlg.setCancelable(false);
		
		UploadImageTask task = new UploadImageTask();
		task.execute(filePath);
	}
	
	
	public void addNewsImage(String filePath,String tempImageId){
		NewsImageEntity entity = new NewsImageEntity();
		entity.localImagePath = filePath;
		entity.tempIndex = tempImageId;
		addedImageList.add(entity);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;

		Bitmap bitmap = ImageUtil.loadFromFile(filePath, options);
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.cell_expert_news_image, null);
		
		entity.view = view;
		view.setTag(entity);
		
		ImageView imageNews = (ImageView)view.findViewById(R.id.imageNews);
		imageNews.setImageBitmap(bitmap);
		
		ImageView iconDelete = (ImageView)view.findViewById(R.id.iconDelete);
		iconDelete.setTag(entity);
		
		final HorizontalScrollView  hView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
		hView.setVisibility(View.VISIBLE);
		
		iconDelete.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewsImageEntity entity = (NewsImageEntity)v.getTag();
				addedImageList.remove(entity);

				View view = entity.view;
				LinearLayout layout = (LinearLayout)findViewById(R.id.imageContainer);
				layout.removeView(view);
				
				if (layout.getChildCount() == 0){
					noImageLabel.setVisibility(View.GONE);
					hView.setVisibility(View.GONE);
				}
			}
		});
		
		
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.imageContainer);
		layout.addView(view);
		
		//noImageLabel.setVisibility(View.VISIBLE);
		
	
		
	}


	class ImageLoadTask extends AsyncTask<String,Void,List<NewsImageEntity>>
	{
		@Override
		protected List<NewsImageEntity> doInBackground(String... params){
			return NewsHandler.readNewsImageList(params[0]);
		}

		@Override
		protected void onPostExecute(List<NewsImageEntity> result)
		{
			if (result != null){
				originImageList = result;
				
				if (result != null && result.size() > 0)
					noImageLabel.setVisibility(View.GONE);
				
				LayoutInflater inflater = LayoutInflater.from(NewsEditActivity.this);
				LinearLayout layout = (LinearLayout)findViewById(R.id.imageContainer);
				
			
				for (int i = 0; i < result.size(); i++){
					View view = inflater.inflate(R.layout.cell_expert_news_image, null);
					NewsImageEntity entity = result.get(i);
					
					entity.view = view;
					view.setTag(entity);
					
					String imageURL = entity.getThumbnailURL();
					ImageView imageNews = (ImageView)view.findViewById(R.id.imageNews);
					BitmapDownloader.getInstance().displayImage(imageURL, null, imageNews, null);
					
					ImageView iconDelete = (ImageView)view.findViewById(R.id.iconDelete);
					iconDelete.setTag(entity);
					
					
					iconDelete.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							NewsImageEntity entity = (NewsImageEntity)v.getTag();
							if(entity.imageIndex != null)
								deletedImageList.add(entity);
							
							originImageList.remove(entity);

							View view = entity.view;
							LinearLayout layout = (LinearLayout)findViewById(R.id.imageContainer);
							layout.removeView(view);
							
							if (layout.getChildCount() == 0){
								noImageLabel.setVisibility(View.GONE);
								HorizontalScrollView  hView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView1);
								hView.setVisibility(View.GONE);
							}
						}
					});
					
					layout.addView(view);
				}
			}
			
			super.onPostExecute(result);
		}
	}

	
	
	class UploadImageTask extends AsyncTask<String, Void, TempImgEntity>{

		@Override
		protected TempImgEntity doInBackground(String... params) {
			String filePath = params[0];
			
			File image = new File(filePath);
			
			TempImgEntity tmpImage = NewsHandler.uploadNewsImage(image,filePath);
			
			return tmpImage;
		}

		@Override
		protected void onPostExecute(TempImgEntity data) {
			progressDlg.dismiss();
			if(data != null){
				addNewsImage(data.localImagePath,data.id);
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.msg_upload_error);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
			
			super.onPostExecute(data);
		}

		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}
		
	}
	
	class SaveNewsTask extends AsyncTask<String,Void,Boolean>{
		
		@Override
		protected Boolean doInBackground(String... params){
		
			String index = params[0];
			String title   = params[1];
			String content = params[2].replaceAll("\n", "\\\\n");
			
			List<String> added = new ArrayList<String>();
			for(NewsImageEntity entity : addedImageList){
				added.add(entity.tempIndex);
			}
			
			List<String> deleted = new ArrayList<String>();
			for(NewsImageEntity entity : deletedImageList){
				if(entity.imageIndex == null) continue;
				deleted.add(entity.imageIndex);
			}
			
			
			boolean result = false;
			result = NewsHandler.saveNews(index, title, content, added, deleted);
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result){
			progressDlg.dismiss();
			if (result == true){
				setResult(RESULT_OK);
				finish();
			}else{
				BigtureEnvironment.showAlertMessage(NewsEditActivity.this, 99);
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}	
		
		
	}

}
