package com.clockworks.android.tablet.bigture.fragment.artclass;

import com.clockworks.android.tablet.bigture.ArtClassDetailActivity;
import com.clockworks.android.tablet.bigture.ArtClassListActivity;
import com.clockworks.android.tablet.bigture.PopupCreateClassActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.artclass.ArtClassAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.views.artclass.ClassThumbnail;
import com.google.android.gms.internal.en;
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
	private Context context;
	private PullToRefreshListView listView;
	
	private int currentPage = 1;
	
	
	private boolean isPro;
	private String userId;
	private ArtClassAdapter classAdpater;
	private int currentTab;
	private String currentStatus = "ALL";
	
	public static ArtClassListFragment newInstance(int currentTab,String currentStatus){
		ArtClassListFragment fragment = new ArtClassListFragment();
		
		Bundle args = new Bundle();
		args.putString("currentStatus", currentStatus);
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
			this.currentStatus = args.getString("currentStatus");
			
		}else{
			this.currentTab = this.isPro ? ArtClassListActivity.TAB_OWNED : ArtClassListActivity.TAB_JOINED;
			this.currentStatus = "ALL";
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_artclass_list, container,false);
		
		this.listView = (PullToRefreshListView)view.findViewById(R.id.listView1);
		this.listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshList();
			}
			
		});
		
		this.classAdpater = new ArtClassAdapter(this.context,this,this.isPro);
		this.listView.setAdapter(this.classAdpater);
		
		refreshList();
		
		return view;
	}

	
	public void refreshList(int currentTab,int currentPage,String currentStatus){
		this.currentPage = currentPage;
		this.currentStatus = currentStatus;
		this.currentTab = currentTab;
		
		refreshList();
	}
	
	private void refreshList(){
		ArtClassListTask task = new ArtClassListTask();
		task.execute();
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
		
		Intent intent = new Intent(context,PopupCreateClassActivity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			refreshList();
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
	
	
	
	class ArtClassListTask extends AsyncTask<Void, Void, ArtClassPageWrapper>{

		@Override
		protected ArtClassPageWrapper doInBackground(Void... params) {
			ArtClassPageWrapper wrapper = null;
			switch (currentTab) {
			case ArtClassListActivity.TAB_OWNED:
				wrapper = ArtClassHandler.findOwnClass(userId, currentPage, currentStatus);
				break;
			case ArtClassListActivity.TAB_JOINED:
				wrapper = ArtClassHandler.findJoinedClass(userId, currentPage, currentStatus);
				break;
			case ArtClassListActivity.TAB_COLLECTION:
				wrapper = ArtClassHandler.findCollectedClass(userId, currentPage, currentStatus);
				break;
			default:
				//TAB_ALL은 여기에서 처리하지 않는다. 
				break;
			}
			
			return wrapper;
		}

		@Override
		protected void onPostExecute(ArtClassPageWrapper result) {
		
			super.onPostExecute(result);
			listView.onRefreshComplete();
			
			classAdpater.setClassItem(result.artClasses, currentTab == ArtClassListActivity.TAB_OWNED ? true : false);
			classAdpater.notifyDataSetChanged();
			
		}
		
	}
}
