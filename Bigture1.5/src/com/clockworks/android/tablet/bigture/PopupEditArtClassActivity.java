package com.clockworks.android.tablet.bigture;

import java.util.Date;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class PopupEditArtClassActivity extends Activity implements DatePickerDialog.OnDateSetListener {
	private ArtClassEntity classEntity;
	
	private TextView startDateLabel;
	private TextView endDateLabel;
	
	private EditText editDesc;
	int activeDateField;
	private Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup_edit_artclass);
		
		Intent intent = getIntent();
		this.classEntity = (ArtClassEntity)intent.getParcelableExtra("classEntity");
		
		this.startDateLabel = (TextView)findViewById(R.id.startDateLabel);
		this.endDateLabel = (TextView)findViewById(R.id.endDateLabel);
		this.editDesc = (EditText)findViewById(R.id.editDesc);
		this.context = this;
		
		final Date startDate = classEntity.dStartDate;
		final Date endDate = classEntity.dEndDate;
		
		this.startDateLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activeDateField = 1;
				int year  = DateUtil.getYear(startDate);
				int month = DateUtil.getMonth(startDate)-1;
				int day   = DateUtil.getDay(startDate);
				
				DatePickerDialog dlg = new DatePickerDialog(context, PopupEditArtClassActivity.this, year, month, day);
				dlg.show();
			}
		});
		
		this.endDateLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activeDateField = 2;
				int year  = DateUtil.getCurrentYear();
				int month = DateUtil.getCurrentMonth() - 1;
				int day   = DateUtil.getCurrentDay();
				
				if (endDate != null){
					year  = DateUtil.getYear(endDate);
					month = DateUtil.getMonth(endDate)-1;
					day   = DateUtil.getDay(endDate);
				}

				DatePickerDialog dlg = new DatePickerDialog(context, PopupEditArtClassActivity.this, year, month, day);
				dlg.show();
			}
		});
		
		Button btnDel = (Button)findViewById(R.id.btnDelete);
		btnDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Confirm	");
				builder.setMessage(R.string.Delete_artclass_confirm);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						WaitDialog.showWailtDialog(context, false);
						DeleteClassTask task = new DeleteClassTask();
						task.execute();
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				builder.create().show();
			}
		});
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WaitDialog.showWailtDialog(context, false);
				
				EditClassTask task = new EditClassTask();
				task.execute();
				
			}
		});
		
		Button btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		updateView();
	}
	
	private void updateView(){
		this.startDateLabel.setText(DateUtil.getDateFormatString(classEntity.dStartDate, "yyyy.MM.dd"));
		this.endDateLabel.setText(DateUtil.getDateFormatString(classEntity.dEndDate, "yyyy.MM.dd"));
		
		this.editDesc.setText(classEntity.description);
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (activeDateField == 1){
			Date date = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(date, "yyyy.MM.dd");
			
			startDateLabel.setText(dateString);
			
		}
		else if (activeDateField == 2){
			Date date = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(date, "yyyy.MM.dd");
			
			endDateLabel.setText(dateString);
			
		}
	}
	
	public void completed(boolean deleted){
		
		Intent intent = new Intent();
		intent.putExtra("deleted", deleted);
		intent.putExtra("classEntity", classEntity);
		setResult(RESULT_OK,intent);
	
		finish();
	}
	
	class EditClassTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			String classId = classEntity.index;
			String startDateStr = startDateLabel.getText().toString();
			String endDateStr = endDateLabel.getText().toString();
			String desc = editDesc.getText().toString();
			
			Date startDateD = DateUtil.createDateFromString(startDateStr, "yyyy.MM.dd");
			Date endDateD = DateUtil.createDateFromString(endDateStr, "yyyy.MM.dd");
			
			return ArtClassHandler.editArtClass(classId, DateUtil.getDateFormatString(startDateD,"yyyy-MM-dd"),
					 DateUtil.getDateFormatString(endDateD,"yyyy-MM-dd"),desc);
		}

		@Override
		protected void onCancelled() {
	
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
	
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			String startDateStr = startDateLabel.getText().toString();
			String endDateStr = endDateLabel.getText().toString();
			classEntity.description = editDesc.getText().toString();
			
			classEntity.dStartDate = DateUtil.createDateFromString(startDateStr, "yyyy.MM.dd");
			classEntity.dEndDate = DateUtil.createDateFromString(endDateStr, "yyyy.MM.dd");
			
			completed(false);
		}
	}
	
	class DeleteClassTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {

			return ArtClassHandler.deleteArtClass(classEntity.index);
		}

		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			completed(true);
		}
		
		
	}
	
	
}
