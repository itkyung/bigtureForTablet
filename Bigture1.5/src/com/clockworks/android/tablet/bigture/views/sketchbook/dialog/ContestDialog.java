package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ContestDialog extends Dialog{
	Context context;
	String artworkIdx;
	ListView listView;
	List<ContestEntity> contestList;
	
	public ContestDialog(Context context, String artworkIdx, List<ContestEntity> contestList){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		
		this.context = context;
		this.artworkIdx = artworkIdx;
		this.contestList = contestList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_ske_contest);
		
		listView = (ListView)findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3)
			{
				ListView listView = (ListView)adapterView;
				listView.setItemChecked(position, true);
			}
			
		});
		
		listView.setChoiceMode(1);
		
		ContestAdapter adapter = new ContestAdapter();
		listView.setAdapter(adapter);
		
		listView.setItemChecked(0, true);
		
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		
		findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int pos = listView.getCheckedItemPosition();
				if (pos < 0)
					return;
				
				final ContestEntity entity = contestList.get(pos);
				
//				ContestHandleTask task = new ContestHandleTask(new ContestHandleTask.Callback()
//				{
//					@Override
//					public void onComplete(ContestHandleTask task, boolean result)
//					{
//						WaitDialog.hideWaitDialog();
//						Toast.makeText(context, "Submitted sucessfully", Toast.LENGTH_LONG).show();
//						dismiss();
//					}
//				});
				
				WaitDialog.showWailtDialog(context, false);
			//	task.submitContest(AccountManager.getUserIdx(), entity.index, artworkIdx);
			}
		});
	}
	
	class ContestAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return contestList == null ? 0 : contestList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return contestList.get(position);
		}

		@Override
		public long getItemId(int arg0)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
//			if (convertView == null)
//			{
//				LayoutInflater inflater = LayoutInflater.from(context);
//				convertView = inflater.inflate(R.layout.cell_ske_contest, null);
//			}
//			
//			ContestEntity entity = (ContestEntity)getItem(position);
//			
//			ImageView imageView  = (ImageView)convertView.findViewById(R.id.imageView1);
//			TextView titleLabel  = (TextView)convertView.findViewById(R.id.titleLabel);
//			TextView periodLabel = (TextView)convertView.findViewById(R.id.periodLabel);
//			TextView descLabel   = (TextView)convertView.findViewById(R.id.descLabel);
//			
//			titleLabel.setText(entity.mainTitle);
//			periodLabel.setText(entity.startTime + " to " + entity.endTime);
//			descLabel.setText(entity.description);
//			
//			String imageURL = entity.getThumbnailURL();
//			BitmapDownloader.getInstance().displayImage(imageURL, null, imageView, null);
			
			return convertView;
		}
		
	}
}
