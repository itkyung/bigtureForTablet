package com.clockworks.android.tablet.bigture.views.sketchbook.button;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SaveAlbumButton extends RelativeLayout {
	Context context;
	ImageView icon;
	
	TextView title;
		
	
	
	public SaveAlbumButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_sketchbook_save_album, this);
	}


	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.icon = (ImageView)findViewById(R.id.bigtureIcon);
		this.title = (TextView)findViewById(R.id.shareTitle);
		
	}
	
}
