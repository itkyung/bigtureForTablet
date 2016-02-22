package com.clockworks.android.tablet.bigture.views.account;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleEntity;
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

public class ForgotPasswordStep1 extends LinearLayout {
	private Context context;
	private Handler handler;
	
	public ForgotPasswordStep1(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(Handler handler){
		this.handler = handler;
	}

	@Override
	protected void onFinishInflate() {
		
		final TextView email = (TextView)findViewById(R.id.editBigtureId);
		
		Button btnSend = (Button)findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(email.getText() == null || email.getText().length() == 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Email Required");
					builder.setMessage("Please input your E-mail.\nAnd try again.");
					builder.setNegativeButton("Ok", null);
					builder.create().show();
				}else{
					PasswordTask task = new PasswordTask();
					task.execute(email.getText().toString());
				}
			}
		});
		
		super.onFinishInflate();
	}

	class PasswordTask extends AsyncTask<String, Void, SimpleEntity>{

		@Override
		protected SimpleEntity doInBackground(String... params) {
			String loginId = params[0];
			
			SimpleEntity entity = LoginHandler.resetPassword(loginId);
			
			return entity;
		}

		@Override
		protected void onPostExecute(SimpleEntity result) {
			if(result.success){
				Message message = handler.obtainMessage(1);
				message.arg1 = BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_FINISH;
				
				message.obj = result.data;
				handler.sendMessage(message);
			}else{
				Message message = handler.obtainMessage(1);
				message.arg1 = BigtureEnvironment.ACCOUNT_POPUP_FORGOT_PASSWD_FINISH;
				//message.obj = result.data; //에러인 경우에는 obj를 보내지 않는다.
				handler.sendMessage(message);
			}
			super.onPostExecute(result);
		}
		
		
		
	}
	
}
