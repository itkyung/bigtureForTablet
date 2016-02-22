package com.clockworks.android.tablet.bigture.common.bitmapDownloader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BitmapDownloader
{
	static int CORE_POOL_SIZE = 3;
	static int MAX_POOL_SIZE = 100;
	static int KEEP_ALIVE = 1;
	
	private static BitmapDownloader imageDownloader;

	//protected Context context;

	// �̹��� �ٿ�ε� �۾� ������ �����
	@SuppressLint("UseSparseArrays")
	private final Map<Integer,ImageDownloadTask> taskMap = Collections.synchronizedMap(new HashMap<Integer,ImageDownloadTask>());

	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(10);

	// ������ ���丮
	private static final ThreadFactory sThreadFactory = new ThreadFactory()
	{
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r)
        {
            return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    
    // Thread pool executor
	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

	protected BitmapDownloader()
	{
		//this.context = context;
		
		if (BitmapCacheManager.getInstance() == null){
			String cacheDir = BigtureEnvironment.getInstance().cacheDir;
			BitmapCacheManager.createInstance(cacheDir);
		}
	}
	
	public static BitmapDownloader createInstance()
	{
		imageDownloader = new BitmapDownloader();
		return imageDownloader;
	}
	
	public static synchronized void deleteInstance()
	{
		if (imageDownloader != null)
		{
			Iterator<Integer> it = imageDownloader.taskMap.keySet().iterator();
			while (it.hasNext())
			{
				Integer key = it.next();
				
				ImageDownloadTask task = imageDownloader.taskMap.get(key);
				task.cancel();
			}

			imageDownloader.taskMap.clear();
			BitmapCacheManager.deleteInstance();
		}
		
		imageDownloader = null;
	}
	
	public static BitmapDownloader getInstance()
	{
		if (imageDownloader == null)
			imageDownloader = createInstance();
		
		return imageDownloader;
	}
	
	public void displayImage(String imageURL, BitmapFactory.Options options, ImageView imageView, ProgressBar progressBar)
	{
		if (imageURL == null)
			return;
		
		cancelDisplayImage(imageView);

		ImageDownloadTask task = new ImageDownloadTask(imageView, progressBar);

		taskMap.put(imageView.hashCode(), task);
		
		if (Build.VERSION.SDK_INT >= 11)
			task.executeOnExecutor(executor, imageURL, options);
		else
			task.execute(imageURL, options);
	}
	
	public void downloadBitmap(String imageURL, BitmapFactory.Options options, BitmapDownloaderListener listener)
	{
		ImageDownloadTask2 task = new ImageDownloadTask2(listener);

		if (Build.VERSION.SDK_INT >= 11)
			task.executeOnExecutor(executor, imageURL, options);
		else
			task.execute(imageURL, options);
	}
	
	public void cancelDisplayImage(ImageView imageView)
	{
		
		ImageDownloadTask task = taskMap.remove(imageView.hashCode());
		
	
		if (task != null)
			task.cancel();
		
		executor.purge();
	}

	class ImageDownloadTask extends AsyncTask<Object,Void,Bitmap>
	{
		boolean cancelled = false;
		//ImageView imageView;
		//ProgressBar progressBar;
		
		WeakReference<ImageView> refImageView;
		WeakReference<ProgressBar> refProgressBar;
		
		public ImageDownloadTask(ImageView imageView, ProgressBar progressBar)
		{
			refImageView = new WeakReference<ImageView>(imageView);
			
			if (progressBar != null)
				refProgressBar = new WeakReference<ProgressBar>(progressBar);
			//this.imageView = imageView;
			//this.progressBar = progressBar;
		}
		
		public void cancel()
		{
			cancelled = true;
			this.cancel(true);
		}

		@Override
		protected Bitmap doInBackground(Object... params)
		{
			Bitmap bitmap = null;
			BitmapCacheManager cacheManager = BitmapCacheManager.getInstance();
			
			if (cancelled)
			{
				Log.e("ImageDownloader", "Cancelled(1)");
				return null;
			}
			
			String imageURL = (String)params[0];
			BitmapFactory.Options options = (BitmapFactory.Options)params[1];

			bitmap = cacheManager.getBitmap(imageURL, options);
			
			if (cancelled)
			{
				Log.e("ImageDownloader", "Cancelled(2)");
				return null;
			}
			
			if (bitmap == null){
				String filePath = cacheManager.generateCacheFilePath(imageURL);
				boolean result = downloadImage(imageURL, filePath);
				if(result == false)
					return null;
				
				if (cancelled){
					Log.e("ImageDownloader", "Cancelled(3)");
					return null;
				}

				try{
					bitmap = ImageUtil.loadFromFile(filePath, options);
					if (bitmap != null)
						cacheManager.setBitmap(imageURL, bitmap);
				}catch(Exception e){
					e.printStackTrace();
					bitmap = null;
				}
			}
			
			if (cancelled){
				Log.e("ImageDownloader", "Cancelled(4)");
				bitmap = null;
			}
			
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result){
			ImageView imageView = refImageView.get();
			ProgressBar progressBar = (refProgressBar == null ? null : refProgressBar.get());
			
			if (!cancelled){
				if (imageView != null && result != null && !result.isRecycled())
					imageView.setImageBitmap(result);
				
				if (progressBar != null)
					progressBar.setVisibility(View.GONE);
			}

			// ��ϵ� �ʿ��� ���� task�� �����Ѵ�.
			if (imageView != null)
				taskMap.remove(imageView.hashCode());
			
			imageView = null;
			progressBar = null;
		}
		
		protected boolean downloadImage(String imageURL, String filePath){
			if(imageURL == null || imageURL.length() == 0)
				return false;

			InputStream is = null;
			BufferedInputStream bis = null;
			
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			
			try{
				URL url = new URL(imageURL);
				is = (InputStream) url.openConnection().getInputStream();
				bis = new BufferedInputStream(is);

				File file = new File(filePath);
				file.createNewFile();
				
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				
				byte[] buffer = new byte[2048];
				int count = 0;
				
				while ((count = bis.read(buffer)) > 0)
				{
					bos.write(buffer, 0, count);
				}
				return true;
//				while (!cancelled && (count = bis.read(buffer)) > 0)
//				{
//					bos.write(buffer, 0, count);
//				}
			}
			catch (MalformedURLException e){
				e.printStackTrace();
			}catch (IOException e){
				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try { bis.close(); is.close(); } catch (Exception e) {}
				try { bos.close(); fos.close(); } catch(Exception e) {}
			}
			return false;
//			if (cancelled)
//			{
//				File file = new File(filePath);
//				file.delete();
//			}
		}
	}
	
	class ImageDownloadTask2 extends AsyncTask<Object,Void,Bitmap>
	{
		boolean cancelled = false;
		BitmapDownloaderListener listener;
		
		public ImageDownloadTask2(BitmapDownloaderListener listener)
		{
			this.listener = listener;
		}
		
		public void cancel()
		{
			cancelled = true;
			this.cancel(true);
		}

		@Override
		protected Bitmap doInBackground(Object... params){
			Bitmap bitmap = null;
			BitmapCacheManager cacheManager = BitmapCacheManager.getInstance();
			
			if (cancelled)
			{
				Log.e("ImageDownloader", "Cancelled(1)");
				return null;
			}
			
			String imageURL = (String)params[0];
			BitmapFactory.Options options = (BitmapFactory.Options)params[1];

			bitmap = cacheManager.getBitmap(imageURL, options);
			
			
			if (cancelled)
			{
				Log.e("ImageDownloader", "Cancelled(2)");
				return null;
			}
			if(bitmap != null){
				boolean recyled = bitmap.isRecycled();
				if(recyled){
					cacheManager.clearBitmap(imageURL);
					bitmap = null;
				}
			}
			if (bitmap == null){
				String filePath = cacheManager.generateCacheFilePath(imageURL);
				downloadImage(imageURL, filePath);

				if (cancelled){
					Log.e("ImageDownloader", "Cancelled(3)");
					return null;
				}

				try{
					bitmap = ImageUtil.loadFromFile(filePath, options);
					if (bitmap != null)
						cacheManager.setBitmap(imageURL, bitmap);
				}catch(Exception e){
					e.printStackTrace();
					bitmap = null;
				}
			}
			
			if (cancelled){
				Log.e("ImageDownloader", "Cancelled(4)");
				bitmap = null;
			}
			
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result)
		{
			if (!cancelled){
				if (listener != null)
					listener.onComplete(result);
			}
		}
		
		protected void downloadImage(String imageURL, String filePath){
			if(imageURL == null || imageURL.length() == 0)
				return;

			InputStream is = null;
			BufferedInputStream bis = null;
			
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			
			try{
				URL url = new URL(imageURL);
				is = (InputStream) url.openConnection().getInputStream();
				bis = new BufferedInputStream(is);

				fos = new FileOutputStream(new File(filePath));
				bos = new BufferedOutputStream(fos);
				
				byte[] buffer = new byte[2048];
				int count = 0;
				
				while (!cancelled && (count = bis.read(buffer)) > 0)
				{
					bos.write(buffer, 0, count);
				}
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try { bis.close(); is.close(); } catch (Exception e) {}
				try { bos.close(); fos.close(); } catch(Exception e) {}
			}
			
			if (cancelled)
			{
				File file = new File(filePath);
				file.delete();
			}
		}
	}
	
	public interface BitmapDownloaderListener
	{
		void onComplete(Bitmap bitmap);
	}
}
