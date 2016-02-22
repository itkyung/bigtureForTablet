package com.clockworks.android.tablet.bigture.views.account;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.GCMManager;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.GCMServiceHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class LoginTwitterView extends LinearLayout
{
	Context context;
	Handler handler;
	
	WebView webView;
	Twitter twitter;
	
	public LoginTwitterView(Context context, Handler handler)
	{
		super(context);

		this.context = context;
		this.handler = handler;

		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.popup_account_login_twitter, this);

		webView = (WebView)findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				if (url.contains(BigtureEnvironment.TWITTER_CALLBACK_URL))
				{
					Uri uri = Uri.parse(url);
					String oAuthVerifier = uri.getQueryParameter("oauth_verifier");

					AccessTokenTask task = new AccessTokenTask();
					task.execute(oAuthVerifier);
					
					return true;
				}
				
				return super.shouldOverrideUrlLoading(view, url);
			}
			
		});

		RequestTokenTask task = new RequestTokenTask();
		task.execute();
	}

	class RequestTokenTask extends AsyncTask<Void,Void,String>
	{

		@Override
		protected String doInBackground(Void... arg0)
		{
			String url = null;
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setOAuthConsumerKey(BigtureEnvironment.TWITTER_CONSUMER_KEY);
			cb.setOAuthConsumerSecret(BigtureEnvironment.TWITTER_CONSUMER_SECRET);
			cb.setUseSSL(true);
			twitter = new TwitterFactory(cb.build()).getInstance();
			
			try
			{
				RequestToken token = twitter.getOAuthRequestToken(BigtureEnvironment.TWITTER_CALLBACK_URL);
				url = token.getAuthenticationURL();
			}
			catch(TwitterException e)
			{
				e.printStackTrace();
			}
			
			return url;
		}

		@Override
		protected void onPostExecute(String result)
		{
			if (result != null){
				if(result.startsWith("http://")){
					result = "https://" + result.substring(7);
				}
				webView.loadUrl(result);
			}

			super.onPostExecute(result);
		}
	}

	//SNS계정을 초기화하는 페이지로 보낸다.
	public void goInitAccount(SimpleEntity result){
		
		Message msg = handler.obtainMessage(1);
		msg.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_FROM_SNS;
		msg.obj = result;
		handler.sendMessage(msg);
	}
			
		
	class AccessTokenTask extends AsyncTask<String,Void,AccountEntity>
	{
		@Override
		protected AccountEntity doInBackground(String... arg0)
		{
			AccountEntity entity = null;
			
			try
			{
				String oAuthVerifier = arg0[0];
				
				AccessToken accToken = twitter.getOAuthAccessToken(oAuthVerifier);
				
				//String token = accToken.getToken();
				//String tokenSecret = accToken.getTokenSecret();
				String screenName = accToken.getScreenName();
				
				User user = twitter.showUser(screenName);
				
				String userId = "" + user.getId();
				String name = user.getName();
				String firstName = name;
				String lastName = "";
				
				
				if (name.lastIndexOf(" ") >= 0)
				{
					int pos = name.lastIndexOf(" ");
					firstName = name.substring(0, pos);
					lastName  = name.substring(pos+1);
				}
				
				String socialId = userId;
				
				SimpleEntity result = LoginHandler.checkSnsAccount("TWITTER", socialId);
				if(result.success){
					if(!result.needInit){
						entity = LoginHandler.loginWithSocialAccount("TWITTER",socialId, result.loginId);
						entity.keepLogin = true;
						entity.likeYous = FriendHandler.getLikeYou(null,null,null);
						entity.friendGroups = FriendHandler.findGroups();
						entity.store(context);
		
						AccountManager.getInstance().setLogin(true);
						
						String regId = GCMManager.getRegistrationId(context);
						
						if (regId == null)
							GCMManager.registerGCM(context);
						else
							GCMServiceHandler.updateGCMRegistrationId(entity.index, regId);
						return entity;
					}
				}
				
				goInitAccount(result);
			}
			catch (TwitterException e)
			{
				e.printStackTrace();
			}
			
			return entity;
		}

		@Override
		protected void onPostExecute(AccountEntity result)
		{
			if (result == null)
			{
//				AlertDialog.Builder builder = new AlertDialog.Builder(context);
//				builder.setTitle("Login Failed");
//				builder.setMessage("Fail to log in\nPlease check your Account.\nAnd try again.");
//				builder.setNegativeButton("Ok", null);
//				builder.create().show();
			}else if (result.resultCode == 0)
			{
				Message message = handler.obtainMessage(100, true);
				message.what = ServerStaticVariable.LOGIN_COMPLETE;
				handler.sendMessage(message);
			}
			// 1006: 사용자의 추가 정보가 필요함
			// 1007: 등록되지 않은 사용자(등록 필요함)
//			else if (result.resultCode == 1006)
//			{
//				Message message = handler.obtainMessage(1, 5, 0, result);
//				handler.sendMessage(message);
//			}
//			else if (result.resultCode == 1007)
//			{
//				Message message = handler.obtainMessage(1, 4, 0, result);
//				handler.sendMessage(message);
//			}
//			else
//			{
//				AlertDialog.Builder builder = new AlertDialog.Builder(context);
//				builder.setTitle("Login Failed");
//				builder.setMessage("Fail to log in\nPlease check your Account.\nAnd try again.");
//				builder.setNegativeButton("Ok", null);
//				builder.create().show();
//			}

			super.onPostExecute(result);
		}
	}
}
