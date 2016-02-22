package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;

public class BitmapStroke extends AbsStrokes{
	Context context;
	UserDrawingManager drawingManager = UserDrawingManager.getInstance();
	
	int color;
	String assetId;
	
	Paint paint;
	List<PointF> pointList;
	List<Float> scaleList;
	List<Float> alphaList;
	
	Bitmap bitmap;
	
	static float minDistance = 40.f;
	float lastX, lastY;
	
	float randFactor = drawingManager.toDeviceUnit(30.f);
	boolean drawing = false;

	public BitmapStroke(Context context)
	{
		randFactor = drawingManager.toDeviceUnit(30.f);

		this.context = context;
		this.color = Color.BLACK;
		this.thick = 100;
		
		paint = new Paint();
		pointList = new ArrayList<PointF>();
		alphaList = new ArrayList<Float>();
		scaleList = new ArrayList<Float>();
		
		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}
	
	public BitmapStroke(Context context, String assetId, int color)
	{
		this(context);

		this.color = color;
		this.thick = 100;
		this.assetId = assetId;

		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}

	@Override
	public void setThick(float thick)
	{
	}

	@Override
	public void setColor(int color)
	{
		this.color = color;
		this.paint.setColor(color);
		
		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}

	@Override
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
		this.paint.setAlpha(alpha);
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (bitmap == null)
			bitmap = UserDrawingManager.getInstance().getAsset(assetId).loadBitmap(context, drawingManager.getViewScale());
		
		int xOffset = bitmap.getWidth() / 2;
		int yOffset = bitmap.getHeight() / 2;
		
		PointF crntPoint;
		
		Matrix m = new Matrix();
		for (int i = 0; i < pointList.size(); i++)
		{
			crntPoint = pointList.get(i);
			
			m.reset();
			m.setScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			
			canvas.drawBitmap(bitmap, m, paint);
		}
		
		if (!drawing)
		{
			bitmap.recycle();
			bitmap = null;
			Log.d("BitmapStroke", "bitmap released");
		}
	}

	@Override
	public void moveTo(float x, float y)
	{
		drawing = true;
		lastX = x;
		lastY = y;
		pointList.add(new PointF(x, y));
		alphaList.add(Float.valueOf(255.f));
		scaleList.add(Float.valueOf(1.f));
	}

	@Override
	public void lineTo(float x, float y)
	{
		if (Math.sqrt((x-lastX)*(x-lastX) + (y-lastY) * (y-lastY)) > minDistance)
		{
			Random rand = new Random();
			x = x + randFactor * (rand.nextFloat() * 2.f - 1.f);
			y = y + randFactor * (rand.nextFloat() * 2.f - 1.f);
			
			alphaList.add(255.f * rand.nextFloat());
			scaleList.add(0.85f + rand.nextFloat() / 2.f);
			
			pointList.add(new PointF(x, y));

			lastX = x;
			lastY = y;
		}
	}

	@Override
	public void quadTo(float x, float y)
	{
		lineTo(x, y);
	}

	@Override
	public void close()
	{
		drawing = false;
	}

	@Override
	public String toXML()
	{
		UserDrawingManager drawingManager = UserDrawingManager.getInstance();
		
		PointF pointLog = new PointF();
		StringBuffer sb = new StringBuffer();
		
		sb.append("<BitmapStroke assetId=\"" + assetId + "\" color=\"" + color + "\">");

		sb.append("<PointList>");
		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		sb.append("</PointList>");

		sb.append("<ScaleList>");
		for (Float scale : scaleList)
			sb.append("<scale value=\"" + scale + "\" />");
		sb.append("</ScaleList>");

		sb.append("<AlphaList>");
		for (Float alpha : alphaList)
			sb.append("<alpha value=\"" + alpha + "\" />");
		sb.append("</AlphaList>");

		sb.append("</BitmapStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		UserDrawingManager drawingManager = UserDrawingManager.getInstance();

		assetId = XmlUtil.getNodeAttribute(element, "assetId");
		color   = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		
		element = XmlUtil.getFirstChildElement(element);
		
		while (element != null)
		{
			String elementName = XmlUtil.getElementName(element);
			
			if (elementName.equals("PointList"))
			{
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					float x = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "x")));
					float y = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "y")));
					
					pointList.add(new PointF(x, y));
					
					child = XmlUtil.getNextSiblingElement(child);
				}
			}
			else if (elementName.equals("ScaleList"))
			{
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					float value = Float.parseFloat(XmlUtil.getNodeAttribute(child, "value"));
					scaleList.add(value);
					
					child = XmlUtil.getNextSiblingElement(child);
				}
				
			}
			else if (elementName.equals("AlphaList"))
			{
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					float value = Float.parseFloat(XmlUtil.getNodeAttribute(child, "value"));
					alphaList.add(value);

					child = XmlUtil.getNextSiblingElement(child);
				}				
			}
			
			element = XmlUtil.getNextSiblingElement(element);
		}

		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}
	
	int playIndex = 0;

	@Override
	public void readyForPlay()
	{
		playIndex = 0;
		drawing = true;
	}

	@Override
	public boolean updateForPlay()
	{
		playIndex++;
		
		if (playIndex >= pointList.size())
		{
			drawing = false;
			return true;
		}
		
		return false;
	}

	@Override
	public void drawForPlay(Canvas canvas)
	{
		if (bitmap == null)
			bitmap = UserDrawingManager.getInstance().getAsset(assetId).loadBitmap(context, drawingManager.getViewScale());
		
		int xOffset = bitmap.getWidth() / 2;
		int yOffset = bitmap.getHeight() / 2;
		
		PointF crntPoint;
		
		Matrix m = new Matrix();
		for (int i = 0; i < playIndex; i++)
		{
			crntPoint = pointList.get(i);
			
			m.reset();
			m.setScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			
			canvas.drawBitmap(bitmap, m, paint);
		}
		
		if (!drawing)
		{
			bitmap.recycle();
			bitmap = null;
			Log.d("BitmapStroke", "bitmap released");
		}
	}
}
