package com.clockworks.android.tablet.bigture.views.sketchbook.data;




import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;

public class BigtureAsset
{
	public static String ASSET_TYPE_STICKER = "Sticker";
	public static String ASSET_TYPE_MAGIC_BRUSH = "MagicBrush";
	public static String ASSET_TYPE_FILL_PATTERN = "FillPattern";
	public static String ASSET_TYPE_STROKE_PATTERN = "StrokePattern";
	
	public String assetId;
	public String assetType;
	public String assetFileName;
	public String thumbFileName;

	public int baseWidth;
	public int baseHeight;
	
	public float initialScale;
	
	public BigtureAsset(String assetId, String assetType, String assetFileName, int baseWidth, int baseHeight)
	{
		this(assetId, assetType, assetFileName, baseWidth, baseHeight, 1.f);
	}
	
	public BigtureAsset(String assetId, String assetType, String assetFileName, int baseWidth, int baseHeight, float initialScale)
	{
		this.assetId = assetId;
		this.assetType = assetType;
		this.assetFileName = assetFileName;
		this.baseWidth = baseWidth;
		this.baseHeight = baseHeight;
		this.initialScale = initialScale;
	}
	
	public Bitmap loadBitmap(Context context, float viewScale)
	{
		Bitmap bitmap = ImageUtil.loadFromAsset(context, assetFileName);
		
		if (bitmap != null)
		{
			float width  = (float)bitmap.getWidth() * viewScale;
			float height = (float)bitmap.getHeight() * viewScale;
			
			bitmap = ImageUtil.resizeBitmap(bitmap, (int)width, (int)height);
		}
		
		return bitmap;
	}
}
