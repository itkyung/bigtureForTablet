package com.clockworks.android.tablet.bigture.views.account;

import java.util.Date;
import java.util.Locale;

import twitter4j.examples.friendsandfollowers.GetFriendsIDs;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.GCMManager;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.GCMServiceHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.dialog.CountryListDialog;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;


import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



public class CreateAccountStep2 extends FrameLayout implements View.OnClickListener, DatePickerDialog.OnDateSetListener, 
	View.OnFocusChangeListener{
	private Context context;
	private Handler handler;
	private boolean useSNS;
	
	private AccountEntity accountEntity;
	private SimpleEntity simpleEntity;
	private Date   birthday;

	private Button btnFemale;
	private Button btnMale;
	
	private TextView birthdayLabel;
	private ProgressDialog progressDlg;
	private EditText editPasswd1;
	private EditText editPasswd2;
	private Button btnDone;
	private EditText editNickName;
	private TextView countrySelectText;
	
	private boolean canDone;
	private CountryEntity selectedCountry;
	
	public CreateAccountStep2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.useSNS = false;
		this.canDone = false;
	}

	public void initView(Handler handler,boolean useSns,SimpleEntity simpleEntity){
		this.handler = handler;
		this.useSNS = useSns;
		this.simpleEntity = simpleEntity;
		
		if(useSNS){
			TextView passwordBoxTitle = (TextView)findViewById(R.id.passwordBoxTitle);
			LinearLayout pwdBox = (LinearLayout)findViewById(R.id.passwordBox);
			
			passwordBoxTitle.setVisibility(View.GONE);
			pwdBox.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onFinishInflate(){	
		btnMale = (Button)findViewById(R.id.btnMale);
		btnMale.setOnClickListener(this);

		btnFemale = (Button)findViewById(R.id.btnFemale);
		btnFemale.setSelected(true);
		btnFemale.setOnClickListener(this);
		
		birthdayLabel = (TextView)findViewById(R.id.birthdayLabel);
		birthdayLabel.setOnClickListener(this);
		
		ImageView imageView = (ImageView)findViewById(R.id.imagePickupBirthday);
		imageView.setOnClickListener(this);
		
		EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
		editBigtureId.setOnFocusChangeListener(this);
		
		editBigtureId.requestFocus();
		
		editPasswd1 = (EditText)findViewById(R.id.editPassword1);
		editPasswd1.setOnFocusChangeListener(this);
		
		editPasswd2 = (EditText)findViewById(R.id.editPassword2);
		editPasswd2.setOnFocusChangeListener(this);
		
		editNickName = (EditText)findViewById(R.id.editNickname);
		editNickName.setOnFocusChangeListener(this);
		
		countrySelectText = (TextView)findViewById(R.id.countrySelect);
		countrySelectText.setOnClickListener(this);
		
		ImageView countryImg = (ImageView)findViewById(R.id.pickupCountry);
		countryImg.setOnClickListener(this);
		
		btnDone = (Button)findViewById(R.id.btnDone);
		btnDone.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				
				String passwd1 = editPasswd1.getText().toString();
				
				String nickName = editNickName.getText().toString();
				
				EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
				String bigtureId = editBigtureId.getText().toString();
				String socialId = null;
				String socialType = null;
				
				if (useSNS){
					socialType = simpleEntity.socialType;
					socialId = simpleEntity.socialId;
				}
				
				String gender = "M";
				if (btnFemale.isSelected())
					gender = "F";
				
				String birthDay = DateUtil.getDateFormatString(birthday, "yyyy-MM-dd");
				
				progressDlg = ProgressDialog.show(context, "", "Please Wait...", true);
				progressDlg.setCancelable(false);

				AccountRegisterTask task = new AccountRegisterTask();
				task.execute(socialType,socialId, bigtureId, passwd1, nickName, birthDay, gender,selectedCountry.code);
			}
		});
		
	}
	

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(hasFocus == false){
			switch(v.getId()){
			case R.id.editPassword1:
			case R.id.editPassword2:
				(new Handler()).postDelayed(new Runnable() {
					@Override
					public void run() {
						validatePasswd(true, true);
						validateData();
					}
				}, 200);
				
				
				break;
			case R.id.editNickname:
			case R.id.editBigtureId:
				validateData();
				
				break;
			}
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		birthday = DateUtil.createDate(year, monthOfYear+1, dayOfMonth);
		String dt = DateUtil.getDateFormatString(birthday, "yyyy-MM-dd");
		
		birthdayLabel.setText(dt);
		
		validateData();
		editNickName.clearFocus();
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()){
		case R.id.btnFemale:
			btnFemale.setSelected(true);
			btnMale.setSelected(false);
			validateData();
			break;
		case R.id.btnMale:
			btnFemale.setSelected(false);
			btnMale.setSelected(true);
			validateData();
			break;
		case R.id.imagePickupBirthday:
		case R.id.birthdayLabel:
			int year  = DateUtil.getCurrentYear();
			int month = DateUtil.getCurrentMonth()-1;
			int day   = DateUtil.getCurrentDay();
			
			if (birthday != null)
			{
				year = DateUtil.getYear(birthday);
				month = DateUtil.getMonth(birthday) - 1;
				day   = DateUtil.getDay(birthday);
			}
			
			DatePickerDialog dlg = new DatePickerDialog(context, this, year, month, day);
			dlg.show();
			
			break;
		case R.id.countrySelect:
		case R.id.pickupCountry:
			showCountryDialog();
			break;
		}
		
	}
	
	/*
	 * 상위 Activity에게 country dialog를 띄우라고 handler를 통해 메시지를 보낸다.
	 */
	public void showCountryDialog(){
		Message message = handler.obtainMessage(200, null);
		handler.sendMessage(message);
	}
	
	/**
	 * 나라가 선택이 된경우.
	 * @param country
	 */
	public void selectCountry(CountryEntity country){
		this.selectedCountry = country;
		
		countrySelectText.setText(country.name);
		
		validateData();
		editNickName.clearFocus();
	}

	private boolean validatePasswd(boolean showAlert,boolean compareTest){
		String passwd = editPasswd1.getText().toString();
		String passwd2 = editPasswd2.getText().toString();
		
		if (passwd == null || passwd.length() < 6){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is too short.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPasswd1.requestFocus();
				
			}
			return false;
		}else if (passwd.length() > 20){
			canDone = false;
			if(showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is too long.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPasswd1.requestFocus();
			}
			return false;
		}else if(compareTest && passwd != null && passwd2 != null && passwd.length() > 0 &&
				passwd2.length() > 0 && passwd.equals(passwd2) == false){
			canDone = false;
			if (showAlert){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage("Password is not correct.");
				builder.setNegativeButton("OK", null);
				builder.create().show();
				
				editPasswd2.setText("");
				editPasswd2.requestFocus();
			}
			return false;
		}
		canDone = true;
		return true;
	}
	
	private void validateData(){
	
		
		String nickName = editNickName.getText().toString();
		
		EditText editBigtureId = (EditText)findViewById(R.id.editBigtureId);
		String bigtureId = editBigtureId.getText().toString();
		
		if(bigtureId == null || bigtureId.length() == 0){
			canDone = false;
			changeDoneBtn();
			return;
		}
		
		if (!useSNS){
			boolean result = validatePasswd(false,false);
			changeDoneBtn();
			if(result == false) return;
		}
		
		if (nickName == null || nickName.length() == 0){
			canDone = false;
			changeDoneBtn();
			return;
		}
		
		if (birthday == null){
			canDone = false;
			changeDoneBtn();
			return;
		}
		
		if (this.selectedCountry == null){
			canDone = false;
			changeDoneBtn();
			return;
		}
		
		canDone = true;
		changeDoneBtn();
	}
	
	private void changeDoneBtn(){
		if(canDone){
			btnDone.setBackgroundResource(R.drawable.selector_contents_button_red);
			btnDone.setTextColor(getResources().getColor(R.color.white));
			
			btnDone.setEnabled(true);
		}else{
			btnDone.setBackgroundResource(R.drawable.selector_popup_button);
			btnDone.setTextColor(getResources().getColor(R.color.white));
			btnDone.setEnabled(false);
		}
	}
	
	
	class AccountRegisterTask extends AsyncTask<String,Void,AccountEntity>{
		//socialType, socialId, bigtureId, passwd1, nickName, birthdayString, gender, countryCode
		private int errorCode;
		
		@Override
		protected AccountEntity doInBackground(String... params)
		{
			String socialType = params[0];
			String socialId    = params[1];
			String loginId     = params[2];
			String passwd    = params[3];
			String nickName = params[4];
			String birthday  = params[5];
			String gender  = params[6];
			String countryCode    = params[7];
			
			AccountEntity entity = LoginHandler.registAccount(socialType,socialId,loginId,passwd,nickName,birthday,gender,countryCode);
			if (entity.success){
				entity.keepLogin = false;
				entity.store(context);
				AccountManager.getInstance().setLogin(false);
				
			}else{
				errorCode = entity.resultCode;
				
			}
			
			return entity;
		}

		@Override
		protected void onPostExecute(AccountEntity result)
		{
			if (result != null && result.success){
				//SNS를 통한 생성이면 자동으로 로그인을 시도한다.
				if(useSNS){
					SnsLoginTask lTask = new SnsLoginTask();
					lTask.execute(result.loginId,result.socialId,result.socialType);
					
					
				}else{
					Message message = handler.obtainMessage(1);
					message.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_COMPLETE;
					handler.sendMessage(message);
				}
			}else{
				StringBuffer errorMsg = new StringBuffer("Error to register account,\n");
				if(errorCode == ServerStaticVariable.ERROR_DUPLICATE){
					errorMsg.append("Duplicate email address,\n");
				}else if(errorCode == ServerStaticVariable.ERROR_MANDATORY){
					errorMsg.append("Please input all field,\n");
				}
				errorMsg.append("Please try again");
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage(errorMsg.toString());
				builder.setNegativeButton("Ok", null);
				builder.create().show();
			}
			
			progressDlg.dismiss();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}
	}
	
	class SnsLoginTask extends AsyncTask<String,Void,AccountEntity>{
		@Override
		protected AccountEntity doInBackground(String... params){
			String loginId = params[0];
			String socialId = params[1];
			String socialType = params[2];
			
			AccountEntity entity =  LoginHandler.loginWithSocialAccount(socialType,socialId, loginId);
			
			if (entity.success){
				AccountManager man = AccountManager.getInstance();
				
				man.setAccountEntity(entity);
				man.setLogin(true);
				
				String regId = GCMManager.getRegistrationId(context);
				
				if (regId == null)
					GCMManager.registerGCM(context);
				else
					GCMServiceHandler.updateGCMRegistrationId(entity.index, regId);
				
				entity.likeYous = FriendHandler.getLikeYou(null,null,null);
				entity.friendGroups = FriendHandler.findGroups();
				entity.store(context);
			}

			return entity;
		}

		@Override
		protected void onPostExecute(AccountEntity result)
		{
			if (result.success){
				Message message = handler.obtainMessage(100);
				message.obj = (Boolean)true;
				handler.sendMessage(message);
				
				
			}
			super.onPostExecute(result);
		}
		
	}
	
}
