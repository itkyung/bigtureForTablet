package com.clockworks.android.tablet.bigture.fragment.story;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.StoryBookViewActivity;
import com.clockworks.android.tablet.bigture.adapter.story.StoryAllAdapter;
import com.clockworks.android.tablet.bigture.adapter.story.StoryShortAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.fragment.story.StoryListFragment.AddCollectionTask;
import com.clockworks.android.tablet.bigture.fragment.story.StoryListFragment.DeleteStoryTask;
import com.clockworks.android.tablet.bigture.fragment.story.StoryListFragment.RemoveCollectionTask;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class StoryAllListFragment extends Fragment implements View.OnClickListener,BookThumbnail.Callback{
	private String currentSortOption;
	private int currentPage;
	private String userId;
	
	private View storyListFrame;
	private View expertListFrameWrapper;
	
	private PullToRefreshListView storyAllList;
	private PullToRefreshListView storyAllListByExpert;
	private LinearLayout sideIndex;
	private StoryAllAdapter storyAdapter;
	private StoryShortAdapter shortAdapter;
	private Context context;
	private String selectedExpertsId;
	
	public static StoryAllListFragment newInstance(String currentSortOption){
		StoryAllListFragment fragment = new StoryAllListFragment();
		Bundle args = new Bundle();
		args.putString("currentSortOption", currentSortOption);
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
		this.userId = AccountManager.getUserIdx();
		this.currentPage = 1;
		
		Bundle args = getArguments();
		if(args != null){
			this.currentSortOption = args.getString("currentSortOption");
		}else{
			this.currentSortOption = "RECENT";
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_story_all_list, container,false);
		
		this.expertListFrameWrapper = view.findViewById(R.id.expertListFrameWrapper);
		this.storyAllList = (PullToRefreshListView)view.findViewById(R.id.storyAllList);
		this.sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		this.storyListFrame = view.findViewById(R.id.storyListFrame);
		
		this.selectedExpertsId = null;
		
		this.storyAllList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshList(1,currentSortOption);
			}
			
		});
		
		this.storyAdapter = new StoryAllAdapter(context, this, false);
		this.storyAllList.setAdapter(storyAdapter);
		
		this.storyAllListByExpert = (PullToRefreshListView)view.findViewById(R.id.storyAllListByExpert); 
		this.storyAllListByExpert.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshListByExpert(selectedExpertsId);
			}	
		});
		this.shortAdapter = new StoryShortAdapter(context,this);
		this.storyAllListByExpert.setAdapter(shortAdapter);
		
		this.sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		String[] sections = (String[])this.storyAdapter.getSections();
		
		TextView tmpTV = null;
		for (String section : sections) {
			tmpTV = new TextView(context);
			tmpTV.setText(section);
			tmpTV.setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
			tmpTV.setOnClickListener(this);
		}
		
		refreshList(1, currentSortOption);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			String text = tv.getText().toString();
			int position = this.storyAdapter.getPositionForSection(text);
			if(position != -1){
				this.storyAllList.getRefreshableView().setSelection(position);
			}
		}
		
	}

	public void refreshList(int currentPage,String currentSortOption){
		this.currentPage = currentPage;
		this.currentSortOption = currentSortOption;
		
		if(currentSortOption.equals("RECENT")){
			hideExpertsFrame();
			hideSideIndex();
			
			StoryAllListTask task = new StoryAllListTask();
			task.execute();
			
		}else if(currentSortOption.equals("STORYNAME")){
			hideExpertsFrame();
			showSideIndex();
			
			StoryAllListTask task = new StoryAllListTask();
			task.execute();
			
		}else{
			showExpertsFrame();
			hideSideIndex();
			
			refreshListByExpert(null);
		}
		
	}
	
	public void refreshListByExpert(String selectedExpertsId){
		this.selectedExpertsId = selectedExpertsId;
		StoryByExpertsTask task = new StoryByExpertsTask();
		task.execute(selectedExpertsId);
	}
	
	private void hideExpertsFrame(){
		this.expertListFrameWrapper.setVisibility(View.GONE);
		this.storyListFrame.setVisibility(View.VISIBLE);
	}
	
	private void showExpertsFrame(){
		this.expertListFrameWrapper.setVisibility(View.VISIBLE);
		this.storyListFrame.setVisibility(View.GONE);
	}
	
	private void hideSideIndex(){
		this.sideIndex.setVisibility(View.GONE);
	}
	
	private void showSideIndex(){
		this.sideIndex.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			refreshList(1, currentSortOption);
		}
	}

	@Override
	public void onShowStoryBook(BookThumbnail thumb, StoryEntity storyEntity) {
		
		
		Intent intent = new Intent(context,StoryBookViewActivity.class);
		intent.putExtra("storyEntity", storyEntity);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SHOW_STORYBOOK);
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
	
	class StoryAllListTask extends AsyncTask<Void, Void, StoryPageWrapper>{

		@Override
		protected StoryPageWrapper doInBackground(Void... params) {
			return StoryHandler.findAllStory(currentSortOption, currentPage);
		}

		@Override
		protected void onPostExecute(StoryPageWrapper result) {
			
			super.onPostExecute(result);
			storyAllList.onRefreshComplete();
			storyAdapter.setStoryItem(result.stories, currentSortOption.equals("RECENT") ? false : true);
			storyAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			storyAllList.onRefreshComplete();
		}
		
	}
	
	class StoryByExpertsTask extends AsyncTask<String, Void, StoryPageWrapper>{

		@Override
		protected StoryPageWrapper doInBackground(String... params) {
			String expertId = params[0];
			if(expertId == null)
				return StoryHandler.findAllStory("RECENT",currentPage);
			else
			return StoryHandler.findStoryByExperts(params[0], currentSortOption, currentPage);
		}

		@Override
		protected void onPostExecute(StoryPageWrapper result) {
			
			super.onPostExecute(result);
			storyAllListByExpert.onRefreshComplete();
			
			shortAdapter.setStoryItem(result.stories);
			shortAdapter.notifyDataSetChanged();
			
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
			refreshList(1,currentSortOption);
		}
		
		
		
	}
	
}
