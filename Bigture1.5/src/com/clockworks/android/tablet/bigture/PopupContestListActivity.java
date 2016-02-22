package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.contest.SimpleContestAdapter;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ContestHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class PopupContestListActivity extends Activity {
	private Context context;
	private ListView listView;
	private SimpleContestAdapter adapter;
	private String artworkId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup_list_contest);
		this.context = this;
		
		Intent intent = getIntent();
		this.artworkId = intent.getStringExtra("artworkId");
		
		listView = (ListView)findViewById(R.id.contestList);
		
		adapter = new SimpleContestAdapter(this);
		listView.setAdapter(adapter);
		
		Button btnCancel = (Button)findViewById(R.id.btnClose);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(adapter.getCount() > 0){
					
					ContestEntity entity = (ContestEntity)adapter.getItem(listView.getCheckedItemPosition());
					
					WaitDialog.showWailtDialog(context, false);
					RequestContestTask task = new RequestContestTask();
					task.execute(artworkId,entity.index);
					
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Confirm");
					builder.setMessage(R.string.label_no_contest);
					builder.setPositiveButton("Ok", null);
					builder.create().show();
				}
			}
		});
		
		getContestData();
	}
	
	private void getContestData(){
		ListContestTask task = new ListContestTask();
		task.execute();
		
	}
	
	class ListContestTask extends AsyncTask<Void, Void, ArrayList<ContestEntity>>{

		@Override
		protected ArrayList<ContestEntity> doInBackground(Void... params) {
			return ContestHandler.listActiveContest(1);
		}

		@Override
		protected void onPostExecute(ArrayList<ContestEntity> result) {
			super.onPostExecute(result);
			
			adapter.setContests(result);
			adapter.notifyDataSetChanged();
			if(result.size() > 0){
				//listView.requestFocusFromTouch();
				listView.setItemChecked(0, true);
				
				if(result.size() > 1){
					//listView의 높이를 조정한다.
					listView.getLayoutParams().height = 160;
					listView.setLayoutParams(listView.getLayoutParams());
				}
			}
		}
	}
	
	
	class RequestContestTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.requestToContest(params[0], params[1]);
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
			
			if(result){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Success");
				builder.setMessage(R.string.message_submit);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(RESULT_OK);
						finish();
					}
					
				});
				builder.create().show();
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("Ok", null);
				builder.create().show();
			}
		}
		
		
	}
	
	
}
