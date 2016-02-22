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

public class AquaStickerDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private WorkLayerView2 workLayerView;
	
	public static int ST_CAMERA=1;
	public static int ST_ALBUM=2;
	
	public AquaStickerDialog(Context context,WorkLayerView2 w) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.workLayerView = w;
	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 231, context.getResources().getDisplayMetrics());
		final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 344, context.getResources().getDisplayMetrics());
		
		LayoutParams params = getWindow().getAttributes();
		params.width = width;
		params.height = height;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		setContentView(R.layout.popup_ske_aqua_sticker);
		
		
		findViewById(R.id.btnBg01).setOnClickListener(this);
		findViewById(R.id.btnBg02).setOnClickListener(this);
		findViewById(R.id.btnBg03).setOnClickListener(this);
		findViewById(R.id.btnBg04).setOnClickListener(this);
		findViewById(R.id.btnBg05).setOnClickListener(this);
		findViewById(R.id.btnBanner).setOnClickListener(this);
		findViewById(R.id.btnRedlion).setOnClickListener(this);
		findViewById(R.id.btnRedtail).setOnClickListener(this);
		findViewById(R.id.btnBlacktip).setOnClickListener(this);
		
		findViewById(R.id.btnBoxfish).setOnClickListener(this);
		findViewById(R.id.btnShark).setOnClickListener(this);
		findViewById(R.id.btnJelly).setOnClickListener(this);
		findViewById(R.id.btnPenguin).setOnClickListener(this);
		findViewById(R.id.btnWalrus).setOnClickListener(this);
		findViewById(R.id.btnDolphin).setOnClickListener(this);
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		
		DrawingMode dm = workLayerView.getDrawingMode();
		String assetId = null;
		switch(v.getId()){
//		case R.id.btnBg01:
//			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_sticker_white_bg);
//			dismiss();
//			break;
//		case R.id.btnBg02:
//			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_popup_sticker_hanhwa_bg02);
//			dismiss();
//			break;
//		case R.id.btnBg03:
//			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_popup_sticker_hanhwa_bg03);
//			dismiss();
//			break;
//		case R.id.btnBg04:
//			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_popup_sticker_hanhwa_bg04);
//			dismiss();
//			break;
//		case R.id.btnBg05:
//			workLayerView.setBackgroundBitmap(R.drawable.sketchbook_tool_popup_sticker_hanhwa_bg05);
//			dismiss();
//			break;
		case R.id.btnBanner:
			assetId = "banner";
			break;
		case R.id.btnRedlion:
			assetId = "redlion";
			break;
		case R.id.btnRedtail:
			assetId = "redtail";
			break;
		case R.id.btnBlacktip:
			assetId = "blacktip";
			break;
		case R.id.btnBoxfish:
			assetId = "boxfish";
			break;
		case R.id.btnShark:
			assetId = "shark";
			break;
		case R.id.btnJelly:
			assetId = "jelly";
			break;
		case R.id.btnPenguin:
			assetId = "penguin";
			break;
		case R.id.btnWalrus:
			assetId = "walrus";
			break;
		case R.id.btnDolphin:
			assetId = "dolphin";
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
