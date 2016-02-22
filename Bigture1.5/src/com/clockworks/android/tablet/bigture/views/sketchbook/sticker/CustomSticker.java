package com.clockworks.android.tablet.bigture.views.sketchbook.sticker;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;


public class CustomSticker extends NormalSticker{
	String filePath;
	UserDrawingManager drawingManager = UserDrawingManager.getInstance();
	
	public CustomSticker(Context context)
	{
		super(context);
	}

	public CustomSticker(Context context, String filePath)
	{
		super(context);
		this.filePath = filePath;
		
		Bitmap bitmap = ImageUtil.loadFromFile(filePath);
		
		width  = bitmap.getWidth();
		height = bitmap.getHeight();

		halfWidth  = width >> 1;
		halfHeight = height >> 1;

		selected = false;
		
		bitmap.recycle();
		bitmap = null;
	}

	public Bitmap loadBitmap()
	{
		Bitmap bitmap = ImageUtil.loadFromFile(filePath);
		return bitmap;
	}

	public String getResourceFilePath()
	{
		return filePath;
	}

	@Override
	public String toXML()
	{
		float[] values = new float[9];
		matrix.getValues(values);
		
		values[Matrix.MSCALE_X] = drawingManager.toLogicalUnit(values[Matrix.MSCALE_X]);
		values[Matrix.MSCALE_Y] = drawingManager.toLogicalUnit(values[Matrix.MSCALE_Y]);
		values[Matrix.MTRANS_X] = drawingManager.toLogicalUnit(values[Matrix.MTRANS_X]);
		values[Matrix.MTRANS_Y] = drawingManager.toLogicalUnit(values[Matrix.MTRANS_Y]);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<CustomSticker>");
		sb.append("<FilePath>" + XmlUtil.toCDATA(filePath) + "</FilePath>");
		
		for (int i = 0; i < 9; i++)
			sb.append("<Matrix value=\"" + values[i] + "\" />");
		
		sb.append("</CustomSticker>");
		
		return sb.toString();
	}

	@Override
	public void parse(Element element)
	{
		float[] values = new float[9];
		
		Element child = XmlUtil.getFirstChildElement(element);
		
		int i = 0;
		while (child != null)
		{
			if (XmlUtil.getElementName(child).equals("FilePath"))
			{
				this.filePath = XmlUtil.getNodeValue(child);
			}
			else
			{
				values[i++] = Float.parseFloat(XmlUtil.getNodeAttribute(child, "value"));
			}

			child = XmlUtil.getNextSiblingElement(child);
		}

		values[Matrix.MSCALE_X] = drawingManager.toDeviceUnit(values[Matrix.MSCALE_X]);
		values[Matrix.MSCALE_Y] = drawingManager.toDeviceUnit(values[Matrix.MSCALE_Y]);
		values[Matrix.MTRANS_X] = drawingManager.toDeviceUnit(values[Matrix.MTRANS_X]);
		values[Matrix.MTRANS_Y] = drawingManager.toDeviceUnit(values[Matrix.MTRANS_Y]);

		matrix = new Matrix();
		matrix.setValues(values);

		Bitmap bitmap = loadBitmap();
		
		width = bitmap.getWidth();
		height = bitmap.getHeight();

		halfWidth = width >> 1;
		halfHeight = height >> 1;

		selected = false;
		
		bitmap.recycle();
		bitmap = null;
	}
}
