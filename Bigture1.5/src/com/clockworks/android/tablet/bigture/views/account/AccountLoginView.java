package com.clockworks.android.tablet.bigture.views.account;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.MainActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.GCMManager;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.GCMServiceHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountLoginView extends RelativeLayout implements View.OnClickListener{
	private Context context;
	private Handler handler;	
	
	private EditText editEmail;
	private EditText editPasswd;
	
	private ImageButton btnKeepLoggedIn;
	private Button btnCreateAccount;
	private TextView labelIntroMsg;
	
	public AccountLoginView(Context context, AttributeSet attrs){
		super(context, attrs);
		
		this.context = context;
	}
	
	@Override
	protected void onFinishInflate(){
		editEmail  = (EditText)findViewById(R.id.editEmail);
		editPasswd = (EditText)findViewById(R.id.editPasswd);
		
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		
		btnKeepLoggedIn = (ImageButton)findViewById(R.id.btnKeepLogin);
		btnKeepLoggedIn.setOnClickListener(this);
		btnKeepLoggedIn.setSelected(true);
		
		TextView tvKeepLoggedIn = (TextView)findViewById(R.id.keepLoginLabel);
		tvKeepLoggedIn.setOnClickListener(this);
		
		
		TextView facebookLogin = (TextView)findViewById(R.id.loginWithFacebook);
		facebookLogin.setOnClickListener(this);
		
		TextView twitterLogin = (TextView)findViewById(R.id.loginWithTwitter);
		twitterLogin.setOnClickListener(this);
		
		btnCreateAccount = (Button)findViewById(R.id.createAccount);
		btnCreateAccount.setOnClickListener(this);
		
		labelIntroMsg = (TextView)findViewById(R.id.labelIntroMsg);
		labelIntroMsg.setText(getResources().getString(R.string.label_enter_email_pwd));
		labelIntroMsg.setTextColor(getResources().getColor(R.color.black));
		
		TextView forgotPassword = (TextView)findViewById(R.id.forgotPassword);
		forgotPassword.setOnClickListener(this);
		
		super.onFinishInflate();
	}
	
	public void initView(Handler handler)
	{
		this.handler = handler;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId()) {
		case R.id.btnKeepLogin:
		case R.id.keepLoginLabel:
			btnKeepLoggedIn.setSelected(!btnKeepLoggedIn.isSelected());
			break;
		case R.id.btnLogin:
			String loginId  = editEmail.getText().toString();
			String passwd = editPasswd.getText().toString();
			boolean keepLogin = btnKeepLoggedIn.isSelected();
			
			if (loginId == null || loginId.length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(AccountLoginView.this.context);
				builder.setTitle("Login Failed");
				builder.setMessage(getResources().getString(R.string.label_enter_email_pwd));
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				return;
			}
			
			if (passwd == null || passwd.length() == 0)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(AccountLoginView.this.context);
				builder.setTitle("Login Failed");
				builder.setMessage(getResources().getString(R.string.label_enter_email_pwd));
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				return;
			}
			WaitDialog.showWailtDialog(context, false);
			LoginTask task = new LoginTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,loginId, passwd, keepLogin);
			}else{
				task.execute(loginId, passwd, keepLogin);				
			}
			
			
			break;
		case R.id.loginWithFacebook:
			Message msg1 = handler.obtainMessage(2);
			handler.sendMessage(msg1);
			break;
		case R.id.loginWithTwitter:
			Message msgL = handler.obtainMessage(3);
			handler.sendMessage(msgL);
			break;
		case R.id.createAccount:
			
			Message msg = handler.obtainMessage(1);
			msg.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_STEP1;	//createAccount Step 1
			handler.sendMessage(msg);
			
			break;
		case R.id.forgotPassword:	
			Message msg2 = handler.obtainMessage(1);
			msg2.arg1 = BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_STEP1;	
			handler.sendMessage(msg2);
			
			break;
		default:
			break;
		}
		
	}
	
	class FriendDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			AccountEntity entity = AccountManager.getInstance().getAccountEntity();
			entity.likeYous = FriendHandler.getLikeYou(null,null,null);
			entity.friendGroups = FriendHandler.findGroups();
			entity.store(context);
			
			return null;
		}
		
		
	}
	
	
	class LoginTask extends AsyncTask<Object, Void, AccountEntity>{
		
		
		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected AccountEntity doInBackground(Object... arg0){
			String loginId = (String)arg0[0];
			String passwd = (String)arg0[1];
			boolean keepLogin = (Boolean)arg0[2];
			
			AccountEntity entity = LoginHandler.login(context,loginId, passwd, keepLogin);
			
			if (entity != null && entity.success){
				AccountManager.getInstance().setAccountEntity(entity);
				AccountManager.getInstance().setLogin(true);
				
				entity.store(context);
				
			}
			
			return entity;
		}

		@Override
		protected void onPostExecute(AccountEntity result){
			WaitDialog.hideWaitDialog();
			if (result.success){
				UpdateRegistTask taskU = new UpdateRegistTask();
				if(Build.VERSION.SDK_INT >= 11){
					taskU.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,result.index);
				}else{
					taskU.execute(result.index);				
				}
				
				
				FriendDataTask task = new FriendDataTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}else{
					task.execute();				
				}
				
				
				Message msg = handler.obtainMessage(ServerStaticVariable.LOGIN_COMPLETE, (Boolean)true);
				handler.sendMessage(msg);
			}else if (result.resultCode == ServerStaticVariable.ERROR_NOT_ACTIVE){
				//아직 Activate가 안된경우
				Message msg = handler.obtainMessage(1, BigtureEnvironment.ACCOUNT_POPUP_LOGIN_NEED_ACTIVATE, 0);
				msg.obj = result;
				handler.sendMessage(msg);
			}else if(result.resultCode == ServerStaticVariable.ERROR_USER_NOT_EXIST){
				labelIntroMsg.setText(getResources().getString(R.string.label_login_not_exist));
				labelIntroMsg.setTextColor(getResources().getColor(R.color.red));
				
			}else if(result.resultCode == ServerStaticVariable.ERROR_PASSWORD){
				labelIntroMsg.setText(getResources().getString(R.string.label_login_wrong_password));
				labelIntroMsg.setTextColor(getResources().getColor(R.color.red));
			}else{
				labelIntroMsg.setText(getResources().getString(R.string.label_login_not_exist));
				labelIntroMsg.setTextColor(getResources().getColor(R.color.red));

			}
			
			super.onPostExecute(result);
		}
	}
	
	class UpdateRegistTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String regId = GCMManager.getRegistrationId(context);
			
			if (regId == null)
				GCMManager.registerGCM(context);
			else
				GCMServiceHandler.updateGCMRegistrationId(params[0], regId);
			
			return true;
		}
	}
}
