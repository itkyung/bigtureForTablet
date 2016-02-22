package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IllustratorMyCell extends LinearLayout{
	View imageDivider;
	ImageView imageArtwork;
	TextView pageLabel;
	BitmapFactory.Options bitmapOptions;
	
	public IllustratorMyCell(Context context){
		super(context);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.cell_storypage_illustrator_mybigture_page, this);
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		imageDivider = findViewById(R.id.imageDivider);
		
		pageLabel = (TextView)findViewById(R.id.textPage);
		imageArtwork = (ImageView)findViewById(R.id.imageArtwork1);
	
	}

	public void updateCell(StoryArtworkEntity item, int page, int pos){
		
		pageLabel.setText(page + " " + getResources().getString(R.string.story_lb_page_info));		
		String imageURL = item.getArtworkImageURL();
		BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, imageArtwork, null);

	}
}
