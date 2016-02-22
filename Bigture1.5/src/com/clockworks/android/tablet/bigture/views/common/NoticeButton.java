package com.clockworks.android.tablet.bigture.views.common;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoticeButton extends RelativeLayout {
	Context context;
	TextView title;
	
	public NoticeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_main, this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
	
		this.title = (TextView)findViewById(R.id.btnTitle);
		this.title.setText(getResources().getString(R.string.common_lb_menu_activity));
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
		this.title.setTextColor(getResources().getColor(R.color.white));
	
	}
	
	private void focusOut(){
		this.title.setTextColor(getResources().getColor(R.color.main_btn_text));
	
	}
}
