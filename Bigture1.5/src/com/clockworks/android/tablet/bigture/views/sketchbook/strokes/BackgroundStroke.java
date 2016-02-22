package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

public class BackgroundStroke extends NormalStroke
{
	public BackgroundStroke()
	{
		super();
	}
	
	public BackgroundStroke(int color, int alpha, float thick)
	{
		super(color, alpha, thick);
	}

	@Override
	public boolean isBackgroundObject()
	{
		return true;
	}
}
