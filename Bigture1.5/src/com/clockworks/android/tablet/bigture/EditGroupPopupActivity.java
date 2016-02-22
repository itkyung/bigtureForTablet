package com.clockworks.android.tablet.bigture;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditGroupPopupActivity extends Activity {
	private FriendGroupEntity groupEntity;
	private Context context;
	private ProgressDialog progressDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.context = this;
		//Bundle에서 데이타를 얻는다.
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null)
			groupEntity = bundle.getParcelable("groupEntity");
		
		setContentView(R.layout.popup_edit_group);
		
		TextView titleView = (TextView)findViewById(R.id.titleLabel);
		final EditText editGroupName = (EditText)findViewById(R.id.editGroupName);
		
		if(groupEntity == null){
			titleView.setText("Add a group");
			editGroupName.setHint("Create a Group name");
		}else{
			titleView.setText("Edit a group name");
			editGroupName.setHint("Edit a Group name");
			editGroupName.setText(groupEntity.groupName);
		}
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String groupName = editGroupName.getText().toString();
				if(groupName.length() == 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Confirm");
					builder.setMessage("Please enter group name");
					builder.setNegativeButton("OK", null);
					builder.create().show();
				}else{
					progressDlg = ProgressDialog.show(context, "", "Please Wait...", true);
					progressDlg.setCancelable(false);
					
					GroupHandleTask task = new GroupHandleTask();
					task.execute(groupEntity == null ? null : groupEntity.index,groupName);
				}
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
		
	}

	public void saveSuccess(){
		setResult(RESULT_OK);
		finish();
	}
	
	class GroupHandleTask extends AsyncTask<String, Void, Boolean>{
		
		
		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String id = params[0];
			String name = params[1];
			
			return FriendHandler.saveGroup(id, name);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDlg.dismiss();
			
			super.onPostExecute(result);
			if(result){
				saveSuccess();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Error occur!");
				builder.setNegativeButton("OK", null);
				builder.create().show();
			}
		}
		
		
		
	}

}
