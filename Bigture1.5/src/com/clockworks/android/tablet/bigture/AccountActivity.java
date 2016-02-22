package com.clockworks.android.tablet.bigture;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleEntity;
import com.clockworks.android.tablet.bigture.views.account.AccountLoginView;
import com.clockworks.android.tablet.bigture.views.account.CreateAccountFinish;
import com.clockworks.android.tablet.bigture.views.account.CreateAccountStep1;
import com.clockworks.android.tablet.bigture.views.account.CreateAccountStep2;
import com.clockworks.android.tablet.bigture.views.account.FacebookLoginHelper;
import com.clockworks.android.tablet.bigture.views.account.ForgotPasswordFinish;
import com.clockworks.android.tablet.bigture.views.account.ForgotPasswordStep1;
import com.clockworks.android.tablet.bigture.views.account.LoginNeedActivate;
import com.clockworks.android.tablet.bigture.views.account.LoginTwitterView;
import com.clockworks.android.tablet.bigture.views.dialog.CountryListDialog;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 팝업으로 나오는 로그인 및 계정 생성 수정 Activity 
 * @author bizwave
 *
 */
public class AccountActivity extends AbstractBigtureActivity implements CountryListDialog.OnSelectCountryListener{
	private AccountMessageHandler accountMessageHandler = new AccountMessageHandler(this);
	
	private ViewGroup viewContainer;
	private Button btnCancel;
	private Button btnBack;
	private TextView titleLabel;
	private View currentView;
	FacebookLoginHelper facebookHelper;
	int viewIndex = BigtureEnvironment.ACCOUNT_POPUP_LOGIN;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.popup_account);
		
		viewContainer = (ViewGroup)findViewById(R.id.viewContainer);
		
		AccountLoginView view = (AccountLoginView)viewContainer.getChildAt(0);
		view.initView(accountMessageHandler);
		currentView = view;
		
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (viewIndex == BigtureEnvironment.ACCOUNT_POPUP_LOGIN_NEED_ACTIVATE ||
						viewIndex == BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_STEP1 ||
						viewIndex == BigtureEnvironment.ACCOUNT_POPUP_CREATE_FROM_SNS){
					showChildView(BigtureEnvironment.ACCOUNT_POPUP_LOGIN, null);
				}else{
					showChildView(viewIndex-1, null);
				}
			}
		});
		btnBack.setVisibility(View.INVISIBLE);
	}


	/**
	 * 내부에 존재하는 Inner view를 다른것으로 바꾼다.
	 * @param viewIndex
	 * @param obj
	 */
	public void showChildView(int _viewIndex, Object obj){
		DisplayMetrics metrics = BigtureEnvironment.getDisplayMetrics(this);
		float density = metrics.density;
		int width  = (int)(density * 540.f);
		int height = (int)(density * 626.f);
		
		viewContainer.removeAllViews();
		LayoutInflater inflater = this.getLayoutInflater();// LayoutInflater.from(context);

		btnBack.setVisibility(View.VISIBLE);
		
		switch (_viewIndex) {
		case BigtureEnvironment.ACCOUNT_POPUP_LOGIN:
			//Login view
			titleLabel.setText(getResources().getString(R.string.account_btn_login));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.INVISIBLE);
			AccountLoginView view = (AccountLoginView)inflater.inflate(R.layout.view_account_login, null);
			view.initView(accountMessageHandler);
			viewContainer.addView(view);
			view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = view;
			
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_CREATE_STEP1:
			titleLabel.setText(getResources().getString(R.string.account_menu_create));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			CreateAccountStep1 cView = (CreateAccountStep1)inflater.inflate(R.layout.view_account_create_step1, null);
			cView.initView(accountMessageHandler);
			viewContainer.addView(cView);
			cView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = cView;
			break;
		
		case BigtureEnvironment.ACCOUNT_POPUP_CREATE_STEP2:
			titleLabel.setText(getResources().getString(R.string.account_menu_create));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			CreateAccountStep2 cView2 = (CreateAccountStep2)inflater.inflate(R.layout.view_account_create_step2, null);
			cView2.initView(accountMessageHandler,false,null);
			viewContainer.addView(cView2);
			cView2.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = cView2;
			
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_CREATE_FROM_SNS:	
			titleLabel.setText(getResources().getString(R.string.account_menu_create));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			CreateAccountStep2 cViewSNS = (CreateAccountStep2)inflater.inflate(R.layout.view_account_create_step2, null);
			cViewSNS.initView(accountMessageHandler,true,(SimpleEntity)obj);
			viewContainer.addView(cViewSNS);
			cViewSNS.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = cViewSNS;
			
			
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_CREATE_COMPLETE:
			titleLabel.setText(getResources().getString(R.string.account_menu_create));
			btnCancel.setText(getResources().getString(R.string.common_btn_close));
			btnBack.setVisibility(View.INVISIBLE);
			CreateAccountFinish fView = (CreateAccountFinish)inflater.inflate(R.layout.view_account_create_finish, null);
			fView.initView(accountMessageHandler);
			viewContainer.addView(fView);
			fView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = fView;
			
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_LOGIN_NEED_ACTIVATE :
			titleLabel.setText(getResources().getString(R.string.account_btn_login));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			LoginNeedActivate nView = (LoginNeedActivate)inflater.inflate(R.layout.view_account_login_need_activate, null);
			nView.initView(accountMessageHandler,(AccountEntity)obj);
			viewContainer.addView(nView);
			nView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = nView;
			
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_STEP1:	
			titleLabel.setText(getResources().getString(R.string.label_forgot_password));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			ForgotPasswordStep1 pView = (ForgotPasswordStep1)inflater.inflate(R.layout.view_account_forgot_step1, null);
			pView.initView(accountMessageHandler);
			viewContainer.addView(pView);
			pView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = pView;
			break;
		case BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_FINISH:
			titleLabel.setText(getResources().getString(R.string.label_forgot_password));
			btnCancel.setText(getResources().getString(R.string.common_btn_cancel));
			btnBack.setVisibility(View.VISIBLE);
			ForgotPasswordFinish pfView = (ForgotPasswordFinish)inflater.inflate(R.layout.view_account_forgot_finish, null);
			if(obj == null){
				pfView.initView(accountMessageHandler,null);
			}else{
				pfView.initView(accountMessageHandler, (String)obj);
			}
			viewContainer.addView(pfView);
			pfView.setLayoutParams(new LinearLayout.LayoutParams(width, height));
			currentView = pfView;
			break;
			
			
		default:
			break;
		}
		this.viewIndex = _viewIndex;
	}
	
	public void loginWithFacebook(){
		if(this.facebookHelper == null){
			facebookHelper = new FacebookLoginHelper(this, accountMessageHandler);
		}
		facebookHelper.login();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(this.facebookHelper != null){
			this.facebookHelper.onActivityResult(requestCode, resultCode, data);
		}
	}


	public void loginWithTwitter(){
		LoginTwitterView view = new LoginTwitterView(this, accountMessageHandler);
		viewContainer.removeAllViews();
		viewContainer.addView(view);
	}
	
	public void showCountryDialog(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		clearCountryDialog(ft,false);
		
		CountryListDialog newFragment = CountryListDialog.newInstance();
		newFragment.show(ft, "countryDialog");
		
	}
	
	@Override
	public void onCountrySelected(CountryEntity country) {
		
		if(currentView instanceof CreateAccountStep2){
			((CreateAccountStep2)currentView).selectCountry(country);
		}
		//Dialog를 닫는다.
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		clearCountryDialog(ft,true);
		
	}

	private void clearCountryDialog(FragmentTransaction ft,boolean dismiss){
		Fragment prev = getSupportFragmentManager().findFragmentByTag("countryDialog");
		if(prev != null){
			if(dismiss){
				DialogFragment df = (DialogFragment)prev;
				df.dismiss();
			}
			ft.remove(prev);
		}
		ft.addToBackStack(null);
	}


	static class AccountMessageHandler extends Handler 
	{
		AccountActivity activity;
		public AccountMessageHandler(AccountActivity activity)
		{
			this.activity = activity;
		}
		
		@Override
		public void handleMessage(Message msg)
		{
			// navigation
			if (msg.what == 1){
				activity.showChildView(msg.arg1, msg.obj);
			}else if (msg.what == 2){
				activity.loginWithFacebook();
			}
			else if (msg.what == 3){
				activity.loginWithTwitter();
			}
			else if (msg.what == ServerStaticVariable.LOGIN_COMPLETE) // Login success
			{
				Boolean result = (Boolean)msg.obj;
				if (result.booleanValue() == true){							
					activity.setResult(Activity.RESULT_OK);
					activity.finish();
				}
			}else if(msg.what == 200){ //Show Country Dialog
				activity.showCountryDialog();
			}
			
			super.handleMessage(msg);
		}
	}
}
