package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;
import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.FindUserPopupActivity.FindUserAdapter;
import com.clockworks.android.tablet.bigture.FindUserPopupActivity.ViewHolder;
import com.clockworks.android.tablet.bigture.adapter.user.FindSimpleUserAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ExpertHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.FriendHandleTask;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FindExpertsPopupActivity extends Activity {
	private ListView listView;
	private EditText keywordEdit;
	private BitmapFactory.Options options;
	private FindExpertsTask findTask;
	private int currentPage;
	
	private Context context;
	private FindSimpleUserAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		context = this;
		currentPage = 1;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		
		setContentView(R.layout.popup_find_experts);
		
		this.keywordEdit = (EditText)findViewById(R.id.editKeyword);
		keywordEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				currentPage = 1;
				if (s.toString().length() > 1){
					searchUser(s.toString());
				}else{
					searchUser(null);
					//listView.setAdapter(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});
		
		findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener(){	
			@Override
			public void onClick(View v){
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
		
		this.listView = (ListView)findViewById(R.id.listView1);
		this.adapter = new FindSimpleUserAdapter(this);
		this.listView.setAdapter(adapter);
		
		searchUser(null);
	}

	private void searchUser(String keyword){

		if (findTask != null)
			findTask.cancelTask();
		
		findTask = new FindExpertsTask();
		findTask.execute(keyword);
		
	}
	
	public void refreshData(ArrayList<SimpleUserEntity> users){
		this.adapter.setUsers(users);
		this.adapter.notifyDataSetChanged();
	}
	
	private class FindExpertsTask extends AsyncTask<String, Void, ArrayList<SimpleUserEntity>>{
		boolean canceled = false;
		
		@Override
		protected ArrayList<SimpleUserEntity> doInBackground(String... params) {
			String keyword = params[0];
			
			return ExpertHandler.findByKeyword(keyword);
		}

		@Override
		protected void onPostExecute(ArrayList<SimpleUserEntity> result) {
			
			super.onPostExecute(result);
			if(!canceled)
				refreshData(result);
		}
		
		public void cancelTask(){
			this.cancel(true);
			canceled = true;
		}
	}
	
	
}
