package com.clockworks.android.tablet.bigture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String msg = intent.getStringExtra("msg");
		
		//NoticeDialog를 띄운다.
		SharedPreferences preferences = getApplicationContext().getSharedPreferences("Intro", Context.MODE_PRIVATE);
		boolean pushAlert = preferences.getBoolean("pushAlert", true);
		if(!pushAlert){
			return;
		}
		
		if(AccountManager.isLogin()){
			//단지 count만 증가시킨다.
			
			Intent pendIntent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, pendIntent, PendingIntent.FLAG_ONE_SHOT);
//			try {
//				pie.send();
//			}catch (CanceledException e) {
//				e.printStackTrace();
//			}			
//			
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification.tickerText = msg;
			notification.when = System.currentTimeMillis();
			notification.vibrate = new long[] { 500, 100, 500, 100 };
			notification.sound = Uri.parse("/system/media/audio/notifications/20_Cloud.ogg");
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, "Bigture", msg, pendingIntent);

			notificationManager.notify(0, notification);
		}else{
			Intent pendIntent = new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, pendIntent, 0);
			
			NotificationManager notificationManager = (NotificationManager)context.getSystemService(Activity.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.icon = R.drawable.ic_launcher;
			notification.tickerText = msg;
			notification.when = System.currentTimeMillis();
			notification.vibrate = new long[] { 500, 100, 500, 100 };
			notification.sound = Uri.parse("/system/media/audio/notifications/20_Cloud.ogg");
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.setLatestEventInfo(context, "Bigture", msg, pendingIntent);

			notificationManager.notify(0, notification);

//		
//			Bundle bun = new Bundle();
//			bun.putString("notiMsg", msg);
//			
//			Intent popupIntent = new Intent(getApplicationContext(),AlertDialogActivity.class);
//			popupIntent.putExtras(bun);
//			
//			PendingIntent pie= PendingIntent.getActivity(getApplicationContext(), 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
//			try {
//				pie.send();
//			}catch (CanceledException e) {
//				e.printStackTrace();
//			}	

		}
	}

	@Override
	protected void onRegistered(Context arg0, String regId) {
		Log.i("GCM Regist", regId);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
