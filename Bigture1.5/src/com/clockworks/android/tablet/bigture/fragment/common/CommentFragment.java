package com.clockworks.android.tablet.bigture.fragment.common;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.CommentAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentWrapperEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentFragment extends Fragment implements View.OnClickListener{
	private Context context;
	private ArtworkEntity artwork;
	private CommentWrapperEntity commentWrapper;
	private boolean alreadySendSticker = false;
	
	private PullToRefreshListView mPullRefreshCommentsListView;
	private EditText editText;
	private int page;
	
	private TextView btnSend;
	private CommentAdapter mAdapter;
	private ObjectType type;
	private boolean hide;
	private String currentUserIdx = null;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
		hide = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = 1;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		View view = inflater.inflate(R.layout.fragment_comment_list, container, false);
		currentUserIdx = AccountManager.getUserIdx();
		
		this.mPullRefreshCommentsListView = (PullToRefreshListView)view.findViewById(R.id.commentList);
		
		this.mPullRefreshCommentsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				readComments(artwork);
			}
	       
		});
		
		ListView listView = mPullRefreshCommentsListView.getRefreshableView();
		listView.setBackgroundResource(R.drawable.artworkspage_talk_list_bg);
		
		
		listView.setOnItemLongClickListener(new AbsListView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!AccountManager.isLogin()){
					BigtureEnvironment.showAlertMessage(context, 20);
					return true;
				}
				if(position > 0)
					position = position - 1; //pulltorefresh라서 position이 조금 다르다.
				
				final CommentEntity entity = (CommentEntity)mAdapter.getItem(position);
				if(currentUserIdx != null && entity.ownerId.equals(currentUserIdx)){
					if(entity.sticker != null && !"NONE".equals(entity.sticker)){
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Confirm");
						builder.setMessage(getResources().getString(R.string.artworks_tp_cancel_sticker));
						builder.setPositiveButton(getResources().getString(R.string.common_btn_yes), new DialogInterface.OnClickListener()
						{	
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								new DeleteStickerTask().execute(entity.id);
							}
						});
						builder.setNegativeButton(getResources().getString(R.string.common_btn_no),null);

						builder.create().show();
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Confirm");
						builder.setMessage(getResources().getString(R.string.artworks_tp_cancel_comment));
						builder.setPositiveButton(getResources().getString(R.string.common_btn_yes), new DialogInterface.OnClickListener()
						{	
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								new DeleteCommentTask().execute(entity.id);
							}
						});
						builder.setNegativeButton(getResources().getString(R.string.common_btn_no), null);
						builder.create().show();
					}
					return true;
				}
				
				
				return false;
			}
		});
		
		this.mAdapter = new CommentAdapter(getActivity());
		this.mPullRefreshCommentsListView.setAdapter(mAdapter);
		this.editText = (EditText)view.findViewById(R.id.editComment);
		
		
		
		this.btnSend = (TextView)view.findViewById(R.id.btnSend);
		this.btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!AccountManager.isLogin()){
					BigtureEnvironment.showAlertMessage(context, 16);
				}else{
					//comment를 보내야한다.
					String comment = editText.getText().toString();
					if(comment == null || comment.length() == 0){
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Confirm");
						builder.setMessage("Please input comments");
						builder.setNegativeButton("OK", null);
						builder.create().show();
					}else{
						WaitDialog.showWailtDialog(context, false);
						SendCommentTask task = new SendCommentTask();
						if(Build.VERSION.SDK_INT >= 11){
							task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,comment);
						}else{
							task.execute(comment);				
						}
					}
				}
			}
		});
		
		if(!AccountManager.isExpert()){
			view.findViewById(R.id.commentInputFrame).setVisibility(View.GONE);
			editText.setEnabled(false);
			btnSend.setVisibility(View.GONE);
		}
		
		view.findViewById(R.id.loveBtn).setOnClickListener(this);
		view.findViewById(R.id.awesomeBtn).setOnClickListener(this);
		view.findViewById(R.id.wowBtn).setOnClickListener(this);
		view.findViewById(R.id.funBtn).setOnClickListener(this);
		view.findViewById(R.id.fantasticBtn).setOnClickListener(this);
		
		
		return view;
	}

	public void updateView(){
		
		mAdapter.setComments(this.commentWrapper.comments);
		mAdapter.notifyDataSetChanged();
	}
	
	public void readComments(ArtworkEntity artwork){
		this.type = ObjectType.ARTWORK;
		this.artwork = artwork;
		reloadComments();
	}
	
	public void reloadComments(){
		page = 1;
		CommentReadTask task = new CommentReadTask();
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,artwork.artworkId);
		}else{
			task.execute(artwork.artworkId);				
		}
		
	}
	
	public void setCommentWrapper(CommentWrapperEntity entity){
		if(entity != null){
			this.commentWrapper = entity;
			this.alreadySendSticker = commentWrapper.alreadySendSticker;
			updateView();
		}
		
	}
	
	private void sendSticker(String stickerType){
		if(alreadySendSticker){
			//Alert
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Confirm");
			builder.setMessage("You already have sended sticker!");
			builder.setNegativeButton("OK", null);
			builder.create().show();
			
		}else{
			WaitDialog.showWailtDialog(context, false);
			SendStickerTask task = new SendStickerTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,stickerType);
			}else{
				task.execute(stickerType);				
			}
		}
	}

	@Override
	public void onClick(View v) {
		if(!AccountManager.isLogin()){
			BigtureEnvironment.showAlertMessage(context, 16);
		}else{
			if(v.getId() == R.id.loveBtn){
				sendSticker("TYPE_LOVE");
			}else if(v.getId() == R.id.awesomeBtn){
				sendSticker("TYPE_AWESOME");
			}else if(v.getId() == R.id.wowBtn){
				sendSticker("TYPE_WOW");
			}else if(v.getId() == R.id.funBtn){
				sendSticker("TYPE_FUN");
			}else if(v.getId() == R.id.fantasticBtn){
				sendSticker("TYPE_FANTASTIC");
			}
		}
		
	}



	class CommentReadTask extends AsyncTask<String, Void, CommentWrapperEntity>{

		@Override
		protected CommentWrapperEntity doInBackground(String... params) {
			return ArtworkHandler.getArtworkComments(params[0], page);
		}

		@Override
		protected void onPostExecute(CommentWrapperEntity result) {
			super.onPostExecute(result);
			mPullRefreshCommentsListView.onRefreshComplete();
			setCommentWrapper(result);
		}
		
	}

	class SendStickerTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			if(type.equals(ObjectType.ARTWORK))
				return ArtworkHandler.sendSticker(artwork.artworkId, params[0]);
			else
				return false;
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
			if(result){
				alreadySendSticker = true;
				reloadComments();
			}
		}
		
	}
	
	class SendCommentTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			if(type.equals(ObjectType.ARTWORK))
				return ArtworkHandler.sendComment(artwork.artworkId, params[0]);
			else
				return false;
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
			if(result){
				reloadComments();
			}
		}
		
	}
	
	class DeleteCommentTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String commentId = params[0];
			return ArtworkHandler.deleteCommentOrSticker(commentId);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(context, getResources().getString(R.string.artworks_tp_deleted_comment), Toast.LENGTH_SHORT).show();
				reloadComments();
			}
			super.onPostExecute(result);
		}
	}
	
	class DeleteStickerTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String commentId = params[0];
			return ArtworkHandler.deleteCommentOrSticker(commentId);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(context, getResources().getString(R.string.artworks_tp_deleted_sticker), Toast.LENGTH_SHORT).show();
				reloadComments();
				alreadySendSticker = false;
				
			}
			super.onPostExecute(result);
		}
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}
	
	
}
