package com.clockworks.android.tablet.bigture.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.clockworks.android.tablet.bigture.utils.file.FileUtil;



import android.util.Log;


public class ZipUtil
{
	private static final String TAG = "ZipUtil";
	
	public static boolean unzip(String zipPath, String dstPath)
	{
		zipPath = zipPath.replaceAll("\\\\", "/");
		dstPath = dstPath.replaceAll("\\\\", "/");
		
		boolean result = true;
		ZipFile zf = null;
		
		if (dstPath.endsWith("/"))
			dstPath = dstPath.substring(0, dstPath.length()-1);
		
		try
		{
			File dir = new File(dstPath);
			if (!dir.isDirectory() && !dir.isFile())
				dir.mkdir();
			
			zf = new ZipFile(zipPath);

			Enumeration<? extends ZipEntry> enumerator = zf.entries();
			
			while( enumerator.hasMoreElements() )
			{
				ZipEntry entry = (ZipEntry)enumerator.nextElement();
				
				if (entry.isDirectory())
				{
					dir = new File(entry.getName());
					dir.mkdir();
					continue;
				}
				else
				{
					if (entry.getName().contains("/"))
					{
						String path = dstPath + "/" + entry.getName();
						path = path.substring(0, path.lastIndexOf("/"));
						dir = new File(path);
						dir.mkdir();
					}
					
					String path = dstPath + "/" + entry.getName();
					
					BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
					copyStream(zf.getInputStream(entry), bos);
				}
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			Log.e(TAG, "Error on unzip", t);
			result = false;
		}
		finally
		{
			try { if (zf != null) zf.close(); } catch(Throwable t) {}
		}
		
		return result;
	}
	
	private static void copyStream(InputStream is, OutputStream os) throws IOException
	{
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		try
		{
			while ((bytes = is.read(buffer, 0, 1024)) > 0)
				os.write(buffer, 0, bytes);
		}
		finally
		{
			try { if (is != null) is.close(); } catch(Throwable t) {}
			try { if (os != null) os.close(); } catch(Throwable t) {}
		}
	}
	
	public static boolean zip(String srcPath, String dstPath)
	{
		File file = new File(srcPath);
		
		if (file.isDirectory())
			return zip(new File[] {file}, file.getAbsolutePath(), dstPath);
		else
			return zip(new File[] {file}, file.getParent(), dstPath);
	}
	
	public static boolean zip(File[] fileList, String rootPath, String dstPath)
	{
		boolean result = true;
		
		ZipOutputStream      zos = null;
		BufferedOutputStream bos = null;

		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(dstPath));
			zos = new ZipOutputStream(bos);
			//zos.setLevel(4);
			
			for (int i = 0; i < fileList.length; i++)
			{
				addToStream(fileList[i], rootPath, zos);
			}
			
			zos.finish();
		}
		catch(Throwable t)
		{
			Log.e(TAG, "Error on archive", t);
			result = false;
		}
		finally
		{
			try { if (zos != null) zos.close(); } catch(Throwable t) {}
			try { if (bos != null) bos.close(); } catch(Throwable t) {}
		}
		
		return result;
	}

	public static boolean zip(File[] fileList, String dstPath)
	{
		boolean result = true;
		
		ZipOutputStream      zos = null;
		BufferedOutputStream bos = null;

		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(dstPath));
			zos = new ZipOutputStream(bos);
			//zos.setLevel(4);
			
			for (int i = 0; i < fileList.length; i++)
			{
				addToStream2(fileList[i], zos);
			}
			
			zos.finish();
		}
		catch(Throwable t)
		{
			Log.e(TAG, "Error on archive", t);
			result = false;
		}
		finally
		{
			try { if (zos != null) zos.close(); } catch(Throwable t) {}
			try { if (bos != null) bos.close(); } catch(Throwable t) {}
		}
		
		return result;
	}

	public static boolean zip(List<File> fileList, String rootPath, String dstPath)
	{
		boolean result = true;
		
		ZipOutputStream      zos = null;
		BufferedOutputStream bos = null;

		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(dstPath));
			zos = new ZipOutputStream(bos);
			//zos.setLevel(4);
			
			for (int i = 0; i < fileList.size(); i++)
			{
				addToStream(fileList.get(i), rootPath, zos);
			}
			
			zos.finish();
		}
		catch(Throwable t)
		{
			Log.e(TAG, "Error on archive", t);
			result = false;
		}
		finally
		{
			try { if (zos != null) zos.close(); } catch(Throwable t) {}
			try { if (bos != null) bos.close(); } catch(Throwable t) {}
		}
		
		return result;
	}
	
	public static boolean zip(List<File> fileList, String dstPath)
	{
		boolean result = true;
		
		ZipOutputStream      zos = null;
		BufferedOutputStream bos = null;

		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(dstPath));
			zos = new ZipOutputStream(bos);
			//zos.setLevel(4);
			
			for (int i = 0; i < fileList.size(); i++)
			{
				addToStream2(fileList.get(i), zos);
			}
			
			zos.finish();
		}
		catch(Throwable t)
		{
			Log.e(TAG, "Error on archive", t);
			result = false;
		}
		finally
		{
			try { if (zos != null) zos.close(); } catch(Throwable t) {}
			try { if (bos != null) bos.close(); } catch(Throwable t) {}
		}
		
		return result;
	}

	private static void addToStream2(File file, ZipOutputStream stream) throws Exception
	{
		if (file.isDirectory())
		{
			File[] fileArray = file.listFiles();
			
			for (int i = 0; i < fileArray.length; i++)
				addToStream2(fileArray[i], stream);
		}
		else
		{
			BufferedInputStream bis = null;
			byte[] buffer = new byte[1024];

			String absPath = file.getAbsolutePath();
			String zipEntryName = FileUtil.pickFileNameAndExt(absPath);
			//String zipEntryName = absPath.substring(rootPath.length()+1, absPath.length());
			
			zipEntryName = zipEntryName.replaceAll("\\\\", "/");
			
			int bytes = 0;
			try
			{
				bis = new BufferedInputStream(new FileInputStream(file));
				ZipEntry zipEntry = new ZipEntry(zipEntryName);
				zipEntry.setTime(file.lastModified());
				
				stream.putNextEntry(zipEntry);
				
				while ((bytes = bis.read(buffer, 0, 1024)) != -1)
				{
					stream.write(buffer, 0, bytes);
				}
				
				stream.closeEntry();
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				//Log.e(TAG, t.getMessage());
				throw new Exception(t);
			}
			finally
			{
				try
				{
					if (bis != null)
						bis.close();
				}
				catch(Exception e)
				{
				}
			}
		}
	}
	
	private static void addToStream(File file, String rootPath, ZipOutputStream stream) throws Exception
	{
		if (file.isDirectory())
		{
			File[] fileArray = file.listFiles();
			
			for (int i = 0; i < fileArray.length; i++)
				addToStream(fileArray[i], rootPath, stream);
		}
		else
		{
			BufferedInputStream bis = null;
			byte[] buffer = new byte[1024];

			String absPath = file.getAbsolutePath();
			String zipEntryName = absPath.substring(rootPath.length()+1, absPath.length());
			
			zipEntryName = zipEntryName.replaceAll("\\\\", "/");
			
			int bytes = 0;
			try
			{
				bis = new BufferedInputStream(new FileInputStream(file));
				ZipEntry zipEntry = new ZipEntry(zipEntryName);
				zipEntry.setTime(file.lastModified());
				
				stream.putNextEntry(zipEntry);
				
				while ((bytes = bis.read(buffer, 0, 1024)) != -1)
				{
					stream.write(buffer, 0, bytes);
				}
				
				stream.closeEntry();
			}
			catch(Throwable t)
			{
				t.printStackTrace();
				//Log.e(TAG, t.getMessage());
				throw new Exception(t);
			}
			finally
			{
				try
				{
					if (bis != null)
						bis.close();
				}
				catch(Exception e)
				{
				}
			}
		}
	}
}
