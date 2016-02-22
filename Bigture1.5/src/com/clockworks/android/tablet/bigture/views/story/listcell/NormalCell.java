package com.clockworks.android.tablet.bigture.views.story.listcell;

import java.util.ArrayList;
import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class NormalCell extends RelativeLayout{
	BookThumbnail.Callback callback;
	BookThumbnail[] books;
	View viewAdd;
	
	
	static int ids[] = {R.id.book1, R.id.book2, R.id.book3, R.id.book4};
	
	public NormalCell(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		books = new BookThumbnail[4];
		
		viewAdd = findViewById(R.id.vwAdd);
		viewAdd.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onAddStoryBook();
			}
		});

		for (int i = 0; i < ids.length; i++)
			books[i] = (BookThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(BookThumbnail.Callback callback, ArrayList<StoryEntity> storyList, int position, boolean canAdd){
		this.callback = callback;
		
		books[0].setVisibility(View.INVISIBLE);
		books[1].setVisibility(View.INVISIBLE);
		books[2].setVisibility(View.INVISIBLE);
		books[3].setVisibility(View.INVISIBLE);
		
		if (position == 0 && storyList.get(0).storyId == null && canAdd){
			books[0].setVisibility(View.GONE);
			viewAdd.setVisibility(View.VISIBLE);
		}else{
			books[0].setVisibility(View.VISIBLE);
			viewAdd.setVisibility(View.GONE);
		}
		
		for (int i = 0, idx = 0; i < storyList.size(); i++, idx++){
			if (i < storyList.size()){
				StoryEntity entity = storyList.get(i);
				
				if(entity.storyId != null){
					books[idx].setVisibility(View.VISIBLE);
					books[idx].updateCell(callback, entity);
				}
			}else{
				books[idx].setVisibility(View.INVISIBLE);
			}
		}
	}
}
