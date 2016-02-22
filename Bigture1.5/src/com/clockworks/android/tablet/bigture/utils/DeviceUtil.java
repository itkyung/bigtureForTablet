package com.clockworks.android.tablet.bigture.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class DeviceUtil
{
	public static String getDeviceId(Context context)
	{
		TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		return telManager.getDeviceId();
	}
	
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	public static String getDeivceModel()
	{
		return android.os.Build.MODEL;
	}
	
	public static String getOSVersionString()
	{
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static int getOSVersionNumber()
	{
		return android.os.Build.VERSION.SDK_INT;
	}
	
	public static boolean isTablet(Context context)
	{
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public static String getAppVersion(Context context)
	{
		String version = null;
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = i.versionName;
		} catch(NameNotFoundException e) { }
		
		return version;
	}
}
