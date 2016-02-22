package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.views.sketchbook.WorkLayerView2;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.DrawingMode;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager.LayoutParams;

public class KoreanStickerDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private WorkLayerView2 workLayerView;
	
	public static int ST_CAMERA=1;
	public static int ST_ALBUM=2;
	
	public KoreanStickerDialog(Context context,WorkLayerView2 w) {
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
//		
		setContentView(R.layout.popup_ske_korean_sticker);
		
		
		findViewById(R.id.btnBg01).setOnClickListener(this);
		findViewById(R.id.btnBg02).setOnClickListener(this);
		findViewById(R.id.btnBg03).setOnClickListener(this);
		
		findViewById(R.id.btnText001).setOnClickListener(this);
		findViewById(R.id.btnText002).setOnClickListener(this);
		findViewById(R.id.btnText003).setOnClickListener(this);
		findViewById(R.id.btnText004).setOnClickListener(this);
		findViewById(R.id.btnText005).setOnClickListener(this);
		findViewById(R.id.btnText006).setOnClickListener(this);
		findViewById(R.id.btnText007).setOnClickListener(this);
		findViewById(R.id.btnText008).setOnClickListener(this);
		
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
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_koreanpainting_bg001);
			dismiss();
			break;
		case R.id.btnBg03:
			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_koreanpainting_bg002);
			dismiss();
			break;
			
		case R.id.btnText001:
			assetId = "korean_text001";
			break;
		case R.id.btnText002:
			assetId = "korean_text002";
			break;
		case R.id.btnText003:
			assetId = "korean_text003";
			break;
		case R.id.btnText004:
			assetId = "korean_text004";
			break;
		case R.id.btnText005:
			assetId = "korean_text005";
			break;
		case R.id.btnText006:
			assetId = "korean_text006";
			break;
		case R.id.btnText007:
			assetId = "korean_text007";
			break;
		case R.id.btnText008:
			assetId = "korean_text008";
			break;
		
		}
		
		dismiss();
		if(assetId != null){
			setMode();
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_STICKER;
			dm.assetIds = new String[] {assetId};
			//dm.bitmapResIds = new int[] {stickerIds[i] };
			workLayerView.setDrawingMode(dm);	
		}
	}

	private void setMode(){
		DrawingMode dm = workLayerView.getDrawingMode();
		dm.drawingMode = UserDrawingManager.DRAWING_MODE_STICKER;
		dm.assetIds = null;
	}

}
