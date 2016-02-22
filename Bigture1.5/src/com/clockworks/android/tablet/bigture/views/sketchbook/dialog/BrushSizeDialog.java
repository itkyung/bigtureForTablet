package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.sketchbook.WorkLayerView2;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.BackgroundDialog.BackgroundListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BrushSizeDialog extends Dialog {
	private Context context;
	private BrushSizeListener listener;
	private WorkLayerView2 workLayerView;
	private ImageView brushImg;
	private SeekBar seekBar;
	
	public BrushSizeDialog(Context context,BrushSizeListener l,WorkLayerView2 w) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.listener = l;
		this.workLayerView = w;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	
		setContentView(R.layout.popup_ske_brush_size);
		
		this.brushImg = (ImageView)findViewById(R.id.brushSizeImg);
		this.seekBar = (SeekBar)findViewById(R.id.seekBar1);
		
		int strokeThickLevel = workLayerView.getStrokeThickLevel(); 
		seekBar.setProgress((strokeThickLevel-1) * 10);
		
		int width = calculateWidth(strokeThickLevel);
		LayoutParams params = brushImg.getLayoutParams();
		params.width = width;
		params.height = width;
		brushImg.setLayoutParams(params);
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				if (fromUser){
					UserDrawingManager manager = UserDrawingManager.getInstance();
					
					 
					
					int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
					int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());

					if (progress < 10){
						seekBar.setProgress(0);
						//workLayerView.setStrokeThick(5.f * manager.density * manager.viewScale,1);
						workLayerView.setStrokeThick(7.f,1);
						width = width * 1;
						height = height * 1;
						listener.selectBrushSize(1);
					}else if (progress >= 10 && progress < 20){
						seekBar.setProgress(10);
						//workLayerView.setStrokeThick(8.f * manager.density * manager.viewScale,2);
						workLayerView.setStrokeThick(14.f,2);
						width = width * 2;
						height = height * 2;
						listener.selectBrushSize(2);
					}else if (progress >= 20 && progress < 30){
						seekBar.setProgress(20);
						//workLayerView.setStrokeThick(11.f * manager.density * manager.viewScale,3);
						workLayerView.setStrokeThick(21.f,3);
						width = width * 3;
						height = height * 3;
						listener.selectBrushSize(3);
					}else if (progress >= 30 && progress < 40){
						seekBar.setProgress(30);
						//workLayerView.setStrokeThick(15.f * manager.density * manager.viewScale,4);
						workLayerView.setStrokeThick(28.f,4);
						width = width * 4;
						height = height * 4;
						listener.selectBrushSize(4);	
					}else if (progress >= 40 && progress < 50){
						seekBar.setProgress(40);
						//workLayerView.setStrokeThick(18.f * manager.density * manager.viewScale,5);
						workLayerView.setStrokeThick(35.f,5);
						width = width * 5;
						height = height * 5;
						listener.selectBrushSize(5);
						
					}else if (progress >= 50 && progress < 60){
						seekBar.setProgress(50);
						//workLayerView.setStrokeThick(21.f * manager.density * manager.viewScale,6);
						workLayerView.setStrokeThick(42.f,6);
						width = width * 6;
						height = height * 6;
						listener.selectBrushSize(6);
					}else if (progress >= 60 && progress < 70){
						seekBar.setProgress(60);
						//workLayerView.setStrokeThick(24.f * manager.density * manager.viewScale,7);
						workLayerView.setStrokeThick(49.f,7);
						width = width * 7;
						height = height * 7;
						listener.selectBrushSize(7);
					}else if (progress >= 70 && progress < 80){
						seekBar.setProgress(70);
						//workLayerView.setStrokeThick(27.f * manager.density * manager.viewScale,8);
						workLayerView.setStrokeThick(56.f,8);
						width = width * 8;
						height = height * 8;
						listener.selectBrushSize(8);
					}else if (progress >= 80 && progress < 90){
						seekBar.setProgress(80);
						//workLayerView.setStrokeThick(29.f * manager.density * manager.viewScale,9);
						workLayerView.setStrokeThick(64.f,9);
						width = width * 9;
						height = height * 9;
						listener.selectBrushSize(9);
					}else if (progress >= 90){
						seekBar.setProgress(100);
						//workLayerView.setStrokeThick(30.f * manager.density * manager.viewScale,10);
						workLayerView.setStrokeThick(70.f,10);
						width = width * 10;
						height = height * 10;
						listener.selectBrushSize(10);
					}
					
					Log.i("BRUSH", "width : " + width);
					
					LayoutParams params = brushImg.getLayoutParams();
					params.width = width;
					params.height = height;
					brushImg.setLayoutParams(params);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}

		});

		
	}
	
	
	private int calculateWidth(int strokeThickLevel){
		int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
		int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
		
		return width * strokeThickLevel;
	}
	
	
	public interface BrushSizeListener{
		public void selectBrushSize(int level);
	}
	
}


