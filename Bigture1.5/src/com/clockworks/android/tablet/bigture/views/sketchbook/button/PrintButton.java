package com.clockworks.android.tablet.bigture.views.sketchbook.button;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class PrintButton extends RelativeLayout {

	public PrintButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.btn_sketchbook_print, this);
	}

}
