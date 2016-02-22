package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import java.util.ArrayList;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class EraserStroke extends NormalStroke implements UserDrawing{
	int color = Color.BLACK;
	
	public EraserStroke()
	{
		super();

		paint.setColor(Color.TRANSPARENT);
		paint.setStyle(Paint.Style.STROKE);

		paint.setStrokeWidth(thick);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);

		paint.setAntiAlias(true);
		paint.setDither(true);
		
		pointList = new ArrayList<PointF>();
	}
	
	public EraserStroke(float thick)
	{
		this();
		this.thick = thick;
		paint.setStrokeWidth(thick);
	}

	@Override
	public void draw(Canvas canvas)
	{
		int size = pointList.size();
		
		if (size > 1)
		{
			paint.setColor(Color.TRANSPARENT);
			paint.setStyle(Paint.Style.STROKE);
			
			//if (!this.editMode)
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

			PointF prevPoint = pointList.get(0);
			PointF crntPoint;
			
			for (int i = 1; i < pointList.size(); i++)
			{
				crntPoint = pointList.get(i);
				
				canvas.drawLine(prevPoint.x, prevPoint.y, crntPoint.x, crntPoint.y, paint);
				prevPoint = crntPoint;
			}
			
			paint.setXfermode(null);
		}
		else if (size == 1)
		{
			paint.setColor(Color.TRANSPARENT);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);

			//if (!this.editMode)
				paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

			PointF point = pointList.get(0);
			canvas.drawCircle(point.x, point.y, thick/2.f, paint);

			paint.setXfermode(null);
		}
	}

	public void lineTo(float x, float y, Canvas canvas)
	{
		if(canvas == null) return;
		pointList.add(new PointF(x, y));

		PointF prevPoint = pointList.get(0);
		PointF crntPoint;
		
		paint.setColor(Color.TRANSPARENT);
		//paint.setAlpha(0xff);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

		for (int i = 1; i < pointList.size(); i++)
		{
			crntPoint = pointList.get(i);
			
			canvas.drawLine(prevPoint.x, prevPoint.y, crntPoint.x, crntPoint.y, paint);
			prevPoint = crntPoint;
		}
		paint.setXfermode(null);
	}

	@Override
	public String toXML()
	{
		PointF pointLog = new PointF();

		StringBuffer sb = new StringBuffer();
		sb.append("<EraserStroke thick=\"" + thick + "\">");
		
		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</EraserStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		thick = Float.parseFloat(XmlUtil.getNodeAttribute(element, "thick"));
		
		Element child = XmlUtil.getFirstChildElement(element);
		
		while (child != null)
		{
			float x = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "x")));
			float y = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "y")));
			
			pointList.add(new PointF(x, y));
			
			child = XmlUtil.getNextSiblingElement(child);
		}
		
		paint.setStrokeWidth(thick);
	}
}
