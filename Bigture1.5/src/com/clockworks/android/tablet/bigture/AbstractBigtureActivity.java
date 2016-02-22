package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.NotificationAdapter;
import com.clockworks.android.tablet.bigture.adapter.user.FindSimpleUserAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.NotificationClickListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationGroup;
import com.clockworks.android.tablet.bigture.serverInterface.handler.MainHandler;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * Bigture의 모든 Activity의 기본이 되는 가상 클래스 
 * 앱의 crash 시점에 catch를 해서 서버에 report를 해주는 등의 기능을 할수 있다.
 * @author bizwave
 *
 */
public abstract class AbstractBigtureActivity extends FragmentActivity {
	private PopupWindow notificationPopup;
	private ListView notificationList;
	private NotificationAdapter notiAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		// Exception Handler
		Thread.setDefaultUncaughtExceptionHandler(new com.clockworks.android.tablet.bigture.common.UncaughtExceptionHandler(
				this));
				
	}
	
	protected void showNotifications(View v){
		if (!AccountManager.isLogin()){
			BigtureEnvironment.showAlertMessage(this, 16);
			return;
		}	
		
		if(notificationPopup == null){
			LayoutInflater inflater = LayoutInflater.from(this);
			View menuView = inflater.inflate(R.layout.popup_notifications, null);
			//notificationPopup = new PopupWindow(menuView,368,592);
			final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 368, getResources().getDisplayMetrics());
			final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 592, getResources().getDisplayMetrics());
			
			notificationPopup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,height);
			notificationPopup.setOutsideTouchable(true);
			notificationPopup.setBackgroundDrawable(new BitmapDrawable()) ;
		}
		
		notificationPopup.dismiss();
		View mView = notificationPopup.getContentView();
		
		this.notificationList = (ListView)mView.findViewById(R.id.listNotifications);
		notiAdapter = new NotificationAdapter(this, new NotificationClickListener() {
			
			@Override
			public void refreshNotifications() {
				new NotificationTask().execute();
			}
			
			@Override
			public void clickNotification(NotificationEntity noti) {
				
				
			}
		});
		
		this.notificationList.setAdapter(notiAdapter);
		
		mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		
		notificationPopup.setAnimationStyle(-1);
		notificationPopup.showAsDropDown(v, 0,5);
		
		new NotificationTask().execute();
	}
	
	class NotificationTask extends AsyncTask<Void, Void, ArrayList<NotificationGroup>>{

		@Override
		protected ArrayList<NotificationGroup> doInBackground(Void... params) {
			return MainHandler.listNotifications();
		}

		@Override
		protected void onPostExecute(ArrayList<NotificationGroup> result) {	
			super.onPostExecute(result);
			if(notiAdapter != null){
				notiAdapter.setNotifications(result);
				notiAdapter.notifyDataSetChanged();
			}
		}
		
	}
	
}
