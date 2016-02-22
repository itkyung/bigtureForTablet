package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.sketchbook.WorkLayerView2;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.DrawingMode;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager.LayoutParams;

public class WorldStickerDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private WorkLayerView2 workLayerView;
	
	public static int ST_CAMERA=1;
	public static int ST_ALBUM=2;
	
	public WorldStickerDialog(Context context,WorkLayerView2 w) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.workLayerView = w;
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 231, context.getResources().getDisplayMetrics());
		final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 344, context.getResources().getDisplayMetrics());
		
//		LayoutParams params = getWindow().getAttributes();
//		params.width = width;
//		params.height = height;
//		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		setContentView(R.layout.popup_ske_world_sticker);
		
		
		findViewById(R.id.btnBg01).setOnClickListener(this);
		findViewById(R.id.btnBg02).setOnClickListener(this);
		findViewById(R.id.btnBg03).setOnClickListener(this);
		findViewById(R.id.btnBg04).setOnClickListener(this);
		findViewById(R.id.btnBg05).setOnClickListener(this);
		findViewById(R.id.btnBg06).setOnClickListener(this);
		findViewById(R.id.btnBg07).setOnClickListener(this);
		
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		
		DrawingMode dm = workLayerView.getDrawingMode();
		String assetId = null;
		switch(v.getId()){
		case R.id.btnBg01:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_white_bg);
			dismiss();
			break;
		case R.id.btnBg02:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg001);
			dismiss();
			break;
		case R.id.btnBg03:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg002);
			dismiss();
			break;
		case R.id.btnBg04:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg003);
			dismiss();
			break;
		case R.id.btnBg05:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg004);
			dismiss();
			break;
		case R.id.btnBg06:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg005);
			dismiss();
			break;	
		
		case R.id.btnBg07:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_worldfamous_bg006);
			dismiss();
			break;	
		}
		
	}



}
