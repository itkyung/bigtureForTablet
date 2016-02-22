package com.clockworks.android.tablet.bigture.views.story.page;


import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;


import android.content.Context;

import android.util.AttributeSet;

import android.widget.RelativeLayout;


public class CoverPage extends RelativeLayout{
	StoryEntity storyEntity;
	
	public CoverPage(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		
		super.onFinishInflate();
	}
	
	public void initView(StoryEntity entity){
		this.storyEntity = entity;
		
		
	}
	
	public void updateArrow(StoryEntity storyEntity){
		this.storyEntity = storyEntity;
		
	}

}
