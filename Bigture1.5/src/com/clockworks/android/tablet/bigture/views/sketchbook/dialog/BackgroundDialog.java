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

public class BackgroundDialog extends Dialog implements View.OnClickListener{
	private Context context;
	
	public static int BG_CAMERA=1;
	public static int BG_ALBUM=2;
	public static int BG_BUCKET=3;
	public static int BG_PEN=4;
	
	private BackgroundListener listener;
	private WorkLayerView2 workLayerView;
	
	private View bucketExtra;
	//private View penExtra;
	
	public BackgroundDialog(Context context,BackgroundListener l,WorkLayerView2 w) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.listener = l;
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
		setContentView(R.layout.popup_ske_background);
		
		bucketExtra = findViewById(R.id.bucketExtra);
	//	penExtra = findViewById(R.id.penExtra);
		
		findViewById(R.id.btnCamera).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bucketExtra.setVisibility(View.GONE);
			//	penExtra.setVisibility(View.GONE);
				listener.selectBackground(BG_CAMERA);
				dismiss();
			}
		});
		
		findViewById(R.id.btnAlbum).setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				bucketExtra.setVisibility(View.GONE);
			//	penExtra.setVisibility(View.GONE);
				listener.selectBackground(BG_ALBUM);
				dismiss();
			}
		});
		
		findViewById(R.id.btnBucket).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bucketExtra.setVisibility(View.VISIBLE);
			//	penExtra.setVisibility(View.GONE);

			}
		});
		
//		findViewById(R.id.btnPen).setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				bucketExtra.setVisibility(View.GONE);
//				penExtra.setVisibility(View.VISIBLE);
//			}
//		});
//		
		findViewById(R.id.btnBucketPlain).setOnClickListener(this);
	//	findViewById(R.id.btnPenPen).setOnClickListener(this);
	//	findViewById(R.id.btnPenBrush).setOnClickListener(this);
	//	findViewById(R.id.btnPenCrayon).setOnClickListener(this);

	}
	
	
	
	@Override
	public void onClick(View v) {
		DrawingMode dm = workLayerView.getDrawingMode();
		switch(v.getId()){
		case R.id.btnBucketPlain:
			workLayerView.setBackgroundColor(dm.foreColor);
			
			break;
		case R.id.btnPenPen:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_BACKGROUND_PEN;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
			break;
		case R.id.btnPenBrush:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_BACKGROUND_WBRUSH;
			dm.strokeAlpha = 128;
			workLayerView.setDrawingMode(dm);
			break;
		case R.id.btnPenCrayon:
			dm.drawingMode = UserDrawingManager.DRAWING_MODE_BACKGROUND_CRAYON;
			dm.strokeAlpha = 255;
			workLayerView.setDrawingMode(dm);
			break;
		
		}
		dismiss();
	}



	public interface BackgroundListener{
		public void selectBackground(int bgType);
	}
}
