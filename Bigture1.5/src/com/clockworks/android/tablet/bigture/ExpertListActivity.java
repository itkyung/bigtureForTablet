package com.clockworks.android.tablet.bigture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment.OnTabMenuSelectionListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.fragment.expert.ExpertListByJobFragment;
import com.clockworks.android.tablet.bigture.fragment.expert.ExpertListFragment;
import com.clockworks.android.tablet.bigture.fragment.expert.ExpertTabFragment;
import com.clockworks.android.tablet.bigture.fragment.expert.ExpertTabFragment.ExpertTabListener;


public class ExpertListActivity extends AbstractBigtureActivity implements TitleBarListener,OnTabMenuSelectionListener,ExpertTabListener{
	private Context context;
	private TitleBar titleBar;
	private ExpertTabFragment tabFragment;
	private int currentViewIdx;
	private final int TAB_NAME = 0;
	private final int TAB_JOB = 1;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
	
		super.onCreate(arg0);
		this.context = this;
		currentViewIdx = 0;
		
		setContentView(R.layout.activity_expert_list);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.hideDrawButton();
		titleBar.setTitle(getResources().getString(R.string.menu_experts));
		
		
		this.tabFragment = new ExpertTabFragment();
		
		Fragment listFragment = changeListFragement(TAB_NAME,false);
		
		getSupportFragmentManager().beginTransaction().add(R.id.tabBarContainer, tabFragment)
		.add(R.id.listContainer, listFragment).commitAllowingStateLoss();
		
		
	}

	
	public Fragment changeListFragement(int position,boolean needCommit){
		Fragment listFragment = null;
		if(position == TAB_NAME){
			listFragment = new ExpertListFragment();
		}else if(position == TAB_JOB){
			listFragment = new ExpertListByJobFragment();
		}
		
		if(needCommit){
			getSupportFragmentManager().beginTransaction().replace(R.id.listContainer, listFragment).commitAllowingStateLoss();
		}
		
		return listFragment;
	}
	
	
	@Override
	public void openFindExperts() {
		Intent intent = new Intent(this, FindExpertsPopupActivity.class);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_FIND_USER);
	}

	@Override
	public void onMenuSelected(HorizontalTabFragment tabMenu, int position) {
		currentViewIdx = position;
		changeListFragement(position, true);
		
	}

	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
		
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		
		
	}

	@Override
	public void onMessageClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		finish();
		
	}


}
