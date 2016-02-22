package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.io.File;
import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BigtureFileAdapter extends BaseAdapter{
	Context context;
	FileThumbnail.Callback callback;
	ArrayList<File> files;
	FileListener listener;
	
	public BigtureFileAdapter(Context context, FileThumbnail.Callback callback, ArrayList<File> files,FileListener l)
	{
		this.context = context;
		this.callback = callback;
		this.files = files;
		this.listener = l;
	}

	@Override
	public int getCount()
	{
		return files == null ? 0 : (files.size() + 2) / 3;
	}

	@Override
	public Object getItem(int arg0){
		return files.get(arg0);
	}

	@Override
	public long getItemId(int arg0){
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.cell_file_list, null);
		}

		
		View firstView = convertView.findViewById(R.id.drawNewArtworks);
		firstView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.drawArtworks();
			}
		});
		
		FileThumbnail thumb1 = (FileThumbnail)convertView.findViewById(R.id.thumb1);
		
		
		FileThumbnail thumb2 = (FileThumbnail)convertView.findViewById(R.id.thumb2);
		FileThumbnail thumb3 = (FileThumbnail)convertView.findViewById(R.id.thumb3);
		
		int pos = position * 3;
		
		File file = (File)getItem(pos);
		if(file.getName().equals("tmp")){
			firstView.setVisibility(View.VISIBLE);
			thumb1.setVisibility(View.GONE);
		}else{
			firstView.setVisibility(View.GONE);
			thumb1.setVisibility(View.VISIBLE);
			thumb1.updateFileInfo(files.get(position*3), callback);
		}
		
		
		if (pos+1 < files.size()){
			thumb2.setVisibility(View.VISIBLE);
			thumb2.updateFileInfo(files.get(pos+1), callback);			
		}else{
			thumb2.setVisibility(View.INVISIBLE);
		}

		if (pos+2 < files.size())
		{
			thumb3.setVisibility(View.VISIBLE);
			thumb3.updateFileInfo(files.get(pos+2), callback);			
		}
		else
		{
			thumb3.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}
	
	public interface FileListener{
		public void drawArtworks();
	}
}
