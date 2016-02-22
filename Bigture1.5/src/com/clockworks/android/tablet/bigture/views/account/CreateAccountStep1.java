package com.clockworks.android.tablet.bigture.views.account;

import java.util.Locale;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CreateAccountStep1 extends RelativeLayout implements View.OnClickListener{
	private Context context;
	private Handler handler;	
	private String language = "EN";
	
	private Button btnKor;
	private Button btnEng;
	private WebView licenseView;
	private WebView privacyView;
	private ImageButton btnAgree;
	private ImageButton btnPrivacy;
	private boolean agreed = false;
	private boolean agreed2 = false;
	
	public CreateAccountStep1(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(Handler handler){
		this.handler = handler;
	}
	
	@Override
	protected void onFinishInflate(){
		btnKor = (Button)findViewById(R.id.btnKor);
		btnKor.setOnClickListener(this);
		
		btnEng = (Button)findViewById(R.id.btnEng);
		btnEng.setOnClickListener(this);
		
		licenseView = (WebView)findViewById(R.id.viewLicense);
		privacyView = (WebView)findViewById(R.id.viewPrivacy);
		btnAgree = (ImageButton)findViewById(R.id.btnAgree);
		btnAgree.setOnClickListener(this);
		btnPrivacy = (ImageButton)findViewById(R.id.btnPrivacy);
		btnPrivacy.setOnClickListener(this);
		
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		
		if(language.equals("ko")){
			language = "KR";
			licenseView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "terms_of_service_kr.html");
			privacyView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "privacy_policy_kr.html");
		}else{
			language = "EN";
			licenseView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "terms_of_service_en.html");
			privacyView.loadUrl(ServerStaticVariable.TERMS_BASE_URL + "privacy_policy_en.html");
		}
		
		findViewById(R.id.btnEng).setSelected(true);
		
		
		Button btnNext = (Button)findViewById(R.id.btnNext1);
		btnNext.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnEng){
			language = "EN";
			//licenseView.setText(getResources().getString(R.string.agree_en));
			findViewById(R.id.btnEng).setSelected(true);
			findViewById(R.id.btnKor).setSelected(false);
		}else if(v.getId() == R.id.btnKor){
			language = "KR";
			//licenseView.setText(getResources().getString(R.string.agree_kr));
			findViewById(R.id.btnEng).setSelected(false);
			findViewById(R.id.btnKor).setSelected(true);
		}else if(v.getId() == R.id.btnNext1){
			if(agreed == false || agreed2 == false){
				AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountStep1.this.context);
				builder.setMessage("You must agreed");
				builder.setTitle("Error");
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				return;
			}else{
				Message msg = handler.obtainMessage(1);
				msg.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_STEP2;	//createAccount Step 2
				handler.sendMessage(msg);
			}
			
		}else if(v.getId() == R.id.btnAgree){
			agreed = !agreed;
			btnAgree.setSelected(!btnAgree.isSelected());
		}else if(v.getId() == R.id.btnPrivacy){
			agreed2 = !agreed2;
			btnPrivacy.setSelected(!btnPrivacy.isSelected());
		}
		
	}
	
	
}
