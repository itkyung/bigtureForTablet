package com.clockworks.android.tablet.bigture.views.common;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchableImageView extends ImageView
{
	public TouchableImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.setOnTouchListener(new ImageOnTouchListener());
	}

	public TouchableImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		this.setOnTouchListener(new ImageOnTouchListener());
	}

	private class ImageOnTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View arg0, MotionEvent event){
			ImageView iv = (ImageView)arg0;
			
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			
//			if (action == MotionEvent.ACTION_DOWN)
//			{
//				iv.setColorFilter(0x88111111, Mode.SRC_OVER);
//			}
//			else if (action == MotionEvent.ACTION_CANCEL ||
//					 action == MotionEvent.ACTION_UP)
//			{
//				iv.setColorFilter(0x00000000, Mode.SRC_OVER);
//			}
			
			return false;
		}
	}
}
