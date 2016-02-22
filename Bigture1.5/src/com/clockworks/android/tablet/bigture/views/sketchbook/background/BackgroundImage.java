package com.clockworks.android.tablet.bigture.views.sketchbook.background;

import java.io.File;


import org.w3c.dom.Element;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.utils.XmlUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawing;



import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class BackgroundImage implements UserDrawing
{
	Context context;

	int resId = 0;
	String filePath;
	boolean editMode;
	Bitmap totalBitmap;
	
	public BackgroundImage(Context context)
	{
		this.context = context;
	}
	
	public BackgroundImage(Context context, Bitmap bitmap){
		this.context = context;
		this.totalBitmap = bitmap;
	}
	
	public BackgroundImage(Context context, int resId)
	{
		this.context = context;
		this.resId = resId;
	}
	
	public BackgroundImage(Context context, String filePath)
	{
		this.context = context;
		this.filePath = filePath;
	}

	@Override
	public boolean isEditMode()
	{
		return this.editMode;
	}

	@Override
	public void setEditMode(boolean editMode)
	{
		this.editMode = editMode;
	}
	
	public String getResourceFilePath()
	{
		return filePath;
	}

	@Override
	public boolean isBackgroundObject()
	{
		return true;
	}

	@Override
	public void draw(Canvas canvas)
	{
		Bitmap bitmap = null;

		try
		{
			if (this.resId != 0)
				bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
			else if (this.filePath != null)
				bitmap = ImageUtil.loadFromFile(filePath);
			else if (this.totalBitmap != null)
				bitmap = totalBitmap;
			
			//Log.d("---", "image size: " + bitmap.getWidth() + ", " + bitmap.getHeight());

			if (bitmap != null)
			{
				//bitmap = ImageUtil.createBitmap(bitmap, canvas.getWidth(), canvas.getHeight());
				Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
				Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
				
				canvas.drawBitmap(bitmap, src, dst, null);
				//canvas.drawBitmap(bitmap, 0, 0, null);

				bitmap.recycle();
				bitmap = null;
			}
		}
		catch(OutOfMemoryError e)
		{
			e.printStackTrace();
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(R.string.common_lb_menu_activity);
			builder.setMessage("Out of memory. Please exit some program and try again");
			builder.setPositiveButton("Ok", null);
			builder.create().show();
		}
	}

	@Override
	public String toXML()
	{
		String xml = null;
		
		if (resId != 0)
		{
			xml = "<BackgroundImage resId=\"" + resId + "\" />";
		}
		else
		{
			String fileName = "";
			int pos = filePath.lastIndexOf("/");
			if (pos >= 0)
				fileName = filePath.substring(pos+1);
			
			xml  = "<BackgroundImage resId=\"0\">";
			xml += " <file>" + XmlUtil.toCDATA(fileName) + "</file>";
			xml += "</BackgroundImage>";
		}
		
		return xml;
	}

	@Override
	public void parse(Element element)
	{
		resId = Integer.parseInt(XmlUtil.getNodeAttribute(element, "resId"));
		
		if (resId == 0)
		{
			Element child = XmlUtil.getFirstChildElement(element);
			if (XmlUtil.getElementName(child).equals("file"))
			{
				String fileName = XmlUtil.getNodeValue(child);
				int pos = fileName.lastIndexOf("/");
				if (pos >= 0)
					fileName = fileName.substring(pos+1);
				
				filePath = FileUtil.getWorkPath(context) + File.separator + fileName;
			}
		}
	}

	@Override
	public void readyForPlay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateForPlay()
	{
		return true;
	}

	@Override
	public void drawForPlay(Canvas canvas)
	{
		draw(canvas);
	}
}
