package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;



import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class FillStroke extends NormalStroke
{
	public FillStroke()
	{
		super();
		
		paint.setStyle(Paint.Style.FILL);	
	}
	
	public FillStroke(int color, float thick)
	{
		this();
		
		this.color = color;
		this.thick = thick;
		
		float strokeWidth = drawingManager.toDeviceUnit(thick);

		paint.setColor(color);
		paint.setStrokeWidth(strokeWidth);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		paint.setStyle((editMode) ? Paint.Style.STROKE : Paint.Style.FILL);
		canvas.drawPath(path, paint);
	}

	@Override
	public void moveTo(float x, float y)
	{
		super.moveTo(x, y);
		editMode = true;
	}

	@Override
	public void lineTo(float x, float y)
	{
		super.lineTo(x, y);
	}

	@Override
	public void close()
	{
		path.close();
		editMode = false;
	}

	@Override
	public String toXML()
	{
		PointF pointLog = new PointF();
		StringBuffer sb = new StringBuffer();
		
		sb.append("<FillStroke color=\"" + color + "\" thick=\"" + thick + "\" >");

		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</FillStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		thick = Float.parseFloat(XmlUtil.getNodeAttribute(element, "thick"));

		Element child = XmlUtil.getFirstChildElement(element);
		
		while (child != null)
		{
			float x = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "x")));
			float y = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "y")));
			
			pointList.add(new PointF(x, y));
			
			child = XmlUtil.getNextSiblingElement(child);
		}
		
		// Build Path
		PointF point1 = pointList.get(0);
		path.moveTo(point1.x, point1.y);
		
		for (PointF point : pointList)
		{
			path.lineTo(point.x, point.y);
		}
		
		path.close();

		paint.setColor(color);
		paint.setStrokeWidth(drawingManager.toDeviceUnit(thick));
	}
}
