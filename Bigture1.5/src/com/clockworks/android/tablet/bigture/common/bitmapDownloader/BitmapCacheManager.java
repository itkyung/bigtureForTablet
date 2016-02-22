package com.clockworks.android.tablet.bigture.common.bitmapDownloader;

import java.io.File;
import java.util.concurrent.LinkedBlockingDeque;

import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapCacheManager
{
	protected static BitmapCacheManager manager;
	private final BitmapMemoryCache memoryCache;
	

	final LinkedBlockingDeque<String> fileNameList;
	
	private final Md5FileNameGenerator fileNameGenerator;
	private int cacheSize;
	
	final String cacheDir;
	
	final int initMemCapacity = 200;
	final int initDiscCacheCapacity = 100;
	final int maxDiscCacheCapacity = 300;

	protected BitmapCacheManager(String cacheDir)
	{
		this.cacheDir = cacheDir;


		cacheSize = 8 * 1024 * 1024;
		memoryCache = new BitmapMemoryCache(cacheSize);
		
	
		fileNameList = new LinkedBlockingDeque<String>(maxDiscCacheCapacity);
		
	
		fileNameGenerator = new Md5FileNameGenerator();

		
		File dir = new File(cacheDir);
		File[] bitmapFiles = dir.listFiles();
		
		try
		{
			for (File file : bitmapFiles)
			{
				fileNameList.add(file.getName());
			}
		}
		catch(Exception e)
		{
		}
	}

	public static BitmapCacheManager createInstance(String cacheDir)
	{
		manager = new BitmapCacheManager(cacheDir);
		return manager;
	}
	
	public static BitmapCacheManager getInstance()
	{
		return manager;
	}
	
	public static void deleteInstance()
	{
		if (manager != null)
		{
			manager.memoryCache.reset();
			manager.fileNameList.clear();
		}
		
		manager = null;
	}

	/**
	 * 
	 * @param url �̹����� URL
	 * @param options ��Ʈ�� �б� �ɼ�
	 * @return
	 */
	public Bitmap getBitmap(String url, BitmapFactory.Options options)
	{
		if (url == null)
			return null;

	
		Bitmap bitmap = memoryCache.get(url);
		

		if (bitmap == null)
		{
		
			String fileName = fileNameGenerator.generate(url);
			String filePath = cacheDir + "/" + fileName;
			File file = new File(filePath);
			

			if (file.exists())
			{
				synchronized(fileNameList)
				{
					trimCacheDisc();

					fileNameList.remove(fileName);
					fileNameList.addFirst(fileName);
				}
				
				bitmap = readBitmapFile(filePath, options);
			}


			if (bitmap != null)
				memoryCache.put(url, bitmap);
		}
		
		return bitmap;
	}
	
	/**
	 * �ش� ��Ʈ�� �̹����� �޸� ĳ�ÿ� �����Ѵ�.
	 * �� ��, ��Ʈ���� ��ũ�� �������� �ʴ´�.
	 * 
	 * @param url �̹����� URL
	 * @param bitmap ������ ��Ʈ��
	 */
	public void setBitmap(String url, Bitmap bitmap)
	{
		this.setBitmap(url, bitmap, false);
	}

	public void clearBitmap(String url){
		memoryCache.remove(url);
	}
	/**
	 * @param url �̹����� URL
	 * @param bitmap ������ ��Ʈ��
	 * @param saveBitmap ĳ�� ��ũ�� ������ ���� ���� 
	 */
	public void setBitmap(String url, Bitmap bitmap, boolean saveBitmap)
	{
		if (bitmap != null)
		{
			String fileName = this.fileNameGenerator.generate(url);
			
			if (saveBitmap)
				ImageUtil.saveBitmap(bitmap, cacheDir + File.separator + fileName);

			memoryCache.remove(url);
			memoryCache.put(url, bitmap);
			
			synchronized(fileNameList)
			{
				// ��ũ ĳ�� ���� �����Ѵ�.
				trimCacheDisc();

				fileNameList.remove(fileName);
				fileNameList.addFirst(fileName);
			}			
		}
	}
	
	private void trimCacheDisc()
	{
		//synchronized(fileNameList)
		{
			while(fileNameList.size() > maxDiscCacheCapacity-1)
			{
				String fileName = fileNameList.removeLast();
				FileUtil.removeFile(cacheDir + File.separator + fileName);
			}			
		}
	}
	
	public int getCacheCount()
	{
		return memoryCache.getCurrentCacheSize();
	}


	public Bitmap downloadBitmap(String imageURL, BitmapFactory.Options options)
	{
		Bitmap bitmap = null;

		// �̹����� ���� ����ҷ� �ٿ�ε� �Ѵ�.
		String filePath = cacheDir + File.separator + this.fileNameGenerator.generate(imageURL);
		HttpUtil.downloadImage(imageURL, filePath);

		// ���� ����ҿ� �ٿ�ε�� �̹��� ������ �д´�.
		bitmap = readBitmapFile(filePath, options);
		
		return bitmap;
	}

	/**
	 * �̹����� URL�� �̿��ؼ� ��ũ ĳ�ÿ� ������ ���ϸ��� ���Ѵ�.
	 * 
	 * @param imageURL
	 * @return
	 */
	public String generateCacheFilePath(String imageURL)
	{
		return this.cacheDir + File.separator + this.fileNameGenerator.generate(imageURL);
	}

	/**
	 * ���� ����ҷκ��� ��Ʈ�� ������ �о� �ǵ�����.
	 * 
	 * @param filePath
	 * @param options
	 * @return ���Ϸκ��� ���� �̹����� Bitmap ��ü
	 */
	protected Bitmap readBitmapFile(String filePath, BitmapFactory.Options options)
	{
		Bitmap bitmap = null;
		
		try
		{
			bitmap = ImageUtil.loadFromFile(filePath, options);
		}
		catch(OutOfMemoryError e)
		{
			System.gc();
			
			try
			{
				bitmap = ImageUtil.loadFromFile(filePath, options);
			}
			catch(OutOfMemoryError e2)
			{
				e2.printStackTrace();
			}
		}
		
		return bitmap;
	}
	
	
}
