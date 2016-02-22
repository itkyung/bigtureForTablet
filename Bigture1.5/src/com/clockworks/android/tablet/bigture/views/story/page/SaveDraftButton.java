package com.clockworks.android.tablet.bigture.views.story.page;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SaveDraftButton extends RelativeLayout {
	Context context;
	ImageView icon;
	ImageView arrow;
	TextView title;
	
	
	public SaveDraftButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_story_draft, this);
	}
	

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.icon = (ImageView)findViewById(R.id.draftIcon);
		this.arrow = (ImageView)findViewById(R.id.draftArrow);
		this.title = (TextView)findViewById(R.id.draftTitle);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			focusOn();
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			focusOut();
		}
		
		return super.onTouchEvent(event);
	}



	private void focusOn(){
		this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bigturestorypage_book_add_savedraft_icon_p));
		this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.common_left_arrow_p));
		this.title.setTextColor(getResources().getColor(R.color.white));
	
	}
	
	private void focusOut(){
		this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bigturestorypage_book_add_savedraft_icon_n));
		this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.common_left_arrow));
		this.title.setTextColor(getResources().getColor(R.color.draft_text));
	
	}

}
