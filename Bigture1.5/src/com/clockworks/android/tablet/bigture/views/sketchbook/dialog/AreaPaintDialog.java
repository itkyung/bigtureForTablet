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

public class AreaPaintDialog extends Dialog implements View.OnClickListener{
	private Context context;
	
	
	private WorkLayerView2 workLayerView;
	

	
	public AreaPaintDialog(Context context,WorkLayerView2 w) {
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
		
		setContentView(R.layout.popup_ske_area_paint);
		
		findViewById(R.id.btnPlain).setOnClickListener(this);
		findViewById(R.id.btnHeart).setOnClickListener(this);
		findViewById(R.id.btnCheck).setOnClickListener(this);
		findViewById(R.id.btnCloud).setOnClickListener(this);
		findViewById(R.id.btnDot).setOnClickListener(this);
		findViewById(R.id.btnStar).setOnClickListener(this);
		findViewById(R.id.btnStripe1).setOnClickListener(this);
		findViewById(R.id.btnStripe2).setOnClickListener(this);
		

	}
	
	
	
	@Override
	public void onClick(View v) {
		DrawingMode drawingMode = workLayerView.getDrawingMode();
		switch(v.getId()){

		case R.id.btnPlain:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_SOLID;
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnHeart:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_heart"};
			workLayerView.setDrawingMode(drawingMode);
			
			break;
		case R.id.btnCheck:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_check"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnCloud:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_cloud"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnDot:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_dot"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnStar:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_star"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnStripe1:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_stripe1"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		case R.id.btnStripe2:
			drawingMode.drawingMode = UserDrawingManager.DRAWING_MODE_FILL_PATTERN;
			drawingMode.assetIds = new String[] {"fill_stripe2"};
			workLayerView.setDrawingMode(drawingMode);
			break;
		
			
			
		}
		dismiss();
	}


}
