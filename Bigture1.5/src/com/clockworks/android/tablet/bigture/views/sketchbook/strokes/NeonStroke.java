package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;




import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;

public class NeonStroke extends NormalStroke
{
	public NeonStroke()
	{
		super();
	}
	
	public NeonStroke(int color, float thick)
	{
		super(color, 255, thick);
	}

	@Override
	public void draw(Canvas canvas)
	{
		paint.setColor(color);
		float radius = drawingManager.toDeviceUnit(20.f);
		BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
		paint.setMaskFilter(filter);
		canvas.drawPath(path, paint);

		paint.setMaskFilter(null);
		paint.setColor(Color.WHITE);
		paint.setAlpha(255);
		
		canvas.drawPath(path, paint);
	}

	@Override
	public String toXML()
	{
		PointF pointLog = new PointF();
		StringBuffer sb = new StringBuffer();
		sb.append("<NeonStroke color=\"" + color + "\" thick=\"" + thick + "\">");

		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</NeonStroke>");
		
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

		paint.setColor(color);
		paint.setStrokeWidth(thick);
	}
}
