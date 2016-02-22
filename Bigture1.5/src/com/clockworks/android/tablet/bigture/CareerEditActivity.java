package com.clockworks.android.tablet.bigture;

import java.util.Date;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.task.CareerHandleTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.CareerHandleTask.CareerHandleTaskListener;
import com.clockworks.android.tablet.bigture.utils.DateUtil;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

public class CareerEditActivity extends Activity implements OnClickListener,DatePickerDialog.OnDateSetListener{
	Context context;
	int activeDateField;
	Date startDate = new Date();
	Date endDate = null;//new Date();
	
	TextView startDateLabel;
	TextView endDateLabel;
	EditText editContent;
	
	CareerEntity careerEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_career);
		
		context = this;

		Button btnOk = (Button)findViewById(R.id.btnOK);
		btnOk.setOnClickListener(this);
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		
		startDateLabel = (TextView)findViewById(R.id.startDateLabel);
		startDateLabel.setOnClickListener(this);
		startDateLabel.setText(DateUtil.getDateFormatString(startDate, "yyyy.MM.dd"));
		
		endDateLabel = (TextView)findViewById(R.id.endDateLabel);
		endDateLabel.setOnClickListener(this);
		
		editContent  = (EditText)findViewById(R.id.editContent);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			careerEntity = bundle.getParcelable("careerEntity");
		
		if (careerEntity != null)
			showCareer(careerEntity);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.startDateLabel){
			activeDateField = 1;
			int year  = DateUtil.getYear(startDate);
			int month = DateUtil.getMonth(startDate)-1;
			int day   = DateUtil.getDay(startDate);
			
			DatePickerDialog dlg = new DatePickerDialog(context, this, year, month, day);
			dlg.show();
		}else if (view.getId() == R.id.endDateLabel){
			activeDateField = 2;
			int year  = DateUtil.getCurrentYear();
			int month = DateUtil.getCurrentMonth() - 1;
			int day   = DateUtil.getCurrentDay();
			
			if (endDate != null)
			{
				year  = DateUtil.getYear(endDate);
				month = DateUtil.getMonth(endDate)-1;
				day   = DateUtil.getDay(endDate);
			}

			DatePickerDialog dlg = new DatePickerDialog(context, this, year, month, day);
			dlg.show();
		}else if (view.getId() == R.id.btnCancel){
			setResult(RESULT_CANCELED);
			finish();
		}else if (view.getId() == R.id.btnOK){
			String content = editContent.getText().toString();
			if (content == null || content.length() == 0)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Notice");
				builder.setMessage(R.string.message_enter_content);
				builder.setPositiveButton("OK", null);
				builder.create().show();

				return;
			}
			
			content = content.replaceAll("\n", "\\\\n");
			
			String date1 = DateUtil.getDateFormatString(startDate, "yyyy-MM-dd");
			String date2 = (endDate != null ? DateUtil.getDateFormatString(endDate, "yyyy-MM-dd") : null);

			if (careerEntity == null){
				CareerHandleTask task = new CareerHandleTask(CareerHandleTask.ACTION_SAVE, new CareerHandleTaskListener() {
	
					@Override
					public void onComplete(CareerHandleTask task, boolean result){
						if(result){
							setResult(RESULT_OK);
							finish();
						}
						
					}
				});

				task.execute(null, content, date1, date2);
			}else{
				CareerHandleTask task = new CareerHandleTask(CareerHandleTask.ACTION_SAVE, new CareerHandleTaskListener() {
					
					@Override
					public void onComplete(CareerHandleTask task, boolean result){
						if(result){
							setResult(1);
							finish();
						}
						
					}
				});

				task.execute(careerEntity.index, content, date1, date2);
			}
		}
		
	}


	public void showCareer(CareerEntity entity){
		String date1 = entity.startTime.substring(0, 10);
		startDate = DateUtil.createDateFromString(date1, "yyyy.MM.dd");
		startDateLabel.setText(date1);
		
		if (entity.endTime != null)
		{
			String date2 = entity.endTime.substring(0, 10);
			endDate   = DateUtil.createDateFromString(date2, "yyyy.MM.dd");
			endDateLabel.setText(date2);
		}
		else
		{
			endDateLabel.setText("Present");
		}
		
		editContent.setText(entity.career);
		
	}

	@Override
	public void onDateSet(DatePicker arg0, int year, int monthOfYear, int dayOfMonth){
		if (activeDateField == 1)
		{
			startDate = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(startDate, "yyyy.MM.dd");
			
			startDateLabel.setText(dateString);
		}
		else if (activeDateField == 2)
		{
			endDate = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
			String dateString = DateUtil.getDateFormatString(endDate, "yyyy.MM.dd");
			
			endDateLabel.setText(dateString);
		}
	}
	
	
}
