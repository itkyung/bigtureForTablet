package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SketchbookActivity;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.views.sketchbook.dialog.BigtureFileAdapter.FileListener;




import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class FileOpenDialog extends Dialog implements FileThumbnail.Callback,FileListener{
	Context context;
	OnOkListener listener;
	
	ListView listView;
	
	public FileOpenDialog(Context context){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.setCanceledOnTouchOutside(false);
		
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_ske_open_file);
		
		listView = (ListView)findViewById(R.id.listFiles);
		
		findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				dismiss();
			}
		});
		
		showFileList();
	}

	public void setOnOkListener(OnOkListener listener)
	{
		this.listener = listener;
	}
	
	public interface OnOkListener
	{
		void onOK(FileOpenDialog dialog, String filename);
		void onClear(FileOpenDialog dialog);
	}
	
	public void showFileList()
	{
		File dataDir = FileUtil.getDataDirectory(context);

		File[] files = dataDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name)
			{
				return name.endsWith(".bigture");
			}
		});
		
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(new File("tmp"));
		fileList.addAll(Arrays.asList(files));
		

		BigtureFileAdapter adapter = new BigtureFileAdapter(context, this, fileList, this);
		listView.setAdapter(adapter);
	}

	@Override
	public void onFileSelected(String filename)
	{
		if (listener != null)
			listener.onOK(this, filename);
	}
	
	@Override
	public void onFileDeleted(final String filename){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.common_lb_menu_activity);
		builder.setMessage(context.getResources().getString(R.string.artworks_sp_delete_picture));
		builder.setCancelable(false);
		builder.setNegativeButton(context.getResources().getString(R.string.common_btn_no), null);
		builder.setPositiveButton(context.getResources().getString(R.string.common_btn_yes), new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				String nName = null;
				if(!filename.endsWith(".bigture")){
					nName = filename + ".bigture";
				}else{
					nName = filename;
				}
				String filePath = FileUtil.getDataDirectory(getContext()) + File.separator + nName;
				FileUtil.removeFile(filePath);
				showFileList();
			}
		});
		builder.show();
	}

	@Override
	public void drawArtworks() {
		listener.onClear(this);
	}
	
	
}
