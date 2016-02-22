package com.clockworks.android.tablet.bigture.common;

import java.io.PrintWriter;
import java.io.StringWriter;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.ErrorView;


import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;


public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler
{
	private final Context myContext;

	public UncaughtExceptionHandler(Context context)
	{
		myContext = context;
	}

	public void uncaughtException(Thread thread, Throwable exception)
	{
		final StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));
		System.err.println(stackTrace);
		
		Log.e("Catch " , "Catch Code ");
		//이메일로 보낸다.
		
		Toast.makeText(myContext, myContext.getResources().getString(R.string.error_common), Toast.LENGTH_LONG).show();
//		
//		Intent intent = new Intent(myContext, ErrorView.class);
//		intent.putExtra(ErrorView.STACKTRACE, stackTrace.toString());
//		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		myContext.startActivity(intent); 		
//		
//		Process.killProcess(Process.myPid());

	}
}
