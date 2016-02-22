package com.clockworks.android.tablet.bigture.utils.image;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;

public class ImageUtil
{
	public static Bitmap loadFromResource(Resources res, int resourceId)
	{
		return BitmapFactory.decodeResource(res, resourceId);
	}

	public static Bitmap loadFromFile(String pathName)
	{
		return BitmapFactory.decodeFile(pathName);
	}

	public static Bitmap loadFromFile(String pathName, BitmapFactory.Options options)
	{	
		return BitmapFactory.decodeFile(pathName, options);
	}

	public static Bitmap resizeBitmap(Bitmap bitmap, int width, int height)
	{
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	public static Bitmap loadFromAsset(Context context, String fileName)
	{
		Bitmap bitmap = null;

		// load image
		try {
			// get input stream
			InputStream is = context.getAssets().open(fileName);
			bitmap = BitmapFactory.decodeStream(is);
		}
		catch(IOException ex) {
			return null;
		}
		
		return bitmap;
	}

	public static boolean saveBitmap(Bitmap bitmap, String pathName)
	{
		boolean result = true;
		String fileExt = FileUtil.pickFileExt(pathName);

		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(pathName);
			if (fileExt.equalsIgnoreCase("JPG"))
				bitmap.compress(CompressFormat.JPEG, 100, fos);
			else
				bitmap.compress(CompressFormat.PNG, 100, fos);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			try { if (fos != null) fos.close(); } catch(Exception e) {}
		}

		return result;
	}
	
	public static boolean saveJPEGBitmap(Bitmap bitmap, String pathName)
	{
		boolean result = true;

		FileOutputStream fos = null;

		try
		{
			fos = new FileOutputStream(pathName);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			result = false;
		}
		finally
		{
			try { if (fos != null) fos.close(); } catch(Exception e) {}
		}

		return result;
	}

	public static Bitmap convertMutable(Bitmap bitmap)
	{
		Bitmap bitmapNew = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Paint paint = new Paint();
		paint.setDither(true);
		paint.setAntiAlias(true);
		
		Canvas canvas = new Canvas(bitmapNew);
		canvas.drawBitmap(bitmap, 0, 0, paint);
		
		return bitmapNew;
	}

	public static Bitmap getVideoThumbnail(Activity activity, Uri videoUri, int type)
	{
		String[] columns = {
				MediaStore.Video.Media._ID,
				MediaStore.Video.Media.DISPLAY_NAME,
				MediaStore.Video.Media.DATA };

		Cursor cursor = activity.getContentResolver().query(videoUri, columns, MediaStore.Video.Media.DISPLAY_NAME+"=?", new String[] {"name.mp4"}, null);

		//		Cursor cursor = activity.managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
		//				columns, MediaStore.Video.Media.DISPLAY_NAME+"=?",new String[] {"name.mp4"}, null);

		cursor.moveToFirst();
		long fileid = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID)); 

		ContentResolver resolver = activity.getContentResolver();
		Bitmap thumb = MediaStore.Video.Thumbnails.getThumbnail(resolver, fileid, type, null);
		return thumb;
	}

	public static int getMinSampleSize(int viewWidth, int viewHeight, int bitmapWidth, int bitmapHeight)
	{
		int sample = 1;
		
		if (viewWidth > 0 && viewHeight > 0)
			sample = Math.min(bitmapWidth / viewWidth, bitmapHeight / viewHeight);
		
		return sample;
	}

	public static int getMaxSampleSize(int viewWidth, int viewHeight, int bitmapWidth, int bitmapHeight)
	{
		int sample = 1;
		
		if (viewWidth > 0 && viewHeight > 0)
			sample = Math.max(Math.round((float)bitmapWidth / (float)viewWidth), Math.round((float)bitmapHeight / (float)viewHeight));
		
		return sample;
	}
	
	public static int dipToPixel(Context context, float dip)
	{
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	}

	public static Bitmap createRoundedCornerBitmap(Bitmap bitmap, float radius)
	{
		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
 
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
 
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);
 
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
 
        return result;
	}
	
	public static Bitmap createCircularBitmap(Bitmap bitmapProfile, float radius)
	{
		Bitmap result = Bitmap.createBitmap((int)radius*2, (int)radius*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
		
        int color = 0xff424242;
        Paint paint = new Paint();
 
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(radius, radius, radius, paint);
 
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        
        Rect dst = new Rect(0, 0, (int)(radius*2.f), (int)(radius*2.f));
        Rect src = new Rect(0, 0, bitmapProfile.getWidth(), bitmapProfile.getHeight());
        
        canvas.drawBitmap(bitmapProfile, src, dst, paint);

        return result;
	}
	
	public static Bitmap createCircularIcon(Context context, Bitmap bitmapProfile)
	{
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        
		Bitmap result = Bitmap.createBitmap(72, 72, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		
		Bitmap bmBg = loadFromResource(context.getResources(), R.drawable.specialclasspage_subject_layer_messagebox_profile_bg);
		canvas.drawBitmap(bmBg, 0, 0, paint);

		Bitmap bmProfile = createCircularBitmap(bitmapProfile, 29);
		canvas.drawBitmap(bmProfile, 7, 4, paint);
		
		return result;
	}
	
	public static Bitmap createBitmap(Bitmap bitmapOrg, int width, int height)
	{
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.WHITE);

		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		float rate1 = (float)bitmapOrg.getWidth() / (float)bitmapOrg.getHeight();
		float rate2 = (float)width / (float)height;

		Rect  srcRect = new Rect();
		RectF dstRect = new RectF(0,0,width,height);

		if (rate1 > rate2) // �̹����� �� ���� ���
		{
			int tempWidth = (int)((float)bitmapOrg.getHeight() * rate2);
			srcRect.left = (bitmapOrg.getWidth() - tempWidth) / 2;
			srcRect.top  = 0;
			srcRect.right = srcRect.left + tempWidth;
			srcRect.bottom = bitmapOrg.getHeight();
		}
		else
		{
			int tempHeight = (int)((float)bitmapOrg.getWidth() / rate2);
			srcRect.left = 0;
			srcRect.right = bitmapOrg.getWidth();
			srcRect.top  = (bitmapOrg.getHeight() - tempHeight) / 2;
			srcRect.bottom = srcRect.top + tempHeight;
		}

		canvas.drawBitmap(bitmapOrg, srcRect, dstRect, paint);
		bitmapOrg.recycle();

		return bitmap;
	}

	public static Bitmap flipHorz(Bitmap bitmap)
	{
		int width  = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(-1.f, 1.f);

		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	public static Bitmap flipVert(Bitmap bitmap)
	{
		int width  = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1.f, -1.f);

		return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	}

	public static Bitmap createNoteThumbnail(Bitmap bitmapOrg, int type)
	{
		int width = 86;
		int height = 86;

		Bitmap rounder = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(rounder);

		Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xferPaint.setColor(Color.RED);
		canvas.drawRoundRect(new RectF(0,0,width, height), 10.0f, 10.0f, xferPaint);

		xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		Bitmap image = createBitmap(bitmapOrg, width, height);

		Bitmap thumb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas2 = new Canvas(thumb);

		canvas2.drawBitmap(image, 0, 0, null);
		canvas2.drawBitmap(rounder, 0, 0, xferPaint);

		return thumb;
	}

	public static Bitmap createRoundedImage(Bitmap bitmapOrg, int width, int height, float r)
	{
		Bitmap rounder = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(rounder);

		Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xferPaint.setColor(Color.RED);
		canvas.drawRoundRect(new RectF(0,0,width, height), r, r, xferPaint);

		xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

		//Bitmap image = createBitmap(bitmapOrg, width, height);

		Bitmap thumb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas2 = new Canvas(thumb);

		Rect src = new Rect(0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight());
		Rect dst = new Rect(0, 0, width, height);
		
		canvas2.drawBitmap(bitmapOrg, src, dst, null);
		//canvas2.drawBitmap(bitmapOrg, 0, 0, null);
		canvas2.drawBitmap(rounder, 0, 0, xferPaint);
		
		bitmapOrg.recycle();
		rounder.recycle();

		return thumb;
	}

	public static Bitmap createNoteThumbnail(String text, int type)
	{
		int width = 86;
		int height = 86;
		int margin = 4;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setARGB(255, 255, 255, 255);
		RectF rect = new RectF(0f,0f,(float)width, (float)height);
		canvas.drawRect(rect, paint);

		char[] chars = text.toCharArray();
		int crntPos = 0;
		int prevPos = 0;
		int maxWidth = width - (margin * 2);

		int x = margin;
		int y = margin + (int)Math.abs(paint.getFontMetrics().ascent) + 4;

		paint.setTextSize(12);
		paint.setColor(0xff000000);

		while (crntPos < chars.length)
		{
			int count = chars.length - prevPos;
			crntPos = paint.breakText(chars, prevPos, count, maxWidth, null) + prevPos;
			canvas.drawText(chars, prevPos, crntPos - prevPos, x, y, paint);

			prevPos = crntPos;
			y += (int)Math.abs(paint.getFontMetrics().ascent) + 4;
		}

		return bitmap;
	}

	public static Bitmap createSummaryThumbnail(Bitmap bitmapOrg)
	{
		int width = 120;
		int height = (width * 3) / 4;

		return createBitmap(bitmapOrg, width, height);
	}

	public static Bitmap createSummaryThumbnail(String text)
	{
		int width = 120;
		int height = (width * 3) / 4;
		int margin = 4;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setARGB(255, 255, 255, 255);
		RectF rect = new RectF(0f,0f,(float)width, (float)height);
		canvas.drawRect(rect, paint);

		char[] chars = text.toCharArray();
		int crntPos = 0;
		int prevPos = 0;
		int maxWidth = width - (margin * 2);

		int x = margin;
		int y = margin + (int)Math.abs(paint.getFontMetrics().ascent) + 4;

		paint.setTextSize(12);
		paint.setColor(0xff000000);

		while (crntPos < chars.length)
		{
			int count = chars.length - prevPos;
			crntPos = paint.breakText(chars, prevPos, count, maxWidth, null) + prevPos;
			canvas.drawText(chars, prevPos, crntPos - prevPos, x, y, paint);

			prevPos = crntPos;
			y += ((int)Math.abs(paint.getFontMetrics().ascent) + 4);
		}

		return bitmap;
	}
}
