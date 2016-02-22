package com.clockworks.android.tablet.bigture.views.sketchbook.data;

public class DrawingMode
{
	public int drawingMode;

	public int foreColor;
	public int strokeAlpha;
	public float strokeThick;

	public int backColor;
	public int backAlpha;
	public int strokeThickLevel;
	
	//public int[] bitmapResIds;
	public String[] assetIds;
	public String bitmapFilePath;
	
	public DrawingMode()
	{
		drawingMode = UserDrawingManager.DRAWING_MODE_PEN;

		strokeThickLevel = 1;
		foreColor = 0xff000000;//검정색.
		strokeThick = 5.f;
		strokeAlpha = 0xff;

		backColor = 0xffffffff;
		backAlpha = 0xff;
	}
	
	public DrawingMode copy()
	{
		DrawingMode newDrawingMode = new DrawingMode();
		
		newDrawingMode.drawingMode = this.drawingMode;
		newDrawingMode.foreColor   = this.foreColor;
		newDrawingMode.strokeThick = this.strokeThick;
		newDrawingMode.strokeThickLevel = this.strokeThickLevel;
		newDrawingMode.strokeAlpha = this.backAlpha;
		newDrawingMode.backColor   = this.backColor;
		newDrawingMode.backAlpha   = this.backAlpha;
		
		if (this.assetIds != null && this.assetIds.length > 0)
		{
			newDrawingMode.assetIds = new String[this.assetIds.length];
			for (int i = 0; i < assetIds.length; i++)
				newDrawingMode.assetIds[i] = this.assetIds[i];
		}
		
		newDrawingMode.bitmapFilePath = this.bitmapFilePath; 
		
		return newDrawingMode;
	}
}
