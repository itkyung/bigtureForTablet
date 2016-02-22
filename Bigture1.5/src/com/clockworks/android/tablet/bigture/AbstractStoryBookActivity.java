package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.story.page.AfterReadPage;
import com.clockworks.android.tablet.bigture.views.story.page.BodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.CoverPage;
import com.clockworks.android.tablet.bigture.views.story.page.EditBodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.IllustratorPage;
import com.clockworks.android.tablet.bigture.views.story.page.PageIndicator;
import com.clockworks.android.tablet.bigture.views.story.page.ViewBodyPage;


/**
 * 하위클래스는 새로생성하는 화면 또는 draft된 story를 수정하는 화면 또는 실제로 story의 page들을 보는 화면일수 있다.
 * @author bizwave
 *
 */
public abstract class AbstractStoryBookActivity extends AbstractBigtureActivity implements ViewPager.OnPageChangeListener,TitleBarListener,StoryPageChanger{
	private TitleBar titleBar;
	public static final int PAGE_COVER = 0;
	public static final int PAGE_BODY = 1;
	public static final int PAGE_AFTERREADING = 2;
	public static final int PAGE_ILLUSTRATOR = 3;
	
	public static final int EDIT_MODE = 1;
	public static final int VIEW_MODE = 2;
	
	protected int currentMode;
	
	protected StoryEntity storyEntity;
	protected ArrayList<StoryPageEntity> pageList;

	protected ViewPager viewPager;
	protected SeekBar seekbar;
	
	protected TextView pageText;
	
	protected ImageView iconInfo;
	protected View infoPopup;


	public CoverPage coverPage;
	public AfterReadPage afterReadPage;
	public IllustratorPage illustratorPage;
	protected List<BodyPage> recycledBodyPageList;
	protected ArrayList<StoryArtworkEntity> storyArtworkList;

	int currentPage = 0;
	int totalPage = 0;
	int displayPage = 1;	
	
	protected int pagerPageCount;
	protected boolean myBook; // Am I creator of this book?
	protected boolean myBigture;
	protected String ownerIdx; // book owner
	
	protected Context context;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.context = this;
		
		initView();
		
		if(illustratorPage != null){
			illustratorPage.setChanger(this);
		}
		
		this.titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideHomeButton();
		titleBar.hideDrawButton();
		if(storyEntity == null || storyEntity.storyId == null){
			titleBar.setTitle("Add my story");
		}else{
			titleBar.setTitle(storyEntity.title);
		}
		
		initPager();
		
	}
	
	/**
	 * 하위 클래스에서 반드시 수정모드이면 intent에서 데이타를 얻고 각종 view들을 초기화를 해야한다.
	 */
	protected abstract void initView();
	protected abstract BodyPage initBodyPageView();
	
	protected abstract void initAfterReadPage(StoryEntity storyEntity);
	
	protected void initPager(){
		seekbar = (SeekBar)findViewById(R.id.seekBar1);
		pageText = (TextView)findViewById(R.id.pageText);
		viewPager = (ViewPager)findViewById(R.id.viewPager1);
		viewPager.setOnPageChangeListener(this);
		recycledBodyPageList = new ArrayList<BodyPage>();
		
		if(storyEntity != null && storyEntity.storyId != null){
			StoryPageListTask task = new StoryPageListTask();
			task.execute(storyEntity.storyId);
		}else{
			pageList = new ArrayList<StoryPageEntity>();
			storyEntity.pages = pageList;
			setPager();
		}
		
		if(totalPage == 0)
			this.seekbar.setMax(totalPage);
		else
			this.seekbar.setMax(totalPage-1);
		this.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				if(totalPage == 0) return;
				
				viewPager.setCurrentItem(progress);
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
		
	}

	protected boolean isEditMode(){
		return currentMode == EDIT_MODE ? true : false;
	}
	
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int page) {
		if (page == 0){
			coverPage.updateArrow(storyEntity);
			//iconInfo.setVisibility(View.VISIBLE);
			onChaggePageType(PAGE_COVER,null);
		}else if (!isEditMode() && page == pagerPageCount - 1 && storyEntity.afterReading){
			//after reading페이지임.
			
			//iconInfo.setVisibility(View.VISIBLE);
			onChaggePageType(PAGE_AFTERREADING,null);
		}
		else if (!isEditMode() && (page == pagerPageCount - 2 && storyEntity.afterReading) 
				|| (page == pagerPageCount - 1 && !storyEntity.afterReading)){
			//illustrator페이지임.
			
			
			onChaggePageType(PAGE_ILLUSTRATOR,null);
			//iconInfo.setVisibility(View.VISIBLE);
		}else{ // body page
			
			BodyPage bodyPage = null;
			for (int i = 0; i < viewPager.getChildCount(); i++){
				View view = viewPager.getChildAt(i);
				int tag = (Integer)view.getTag();

				if (tag == page){
					bodyPage = (BodyPage)view;
					break;
				}
			}
			bodyPage.updateArrow(storyEntity, pageList.get(page-1));
			if(bodyPage instanceof ViewBodyPage){
				onChaggePageType(PAGE_BODY,((ViewBodyPage)bodyPage).getFirstArtwork());
				((ViewBodyPage)bodyPage).setFirstArtwork();
			}else{
				onChaggePageType(PAGE_BODY,null);
			}
		}

		setCurrentPage(page);
	}
	
	public void onChaggePageType(int pageType,StoryArtworkEntity artwork){
		//하위 클래스에서 구현해야함.
		
		
	}
	
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		finish();
	}

	public void onPageInitCompleted(){
		
	}
	
	
	public void setPager(){
		if(isEditMode()){
			pagerPageCount = 1 + pageList.size();
		}else{
			pagerPageCount = 2 + pageList.size() + (storyEntity.afterReading ? 1 : 0);
			if (storyEntity.afterReading){
				initAfterReadPage(storyEntity);
				afterReadPage.initView(storyEntity);
			}	
			illustratorPage.initView(storyEntity,myBigture);
		}

		setPageCount(pagerPageCount);

		StoryBookPageAdapter adapter = new StoryBookPageAdapter();
		viewPager.setAdapter(adapter);
		onPageInitCompleted();
	}

	class StoryPageListTask extends AsyncTask<String,Void,Void>{
		@Override
		protected Void doInBackground(String... params){
			String storyId = params[0];

			pageList = StoryHandler.getPageList(storyId);
			storyEntity.pages = pageList;


			if(!isEditMode()){
				storyArtworkList = StoryHandler.getStoryArtworkList(storyId);
				// group by PageId
				if (storyArtworkList != null){
					for (int i = 0; i < storyArtworkList.size(); i++){
						StoryArtworkEntity storyArtworkEntity = storyArtworkList.get(i);
						String pageId = storyArtworkEntity.pageId;

						for (int j = 0; j < pageList.size(); j++){
							if (pageList.get(j).pageId.equals(pageId)){
								pageList.get(j).addArtwork(storyArtworkEntity);
								break;
							}
						}
					}
				}
			}
			
			return null;
		}

		@Override
		protected void onPreExecute(){
			WaitDialog.showWailtDialog(context, false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void result){
			WaitDialog.sweepWaitDialog();
			
			setPager();

			super.onPostExecute(result);
		}
	}

	
	
	@Override
	protected void onDestroy() {
		if(!isEditMode()){
			//내부의 viewpager안의 bodypage안의 bitmap을 초기화해야한다.
			int count = viewPager.getChildCount();
			for(int i=0; i < count; i++){
				View view = viewPager.getChildAt(i);
				if(view instanceof ViewBodyPage){
					((ViewBodyPage)view).destorySubView();
				}
			}
		}
		super.onDestroy();
	}

	
	public void setPageCount(int pageCount){
		totalPage = pageCount;
		this.pageText.setText(displayPage + " / " + pageCount);
		this.seekbar.setMax(totalPage-1);

	}
	
	
	public void setCurrentPage(int page){
		currentPage = page;
		displayPage = currentPage+1;
		this.pageText.setText(displayPage + " / " + totalPage);
		this.seekbar.setProgress(page);

	}
	

	class StoryBookPageAdapter extends PagerAdapter{
		//public View currentView;

		@Override
		public int getCount(){
			if(isEditMode()){
				return 1 + pageList.size();
			}else{
				return 2 + pageList.size() + (storyEntity.afterReading ? 1 : 0);
			}
		}

		@Override
		public boolean isViewFromObject(View view, Object obj){
			return (view == obj);
		}

		@Override
		public int getItemPosition(Object object){
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object){

			if (object instanceof BodyPage){
				//recycledBodyPageList.add((BodyPage)object);
				if(object instanceof ViewBodyPage){
					//((ViewBodyPage)object).destorySubView();
				}
			}
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position){
			View view = null;
			
			if(isEditMode()){
				if (position == 0){
					view = coverPage;
				}else{
					BodyPage bodyPage = null;
					
//					if (recycledBodyPageList.size() > 0){
//						bodyPage = recycledBodyPageList.remove(0);
//					}else{
						bodyPage = initBodyPageView();
					//}
					
					bodyPage.initView(context, storyEntity,  pageList.get(position-1));
					view = bodyPage;
				}
				view.setTag(position);
				container.addView(view, 0);
				return view;
				
			}else{
			
				if (position == 0){
					view = coverPage;
				}else if (position == getCount() - 1 && storyEntity.afterReading){
					view = afterReadPage;
				}else if ( (position == getCount() - 2 && storyEntity.afterReading) || 
						(position == getCount() - 1 && !storyEntity.afterReading)){
					view = illustratorPage;
					illustratorPage.initView(storyEntity,myBigture);
				}else{
					BodyPage bodyPage = null;
	
					//if (recycledBodyPageList.size() > 0){
					//	bodyPage = recycledBodyPageList.remove(0);
					//}else{
						bodyPage = initBodyPageView();
					//}
					
					bodyPage.initView(context, storyEntity,  pageList.get(position-1));
					view = bodyPage;
				}
	
				view.setTag(position);
				container.addView(view, 0);
				return view;
			}
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object){
		//	currentView = (View)object;
			//super.setPrimaryItem(container, position, object);
		}

	}
	
	
}
