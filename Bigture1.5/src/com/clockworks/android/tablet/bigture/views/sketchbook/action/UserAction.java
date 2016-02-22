package com.clockworks.android.tablet.bigture.views.sketchbook.action;



import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;
import com.clockworks.android.tablet.bigture.views.sketchbook.sticker.NormalSticker;

import android.graphics.Matrix;

public class UserAction{
	public static int ACTION_REGISTER = 1; 
	public static int ACTION_DELETE = 2; 
	public static int ACTION_MATRICS  = 12; 
	public static int ACTION_LAYER = 13; 
	
	public int action;
	//WeakReference<UserDrawing> refDrawing;
	UserDrawing userDrawing;

	public Matrix orgMatrix;
	public Matrix newMatrix;
	
	public int orgLayerPosition;
	public int newLayerPosition;
	
	public UserAction(int action, UserDrawing drawing){
		this.action = action;
		this.userDrawing = drawing;
		//refDrawing = new WeakReference<UserDrawing>(drawing);
		
		if (drawing instanceof NormalSticker){
			Matrix m = ((NormalSticker)drawing).getMatrix();
			orgMatrix = new Matrix();
			orgMatrix.set(m);
		}
	}

	public UserAction(UserDrawing drawing, Matrix orgMatrix, Matrix newMatrix)
	{
		this.action = ACTION_MATRICS;
		this.userDrawing = drawing;
		//refDrawing = new WeakReference<UserDrawing>(drawing);
		
		this.orgMatrix = new Matrix();
		this.orgMatrix.set(orgMatrix);

		this.newMatrix = new Matrix();
		this.newMatrix.set(newMatrix);
	}

	public UserAction(UserDrawing drawing, int orgLayerPosition, int newLayerPosition)
	{
		this.action = ACTION_LAYER;
		this.userDrawing = drawing;
		//refDrawing = new WeakReference<UserDrawing>(drawing);
		
		this.orgLayerPosition = orgLayerPosition;
		this.newLayerPosition = newLayerPosition;
	}
	
	public UserDrawing getUserDrawing()
	{
		return this.userDrawing;
		//return refDrawing.get();
	}
	
//	public void undo()
//	{
//		if (action == ACTION_REGISTER)
//		{
//			
//		}
//		else if (action == ACTION_MATRICS)
//		{
//			
//		}
//		else if (action == ACTION_LAYER)
//		{
//			
//		}
//	}
//	
//	public void redo()
//	{
//		
//	}
}
