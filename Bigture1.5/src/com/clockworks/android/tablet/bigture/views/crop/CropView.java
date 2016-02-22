package com.clockworks.android.tablet.bigture.views.crop;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CropView extends View
{
	public static int HIT_LEFT = 1;
	public static int HIT_TOP = 4;
	public static int HIT_RIGHT = 2;
	public static int HIT_BOTTOM = 8;
	public static int HIT_LEFTTOP = HIT_LEFT | HIT_TOP;
	public static int HIT_RIGHTTOP = HIT_RIGHT | HIT_TOP;
	public static int HIT_LEFTBOTTOM = HIT_LEFT | HIT_BOTTOM;
	public static int HIT_RIGHTBOTTOM = HIT_RIGHT | HIT_BOTTOM;
	
	public static int HIT_INNER = HIT_LEFTTOP | HIT_RIGHTBOTTOM;
	
	private int measuredWidth;
	private int measuredHeight;
	
	ContentResolver contentResolver;
	String imagePath;
	Bitmap bitmap;
	Rect rectBitmapSrc;
	RectF rectBitmapDst;
	
	float aspectX;
	float aspectY;
	
	RectF rect;
	RectF rectHandle;
	
	Matrix bitmapMatrix;
	Paint paint = new Paint();
	DashPathEffect dashEffect = new DashPathEffect(new float[] {2,2}, 0);
	PorterDuffColorFilter colorFilter;
	LightingColorFilter colorFilter2;
	
	float lastTouchX;
	float lastTouchY;
	int hitTest;

	public CropView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setARGB(255, 128, 128, 128);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
		
		colorFilter = new PorterDuffColorFilter(Color.GRAY, PorterDuff.Mode.XOR);
		colorFilter2 = new LightingColorFilter(Color.GRAY, Color.BLACK);

		rectHandle = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		if (bitmap != null && rectBitmapDst != null)
		{
			canvas.drawBitmap(bitmap, rectBitmapSrc, rectBitmapDst, null);
		}
		
		if (rect != null)
		{
			paint.setStyle(Paint.Style.STROKE);
			paint.setPathEffect(dashEffect);
			paint.setColorFilter(colorFilter2);
			canvas.drawRect(rect, paint);

			
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setPathEffect(null);
			//paint.setColorFilter(null);
			
			float gab = 6;
			rectHandle.set(rect.left-gab, -gab + (rect.top + rect.bottom) / 2, rect.left + gab, gab + (rect.top + rect.bottom) / 2);
			canvas.drawRect(rectHandle, paint);
			
			rectHandle.set(rect.right-gab, -gab + (rect.top + rect.bottom) / 2, rect.right + gab, gab + (rect.top + rect.bottom) / 2);
			canvas.drawRect(rectHandle, paint);

			rectHandle.set(-gab + (rect.left + rect.right) / 2, rect.top-gab, gab + (rect.left + rect.right) / 2, rect.top + gab);
			canvas.drawRect(rectHandle, paint);

			rectHandle.set(-gab + (rect.left + rect.right) / 2, rect.bottom-gab, gab + (rect.left + rect.right) / 2, rect.bottom + gab);
			canvas.drawRect(rectHandle, paint);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		// ��Ʈ���� �׷��� ������ �����Ѵ�.
		if (imagePath != null){
			initView();
		}
		
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			lastTouchX = event.getX();
			lastTouchY = event.getY();
			
			hitTest = hitTest(event.getX(), event.getY());
			
			if (hitTest == 0)
				return false;
			
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (hitTest == 0)
				return false;
			
			float x = event.getX();
			float y = event.getY();
			
			float dx = x - lastTouchX;
			float dy = y - lastTouchY;
			
			lastTouchX = event.getX();
			lastTouchY = event.getY();

			if (aspectX == 0.f || aspectY == 0)
			{
				if ((hitTest & HIT_LEFT) != 0)
					rect.left += dx;
				if ((hitTest & HIT_RIGHT) != 0)
					rect.right += dx;
	
				if ((hitTest & HIT_TOP) != 0)
					rect.top += dy;
				if ((hitTest & HIT_BOTTOM) != 0)
					rect.bottom += dy;
				
				if (rect.left < rectBitmapDst.left)
				{
					float gab = rect.left - rectBitmapDst.left;
					rect.left -= gab;
					
					if ((hitTest & HIT_RIGHT) != 0)
						rect.right -= gab;
				}
				else if (rect.right > rectBitmapDst.right)
				{
					float gab = rect.right - rectBitmapDst.right;
					if ((hitTest & HIT_LEFT) != 0)
						rect.left -= gab;
					rect.right -= gab;
				}
				
				if (rect.top < rectBitmapDst.top)
				{
					float gab = rect.top - rectBitmapDst.top;
					rect.top -= gab;
					if ((hitTest & HIT_BOTTOM) != 0)
						rect.bottom -= gab;
				}
				else if (rect.bottom > rectBitmapDst.bottom)
				{
					float gab = rect.bottom -rectBitmapDst.bottom;
					if ((hitTest & HIT_TOP) != 0)
						rect.top -= gab;
					rect.bottom -= gab;
				}
			}
			else
			{
				if (hitTest == HIT_INNER)
				{
					rect.left += dx;
					rect.right += dx;
					rect.top += dy;
					rect.bottom += dy;
					
					if (rect.left < rectBitmapDst.left)
					{
						float gab = rect.left - rectBitmapDst.left;
						rect.left -= gab;
						rect.right -= gab;
					}
					else if (rect.right > rectBitmapDst.right)
					{
						float gab = rect.right - rectBitmapDst.right;
						rect.left -= gab;
						rect.right -= gab;
					}
					if (rect.top < rectBitmapDst.top)
					{
						float gab = rect.top - rectBitmapDst.top;
						rect.top -= gab;
						rect.bottom -= gab;
					}
					else if (rect.bottom > rectBitmapDst.bottom)
					{
						float gab = rect.bottom - rectBitmapDst.bottom;
						rect.top -= gab;
						rect.bottom -= gab;
					}
				}
				else
				{
					if ((hitTest & HIT_LEFT) != 0)
					{
						rect.left += dx;
						rect.top += aspectY * dx / aspectX;
					}
					else if ((hitTest & HIT_TOP) != 0)
					{
						rect.top += dy;
						rect.left  += aspectX * dy / aspectY;
					}
					else if ((hitTest & HIT_RIGHT) != 0)
					{
						rect.right += dx;
						rect.bottom += aspectY * dx / aspectX;
					}
					else if ((hitTest & HIT_BOTTOM) != 0)
					{
						rect.bottom += dy;
						rect.right += aspectX * dy / aspectY;
					}
					
					// �¿� ������ �����Ѵ�.
					rect.bottom = rect.top + (rect.right - rect.left) * aspectY / aspectX;
					
					// ���� ������ �̹����� ������� Ȯ���Ѵ�.
					if (rect.left < rectBitmapDst.left)
					{
						float gabX = rectBitmapDst.left - rect.left;
						float gabY = gabX * aspectY / aspectX;
						
						rect.left   += gabX;
						rect.top    += gabY;
					}
					if (rect.right > rectBitmapDst.right)
					{
						float gabX = rect.right - rectBitmapDst.right;
						float gabY = gabX * aspectY / aspectX;
						
						rect.right  -= gabX;
						rect.bottom -= gabY;
					}
					if (rect.top < rectBitmapDst.top)
					{
						float gabY = rectBitmapDst.top - rect.top;
						float gabX = gabY * aspectX / aspectY;
						
						rect.top    += gabY;
						rect.left   += gabX;
					}
					if (rect.bottom > rectBitmapDst.bottom)
					{
						float gabY = rect.bottom - rectBitmapDst.bottom;
						float gabX = gabY * aspectX / aspectY;
						rect.bottom -= gabY;
						rect.right  -= gabX;
					}
				}
			}
			
			invalidate();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	public int hitTest(float x, float y)
	{
		int hitTest = 0;
		float gab = 10;
		
		if (x > rect.left - gab && x < rect.left + gab)
		{
			if (y > rect.top - gab && y < rect.top + gab)
				hitTest = HIT_LEFTTOP;
			else if (y > rect.bottom - gab && y < rect.bottom + gab)
				hitTest = HIT_LEFTBOTTOM;
			else
				hitTest = HIT_LEFT;
		}
		else if (x > rect.right - gab && x < rect.right + gab)
		{
			if (y > rect.top - gab && y < rect.top + gab)
				hitTest = HIT_RIGHTTOP;
			else if (y > rect.bottom - gab && y < rect.bottom + gab)
				hitTest = HIT_RIGHTBOTTOM;
			else
				hitTest = HIT_RIGHT;
		}
		else if (x > rect.left && x < rect.right)
		{
			if (y > rect.top - gab && y < rect.top + gab)
				hitTest = HIT_TOP;
			else if (y > rect.bottom - gab && y < rect.bottom + gab)
				hitTest = HIT_BOTTOM;
			else if (y > rect.top && y < rect.bottom)
				hitTest = HIT_INNER;
		}
		
		Log.d("HitTest", "" + hitTest);
		
		return hitTest;
	}

	public void setImagePath(String filePath, float aspectX, float aspectY, int mWidth,int mHeight){
		this.imagePath = filePath;
		
		this.measuredHeight = mHeight;
		this.measuredWidth = mWidth;
		

		
		this.aspectX = aspectX;
		this.aspectY = aspectY;
		
		 
		if (measuredWidth > 0 && measuredHeight > 0){
			LoadBitmapTask task = new LoadBitmapTask();
			task.execute(filePath);
		}
	}

	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
		initView();
		invalidate();
	}
	
	/**
	 * 이 함수는 별도의 Task에서 실행된다.
	 * @param filePath
	 * @return
	 */
	private Bitmap loadBitmap(String filePath){
		if (bitmap != null)
			bitmap.recycle();
		
		if(filePath.startsWith("content://com.google.android.gallery3d.provider")){
			File cacheDir;
			// if the device has an SD card
//			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
//				cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),".OCFL311");
//			} else {
//				// it does not have an SD card
//			   	//cacheDir=ActivityPicture.this.getCacheDir();
//				cacheDir = BigtureEnvironment.getInstance().cacheDir;
//			}
			cacheDir = new File(BigtureEnvironment.getInstance().cacheDir);
			if(!cacheDir.exists())
			    	cacheDir.mkdirs();

			File f = new File(cacheDir, "picasa");
			
			Uri imageUri = Uri.parse(filePath);
			try{
				InputStream is = contentResolver.openInputStream(imageUri);
	//			return BitmapFactory.decodeStream(is);
				OutputStream os = new FileOutputStream(f);
				byte[] buffer = new byte[2048];
				int len;
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				
				os.close();
				
				filePath = f.getAbsolutePath();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		
		int width  = options.outWidth;
		int height = options.outHeight;
		
		int wScale = (width / measuredWidth);
		if (width % measuredWidth > 0)
			wScale += 1;
		int hScale = (height / measuredHeight);
		if (height % measuredHeight > 0)
			hScale += 1;
		
		options.inJustDecodeBounds = false;
		//options.inSampleSize = Math.max(wScale, hScale);
		options.inSampleSize = 2;
		options.inPurgeable = true;
		
		Bitmap m = ImageUtil.loadFromFile(filePath, options);
		return m;
		
	}
	
	private void initView(){
		if(bitmap == null) return;
		
		measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED); 
		
		
		rectBitmapSrc = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		int w = measuredWidth;
		int h = measuredHeight;

		int bmWidth  = bitmap.getWidth();
		int bmHeight = bitmap.getHeight();
		
		rectBitmapDst = getDrawingRect(w, h, bmWidth, bmHeight);

		if (aspectX == 0.f || aspectY == 0.f)
		{
			float nh = rectBitmapDst.height() * 0.5f;
			float nw = rectBitmapDst.width() * 0.5f;
			
			rect = new RectF();
			rect.left = ((w - nw) / 2.f);
			rect.right = rect.left + (int)nw;
			rect.top  = ((h - nh) / 2.f);
			rect.bottom = rect.top + (int)nh;
		}
		else
		{
			if (bmWidth/bmHeight > aspectX / aspectY)
			{
				rect = new RectF();
				float nh = rectBitmapDst.height() * 0.5f;
				float nw = nh * aspectX / aspectY;
				
				rect.left = ((w - nw) / 2.f);
				rect.right = rect.left + (int)nw;
				rect.top  = ((h - nh) / 2.f);
				rect.bottom = rect.top + (int)nh;
			}
			else
			{
				rect = new RectF();
				float nw = rectBitmapDst.width() * 0.5f;
				float nh = nw * aspectY / aspectX;
				
				rect.left = ((w - nw) / 2.f);
				rect.right = rect.left + (int)nw;
				rect.top  = ((h - nh) / 2.f);
				rect.bottom = rect.top + (int)nh;
			}
		}		
	}
	
	private RectF getDrawingRect(float w, float h, float bmWidth, float bmHeight)
	{
		RectF rectBitmap = new RectF();

		if (bmWidth/bmHeight < w/h)
		{
			rectBitmap.top = 0;
			rectBitmap.bottom = h;
			
			// bw : bh = nw : nh
			float nh = h;
			float nw = nh * bmWidth / bmHeight;
			rectBitmap.left = (w - nw) / 2;
			rectBitmap.right = rectBitmap.left + nw;
		}
		else
		{
			rectBitmap.left = 0;
			rectBitmap.right = w;
			
			float nw = w;
			float nh = nw * bmHeight / bmWidth;
			rectBitmap.top = (h - nh) / 2;
			rectBitmap.bottom = rectBitmap.top + nh;
		}
		
		return rectBitmap;
	}
	
	public Bitmap crop()
	{
		if (bitmap == null)
			return null;
		
		float scale = (float)bitmap.getWidth() / (float)rectBitmapDst.width();
		
		int x = (int)((rect.left - rectBitmapDst.left) * scale);
		int y = (int)((rect.top  - rectBitmapDst.top) * scale);
		int width  = (int)((rect.right - rect.left) * scale);
		int height = (int)((rect.bottom - rect.top) * scale);
		
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		if (width > bitmap.getWidth())
			width = bitmap.getWidth();
		if (height > bitmap.getHeight())
			height = bitmap.getHeight();
		
		Bitmap bitmapCrop = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapCrop);
		
		Rect src = new Rect(x, y, x + width, y + height);
		Rect dst = new Rect(0, 0, width, height);
		
		canvas.drawBitmap(bitmap, src, dst, null);
		
		return bitmapCrop;
	}
	
	public Bitmap cropAndResize(int width, int height)
	{
		if (bitmap == null)
			return null;
		
		Bitmap bitmapCrop = crop();
		Bitmap bitmapResized = ImageUtil.resizeBitmap(bitmapCrop, width, height);
		
		bitmapCrop.recycle();
		
		return bitmapResized;
	}
	
	class LoadBitmapTask extends AsyncTask<String, Void, Bitmap>{

		@Override
		protected Bitmap doInBackground(String... params) {
			return loadBitmap(params[0]);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
		
			super.onPostExecute(result);
			setBitmap(result);
		}
	}

	public void setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}
	
	
}
