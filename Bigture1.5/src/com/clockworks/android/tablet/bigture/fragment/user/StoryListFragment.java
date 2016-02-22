package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.StoryBookEditActivity;
import com.clockworks.android.tablet.bigture.StoryBookViewActivity;
import com.clockworks.android.tablet.bigture.adapter.artwork.UserArtworkAdapter;
import com.clockworks.android.tablet.bigture.adapter.story.UserStoryAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.StoryListTask;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;

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

public class StoryListFragment extends Fragment implements BookThumbnail.Callback{
	private String userId;
	private boolean myPage;
	private boolean isPro;
	private Context context;
	private ListView listView;
	private PullToRefreshListView mPullRefreshListView;
	private UserStoryAdapter mAdpater;
	
	public static StoryListFragment newInstance(String userId,boolean myPage,boolean isPro){
		StoryListFragment fragment = new StoryListFragment();
		
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
		
		View view = inflater.inflate(R.layout.fragment_user_story_list, container,false);
		
		this.mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.storyList);
		
		this.listView = mPullRefreshListView.getRefreshableView();
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshStory(null,null,true);
			}
			
		});
		
		this.mAdpater = new UserStoryAdapter(getActivity(), this, myPage, isPro);
		this.mPullRefreshListView.setAdapter(mAdpater);
		
		refreshStory(null,null,false);
		
		return view;
	}

	public void refreshStory(String status,String sortOption,final boolean pullToRefresh){
		StoryListTask task = new StoryListTask(StoryListTask.USER_STORY, new StoryListTask.StoryListListener() {
			
			@Override
			public void onComplete(ArrayList<StoryEntity> stories) {
				if(pullToRefresh)
					mPullRefreshListView.onRefreshComplete();
				
				mAdpater.setStoryItem(stories);
				mAdpater.notifyDataSetChanged();
			}
		},myPage,isPro);
		
		task.execute(userId,status,sortOption);
		
	}

	@Override
	public void onShowStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		
		if(storyEntity.status.equals("DRAFT")){
			Intent intent = new Intent(context,StoryBookEditActivity.class);
			intent.putExtra("storyEntity", storyEntity);
			intent.putExtra("myBigture", true);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_STORY);
		}else{
			Intent intent = new Intent(context,StoryBookViewActivity.class);
			intent.putExtra("storyEntity", storyEntity);
			intent.putExtra("myBigture", true);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SHOW_STORYBOOK);
		}
		
		
	}

	@Override
	public void onToggleFavorite(BookThumbnail thumb, StoryEntity storyEntity) {
		if(storyEntity.collected){
			RemoveCollectionTask task = new RemoveCollectionTask();
			task.execute(storyEntity.storyId);
			storyEntity.collected = false;
		}else{
			AddCollectionTask task = new AddCollectionTask();
			task.execute(storyEntity.storyId);
			storyEntity.collected = true;
		}
		thumb.updateCollection(storyEntity.collected);
		
	}

	@Override
	public void onAddStoryBook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEditStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleteStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		// TODO Auto-generated method stub
		
	}

	class AddCollectionTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return StoryHandler.addCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
	class RemoveCollectionTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return StoryHandler.deleteCollection(params[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
}
