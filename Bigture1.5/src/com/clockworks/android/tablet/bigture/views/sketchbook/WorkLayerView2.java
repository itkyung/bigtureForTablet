package com.clockworks.android.tablet.bigture.views.sketchbook;




import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.background.BackgroundFill;
import com.clockworks.android.tablet.bigture.views.sketchbook.background.BackgroundImage;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.DrawingMode;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;
import com.clockworks.android.tablet.bigture.views.sketchbook.sticker.CustomSticker;
import com.clockworks.android.tablet.bigture.views.sketchbook.sticker.NormalSticker;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.AbsStrokes;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BackgroundShaderStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BackgroundStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BitmapFillStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.BitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.EraserStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.FillStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.LinearGradientStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.NeonStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.NormalStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RandomBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RotatedBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.RotatedRandomBitmapStroke;
import com.clockworks.android.tablet.bigture.views.sketchbook.strokes.ShaderStroke;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WorkLayerView2 extends SurfaceView implements OnClickListener, SurfaceHolder.Callback{
	Context context;

	UserDrawingManager drawingManager;
	private DrawingMode drawingMode;

	//boolean blockUserInterface = false;
	int selectedPalette;

	Bitmap bmHigh;
	Bitmap bmLow;
	
	Canvas canvasHigh;
	Canvas canvasLow;
	
	Matrix selectedObjectMatrix;
	
	ScaleGestureDetector scaleGestureDetector;
	float scaleFactor = 1.f;
	float lastTouchX;
	float lastTouchY;
	double lastAngle;

	int pointerId = -1;
	int pointerId2 = -1;

	public LinearLayout stickerToolbar;
	public ImageView deleteButton;

	// current active sticker
	NormalSticker activeSticker;

	// current active stroke
	AbsStrokes activeStroke = null;

	SurfaceHolder holder;
	int foreColor = 0xff000000;//검정색.

	public WorkLayerView2(Context context, AttributeSet attrs){
		super(context, attrs);
		this.context = context;
		
		try{
			drawingManager = UserDrawingManager.createInstance(context);
			scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
			
			holder = getHolder();
			holder.addCallback(this);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int format, int width, int height){
		bmHigh = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bmLow  = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		
		//float viewScale = (float)width / 1024.f;
		drawingManager.setCanvasSize(width, height);
		
		drawingMode = new DrawingMode();
		drawingMode.foreColor = foreColor;
		activeStroke = createStroke();
		
		canvasLow = new Canvas(bmLow);
		canvasLow.drawColor(Color.WHITE);

		// draw all objects on canvas low.
		drawingManager.draw(canvasLow, true);
		setNeedDraw(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0)
	{
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		if (bmHigh != null)
			bmHigh.recycle();
		if (bmLow != null)
			bmLow.recycle();
		
		bmHigh = null;
		bmLow = null;
		canvasLow = null;
		canvasHigh = null;
	}
	
	protected void drawObjects(Canvas canvas)
	{
		canvas.drawColor(Color.WHITE);
		
		// Low layer objects
		canvas.drawBitmap(bmLow, 0, 0, null);
		
		// Active sticker
		if (activeSticker != null)
			activeSticker.draw(canvas);
		
		// Active stroke
		if (activeStroke != null && drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_ERASE)
			activeStroke.draw(canvas);
		
		// High layer object
		if (canvasHigh != null)
			canvas.drawBitmap(bmHigh, 0, 0, null);

//		if (activeStroke != null && drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_ERASE)
//			activeStroke.draw(canvas);
//		else if (activeSticker != null)
//			activeSticker.draw(canvas);
//		
//		if (canvasHigh != null)
//			canvas.drawBitmap(bmHigh, 0, 0, null);
	}

	public void rebuildCanvas()
	{
		// draw all objects on canvas low.
		drawingManager.draw(canvasLow, true);
		setNeedDraw(true);	
	}
	
	public void setNeedDraw(boolean dirty)
	{
		Canvas c = holder.lockCanvas();
		if (c != null)
		{
			drawObjects(c);
			holder.unlockCanvasAndPost(c);
		}
	}
	
	public void undo()
	{
		if (drawingManager.canUndo())
		{
			if (activeSticker != null)
				applyActiveSticker();
			
			drawingManager.undo();

			// Erase background & rebuild bitmap with all objects include background
			canvasLow.drawColor(Color.WHITE);
			drawingManager.draw(canvasLow, true);
			
			if (canvasHigh != null)
				canvasHigh = null;
			
			setNeedDraw(true);
		}
	}

	public void redo()
	{
		if (drawingManager.canRedo())
		{
			if (activeSticker != null)
				applyActiveSticker();

			drawingManager.redo();
			
			// Erase background & rebuild bitmap with all objects include background
			canvasLow.drawColor(Color.WHITE);
			drawingManager.draw(canvasLow, true);				

			if (canvasHigh != null)
				canvasHigh = null;

			setNeedDraw(true);
		}
	}

	public void clear()
	{
		drawingManager.clear();
		
		if (activeSticker != null)
		{
			activeSticker.setSelected(false);
			activeSticker = null;
		}
	//	this.foreColor = 0xff000000;//검정색.
		stickerToolbar.setVisibility(View.GONE);
		deleteButton.setVisibility(View.GONE);

		// Clear the working bitmap
		canvasLow.drawColor(Color.WHITE);
		canvasHigh = null;
		
		setNeedDraw(true);
	}

	public void applyActiveSticker()
	{
		if (activeSticker == null)
			return;
		
		// reset selected state
		activeSticker.setSelected(false);
		stickerToolbar.setVisibility(View.GONE);
		deleteButton.setVisibility(View.GONE);
				
		if (drawingManager.isTopLevelObject(activeSticker))
		{
			activeSticker.draw(canvasLow);
		}
		else
		{
			canvasLow.drawColor(Color.WHITE);
			drawingManager.drawBackground(canvasLow);
			
			Bitmap bitmap = Bitmap.createBitmap(bmLow);
			Canvas canvas = new Canvas(bitmap);
			drawingManager.draw(canvas, false);
						
			canvasLow.drawBitmap(bitmap, 0, 0, null);
			
			bitmap.recycle();
			canvas = null;
		}
		
		if (canvasHigh != null)
			canvasHigh = null;
		
		// Is there a drawing object above activeSticker
//		if (drawingManager.hasEraserStrokeAbove(activeSticker))
//		{
//			canvasLow.drawColor(Color.WHITE);
//			drawingManager.draw(canvasLow, true);
//			
//			if (canvasHigh != null)
//				canvasHigh = null;
//		}
//		else
//		{
//			// Just draw the stick on the top of canvas
//			activeSticker.draw(canvasLow);
//			
//			if (canvasHigh != null)
//			{
//				canvasLow.drawBitmap(bmHigh, 0, 0, null);
//				canvasHigh = null;
//			}			
//		}

		activeSticker = null;
	}
	
	private void repositionStickerDeleteButton()
	{
		deleteButton.setVisibility(View.VISIBLE);

		RectF rectf = activeSticker.getBounds();
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)deleteButton.getLayoutParams();
		params.leftMargin = getLeft() + (int)rectf.right - 46;
		params.topMargin  = getTop() + (int)rectf.top;
		deleteButton.setLayoutParams(params);
	}
	
	public void setDrawingMode(DrawingMode drawingMode)
	{
		if (activeSticker != null)
			applyActiveSticker();

		if (this.drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ERASE)
		{
			canvasLow.drawColor(Color.WHITE);
			drawingManager.draw(canvasLow, true);
		
			canvasHigh = null;
		}
		
		this.drawingMode = drawingMode;

		if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_CUSTOM_STICKER){
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(drawingMode.bitmapFilePath, options);
			
			CustomSticker sticker = new CustomSticker(context, drawingMode.bitmapFilePath);

			drawingManager.add(sticker);
			
			FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)getLayoutParams();
			
			int viewWidth  = params.width;
			int viewHeight = params.height;
			
			if (viewWidth > 0 && viewHeight > 0)
				sticker.translate(viewWidth/2 - options.outWidth/2, viewHeight/2 - options.outHeight/2);

			activeStroke = null;
			activeSticker = sticker;
			activeSticker.setSelected(true);
			
			stickerToolbar.setVisibility(View.VISIBLE);
			
			repositionStickerDeleteButton();
			
			setNeedDraw(true);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_STICKER){
			String assetId = drawingMode.assetIds == null ? null : drawingMode.assetIds[0];
			activeStroke = null;
			
			if (assetId != null)
			{
				NormalSticker sticker = new NormalSticker(context, assetId);

				drawingManager.add(sticker);

				RectF bounds = sticker.getBounds();
				sticker.translate(getWidth()/2 - bounds.width()/2, getHeight()/2 - bounds.height()/2);

				activeSticker = sticker;
				activeSticker.setSelected(true);

				stickerToolbar.setVisibility(View.VISIBLE);

				repositionStickerDeleteButton();

				setNeedDraw(true);
			}
		}
		else if (this.drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ERASE)
		{
			// Draw only background objects on backgroundCanvas
			canvasLow.drawColor(Color.WHITE);
			drawingManager.drawBackground(canvasLow);
			
			// Draw only foreground objects on foregroundCanvas
			canvasHigh = new Canvas(bmHigh);
			canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
			drawingManager.drawForeground(canvasHigh);
			
			// Prepare new stroke
			activeStroke = createStroke();
		}
		else
		{
			activeStroke = createStroke();
		}
	}
	
	public void changeDrawingMode(int drawMode){
		drawingMode.drawingMode = drawMode;
	}
	
	public DrawingMode getDrawingMode()
	{
		return drawingMode.copy();
	}

	public void setStrokeColor(int color)
	{
		drawingMode.foreColor = color;
		this.foreColor = color;
		if (activeStroke != null)
			activeStroke.setColor(color);
	}

	public int getStrokeThickLevel(){
		return drawingMode.strokeThickLevel;
	}
	
	public void setStrokeThick(float strokeThick,int sizeLevel){
		drawingMode.strokeThick = strokeThick;
		drawingMode.strokeThickLevel = sizeLevel;
		if (activeStroke != null)
			activeStroke.setThick(strokeThick);
	}
	
	
	public void setBackgroundBitmap(int resId){
		BackgroundImage backgroundImage = new BackgroundImage(context, resId);
		drawingManager.add(backgroundImage);
		drawingManager.draw(canvasLow, true);

		setNeedDraw(true);
	}
	
	public void setBackgroundBitmap(String filePath){
		BackgroundImage backgroundImage = new BackgroundImage(context, filePath);
		drawingManager.add(backgroundImage);
		drawingManager.draw(canvasLow, true);

		setNeedDraw(true);
	}
	
	public void setBackgroundColor(int color)
	{
		BackgroundFill backgroundFill = new BackgroundFill(color);
		drawingManager.add(backgroundFill);
		drawingManager.draw(canvasLow, true);

		setNeedDraw(true);
	}

	public void saveThumbnail(String filePath){
		try{
			
			Bitmap bitmapThumb = ImageUtil.resizeBitmap(bmLow, 200, 150);
			ImageUtil.saveBitmap(bitmapThumb, filePath);
			
			bitmapThumb.recycle();
			bitmapThumb = null;
		}
		catch(OutOfMemoryError e)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Notice");
			builder.setMessage("Out of memory error");
			builder.setNegativeButton("OK", null);
			builder.create().show();
		}
	}
	
	public void saveImage(String filePath, boolean pngType)
	{
		try{
			
			if (pngType)
				ImageUtil.saveBitmap(bmLow, filePath);
			else
				ImageUtil.saveJPEGBitmap(bmLow, filePath);
		}
		catch(OutOfMemoryError e)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Notice");
			builder.setMessage("Out of memory error");
			builder.setNegativeButton("OK", null);
			builder.create().show();
		}
	}

	public boolean saveImageForUpload(String filePath)
	{
		boolean result = true;
		
		try
		{	
			
			ImageUtil.saveJPEGBitmap(bmLow, filePath);
		}
		catch(OutOfMemoryError e)
		{
			result = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Notice");
			builder.setMessage("Out of memory error");
			builder.setNegativeButton("OK", null);
			builder.create().show();
		}
		
		return result;
	}

//	@Override
//	public boolean dispatchTouchEvent(MotionEvent pEvent)
//	{
//		if (!blockUserInterface)
//			return super.dispatchTouchEvent(pEvent);
//
//		return blockUserInterface;
//	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (drawingMode.drawingMode < UserDrawingManager.DRAWING_MODE_ERASE)
		{
			return handleStroke(event);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ERASE)
		{
			return handleStroke2(event);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_STICKER ||
				drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_CUSTOM_STICKER)
		{
			if (activeSticker != null)
			{
				scaleGestureDetector.onTouchEvent(event);

				int action = event.getAction() & MotionEvent.ACTION_MASK;

				if (action == MotionEvent.ACTION_DOWN)
				{
					pointerId = event.getPointerId(0);
					
					lastTouchX = event.getX();
					lastTouchY = event.getY();
					
					if (activeSticker.hitTest(lastTouchX, lastTouchY))
					{
						selectedObjectMatrix = new Matrix();
						selectedObjectMatrix.set(activeSticker.getMatrix());

						deleteButton.setVisibility(View.GONE);
					}
					else
					{
						applyActiveSticker();					
						setNeedDraw(true);
					}
				}
				else if (action == MotionEvent.ACTION_MOVE)
				{
					final int pidx = event.findPointerIndex(pointerId);

					if (pidx >= 0)
					{
						final float x = event.getX(pidx);
						final float y = event.getY(pidx);
						
						if (!scaleGestureDetector.isInProgress() && activeSticker != null)
						{
							final float dx = x - lastTouchX;
							final float dy = y - lastTouchY;
							
							activeSticker.translate(dx, dy);
						}
		
						lastTouchX = x;
						lastTouchY = y;
						
						if (scaleGestureDetector.isInProgress())
						{
							int pidx2 = event.findPointerIndex(pointerId2);
							if (pidx2 >= 0)
							{
								float x2 = event.getX(pidx2);
								float y2 = event.getY(pidx2);
								
								double angle = Math.atan2(y-y2, x-x2) * 180.0 / Math.PI;

								activeSticker.postRotate((float)(angle-lastAngle), (x+x2)/2, (y+y2)/2);
								lastAngle = angle;
							}
						}
						
						setNeedDraw(true);

						return true;
					}
				}
				else if (action == MotionEvent.ACTION_POINTER_DOWN)
				{
					if (event.getPointerCount() == 2)
					{
						int actionIndex1  = event.findPointerIndex(pointerId);
						float x1 = event.getX(actionIndex1);
						float y1 = event.getY(actionIndex1);
						
						int actionIndex2  = event.getActionIndex();
						pointerId2 = event.getPointerId(actionIndex2);
						
						float x2 = event.getX(actionIndex2);
						float y2 = event.getY(actionIndex2);
						
						lastAngle = Math.atan2(y1-y2, x1-x2) * 180.0 / Math.PI;
						//lastAngle = lastAngle * 180.0 / Math.PI;
						
						Log.d("Touch", "Second finger down " + lastAngle);
					}
				}
				else if (action == MotionEvent.ACTION_POINTER_UP)
				{
					int actionIndex = event.getActionIndex();
					
					if (event.getPointerId(actionIndex) == pointerId2)
					{
						pointerId2 = -1;
						Log.d("Touch", "Second finger up");
					}
				}
				else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL)
				{
					pointerId = -1;
					
					repositionStickerDeleteButton();

					Matrix matrix = new Matrix();
					matrix.set(activeSticker.getMatrix());

					if (!selectedObjectMatrix.equals(matrix))
						drawingManager.changeMatrix(activeSticker, selectedObjectMatrix, matrix);

					setNeedDraw(true);
				}
			}
			/**
			 * There is no active sticker
			 */
			else
			{
				lastTouchX = event.getX();
				lastTouchY = event.getY();

				activeSticker = drawingManager.findSticker(lastTouchX, lastTouchY);

				if (activeSticker != null)
				{
					selectedObjectMatrix = new Matrix();
					selectedObjectMatrix.set(activeSticker.getMatrix());
					
					// Activate the sticker found.
					activeSticker.setSelected(true);
					stickerToolbar.setVisibility(View.VISIBLE);

					if (!drawingManager.isTopLevelObject(activeSticker))
					{
						// 배경을 그린다.
						canvasLow.drawColor(Color.WHITE);
						drawingManager.drawBackground(canvasLow);
						
						// draw objects below the activeSticker except background. 
						// 배경과 함께 바로 그리면, eraser 객체 때문에 제대로 그려지지 않는다.
						Bitmap bitmap = Bitmap.createBitmap(bmLow);
						Canvas canvas = new Canvas(bitmap);
						canvas.drawColor(0, PorterDuff.Mode.CLEAR);
						drawingManager.drawBelow(canvas, activeSticker, false);
						
						canvasLow.drawBitmap(bitmap, 0, 0, null);
						
						// release memory
						bitmap.recycle();
						canvas = null;
						
						// draw objects above the activeSticker.
						canvasHigh = new Canvas(bmHigh);
						canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
						drawingManager.drawAbove(canvasHigh, activeSticker);

//						canvasLow.drawColor(Color.WHITE);
//						drawingManager.drawBelow(canvasLow, activeSticker);
//
//						canvasHigh = new Canvas(bmHigh);
//						canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
//						drawingManager.drawAbove(canvasHigh, activeSticker);
					}
					else
					{						
						canvasLow.drawColor(Color.WHITE);
						drawingManager.draw(canvasLow, true);
					}

					setNeedDraw(true);
				}
			}
		}

		return true;
		//return super.onTouchEvent(event);
	}

	private boolean handleStroke(MotionEvent event)
	{
		int action = event.getAction() & MotionEvent.ACTION_MASK;

		if (action == MotionEvent.ACTION_DOWN)
		{
			pointerId = event.getPointerId(0);

			if (pointerId >= 0)
			{
				activeStroke.moveTo(event.getX(), event.getY());
				activeStroke.setEditMode(true);
				
				if (activeStroke.isBackgroundObject())
				{
					canvasLow.drawColor(Color.WHITE);
					drawingManager.drawBackground(canvasLow);
					
					canvasHigh = new Canvas(bmHigh);
					canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
					drawingManager.draw(canvasHigh, false);
				}
	
				return true;
			}
		}
		else if (action == MotionEvent.ACTION_MOVE)
		{
			int pidx = event.findPointerIndex(pointerId);

			if (pidx >= 0)
			{
				if (drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH &&
						drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_BITMAP_RANDOM_BRUSH &&
						drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_ROTATED_BITMAP_BRUSH &&
						drawingMode.drawingMode != UserDrawingManager.DRAWING_MODE_ROTATED_RANDOM_BITMAP_BRUSH)
				{
					for (int i = 0; i < event.getHistorySize(); i++)
						activeStroke.quadTo(event.getHistoricalX(pidx, i), event.getHistoricalY(pidx, i));
				}

				float x = event.getX(pidx);
				float y = event.getY(pidx);
	
				activeStroke.quadTo(x, y);
				setNeedDraw(true);

				return true;
			}
		}
		else if (action == MotionEvent.ACTION_UP)
		{
			int pidx = event.findPointerIndex(pointerId);

			if (pidx >= 0)
				activeStroke.lineTo(event.getX(pidx), event.getY(pidx));
			
			activeStroke.close();
			activeStroke.setEditMode(false);
	
			drawingManager.add(activeStroke);
			
			if (activeStroke.isBackgroundObject())
			{
				activeStroke.draw(canvasLow);				
				canvasLow.drawBitmap(bmHigh, 0, 0, null);
				canvasHigh = null;
			}
			else
			{
				activeStroke.draw(canvasLow);					
			}

			pointerId = -1;

			// Prepare new stroke
			activeStroke = createStroke();
			setNeedDraw(true);

			return true;
		}
		else if (action == MotionEvent.ACTION_CANCEL)
		{
			pointerId = -1;
			
			setNeedDraw(true);

			// Prepare new stroke
			activeStroke = createStroke();
		}

		return super.onTouchEvent(event);
	}

	private boolean handleStroke2(MotionEvent event)
	{
		int action = event.getAction() & MotionEvent.ACTION_MASK;

		if (action == MotionEvent.ACTION_DOWN)
		{
			pointerId = event.getPointerId(0);

			if (pointerId >= 0)
			{
				activeStroke.moveTo(event.getX(), event.getY());				
				return true;
			}
		}
		else if (action == MotionEvent.ACTION_MOVE)
		{
			EraserStroke eraserStroke = (EraserStroke)activeStroke;
			
			int pidx = event.findPointerIndex(pointerId);

			if (pidx >= 0)
			{
				float x = event.getX(pidx);
				float y = event.getY(pidx);
	
				eraserStroke.lineTo(x, y, canvasHigh);

				setNeedDraw(true);
				return true;
			}
		}
		else if (action == MotionEvent.ACTION_UP)
		{
			int pidx = event.findPointerIndex(pointerId);

			if (pidx >= 0)
			{
				float x = event.getX(pidx);
				float y = event.getY(pidx);
	
				activeStroke.lineTo(x, y);
			}
			activeStroke.close();

			drawingManager.add(activeStroke);

			setNeedDraw(true);

			// Prepare new stroke
			activeStroke = createStroke();

			pointerId = -1;

			return true;
		}
		else if (action == MotionEvent.ACTION_CANCEL)
		{
			pointerId = -1;

			setNeedDraw(true);

			// Prepare new stroke
			activeStroke = createStroke();
		}

		return super.onTouchEvent(event);
	}

	class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		float lastFocusX;
		float lastFocusY;
		
		double angle;
		
		@Override
		public boolean onScale(ScaleGestureDetector detector)
		{
			if (activeSticker != null)
			{
				float focusX = detector.getFocusX();
				float focusY = detector.getFocusY();
				
				activeSticker.translate(focusX - lastFocusX, focusY - lastFocusY);
				activeSticker.postScale(detector.getScaleFactor(), focusX, focusY);
				
				setNeedDraw(true);
				
				lastFocusX = focusX;
				lastFocusY = focusY;

				return true;
			}
			
			return super.onScale(detector);
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector)
		{
			lastFocusX = detector.getFocusX();
			lastFocusY = detector.getFocusY();
			
			return super.onScaleBegin(detector);
		}
	}
	
	private AbsStrokes createStroke()
	{
		AbsStrokes stroke = null;

		if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ERASE)
			stroke = new EraserStroke(drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_CRAYON)
			stroke = new ShaderStroke(context, "crayon1", drawingMode.foreColor, drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_BACKGROUND_CRAYON)
			stroke = new BackgroundShaderStroke(context, "crayon1", drawingMode.foreColor, drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_NEON)
			stroke = new NeonStroke(drawingMode.foreColor, drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_RAINBOW)
		{
			stroke = new LinearGradientStroke(drawingMode.foreColor, drawingMode.strokeAlpha, drawingMode.strokeThick);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_BITMAP_BRUSH)
		{
			int color = drawingMode.foreColor;
			stroke = new BitmapStroke(context, drawingMode.assetIds[0], color);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_BITMAP_RANDOM_BRUSH)
		{
			int color = drawingMode.foreColor;
			stroke = new RandomBitmapStroke(context, drawingMode.assetIds, color);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ROTATED_BITMAP_BRUSH)
		{
			int color = drawingMode.foreColor;
			stroke = new RotatedBitmapStroke(context, drawingMode.assetIds[0], color);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_ROTATED_RANDOM_BITMAP_BRUSH)
		{
			int color = drawingMode.foreColor;
			stroke = new RotatedRandomBitmapStroke(context, drawingMode.assetIds, color);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_FILL_PATTERN)
		{
			int color = drawingMode.foreColor;
			float thick = drawingMode.strokeThick;
			
			stroke = new BitmapFillStroke(context, drawingMode.assetIds[0], color, thick);
		}
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_FILL_SOLID)
			stroke = new FillStroke(drawingMode.foreColor, drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_BACKGROUND_PEN)
			stroke = new BackgroundStroke(drawingMode.foreColor, drawingMode.strokeAlpha, drawingMode.strokeThick);
		else if (drawingMode.drawingMode == UserDrawingManager.DRAWING_MODE_BACKGROUND_WBRUSH)
			stroke = new BackgroundStroke(drawingMode.foreColor, drawingMode.strokeAlpha, drawingMode.strokeThick);
		else
			stroke = new NormalStroke(drawingMode.foreColor, drawingMode.strokeAlpha, drawingMode.strokeThick);

		return stroke;
	}

	@Override
	public void onClick(View view){
		if (view.getId() == R.id.imageLayerUp){
			if (drawingManager.isTopLevelObject(activeSticker) == false)
			{
				drawingManager.layerUp(activeSticker);
				
				canvasLow.drawColor(Color.WHITE);
				drawingManager.drawBackground(canvasLow);
				
				Bitmap bitmap = Bitmap.createBitmap(bmLow);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawColor(0, PorterDuff.Mode.CLEAR);
				drawingManager.drawBelow(canvas, activeSticker, false);
				
				canvasLow.drawBitmap(bitmap, 0, 0, null);
				
				bitmap.recycle();
				canvas = null;
				
				if (canvasHigh == null)
					canvasHigh = new Canvas(bmHigh);
				
				canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
				drawingManager.drawAbove(canvasHigh, activeSticker);
				
				setNeedDraw(true);
			}
		}else if (view.getId() == R.id.imageLayerDown){
			drawingManager.layerDown(activeSticker);

			canvasLow.drawColor(Color.WHITE);
			drawingManager.drawBackground(canvasLow);
			
			Bitmap bitmap = Bitmap.createBitmap(bmLow);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawColor(0, PorterDuff.Mode.CLEAR);
			drawingManager.drawBelow(canvas, activeSticker, false);
			
			canvasLow.drawBitmap(bitmap, 0, 0, null);
			
			if (canvasHigh == null)
				canvasHigh = new Canvas(bmHigh);
			
			canvasHigh.drawColor(0, PorterDuff.Mode.CLEAR);
			drawingManager.drawAbove(canvasHigh, activeSticker);
			
			setNeedDraw(true);
		}
		else if (view.getId() == R.id.imageFlipHorz)
		{
			activeSticker.flipHorz();
			setNeedDraw(true);
		}
		else if (view.getId() == R.id.imageFlipVert)
		{
			activeSticker.flipVert();
			setNeedDraw(true);
		}
		else if (view.getId() == R.id.imageDelete)
		{
			boolean redrawAll = !drawingManager.isTopLevelObject(activeSticker);
			
			drawingManager.deleteObject(activeSticker);
			
			activeSticker.setSelected(false);
			activeSticker = null;
			stickerToolbar.setVisibility(View.GONE);
			deleteButton.setVisibility(View.GONE);
			
			if (redrawAll)
			{
				canvasLow.drawColor(Color.WHITE);
				drawingManager.draw(canvasLow, true);
			}
			
			if (canvasHigh != null)
				canvasHigh = null;
			
			setNeedDraw(true);
		}
	}
}
