package com.clockworks.android.tablet.bigture.views.sketchbook.strokes;

import org.w3c.dom.Element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PointF;
import android.graphics.Shader.TileMode;

public class LinearGradientStroke extends NormalStroke
{
	public LinearGradientStroke()
	{
		super();

		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
		paint.setShader(new LinearGradient(0, 0, 80, 0, colors, null, TileMode.MIRROR));
	}
	
	public LinearGradientStroke(int color, int alpha, float thick)
	{
		super(color, alpha, thick);

		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA};
		paint.setShader(new LinearGradient(0, 0, 80, 0, colors, null, TileMode.MIRROR));
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
//		if (empty)
//		{
//			paint.setStrokeWidth(1.f);
//			paint.setStyle(Paint.Style.FILL_AND_STROKE);
//			
//			canvas.drawCircle(lastX, lastY, thick/2.f, paint);
//		}
//		else
//		{
//			paint.setStrokeWidth(thick);
//			paint.setStyle(Paint.Style.STROKE);
//			
//			canvas.drawPath(path, paint);
//		}
	}

	@Override
	public String toXML()
	{
		PointF pointLog = new PointF();

		StringBuffer sb = new StringBuffer();
		sb.append("<LinearGradientStroke color=\"" + color + "\" alpha=\"" + alpha + "\" thick=\"" + thick + "\">");

		for (PointF point : pointList)
		{
			drawingManager.toLogicalUnit(point, pointLog);
			sb.append("<point x=\"" + pointLog.x + "\" y=\"" + pointLog.y + "\" />");
		}
		
		sb.append("</LinearGradientStroke>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		super.parse(element);
	}
}
