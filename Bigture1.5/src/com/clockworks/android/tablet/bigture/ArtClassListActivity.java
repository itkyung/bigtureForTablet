package com.clockworks.android.tablet.bigture;


import android.os.Bundle;
import android.support.v4.app.Fragment;



import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassAllListFragment;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassListFragment;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassTabFragment;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassTabFragment.ArtClassTabListener;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment.OnTabMenuSelectionListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;


public class ArtClassListActivity extends AbstractBigtureActivity implements TitleBarListener,ArtClassTabListener,OnTabMenuSelectionListener {
	private TitleBar titleBar;
	
	private ArtClassTabFragment tabFragment;
	
	final static public int TAB_OWNED = 0;
	final static public int TAB_JOINED = 1;
	final static public int TAB_COLLECTION = 2;
	final static public int TAB_ALL = 3;
	private boolean isPro;
	private int currentTab;
	private String currentStatus = "ALL";
	private String currentSortOption = "RECENT";
	private Fragment listFragment;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_artclass_list);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.hideDrawButton();
		titleBar.setTitle("Art class");
		this.isPro = AccountManager.isExpert();
	
		
		this.tabFragment = ArtClassTabFragment.newInstance(this.isPro);
		if(this.isPro){
			currentTab = TAB_OWNED;
		}else{
			currentTab = TAB_JOINED;
		}
		
		this.listFragment = ArtClassListFragment.newInstance(currentTab, currentStatus);
		
		getSupportFragmentManager().beginTransaction().add(R.id.tabBarContainer, tabFragment).add(R.id.listContainer,listFragment).commitAllowingStateLoss();
		
		
	}
	
	
	private void refreshList(int currentPage){
		
		if(currentTab == TAB_ALL){
			ArtClassAllListFragment lf = (ArtClassAllListFragment)this.listFragment;
			lf.refreshList(currentPage, currentSortOption);
			
		}else{
			ArtClassListFragment lf = (ArtClassListFragment)this.listFragment;
			lf.refreshList(currentTab, currentPage, currentStatus);
		}
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
	public void onSelectSortOption(String sortOption) {
		this.currentSortOption = sortOption;
		refreshList(1);
		
	}


	@Override
	public void onSelectStatus(String status) {
		this.currentStatus = status;
		refreshList(1);
		
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
				//ArtclassListFragment를 생성해야한다. 
					this.listFragment = ArtClassListFragment.newInstance(currentTab, currentStatus);
					getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
				}
			}else{
				if(currentTab == TAB_ALL){
					//AllListFragement를 생성해야한다.
					this.listFragment= ArtClassAllListFragment.newInstance(currentSortOption);
					getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
				}else{
					//단순하게 현재 list만 바꿔야한다.
					refreshList(1);
				}
			}
		}
		changeTabExtraMenu();
		
	}

	//우측상단에 sortOption과 status등을 변경한다.
	private void changeTabExtraMenu(){
		if(currentTab == TAB_ALL){
			//status를 hide하고 sortOption을 보이게한다.
			this.tabFragment.hideStatusMenu();
			this.tabFragment.showSortMenu();
			
		}else{
			//sortOption을 hide하고 status를 보이게한다.
			this.tabFragment.hideSortMenu();
			this.tabFragment.showStatusMenu();
		}
		
	}

}
