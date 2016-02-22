package com.clockworks.android.tablet.bigture;

import java.io.File;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.crop.CropView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;

public class CropActivity extends Activity
{
	CropView cropView;
	
	int returnImageWidth;
	int returnImageHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);

		cropView = (CropView)findViewById(R.id.cropView);
		
		cropView.setContentResolver(getContentResolver());
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			Uri imageUri = bundle.getParcelable("imageUri");
			if (imageUri != null){
				String filePath = getImagePath(imageUri);
				
				int aspectX = bundle.getInt("aspectX", 0);
				int aspectY = bundle.getInt("aspectY", 0);
				
				returnImageWidth  = bundle.getInt("returnImageWidth", 0);
				returnImageHeight = bundle.getInt("returnImageHeight", 0);
				
				cropView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				

				DisplayMetrics displayMetrics = new DisplayMetrics(); 
				getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); 
				    
				int width = displayMetrics.widthPixels; 
				int height = displayMetrics.heightPixels;
				
				cropView.setImagePath(filePath, aspectX, aspectY, width, height - 56);
			}
		}
		
		findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
			}
		});
		
		findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Bitmap bitmap = null;
				
				
				if (returnImageWidth != 0 && returnImageHeight != 0)
					bitmap = cropView.cropAndResize(returnImageWidth, returnImageHeight);
				else
					bitmap = cropView.crop();
				
				
				String filePath = FileUtil.getTempPath(CropActivity.this) + File.separator + "crop.image";
				ImageUtil.saveJPEGBitmap(bitmap, filePath);
				//ImageUtil.saveBitmap(bitmap, filePath);
				
				Intent i = new Intent();
				i.putExtra("imagePath", filePath);
				
				// ���� �ڵ带 �����Ѵ�.
				CropActivity.this.setResult(Activity.RESULT_OK, i);
				
				finish();
			}
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				CropActivity.this.setResult(Activity.RESULT_CANCELED);
				finish();
			}
		});
	}
	
	private String getImagePath(Uri imageUri){
		String filePath = null;
		String uriString = imageUri.toString();

		if (uriString.startsWith("file:")){
			File file = new File(imageUri.getPath());
			filePath = file.getAbsolutePath();
		}else if (uriString.startsWith("content:")){
			if(uriString.startsWith("content://com.android.gallery3d.provider")){
				uriString = "content://com.google.android.gallery3d.provider";
				imageUri = Uri.parse(imageUri.toString().replace("com.android.gallery3d","com.google.android.gallery3d"));
			}
			
			if(uriString.startsWith("content://com.google.android.gallery3d.provider")){
				final String[] filePathColumn = { MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
				Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
				if(cursor != null){
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(MediaColumns.DATA);
					// if it is a picasa image on newer devices with OS 3.0 and up
					
					columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
					if (columnIndex != -1) {
						filePath = imageUri.toString();
//						final Uri uriurl = imageUri;
//						// Do this in a background thread, since we are fetching a large image from the web
//						new Thread(new Runnable() {
//							public void run() {
//								Bitmap the_image = getBitmap("image_file_name.jpg", uriurl);
//							}
//						}).start();
					}
					
				}
			}else{
			
				final String[] projection = {MediaStore.Images.Media.DATA};
				
				Cursor cursor = getContentResolver().query(imageUri, projection, null, null, null);
				cursor.moveToFirst();
				
				int columnIndex = cursor.getColumnIndex(projection[0]);
				filePath = cursor.getString(columnIndex);
	
				cursor.close();		
			}
		}

		return filePath;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == BigtureEnvironment.REQ_CODE_PICKUP_PICTURE){
			if (resultCode == RESULT_OK){
				Uri imageUri = data.getData();
				String filePath = getImagePath(imageUri);
				
				cropView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
				
				DisplayMetrics displayMetrics = new DisplayMetrics(); 
				getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); 
				    
				int width = displayMetrics.widthPixels; 
				int height = displayMetrics.heightPixels;
				
				cropView.setImagePath(filePath, 3, 2, width, height - 56);
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
