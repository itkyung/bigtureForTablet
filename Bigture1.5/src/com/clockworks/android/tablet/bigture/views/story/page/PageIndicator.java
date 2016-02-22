package com.clockworks.android.tablet.bigture.views.story.page;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PageIndicator extends LinearLayout
{
	int currentPage = 0;
	
	public PageIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		this.setOrientation(HORIZONTAL);
	}
	
	public void setPageCount(int pageCount)
	{
		DisplayMetrics metrics = BigtureEnvironment.getDisplayMetrics(getContext());
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.leftMargin = (int)(5 * metrics.density);
		params.rightMargin = (int)(5 * metrics.density);
		
		for (int i = 0; i < pageCount; i++)
		{				
			ImageView imageView = new ImageView(getContext());
			imageView.setLayoutParams(params);
			imageView.setImageResource(R.drawable.bigturestorypage_book_page_dot);
			imageView.setTag((Integer)i);
			
			if (i == 0)
				imageView.setSelected(true);
			
			addView(imageView);
		}
	}
	
	public void setCurrentPage(int page){
//		ImageView imageView = (ImageView)getChildAt(currentPage);
//		imageView.setSelected(false);
//		
//		currentPage = page;
//		
//		imageView = (ImageView)getChildAt(currentPage);
//		imageView.setSelected(true);
	}

}
