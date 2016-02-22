package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.StoryPageChanger;
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

public class IllustratorCell extends LinearLayout{
	View imageDivider;
	ImageView[] imageArtworks;
	TextView[] textNames;
	View[] covers;
	TextView pageLabel;
	BitmapFactory.Options bitmapOptions;
	
	public IllustratorCell(Context context){
		super(context);
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.cell_storypage_illustrator_page, this);

		imageDivider = findViewById(R.id.imageDivider);
		
		pageLabel = (TextView)findViewById(R.id.textPage);
		
		imageArtworks = new ImageView[4];
		imageArtworks[0] = (ImageView)findViewById(R.id.imageArtwork1);
		imageArtworks[1] = (ImageView)findViewById(R.id.imageArtwork2);
		imageArtworks[2] = (ImageView)findViewById(R.id.imageArtwork3);
		
		textNames = new TextView[4];
		textNames[0] = (TextView)findViewById(R.id.textName1);
		textNames[1] = (TextView)findViewById(R.id.textName2);
		textNames[2] = (TextView)findViewById(R.id.textName3);
		
		covers = new View[4];
		covers[0] = findViewById(R.id.cover1);
		covers[1] = findViewById(R.id.cover2);
		covers[2] = findViewById(R.id.cover3);
	}

	public void updateCell(StoryArtworkSectionItem item, int pos, final StoryPageChanger changer){
		if (item.isViewPage()){
			setPadding(0, 30, 0, 0);
			pageLabel.setVisibility(View.VISIBLE);
		}else{
			setPadding(0, 0, 0, 0);
			pageLabel.setVisibility(View.INVISIBLE);
		}
		
		if (item.isViewDivider()){
			imageDivider.setVisibility(View.VISIBLE);
		}else{
			imageDivider.setVisibility(View.INVISIBLE);
		}
		
		
		
		pageLabel.setText(item.getPageNo() + " " + getResources().getString(R.string.story_lb_page_info));
		
		for (int i = 0; i < 3; i++){
			if (i >= item.getArtworks().size()){
				covers[i].setVisibility(View.INVISIBLE);
				imageArtworks[i].setVisibility(View.INVISIBLE);
				textNames[i].setVisibility(View.INVISIBLE);
			}else{
				covers[i].setVisibility(View.VISIBLE);
				imageArtworks[i].setVisibility(View.VISIBLE);
				textNames[i].setVisibility(View.VISIBLE);
				
				final StoryArtworkEntity entity = item.getArtworks().get(i);
				
				textNames[i].setText(entity.ownerName);
				
				imageArtworks[i].setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changer.setCurrentPage(entity.pageNo+1);
					}
				});
				
				String imageURL = entity.getThumbnailURL();
				BitmapDownloader.getInstance().displayImage(imageURL, bitmapOptions, imageArtworks[i], null);
			}
		}
	}
}
