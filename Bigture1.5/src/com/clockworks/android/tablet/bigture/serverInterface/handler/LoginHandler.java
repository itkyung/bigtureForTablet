package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;


import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotiSettingEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SaveAccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpClientWrapper;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;



public class LoginHandler
{
	public static int CodeSuccess = 0;
	public static int CodeDuplicateAccount = 8;
	public static int CodeNoAccount = 9;
	public static int CodeEmpty = 10;
	public static int CodeAlreadyExist = 11;
	public static int CodeUnknown = -1;
	
	public static AccountEntity login(Context context,String userId, String passwd, boolean keepLogin)
	{
		String deviceType = AccountManager.isTablet ? "tablet" : "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("loginId",userId);
		params.add("pwd",passwd);
		
		AccountEntity entity = new AccountEntity();
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient, ServerStaticVariable.LoginURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");
			
			if (success){
				
				syncCookie(context,httpClient);
				
				
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.resultCode = errorNo;
				entity.keepLogin = keepLogin;
				entity.success = true;
			}else{
				entity = new AccountEntity();
				entity.resultCode = errorNo;
				entity.success = false;
			}
		}catch(Exception e){
			e.printStackTrace();
			entity.resultCode = -1;
			entity.success = false;
		}

		return entity;
	}

	
	public static AccountEntity login(Context context,String loginId,String loginToken){
		AccountEntity entity = new AccountEntity();
		
		String deviceType = AccountManager.isTablet ? "tablet" : "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
		
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("loginToken", loginToken);
		params.add("loginId", loginId);
	
		
		HttpUtil util = new HttpUtil();
		DefaultHttpClient httpClient = HttpClientWrapper.getClient();
		String resultString = util.execute(httpClient,ServerStaticVariable.LoginByTokenURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			if (success){
				syncCookie(context,httpClient);
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.keepLogin = true;
				entity.resultCode = 0;
				entity.success = true;
				
			}else{
				entity.success = false;
				entity.resultCode = errorNo;
			}
		}
		catch(Exception e)
		{
			entity.resultCode = -1;
			entity.success = false;
			e.printStackTrace();
		}

		return entity;
	}
//	
	public static void syncCookie(Context context,DefaultHttpClient client){
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		
		if (!client.getCookieStore().getCookies().isEmpty()) {
			cookieManager.removeSessionCookie();
			
			try{
				// ICS 
				Thread.sleep(500);
			}catch(Exception e){
				
			}
		}
			
		for (Cookie cookie : client.getCookieStore().getCookies()){
			CookieManager.getInstance().setCookie(cookie.getDomain(), cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain());
		}
		CookieSyncManager.getInstance().sync();
	}
	
	public static SimpleEntity checkSnsAccount(String socialType,String socialId){
		SimpleEntity entity = new SimpleEntity();
		entity.socialType = socialType;
		entity.socialId = socialId;
		
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("socialType", socialType);
		params.add("socialId", socialId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.CheckSnsAccountURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");

			if (success){
				JSONObject data = (JSONObject)object.get("data");
				entity.success = true;
				entity.needInit = data.getBoolean("needInit");
				entity.loginId = data.getString("loginId");
			}else{	
				entity.success = false;
			}
		}catch(Exception e){
			entity.success = false;
			e.printStackTrace();
		}
		
		return entity;
	}
	
	
	public static AccountEntity loginWithSocialAccount(String socialType, String socialId, String loginId)
	{
		AccountEntity entity = new AccountEntity();;
		HttpRequestParameters params = new HttpRequestParameters();
		
		String deviceType = AccountManager.isTablet ? "tablet" : "mobile";
		String osType = "Android";
		String osVersion = "" + AccountManager.osVersion;
		String appVersion = "" + AccountManager.appVersion;
	
		params.add("deviceType",deviceType);
		params.add("osVersion",osVersion);
		params.add("osType",osType);
		params.add("appVersion", appVersion);
		params.add("socialType", socialType);
		params.add("socialId", socialId);
		params.add("loginId", loginId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.LoginBySocialURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			int errorNo = object.getInt("errno");
			boolean success = object.getBoolean("success");

			if (success){
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.keepLogin = true;
				entity.resultCode = errorNo;
				entity.success = true;
			}else{
			
				entity.resultCode = errorNo;
				entity.success = false;
			}
		}catch(Exception e){
			entity.success = false;
			entity.resultCode = -1;
			e.printStackTrace();
		}

		return entity;
	}
	
	public static boolean logout()
	{
		boolean result = false;
		HttpRequestParameters params = new HttpRequestParameters();
		
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.LoginOutURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			result = object.getInt("errno") == 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;		
	}
	
	public static boolean resendActivateLink(String userId)
	{
		int result = -1;
		HttpRequestParameters params = new HttpRequestParameters();
		
		params.add("userId",userId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ResendActivateURL, params);

		boolean success = false;
		try
		{
			JSONObject object = new JSONObject(resultString);
			success = object.getBoolean("success");
			result = object.getInt("errno");
		}catch(Exception e){
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	public static AccountEntity registAccount(String socialType,String socialId,String loginId, String passwd, 
			String nickName, String birthday, String gender, String countryCode){
		AccountEntity entity = new AccountEntity();
		
		
		HttpRequestParameters params = new HttpRequestParameters();

		params.add("loginId", loginId);
		params.add("socialType",socialType);
		params.add("socialId",socialId);
		params.add("password",passwd);
		params.add("nickName",nickName);
		params.add("gender",gender);
		params.add("birthDay",birthday);
		params.add("countryCode",countryCode);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.RegistAccountURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int result = object.getInt("errno");
			
			if (success){
				JSONObject data = (JSONObject)object.get("data");
				entity.importData(data);
				entity.keepLogin = true;
				entity.resultCode = result;
				entity.success = true;
			}else{
				entity.resultCode = result;
				entity.success = false;
			}
		}
		catch(Exception e){
			entity.resultCode = -1;
			entity.success = false;
			e.printStackTrace();
		}
		
		return entity;
	}
	
	public static AccountEntity updateAccount(SaveAccountEntity entity){
		HttpRequestParameters params = new HttpRequestParameters();


		params.add("nickName",entity.getNickName());
		params.add("gender",entity.getGender());
		params.add("birthDay",entity.getBirthday());
		params.add("countryCode",entity.getCountryCode());
		if(entity.getComment() != null)
			params.add("comment",entity.getComment().replace("\\", "\\\\")
				.replace("\"", "\\\"")
				.replace("\r", "\\r")
				.replace("\n", "\\n"));
		if(entity.getTempImageId() != null){
			params.add("tempImageId",entity.getTempImageId());
		}
		
		NotiSettingEntity setting = entity.getSetting();
		
		params.add("talkCommentPush",""+setting.talkCommentPush);
		params.add("likePush",""+setting.likePush);
		params.add("picMePush",""+setting.picMePush);
		params.add("contestPush",""+setting.contestPush);
		params.add("myClassPush",""+setting.myClassPush);
		params.add("cardPush",""+setting.cardPush);
		params.add("storyPush",""+setting.storyPush);
		params.add("classPush",""+setting.classPush);
		params.add("pushAlert",""+setting.pushAlert);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.UpdateAccountURL, params);
		AccountEntity account = AccountManager.getInstance().getAccountEntity();
		
		try{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int result = object.getInt("errno");
			
			if (success){
				JSONObject data = (JSONObject)object.get("data");
				account.importData(data);
				account.success = true;
			}else{
				account.success = false;
			}
		}
		catch(Exception e){
			account.resultCode = -1;
			account.success = false;
			e.printStackTrace();
		}
		
		return account;
	}
	
	public static boolean changePassword(String currentPasswd,String newPasswd){
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("currentPasswd",currentPasswd);
		params.add("newPasswd",newPasswd);
		
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ChangePassworkdURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			if(object.getBoolean("success")){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static SimpleEntity resetPassword(String loginId){
		SimpleEntity result = new SimpleEntity();
		result.data = loginId;
		HttpRequestParameters params = new HttpRequestParameters();
		params.add("loginId",loginId);
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.ResetPasswordURL, params);

		try{
			JSONObject object = new JSONObject(resultString);
			if(object.getBoolean("success")){
				result.success = true;
			}else{
				result.success = false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;		
	}
	
}
