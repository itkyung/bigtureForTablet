package com.clockworks.android.tablet.bigture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment.OnTabMenuSelectionListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.fragment.user.ArtClassListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.ArtworkListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.CareerNewsListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.LikeListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.MyBigtureTabFragment;
import com.clockworks.android.tablet.bigture.fragment.user.PostCardListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.StoryListFragment;
import com.clockworks.android.tablet.bigture.fragment.user.CareerNewsListFragment.CareerNewsListener;
import com.clockworks.android.tablet.bigture.fragment.user.MyBigtureTabFragment.MyBigtureTabListener;
import com.clockworks.android.tablet.bigture.fragment.user.PostCardListFragment.PostCardListListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.ProfileReadTask;


public class MyBigtureActivity extends AbstractBigtureActivity implements TitleBarListener,View.OnClickListener,
	OnTabMenuSelectionListener,CareerNewsListener,MyBigtureTabListener,PostCardListListener{

	private boolean isMyPage;
	private ProfileEntity profileEntity;
	
	private TitleBar titleBar;

	
	private String ownerId;
	private int currentViewIdx;
	
	private final int TAB_CAREER = 0;
	private final int TAB_ARTWORKS = 1;
	private final int TAB_STORY = 2;
	//private final int TAB_CLASS = 3;
	private final int TAB_LIKE = 3;
	private final int TAB_POSTBOX = 4;
	private MyBigtureTabFragment tabFragment;
	
	private Button friendBtn;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_bigture);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.hideDrawButton();
		
		context = this;
		this.friendBtn = (Button)findViewById(R.id.btnFriend);
		
		if (savedInstanceState == null){
			ownerId = getIntent().getExtras().getString("userId");
			currentViewIdx = getIntent().getExtras().getInt("viewIdx");
			isMyPage = AccountManager.isMyUserIndex(ownerId);
			
			readUserProfile(ownerId);
			
			
		}else{
			profileEntity = savedInstanceState.getParcelable("profileEntity");
			ownerId = profileEntity.index;
			isMyPage = AccountManager.isMyUserIndex(ownerId);
			showProfile(profileEntity,false);
			
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("profileEntity", profileEntity);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (profileEntity == null){
			profileEntity = savedInstanceState.getParcelable("profileEntity");
			ownerId = profileEntity.index;
			isMyPage = AccountManager.isMyUserIndex(ownerId);
			showProfile(profileEntity,false);
		}
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	private void showProfile(ProfileEntity profileEntity,boolean needFragementAdd){
		findViewById(R.id.friendButtonContainer).setVisibility(View.VISIBLE);
	
		titleBar.setTitle(profileEntity.nickName + "'s Bigture");

		TextView nameLabel = (TextView)findViewById(R.id.nameLabel);
		nameLabel.setText(profileEntity.nickName);					
		
		TextView countryLabel = (TextView)findViewById(R.id.countryLabel);
		if(AccountManager.getInstance().isKorean()){
			countryLabel.setText(profileEntity.countryNameKr);
		}else{
			countryLabel.setText(profileEntity.countryName);
		}
		
		

		// Comment
		TextView commentLabel = (TextView)findViewById(R.id.commentLabel);
		commentLabel.setText(profileEntity.comment);

		if (AccountManager.isMyUserIndex(profileEntity.index)){
			titleBar.setTitle(getResources().getString(R.string.common_lb_menu_mybigture));
			ImageButton button = (ImageButton)findViewById(R.id.btnEditProfile); 
			//button.setVisibility(View.VISIBLE);
			button.setOnClickListener(MyBigtureActivity.this);
			
			friendBtn.setVisibility(View.GONE);
			
		}else if (!profileEntity.likeYou){ 
		
			titleBar.setTitle(profileEntity.nickName + "'s Artroom");
			findViewById(R.id.btnEditProfile).setVisibility(View.GONE);
			
			friendBtn.setBackground(getResources().getDrawable(R.drawable.selector_btn_like_you_none));
			friendBtn.setVisibility(View.VISIBLE);
			friendBtn.setOnClickListener(MyBigtureActivity.this);
			
		}else{
			findViewById(R.id.btnEditProfile).setVisibility(View.GONE);
			
			friendBtn.setBackground(getResources().getDrawable(R.drawable.selector_btn_like_you));
			friendBtn.setVisibility(View.VISIBLE);
			friendBtn.setOnClickListener(MyBigtureActivity.this);
		}
		
		// Profile image
		ImageView profileImageView = (ImageView)findViewById(R.id.imageProfile);
		
		BitmapFactory.Options profileBitmapOption = new BitmapFactory.Options();
		profileBitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
		profileBitmapOption.inSampleSize = 3;

		String imageURL = profileEntity.getProfileImageURL();
		if (imageURL != null)
			BitmapDownloader.getInstance().displayImage(imageURL, profileBitmapOption, profileImageView, null);
		
		if(needFragementAdd){
			//TabFragment를 초기화한다.
			
			if(profileEntity.isPro ){
				this.tabFragment = MyBigtureTabFragment.newInstance(true,isMyPage);
			}else{
				this.tabFragment = MyBigtureTabFragment.newInstance(false,isMyPage);
			}
			
			Fragment listFragment = null;
			
			if(profileEntity.isPro){
				listFragment = changeListFragement(TAB_CAREER,false);
			}else{
				listFragment = changeListFragement(TAB_ARTWORKS,false);
			}
			
			getSupportFragmentManager().beginTransaction().add(R.id.tabBarContainer, tabFragment)
			.add(R.id.listContainer, listFragment).commitAllowingStateLoss();
			
		}	
	}
	
	public Fragment changeListFragement(int position,boolean needCommit){
		Fragment listFragment = null;
		if(position == TAB_CAREER){
			listFragment = CareerNewsListFragment.newInstance(profileEntity.index, isMyPage);
		}else if(position == TAB_ARTWORKS){
			listFragment = ArtworkListFragment.newInstance(profileEntity.index, isMyPage);
//		}else if(position == TAB_CLASS){
//			listFragment = ArtClassListFragment.newInstance(profileEntity.index, isMyPage, profileEntity.isPro);
		}else if(position == TAB_STORY){
			listFragment = StoryListFragment.newInstance(profileEntity.index, isMyPage, profileEntity.isPro);
			
		}else if(position == TAB_LIKE){
			listFragment = LikeListFragment.newInstance(profileEntity.index, isMyPage, profileEntity.groupCount);
			
		}else if(position == TAB_POSTBOX){
			listFragment = PostCardListFragment.newInstance(profileEntity.index, isMyPage);
		}
		
		if(needCommit){
			getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
			changeRightAction(position);
		}
		
		return listFragment;
	}
	
	/*
	 * 탭영역의 우측 메뉴를 바꾼다.
	 */
	public void changeRightAction(int position){
		if(position == TAB_CAREER){
			this.tabFragment.hideAllMenu();
		}else if(position == TAB_ARTWORKS){
			this.tabFragment.showArtworksMenu();
//		}else if(position == TAB_CLASS){
//			this.tabFragment.hideAllMenu();
		}else if(position == TAB_STORY){
			this.tabFragment.showStoryMenu();
			
		}else if(position == TAB_LIKE){
			this.tabFragment.showLikeMenu();
			
		}else if(position == TAB_POSTBOX){
			this.tabFragment.showCardMenu();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnEditProfile){
			//프로필 수정.
			
			
		}else if(v.getId() == R.id.btnFriend){
			//like you 
			if (!profileEntity.likeYou){ 	
				FriendLikeTask task = new FriendLikeTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,profileEntity.index);
				}else{
					task.execute(profileEntity.index);				
				}
			}else{
				FriendUnLikeTask task = new FriendUnLikeTask();
				
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,profileEntity.index);
				}else{
					task.execute(profileEntity.index);				
				}
				
			}
			
			
		}
	}

	// TitleBarListener 함수 구현.
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		Intent intent = new Intent(this, SketchbookActivity.class);
		this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
	}

	@Override
	public void onMessageClicked(TitleBar titleBar) {
		
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		finish();
	}

	
	// Tabbar Listener
	@Override
	public void onMenuSelected(HorizontalTabFragment tabMenu, int position) {
		//탭메뉴가 선택되었을때 호출된다.
		if(!profileEntity.isPro){
			position = position + 1;
		}
		changeListFragement(position,true);
	}

	private void readUserProfile(String userIndex){
		ProfileReadTask task = new ProfileReadTask(new ProfileReadTask.ProfileReadTaskListener(){
			@Override
			public void onSuccess(ProfileEntity profileEntity){
				MyBigtureActivity.this.profileEntity = profileEntity;

				showProfile(profileEntity,true);
			}

			@Override
			public void onFail(){
				
			}
			
		});
		
		task.execute(userIndex, AccountManager.getUserIdx());
	}

	
	// News Listener
	@Override
	public void clickAddNews() {
		Intent intent = new Intent(MyBigtureActivity.this, NewsEditActivity.class);
		
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_NEWS);
	}

	@Override
	public void editNews(NewsEntity entity) {
		Intent intent = new Intent(MyBigtureActivity.this, NewsEditActivity.class);
		intent.putExtra("newsEntity", (Parcelable)entity);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_EDIT_NEWS);
		
	}

	// Career Listener
	@Override
	public void clickAddCareer() {
		Intent intent = new Intent(MyBigtureActivity.this, CareerEditActivity.class);
		
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_CAREER);
	}

	@Override
	public void editCareer(CareerEntity entity) {
		Intent intent = new Intent(MyBigtureActivity.this, CareerEditActivity.class);
		intent.putExtra("careerEntity", (Parcelable)entity);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_EDIT_CAREER);
	}

	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != RESULT_OK){
			return;
		}
		if(requestCode == BigtureEnvironment.EVENT_CODE_ADD_CAREER ||
				requestCode == BigtureEnvironment.EVENT_CODE_EDIT_CAREER){
			//career list를 refresh한다.
			Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
			if(f instanceof CareerNewsListFragment){
				((CareerNewsListFragment) f).refreshCareer();
			}
		}else if(requestCode == BigtureEnvironment.EVENT_CODE_ADD_NEWS ||
				requestCode == BigtureEnvironment.EVENT_CODE_EDIT_NEWS){
			Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
			if(f instanceof CareerNewsListFragment){
				((CareerNewsListFragment) f).refreshNews();
			}
		}else if(requestCode == BigtureEnvironment.EVENT_CODE_FIND_USER){
			Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
			if(f instanceof LikeListFragment){
				((LikeListFragment) f).refreshData(0);
				
			}
		}
		
		
		super.onActivityResult(requestCode,resultCode,data);
	}

	
	
	@Override
	public void openFindFriendPopup() {
		
		Intent intent = new Intent(MyBigtureActivity.this, FindUserPopupActivity.class);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_FIND_USER);
	}

	@Override
	public void selectArtworksPopupMenu(String type, String sortOption) {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
		if(f instanceof ArtworkListFragment){
			((ArtworkListFragment) f).refreshArtworks(type, sortOption,false);
		}
	}

	@Override
	public void selectStoryPopupMenu(String status) {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
		if(f instanceof StoryListFragment){
			((StoryListFragment) f).refreshStory(status, null,false);
		}
		
	}

	@Override
	public void selectCardPopupMenu(String listType) {
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.listContainer);
		if(f instanceof PostCardListFragment){
			if(listType.equals("RECEIVED")){
				((PostCardListFragment) f).getReceivedData();
			}else{
				((PostCardListFragment) f).getSendedData();
			}
		}
	}

	public void updateLikeBtn(){
		if (profileEntity.likeYou){ 	
			this.friendBtn.setBackground(getResources().getDrawable(R.drawable.selector_btn_like_you)); 
		}else{
			this.friendBtn.setBackground(getResources().getDrawable(R.drawable.selector_btn_like_you_none));
		}
	}
	
	@Override
	public void changeCardMenuText(String txt, int idx) {
		this.tabFragment.changeCardMenuText(txt, idx);
	}

	class FriendLikeTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return FriendHandler.likeYou(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast.makeText(context, getResources().getString(R.string.mybigture_tp_likeu), Toast.LENGTH_SHORT).show();
				profileEntity.likeYou = true;
				updateLikeBtn();
			}else{
				
			}
			
			
		}
	}
	
	class FriendUnLikeTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return FriendHandler.unlike(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result){
				Toast.makeText(context, getResources().getString(R.string.mybigture_tp_likeu_un), Toast.LENGTH_SHORT).show();
				profileEntity.likeYou = false;
				updateLikeBtn();
			}else{
				
			}
			
			
		}
	}
	
}
