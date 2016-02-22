package com.clockworks.android.tablet.bigture.views.account;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ForgotPasswordFinish extends LinearLayout {
	private Context context;
	private Handler handler;
	private String loginId;
	
	public ForgotPasswordFinish(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initView(final Handler handler,String loginId){
		this.handler = handler;
		this.loginId = loginId;
		
		if(this.loginId != null){
			View view = (View)findViewById(R.id.successContainer);
			view.setVisibility(View.VISIBLE);
			View view2 = (View)findViewById(R.id.failContainer);
			view2.setVisibility(View.GONE);
			
			TextView textEmail = (TextView)findViewById(R.id.textEmail);
			textEmail.setText(loginId);
		
		}else{
			View view = (View)findViewById(R.id.successContainer);
			view.setVisibility(View.GONE);
			View view2 = (View)findViewById(R.id.failContainer);
			view2.setVisibility(View.VISIBLE);
			
			Button btn = (Button)findViewById(R.id.btnGoCreate);
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Message message = handler.obtainMessage(1);
					message.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_STEP1;
					handler.sendMessage(message);
				}
			});
			
		}
		
	}

	@Override
	protected void onFinishInflate() {
		
		
		
		
		
		super.onFinishInflate();
	}

	
	
	
}
