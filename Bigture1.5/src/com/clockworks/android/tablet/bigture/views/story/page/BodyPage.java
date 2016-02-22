package com.clockworks.android.tablet.bigture.views.story.page;

import java.io.File;
import java.util.Date;
import java.util.List;




import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


public class BodyPage extends RelativeLayout{
	protected StoryEntity storyEntity;
	protected StoryPageEntity pageEntity;
	protected Context context;
	
	public BodyPage(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
	}

	public void initView(Context context,StoryEntity storyEntity,StoryPageEntity pageEntity){
		this.storyEntity = storyEntity;
		this.pageEntity = pageEntity;
		
		
	}
	
	public void updateArrow(StoryEntity storyEntity,StoryPageEntity pageEntity){
		this.storyEntity = storyEntity;
		this.pageEntity = pageEntity;
		
	}
}
