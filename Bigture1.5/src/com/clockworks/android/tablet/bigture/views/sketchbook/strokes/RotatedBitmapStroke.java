package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;

public class RotatedBitmapStroke extends BitmapStroke{
	List<Float> angleList;

	public RotatedBitmapStroke(Context context)
	{
		super(context);

		angleList = new ArrayList<Float>();
	}
	
	public RotatedBitmapStroke(Context context, String resId, int color)
	{
		super(context, resId, color);
		
		angleList = new ArrayList<Float>();
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
		for (int i = 1; i < pointList.size(); i++)
		{
			crntPoint = pointList.get(i);
			
			m.reset();
			m.setRotate(angleList.get(i), xOffset, yOffset);
			m.postScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			canvas.drawBitmap(bitmap, m, paint);
		}
		
		if (!drawing)
		{
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	public void moveTo(float x, float y)
	{
		super.moveTo(x, y);
		angleList.add(0.f);
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

			float angle = (float)(Math.atan2(y - lastY, x - lastX) * 180.0 / 3.1415);
			angleList.add(angle);
			
			lastX = x;
			lastY = y;
		}
	}
	
	@Override
	public String toXML()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("<RotatedBitmapStroke assetId=\"" + assetId + "\" color=\"" + color + "\">");

		PointF pointLog = new PointF();
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

		sb.append("<AngleList>");
		for (Float angle : angleList)
			sb.append("<alpha value=\"" + angle + "\" />");
		sb.append("</AngleList>");

		sb.append("</RotatedBitmapStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		assetId = XmlUtil.getNodeAttribute(element, "assetId");
		color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		
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
			else if (elementName.equals("AngleList"))
			{
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					float value = Float.parseFloat(XmlUtil.getNodeAttribute(child, "value"));
					angleList.add(value);

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
		for (int i = 1; i < playIndex; i++)
		{
			crntPoint = pointList.get(i);
			
			m.reset();
			m.setRotate(angleList.get(i), xOffset, yOffset);
			m.postScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			canvas.drawBitmap(bitmap, m, paint);
		}
		
		if (!drawing)
		{
			bitmap.recycle();
			bitmap = null;
		}
	}
}
