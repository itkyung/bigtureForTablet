package com.clockworks.android.tablet.bigture.fragment.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtClassDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artclass.ArtClassAllAdapter;
import com.clockworks.android.tablet.bigture.adapter.artclass.ArtClassShortAdapter;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertCellListAdapter;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertCellListAdapter.ExpertClickListener;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassListFragment.ArtClassCollectTask;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassListFragment.ArtClassUnCollectTask;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ExpertWordWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ExpertHandler;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ArtClassAllListFragment extends Fragment implements ClassThumbnail.Callback,View.OnClickListener,ExpertClickListener{
	private String currentSortOption;
	private int currentPage;
	private String userId;
	private View expertListFrame;
	private View artClassListFrame;
	private EditText editKeyword;
	private ListView expertListView;
	private PullToRefreshListView artClassAllList;
	private PullToRefreshListView artClassAllListByExpert;
	private LinearLayout sideIndex;
	private ArtClassAllAdapter classAdapter;
	private ExpertCellListAdapter expertAdapter;
	private ArtClassShortAdapter classShortAdapter;
	private Context context;
	private String selectedExpertId;
	private ExpertListTask findTask;
	
	
	public static ArtClassAllListFragment newInstance(String currentSortOption){
		ArtClassAllListFragment fragment = new ArtClassAllListFragment();
		
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
		this.selectedExpertId = null;
		
		Bundle args = getArguments();
		if(args != null){
			this.currentSortOption = args.getString("currentSortOption");
		}else{
			this.currentSortOption = "RECENT";
		}
		findTask = null;
		
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_artclass_all_list, container,false);
		
		this.expertListFrame = view.findViewById(R.id.expertListFrame);
		this.editKeyword = (EditText)view.findViewById(R.id.editKeyword);
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
		
		
		this.expertListView = (ListView)view.findViewById(R.id.expertListView);
		this.artClassAllList = (PullToRefreshListView)view.findViewById(R.id.artClassAllList);
		this.sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		this.artClassListFrame = view.findViewById(R.id.artClassListFrame);
		
		
		this.artClassAllList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshList(1,currentSortOption);
			}
			
		});
		
		this.classAdapter = new ArtClassAllAdapter(context, this, false);
		this.artClassAllList.setAdapter(classAdapter);
		
		this.artClassAllListByExpert = (PullToRefreshListView)view.findViewById(R.id.artClassAllListByExpert); 
		this.artClassAllListByExpert.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshListByExpert();
			}	
		});
		this.classShortAdapter = new ArtClassShortAdapter(context,this);
		this.artClassAllListByExpert.setAdapter(classShortAdapter);
		
		
		this.expertAdapter = new ExpertCellListAdapter(context,this);
		this.expertListView.setAdapter(expertAdapter);
		
		
		
		this.sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		String[] sections = (String[])this.classAdapter.getSections();
		
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



	public void refreshList(int currentPage,String currentSortOption){
		this.currentPage = currentPage;
		this.currentSortOption = currentSortOption;
		
		if(currentSortOption.equals("RECENT")){
			hideExpertsFrame();
			hideSideIndex();
			
			ArtClassAllListTask task = new ArtClassAllListTask();
			task.execute();
			
		}else if(currentSortOption.equals("CLASSNAME")){
			hideExpertsFrame();
			showSideIndex();
			
			ArtClassAllListTask task = new ArtClassAllListTask();
			task.execute();
			
		}else{
			showExpertsFrame();
			hideSideIndex();
			
			searchUser(null);
			
			refreshListByExpert();
		}
		
	}
	
	private void searchUser(String keyword){

		if (findTask != null)
			findTask.cancelTask();
		
		findTask = new ExpertListTask();
		findTask.execute(keyword);
		
	}
	
	private void hideExpertsFrame(){
		this.expertListFrame.setVisibility(View.GONE);
		this.artClassListFrame.setVisibility(View.VISIBLE);
	}
	
	private void showExpertsFrame(){
		this.expertListFrame.setVisibility(View.VISIBLE);
		this.artClassListFrame.setVisibility(View.GONE);
	}
	
	private void hideSideIndex(){
		this.sideIndex.setVisibility(View.GONE);
	}
	
	private void showSideIndex(){
		this.sideIndex.setVisibility(View.VISIBLE);
	}
	
	public void refreshListByExpert(){
		ArtClassByExpertsTask task = new ArtClassByExpertsTask();
		task.execute(this.selectedExpertId);
	}
	
	@Override
	public void onSelectExpert(SimpleUserEntity entity) {
		this.selectedExpertId = entity.index;
		currentPage = 1;
		refreshListByExpert();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			refreshList(1, currentSortOption);
		}
	}


	@Override
	public void onClick(View v) {
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			String text = tv.getText().toString();
			int position = this.classAdapter.getPositionForSection(text);
			if(position != -1){
				this.artClassAllList.getRefreshableView().setSelection(position);
			}
		}
		
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
	
	class ArtClassAllListTask extends AsyncTask<Void, Void, ArtClassPageWrapper>{

		@Override
		protected ArtClassPageWrapper doInBackground(Void... params) {
			return ArtClassHandler.findAllClass(userId, currentPage, currentSortOption);
		}

		@Override
		protected void onPostExecute(ArtClassPageWrapper result) {
			super.onPostExecute(result);
			
			artClassAllList.onRefreshComplete();
			
			classAdapter.setClassItem(result.artClasses, currentSortOption.equals("RECENT") ? false : true);
			classAdapter.notifyDataSetChanged();
		}
		
		
	}
	
	class ArtClassByExpertsTask extends AsyncTask<String, Void, ArtClassPageWrapper>{

		@Override
		protected ArtClassPageWrapper doInBackground(String... params) {
			String expertId = params[0];
			if(expertId == null)
				return ArtClassHandler.findAllClass(userId, currentPage, "RECENT");
			else
			return ArtClassHandler.findClassByExperts(params[0], currentPage, currentSortOption);
		}

		@Override
		protected void onPostExecute(ArtClassPageWrapper result) {
			
			super.onPostExecute(result);
			artClassAllListByExpert.onRefreshComplete();
			
			classShortAdapter.setClassItem(result.artClasses);
			classShortAdapter.notifyDataSetChanged();
			
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
