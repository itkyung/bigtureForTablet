package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.user.FindSimpleUserAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.common.GCMManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.MainContentsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.GCMServiceHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.LoginHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.MainHandler;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;
import com.clockworks.android.tablet.bigture.views.main.SplashView;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AbstractBigtureActivity implements View.OnClickListener, AccountManager.OnLoginChangeListener{
	private Handler handler = new MessageHandler(this);
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		//Bigture 환경설정과 관련된 instance를 만든다.Singleton형태.
		if (BigtureEnvironment.getInstance() == null)
			BigtureEnvironment.createInstance(this);
		
		AccountManager.createInstance(this);
		AccountManager.getInstance().addOnLoginChangeListener(this);
		
		//로그인과 계정생성.
		TextView welcomeLabel = (TextView)findViewById(R.id.welcomeLabel);
		welcomeLabel.setOnClickListener(this);
		TextView nameLabel = (TextView)findViewById(R.id.nameLabel);
		nameLabel.setOnClickListener(this);
		TextView loginLabel = (TextView)findViewById(R.id.loginLabel);
		loginLabel.setOnClickListener(this);
		
		findViewById(R.id.linearLayout1).setOnClickListener(this);
		
		// Artwork Button
		Button btnArtwork = (Button)findViewById(R.id.btnArtwork);
		btnArtwork.setOnClickListener(this);
		
		Button btnContest = (Button)findViewById(R.id.btnContest);
		btnContest.setOnClickListener(this);
		
		Button btnExpert = (Button)findViewById(R.id.btnExpert);
		btnExpert.setOnClickListener(this);
		
		Button btnStory = (Button)findViewById(R.id.btnStory);
		btnStory.setOnClickListener(this);
		
		
//		Button btnClass = (Button)findViewById(R.id.btnArtClass);
//		btnClass.setOnClickListener(this);
		
	
		
		//스플래쉬뷰 디스플레이
		if (!BigtureEnvironment.isSplashViewDisplayed(this)){
			SplashView splashView = (SplashView)findViewById(R.id.splashView1);
			splashView.setVisibility(View.VISIBLE);
			splashView.loadImage(handler);
			
			BigtureEnvironment.setSplashViewDisplayed(this, true);
		}
		else
		{
			SplashView splashView = (SplashView)findViewById(R.id.splashView1);
			splashView.setVisibility(View.GONE);
		}
		
		findViewById(R.id.btnMyBigture).setOnClickListener(this);
		findViewById(R.id.btnSketchbook).setOnClickListener(this);
		findViewById(R.id.btnNotice).setOnClickListener(this);
		findViewById(R.id.btnSettings).setOnClickListener(this);
		findViewById(R.id.btnInfo).setOnClickListener(this);
		
		
		doAutoLogin();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/**
	 * 로그인이 성공하면 호출됨.
	 * 상단 영역에 사용자의 이름을 표시해줌.
	 */
	@Override
	public void onLoginChanged(boolean login) {
		this.runOnUiThread(new Runnable() {

			@Override
			public void run(){
				showProfile();
			}
		});
	}

	/**
	 * 버튼과 라벨을 클릭했을때 처리하는 listener
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.welcomeLabel:
		case R.id.nameLabel:
		case R.id.loginLabel:
		case R.id.linearLayout1:
			if (AccountManager.isLogin()){
				logout();
			}else{
				Intent intent = new Intent(this, AccountActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.btnMyBigture:
			if (AccountManager.isLogin()){
				Intent intent = new Intent(this, MyBigtureActivity.class);
				intent.putExtra("userId", AccountManager.getUserIdx());
				intent.putExtra("viewIndex", 0);
				startActivity(intent);
			}else{
				BigtureEnvironment.showAlertMessage(this, 16);
			}			
			break;
		case R.id.btnArtwork:
			goArtworksList();
			break;
		case R.id.btnContest:
			goContestList();
			break;
		case R.id.btnExpert:
			goExpertList();
			break;
//		case R.id.btnArtClass:	
//			goArtClassList();
//			break;
		case R.id.btnStory:
			goStoryList();
			break;
		case R.id.btnSketchbook:
			//if (AccountManager.isLogin()){
				Intent intent = new Intent(this, SketchbookActivity.class);
				DrawingPurpose purpose = new DrawingPurpose();
				purpose.reason = DrawingPurpose.NORMAL;
				
				intent.putExtra("drawingPurpose", purpose);
				this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
			//	
			break;
		case R.id.btnNotice:
			if (AccountManager.isLogin()){
				showNotifications(v);
			}else{
				BigtureEnvironment.showAlertMessage(this, 20);
			}
			break;
		case R.id.btnInfo:
			showInfo();
			break;
		case R.id.btnSettings:
			if (AccountManager.isLogin()){
				Intent intent2 = new Intent(this, SettingPopupActivity.class);
				startActivityForResult(intent2,BigtureEnvironment.REQ_CODE_SETTING);
			}else{
				BigtureEnvironment.showAlertMessage(this, 16);
			}	
			break;
		default:
			
		}
	}
	
	private void showInfo(){
		Intent intent = new Intent(this,InfoCenterPopupActivity.class);
		startActivity(intent);
	}
	
	private void goStoryList(){
		Intent intent = new Intent(this, StoryListActivity.class);
		startActivity(intent);
	}

	private void goArtworksList(){
		Intent intent = new Intent(this, ArtworkListActivity.class);
		startActivity(intent);
	}
	
	private void goContestList(){
		Intent intent = new Intent(this, ContestListActivity.class);
		startActivity(intent);
	}
	
	private void goExpertList(){
		Intent intent = new Intent(this, ExpertListActivity.class);
		startActivity(intent);
	}
	
	private void goArtClassList(){
		Intent intent = new Intent(this, ArtClassListActivity.class);
		startActivity(intent);
	}
	
	private void doAutoLogin(){
		AccountManager.getInstance().load();
		AccountEntity account = AccountManager.getInstance().getAccountEntity();
		if(account != null && account.keepLogin){
			//자동 로그인을 시도한다.
			AutoLoginTask task = new AutoLoginTask();
			//if(Build.VERSION.SDK_INT >= 11){
			//	task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,account.loginId,account.loginToken);
			//}else{
				task.execute(account.loginId,account.loginToken);				
			//}
			
		}
		
	}
	
	private void logout(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Notice");
		builder.setMessage(R.string.Logout_from_Bigture);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				new AsyncTask<Void,Void,Void>(){

					@Override
					protected Void doInBackground(Void... params)
					{
						LoginHandler.logout();
						return null;
					}

					@Override
					protected void onPostExecute(Void result)
					{
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
	
	@Override
	protected void onResume(){
		if (HttpUtil.isNetworkAvailable(MainActivity.this)){
			showProfile();

			new MainContentsTask().execute();
			
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("Network Error");
			builder.setMessage("There is no available network, Please check your network state.");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{	
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.create().show();
		}
		
		if (AccountManager.isLogin())
		{
			long messageIndex = getIntent().getLongExtra("messageIndex", 0);
			
			if (messageIndex > 0)
			{
				GCMManager.handleGCMessage(this, messageIndex);
				getIntent().putExtra("messageIndex", (long)0);
			}
		}

		super.onResume();
	}
	
	private void showProfile(){
		TextView welcomeLabel = (TextView)findViewById(R.id.welcomeLabel);
		TextView nameLabel = (TextView)findViewById(R.id.nameLabel);
		TextView logoutLabel = (TextView)findViewById(R.id.loginLabel);
		ImageView imageView = (ImageView)findViewById(R.id.imageProfile);

		if (AccountManager.isLogin()){
			AccountEntity entity = AccountManager.getInstance().entity;
			nameLabel.setVisibility(View.VISIBLE);
			welcomeLabel.setVisibility(View.VISIBLE);
			welcomeLabel.setText(R.string.label_welcome);
			nameLabel.setText(entity.nickName);
			//logoutLabel.setVisibility(View.VISIBLE);
			
			String imageURL = entity.getProfileImageURL();
			
			if (imageURL != null){
				BitmapFactory.Options profileBitmapOption = new BitmapFactory.Options();
				profileBitmapOption = new BitmapFactory.Options();
				profileBitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
				profileBitmapOption.inSampleSize = 2;
				
				BitmapDownloader.getInstance().displayImage(imageURL, profileBitmapOption, imageView, null);
			}
		}else{
			nameLabel.setVisibility(View.GONE);
			welcomeLabel.setText(R.string.label_create_or_login);
			
			logoutLabel.setVisibility(View.INVISIBLE);
			imageView.setImageResource(R.drawable.main_profile_photo);
		}
		
	
	}
	
	protected void closeSplashView(){
		SharedPreferences preferences = getApplicationContext().getSharedPreferences("Intro", Context.MODE_PRIVATE);
		boolean alreadyViewIntro = preferences.getBoolean("alreadyViewIntro", false);
		//alreadyViewIntro = false;
		if(!alreadyViewIntro){
			Intent intent = new Intent(this,LookActivity.class);
			intent.putExtra("fromFirst", true);
			startActivity(intent);
			
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					SplashView splashView = (SplashView)findViewById(R.id.splashView1);
					splashView.setVisibility(View.GONE);
					splashView.clearBitmap();
					
					RelativeLayout layout = (RelativeLayout)getWindow().getDecorView().findViewById(R.id.root_view);
					layout.removeView(splashView);
				}
				
			},1000);
			
		}else{
			SplashView splashView = (SplashView)findViewById(R.id.splashView1);
			splashView.setVisibility(View.GONE);
			splashView.clearBitmap();
			
			RelativeLayout layout = (RelativeLayout)getWindow().getDecorView().findViewById(R.id.root_view);
			layout.removeView(splashView);
		}
	}
	
	static class MessageHandler extends Handler{
		MainActivity activity;
		
		public MessageHandler(MainActivity activity){
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message msg)
		{
			if (msg.what == BigtureEnvironment.EVENT_CODE_CLOSE_SPLASH)
			{
				activity.closeSplashView();
			}
			
			super.handleMessage(msg);
		}
	}
	
	class FriendDataTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			AccountEntity entity = AccountManager.getInstance().getAccountEntity();
			entity.likeYous = FriendHandler.getLikeYou(null,null,null);
			entity.friendGroups = FriendHandler.findGroups();
			entity.store(mContext);
			
			return null;
		}
		
		
	}
	
	class AutoLoginTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = false;
			String loginId = params[0];
			String loginToken = params[1];
			
			AccountEntity entity = LoginHandler.login(mContext, loginId, loginToken);
			if (entity.success){

				AccountManager.getInstance().setAccountEntity(entity);
				AccountManager.getInstance().setLogin(true);
				entity.store(mContext);
				result = true;
			}
			
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result){
				AccountEntity account = AccountManager.getInstance().getAccountEntity();
				UpdateRegistTask taskU = new UpdateRegistTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					taskU.executeOnExecutor(THREAD_POOL_EXECUTOR,account.index);
				}else{
					taskU.execute(account.index);					
				}
				
				AccountManager.getInstance().setLogin(result);
				
				FriendDataTask task = new FriendDataTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(THREAD_POOL_EXECUTOR);
				}else{
					task.execute();				
				}
				
				
				Bundle bundle = getIntent().getExtras();
				if (bundle != null && bundle.containsKey("messageIndex")){
					long messageIndex = bundle.getLong("messageIndex");
					GCMManager.handleGCMessage(MainActivity.this, messageIndex);
					getIntent().putExtra("messageIndex", (long)0);
				}
				
				
			}
			super.onPostExecute(result);
		}
	}

	class UpdateRegistTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String regId = GCMManager.getRegistrationId(MainActivity.this);
			
			if (regId == null)
				GCMManager.registerGCM(MainActivity.this);
			else
				GCMServiceHandler.updateGCMRegistrationId(params[0], regId);
			
			return true;
		}
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				finish();
				return true;
			}
		}
		
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	class MainContentsTask extends AsyncTask<Void, Void, MainContentsEntity>{

		@Override
		protected MainContentsEntity doInBackground(Void... params) {
			return MainHandler.getMainContents();
		}

		@Override
		protected void onPostExecute(MainContentsEntity result) {
			super.onPostExecute(result);
			
			BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
			bitmapOption = new BitmapFactory.Options();
			bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmapOption.inSampleSize = 2;
			
			BitmapFactory.Options bitmapOption2 = new BitmapFactory.Options();
			bitmapOption2 = new BitmapFactory.Options();
			bitmapOption2.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmapOption2.inSampleSize = 1;
			
			
			if(result.getContests().size() > 0){
				ContestEntity entity = result.getContests().get(0);
				ImageView contestImage = (ImageView)findViewById(R.id.contestImage);
				TextView title = (TextView)findViewById(R.id.contestTitle);
				title.setText(entity.mainTitle);
				String imageUrl = entity.getThumbnailURL();
				if(imageUrl != null)
					BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, contestImage, null);
				
				contestImage.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						goContestList();
					}
				});
				title.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						goContestList();
						
					}
				});
			}
			
			if(result.getExperts().size() > 0){
				SimpleUserEntity entity = result.getExperts().get(0);
				ImageView expertImage = (ImageView)findViewById(R.id.expertImage);
				TextView expertName = (TextView)findViewById(R.id.expertName);
				expertName.setText(entity.name);
				String imageUrl = entity.getProfileImageURL();
				if(imageUrl != null){
					BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption2, expertImage, null);
				}
				expertImage.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						goExpertList();
					}
				});
				expertName.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						goExpertList();
					}
				});
			}
			
			
			displayArtwork(result.getArtworks());
			displayStory(result.getStories());
			displayClass(result.getClasses());
		}
	}
	
	private void displayArtwork(ArrayList<ArtworkEntity> artworks){
		BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 4;
		
		View[] artworkWrappers = new View[2];
		artworkWrappers[0] = findViewById(R.id.artwork1);
		artworkWrappers[1] = findViewById(R.id.artwork2);
		
		ImageView[] artworkImages = new ImageView[2];
		artworkImages[0] = (ImageView)findViewById(R.id.artworkImage1);
		artworkImages[1] = (ImageView)findViewById(R.id.artworkImage2);
		
		TextView[] artworkTitles = new TextView[2];
		artworkTitles[0] = (TextView)findViewById(R.id.artworkTitle1);
		artworkTitles[1] = (TextView)findViewById(R.id.artworkTitle2);
		
		
		for(int i=0; i < artworks.size(); i++){
			ArtworkEntity entity = artworks.get(i);
			if (i >= artworks.size()){
				artworkWrappers[i].setVisibility(View.INVISIBLE);				
				continue;
			}
			artworkWrappers[i].setVisibility(View.VISIBLE);
			artworkWrappers[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					goArtworksList();
				}
			});
			
			artworkTitles[i].setText(entity.title);
			String imageUrl = entity.getArtworkImageURL();
			if(imageUrl != null){
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, artworkImages[i], null);
			}
		}
	}
	
	private void displayStory(ArrayList<StoryEntity> stories){
		BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 4;
	
		View[] storyWrappers = new View[3];
		storyWrappers[0] = findViewById(R.id.story1);
		storyWrappers[1] = findViewById(R.id.story2);
		storyWrappers[2] = findViewById(R.id.story3);
		
		ImageView[] images = new ImageView[3];
		images[0] = (ImageView)findViewById(R.id.storyImage1);
		images[1] = (ImageView)findViewById(R.id.storyImage2);
		images[2] = (ImageView)findViewById(R.id.storyImage3);
		
		TextView[] titles = new TextView[3];
		titles[0] = (TextView)findViewById(R.id.storyTitle1);
		titles[1] = (TextView)findViewById(R.id.storyTitle2);
		titles[2] = (TextView)findViewById(R.id.storyTitle3);
		
		TextView[] owners = new TextView[3];
		owners[0] = (TextView)findViewById(R.id.storyOwner1);
		owners[1] = (TextView)findViewById(R.id.storyOwner2);
		owners[2] = (TextView)findViewById(R.id.storyOwner3);
		
		
		for(int i=0; i < stories.size(); i++){
			StoryEntity entity = stories.get(i);
			if (i >= stories.size()){
				storyWrappers[i].setVisibility(View.INVISIBLE);				
				continue;
			}
			storyWrappers[i].setVisibility(View.VISIBLE);
			storyWrappers[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					goStoryList();
				}
			});
			titles[i].setText(entity.title);
			owners[i].setText(entity.ownerName);
			
			String imageUrl = entity.getCoverImageURL();
			if(imageUrl != null){
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, images[i], null);
			}
		}
	}
	
	private void displayClass(ArrayList<ArtClassEntity> classes){
		BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
		bitmapOption = new BitmapFactory.Options();
		bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOption.inSampleSize = 4;
	
		View[] wrappers = new View[2];
		wrappers[0] = findViewById(R.id.class1);
		wrappers[1] = findViewById(R.id.class2);
		
//		
//		ImageView[] images = new ImageView[2];
//		images[0] = (ImageView)findViewById(R.id.classImage1);
//		images[1] = (ImageView)findViewById(R.id.classImage2);
//	
//		
//		TextView[] titles = new TextView[2];
//		titles[0] = (TextView)findViewById(R.id.classTitle1);
//		titles[1] = (TextView)findViewById(R.id.classTitle2);
//		
//		
//		TextView[] owners = new TextView[2];
//		owners[0] = (TextView)findViewById(R.id.classOwner1);
//		owners[1] = (TextView)findViewById(R.id.classOwner2);
	
		
		
//		for(int i=0; i < classes.size(); i++){
//			ArtClassEntity entity = classes.get(i);
//			if (i >= classes.size()){
//				wrappers[i].setVisibility(View.INVISIBLE);				
//				continue;
//			}
//			wrappers[i].setVisibility(View.VISIBLE);
//			wrappers[i].setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					goArtClassList();
//				}
//			});
//			titles[i].setText(entity.className);
//			owners[i].setText(entity.ownerName);
//			
//			String imageUrl = entity.getCoverImageURL();
//			if(imageUrl != null){
//				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOption, images[i], null);
//			}
//		}
	}
	
}
