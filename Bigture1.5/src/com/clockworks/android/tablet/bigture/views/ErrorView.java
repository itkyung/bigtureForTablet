package com.clockworks.android.tablet.bigture.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;

public class ErrorView extends Activity{
	public static String STACKTRACE = "gate.stacktrace";

	public void onCreate(Bundle icicle){
		super.onCreate(icicle);

		try
		{
			// 레이아웃
			LinearLayout llErrorView = new LinearLayout(this);
			llErrorView.setOrientation(LinearLayout.VERTICAL);
			llErrorView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	
			// Title
			final TextView tvViewTitle = new TextView(this);
			tvViewTitle.setText("");
			tvViewTitle.setTextSize(12); 
			tvViewTitle.setTextColor(Color.BLACK);
			tvViewTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			llErrorView.addView(tvViewTitle);
	
			// Text
			final TextView tvViewText = new TextView(this);
			tvViewText.setText("");
			tvViewText.setTextSize(12); 
			tvViewText.setTextColor(Color.GRAY);
			tvViewText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
			tvViewText.setMovementMethod(ScrollingMovementMethod.getInstance());
			tvViewText.setClickable(false);
			tvViewText.setLongClickable(false);
			llErrorView.addView(tvViewText);
			
			// 버튼 레이아웃
			LinearLayout llButton = new LinearLayout(this);
			llButton.setOrientation(LinearLayout.HORIZONTAL);
			llButton.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llErrorView.addView(llButton);
			 
			// 보내기 버튼
			Button btnSend = new Button(this);
			btnSend.setText("보내기");
			btnSend.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
			llButton.addView(btnSend);
			 
			// 종료 버튼
			Button btnClose = new Button(this);
			btnClose.setText("종료");
			btnClose.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT, 1));
			llButton.addView(btnClose);
			
			this.setContentView(llErrorView);

			// 단말기 기종 / 네트워크 상태(WiFi/3G) /
			String szModel = Build.MODEL;
			
			TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
			String szTelecom = telManager.getSimOperatorName();
					
			//Log.e("_TEST", "[uncaughtException]======= 기종 : " + szModel);
			//Log.e("_TEST", "[uncaughtException]======= 통신사 : " + szTelecom);
	
			ConnectivityManager cmgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
			NetworkInfo netInfo = cmgr.getActiveNetworkInfo();
			String szNetworkType = netInfo.getTypeName() + " " + netInfo.isConnected();
			//Log.e("_TEST", "[uncaughtException]======= 네트워크 : " + szNetworkType);
	
			String szMyLocation = getMyLocation();
			//Log.e("_TEST", "[uncaughtException]======= GPS : " + szMyLocation);
			
			String szAppName = "";
			String szAppVersion = "";
			try
			{
				szAppName = getPackageManager().getPackageInfo(getPackageName(), 0).packageName.toString();
				szAppVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			}
			catch (Exception e)
			{}
			//Log.e("_TEST", "[uncaughtException]======= 어플정보 : " + szAppName + " " + szAppVersion);
			
			final String szStackTrace = getIntent().getStringExtra(STACKTRACE);
			//Log.e("_TEST", "[uncaughtException]======= 오류 로그 : " + szStackTrace);
	
			// 화면에 뿌릴 메세지
			String szViewTitle = "불편을 끼쳐드려서 죄송합니다.\n오류가 발생하여 프로그램을 종료하여야 합니다.\n이 문제를 개선하기 위해 오류보고서를 작성하였습니다.\n이 내용은 익명으로 관리합니다.\n";

			String szViewText = "기기종류 : " + szModel + "\n";
			//szViewText += "이동통신 : " + szTelecom + "\n";
			szViewText += "네트워크 : " + szNetworkType + "\n";
//			szViewText += "이용위치 : " + szMyLocation + "\n";
			szViewText += "어플정보 : " + szAppName + " " + szAppVersion + "\n";
		
			szViewText += "오류로그 : \n" + szStackTrace + "\n";
			
			// 메일 제목
			final String szMailTitle = szAppName + " " + szAppVersion + " 오류보고서";
	
			// 메일 내용
			final String szMailText = szViewText;
			
			// 화면 출력
			tvViewTitle.setText(szViewTitle);
			tvViewText.setText(szViewText);
			btnSend.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					Intent sendIntent = new Intent(Intent.ACTION_SEND);
					sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"itkyung@gmail.com"});
					sendIntent.putExtra(Intent.EXTRA_SUBJECT, szMailTitle);
					sendIntent.putExtra(Intent.EXTRA_TEXT, szMailText);
					sendIntent.setType("plain/text");
					startActivity(Intent.createChooser(sendIntent, "send mail"));
					finish();
				}
			});
	
			btnClose.setOnClickListener(new OnClickListener()
			{
				public void onClick(View view)
				{
					finish();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
			finish();
		}
	}


    // GPS를 이용하여 현재 위치 좌표 구함
	public String getMyLocation()
	{
		String szMyLocation = "GPS 미사용";
		try
		{
			LocationManager locationMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	        
	        //Criteria 클래스를 이용하여 요구조건을 명시하여, 가장 적합한 기술을 결정
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setSpeedRequired(false);
	        criteria.setCostAllowed(true);
	        
	        //현재 위치        
	        String bestProvider = locationMgr.getBestProvider(criteria, true);
	        Location location = locationMgr.getLastKnownLocation(bestProvider);
	
	        // 위도:경도
	        szMyLocation = location.getLatitude() + ":" + location.getLongitude();
		}
		catch (Exception e)
		{
		}

        return szMyLocation;
	}
}
