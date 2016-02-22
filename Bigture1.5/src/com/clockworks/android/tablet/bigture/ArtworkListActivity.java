package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artwork.ArtworkAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.fragment.artwork.ArtworkTabFragment;
import com.clockworks.android.tablet.bigture.fragment.artwork.ArtworkTabFragment.ArtworkTabListener;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment.OnTabMenuSelectionListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;

public class ArtworkListActivity extends AbstractBigtureActivity implements TitleBarListener,ArtworkTabListener,OnTabMenuSelectionListener,ArtworkThumbnail.Callback{
	private TitleBar titleBar;
	private ArtworkTabFragment tabFragment;
	final private int TAB_ALL = 0;
	final private int TAB_FRIEND = 1;
	final private int TAB_COLLECTION = 2;
	
	private ListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private String currentSortOption = "RECENT";
	private int currentPage = 1;
	private int currentTab = TAB_ALL;
	private ArtworkAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_artwork_list);
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideBackButton();
		titleBar.showDrawButton();
		titleBar.setTitle(getResources().getString(R.string.menu_artworks));
		
		
		this.mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.listView1);
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshArtworks();
			}
			
		});
		
		
		this.listView = mPullRefreshListView.getRefreshableView();
		
		this.mAdapter = new ArtworkAdapter(this, this);
		this.mPullRefreshListView.setAdapter(mAdapter);
		
		this.tabFragment = new ArtworkTabFragment();
		
		getSupportFragmentManager().beginTransaction().add(R.id.tabBarContainer, tabFragment).commitAllowingStateLoss();
		
		refreshArtworks();
		
	}

	public void refreshArtworks(){
		ArtworkListTask task = new ArtworkListTask();
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,currentTab);
		}else{
			task.execute(currentTab);				
		}
	
	}
	
	@Override
	public void onMenuSelected(HorizontalTabFragment tabMenu, int position) {
		this.currentTab = position;
		currentPage = 1;
		refreshArtworks();
	}


	@Override
	public void onSelectSortOption(String sortOption) {
		currentPage = 1;
		this.currentSortOption = sortOption;
		refreshArtworks();
	}
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
		
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		Intent intent = new Intent(this, SketchbookActivity.class);
		DrawingPurpose purpose = new DrawingPurpose();
		purpose.reason = DrawingPurpose.NORMAL;
		
		intent.putExtra("drawingPurpose", purpose);
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
	public void onShowArtworkDetail(ArtworkEntity artwork) {
		ArrayList<ArtworkEntity> artworkList = this.mAdapter.getArtworkList();
		if(artworkList == null){
			//TODO 에러처리필요함.
			
			return;
		}
		int index = -1;
		for(int i=0; i < artworkList.size(); i++){
			ArtworkEntity e = artworkList.get(i);
			if(artwork.artworkId.equals(e.artworkId)){
				index = i;
				break;
			}
		}
		
		Intent intent = new Intent(this,ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", index);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
		
	}

	@Override
	public void onAddToCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		WaitDialog.showWailtDialog(this, false);
		AddCollectionTask task = new AddCollectionTask();
		task.execute(entity.artworkId);
	}

	@Override
	public void onRemoveFromCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		WaitDialog.showWailtDialog(this, false);
		RemoveCollectionTask task = new RemoveCollectionTask();
		task.execute(entity.artworkId);
	}

	@Override
	public void onNextPage(int crntPage) {
		this.currentPage = crntPage+1;
		refreshArtworks();
	}

	@Override
	public void onPrevPage(int crntPage) {
		this.currentPage = crntPage-1;
		refreshArtworks();
	}

	class AddCollectionTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.addCollection(params[0]);
		}

		@Override
		protected void onCancelled() {
		
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
	
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			refreshArtworks();
		}
	}

	class RemoveCollectionTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.removeCollection(params[0]);
		}

		@Override
		protected void onCancelled() {
		
			super.onCancelled();
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
	
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			refreshArtworks();
		}
	}
	
	class ArtworkListTask extends AsyncTask<Integer, Void, ArtworkPageWrapper>{
		
		@Override
		protected void onCancelled() {
		
			super.onCancelled();
			mPullRefreshListView.onRefreshComplete();
		}

		@Override
		protected void onCancelled(ArtworkPageWrapper result) {
		
			super.onCancelled(result);
			mPullRefreshListView.onRefreshComplete();
		}

		@Override
		protected ArtworkPageWrapper doInBackground(Integer... params) {
			int typeInt = params[0];
			ArtworkPageWrapper pageWrapper = null;
			switch(typeInt){
			case TAB_ALL:
				pageWrapper = ArtworkHandler.listAllArtworks(null, currentSortOption, currentPage);
				break;
			case TAB_FRIEND:
				pageWrapper = ArtworkHandler.listFriendsArtworks(currentSortOption, currentPage);
				break;
			case TAB_COLLECTION:
				pageWrapper = ArtworkHandler.listCollectedArtworks(currentSortOption, currentPage);
				break;
			}
			
			return pageWrapper;
		}

		@Override
		protected void onPostExecute(ArtworkPageWrapper pageWrapper) {
			super.onPostExecute(pageWrapper);
			mPullRefreshListView.onRefreshComplete();
			mAdapter.setArtworkList(pageWrapper.artworks, pageWrapper.totalPage, currentPage);
			mAdapter.notifyDataSetChanged();
		}
	
	}

	@Override
	public void onAddClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		if(resultCode == RESULT_OK){
			refreshArtworks();
		}
	}
	
	
}
