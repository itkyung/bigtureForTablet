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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;


public class FacebookLoginHelper{
	Context context;
	Handler handler;
	
	String uid;
	String firstName;
	String lastName;
	String email;
	String birthday;
	
	private ProgressDialog progressDlg;
	
	//private Session.StatusCallback statusCallback = new SessionStatusCallback();
	//private GraphCallback graphCallback = new GraphCallback();
	
	public FacebookLoginHelper(Context context, Handler handler)
	{
		this.context = context;
		this.handler = handler;
	}
	
	public void login(){
		Session session = Session.getActiveSession();
		
		if(session != null && session.isOpened()){
			session.closeAndClearTokenInformation();
		}else{
			session = new Session((Activity)context);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
		}
		
		Session.openActiveSession((Activity)context, true, new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				
				if(session.isOpened()){
	        		Request.newMeRequest(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(GraphUser user, Response response) {
							uid = user.getId();

							progressDlg = ProgressDialog.show(context, "", "Login .....", true);
							progressDlg.setCancelable(false);
							
							CheckDataTask task = new CheckDataTask();
							task.execute(uid);
						}
					}).executeAsync();
	        	}
			}
		});
		
//		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//		
//		// 현재 세션을 얻는다.
//		Session currentSession = Session.getActiveSession();
//		
//		// 현재 세션이 유효한지 테스트 한다.
//		if (currentSession == null)
//		{
//			// 등록된 어플 아이디를 이용해서 세션을 새로 생성한다.
//			String applicationId = context.getResources().getString(R.string.app_id);
//			Session session = new Session.Builder((Activity)context).setApplicationId(applicationId).build();
//			Session.setActiveSession(session);
//			currentSession = session;
//		}
//		
//		// 현재 세션이 열린 상태인지 확인한다.
//		if (currentSession != null && !currentSession.isOpened())
//		{
//			OpenRequest openRequest = new OpenRequest((Activity)context);
//			openRequest.setCallback(statusCallback);
//			
//			//openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
//			//openRequest.setPermissions(Arrays.asList("email", "location", "birthday"));
//			currentSession.openForRead(openRequest);
//		}
//		else
//		{
//			Request.executeMeRequestAsync(currentSession, graphCallback);
//		}
	}
	
//	class SessionStatusCallback implements Session.StatusCallback
//	{
//        @Override
//        public void call(Session session, SessionState state, Exception exception){
//        	if(session.isOpened()){
//        		Request.executeMeRequestAsync(session, graphCallback);
//        	}
//        	// 진행 중..
//        	if (state == SessionState.OPENING)
//        	{
//        		
//        	}
//        	else if (state == SessionState.OPENED)
//        	{
//        		if (session.isOpened())
//	        	{
//	        		Request.executeMeRequestAsync(session, graphCallback);
//	        	}        		
//        	}
//        	else if (state == SessionState.CLOSED_LOGIN_FAILED)
//        	{
//        		// 세션을 새로 연다.
//        		Session session2 = Session.openActiveSession((Activity)context, true, statusCallback);				
//				Session.setActiveSession(session2);        		
//        	}
//        }
//    }
//	
//	class GraphCallback implements GraphUserCallback
//	{
//		@Override
//		public void onCompleted(GraphUser user, Response response)
//		{
//			uid = user.getId();
//			firstName = user.getFirstName();
//			lastName  = user.getLastName();
//			birthday  = user.getBirthday();
//			email     = (String)user.asMap().get("email");
//			
//			CheckDataTask task = new CheckDataTask();
//			task.execute(uid);
//		}		
//	}
	
	//SNS계정을 초기화하는 페이지로 보낸다.
	public void goInitAccount(SimpleEntity result){
		
		Message msg = handler.obtainMessage(1);
		msg.arg1 = BigtureEnvironment.ACCOUNT_POPUP_CREATE_FROM_SNS;
		msg.obj = result;
		handler.sendMessage(msg);
		
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		Session.getActiveSession().onActivityResult((Activity)context, requestCode, resultCode, data);
	}
	
	/**
	 * 서버에 해당 사용자에 대한 loginId를 물어본다. 
	 * 만약 없으면 아직 초기화가 안된것이기 때문에 create Account에서 초기화작업을 하게 한다.
	 * @author bizwave
	 *
	 */
	class CheckDataTask extends AsyncTask<String, Void, SimpleEntity>{

		@Override
		protected SimpleEntity doInBackground(String... params) {
			
			String socialId = params[0];
			
			return LoginHandler.checkSnsAccount("FACEBOOK", socialId);
		}

		@Override
		protected void onPostExecute(SimpleEntity result) {
			if(result.success){
				if(!result.needInit){
					LoginTask task = new LoginTask();
					task.execute(result.loginId,result.socialId);
					return;
				}
			}
			progressDlg.dismiss();
			goInitAccount(result);
			super.onPostExecute(result);
		}
		
	}
	
	
	class LoginTask extends AsyncTask<String,Void,AccountEntity>{
		@Override
		protected AccountEntity doInBackground(String... params){
			String loginId = params[0];
			String socialId = params[1];
			
			AccountEntity entity =  LoginHandler.loginWithSocialAccount("FACEBOOK",socialId, loginId);
			
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
			progressDlg.dismiss();
			if (result.success){
				Message message = handler.obtainMessage(ServerStaticVariable.LOGIN_COMPLETE);
				message.obj = (Boolean)true;
				handler.sendMessage(message);
			}
			super.onPostExecute(result);
		}
		
	}
}
