package com.clockworks.android.tablet.bigture.views.common;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import android.widget.RelativeLayout;
import android.widget.TextView;

public class SketchbookButton extends RelativeLayout {
	Context context;
	TextView title;
	
	public SketchbookButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_main, this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.title = (TextView)findViewById(R.id.btnTitle);
		this.title.setText(getResources().getString(R.string.menu_sketchbook));
		this.title.setTextColor(getResources().getColor(R.color.white));
	}
	
	
	
}
