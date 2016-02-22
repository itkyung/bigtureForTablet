package com.clockworks.android.tablet.bigture.fragment.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtworkDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SketchbookActivity;
import com.clockworks.android.tablet.bigture.adapter.artclass.ArtworkAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.common.ClassType;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;

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
import android.widget.PopupWindow;

public class ArtworksListFragment extends Fragment implements ArtworkThumbnail.Callback{
	private int currentPage;
	private String currentSortOption;
	private boolean isOwner;
	private Context context;
	private String classId;
	private View tabAction;
	private PullToRefreshListView listView;
	private ArtworkAdapter mAdapter;
	private View sortMenu;
	private boolean joined;
	private ClassType classType;
	private ClassShare shareType;
	
	private PopupWindow sortMenuPoptup;
	
	
	public static ArtworksListFragment newInstance(String classId,boolean isOwner,boolean joined,ClassType classType, ClassShare shareType){
		ArtworksListFragment fragment = new ArtworksListFragment();
		Bundle args = new Bundle();
		args.putBoolean("isOwner", isOwner);
		args.putString("classId", classId);
		args.putBoolean("joined", joined);
		args.putString("classType", classType.name());
		args.putString("shareType", shareType.name());
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
		currentSortOption = "RECENT";
		
		Bundle args = getArguments();
		this.isOwner = args.getBoolean("isOwner");
		this.classId = args.getString("classId");
		this.joined = args.getBoolean("joined");
		this.classType = ClassType.valueOf(args.getString("classType"));
		this.shareType = ClassShare.valueOf(args.getString("shareType"));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_artclass_artworks, container,false);
		
		this.listView = (PullToRefreshListView)view.findViewById(R.id.listArtworks);
		this.listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshList();
			}
			
		});
		
		this.mAdapter = new ArtworkAdapter(context, this);
		this.listView.setAdapter(mAdapter);
		
		this.sortMenu = view.findViewById(R.id.tabAction);
		this.sortMenu.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		
		refreshList();
		
		return view;
	}
	
	private void refreshList(){
		ArtworkListTask task = new ArtworkListTask();
		task.execute();
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
		
		Intent intent = new Intent(context,ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", index);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
		
	}


	@Override
	public void onAddToCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		AddCollectionTask task = new AddCollectionTask();
		task.execute(entity.artworkId);
	}


	@Override
	public void onRemoveFromCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		RemoveCollectionTask task = new RemoveCollectionTask();
		task.execute(entity.artworkId);
	}


	@Override
	public void onNextPage(int crntPage) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onPrevPage(int crntPage) {
		// TODO Auto-generated method stub
		
	}

	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			refreshList();
		}
	}


	@Override
	public void onAddClick() {
		//normal이면 스케치북을 띄우고 puzzle이면 puzzle화면을 띄운다. 
		boolean canWrite = false;
		if(shareType.equals(ClassShare.OPEN) || (shareType.equals(ClassShare.EXCLUSIVE) && joined)){
			canWrite = true;
		}
		if(isOwner || canWrite){
			if(classType.equals(ClassType.NORMAL)){
				Intent intent = new Intent(context, SketchbookActivity.class);
				DrawingPurpose purpose = new DrawingPurpose();
				purpose.reason = DrawingPurpose.ARTCLASS;
				purpose.classId = classId;
				
				intent.putExtra("drawingPurpose", purpose);
				this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
				
				
			}else{
			
			}
		}
	}


	class ArtworkListTask extends AsyncTask<Void, Void, ArtworkPageWrapper>{

		@Override
		protected void onCancelled() {
			super.onCancelled();
			listView.onRefreshComplete();
		}

		@Override
		protected ArtworkPageWrapper doInBackground(Void... params) {
			
			return ArtClassHandler.listClassArtworks(classId, currentPage, currentSortOption);
		}

		@Override
		protected void onPostExecute(ArtworkPageWrapper result) {
			super.onPostExecute(result);
			listView.onRefreshComplete();
			
			boolean canWrite = isOwner;
			if(!isOwner){
				//Class owner가 아니면 open된 class에만 그림을 그릴수 있다.
				canWrite = shareType.equals(ClassShare.OPEN) ? true : joined;
			}
			
			mAdapter.setArtworkList(result.artworks, result.totalPage, currentPage, canWrite);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	class AddCollectionTask extends AsyncTask<String, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}
		
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
			
			refreshList();
		}
	}

	class RemoveCollectionTask extends AsyncTask<String, Void, Boolean>{
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}

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
			
			refreshList();
		}
	}
	
}
