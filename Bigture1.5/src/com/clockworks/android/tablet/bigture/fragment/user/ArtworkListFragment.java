package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtworkDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artwork.UserArtworkAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtworkListTask;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ArtworkListFragment extends Fragment implements ArtworkThumbnail.Callback{
	private String userId;
	private boolean myPage;
	private Context context;
	private ListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private UserArtworkAdapter mAdapter;
	private String currentType = null;
	
	public static ArtworkListFragment newInstance(String userId,boolean myPage){
		ArtworkListFragment fragment = new ArtworkListFragment();
		
		Bundle args = new Bundle();
		args.putString("userId", userId);
		args.putBoolean("myPage", myPage);
		fragment.setArguments(args);
		
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		this.context = activity;
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if(args != null){
			this.myPage = args.getBoolean("myPage");
			this.userId = args.getString("userId");
		}else{
			this.myPage = false;
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_user_artwork_list, container,false);
		
		this.mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.artworkList);
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//				String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
//						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
//
//				// Update the LastUpdatedLabel
//				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				refreshArtworks(currentType,null,true);
			}
			
		});
		
		
		this.listView = mPullRefreshListView.getRefreshableView();
		
		this.mAdapter = new UserArtworkAdapter(getActivity(), this, myPage);
		this.mPullRefreshListView.setAdapter(mAdapter);
		
		refreshArtworks(currentType,null,false);
		
		return view;
	}

	public void refreshArtworks(String type,String sortOption,final boolean pullToRefresh){
		this.currentType = type;
		ArtworkListTask task = new ArtworkListTask(ArtworkListTask.USER_ARTWORKS, new ArtworkListTask.ArtworkListListener() {
			
			@Override
			public void onComplete(ArtworkPageWrapper pageWrapper) {
				if(pullToRefresh)
					mPullRefreshListView.onRefreshComplete();
				
				mAdapter.setArtworkList(pageWrapper.artworks);
				mAdapter.notifyDataSetChanged();
				
			}
		});
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId,type,sortOption);
		}else{
			task.execute(userId,type,sortOption);				
		}
		
	}

	@Override
	public void onShowArtworkDetail(ArtworkEntity artwork) {
		//artwork 자세히보기페이지로 간다.
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
		
		Intent intent = new Intent(getActivity(),ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", myPage);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", index);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
		
	}

	//해당 artwork을 collection에 추가한다.
	@Override
	public void onAddToCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		WaitDialog.showWailtDialog(context, false);
		AddCollectionTask task = new AddCollectionTask();
		task.execute(entity.artworkId);
	}

	@Override
	public void onRemoveFromCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		WaitDialog.showWailtDialog(context, false);
		RemoveCollectionTask task = new RemoveCollectionTask();
		task.execute(entity.artworkId);
	}

	@Override
	public void onNextPage(int crntPage) {
		
		
	}

	@Override
	public void onPrevPage(int crntPage) {
		
		
	}
	

	@Override
	public void onAddClick() {
		// TODO Auto-generated method stub
		
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
			
			refreshArtworks(null,null,false);
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
			
			refreshArtworks(null,null,false);
		}
	}
}
