package com.clockworks.android.tablet.bigture.fragment;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotiSettingEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.MainHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NotificationFragment extends Fragment {
	private Context context;
	private NotiSettingEntity setting;
	
	private Button talkBtn;
	private Button likeBtn;
	private Button pickBtn;
	private Button contestBtn;
	private Button postcardBtn;
	private Button myClassBtn;
	private Button classBtn;
	private Button storyBtn;
	private Button pushBtn;
	
	private boolean modified;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.modified = false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_nofitications, container,false);
		
		this.talkBtn = (Button)view.findViewById(R.id.talkBtn);
		view.findViewById(R.id.talkFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.talkCommentPush = !setting.talkCommentPush;
					talkBtn.setSelected(setting.talkCommentPush);
					modified = true;
				}	
			}
		});
		
		this.likeBtn = (Button)view.findViewById(R.id.likeBtn);
		view.findViewById(R.id.likeFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.likePush = !setting.likePush;
					likeBtn.setSelected(setting.likePush);
					modified = true;
				}	
			}
		});
		
		this.pickBtn = (Button)view.findViewById(R.id.pickBtn);
		view.findViewById(R.id.pickFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.picMePush = !setting.picMePush;
					pickBtn.setSelected(setting.picMePush);
					modified = true;
				}	
			}
		});
		
		this.contestBtn = (Button)view.findViewById(R.id.contestBtn);
		view.findViewById(R.id.contestFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.contestPush = !setting.contestPush;
					contestBtn.setSelected(setting.contestPush);
					modified = true;
				}	
			}
		});
		
		this.postcardBtn = (Button)view.findViewById(R.id.postcardBtn);
		view.findViewById(R.id.postcardFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.cardPush = !setting.cardPush;
					postcardBtn.setSelected(setting.cardPush);
					modified = true;
				}	
			}
		});
		
		this.myClassBtn = (Button)view.findViewById(R.id.myClassBtn);
		view.findViewById(R.id.myClassFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.myClassPush = !setting.myClassPush;
					myClassBtn.setSelected(setting.myClassPush);
					modified = true;
				}	
			}
		});
		
		this.classBtn = (Button)view.findViewById(R.id.classBtn);
		view.findViewById(R.id.classFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.classPush = !setting.classPush;
					classBtn.setSelected(setting.classPush);
					modified = true;
				}	
			}
		});
		
		this.storyBtn = (Button)view.findViewById(R.id.storyBtn);
		view.findViewById(R.id.storyFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.storyPush = !setting.storyPush;
					storyBtn.setSelected(setting.storyPush);
					modified = true;
				}	
			}
		});
		
		this.pushBtn = (Button)view.findViewById(R.id.pushBtn);
		view.findViewById(R.id.pushFrame).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(setting != null){
					setting.pushAlert = !setting.pushAlert;
					pushBtn.setSelected(setting.pushAlert);
					modified = true;
					
					SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("Intro", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("pushAlert", setting.pushAlert);
					editor.commit();
				}
			}
		});
		
		
		
		new NotificationSettingTask().execute();
		return view;
	}

	public void updateView(NotiSettingEntity entity){
		this.setting = entity;
		talkBtn.setSelected(setting.talkCommentPush);
		likeBtn.setSelected(setting.likePush);
		pickBtn.setSelected(setting.picMePush);
		contestBtn.setSelected(setting.contestPush);
		postcardBtn.setSelected(setting.cardPush);
		myClassBtn.setSelected(setting.myClassPush);
		classBtn.setSelected(setting.classPush);
		storyBtn.setSelected(setting.storyPush);
		pushBtn.setSelected(setting.pushAlert);
		
	}
	
	public NotiSettingEntity getSetting(){
		return this.setting;
	}
	
	class NotificationSettingTask extends AsyncTask<Void, Void, NotiSettingEntity>{

		@Override
		protected NotiSettingEntity doInBackground(Void... params) {
			return MainHandler.loadSettingInfo();
		}

		@Override
		protected void onPostExecute(NotiSettingEntity result) {
			super.onPostExecute(result);
			updateView(result);
		}
		
		
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}
	

}
