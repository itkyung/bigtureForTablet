package com.clockworks.android.tablet.bigture;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.fragment.MyProfileFragment;
import com.clockworks.android.tablet.bigture.fragment.NotificationFragment;
import com.clockworks.android.tablet.bigture.fragment.MyProfileFragment.CountrySelectListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SaveAccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.dialog.CountryListDialog;

import android.app.AlertDialog;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingPopupActivity extends AbstractBigtureActivity implements CountryListDialog.OnSelectCountryListener,CountrySelectListener {
	private TextView myProfile;
	private TextView notification;
	private ImageView tabImage1;
	private ImageView tabImage2;
	private Button btnLogout;
	private MyProfileFragment profileFragment;
	private NotificationFragment notiFragment;
	private Button btnClose;
	private Context context;
	private int currentTab = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		
		setContentView(R.layout.popup_settings);
		
		this.profileFragment = (MyProfileFragment)getSupportFragmentManager().findFragmentById(R.id.myProfileFragment);
		this.notiFragment = (NotificationFragment)getSupportFragmentManager().findFragmentById(R.id.notificationFragment);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.hide(notiFragment);
		ft.commit();
		
		
		this.myProfile = (TextView)findViewById(R.id.btnMyProfile);
		this.myProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentTab == 2){
					changeTab(1);
				}
			}
		});
		
		this.notification = (TextView)findViewById(R.id.btnNotification);
		this.notification.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentTab == 1){
					changeTab(2);
				}
			}
		});
		
		this.tabImage1 = (ImageView)findViewById(R.id.tabArrow1);
		this.tabImage2 = (ImageView)findViewById(R.id.tabArrow2);
		this.btnLogout = (Button)findViewById(R.id.btnLogout);
		this.btnLogout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				logout();
			}
		});
		
		this.btnClose = (Button)findViewById(R.id.btnClose);
		this.btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//설정의 내용을 서버에 저장을 한다.
				if(profileFragment.isModified() || notiFragment.isModified()){
					SaveAccountEntity data = profileFragment.validateAndGetData();
					if(data != null){
						data.setSetting(notiFragment.getSetting());
						
						new SaveProfileTask().execute(data);
					}
				}else{
					finish();
				}
			}
		});
		
		
		
	}
	
	public void changeTab(int toTab){
		if(toTab == 1){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.hide(notiFragment);
			ft.show(profileFragment);
			ft.commit();
			tabImage1.setVisibility(View.VISIBLE);
			tabImage2.setVisibility(View.GONE);
		}else{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.hide(profileFragment);
			ft.show(notiFragment);
			ft.commit();		
			tabImage1.setVisibility(View.GONE);
			tabImage2.setVisibility(View.VISIBLE);
		}
		
		this.currentTab = toTab;
	}
	
	
	
	@Override
	public void closeWindow() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void clickCountrySelect() {
		showCountryDialog();
	}

	public void showCountryDialog(){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		clearCountryDialog(ft,false);
		
		CountryListDialog newFragment = CountryListDialog.newInstance();
		newFragment.show(ft, "countryDialog");
		
	}
	
	@Override
	public void onCountrySelected(CountryEntity country) {
		
		profileFragment.selectCountry(country);
		
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


	public void logout(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Notice");
		builder.setMessage(R.string.Logout_from_Bigture);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				new AsyncTask<Void,Void,Void>(){

					@Override
					protected Void doInBackground(Void... params){
						LoginHandler.logout();
						return null;
					}

					@Override
					protected void onPostExecute(Void result){
						AccountManager manager = AccountManager.getInstance();
						manager.setAutoLogin(false);
						manager.setLogin(false);

						super.onPostExecute(result);
					}
					
				}.execute();				
			}
		});
		
		builder.setNegativeButton("Cancel", null);
		builder.create().show();
	}
	
	class SaveProfileTask extends AsyncTask<SaveAccountEntity, Void, AccountEntity>{

		@Override
		protected AccountEntity doInBackground(SaveAccountEntity... params) {
			return LoginHandler.updateAccount(params[0]);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(AccountEntity result) {
			WaitDialog.hideWaitDialog();
			super.onPostExecute(result);
			
			if(result.success){
			
				AccountManager man = AccountManager.getInstance();
			
				result.store(context);
				man.setAccountEntity(result);
				
				FriendDataTask task = new FriendDataTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(THREAD_POOL_EXECUTOR);
				}else{
					task.execute();				
				}
				
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingPopupActivity.this);
				builder.setTitle("Notice");
				builder.setMessage(R.string.save_profile_error);
				builder.setPositiveButton("OK",null);
				builder.create().show();
				
			}
			
			
		}

		@Override
		protected void onPreExecute() {
			WaitDialog.showWailtDialog(SettingPopupActivity.this, false);
			super.onPreExecute();
		}
		
		
		
	}
	
	class FriendDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			AccountEntity entity = AccountManager.getInstance().getAccountEntity();
			entity.likeYous = FriendHandler.getLikeYou(null,null,null);
			entity.friendGroups = FriendHandler.findGroups();
			entity.store(context);
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			finish();
		}
		
		
	}
}
