package com.clockworks.android.tablet.bigture.fragment.common;

import java.util.ArrayList;
import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.GCMManager;
import com.clockworks.android.tablet.bigture.serverInterface.entities.GCMessage;








import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class TitleBar extends Fragment {

	TextView textTitle;
	TextView textMsgCount;
	
	ImageView imageHome;
	ImageView imageBack;
	ImageView imageDraw;
	ImageView imageVline1;
	ImageView imageVline;
	ImageView imageVline2;
	
	
	TitleBarListener listener;

	BroadcastReceiver receiver;
	List<GCMessage> noticeList;
	
	PopupWindow noticePopup;
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		this.listener = (TitleBarListener)activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view =  inflater.inflate(R.layout.fragment_title_bar, container,false);
		initView(view);
		return view;
	}
	
	public void initView(final View view){
		noticeList = GCMManager.loadMessage(getActivity());
		
		if (noticeList == null)
			noticeList = new ArrayList<GCMessage>();
		
		
		textTitle = (TextView)view.findViewById(R.id.textTitle);
		textMsgCount = (TextView)view.findViewById(R.id.textMsgCount);
		textMsgCount.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				//TODO noticePopup을 처리한다.
				
				
			}
			
		});
		
		imageHome = (ImageView)view.findViewById(R.id.imageHome);
		imageHome.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				listener.onMainButtonClicked(TitleBar.this);
								
			}
		});
		
		imageBack = (ImageView)view.findViewById(R.id.imageBack);
		imageBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				listener.onBackButtonClicked(TitleBar.this);
			}
		});
		
		imageDraw = (ImageView)view.findViewById(R.id.imageDraw);
		imageDraw.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				listener.onDrawButtonClicked(TitleBar.this);

			}
		});
		
		imageVline = (ImageView)view.findViewById(R.id.imageVline);
		imageVline1 = (ImageView)view.findViewById(R.id.imageView1);
		imageVline2 = (ImageView)view.findViewById(R.id.imageView2);
		
		textMsgCount.setText("" + noticeList.size());
		
		if (noticeList.size() == 0)
			textMsgCount.setVisibility(View.GONE);
		else
			textMsgCount.setVisibility(View.VISIBLE);
	}

	public void setTitle(String title){
		textTitle.setText(title);
	}
	
	public void showDrawButton(){
		imageDraw.setVisibility(View.VISIBLE);
		imageVline.setVisibility(View.VISIBLE);
	}
	
	public void hideDrawButton(){
		imageDraw.setVisibility(View.GONE);
		imageVline.setVisibility(View.INVISIBLE);
	}
	
	public void hideHomeButton(){
		imageHome.setVisibility(View.GONE);
		imageVline1.setVisibility(View.GONE);
	}
	
	public void hideBackButton(){
		imageBack.setVisibility(View.GONE);
		imageVline2.setVisibility(View.GONE);
	}

	public interface TitleBarListener
	{
		void onBackButtonClicked(TitleBar titleBar);
		void onDrawButtonClicked(TitleBar titleBar);
		void onMessageClicked(TitleBar titleBar);
		void onMainButtonClicked(TitleBar titleBar);
	}


	@Override
	public void onPause() {
		getActivity().unregisterReceiver(receiver);
		super.onPause();
	}

	@Override
	public void onResume() {
		if (textMsgCount != null){
			noticeList = GCMManager.loadMessage(getActivity());
			if (noticeList == null)
				noticeList = new ArrayList<GCMessage>();

			textMsgCount.setText("" + noticeList.size());
			
			if (noticeList.size() == 0)
				textMsgCount.setVisibility(View.GONE);
			else
				textMsgCount.setVisibility(View.VISIBLE);
		}
		
		IntentFilter filter = new IntentFilter("com.clockworks.android.bigture.GCM_RECEIVE");
		filter.setPriority(99999999);
		
		receiver = new MyBroadcastReceiver();
		getActivity().registerReceiver(receiver, filter);
		super.onResume();
	}
	
	class MyBroadcastReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			Bundle bundle = intent.getExtras();
			
			GCMessage msg = GCMessage.newInstance(bundle);
			
			if (msg != null)
			{
				noticeList.add(0, msg);
				GCMManager.saveMessage(context, noticeList);
				
				textMsgCount.setText("" + noticeList.size());
				textMsgCount.setVisibility(View.VISIBLE);
				
				Toast.makeText(context, "You have a new message from Bigture", Toast.LENGTH_LONG).show();
			}
			
			abortBroadcast();
		}
	}
	
}
