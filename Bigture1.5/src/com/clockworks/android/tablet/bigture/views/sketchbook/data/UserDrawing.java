package com.clockworks.android.tablet.bigture.views.sketchbook.data;

import org.w3c.dom.Element;

import android.graphics.Canvas;

public interface UserDrawing
{
	public boolean isBackgroundObject();
	public void draw(Canvas canvas);
	
	public boolean isEditMode();
	public void setEditMode(boolean editMode);
	
	public String toXML();
	public void parse(Element element);
	
	public void readyForPlay();
	public boolean updateForPlay();
	public void drawForPlay(Canvas canvas);
}
