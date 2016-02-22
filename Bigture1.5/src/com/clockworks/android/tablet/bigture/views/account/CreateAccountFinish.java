package com.clockworks.android.tablet.bigture.views.account;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateAccountFinish extends LinearLayout implements View.OnClickListener{
	private Context context;
	private Handler handler;
	
	public CreateAccountFinish(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(Handler handler){
		this.handler = handler;
	}

	@Override
	protected void onFinishInflate(){
		Button btnResend = (Button)findViewById(R.id.btnResend);
		btnResend.setOnClickListener(this);
		
		Button btnGoLogin = (Button)findViewById(R.id.btnGoLogin);
		btnGoLogin.setOnClickListener(this);
		
		TextView textEmail = (TextView)findViewById(R.id.textEmail);
		AccountEntity account = new AccountEntity();
		account.load(context);
		textEmail.setText(account.loginId);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnResend){
			ResendTask task = new ResendTask();
			task.execute(this.context);
		}else if(v.getId() == R.id.btnGoLogin){
			Message message = handler.obtainMessage(1);
			message.arg1 = BigtureEnvironment.ACCOUNT_POPUP_LOGIN;
			handler.sendMessage(message);
		}
		
	}
	
	class ResendTask extends AsyncTask<Context, Void, Void>{

		@Override
		protected Void doInBackground(Context... params) {
			AccountEntity entity = new AccountEntity();
			entity.load(params[0]);
			
			LoginHandler.resendActivateLink(entity.index);
			
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
