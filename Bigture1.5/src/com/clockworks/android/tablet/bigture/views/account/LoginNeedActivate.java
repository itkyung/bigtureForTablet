package com.clockworks.android.tablet.bigture.views.account;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginNeedActivate extends LinearLayout {
	private Context context;
	private Handler hanlder;
	private AccountEntity account;
	
	public LoginNeedActivate(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(Handler handler,AccountEntity account){
		this.hanlder = handler;
		this.account = account;
		
		TextView textEmail = (TextView)findViewById(R.id.textEmail10);
		textEmail.setText(account.loginId);
		
	}

	@Override
	protected void onFinishInflate() {
		
		Button resendBtn = (Button)findViewById(R.id.btnResend);
		resendBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ResendTask task = new ResendTask();
				task.execute(account.index);
			}
		});
		
		
		super.onFinishInflate();
	}
	
	class ResendTask extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			
			LoginHandler.resendActivateLink(params[0]);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage("Resend request is completed!");
			builder.setNegativeButton("Ok", null);
			builder.create().show();
			
		}
		
	}
	
}
