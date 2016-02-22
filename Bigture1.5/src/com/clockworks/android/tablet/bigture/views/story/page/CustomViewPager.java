package com.clockworks.android.tablet.bigture.views.story.page;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class CustomViewPager extends ViewPager
{
	public CustomViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected boolean canScroll(View view, boolean checkView, int dx, int x, int y)
	{
		if (view != this && view instanceof ViewPager)
		{
			ViewPager pager = (ViewPager)view;
			if (pager.getAdapter() != null && pager.getAdapter().getCount() > 1)
				return true;
			else
				return false;
		}

		return super.canScroll(view, checkView, dx, x, y);
	}	
}
