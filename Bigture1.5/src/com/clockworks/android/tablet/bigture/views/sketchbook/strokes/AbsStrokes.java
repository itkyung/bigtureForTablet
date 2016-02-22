package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;

public abstract class AbsStrokes implements UserDrawing
{
	protected float thick;
	protected int color;
	protected int alpha;
	protected boolean editMode;
	
	public abstract void setThick(float thick);
	public float getThick() { return this.thick; };
	
	public abstract void setColor(int color);
	public int getColor() { return this.color; };
	
	public abstract void setAlpha(int alpha);
	public int getAlpha() { return this.alpha; };
	
	public abstract void moveTo(float x, float y);
	public abstract void lineTo(float x, float y);
	public abstract void quadTo(float x, float y);
	public abstract void close();
	
	public boolean isEditMode() { return editMode; }
	public void setEditMode(boolean editMode) { this.editMode = editMode; }
	public boolean isBackgroundObject() { return false; }
}
