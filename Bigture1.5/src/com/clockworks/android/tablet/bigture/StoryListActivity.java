package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertCellListAdapter;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertCellListAdapter.ExpertClickListener;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment.OnTabMenuSelectionListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.fragment.story.StoryAllListFragment;
import com.clockworks.android.tablet.bigture.fragment.story.StoryListFragment;
import com.clockworks.android.tablet.bigture.fragment.story.StoryTabFragment;
import com.clockworks.android.tablet.bigture.fragment.story.StoryTabFragment.StoryTabListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ExpertHandler;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class StoryListActivity extends AbstractBigtureActivity implements TitleBarListener,OnTabMenuSelectionListener,StoryTabListener,ExpertClickListener{
	private Context context;
	private TitleBar titleBar;
	
	final static public int TAB_OWNED = 0;
	final static public int TAB_JOINED = 1;
	final static public int TAB_COLLECTION = 2;
	final static public int TAB_ALL = 3;
	private boolean isPro;
	private int currentTab;
	private String currentSortOption = "RECENT";
	private Fragment listFragment;
	private StoryTabFragment tabFragment;
	private View expertListFrame;
	private ExpertCellListAdapter expertAdapter;
	private ExpertListTask findTask;
	private String selectedExpertId;
	private ListView expertListView;
	private int currentPage;
	private EditText editKeyword;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		this.context = this;
		this.selectedExpertId = null;
		this.currentPage = 1;
		
		setContentView(R.layout.activity_story_list);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.hideDrawButton();
		titleBar.setTitle(getResources().getString(R.string.common_lb_menu_artstory));
		
		this.expertListFrame = findViewById(R.id.expertListFrame);
		this.isPro = AccountManager.isExpert();
		
		this.tabFragment = StoryTabFragment.newInstance(this.isPro);
		if(this.isPro){
			currentTab = TAB_OWNED;
		}else{
			currentTab = TAB_JOINED;
		}
		
		this.editKeyword = (EditText)findViewById(R.id.editKeyword);
		editKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				currentPage = 1;
				if (s.toString().length() > 1){
					searchUser(s.toString());
				}else{
					searchUser(null);
					//listView.setAdapter(null);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});
		this.expertListView = (ListView)findViewById(R.id.expertListView);
		this.expertAdapter = new ExpertCellListAdapter(context,this);
		this.expertListView.setAdapter(expertAdapter);
		
		this.listFragment = StoryListFragment.newInstance(currentTab);
		
		getSupportFragmentManager().beginTransaction().add(R.id.tabBarContainer, tabFragment).add(R.id.listContainer,listFragment).commitAllowingStateLoss();
		
	}


	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
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

	

	@Override
	public void onSelectExpert(SimpleUserEntity entity) {
		this.selectedExpertId = entity.index;
		
		if(currentTab == TAB_ALL){
			StoryAllListFragment lf = (StoryAllListFragment)this.listFragment;
			lf.refreshListByExpert(this.selectedExpertId);
		}
	}


	@Override
	public void onMenuSelected(HorizontalTabFragment tabMenu, int position) {
		int oldTab = this.currentTab;
		if(this.isPro){
			this.currentTab = position;
		}else{
			this.currentTab = position+1;
		}
		
		//이전의 tab이 ALL에서 다른데로 옮겼거나 아니면 ALL로 옮겼으면 Fragment를 변경해야한다.
		
		if(oldTab != currentTab){
			if(oldTab == TAB_ALL){
				if(currentTab != TAB_ALL){
					this.expertListFrame.setVisibility(View.GONE);
					this.listFragment = StoryListFragment.newInstance(currentTab);
					getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
				}
			}else{
				if(currentTab == TAB_ALL){
					//AllListFragement를 생성해야한다.
					currentSortOption = "RECENT";
					tabFragment.changeSortMenu(getResources().getString(R.string.label_recent));
					this.listFragment= StoryAllListFragment.newInstance(currentSortOption);
					getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
				}else{
					//단순하게 현재 list만 바꿔야한다.
					refreshList(1);
				}
			}
		}
		changeTabExtraMenu();
		
	}

	private void refreshList(int currentPage){
		
		if(currentTab == TAB_ALL){
			StoryAllListFragment lf = (StoryAllListFragment)this.listFragment;
			lf.refreshList(currentPage, currentSortOption);
//			
		}else{
			StoryListFragment lf = (StoryListFragment)this.listFragment;
			lf.refreshList(currentTab, currentPage);
		}
	}
	

	@Override
	public void onSelectSortOption(String sortOption) {
		this.currentSortOption = sortOption;
		if(sortOption.equals("EXPERTNAME")){
			searchUser(null);
			this.expertListFrame.setVisibility(View.VISIBLE);
		}else{
			this.expertListFrame.setVisibility(View.GONE);
		}
		refreshList(1);
	}

	
	//우측상단에 sortOption과 status등을 변경한다.
	private void changeTabExtraMenu(){
		if(currentTab == TAB_ALL){
			//status를 hide하고 sortOption을 보이게한다.
			this.tabFragment.showSortMenu();
			
		}else{
			//sortOption을 hide하고 status를 보이게한다.
			this.tabFragment.hideSortMenu();
		}
		
	}
	
	private void searchUser(String keyword){

		if (findTask != null)
			findTask.cancelTask();
		
		findTask = new ExpertListTask();
		findTask.execute(keyword);
		
	}
	
	class ExpertListTask extends AsyncTask<String, Void,ArrayList<SimpleUserEntity>>{
		boolean canceled = false;
		
		@Override
		protected ArrayList<SimpleUserEntity> doInBackground(String... params) {
			
			return ExpertHandler.findByKeyword(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<SimpleUserEntity> result) {
			
			super.onPostExecute(result);
			if(!canceled){
				expertAdapter.setExperts(result);
				expertAdapter.notifyDataSetChanged();
			}
		}
		
		public void cancelTask(){
			this.cancel(true);
			canceled = true;
		}
	}
}
