package com.clockworks.android.tablet.bigture.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * @author Administrator
 *
 */
public class FileUtil
{
	public static final String IMG_PATH = "/image";
	public static final String SOUND_PATH = "/sound";
	public static final String VIDEO_PATH = "/video";
	
	public class ResourceFilter implements FilenameFilter
	{
		public String resId;

		public ResourceFilter(String resId)
		{
			this.resId = resId;
		}

		public boolean accept(File dir, String name)
		{
			return name.startsWith(resId);
		}
	}

	public static String getFilePathInDataPath(Context context, String filePath)
	{
		if (filePath.startsWith("/"))
			return getDataPath(context) + filePath;
		else
			return getDataPath(context) + "/" + filePath;
	}

	public static String getDataPath(Context context)
	{
		return getDataDirectory(context).getPath();
	}

	public static File getDataDirectory(Context context)
	{
		File dataDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			String packageName = context.getPackageName();
			
			String path = Environment.getExternalStorageDirectory().getPath();
			path += "/Android/data/" + packageName;
			
			dataDir = new File(path);
			dataDir.mkdirs();
		}

		return dataDir;
	}
	

	public static void prepareCacheDirectory(Context context)
	{
		String dir = getCachePath(context);

		File imageDir = new File(dir + "/picture");
		imageDir.mkdir();
		
		File thumbDir = new File(dir + "/thumbnail");
		thumbDir.mkdir();
		
		File profileDir = new File(dir + "/profile");
		profileDir.mkdir();
	}
	
	public static String getCachePath(Context context)
	{
		File dir = getCacheDirectory(context);
		String filePath = dir.getAbsolutePath();
		
		return filePath;
	}

	public static File getCacheDirectory(Context context)
	{
		File cacheDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			String packageName = context.getPackageName();
			
			String path = Environment.getExternalStorageDirectory().getPath();
			path += ("/Android/data/" + packageName + "/cache");
			
			cacheDir = new File(path);
			cacheDir.mkdirs();
		}

		return cacheDir;
	}

	public static String getTempPath(Context context)
	{
		return getTempDirectory(context).getAbsolutePath();
	}

	public static File getTempDirectory(Context context)
	{
		File tempDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			String packageName = context.getPackageName();
			
			String path = Environment.getExternalStorageDirectory().getPath();
			path += ("/Android/data/" + packageName + "/temp");
			
			tempDir = new File(path);
			tempDir.mkdirs();
		}
		
//		File dir = context.getDir("temp", Context.MODE_WORLD_WRITEABLE);
//		dir.mkdir();

		return tempDir;
	}
	
	public static String getWorkPath(Context context)
	{
		return getWorkDirectory(context).getAbsolutePath();
	}
	
	public static File getWorkDirectory(Context context)
	{
		File tempDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			String packageName = context.getPackageName();
			
			String path = Environment.getExternalStorageDirectory().getPath();
			path += ("/Android/data/" + packageName + "/temp/works");
			
			tempDir = new File(path);
			tempDir.mkdirs();
		}

		return tempDir;
	}
	
	public static String getMessagePath(Context context)
	{
		File messageDir = null;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state))
		{
			String packageName = context.getPackageName();
			
			String path = Environment.getExternalStorageDirectory().getPath();
			path += ("/Android/data/" + packageName + "/message");
			
			messageDir = new File(path);
			
			if (!messageDir.isDirectory())
				messageDir.mkdirs();
		}
		
		if (messageDir != null)
			return messageDir.getAbsolutePath();
		else
			return null;
	}

	public static File createTempFile(Context context, String filename) throws IOException
	{
		return createTempFile(context, filename, null);
	}

	public static File createTempFile(Context context, String prefix, String suffix) throws IOException
	{
		File dir = getTempDirectory(context);
		File file = File.createTempFile(prefix, suffix, dir);

		return file;
	}

	public static String getMediaPathFromUri(Activity activity, Uri uri)
	{
		// can post image
		String [] proj = { MediaStore.Images.Media.DATA, // ������ ���� ��� ���(���ڿ���)
				MediaStore.Images.Media._ID   // ������ ���̺���� ���̵�(������)
		};

		Cursor cursor = activity.managedQuery( uri,
				proj,  // which columns to return
				null,  // where clause; which rows to return (all rows)
				null,  // where clause selection arguments (none)
				null);  // Order-by clause (ascending by name)

		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public StringBuffer loadFile(String filePath) throws Exception
	{
		StringBuffer buffer = new StringBuffer();

		try
		{
			int count = 0;
			char buff[] = new char[8196];

			Reader reader = new BufferedReader(new FileReader(filePath));
			while((count = reader.read(buff)) > 0)
			{
				buffer.append(buff, 0, count);
			}

			reader.close();
		}
		catch(Exception e)
		{
			buffer = null;
			System.out.println("FileUtil::loadFile:: Cannot load the file " + filePath);
		}

		return buffer;
	}

	public static String loadText(String filePath) throws Exception
	{
		return FileUtil.loadText(filePath, "euc-kr");
	}

	public static String loadText(String filePath, String charset) throws Exception
	{
		StringBuffer result = new StringBuffer();

		InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), charset);
		BufferedReader reader = new BufferedReader(isr);

		int read = 0;
		char[] buffer = new char[4096];

		try
		{
			while((read = reader.read(buffer, 0, 4096)) > 0)
			{
				result.append(new String(buffer, 0, read));
			}
		}
		finally
		{
			if(isr != null)
				isr.close();

			if(reader != null)
				reader.close();
		}

		return result.toString();		
	}
	
	public static void saveText(String filePath, String text) throws Exception
	{
		FileOutputStream fos = new FileOutputStream(filePath);
		
		try
		{
			fos.write(text.getBytes("UTF-8"));
		}
		finally
		{
			if(fos != null)
				fos.close();
		}
	}

	public static long getFileSize(String filePath)
	{
		File file = new File(filePath);
		return getFileSize(file);
	}

	public static long getFileSize(File file)
	{
		return file.length();
	}

	public static List<String> findFile(String filePathPattern)
	{
		String dirPath = "";
		String fileName = "";

		int pos = filePathPattern.lastIndexOf("/");
		if(pos >= 0)
		{
			dirPath  = filePathPattern.substring(0, pos);
			fileName = filePathPattern.substring(pos+1);
		}

		if(dirPath.equals(""))
		{
			pos = filePathPattern.lastIndexOf("\\");
			if(pos >= 0)
			{
				dirPath = filePathPattern.substring(0, pos);
				fileName = filePathPattern.substring(pos+1);
			}
		}

		FileUtil fileUtil = new FileUtil();
		ResourceFilter filter = fileUtil.new ResourceFilter(fileName);

		File file = new File(dirPath);
		String[] fileNames = file.list(filter);

		ArrayList<String> result = new ArrayList<String>();
		for(int i = 0; i < fileNames.length; i++)
			result.add(fileNames[i]);

		return result;
	}

	public static String findResourceFile(String filePathPattern)
	{
		String dirPath = "";
		String fileName = "";

		int pos = filePathPattern.lastIndexOf("/");
		if(pos >= 0)
		{
			dirPath  = filePathPattern.substring(0, pos);
			fileName = filePathPattern.substring(pos+1);
		}

		if(dirPath.equals(""))
		{
			pos = filePathPattern.lastIndexOf("\\");
			if(pos >= 0)
			{
				dirPath = filePathPattern.substring(0, pos);
				fileName = filePathPattern.substring(pos+1);
			}
		}

		FileUtil fileUtil = new FileUtil();
		ResourceFilter filter = fileUtil.new ResourceFilter(fileName);

		File file = new File(dirPath);
		String[] fileNames = file.list(filter);

		if(fileNames != null && fileNames.length > 0)
			return fileNames[0];
		else
			return null;

	}

	/*	public static String[] findResourceFiles(String filePathPattern)
	{
		String dirPath = "";
		String fileName = "";

		int pos = filePathPattern.lastIndexOf("/");
		if(pos >= 0)
		{
			dirPath  = filePathPattern.substring(0, pos);
			fileName = filePathPattern.substring(pos+1);
		}

		if(dirPath.equals(""))
		{
			pos = filePathPattern.lastIndexOf("\\");
			if(pos >= 0)
			{
				dirPath = filePathPattern.substring(0, pos);
				fileName = filePathPattern.substring(pos+1);
			}
		}

		File file = new File(dirPath);
		String[] files = file.list(FileFilterUtils.prefixFileFilter(fileName));

		return files;
	}*/

	public static String pickDir(String filePath)
	{
		String dirName = "";
		int pos1 = filePath.lastIndexOf("\\");
		int pos2 = filePath.lastIndexOf("/");

		if(pos1 < 0 && pos2 < 0)
		{
			return dirName;
		}
		else if(pos1 > 0 && pos2 > 0)
		{
			int pos = Math.min(pos1, pos2);
			dirName = filePath.substring(0, pos);
		}
		else if(pos1 > 0 && pos2 < 0)
		{
			dirName = filePath.substring(0, pos1);
		}
		else if(pos2 > 0 && pos1 < 0)
		{
			dirName = filePath.substring(0, pos2);
		}

		return dirName;

	}

	public static String pickFileName(String filePath)
	{
		String fileName = pickFileNameAndExt(filePath);

		int pos = fileName.lastIndexOf(".");
		if(pos >= 0)
			fileName = fileName.substring(0, pos);

		return fileName;
	}

	public static String pickFileNameAndExt(String filePath)
	{
		String fileName = filePath;

		int pos = filePath.lastIndexOf("/");

		if(pos < 0)
			pos = filePath.lastIndexOf("\\");

		if(pos >= 0)
			fileName = filePath.substring(pos+1);

		return fileName;
	}

	public static String pickFileExt(String fileName)
	{
		String ext = "";

		int pos = fileName.lastIndexOf(".");
		if(pos >= 0)
			ext = fileName.substring(pos+1);

		return ext;
	}

	public static boolean isFileExists(String filePath)
	{
		boolean result = false;

		File file = new File(filePath);
		result = file.exists();

		return result;
	}

	public static void mkDir(String dirPath) throws Exception
	{
		File dir = new File(dirPath);
		dir.mkdirs();
	}

	public static boolean isDirectoryEmpty(String dirPath)
	{
		boolean result = false;
		File dir = new File(dirPath);
		
		if (dir.isDirectory())
			result = (dir.listFiles().length == 0);
		
		return result;
	}
	
	public static void deleteFile(File file)
	{
		try
		{
			if (file.exists())
			{
				if (file.isDirectory())
				{
					File[] files = file.listFiles();

					for (File childFile : files)
						deleteFile(childFile);
				}

				file.delete();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void moveFile(String srcPath, String dstPath) throws Exception
	{
		//		Runtime rt = java.lang.Runtime.getRuntime();
		//		rt.exec("mv " + srcPath + " " + dstPath);

		File srcFile = new File(srcPath);
		File dstFile = new File(dstPath);

		srcFile.renameTo(dstFile);
	}

	public static String moveFileRename(String srcPath, String dstPath) throws Exception
	{
		if(srcPath == null || srcPath.equals(""))
			throw new Exception("�ҽ� ���ϰ�ΰ� �������� �ʽ��ϴ�.");
		else if(dstPath == null || dstPath.equals(""))
			throw new Exception("�ҽ� ���͸� ��ΰ� �������� �ʽ��ϴ�.");

		srcPath = srcPath.replaceAll("\\\\", "/");
		dstPath = dstPath.replaceAll("\\\\", "/");

		File dstFile = new File(dstPath);

		String dstDir = pickDir(dstPath);
		String dstFileName = pickFileName(dstPath);
		String dstFileExt = pickFileExt(dstPath);

		String newDstPath = dstPath;

		if(!dstFile.isDirectory())
			throw new Exception("���� ��� ��ΰ� ���͸��� �ƴմϴ�.");

		if(!dstFile.exists())
			throw new Exception("���� ��� ��ΰ� �������� �ʽ��ϴ�.");

		int index = 1;
		while(true)
		{
			newDstPath = dstDir + "/" + dstFileName + "[" + index + "]" + "." + dstFileExt;
			File file = new File(newDstPath);

			if(!file.exists())
				break;

			index++;
		}

		moveFile(srcPath, newDstPath);

		return newDstPath;
	}

	public static void copyFile(String srcPath, String dstPath) throws Exception
	{
		File srcFile = new File(srcPath);
		File dstFile = new File(dstPath);

		copyFile(srcFile, dstFile);
	}

	public static void copyFile(File srcFile, File dstFile) throws Exception
	{
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try
		{
			byte buff[] = new byte[8196];


			in = new BufferedInputStream(new FileInputStream(srcFile));
			out = new BufferedOutputStream(new FileOutputStream(dstFile));

			int count;
			while((count = in.read(buff)) > 0)
				out.write(buff, 0, count);

			out.flush();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			in.close();
			out.close();
		}

		/*		FileInputStream is = null;
		FileOutputStream os = null;

		FileChannel fcIn = null;
		FileChannel fcOut = null;

		try
		{
			is = new FileInputStream(srcFile);
			os = new FileOutputStream(dstFile);

			fcIn = is.getChannel();
			fcOut = os.getChannel();

			fcOut.transferFrom(fcIn, 0, fcIn.size());
		}
		finally
		{
			fcOut.close();
			fcIn.close();

			is.close();
			os.close();
		}*/
	}

	public static void copyFile(InputStream stream, File dstFile) throws Exception
	{
		BufferedInputStream in = null;
		BufferedOutputStream out = null;

		try
		{
			in = new BufferedInputStream(stream);
			out = new BufferedOutputStream(new FileOutputStream(dstFile));

			int c;

			while ((c = in.read()) != -1) {
				out.write(c);
			}
			out.flush();
		}
		finally
		{
			in.close();
			out.close();
		}
	}

	public static void removeFile(String filePath)
	{
		try
		{
		//		Runtime rt = java.lang.Runtime.getRuntime();
		//		rt.exec("rm " + filePath);
		File file = new File(filePath);
		file.delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void removeDirectory(String dirPath) throws Exception
	{
		File dir = new File(dirPath);
		String[] fileList = dir.list();

		if(fileList != null)
		{
			for(int i = 0; i < fileList.length; i++)
			{
				File file = new File(dirPath + "/" + fileList[i]);
				if(file.isFile())
					file.delete();
				else if(file.isDirectory())
					removeDirectory(file.getPath());
			}
		}

		dir.delete();
	}

	public static String[] getDirFileList(String dirPath) throws Exception
	{
		File file = new File(dirPath);
		String[] fileList = file.list();

		return fileList;
	}

	/**
	 * �־��� ���͸��� ���� ���͸��� �����ϴ� ��� ������ �����Ѵ�.
	 * 
	 * @param dirPath
	 * @throws Exception
	 */
	public static void removeAllFiles(String dirPath) throws Exception
	{
		File dir = new File(dirPath);
		String[] fileList = dir.list();

		if(fileList != null)
		{
			for(int i = 0; i < fileList.length; i++)
			{
				File file = new File(dirPath + "/" + fileList[i]);
				if(file.isFile())
				{
					file.delete();
				}
				else if(file.isDirectory())
				{
					removeAllFiles(file.getPath());
					file.delete();
				}
			}
		}
		
		dir.delete();
	}

	public static void rename(String srcFilePath, String dstFilePath) throws Exception
	{
		File srcFile = new File(srcFilePath);
		File dstFile = new File(dstFilePath);

		srcFile.renameTo(dstFile);
	}

	public static void writeFile(String filePath, InputStream in) throws Exception
	{
		try
		{
			int count = 0;
			byte buff[] = new byte[8196];

			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			while((count = in.read(buff)) > 0)
				out.write(buff, 0, count);
			
			out.close();
			in.close();
		}
		catch(Exception e)
		{
			System.out.println("FileUtil::writeFile:: Cannot write the file " + filePath);
			throw e;
		}
	}

	public static void writeFile(String filePath, OutputStream out) throws Exception
	{
		try
		{
			int count = 0;
			byte buff[] = new byte[4096];

			File file = new File(filePath);

			FileInputStream in = new FileInputStream(file);
			while((count = in.read(buff)) > 0)
				out.write(buff, 0, count);

			in.close();
		}
		catch(Exception e)
		{
			System.out.println("FileUtil::writeFile:: Cannot write the file " + filePath);
			throw e;
		}
		finally
		{
			out.close();
		}
	}

	public static String getFileSeparator() throws Exception{
		return File.separator;
	}

	//	public static void main(String[] args) throws Exception
	//	{
	//		long stamp1 = System.currentTimeMillis();
	//		
	//		System.out.println(FileUtil.getFileSeparator());
	//		String file = FileUtil.findResourceFile("C:\\Users\\Administrator\\Documents\\Projects\\Media\\Images\\eps3\\01AST01200904210166701");
	//
	//		long stamp2 = System.currentTimeMillis();
	//		String[] files = FileUtil.findResourceFiles("C:\\Users\\Administrator\\Documents\\Projects\\Media\\Images\\eps3\\01AST01200904210166701");
	//		
	//		long stamp3 = System.currentTimeMillis();
	//		
	//		System.out.println(stamp1);
	//		System.out.println(stamp2-stamp1);
	//		System.out.println(stamp3-stamp2);
	//		
	//		System.out.println(file);
	//		for(int i = 0; i < files.length; i++)
	//			System.out.println(files[i]);
	//	}
}
