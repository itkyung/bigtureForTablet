package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.PopupArtworkListActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SendPostCardPopupActivity;
import com.clockworks.android.tablet.bigture.SketchbookActivity;

import com.clockworks.android.tablet.bigture.ViewPostCardPopupActivity;
import com.clockworks.android.tablet.bigture.adapter.my.PostCardAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.PostCardHandler;
import com.clockworks.android.tablet.bigture.views.postcard.CardThumbnail;
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


public class PostCardListFragment extends Fragment implements CardThumbnail.Callback {
	private String userId;
	private boolean myPage;
	private Context context;
	private ListView listView;
	private boolean receivedPage;
	private PostCardAdapter adapter;
	private int currentPage;
	private PostCardListListener listener;
	private PullToRefreshListView mPullRefreshListView;
	
	public static PostCardListFragment newInstance(String userId,boolean myPage){
		PostCardListFragment fragment = new PostCardListFragment();
		
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
		this.listener = (PostCardListListener)activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentPage = 1;
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
		
		View view = inflater.inflate(R.layout.fragment_postcard_list, container,false);
		
		this.mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.postCardList);
		this.listView = mPullRefreshListView.getRefreshableView();
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				if(myPage){
					if(receivedPage)
						getReceivedData();
					else
						getSendedData();
				}else
					getAllData();
				
			}
		});
		
		this.adapter = new PostCardAdapter(context, myPage,this,receivedPage);
		this.mPullRefreshListView.setAdapter(adapter);
		
		if(myPage)
			getReceivedData();
		else
			getAllData();
		
		return view;
	}

	public void getSendedData(){
		receivedPage = false;
		this.adapter.setReceivedCell(false);
		currentPage = 1;
		
		PostCardListTask task = new PostCardListTask();
		task.execute("sended");
	}
	
	public void getReceivedData(){
		receivedPage = true;
		this.adapter.setReceivedCell(true);
		currentPage = 1;
		PostCardListTask task = new PostCardListTask();
		task.execute("received");
	}

	public void getAllData(){
		this.adapter.setReceivedCell(false);
		receivedPage = false;
		currentPage = 1;
		PostCardListTask task = new PostCardListTask();
		task.execute("all",userId);
	}
	
	class PostCardListTask extends AsyncTask<String, Void, ArrayList<PostCardEntity>>{

		@Override
		protected ArrayList<PostCardEntity> doInBackground(String... params) {
			String searchType = params[0];
			
			if(searchType.equals("all")){
				String userId = params[1];
				return PostCardHandler.listAllPostCards(userId, currentPage);
			}else if(searchType.equals("sended")){
				return PostCardHandler.listSendedPostCards(currentPage);
			}else{
				return PostCardHandler.listReceivedPostCards(currentPage);
			}
			
		}

		@Override
		protected void onPostExecute(ArrayList<PostCardEntity> result) {
			super.onPostExecute(result);
			if(currentPage == 1){
				adapter.setCardItem(result);
			}else{
				
			}
			adapter.notifyDataSetChanged();
			mPullRefreshListView.onRefreshComplete();
		}
		
	}
	
	
	@Override
	public void onShowPostCard(CardThumbnail thumb, PostCardEntity cardEntity) {
		Intent intent = new Intent(context,ViewPostCardPopupActivity.class);
		intent.putExtra("cardId", cardEntity.id);
		intent.putExtra("sendFlag", !receivedPage);
		intent.putExtra("myPage",myPage);
		
		startActivityForResult(intent,BigtureEnvironment.EVENT_CODE_VIEW_CARD);
		
	}


	
	@Override
	public void onSelectArtwork() {
		Intent intent = new Intent(context,PopupArtworkListActivity.class);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
	}


	@Override
	public void onDrawSketchbook() {
		Intent intent = new Intent(context, SketchbookActivity.class);
		DrawingPurpose purpose = new DrawingPurpose();
		purpose.reason = DrawingPurpose.POSTCARD;
		
		intent.putExtra("drawingPurpose", purpose);
		this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
		
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Activity.RESULT_OK){
		
			if(requestCode == BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK){
				Intent intent = new Intent(context,SendPostCardPopupActivity.class);
				//Artwork 를 넘겨야한다.
				ArtworkEntity entity = data.getParcelableExtra("selectedArtwork");
				intent.putExtra("selectedArtwork", entity);
				startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SEND_CARD);
			}else if(requestCode == BigtureEnvironment.EVENT_CODE_SEND_CARD){
				this.listener.changeCardMenuText("Sended",1);
				getSendedData();
			}else if(requestCode == BigtureEnvironment.REQ_CODE_SKETCHBOOK){
				String artworkId = data.getStringExtra("artworkId");
				new GetArtworkInfoTask().execute(artworkId);
				
			}else if(requestCode == BigtureEnvironment.EVENT_CODE_VIEW_CARD){
				if(data != null){
					boolean replyFlag = data.getBooleanExtra("reply", false);
					if(replyFlag){
						PostCardEntity entity = (PostCardEntity)data.getParcelableExtra("cardEntity");
						ArtworkEntity artwork = (ArtworkEntity)data.getParcelableExtra("selectedArtwork");
						Intent intent = new Intent(context,SendPostCardPopupActivity.class);
						intent.putExtra("reply", true);
						intent.putExtra("originCard", entity);
						intent.putExtra("selectedArtwork", artwork);
						startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SEND_CARD);
					}else{
						boolean reloadFlag = data.getBooleanExtra("reload", false);
						if(reloadFlag){
							if(receivedPage){
								getSendedData();
							}else{
								getReceivedData();
							}
						}
					}
				}
			}
		
		}
	}
	
	class GetArtworkInfoTask extends AsyncTask<String,Void, ArtworkEntity>{

		@Override
		protected ArtworkEntity doInBackground(String... params) {
			return ArtworkHandler.loadArtwork(params[0]);
		}

		@Override
		protected void onPostExecute(ArtworkEntity result) {
			super.onPostExecute(result);
			Intent intent = new Intent(context,SendPostCardPopupActivity.class);

			intent.putExtra("selectedArtwork", result);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SEND_CARD);
		}
		
		
	}

	public interface PostCardListListener{
		public void changeCardMenuText(String txt,int idx);
		
	}
	
}


