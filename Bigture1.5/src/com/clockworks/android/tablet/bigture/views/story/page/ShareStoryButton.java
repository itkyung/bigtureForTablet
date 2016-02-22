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

public class ShareStoryButton extends RelativeLayout {
	Context context;
	ImageView icon;
	ImageView arrow;
	TextView title;
	TextView desc;
	
	public ShareStoryButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_story_share, this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.icon = (ImageView)findViewById(R.id.bigtureIcon);
		this.arrow = (ImageView)findViewById(R.id.shareArrow);
		this.title = (TextView)findViewById(R.id.shareTitle);
		this.desc = (TextView)findViewById(R.id.shareDesc);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isEnabled()){
			if(event.getAction() == MotionEvent.ACTION_DOWN){
				focusOn();
			}else if(event.getAction() == MotionEvent.ACTION_UP){
				focusOut();
			}
		}
		return super.onTouchEvent(event);
	}

	private void focusOn(){
		this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bigturestorypage_book_add_share_icon_p));
		this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.common_left_arrow_p));
		this.title.setTextColor(getResources().getColor(R.color.white));
		this.desc.setTextColor(getResources().getColor(R.color.white));
	}
	
	private void focusOut(){
		this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bigturestorypage_book_add_share_icon_n));
		this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.common_left_arrow));
		this.title.setTextColor(getResources().getColor(R.color.share_story_text));
		this.desc.setTextColor(getResources().getColor(R.color.share_story_text2));
	}

	@Override
	public void setEnabled(boolean enabled) {
		if(enabled){
			focusOut();
		}else{
			//dimmed처리.
			this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bigturestorypage_book_add_share_icon_dimmed));
			this.arrow.setImageDrawable(getResources().getDrawable(R.drawable.common_left_arrow_dimmed));
			this.title.setTextColor(getResources().getColor(R.color.share_story_text_dimmed));
			this.desc.setTextColor(getResources().getColor(R.color.share_story_text_dimmed));
		}
		super.setEnabled(enabled);
	}
	
	
}
