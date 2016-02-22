package com.clockworks.android.tablet.bigture.common.bitmapDownloader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

import android.graphics.Bitmap;
import android.util.Log;

public class BitmapMemoryCache
{
	public static String TAG = "BitmapMemoryCache";
	public static int DEFAULT_SIZE = 16 * 1024 * 1024;
	private int maxCacheSize;
	private int curCacheSize;

	// ��Ʈ�� �����
	final Map<String,Bitmap> cacheMap = Collections.synchronizedMap(new HashMap<String,Bitmap>());
	
	// �ֱ� ���� ���� ��Ʈ�� ������� Ű�� �����ϴ� deque
	final LinkedBlockingDeque<String> cacheKeyList = new LinkedBlockingDeque<String>();

	public BitmapMemoryCache()
	{
		this(DEFAULT_SIZE);
	}
	
	public BitmapMemoryCache(int cacheSize)
	{
		this.maxCacheSize = cacheSize;
	}
	
	public void reset()
	{
		while (!cacheKeyList.isEmpty())
		{
			String key = cacheKeyList.removeFirst();
			Bitmap bitmap = cacheMap.remove(key);
			if (bitmap != null && !bitmap.isRecycled())
			{
				//bitmap.recycle();
				bitmap = null;
			}
		}
	}
	
	public Bitmap get(String key)
	{
		return null;
//		Bitmap bitmap = cacheMap.get(key);
//		
//		if (bitmap != null)
//		{
//			// Ű�� �����ϰ�, ť�� ���� �տ� ���ġ ��Ų��.
//			cacheKeyList.remove(key);
//			cacheKeyList.addFirst(key);
//		}
//		else
//		{
//			cacheKeyList.remove(key);
//		}
//		
//		return bitmap;
	}
	
	public void put(String key, Bitmap bitmap)
	{
//		int bitmapBytes = getSize(bitmap);
//
//		if (curCacheSize + bitmapBytes > maxCacheSize)
//			trimMemory(curCacheSize + bitmapBytes - maxCacheSize);
//
//		Bitmap bitmapOld = remove(key);
//		
//		if (bitmapOld != null && !bitmapOld.equals(bitmap))
//		{
//			//bitmapOld.recycle();
//			bitmapOld = null;
//		}
//		
//		cacheKeyList.addFirst(key);
//		cacheMap.put(key, bitmap);
//		
//		curCacheSize += bitmapBytes;
//
//		Log.e(TAG, "count: " + cacheKeyList.size() + ", size: " + curCacheSize);
	}
	
	public Bitmap remove(String key)
	{
		Bitmap bitmap = cacheMap.remove(key);
		cacheKeyList.remove(key);

		curCacheSize -= getSize(bitmap);
		
		return bitmap;
	}

	protected Bitmap removeEldest()
	{
		String key = cacheKeyList.pollLast();
		Bitmap bitmap = cacheMap.remove(key);
		
		if (bitmap != null)
		{
			curCacheSize -= getSize(bitmap);

//			if (key != null)
//			{
//				BitmapCacheManager cacheManager = BitmapCacheManager.getInstance();
//				cacheManager.putInDiscCache(key);
//			}
		}
		
		return bitmap;
	}

	public int getCurrentCacheSize()
	{
		return curCacheSize;
	}

	protected void trimMemory(int bytes)
	{
		int trimed = 0;
		
		while(bytes > trimed)
		{
			Bitmap bitmap = removeEldest();
			if (bitmap != null)
			{
				trimed += getSize(bitmap);
				//bitmap.recycle();
				bitmap = null;
			}
		}
	}

	protected int getSize(Bitmap bitmap)
	{
		if (bitmap == null)
			return 0;
		else
			return bitmap.getRowBytes() * bitmap.getHeight();
	}
	
}
