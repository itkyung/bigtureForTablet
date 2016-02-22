package com.clockworks.android.tablet.bigture.views.sketchbook;

import java.io.File;
import java.util.List;

import com.clockworks.android.tablet.bigture.utils.ZipUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.UserDrawingManager;




import android.content.Context;
import android.os.AsyncTask;

public class FileStoreTask extends AsyncTask<String,Void,Boolean>{
	Context context;
	Callback callback;
	
	public FileStoreTask(Context context, Callback callback)
	{
		this.context = context;
		this.callback = callback;
	}
	
	public void save(String filePath)
	{
		execute(filePath);
	}
	
	@Override
	protected Boolean doInBackground(String... params){
		String filePath = params[0];

		try{
			FileUtil.removeAllFiles(FileUtil.getWorkPath(context));
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		String workDirName = FileUtil.getWorkPath(context);

		String thumbPath = workDirName + File.separator + "thumb.image";
		callback.saveThumbnail(thumbPath);

		String xml = UserDrawingManager.getInstance().toXML();
		String xmlFilePath = workDirName + File.separator + "content.xml";

		try{
			FileUtil.deleteFile(new File(xmlFilePath));
			FileUtil.saveText(xmlFilePath, xml);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}

		
		List<File> fileList = UserDrawingManager.getInstance().getResourceFileList();
		
		File file = new File(xmlFilePath); 
		fileList.add(file);
		
		file = new File(thumbPath);
		fileList.add(file);

		ZipUtil.zip(fileList, filePath);

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result)
	{
		if (this.callback != null)
			callback.onComplete(this, result);
		
		super.onPostExecute(result);
	}

	public interface Callback
	{
		public void onComplete(FileStoreTask task, Boolean result);
		public boolean saveThumbnail(String thumbPath);
	}
}
