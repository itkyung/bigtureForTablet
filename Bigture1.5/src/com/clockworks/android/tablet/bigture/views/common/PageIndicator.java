package com.clockworks.android.tablet.bigture.views.common;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageIndicator extends LinearLayout
{
	int currentPage = 0;
	
	public PageIndicator(Context context, AttributeSet attrs){
		super(context, attrs);

		this.setOrientation(HORIZONTAL);
	}
	
	public void setPageCount(int pageCount){
		DisplayMetrics metrics = BigtureEnvironment.getDisplayMetrics(getContext());
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int)(5 * metrics.density);
		params.rightMargin = (int)(5 * metrics.density);
		
		for (int i = 0; i < pageCount; i++){				
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(params);
			imageView.setImageBitmap(createNormalDot());
			imageView.setTag((Integer)i);
			
			
			addView(imageView);
		}
	}
	
	public void setCurrentPage(int page){
		ImageView imageView = (ImageView)getChildAt(currentPage);
		imageView.setImageBitmap(createNormalDot());
		
		currentPage = page;
		
		imageView = (ImageView)getChildAt(currentPage);
		imageView.setImageBitmap(createNowDot());
	}
	
	private Bitmap createNormalDot(){
		Bitmap bm = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
		Canvas backScreen = new Canvas(bm);
		//backScreen.drawColor(R.color.bot_normal);
		backScreen.drawColor(Color.parseColor("#c9c7c3"));
		backScreen.drawBitmap(bm,0,0,null);
		return ImageUtil.createCircularBitmap(bm,10);
	}

	private Bitmap createNowDot(){
		Bitmap bm = Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565);
		Canvas backScreen = new Canvas(bm);
		backScreen.drawColor(Color.parseColor("#d3401e"));
		backScreen.drawBitmap(bm,0,0,null);
		return ImageUtil.createCircularBitmap(bm,10);
	}
}
