package com.clockworks.android.tablet.bigture.views.story.listcell;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ShortCell extends RelativeLayout {

	BookThumbnail.Callback callback;
	BookThumbnail[] books;

	
	
	static int ids[] = {R.id.book1, R.id.book2, R.id.book3};
	
	public ShortCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate(){
		books = new BookThumbnail[3];
		
		for (int i = 0; i < ids.length; i++)
			books[i] = (BookThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(BookThumbnail.Callback callback, ArrayList<StoryEntity> storyList, int position, boolean canAdd){
		this.callback = callback;
		
		books[0].setVisibility(View.INVISIBLE);
		books[1].setVisibility(View.INVISIBLE);
		books[2].setVisibility(View.INVISIBLE);
	
		books[0].setVisibility(View.VISIBLE);
		
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
