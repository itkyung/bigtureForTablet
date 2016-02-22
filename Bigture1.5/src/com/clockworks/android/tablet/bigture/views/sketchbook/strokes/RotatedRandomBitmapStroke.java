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
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.PointF;

public class RotatedRandomBitmapStroke extends RandomBitmapStroke{
	List<Float> angleList;

	public RotatedRandomBitmapStroke(Context context)
	{
		super(context);
		angleList = new ArrayList<Float>();
	}
	
	public RotatedRandomBitmapStroke(Context context, String[] assetIds, int color)
	{
		super(context, assetIds, color);
		angleList = new ArrayList<Float>();
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (bitmaps == null)
		{
			bitmaps = new Bitmap[assetIds.length];
			for (int i = 0; i < assetIds.length; i++)
				bitmaps[i] = UserDrawingManager.getInstance().getAsset(assetIds[i]).loadBitmap(context, drawingManager.getViewScale());
		}

		PointF crntPoint;
		
		Matrix m = new Matrix();
		for (int i = 1; i < pointList.size(); i++)
		{
			int imageIndex = imageIdxList.get(i);
			Bitmap bitmap = bitmaps[imageIndex];
			
			int xOffset = bitmap.getWidth() / 2;
			int yOffset = bitmap.getHeight() / 2;

			crntPoint = pointList.get(i);
			
			m.reset();
			m.setRotate(angleList.get(i), xOffset, yOffset);
			m.postScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			canvas.drawBitmap(bitmap, m, paint);
		}

		if (!editing)
		{
			for (int i = 0; i < bitmaps.length; i++)
			{
				bitmaps[i].recycle();
				bitmaps[i] = null;
			}
			
			bitmaps = null;
		}
	}

	@Override
	public void moveTo(float x, float y)
	{
		super.moveTo(x, y);

		if (bitmaps == null)
		{
			bitmaps = new Bitmap[assetIds.length];
			for (int i = 0; i < assetIds.length; i++)
				bitmaps[i] = UserDrawingManager.getInstance().getAsset(assetIds[i]).loadBitmap(context, drawingManager.getViewScale());
		}

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

			int idx = random.nextInt(bitmaps.length);
			imageIdxList.add(idx);
			
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
		
		sb.append("<RotatedRandomBitmapStroke color=\"" + color + "\">");

		sb.append("<AssetList count=\"" + assetIds.length + "\">");
		for (int i = 0; i < assetIds.length; i++)
			sb.append("<assetId value=\"" + assetIds[i] + "\" />");
		sb.append("</AssetList>");

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

		sb.append("<ImageIdList>");
		for (Integer id : imageIdxList)
			sb.append("<imageId value=\"" + id + "\" />");
		sb.append("</ImageIdList>");

		sb.append("<AngleList>");
		for (Float angle : angleList)
			sb.append("<alpha value=\"" + angle + "\" />");
		sb.append("</AngleList>");

		sb.append("</RotatedRandomBitmapStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		
		element = XmlUtil.getFirstChildElement(element);
		
		while (element != null)
		{
			String elementName = XmlUtil.getElementName(element);
			
			if (elementName.equals("AssetList"))
			{
				int count = Integer.parseInt(XmlUtil.getNodeAttribute(element, "count"));
				assetIds = new String[count];
				
				int i = 0;
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					String value = XmlUtil.getNodeAttribute(child, "value");
					assetIds[i++] = value;
					
					child = XmlUtil.getNextSiblingElement(child);
				}
			}
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
			else if (elementName.equals("ImageIdList"))
			{
				Element child = XmlUtil.getFirstChildElement(element);
				
				while (child != null)
				{
					int value = Integer.parseInt(XmlUtil.getNodeAttribute(child, "value"));
					imageIdxList.add(value);

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

		// �÷� ���͸� ���
		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}

	int playIndex = 0;

	@Override
	public void readyForPlay()
	{
		playIndex = 0;
		editing = true;
	}

	@Override
	public boolean updateForPlay()
	{
		playIndex++;
		
		if (playIndex >= pointList.size())
		{
			editing = false;
			return true;
		}
		
		return false;
	}

	@Override
	public void drawForPlay(Canvas canvas)
	{
		if (bitmaps == null)
		{
			bitmaps = new Bitmap[assetIds.length];
			for (int i = 0; i < assetIds.length; i++)
				bitmaps[i] = UserDrawingManager.getInstance().getAsset(assetIds[i]).loadBitmap(context, drawingManager.getViewScale());
		}

		PointF crntPoint;
		
		Matrix m = new Matrix();
		for (int i = 1; i < playIndex; i++)
		{
			int imageIndex = imageIdxList.get(i);
			Bitmap bitmap = bitmaps[imageIndex];
			
			int xOffset = bitmap.getWidth() / 2;
			int yOffset = bitmap.getHeight() / 2;

			crntPoint = pointList.get(i);
			
			m.reset();
			m.setRotate(angleList.get(i), xOffset, yOffset);
			m.postScale(scaleList.get(i), scaleList.get(i));
			m.postTranslate(crntPoint.x - xOffset, crntPoint.y - yOffset);
			
			paint.setAlpha((int)alphaList.get(i).floatValue());
			canvas.drawBitmap(bitmap, m, paint);
		}

		if (!editing)
		{
			for (int i = 0; i < bitmaps.length; i++)
			{
				bitmaps[i].recycle();
				bitmaps[i] = null;
			}
			
			bitmaps = null;
		}
	}
}
