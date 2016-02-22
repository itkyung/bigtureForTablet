package com.clockworks.android.tablet.bigture.views.sketchbook.sticker;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.BigtureAsset;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;




import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class NormalSticker implements UserDrawing{
	protected Context context;
	private BigtureAsset asset;
	
	protected Paint paint;
	protected Matrix matrix = new Matrix();
	
	protected int width;
	protected int height;
	
	protected int halfWidth;
	protected int halfHeight;
	
	protected boolean selected;
	protected boolean editMode;
	
	protected Bitmap bitmap;
	
	protected float values[] = new float[9];
	
	public NormalSticker(Context context)
	{
		this.context = context;
		selected = false;
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
	}
	
	public NormalSticker(Context context, String assetId){
		UserDrawingManager man = UserDrawingManager.getInstance();
		
		this.context = context;
		this.asset = man.getAsset(assetId);
		
		width  = (int)man.toDeviceUnit(asset.baseWidth);
		height = (int)man.toDeviceUnit(asset.baseHeight);

		halfWidth  = width >> 1;
		halfHeight = height >> 1;

		float scale = man.toDeviceUnit(asset.initialScale);
		matrix.postScale(scale, scale, 0, 0);

		selected = false;
		
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
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
		return false;
	}

	public Matrix getMatrix()
	{
		return matrix;
	}
	
	public void setMatrix(Matrix matrix)
	{
		this.matrix = new Matrix();
		this.matrix.set(matrix);
	}
	
	public void setCenter(float x, float y)
	{
		matrix.setTranslate(x - halfWidth, y - halfHeight);
	}
	
	public float getScale()
	{
		matrix.getValues(values);
		
		return values[Matrix.MSCALE_X];
	}
	
	public RectF getRect()
	{
		RectF rect = new RectF(0,0,width,height);
		matrix.mapRect(rect);
		
		return rect;
	}
	
	public PointF getCenterTop()
	{
		float[] pts = new float[] {halfWidth, 0};
		matrix.mapPoints(pts);
		
		return new PointF(pts[0], pts[1]);
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
		
		if (selected)
		{
			bitmap = loadBitmap();
		}
		else
		{
			if (bitmap != null)
			{
				bitmap.recycle();
				bitmap = null;
			}
		}
	}
	
	public boolean getSelected()
	{
		return this.selected;
	}
	
	public void translate(float dx, float dy)
	{
		matrix.postTranslate(dx, dy);
	}
	
	public void postScale(float scale, float pivotX, float pivotY)
	{
		matrix.postScale(scale, scale, pivotX, pivotY);
	}
	
	public void postRotate(float angle, float pivotX, float pivotY)
	{
		matrix.postRotate(angle, pivotX, pivotY);
	}
	
	public Bitmap loadBitmap()
	{
		Bitmap bitmap = ImageUtil.loadFromAsset(context, asset.assetFileName);
		return bitmap;
	}
	
	public void flipHorz()
	{
		matrix.preScale(-1.f, 1.f, halfWidth, halfHeight);
	}
	
	public void flipVert()
	{
		matrix.preScale(1.f, -1.f, halfWidth, halfHeight);
	}
	
	public boolean hitTest(float x, float y)
	{
		RectF rect = new RectF(0, 0, width, height);
		matrix.mapRect(rect);
		
		return rect.contains(x, y);
	}
	
	public RectF getBounds()
	{
		RectF rectf = new RectF(0,0,width,height);
		matrix.mapRect(rectf);

		return rectf;
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (this.bitmap == null)
			this.bitmap = loadBitmap();
		
		if(canvas == null){
			return;
		}
		canvas.drawBitmap(bitmap, matrix, paint);
		
		if (selected)
		{
			paint.setARGB(255, 128, 128, 128);
			paint.setStyle(Style.STROKE);
			paint.setPathEffect(new DashPathEffect(new float[] {2,2}, 0));
			
			RectF rectf = new RectF(0,0,width,height);
			matrix.mapRect(rectf);
			Rect rect = new Rect((int)rectf.left, (int)rectf.top, (int) rectf.right, (int)rectf.bottom);
			canvas.drawRect(rect, paint);
		}
		
		if (!selected)
		{
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	public String toXML()
	{
		float[] values = new float[9];
		matrix.getValues(values);
		
		UserDrawingManager man = UserDrawingManager.getInstance();
		values[Matrix.MSCALE_X] = man.toLogicalUnit(values[Matrix.MSCALE_X]);
		values[Matrix.MSCALE_Y] = man.toLogicalUnit(values[Matrix.MSCALE_Y]);
		values[Matrix.MTRANS_X] = man.toLogicalUnit(values[Matrix.MTRANS_X]);
		values[Matrix.MTRANS_Y] = man.toLogicalUnit(values[Matrix.MTRANS_Y]);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<NormalSticker assetId=\"" + asset.assetId + "\">");
		
		for (int i = 0; i < 9; i++)
			sb.append("<matrix value=\"" + values[i] + "\" />");
		
		sb.append("</NormalSticker>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element){
		float[] values = new float[9];
		
		String assetId = XmlUtil.getNodeAttribute(element, "assetId");
		this.asset = UserDrawingManager.getInstance().getAsset(assetId);
		
		Element child = XmlUtil.getFirstChildElement(element);
		
		int i = 0;
		while (child != null)
		{
			values[i++] = Float.parseFloat(XmlUtil.getNodeAttribute(child, "value"));
			child = XmlUtil.getNextSiblingElement(child);
		}

		UserDrawingManager man = UserDrawingManager.getInstance();
		values[Matrix.MSCALE_X] = man.toDeviceUnit(values[Matrix.MSCALE_X]);
		values[Matrix.MSCALE_Y] = man.toDeviceUnit(values[Matrix.MSCALE_Y]);
		values[Matrix.MTRANS_X] = man.toDeviceUnit(values[Matrix.MTRANS_X]);
		values[Matrix.MTRANS_Y] = man.toDeviceUnit(values[Matrix.MTRANS_Y]);

		matrix = new Matrix();
		matrix.setValues(values);

		width  = (int)man.toDeviceUnit(asset.baseWidth);
		height = (int)man.toDeviceUnit(asset.baseHeight);

		halfWidth  = width >> 1;
		halfHeight = height >> 1;

		selected = false;
	}

	@Override
	public void readyForPlay()
	{
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
