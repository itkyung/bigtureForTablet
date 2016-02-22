package com.clockworks.android.tablet.bigture;

import java.io.File;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.fragment.common.CommentFragment;
import com.clockworks.android.tablet.bigture.fragment.story.AfterWorksFragment;
import com.clockworks.android.tablet.bigture.fragment.story.NameCardFragment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.views.story.page.AfterReadPage;
import com.clockworks.android.tablet.bigture.views.story.page.BodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.IllustratorPage;
import com.clockworks.android.tablet.bigture.views.story.page.ViewBodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.ViewCoverPage;
import com.clockworks.android.tablet.bigture.views.story.page.ViewBodyPage.ViewBodyPageListener;

public class StoryBookViewActivity extends AbstractStoryBookActivity implements ViewBodyPageListener,ShowArtworkInterface{
	private CommentFragment commentFragment;
	private View rightFrameContainer;
	private Button infoBtn;
	private PopupWindow editContainer;
	private PopupWindow infoPopup;
	private boolean fragmentAdded;
	private int currentPageType;
	
	@Override
	protected void initView() {
		currentPageType = PAGE_COVER;
		fragmentAdded = false;
		this.currentMode = VIEW_MODE;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_story_book_view);
		this.storyEntity  = getIntent().getParcelableExtra("storyEntity");
		this.myBigture = getIntent().getBooleanExtra("myBigture", false);
		
		this.rightFrameContainer = findViewById(R.id.rightFrameContainer);
		this.commentFragment = (CommentFragment)getSupportFragmentManager().findFragmentById(R.id.commentFragment);
		
		this.infoBtn = (Button)findViewById(R.id.infoBtn);
		this.infoBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(infoPopup == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_story_info_popup, null);
					final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 360, getResources().getDisplayMetrics());
					final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 218, getResources().getDisplayMetrics());
					infoPopup = new PopupWindow(menuView,width,height);
					
					infoPopup.setOutsideTouchable(true);
					infoPopup.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
				bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
				bitmapOptions.inSampleSize = 3;
				
				infoPopup.dismiss();
				View mView = infoPopup.getContentView();
				
				TextView bookTitleView = (TextView)mView.findViewById(R.id.bookTitle);
				bookTitleView.setText(storyEntity.title);
				
				ImageView imageProfile = (ImageView)mView.findViewById(R.id.imageProfile);
				String profileUrl = storyEntity.getProfileImageURL();
				if(profileUrl != null){
					BitmapDownloader.getInstance().displayImage(profileUrl, bitmapOptions, imageProfile, null);
				}
				
				TextView jobLabel = (TextView)mView.findViewById(R.id.jobLabel);
				jobLabel.setText(storyEntity.ownerJob);
				
				TextView countryLabel = (TextView)mView.findViewById(R.id.countryLabel);
				countryLabel.setText(storyEntity.ownerCountry);
				
				TextView nameLabel = (TextView)mView.findViewById(R.id.nameLabel);
				nameLabel.setText(storyEntity.ownerName);
				
				infoPopup.setAnimationStyle(-1);
				infoPopup.showAsDropDown(v, 0, -250);
				
			}
		});
		
		
		LayoutInflater inflater = LayoutInflater.from(this);
		coverPage = (ViewCoverPage)inflater.inflate(R.layout.view_bigture_story_cover, null);
		coverPage.initView(storyEntity);
	
		
		toggleComment(false);
		
		illustratorPage = (IllustratorPage)inflater.inflate(R.layout.view_bigture_story_illustrator, null);
		
		
	}

	private void toggleComment(boolean show){
//		if(fragmentShown == show){
//			return;
//		}
		
		if(commentFragment.isHide() != show){
			return;
		}
		
		if(show){
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.show(commentFragment);
			ft.commit();
			commentFragment.setHide(false);
		}else{
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.hide(commentFragment);
			ft.commit();
			commentFragment.setHide(true);
		}
		
		
	}
	
	private void readComment(ArtworkEntity artwork){
		commentFragment.readComments(artwork);
	}
	
	@Override
	protected BodyPage initBodyPageView() {
		
		LayoutInflater inflater = LayoutInflater.from(this);
		ViewBodyPage bodyPage  = (ViewBodyPage)inflater.inflate(R.layout.view_bigture_story_body, null);
		bodyPage.setListener(this,myBigture);
		return bodyPage;
	}

	@Override
	protected void initAfterReadPage(StoryEntity storyEntity) {
		LayoutInflater inflater = LayoutInflater.from(this);
		afterReadPage = (AfterReadPage)inflater.inflate(R.layout.view_bigture_story_afterread, null);
		
	}

	//맨 처음을 제외하고 내부에서 artwork page가 변경될때 호출됨.
	@Override
	public void onPageArtworksChanged(StoryArtworkEntity artwork) {
		//우측에 comment창을 보이고 말고를 결정한다.
		if(artwork == null){
			//맨 마직막으로 comment를 감춘다.
			toggleComment(false);
		}else{
			toggleComment(true);
			readComment(artwork);
		}
	}

	/**
	 * pager내의 페이지의 종류에 따라서 우측 fragment를 다른걸로 보이게 한다.
	 */
	public void onChaggePageType(int pageType,StoryArtworkEntity artwork){
		currentPageType = pageType;
		switch(pageType){
		case PAGE_COVER:
			toggleComment(false);
			hideRightFragment();
			break;
		case PAGE_BODY:
			if(artwork == null)
				toggleComment(false);
			else{
				toggleComment(true);
				readComment(artwork);
			}
			hideRightFragment();
			break;
		case PAGE_ILLUSTRATOR:
			toggleComment(false);
			//name card를 보이게 힌다.
			changeRightFragement(pageType);
			break;
		case PAGE_AFTERREADING:
			toggleComment(false);
			//afterLeading을 보이게 한다.
			changeRightFragement(pageType);
			break;
		}
	}

	public void changeRightFragement(int pageType){
		
		rightFrameContainer.setVisibility(View.VISIBLE);
		Fragment fragment = null;
		
		if(pageType == PAGE_ILLUSTRATOR){
			fragment = NameCardFragment.newInstance(storyEntity, myBigture);
		}else{
			fragment = AfterWorksFragment.newInstance(storyEntity);
		}
		
		if(fragmentAdded){
			getSupportFragmentManager().beginTransaction().replace(R.id.rightFrameContainer, fragment).commitAllowingStateLoss();
		}else{
			getSupportFragmentManager().beginTransaction().add(R.id.rightFrameContainer, fragment).commitAllowingStateLoss();
			fragmentAdded = true;
		}
		
		
	}
	
	public void hideRightFragment(){
		rightFrameContainer.setVisibility(View.GONE);
	}
	
	
	public void onShowArtworkDetail(ArtworkEntity artwork) {
		ArrayList<ArtworkEntity> artworkList = new ArrayList<ArtworkEntity>();
		artworkList.add(artwork);
		
		Intent intent = new Intent(this,ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", 0);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(resultCode == RESULT_OK){
			if(requestCode == BigtureEnvironment.REQ_CODE_SKETCHBOOK){
				//page안에 그림을 완성한 경우.
				//현재 active한 페이지의 데이타를 얻어와서 추가해야한다.
				//intent에서 데이타를 얻어와야한다.
				
				
				int idx = viewPager.getCurrentItem();
				View view = viewPager.getChildAt(idx);
				if(view instanceof ViewBodyPage){
					//현재 viewBodypage의 pageEntity에 artworkList에 새로운 artwork를 추가한다.
					StoryPageEntity entity = ((ViewBodyPage)view).getPageEntity();
					PageArtworkTask task = new PageArtworkTask();
					task.execute(entity.pageId);
				}else if(currentPageType == PAGE_AFTERREADING){
					Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.rightFrameContainer);
					if(fragment instanceof AfterWorksFragment){
						((AfterWorksFragment)fragment).refresh();
					}
				}
			}
		}
	}
	
	class PageArtworkTask extends AsyncTask<String, Void, ArrayList<StoryArtworkEntity>>{

		@Override
		protected ArrayList<StoryArtworkEntity> doInBackground(String... params) {
			return StoryHandler.getStoryArtworkListByPage(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<StoryArtworkEntity> result) {
			super.onPostExecute(result);
			int idx = viewPager.getCurrentItem();
			View view = viewPager.getChildAt(idx);
			if(view instanceof ViewBodyPage){
				//현재 viewBodypage의 pageEntity에 artworkList에 새로운 artwork를 추가한다.
				((ViewBodyPage)view).addArtworkList(result);
			}
			
		}
		
		
	}
	
	
}
