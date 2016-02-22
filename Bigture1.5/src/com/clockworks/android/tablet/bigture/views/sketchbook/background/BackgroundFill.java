package com.clockworks.android.tablet.bigture.views.sketchbook.background;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;




import android.graphics.Canvas;
import android.graphics.Color;

public class BackgroundFill implements UserDrawing
{
	int color;
	boolean editMode;

	public BackgroundFill()
	{
		this.color = Color.TRANSPARENT;
	}
	
	public BackgroundFill(int color)
	{
		this.color = color;
	}
	
	@Override
	public boolean isEditMode()
	{
		return this.editMode;
	}

	@Override
	public void setEditMode(boolean editMode)
	{
		this.editMode = editMode;
	}

	@Override
	public boolean isBackgroundObject()
	{
		return true;
	}

	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawColor(color);
	}

	@Override
	public String toXML()
	{
		return "<BackgroundFill color=\"" + color + "\" />";
	}

	@Override
	public void parse(Element element)
	{
		this.color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
	}

	@Override
	public void readyForPlay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateForPlay()
	{
		return true;
	}

	@Override
	public void drawForPlay(Canvas canvas)
	{
		draw(canvas);
	}
}
