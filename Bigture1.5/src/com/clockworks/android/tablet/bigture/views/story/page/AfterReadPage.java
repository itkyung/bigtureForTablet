package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SketchbookActivity;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AfterReadPage extends RelativeLayout implements View.OnClickListener{
	Context context;
	
	StoryEntity storyEntity;
	//List<StoryArtworkEntity> artworkList;
	
	
	public AfterReadPage(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
	}

	@Override
	protected void onFinishInflate(){
		super.onFinishInflate();
		findViewById(R.id.afterBg).setOnClickListener(this);
	}

	public void initView(StoryEntity storyEntity){
		this.storyEntity = storyEntity;
	}
	
	
	@Override
	public void onClick(View v){
		if (v.getId() == R.id.afterBg){
			Intent intent = new Intent(context, SketchbookActivity.class);
			
			DrawingPurpose purpose = new DrawingPurpose();
			purpose.reason = DrawingPurpose.STORY;
			purpose.storyId = storyEntity.storyId;
			purpose.pageNo  = 0;	//0이면 afterread
			purpose.pageId = null;
			purpose.afterReading = true;

			intent.putExtra("drawingPurpose", purpose);
			
			((Activity)context).startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
		}
	}

}
