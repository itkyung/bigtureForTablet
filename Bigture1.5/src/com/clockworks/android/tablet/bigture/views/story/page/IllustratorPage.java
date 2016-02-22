package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.ArrayList;
import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.StoryPageChanger;
import com.clockworks.android.tablet.bigture.adapter.story.IllustratorAdapter;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IllustratorPage extends RelativeLayout{
	StoryEntity storyEntity;
	ArrayList<StoryPageEntity> pageList;
	ListView listView;
	TextView titleLabel;
	StoryPageChanger changer;
	
	public IllustratorPage(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		super.onFinishInflate();
		this.listView = (ListView)findViewById(R.id.listView);
		this.titleLabel = (TextView)findViewById(R.id.titleLabel);
	}

	public void initView(StoryEntity storyEntity,boolean myBigture){
		this.storyEntity = storyEntity;
		this.pageList = storyEntity.pages;
		
		String title = getResources().getString(R.string.story_lb_illustrators);
		
		if(myBigture){
			titleLabel.setText(title);
		}else{
			titleLabel.setText(title + " (" + storyEntity.memberCount + ")");
		}
		
		IllustratorAdapter adapter = new IllustratorAdapter(getContext(), pageList, myBigture,this.changer);
		listView.setAdapter(adapter);
		
//		EpilogueAdapter adapter = new EpilogueAdapter(getContext(), storyEntity);
//		listView.setAdapter(adapter);
	}
	
	public void setChanger(StoryPageChanger changer){
		this.changer = changer;
	}
	
}
