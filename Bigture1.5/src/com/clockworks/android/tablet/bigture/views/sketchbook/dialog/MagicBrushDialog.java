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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MagicBrushDialog extends Dialog implements View.OnClickListener{
	private Context context;
	
	public static int BG_CAMERA=1;
	public static int BG_ALBUM=2;
	public static int BG_BUCKET=3;
	public static int BG_PEN=4;
	
	private WorkLayerView2 workLayerView;
	

	
	public MagicBrushDialog(Context context,WorkLayerView2 w) {
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
		
		setContentView(R.layout.popup_ske_magic_brush);
		
		findViewById(R.id.btnNeon).setOnClickListener(this);
		findViewById(R.id.btnRainbow).setOnClickListener(this);
		findViewById(R.id.btnSnow).setOnClickListener(this);
		findViewById(R.id.btnPaintDrop).setOnClickListener(this);
		findViewById(R.id.btnBirdFoot).setOnClickListener(this);
		findViewById(R.id.btnBubble).setOnClickListener(this);
		findViewById(R.id.btnLeaf).setOnClickListener(this);
		findViewById(R.id.btnFur).setOnClickListener(this);
		findViewById(R.id.btnTwinkle).setOnClickListener(this);

	}
	
	
	
	@Override
	public void onClick(View v) {
		DrawingMode drawingMode = workLayerView.getDrawingMode();
		switch(v.getId()){

		case R.id.btnNeon:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_NEON;
			drawingMode.strokeAlpha = 255;

			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnRainbow:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_RAINBOW;
			drawingMode.strokeAlpha = 255;

			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnSnow:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_snow"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnPaintDrop:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_RANDOM_BRUSH;
			drawingMode.assetIds = new String[] {"magic_drop1", "magic_drop2", "magic_drop3", "magic_drop4"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnBirdFoot:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_birdfootprint"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnBubble:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_bubble"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnLeaf:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_ROTATED_RANDOM_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_leaf1", "magic_leaf2", "magic_leaf3", "magic_leaf4", "magic_leaf5"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnFur:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_hair"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnTwinkle:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH;
			drawingMode.assetIds = new String[] {"magic_twinkle"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		
			
			
		}
		dismiss();
	}


}
