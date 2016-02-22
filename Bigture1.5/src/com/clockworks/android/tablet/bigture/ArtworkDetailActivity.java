package com.clockworks.android.tablet.bigture;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.ViewPostCardPopupActivity.PostCardDeleteTask;
import com.clockworks.android.tablet.bigture.adapter.artwork.ArtworkDetailAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader.BitmapDownloaderListener;
import com.clockworks.android.tablet.bigture.fragment.common.CommentFragment;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommentWrapperEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkDetailCell;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;

import com.handmark.pulltorefresh.library.PullToRefreshListView;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class ArtworkDetailActivity extends AbstractBigtureActivity implements TitleBarListener, ArtworkDetailCell.Callback{
	private TitleBar titleBar;
	private ArrayList<ArtworkEntity> artworkList;
	private ListView artworkListView;
	
	private ArtworkEntity currentArtwork;
	private CommentFragment commentFragment;
	private boolean fromMyBigture=false;
	
	boolean scrolling;
	boolean adjusting;
	boolean flingForPaging;
	
	int currentIndex;
	float touchSpeed;
	float lastTouchY;
	long lastEventTime;
	
	PopupWindow moreMenu;
	String myUserId;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_artwork_detail);
		
		myUserId = AccountManager.getInstance().getUserIndex();
		
		initView();
		
		if (savedInstanceState == null){
			Bundle bundle = getIntent().getExtras();
			artworkList  = bundle.getParcelableArrayList("artworkList");
			currentIndex = bundle.getInt("currentIndex");
			fromMyBigture = bundle.getBoolean("fromMyBigture", false);
		}else{
			artworkList  = savedInstanceState.getParcelableArrayList("artworkList");
			currentIndex = savedInstanceState.getInt("currentIndex");
			fromMyBigture = savedInstanceState.getBoolean("fromMyBigture");
		}		
		
		this.currentArtwork = artworkList.get(currentIndex);
		
		ArtworkDetailAdapter adapter = new ArtworkDetailAdapter(ArtworkDetailActivity.this, artworkList, ArtworkDetailActivity.this,fromMyBigture);
		artworkListView.setAdapter(adapter);
		artworkListView.setSelectionFromTop(currentIndex, 20);
			
		readComment(currentArtwork);

		titleBar.setTitle(getResources().getString(R.string.mybigture_po_artworks));
		
	}

	private void initView(){
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideHomeButton();
		titleBar.showDrawButton();
		
		//Comment List Fragment를 추가한다.
		commentFragment = (CommentFragment)getSupportFragmentManager().findFragmentById(R.id.commentFragment);
		
		
		artworkListView = (ListView)findViewById(R.id.artworkList);
		artworkListView.setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View arg0, MotionEvent event)
			{
				int action = event.getAction() & MotionEvent.ACTION_MASK;
				
				if (action == MotionEvent.ACTION_DOWN)
				{
					lastTouchY = event.getY();
					lastEventTime = event.getEventTime();
				}
				else if (action == MotionEvent.ACTION_MOVE)
				{
					touchSpeed = 1000.f * (event.getY() - lastTouchY) / (event.getEventTime() - lastEventTime); 
					lastEventTime = event.getEventTime();
					lastTouchY = event.getY();
				}
				
				return false;
			}
		});
		
		artworkListView.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState){
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && scrolling == true){
					if (!flingForPaging){
						if (adjustScrollPosition()){
							scrolling = false;
							adjusting = true;
							
							ArtworkDetailAdapter adapter = (ArtworkDetailAdapter)artworkListView.getAdapter();
							adapter.scrolling = false;
						}else{
							adjusting = false;
							ArtworkDetailAdapter adapter = (ArtworkDetailAdapter)artworkListView.getAdapter();
							adapter.scrolling = false;

							readComment(currentArtwork);
						}
					}
				}else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && adjusting == true){
					adjusting = false;
					ArtworkDetailAdapter adapter = (ArtworkDetailAdapter)artworkListView.getAdapter();
					adapter.scrolling = false;

					readComment(currentArtwork);
				}
				else if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
					scrolling = true;
					ArtworkDetailAdapter adapter = (ArtworkDetailAdapter)artworkListView.getAdapter();
					adapter.scrolling = true;
				}
				else if (scrollState == OnScrollListener.SCROLL_STATE_FLING){
					if (flingForPaging || Math.abs(touchSpeed) < 500)
						return;
					
					flingForPaging = true;
					
					artworkListView.post(new Runnable() {

						@Override
						public void run(){
							if (touchSpeed < 0){ // scroll up
							
								int viewIndex = getTouchedChildViewIndex();
								
								View childView = artworkListView.getChildAt(viewIndex + 1);
								currentIndex = artworkListView.getFirstVisiblePosition() + viewIndex + 1;
								
								if (childView == null)
								{
									childView = artworkListView.getChildAt(viewIndex);
									currentIndex = artworkListView.getFirstVisiblePosition() + viewIndex;
								}
								
								currentArtwork = artworkList.get(currentIndex);
								int cy2 = childView.getTop() + childView.getHeight() / 2;
								int cy1 = artworkListView.getHeight() / 2;
								
								final int scrollY = cy2 - cy1;
								final int duration = scrollY * 1000 / (int)Math.abs(touchSpeed);
								artworkListView.smoothScrollBy(scrollY, duration);
							}else if (touchSpeed > 0){ // scroll down
							
								int viewIndex = getTouchedChildViewIndex();

								View childView = artworkListView.getChildAt(viewIndex - 1);
								currentIndex = artworkListView.getFirstVisiblePosition() + viewIndex - 1;
								
								if (childView == null)
								{
									childView = artworkListView.getChildAt(viewIndex);
									currentIndex = artworkListView.getFirstVisiblePosition() + viewIndex;
								}
								
								currentArtwork = artworkList.get(currentIndex);
								int cy2 = childView.getTop() + childView.getHeight() / 2;
								int cy1 = artworkListView.getHeight() / 2;
								
								final int scrollY = cy1 - cy2;
								final int duration = scrollY * 1000 / (int)Math.abs(touchSpeed);
								artworkListView.smoothScrollBy(-scrollY, duration);
							}

							flingForPaging = false;
						}
						
					});
				}
			}
		});
		
		
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		if (artworkList == null){
			artworkList  = savedInstanceState.getParcelableArrayList("artworkList");
			currentIndex = savedInstanceState.getInt("currentIndex");
			fromMyBigture = savedInstanceState.getBoolean("fromMyBigture");
			
			ArtworkDetailAdapter adapter = new ArtworkDetailAdapter(ArtworkDetailActivity.this, artworkList, this,fromMyBigture);
			artworkListView.setAdapter(adapter);
			artworkListView.setSelectionFromTop(currentIndex, 20);
	
			this.currentArtwork = artworkList.get(currentIndex);
			
			readComment(currentArtwork);
		}
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState){
		outState.putParcelableArrayList("artworkList", (ArrayList<ArtworkEntity>)artworkList);
		outState.putInt("currentIndex", currentIndex);
		outState.putBoolean("fromMyBigture", fromMyBigture);
		super.onSaveInstanceState(outState);
	}

	
	private int getTouchedChildViewIndex(){
		int viewIndex = 0;

		for (int i = 0; i < artworkListView.getChildCount(); i++)
		{
			View childView = artworkListView.getChildAt(i);
			if (childView.getTop() < lastTouchY && childView.getBottom() > lastTouchY)
			{
				viewIndex = i;
				break;
			}
		}

		return viewIndex;
	}
	
	private boolean adjustScrollPosition(){
		View item = artworkListView.getChildAt(0);

		if (artworkListView.getFirstVisiblePosition() == 0 && item.getTop() >= 0)
			return false;
		
		int top = Math.abs(item.getTop());
		int bottom = Math.abs(item.getBottom());
		
		int pos = 0;
		
		final int position; 
		if (top >= bottom)
		{
			position = artworkListView.getFirstVisiblePosition() + 1;
			pos = 1;
		}
		else
		{
			position = artworkListView.getFirstVisiblePosition();
			pos = 0;
		}

		item = artworkListView.getChildAt(pos);
		int c2 = artworkListView.getHeight() / 2;
		int c1 = (item.getTop() + item.getBottom()) / 2;
		
		boolean result = false;
		
		final int scrollY = c1 - c2;
		
		if (scrollY != 0)
		{
			final int duration = (1500 * Math.abs(scrollY)) / c2;
			currentIndex = position;
			currentArtwork = artworkList.get(currentIndex);
			
			artworkListView.post(new Runnable()
			{
				public void run() {
	
					artworkListView.smoothScrollBy(scrollY, duration);
				}
			});
			
			result = true;
		}
		
		return result;
	}
	
	private void readComment(ArtworkEntity artwork){
		commentFragment.readComments(artwork);
	}
	
	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		Intent intent = new Intent(this, SketchbookActivity.class);
		DrawingPurpose purpose = new DrawingPurpose();
		purpose.reason = DrawingPurpose.NORMAL;
		
		intent.putExtra("drawingPurpose", purpose);
		this.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
	}

	@Override
	public void onMessageClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMainButtonClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShowUserPage(ArtworkDetailCell cell,
			ArtworkEntity artworkEntity) {
		Intent intent = new Intent(this, MyBigtureActivity.class);
		intent.putExtra("userId", artworkEntity.userId);
		intent.putExtra("viewIndex", 0);
		startActivity(intent);
		
	}

	@Override
	public void onShowArtwork(ArtworkDetailCell cell,
			ArtworkEntity artworkEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContestButtonClicked(ArtworkDetailCell cell, View view,
			ArtworkEntity artworkEntity) {
		
		if(artworkEntity.referenceType != null && artworkEntity.referenceType.equals("CONTEST")){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Confirm");
			builder.setMessage(R.string.artworks_sp_contest_alread_submitted);
			builder.setPositiveButton("Ok", null);
			builder.create().show();
			
		}else{
			Intent intent = new Intent(context, PopupContestListActivity.class);
			intent.putExtra("artworkId", artworkEntity.artworkId);
			startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SHOW_CONTESTENTRY);
		}
	}

	@Override
	public void onReplyMapButtonClicked(ArtworkDetailCell cell, View view,
			ArtworkEntity artworkEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoreMenuButtonClicked(ArtworkDetailCell cell, View view,
			ArtworkEntity artworkEntity) {
		
		String myUserId = AccountManager.getInstance().getUserIndex();
		boolean myArtwork = artworkEntity.userId.equals(myUserId) ? true : false;
		
		
		//if(moreMenu == null){
			LayoutInflater inflater = LayoutInflater.from(this);
			View menuView = null;
			if(myArtwork){		
				menuView = inflater.inflate(R.layout.menu_artworks_more, null);
			}else{
				menuView = inflater.inflate(R.layout.menu_artworks_spam, null);
			}
			moreMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			moreMenu.setOutsideTouchable(true);
			moreMenu.setBackgroundDrawable(new BitmapDrawable()) ;
		//}
		
		moreMenu.dismiss();
		View mView = moreMenu.getContentView();
		
		if(myArtwork){
		
			Button menuSaveImage = (Button)mView.findViewById(R.id.context_menu_save_image);
			menuSaveImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					moreMenu.dismiss();
					
					WaitDialog.showWailtDialog(ArtworkDetailActivity.this, false);
					String imageURL = currentArtwork.getArtworkImageURL();
					BitmapDownloader.getInstance().downloadBitmap(imageURL, null, new BitmapDownloaderListener() {
	
						
						@Override
						public void onComplete(Bitmap bitmap){
							WaitDialog.hideWaitDialog();
							
							if (bitmap != null){
								//File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bigture");
								File storageDir = new File(Environment.getExternalStorageDirectory(), "Bigture");
								if (!storageDir.exists())
									storageDir.mkdirs();
						
								String fileName = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmss") + ".png";
								String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
								ImageUtil.saveBitmap(bitmap, filePath);
								
								Toast.makeText(ArtworkDetailActivity.this, "Image saved in Photo Album.", Toast.LENGTH_LONG).show();
								
								if(Build.VERSION.SDK_INT >= 16){
									scanPhoto(filePath);
									
								}else{
								
									sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
						                + Environment.getExternalStorageDirectory())));
								
								}
							}else{
								BigtureEnvironment.showAlertMessage(ArtworkDetailActivity.this, 99);
							}
						}
					});
				}
			});
			
			Button menuShare = (Button)mView.findViewById(R.id.context_menu_share_other);
			menuShare.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					moreMenu.dismiss();
					WaitDialog.showWailtDialog(ArtworkDetailActivity.this, false);
					String imageURL = currentArtwork.getArtworkImageURL();
					BitmapDownloader.getInstance().downloadBitmap(imageURL, null, new BitmapDownloaderListener() {
	
						@Override
						public void onComplete(Bitmap bitmap){
							WaitDialog.hideWaitDialog();
	
							if (bitmap != null){
								String fileName = DateUtil.getDateFormatString(new Date(), "yyyyMMddHHmmss") + ".image";
								String filePath = FileUtil.getTempPath(ArtworkDetailActivity.this) + File.separator + fileName;
								ImageUtil.saveBitmap(bitmap, filePath);
								
								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("image/*");
								
								share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));
								
								String title = "from Bigture";
								if (AccountManager.isLogin()){
									AccountEntity entity = AccountManager.getInstance().getAccountEntity();
									title += " " + entity.nickName;
								}
								
								share.putExtra(Intent.EXTRA_SUBJECT, title);
								startActivity(Intent.createChooser(share, "Share Image"));
							}else{
								BigtureEnvironment.showAlertMessage(ArtworkDetailActivity.this, 99);
							}
						}
					});
				
				}
			});
			
			Button menuSendCard = (Button)mView.findViewById(R.id.context_menu_send_card);
			menuSendCard.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					moreMenu.dismiss();
					
					Intent intent = new Intent(ArtworkDetailActivity.this, SendPostCardPopupActivity.class);
					intent.putExtra("selectedArtwork", currentArtwork);
					intent.putExtra("reply", false);
					startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SEND_CARD);
				}
			});
			
			
			
			Button menuEditArtworks = (Button)mView.findViewById(R.id.context_menu_edit_artworks);
			
		
				menuEditArtworks.setVisibility(View.VISIBLE);
				menuEditArtworks.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						moreMenu.dismiss();
						
						Intent intent = new Intent(ArtworkDetailActivity.this,PopupEditArtworkActivity.class);
						intent.putExtra("artwork", currentArtwork);
						startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_EDIT_ARTWORK);
					}
				});
		
			
			Button menuDelete = (Button)mView.findViewById(R.id.context_menu_delete);
			
			menuDelete.setVisibility(View.VISIBLE);
			menuDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					moreMenu.dismiss();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(ArtworkDetailActivity.this);
					builder.setTitle("Delete Artwork");
					builder.setMessage(R.string.artworks_sp_delete_picture);
					builder.setNegativeButton("Cancel", null);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							WaitDialog.showWailtDialog(context, false);
							ArtworkDeleteTask task = new ArtworkDeleteTask();
							task.execute(currentArtwork.artworkId);
						}
						
					});
					builder.create().show();
				}
			});
		}else{
			Button menuSpam = (Button)mView.findViewById(R.id.context_menu_spam);
			
			menuSpam.setVisibility(View.VISIBLE);
			menuSpam.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					moreMenu.dismiss();
					AlertDialog.Builder builder = new AlertDialog.Builder(ArtworkDetailActivity.this);
					builder.setTitle("Report Spam");
					builder.setMessage(R.string.artworks_sp_report_picture);
					builder.setNegativeButton("Cancel", null);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							WaitDialog.showWailtDialog(context, false);
							ArtworkSpamTask task = new ArtworkSpamTask();
							task.execute(currentArtwork.artworkId);
						}
						
					});
					builder.create().show();
				
				}
			});
			
		}
	
		
		moreMenu.setAnimationStyle(-1);
		
		if(Build.VERSION.SDK_INT >= 19){
			if(myArtwork){
				moreMenu.showAsDropDown(view, -450, -600);
			}else{
				moreMenu.showAsDropDown(view, -450, -200);
			}
			
		}else{
			if(myArtwork){
				moreMenu.showAsDropDown(view, -250, -300);
			}else{
				moreMenu.showAsDropDown(view, -250, -120);
			}
			
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			if(requestCode == BigtureEnvironment.EVENT_CODE_SHOW_CONTESTENTRY){
				//컨테스트 참여이후.
				currentArtwork.referenceType = "CONTEST";
				((ArtworkDetailAdapter)artworkListView.getAdapter()).notifyDataSetChanged();
				
			}else if(requestCode == BigtureEnvironment.EVENT_CODE_EDIT_ARTWORK){
				//아트워크 수정완료이후.
				ArtworkEntity newArtwork = (ArtworkEntity)data.getParcelableExtra("artwork");
				currentArtwork.title = newArtwork.title;
				currentArtwork.opend = newArtwork.opend;
				currentArtwork.comment = newArtwork.comment;
				
				artworkList.set(currentIndex, currentArtwork);
				
				((ArtworkDetailAdapter)artworkListView.getAdapter()).notifyDataSetChanged();
			}
		}
		
		
	}
	
	MediaScannerConnection msConn = null;
	public void scanPhoto(final String imageFileName){
		msConn = new MediaScannerConnection(ArtworkDetailActivity.this,new MediaScannerConnectionClient(){
			public void onMediaScannerConnected(){
				msConn.scanFile(imageFileName, null);
			}
			
			public void onScanCompleted(String path, Uri uri){
				msConn.disconnect();
				
			}
		});
		msConn.connect();
	} 
	
	class ArtworkDeleteTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.deleteArtwork(params[0]);
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
				setResult(RESULT_OK);
				finish();
			}
		}
	}
	
	class ArtworkSpamTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.reportSpam(params[0]);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Success");
				builder.setMessage(R.string.message_reported);
				builder.setPositiveButton("Ok",  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						currentArtwork.spam = true;
						((ArtworkDetailAdapter)artworkListView.getAdapter()).notifyDataSetChanged();
					}
				});
				builder.create().show();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("Ok", null);
				builder.create().show();
			}
		}
		
	}
	
}
