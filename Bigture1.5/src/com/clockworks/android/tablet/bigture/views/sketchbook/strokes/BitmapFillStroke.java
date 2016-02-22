package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;

public class BitmapFillStroke extends FillStroke
{
	Context context;
	String assetId;
	
	public BitmapFillStroke(Context context)
	{
		super();
		
		this.context = context;

		paint.setStyle(Paint.Style.FILL);			
	}
	
	public BitmapFillStroke(Context context, String assetId, int color, float thick)
	{
		super(color, thick);
		
		this.context = context;
		this.assetId = assetId;
		
		paint.setStyle(Paint.Style.FILL);			
	}

	@Override
	public void draw(Canvas canvas)
	{
		if(canvas == null){
			return;
		}
		canvas.save();
		
		if (!editMode)
		{
			Bitmap bitmap = UserDrawingManager.getInstance().getAsset(assetId).loadBitmap(context, drawingManager.getViewScale());
			
			BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			paint.setShader(shader);
		
			ColorFilter filter = new LightingColorFilter(0xffffffff, color);
			paint.setColorFilter(filter);
			paint.setStyle(Paint.Style.FILL);
			
			bitmap.recycle();
			bitmap = null;
		}
		else
		{
			paint.setColor(color);
			paint.setAlpha(255);
			paint.setColorFilter(null);
			paint.setStyle(Paint.Style.STROKE);			
		}

		canvas.drawPath(path, paint);
		
		if (!editMode)
			paint.setShader(null);
		
		canvas.restore();
	}

	@Override
	public String toXML()
	{
		PointF pointLog = new PointF();
		StringBuffer sb = new StringBuffer();

		sb.append("<BitmapFillStroke assetId=\"" + assetId + "\" color=\"" + color + "\" thick=\"" + thick + "\">");
		
		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</BitmapFillStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		assetId = XmlUtil.getNodeAttribute(element, "assetId");
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
