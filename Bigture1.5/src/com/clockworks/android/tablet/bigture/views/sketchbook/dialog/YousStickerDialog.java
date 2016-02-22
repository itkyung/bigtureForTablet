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

public class YousStickerDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private WorkLayerView2 workLayerView;
	
	public static int ST_CAMERA=1;
	public static int ST_ALBUM=2;
	
	public YousStickerDialog(Context context,WorkLayerView2 w) {
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
		
		setContentView(R.layout.popup_ske_yous_sticker);
		
		
		findViewById(R.id.btnFabric1).setOnClickListener(this);
		findViewById(R.id.btnFabric2).setOnClickListener(this);
		findViewById(R.id.btnCellophane).setOnClickListener(this);
		findViewById(R.id.btnVelcro).setOnClickListener(this);
		findViewById(R.id.btnPlastic).setOnClickListener(this);
		findViewById(R.id.btnWire01).setOnClickListener(this);
		findViewById(R.id.btnWire02).setOnClickListener(this);
		findViewById(R.id.btnRope).setOnClickListener(this);
		findViewById(R.id.btnTape).setOnClickListener(this);
		
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		setMode();
		DrawingMode dm = workLayerView.getDrawingMode();
		String assetId = null;
		switch(v.getId()){
		case R.id.btnFabric1:
			assetId = "fabric1";
			break;
		case R.id.btnFabric2:
			assetId = "fabric2";
			break;
		case R.id.btnCellophane:
			assetId = "cellophane";
			break;
		case R.id.btnVelcro:
			assetId = "velcro";
			break;
		case R.id.btnPlastic:
			assetId = "plastic";
			break;
		case R.id.btnWire01:
			assetId = "wire1";
			break;
		case R.id.btnWire02:
			assetId = "wire2";
			break;
		case R.id.btnRope:
			assetId = "rope";
			break;
		case R.id.btnTape:
			assetId = "tape";
			break;
		
			
		}
		
		dismiss();
		if(assetId != null){
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
