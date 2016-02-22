package com.clockworks.android.tablet.bigture.views;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.NotificationClickListener;
import com.clockworks.android.tablet.bigture.serverInterface.handler.MainHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationSectionCell extends RelativeLayout {
	private Button btnDelete1;
	private Button btnDelete2;
	private NotificationClickListener listener;
	private TextView title;
	private Context context;
	private String date;
	private Date dateVal;
	private DateFormat fm = new SimpleDateFormat("MM-dd-yyyy");
	
	public NotificationSectionCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		this.btnDelete1 = (Button)findViewById(R.id.btnDelete1);
		this.btnDelete2 = (Button)findViewById(R.id.btnDelete2);
		this.title = (TextView)findViewById(R.id.sectionTitle);
		
		this.btnDelete1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnDelete1.setVisibility(View.GONE);
				btnDelete2.setVisibility(View.VISIBLE);
			}
		});
		
		this.btnDelete2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new DeleteNotificationTask().execute(fm.format(dateVal));
			}
		});
	}



	public void updateView(Date dateVal,String date,NotificationClickListener listener){
		this.title.setText(date);
		this.listener = listener;
		this.date = date;
		this.dateVal = dateVal;
	}
	
	class DeleteNotificationTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected void onCancelled() {
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			return MainHandler.deleteNotification(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			if(listener != null){
				listener.refreshNotifications();
			}
		}
		
		
	}
}
