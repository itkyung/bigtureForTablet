package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileThumbnail extends RelativeLayout{
	ImageView imageArtwork;
	TextView filenameLabel;
	TextView timeLabel;
	
	
	Callback callback;
	
	String dataDir;
	String fileName;

	public FileThumbnail(Context context){
		super(context);

		dataDir = FileUtil.getDataPath(context);

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_file_thumb, this);

		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		filenameLabel = (TextView)findViewById(R.id.filenameLabel);
		timeLabel = (TextView)findViewById(R.id.timeLabel);
		
	}
	
	public FileThumbnail(Context context, AttributeSet attrs){
		super(context, attrs);

		dataDir = FileUtil.getDataPath(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_file_thumb, this);

		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		filenameLabel = (TextView)findViewById(R.id.filenameLabel);
		timeLabel = (TextView)findViewById(R.id.timeLabel);
		
		
		imageArtwork.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				if (callback != null)
					callback.onFileSelected(fileName);
			}
		});
		
		ImageButton ibDelete = (ImageButton)findViewById(R.id.btnDelete);
		ibDelete.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (callback != null)
					callback.onFileDeleted(fileName);
			}
		});
	}

//	@Override
//	protected void onFinishInflate()
//	{
//		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
//		filenameLabel = (TextView)findViewById(R.id.filenameLabel);
//		timeLabel = (TextView)findViewById(R.id.timeLabel);
//
//		super.onFinishInflate();
//	}

	public void updateFileInfo(File file, Callback callback){
		this.callback = callback;
		this.fileName = file.getName();
		
		if(fileName.endsWith(".bigture")){
			fileName = fileName.substring(0,fileName.indexOf(".bigture"));
		}
		
		filenameLabel.setText(fileName);
		Date d = new Date(file.lastModified());
		
		timeLabel.setText(DateUtil.getDateFormatString(d, "yyyy-MM-dd HH:mm:ss"));

		
		
		try
		{
			ZipFile zf = new ZipFile(file);
	
			Enumeration<? extends ZipEntry> enumerator = zf.entries();
			
			while( enumerator.hasMoreElements() )
			{
				ZipEntry entry = (ZipEntry)enumerator.nextElement();
				
				if (entry.getName().contains("thumb.image"))
				{
					InputStream is = zf.getInputStream(entry);
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					imageArtwork.setImageBitmap(bitmap);
					is.close();
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
	}
	
	public interface Callback
	{
		void onFileSelected(String filename);
		void onFileDeleted(String filename);
	}
}
