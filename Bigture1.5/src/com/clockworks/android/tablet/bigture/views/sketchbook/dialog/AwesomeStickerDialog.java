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

public class AwesomeStickerDialog extends Dialog implements View.OnClickListener{
	private Context context;
	private WorkLayerView2 workLayerView;
	private AwesomeStickerListener listener;
	public static int ST_CAMERA=1;
	public static int ST_ALBUM=2;
	
	public AwesomeStickerDialog(Context context,AwesomeStickerListener l,WorkLayerView2 w) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.workLayerView = w;
		this.listener = l;
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
		
		setContentView(R.layout.popup_ske_awesome_sticker);
		
		
		findViewById(R.id.btnCamera).setOnClickListener(this);
		findViewById(R.id.btnAlbum).setOnClickListener(this);
		findViewById(R.id.btnBalloon).setOnClickListener(this);
		findViewById(R.id.btnFlower01).setOnClickListener(this);
		findViewById(R.id.btnFlower02).setOnClickListener(this);
		findViewById(R.id.btnLeaf01).setOnClickListener(this);
		findViewById(R.id.btnLeaf02).setOnClickListener(this);
		findViewById(R.id.btnBroccoli).setOnClickListener(this);
		findViewById(R.id.btnOrange).setOnClickListener(this);
		findViewById(R.id.btnKiwi).setOnClickListener(this);
		findViewById(R.id.btnLotus).setOnClickListener(this);
		findViewById(R.id.btnRamyeon).setOnClickListener(this);
		findViewById(R.id.btnWrap002).setOnClickListener(this);
		findViewById(R.id.btnWrap001).setOnClickListener(this);
		findViewById(R.id.btnCap002).setOnClickListener(this);
		findViewById(R.id.btnCap001).setOnClickListener(this);
		findViewById(R.id.btnCap003).setOnClickListener(this);
		findViewById(R.id.btnCap004).setOnClickListener(this);
		findViewById(R.id.btnCap005).setOnClickListener(this);
		findViewById(R.id.btnCap006).setOnClickListener(this);
		findViewById(R.id.btnCap007).setOnClickListener(this);
		findViewById(R.id.btnCork001).setOnClickListener(this);
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		setMode();
		DrawingMode dm = workLayerView.getDrawingMode();
		String assetId = null;
		switch(v.getId()){
		case R.id.btnCamera:
			listener.selectStickerPig(ST_CAMERA);
			dismiss();
			break;
		case R.id.btnAlbum:
			listener.selectStickerPig(ST_ALBUM);
			dismiss();
			break;
		case R.id.btnBalloon:
			assetId = "balloon";
			break;
		case R.id.btnFlower01:
			assetId = "flower01";
			break;
		case R.id.btnFlower02:
			assetId = "flower02";
			break;
		case R.id.btnLeaf01:
			assetId = "leaf01";
			break;
		case R.id.btnLeaf02:
			assetId = "leaf02";
			break;
		case R.id.btnBroccoli:
			assetId = "broccoli";
			break;
		case R.id.btnOrange:
			assetId = "orange";
			break;
		case R.id.btnKiwi:
			assetId = "kiwi";
			break;
		case R.id.btnLotus:
			assetId = "lotus";
			break;
		case R.id.btnRamyeon:
			assetId = "ramyeon";
			break;
		case R.id.btnWrap002:
			assetId = "wrap002";
			break;
		case R.id.btnWrap001:
			assetId = "wrap001";
			break;
		case R.id.btnCap001:
			assetId = "cap001";
			break;
		case R.id.btnCap002:
			assetId = "cap002";
			break;	
		case R.id.btnCap003:
			assetId = "cap003";
			break;	
		case R.id.btnCap004:
			assetId = "cap004";
			break;			
		case R.id.btnCap005:
			assetId = "cap005";
			break;				
		case R.id.btnCap006:
			assetId = "cap006";
			break;	
		case R.id.btnCap007:
			assetId = "cap007";
			break;				
		case R.id.btnCork001:
			assetId = "cork001";
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
	
	public interface AwesomeStickerListener{
		public void selectStickerPig(int bgType);
	}
}
