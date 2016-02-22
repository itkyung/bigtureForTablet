package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtClassDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artclass.UserArtClassAdapter;
import com.clockworks.android.tablet.bigture.adapter.artwork.UserArtworkAdapter;
import com.clockworks.android.tablet.bigture.adapter.story.UserStoryAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ArtClassListFragment extends Fragment implements ClassThumbnail.Callback{
	private String userId;
	private boolean myPage;
	private Context context;
	private ListView listView;
	private boolean isPro;
	private int currentPage;
	private PullToRefreshListView mPullRefreshListView;
	private UserArtClassAdapter mAdapter;
	
	
	public static ArtClassListFragment newInstance(String userId,boolean myPage,boolean isPro){
		ArtClassListFragment fragment = new ArtClassListFragment();
		
		Bundle args = new Bundle();
		args.putString("userId", userId);
		args.putBoolean("myPage", myPage);
		args.putBoolean("isPro",isPro);
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
		
		currentPage = 1;
		Bundle args = getArguments();
		if(args != null){
			this.myPage = args.getBoolean("myPage");
			this.userId = args.getString("userId");
			this.isPro = args.getBoolean("isPro");
		}else{
			this.myPage = false;
			this.isPro = false;
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_user_artclass_list, container,false);
		
		this.mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.artClassList);
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshArtClass();
			}
		});
		
		this.listView = mPullRefreshListView.getRefreshableView();
		
		this.mAdapter = new UserArtClassAdapter(getActivity(), this, myPage, isPro);
		this.mPullRefreshListView.setAdapter(mAdapter);
		
		refreshArtClass();
		
		return view;
		
	}
	
	public void refreshArtClass(){
		ArtClassListTask task = new ArtClassListTask();
		task.execute(userId);
	}
	
	
	@Override
	public void onShowArtClass(ClassThumbnail thumb, ArtClassEntity entity) {
		Intent intent = new Intent(context,ArtClassDetailActivity.class);
		intent.putExtra("artClass", entity);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SHOW_ARTCLASS);
		
	}

	@Override
	public void onToggleFavorite(ClassThumbnail thumb, ArtClassEntity entity) {
		if(entity.collected){
			ArtClassUnCollectTask task = new ArtClassUnCollectTask();
			task.execute(entity.index);
			entity.collected = false;
		}else{
			ArtClassCollectTask task = new ArtClassCollectTask();
			task.execute(entity.index);
			entity.collected = true;
		}
		thumb.updateCollection(entity.collected);
		
	}

	@Override
	public void onAddArtClass() {
		// TODO Auto-generated method stub
		
	}
	
	class ArtClassListTask extends AsyncTask<String, Void, ArrayList<ArtClassEntity>>{

		@Override
		protected ArrayList<ArtClassEntity> doInBackground(String... params) {
			String userId = params[0];
			
			return ArtClassHandler.listUserArtclass(userId,currentPage);
		}

		@Override
		protected void onPostExecute(ArrayList<ArtClassEntity> result) {
			super.onPostExecute(result);
			
//			if(isPro && myPage){
//				ArrayList<ArtClassEntity> classes = new ArrayList<ArtClassEntity>();
//				ArtClassEntity dummy = new ArtClassEntity();
//				classes.add(dummy);
//				classes.addAll(result);
//				mAdapter.setClassItem(classes);
//			}else{
				mAdapter.setClassItem(result);
			//}
			mAdapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}
		
	}
	
	class ArtClassCollectTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtClassHandler.addClassCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
		}
	}
	
	class ArtClassUnCollectTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtClassHandler.removeClassCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
		
			super.onPostExecute(result);
		}
		
		
	}
}
