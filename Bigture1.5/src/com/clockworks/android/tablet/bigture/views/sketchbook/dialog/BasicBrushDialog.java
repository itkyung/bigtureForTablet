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

public class BasicBrushDialog extends Dialog implements View.OnClickListener{
	private Context context;
	
	public static int BG_CAMERA=1;
	public static int BG_ALBUM=2;
	public static int BG_BUCKET=3;
	public static int BG_PEN=4;
	
	private WorkLayerView2 workLayerView;
	

	
	public BasicBrushDialog(Context context,WorkLayerView2 w) {
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
		
		setContentView(R.layout.popup_ske_basic_brush);
		
		findViewById(R.id.btnPenPen).setOnClickListener(this);
		findViewById(R.id.btnPenBrush).setOnClickListener(this);
		findViewById(R.id.btnPenCrayon).setOnClickListener(this);
		
		DrawingMode dm = workLayerView.getDrawingMode();
		if(dm.drawingMode == UserDrawingManager.DRAWING_MODE_PEN){
			findViewById(R.id.btnPenPen).setSelected(true);
		}else{
			findViewById(R.id.btnPenPen).setSelected(false);
		}
		
	}
	
	
	
	@Override
	public void onClick(View v) {
		DrawingMode dm = workLayerView.getDrawingMode();
		switch(v.getId()){

		case R.id.btnPenPen:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_PEN;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
			break;
		case R.id.btnPenBrush:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_WBRUSH;
			dm.strokeAlpha = 128;
			workLayerView.setDrawingMode(dm);
			break;
		case R.id.btnPenCrayon:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_CRAYON;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
			break;
		
		}
		dismiss();
	}


}
