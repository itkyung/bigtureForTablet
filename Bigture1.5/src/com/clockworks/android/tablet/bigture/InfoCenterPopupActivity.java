package com.clockworks.android.tablet.bigture;



import java.util.ArrayList;
import java.util.Locale;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.NoticeAdapter;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NoticeEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.MainHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class InfoCenterPopupActivity extends AbstractBigtureActivity {
	Button changeLangBtn;
	private Button btnClose;
	private Context context;
	private int currentTab = 1;
	private ImageView tabImage1;
	private ImageView tabImage2;
	private ImageView tabImage3;
	private ImageView tabImage4;
	
	private View aboutBigtureFrame;
	private ListView noticeList;
	private View contactUsFrame;
	private View termsFrame;
	
	private String currentLang;
	private TextView bigtureDesc;
	private ImageView bigtureDescImg;
	private TextView contactMsg;
	private NoticeAdapter adapter;
	
	private static final int TAB_WHAT = 1;
	private static final int TAB_NOTICE = 2;
	private static final int TAB_TERMS = 3;
	private static final int TAB_CONTACT = 4;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.context = this;
		currentLang = "EN";
		
		setContentView(R.layout.popup_infocenter);
		
		this.aboutBigtureFrame = findViewById(R.id.aboutBigtureFrame);
		this.noticeList = (ListView)findViewById(R.id.noticeList);
		this.contactUsFrame = findViewById(R.id.contactUsFrame);
		this.termsFrame = findViewById(R.id.termsFrame);
		
		WebView termsWebView = (WebView)findViewById(R.id.termWebView);
		
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		
		if(language.equals("ko")){
			currentLang = "KR";
			termsWebView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "terms_of_service_kr.html");
			
		}else{
			currentLang = "EN";
			termsWebView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "terms_of_service_en.html");
			
		}
		
		
		this.adapter = new NoticeAdapter(context);
		this.noticeList.setAdapter(adapter);
		
		this.tabImage1 = (ImageView)findViewById(R.id.tabArrow1);
		this.tabImage2 = (ImageView)findViewById(R.id.tabArrow2);
		this.tabImage3 = (ImageView)findViewById(R.id.tabArrow3);
		this.tabImage4 = (ImageView)findViewById(R.id.tabArrow4);
		
		findViewById(R.id.btnBigture).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(TAB_WHAT);
				
			}
		});
		findViewById(R.id.btnNotice).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(TAB_NOTICE);
				
			}
		});
		

		findViewById(R.id.btnTerms).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(TAB_TERMS);
				
			}
		});
		
		
		findViewById(R.id.btnContact).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeTab(TAB_CONTACT);
				
			}
		});
		
		this.bigtureDesc = (TextView)findViewById(R.id.bigtureDesc);
		this.bigtureDescImg = (ImageView)findViewById(R.id.bigtureDescImg);
		this.contactMsg = (TextView)findViewById(R.id.contactMsg);
		
		this.btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		changeLangBtn = (Button)findViewById(R.id.btnChangeLang);
		changeLangBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				if(currentLang.equals("EN")){
//					//한국어로 바꾼다.
//					bigtureDesc.setText(getResources().getString(R.string.what_is_bigture_kor));
//					contactMsg.setText(getResources().getString(R.string.message_contact_kor));
//					bigtureDescImg.setImageDrawable(getResources().getDrawable(R.drawable.infocenter_what_image_kor));
//					
//					changeLangBtn.setText("English");
//					currentLang = "KR";
//				}else{
//					//영어로 바꾼다.
//					bigtureDesc.setText(getResources().getString(R.string.what_is_bigture_eng));
//					contactMsg.setText(getResources().getString(R.string.message_contact_eng));
//					bigtureDescImg.setImageDrawable(getResources().getDrawable(R.drawable.infocenter_what_image_eng));
//					
//					changeLangBtn.setText("한국어로 보기");
//					currentLang = "EN";
//				}
				getNotice();
			}
		});
		 
		findViewById(R.id.btnMail).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String email = context.getResources().getString(R.string.master_email);
				
				try{
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("plain/text");
					
					String[] to = { email };
					
					intent.putExtra(Intent.EXTRA_EMAIL, to);
					intent.putExtra(Intent.EXTRA_SUBJECT, "Subject here");
					intent.putExtra(Intent.EXTRA_TEXT, "Message here");
					
					((Activity)context).startActivity(intent);
				}catch(Exception e){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Notice");
					builder.setMessage("Fail to launch email application, Check your email client application");
					builder.setNegativeButton("OK", null);
					builder.create().show();
				}
				
				
			}
		});
		
		getNotice();
	}

	
	public void changeTab(int toTab){
		currentTab = toTab;
		
		if(toTab == TAB_WHAT){
			tabImage1.setVisibility(View.VISIBLE);
			tabImage2.setVisibility(View.GONE);
			tabImage3.setVisibility(View.GONE);
			tabImage4.setVisibility(View.GONE);
			
			aboutBigtureFrame.setVisibility(View.VISIBLE);
			noticeList.setVisibility(View.GONE);
			contactUsFrame.setVisibility(View.GONE);
			termsFrame.setVisibility(View.GONE);
			
		}else if(toTab == TAB_NOTICE){
			tabImage1.setVisibility(View.GONE);
			tabImage2.setVisibility(View.VISIBLE);
			tabImage3.setVisibility(View.GONE);
			tabImage4.setVisibility(View.GONE);
			
			aboutBigtureFrame.setVisibility(View.GONE);
			noticeList.setVisibility(View.VISIBLE);
			contactUsFrame.setVisibility(View.GONE);
			termsFrame.setVisibility(View.GONE);
		}else if(toTab == TAB_TERMS){
			tabImage1.setVisibility(View.GONE);
			tabImage2.setVisibility(View.GONE);
			tabImage3.setVisibility(View.VISIBLE);
			tabImage4.setVisibility(View.GONE);
			
			aboutBigtureFrame.setVisibility(View.GONE);
			noticeList.setVisibility(View.GONE);
			contactUsFrame.setVisibility(View.GONE);
			termsFrame.setVisibility(View.VISIBLE);
			
		}else if(toTab == TAB_CONTACT){
			tabImage1.setVisibility(View.GONE);
			tabImage2.setVisibility(View.GONE);
			tabImage3.setVisibility(View.GONE);
			tabImage4.setVisibility(View.VISIBLE);
			
			aboutBigtureFrame.setVisibility(View.GONE);
			noticeList.setVisibility(View.GONE);
			contactUsFrame.setVisibility(View.VISIBLE);
			termsFrame.setVisibility(View.GONE);
		}
		
		
	}
	
	private void getNotice(){
		new NoticeListTask().execute(currentLang);
	}
	
	class NoticeListTask extends AsyncTask<String, Void, ArrayList<NoticeEntity>>{

		@Override
		protected ArrayList<NoticeEntity> doInBackground(String... params) {
			return MainHandler.listNotice(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<NoticeEntity> result) {
			super.onPostExecute(result);
			
			adapter.setNotice(result);
			adapter.notifyDataSetChanged();
		}
		
		
	}
	
}
