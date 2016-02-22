package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import java.util.ArrayList;
import java.util.List;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;




import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class NormalStroke extends AbsStrokes{
	Paint paint = new Paint();
	
	Path path = null;
	List<PointF> pointList;
	
	float lastX;
	float lastY;
	
	UserDrawingManager drawingManager = UserDrawingManager.getInstance();
	
	public NormalStroke(){
		this.color = Color.BLACK;
		this.alpha = 255;
		this.thick = 5.f; // Logical unit

		paint.setColor(color);
		paint.setAlpha(alpha);
		paint.setStrokeWidth(thick);

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);

		paint.setAntiAlias(true);
		paint.setDither(true);
		
		path = new Path();
		pointList = new ArrayList<PointF>();
	}
	
	public NormalStroke(int color, int alpha, float thick)
	{
		this();
		
		this.color = color;
		this.alpha = alpha;
		this.thick = thick;
		
		paint.setColor(color);
		paint.setAlpha(alpha);
		paint.setStrokeWidth(thick);
	}
	
	@Override
	public void setThick(float thick)
	{
		this.thick = thick;
		paint.setStrokeWidth(thick);
	}

	@Override
	public void setColor(int color)
	{
		this.color = color;
		paint.setColor(color);
		paint.setAlpha(alpha);
	}

	@Override
	public void setAlpha(int alpha)
	{
		this.alpha = alpha;
		this.paint.setAlpha(alpha);
	}

	@Override
	public void moveTo(float x, float y)
	{
		this.editMode = true;
		
		lastX  = x;
		lastY  = y;
		
		pointList.add(new PointF(x,y));
		
		path.moveTo(x, y);
	}
	
	@Override
	public void lineTo(float x, float y)
	{
		if ((lastX != x && lastY != y) || pointList.size() > 1)
		{
			pointList.add(new PointF(x,y));
			
			//path.quadTo(lastX, lastY, (x + lastX)/2.f, (y + lastY)/2);
			path.lineTo(x, y);
			lastX = x;
			lastY = y;
		}
	}
	
	@Override
	public void quadTo(float x, float y)
	{
		if (getDistanceSquare(x, y, lastX, lastY) >= 9)
		{
			pointList.add(new PointF(x,y));
			
			path.quadTo(lastX, lastY, (x + lastX)/2.f, (y + lastY)/2.f);
			
			lastX = x;
			lastY = y;
		}
	}
	
	protected float getDistanceSquare(float x1, float y1, float x2, float y2)
	{
		return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
	}
	
	@Override
	public void close()
	{
		editMode = false;
	}

	@Override
	public void draw(Canvas canvas)
	{
		if(canvas == null){
			return;
		}
		
		if (pointList.size() == 1)
		{
			paint.setStrokeWidth(1.f);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			
			PointF point = pointList.get(0);
			
			float radius = drawingManager.toDeviceUnit(thick/2.f); 
			canvas.drawCircle(point.x, point.y, radius, paint);
		}
		else
		{
			float strokeWidth = drawingManager.toDeviceUnit(thick);
			paint.setStrokeWidth(strokeWidth);
			paint.setStyle(Paint.Style.STROKE);
			
			canvas.drawPath(path, paint);
		}
	}

	@Override
	public String toXML()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<NormalStroke color=\"" + color + "\" alpha=\"" + alpha + "\" thick=\"" + thick + "\" >");
		
		PointF pointLog = new PointF();

		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</NormalStroke>\n");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		alpha = Integer.parseInt(XmlUtil.getNodeAttribute(element, "alpha"));
		color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		thick = Float.parseFloat(XmlUtil.getNodeAttribute(element, "thick"));

		Element child = XmlUtil.getFirstChildElement(element);

		while (child != null)
		{
			float x = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "x")));
			float y = drawingManager.toDeviceUnit(Float.parseFloat(XmlUtil.getNodeAttribute(child, "y")));
			
			pointList.add(new PointF(x,y));
			
			child = XmlUtil.getNextSiblingElement(child);
		}

		paint.setColor(color);
		paint.setAlpha(alpha);
		paint.setStrokeWidth(drawingManager.toDeviceUnit(thick));

		// Build path
		PointF point1 = pointList.get(0);
		path.moveTo(point1.x, point1.y);
		
		float lastX = point1.x;
		float lastY = point1.y;
		
		for (int i = 1; i < pointList.size()-1; i++)
		{
			float x = pointList.get(i).x;
			float y = pointList.get(i).y;
			
			path.quadTo(lastX, lastY, (x + lastX)/2.f, (y + lastY)/2);
			
			lastX = x;
			lastY = y;			
		}
		
		if (pointList.size() > 0)
		{
			PointF point = pointList.get(pointList.size()-1);
			path.lineTo(point.x, point.y);
		}
	}

	int playIndex = 0;

	@Override
	public void readyForPlay()
	{
		playIndex = 1;
		path = new Path();
		PointF point = pointList.get(0);
		path.moveTo(point.x, point.y);
		
		lastX = point.x;
		lastY = point.y;
		
		editMode = true;
	}

	@Override
	public boolean updateForPlay()
	{
		if (pointList.size() == 1)
		{
			editMode = false;
			return true;
		}

		int endPoint = Math.min(playIndex+3, pointList.size());

		for (int i = playIndex; i < endPoint; i++)
		{
			PointF point = pointList.get(i);
			
			if (i == pointList.size()-1)
			{
				path.lineTo(point.x, point.y);
				editMode = false;
				return true;
			}
			else
			{
				path.quadTo(lastX, lastY, (point.x + lastX)/2.f, (point.y + lastY)/2.f);
			}

			lastX = point.x;
			lastY = point.y;
		}
		
		playIndex += 3;

		return false;
	}

	@Override
	public void drawForPlay(Canvas canvas)
	{
		draw(canvas);
	}
}
