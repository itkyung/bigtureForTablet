package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.contest.ContestListAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ContestHandler;
import com.clockworks.android.tablet.bigture.views.contest.ContestListCell;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

public class ContestListActivity extends AbstractBigtureActivity implements TitleBarListener,ContestListCell.ContestCallback{
	private ArrayList<ContestEntity> contestList;
	private PullToRefreshListView mPullRefreshListView;
	private TitleBar titleBar;
	private int currentPage;
	private ContestListAdapter adapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
	
		super.onCreate(arg0);
		currentPage = 1;
		
		setContentView(R.layout.activity_contest_list);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.hideDrawButton();
		titleBar.setTitle(getResources().getString(R.string.mybigture_po_contest));
		
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.listView1);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshContest();
			}
			
		});
		
		this.adapter = new ContestListAdapter(this,this);
		mPullRefreshListView.setAdapter(adapter);
		
		refreshContest();
	}

	private void refreshContest(){
		ContestListTask task =  new ContestListTask();
		task.execute();
	}
	
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		finish();
		
	}

	@Override
	public void requestToContest(ContestEntity contest) {
		if (!AccountManager.isLogin()){
			BigtureEnvironment.showAlertMessage(this, 20);
			return;
		}
		
		Intent intent = new Intent(this, SketchbookActivity.class);
		DrawingPurpose purpose = new DrawingPurpose();
		purpose.reason = DrawingPurpose.CONTEST;
		purpose.contestId = contest.index;
		
		intent.putExtra("drawingPurpose", purpose);
		this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
		
	}
	
	
	@Override
	public void viewDetail(ContestEntity contest) {
		Intent intent = new Intent(this, ContestDetailActivity.class);
		intent.putExtra("contest", contest);
		startActivity(intent);
	}


	class ContestListTask extends AsyncTask<Void, Void, ContestPageWrapper>{

		@Override
		protected ContestPageWrapper doInBackground(Void... params) {
			return ContestHandler.listAllContest(currentPage);
		}

		@Override
		protected void onPostExecute(ContestPageWrapper result) {
			
			super.onPostExecute(result);
			mPullRefreshListView.onRefreshComplete();
			adapter.setContests(result.contests);
			adapter.notifyDataSetChanged();
		}
		
		
	}
	
}
