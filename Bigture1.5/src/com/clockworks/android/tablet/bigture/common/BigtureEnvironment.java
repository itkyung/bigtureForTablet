package com.clockworks.android.tablet.bigture.common;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class BigtureEnvironment
{
	public static final int REQ_CODE_LOGIN = 1101;
	public static final int REQ_CODE_SKETCHBOOK = 1102;
	public static final int REQ_CODE_PICKUP_PICTURE = 1103;
	public static final int REQ_CODE_TAKE_PICTURE = 1104;
	public static final int REQ_CODE_CROP_PICTURE = 1105;
	public static final int REQ_CODE_EDIT_NEWS = 1106;
	public static final int REQ_CODE_SETTINGS = 1107;
	public static final int REQ_CODE_CLASS_MAIN = 1108;
	public static final int REQ_CODE_CLASS_SUBJECT = 1109;
	public static final int REQ_CODE_ARTWORK_DETAIL = 1110;
	public static final int REQ_CODE_SETTING = 1111;
	
	public static final int EVENT_CODE_RELOAD_DATA = 1201;
	public static final int EVENT_CODE_RELOAD_NEWS = 1202;
	public static final int EVENT_CODE_RELOAD_CAREER = 1203;
	public static final int EVENT_CODE_DATA_DELETED = 1204;
	public static final int EVENT_CODE_DATA_UPDATED = 1205;
	
	
	public static final int EVENT_CODE_SHOW_ARTWORKDETAIL = 1;
	public static final int EVENT_CODE_SHOW_MYARTWORK = 2;
	public static final int EVENT_CODE_SHOW_CONTESTENTRY = 3;
	public static final int EVENT_CODE_SHOW_EXPERTMYPAGE = 4;
	public static final int EVENT_CODE_SHOW_REPLYMAP = 5;
	public static final int EVENT_CODE_UPDATE_PROFILEVIEW = 6;
	public static final int EVENT_CODE_COLLECTION_CHANGED = 7;

	public static final int EVENT_CODE_EDIT_PROFILE = 11;
	public static final int EVENT_CODE_ADD_CAREER = 12;
	public static final int EVENT_CODE_EDIT_CAREER = 13;
	public static final int EVENT_CODE_ADD_NEWS = 14;
	public static final int EVENT_CODE_EDIT_NEWS = 15;
	public static final int EVENT_CODE_SHOW_STORYBOOK = 18;
	public static final int EVENT_CODE_ADD_STORY = 19;
	public static final int EVENT_CODE_EDIT_STORY = 20;
	public static final int EVENT_CODE_DELETE_STORY = 21;
	public static final int EVENT_CODE_EDIT_STORY_PAGE = 22;
	public static final int EVENT_CODE_EDIT_ARTWORK = 23;
	public static final int EVENT_CODE_ADD_FRIEND = 24;
	public static final int EVENT_CODE_REMOVE_FRIEND = 25;
	public static final int EVENT_TOGGLE_FAVORITE = 26;
	public static final int EVENT_CODE_FIND_USER = 27;
	public static final int EVENT_EDIT_GROUP = 28;
	public static final int EVENT_EDIT_GROUP_MEMBER = 29;
	
	public static final int EVENT_CODE_SHOW_LOOK_AROUND = 31;
	public static final int EVENT_CODE_SHOW_WHAT_IS_BIGTURE = 32;
	public static final int EVENT_CODE_SHOW_NOTICE = 33;
	public static final int EVENT_CODE_SHOW_CONTACT = 34;
	public static final int EVENT_CODE_SHOW_SKETCHBOOK = 35;
	public static final int EVENT_CODE_SHOW_ARTCLASS = 36;
	
	
	public static final int EVENT_CODE_SEND_CARD = 41;
	public static final int EVENT_CODE_SELECT_FRIENDS = 42;
	public static final int EVENT_CODE_VIEW_CARD = 43;
	public static final int EVENT_CODE_EDIT_CLASS = 44;
	public static final int EVENT_CODE_ADD_CLASS_MEMBER = 45;
	
	
	public static final int EVENT_CODE_PICKUP_IMAGE = 101;
	public static final int EVENT_CODE_TAKE_PICTURE = 102;
	public static final int EVENT_CODE_SELECT_ARTWORK = 103;
	public static final int EVENT_CODE_DRAW_COMPLETE = 104;
	
	public static final int EVENT_CODE_CHANGE_RIGHT_MENU_BUTTON = 201;
	public static final int EVENT_CODE_ARTWORK_DELETED = 202;
	public static final int EVENT_CODE_SPLASH_LOADED = 203;
	public static final int EVENT_CODE_CLOSE_SPLASH = 204;
	
	
	public static final int ACCOUNT_POPUP_LOGIN = 0;
	public static final int ACCOUNT_POPUP_CREATE_STEP1 = 1;
	public static final int ACCOUNT_POPUP_CREATE_STEP2 = 2;
	public static final int ACCOUNT_POPUP_LOGIN_NEED_ACTIVATE = 4;
	public static final int ACCOUNT_POPUP_FORGOT_PASSWD_STEP1 = 10;
	public static final int ACCOUNT_POPUP_FORGOT_PASSWD_FINISH = 11;
	public static final int ACCOUNT_POPUP_FORGOT_PASSWD_ERROR = 12;
	public static final int ACCOUNT_POPUP_CREATE_FROM_SNS = 13;
	public static final int ACCOUNT_POPUP_CREATE_COMPLETE = 100;
	
	
	
	public static String TWITTER_ID					= "the_Clockworks";
	public static String TWITTER_CONSUMER_KEY		= "wM5ppiXtqvkbRwGHilz16Krzp";
	public static String TWITTER_CONSUMER_SECRET    = "8GLOavFhAQHJWFB7WVPrEnC996hkzvcF4n2JcpxqYuslm5T93F";
	public static String TWITTER_CALLBACK_URL		= "http://www.bigture.co.kr";

	public static int ARTWORK_COUNT_PER_PAGE = 40;

	protected static BigtureEnvironment env;
	
	public final String cacheDir;
	public final String tempDir;
	
	static boolean displaySplash;
	
	protected BigtureEnvironment(Context context)
	{
		cacheDir = FileUtil.getCachePath(context);
		tempDir  = FileUtil.getTempPath(context);
		
		SharedPreferences preferences = context.getSharedPreferences("System", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		
		editor.putBoolean("splashViewDispled", false);
		editor.commit();
	}
	
	public static BigtureEnvironment createInstance(Context context)
	{
		env = new BigtureEnvironment(context);
		return env;
	}
	
	public static BigtureEnvironment getInstance()
	{
		return env;
	}

	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		
		return metrics;
	}
	
	public static boolean isSplashViewDisplayed(Context context)
	{
		return displaySplash;
//		SharedPreferences preferences = context.getSharedPreferences("System", Context.MODE_PRIVATE);
//		return preferences.getBoolean("splashViewDispled", false);
	}
	
	public static void setSplashViewDisplayed(Context context, boolean displayed)
	{
		displaySplash = displayed;
//		SharedPreferences preferences = context.getSharedPreferences("System", Context.MODE_PRIVATE);
//		SharedPreferences.Editor editor = preferences.edit();
//			
//		editor.putBoolean("splashViewDispled", displayed);
//		editor.commit();
	}
	
	public static void showAlertMessage(Context context, int resultCode)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Notice");
		
		if (resultCode == 11)
			builder.setMessage(R.string.error_already_comment);
		else if (resultCode == 2)
			builder.setMessage(R.string.error_account_already_exists);
		else if (resultCode == 3)
			builder.setMessage(R.string.error_invalid_login_key);
		else if (resultCode == 4)
			builder.setMessage(R.string.error_invalid_login_code);
		else if (resultCode == 5)
			builder.setMessage(R.string.error_invalid_password);
		else if (resultCode == 8)
			builder.setMessage(R.string.error_account_already_exists);
		else if (resultCode == 9)
			builder.setMessage(R.string.error_no_account);
//		else if (resultCode == 11)
//			builder.setMessage(R.string.error_duplicated_data);
		else if (resultCode == 15)
			builder.setMessage(R.string.error_upload);
		else if (resultCode == 16)
			builder.setMessage(R.string.message_need_login);
		else if (resultCode == 17)
			builder.setMessage(R.string.label_not_your_artwork);
		else
			builder.setMessage(R.string.error_common);
		
		builder.setNegativeButton("OK", null);
		builder.create().show();
	}
	
	public static void showAlertMessage(Context context, String message)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Notice");
		builder.setMessage(message);
		builder.setNegativeButton("OK", null);
		builder.create().show();
		
	}
	
	public interface PhotoPickupListener
	{
		void onPhotoPickedUp(String filePath);
		void onPhotoCanceled();
		
		boolean isCropImage();
		
		Point getImageSize();
		Point getAspect();
	}
	
	public interface DialogListener
	{
		void onCanceled();
		void onCompleted();
	}
}
