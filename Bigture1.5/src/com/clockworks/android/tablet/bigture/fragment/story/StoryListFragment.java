package com.clockworks.android.tablet.bigture.fragment.story;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.StoryBookEditActivity;
import com.clockworks.android.tablet.bigture.StoryBookViewActivity;
import com.clockworks.android.tablet.bigture.StoryListActivity;
import com.clockworks.android.tablet.bigture.adapter.story.StoryAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class StoryListFragment extends Fragment implements BookThumbnail.Callback{
	private Context context;
	private PullToRefreshListView listView;
	
	private int currentPage = 1;
	
	private StoryAdapter adapter;
	private boolean isPro;
	private String userId;

	private int currentTab;
	
	public static StoryListFragment newInstance(int currentTab){
		StoryListFragment fragment = new StoryListFragment();
		
		Bundle args = new Bundle();
		args.putInt("currentTab", currentTab);
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
		
		
		this.isPro = AccountManager.isExpert();
		this.userId = AccountManager.getUserIdx();
		
		currentPage = 1;
		
		Bundle args = getArguments();
		if(args != null){
			this.currentTab = args.getInt("currentTab");
			
		}else{
			this.currentTab = this.isPro ? StoryListActivity.TAB_OWNED : StoryListActivity.TAB_JOINED;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_story_list, container,false);
		
		this.listView = (PullToRefreshListView)view.findViewById(R.id.listView1);
		this.listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshList();
			}
			
		});
		
		this.adapter = new StoryAdapter(this.context,this,this.isPro,this.currentTab == StoryListActivity.TAB_OWNED);
		this.listView.setAdapter(this.adapter);
		
		refreshList();
		
		return view;
	}
	
	public void refreshList(int currentTab,int currentPage){
		this.currentPage = currentPage;
		this.currentTab = currentTab;
		
		refreshList();
	}
	
	private void refreshList(){
		StoryListTask task = new StoryListTask();
		task.execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			
			
		}
	}

	@Override
	public void onShowStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		if(storyEntity.status.equals("DRAFT")){
			Intent intent = new Intent(context,StoryBookEditActivity.class);
			intent.putExtra("storyEntity", storyEntity);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_STORY);
		}else{
			Intent intent = new Intent(context,StoryBookViewActivity.class);
			intent.putExtra("storyEntity", storyEntity);
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
		Intent intent = new Intent(context,StoryBookEditActivity.class);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_STORY);
	}

	@Override
	public void onEditStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		Intent intent = new Intent(context,StoryBookEditActivity.class);
		intent.putExtra("storyEntity", storyEntity);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_ADD_STORY);
		
	}

	@Override
	public void onDeleteStoryBook(BookThumbnail thumb, final StoryEntity storyEntity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Confirm	");
		builder.setMessage(R.string.Delete_story_confirm);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				WaitDialog.showWailtDialog(context, false);
				DeleteStoryTask task = new DeleteStoryTask();
				task.execute(storyEntity.storyId);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create().show();
		
		
		
	}
	
	
	class StoryListTask extends AsyncTask<Void, Void, StoryPageWrapper>{

		@Override
		protected StoryPageWrapper doInBackground(Void... params) {
			switch (currentTab) {
			case StoryListActivity.TAB_OWNED:
				return StoryHandler.findOwnStory(null, null, currentPage);
			case StoryListActivity.TAB_JOINED:
				return StoryHandler.findJoinedStory(null, null, currentPage);
			case StoryListActivity.TAB_COLLECTION:
				return StoryHandler.findCollectedStory(null, null, currentPage);
			default:
				return null;
			}
			
		}

		@Override
		protected void onPostExecute(StoryPageWrapper result) {
			super.onPostExecute(result);
			
			listView.onRefreshComplete();
			adapter.setStoryItem(result.stories,currentTab==StoryListActivity.TAB_OWNED);
			adapter.notifyDataSetChanged();
		}	
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
	
	class DeleteStoryTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return StoryHandler.deleteStory(params[0]);
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
			refreshList();
		}
		
		
		
	}
	
	
}
