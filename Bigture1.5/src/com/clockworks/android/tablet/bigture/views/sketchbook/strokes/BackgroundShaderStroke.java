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
import android.graphics.PointF;
import android.graphics.Shader;

public class BackgroundShaderStroke extends NormalStroke {
	Context context;
	String assetId;

	public BackgroundShaderStroke(){
		super();
	}
	
	
	public BackgroundShaderStroke(Context context){
		super();
		
		this.context = context;
	}
	
	public BackgroundShaderStroke(Context context, String assetId, int color, float thick){
		this(context);
		
		this.assetId = assetId;
		this.thick = thick;
		this.color = color;

		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
		paint.setStrokeWidth(thick);
	}
	
	@Override
	public void setEditMode(boolean editMode)
	{
		if (editMode)
		{
			Bitmap bitmap = UserDrawingManager.getInstance().getAsset(assetId).loadBitmap(context, drawingManager.getViewScale());
			BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			paint.setShader(shader);
		}
		else
		{
			paint.setShader(null);
		}
		
		super.setEditMode(editMode);
	}

	@Override
	public void setColor(int color)
	{
		super.setColor(color);

		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
	}

	@Override
	public void draw(Canvas canvas)
	{
		if (paint.getShader() == null)
		{
			Bitmap bitmap = UserDrawingManager.getInstance().getAsset(assetId).loadBitmap(context, drawingManager.getViewScale());
			BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
			paint.setShader(shader);
		}

		canvas.drawPath(path, paint);
		
		if (!isEditMode())
			paint.setShader(null);
	}

	@Override
	public void moveTo(float x, float y)
	{
		super.moveTo(x, y);
	}

	@Override
	public void lineTo(float x, float y)
	{
		super.lineTo(x, y);
	}

	@Override
	public boolean isBackgroundObject()
	{
		return true;
	}
	
	@Override
	public void close()
	{
		if (pointList.size() == 1)
		{
			PointF point = pointList.get(0);
			point.x += 1.f;
			
			PointF point2 = new PointF();
			point2.x = point.x + 1.f;
			point2.y = point.y;
			
			pointList.add(point2);
			path.lineTo(point2.x, point2.y);
		}
	}

	@Override
	public String toXML()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("<BackgroundShaderStroke color=\"" + color + "\" assetId=\"" + assetId + "\" thick=\"" + thick + "\">");
		
		for (PointF point : pointList)
		{
			sb.append("<point x=\"" + point.x + "\" y=\"" + point.y + "\" />");
		}
		
		sb.append("</BackgroundShaderStroke>\n");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element){
		assetId = XmlUtil.getNodeAttribute(element, "assetId");
		color = Integer.parseInt(XmlUtil.getNodeAttribute(element, "color"));
		thick = Float.parseFloat(XmlUtil.getNodeAttribute(element, "thick"));
		
		Element child = XmlUtil.getFirstChildElement(element);
		
		while (child != null)
		{
			float x = Float.parseFloat(XmlUtil.getNodeAttribute(child, "x"));
			float y = Float.parseFloat(XmlUtil.getNodeAttribute(child, "y"));
			
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

		ColorFilter filter = new LightingColorFilter(0xffffffff, color);
		paint.setColorFilter(filter);
		paint.setStrokeWidth(thick);
	}
}
