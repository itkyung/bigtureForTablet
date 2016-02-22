package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.contest.ContestArtworkAdapter;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.fragment.artwork.ArtworkTabFragment.ArtworkTabListener;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar;
import com.clockworks.android.tablet.bigture.fragment.common.TitleBar.TitleBarListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkPageWrapper;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestWinnerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ContestHandler;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.artwork.ContestArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.contest.ChildViewWinner;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


public class ContestDetailActivity extends AbstractBigtureActivity implements TitleBarListener,ArtworkThumbnail.Callback,ContestArtworkThumbnail.Callback{
	private PullToRefreshListView mPullRefreshListView;
	private TitleBar titleBar;
	private int currentPage;
	private ContestEntity contest;
	private View ingContestWrapper;
	private View electionWrapper;
	private View completeWrapper;
	private ContestArtworkAdapter adapter;
	private String currentSortOption;
	private ChildViewWinner winnerView;
	BitmapFactory.Options bitmapOptions;
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		this.context = this;
		
		setContentView(R.layout.activity_contest_detail);
		
		Intent intent = getIntent();
		contest =  (ContestEntity)intent.getParcelableExtra("contest");
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		
		titleBar = (TitleBar)getSupportFragmentManager().findFragmentById(R.id.titleBar);
		titleBar.hideHomeButton();
		titleBar.hideDrawButton();
		titleBar.setTitle(contest.mainTitle);
		
		
		currentPage = 1;
		currentSortOption = "RECENT";
				
				
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.listView1);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage = 1;
				refreshArtworks(currentSortOption);
			}
			
		});
		
		this.adapter = new ContestArtworkAdapter(this, this);
		mPullRefreshListView.setAdapter(adapter);
		
		refreshArtworks(currentSortOption);
		
		ingContestWrapper = findViewById(R.id.ingContestWrapper);
		electionWrapper = findViewById(R.id.electionWrapper);
		completeWrapper = findViewById(R.id.completeWrapper);
		
		TextView contentsText = (TextView)findViewById(R.id.contentsText);
		WebView contentsTextWithImg = (WebView)findViewById(R.id.contentsTextWithImg);
		Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
		ImageView contestLogo = (ImageView)findViewById(R.id.contestLogo);
		
		
		if (contest.contents.trim().startsWith("<")){
			contentsTextWithImg.setBackgroundColor(Color.TRANSPARENT);
			
			String html = "<html><body>" + contest.contents + "</body></html>";
			html = html.replaceAll("\n", "<br/>");
			contentsTextWithImg.loadData(html, "text/html", "utf-8");
			contentsTextWithImg.setVisibility(View.VISIBLE);
			contentsText.setVisibility(View.GONE);
		}else{
			contestLogo.setVisibility(View.GONE);
			
			contentsText.setText(contest.contents);
			contentsText.setVisibility(View.VISIBLE);
			contentsTextWithImg.setVisibility(View.GONE);
		}
		
		
		if(contest.status.equals("ING")){
			ingContestWrapper.setVisibility(View.VISIBLE);
			electionWrapper.setVisibility(View.GONE);
			completeWrapper.setVisibility(View.GONE);
			
			btnSubmit.setVisibility(View.VISIBLE);
			btnSubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent intent = new Intent(ContestDetailActivity.this,PopupArtworkListActivity.class);
//					startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
					if (!AccountManager.isLogin()){
						BigtureEnvironment.showAlertMessage(context, 20);
						return;
					}
					
					Intent intent = new Intent(context, SketchbookActivity.class);
					DrawingPurpose purpose = new DrawingPurpose();
					purpose.reason = DrawingPurpose.CONTEST;
					purpose.contestId = contest.index;
					
					intent.putExtra("drawingPurpose", purpose);
					startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
				}
			});
			
			final TextView tabSortLabel = (TextView)findViewById(R.id.tabSortLabel);
			tabSortLabel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					
					
				}
			});
			
			
			
		}else if(contest.status.equals("ELECTION")){
			ingContestWrapper.setVisibility(View.GONE);
			electionWrapper.setVisibility(View.VISIBLE);
			completeWrapper.setVisibility(View.GONE);
			btnSubmit.setVisibility(View.GONE);
			
			
		}else if(contest.status.equals("COMPLETED")){
			ingContestWrapper.setVisibility(View.GONE);
			electionWrapper.setVisibility(View.GONE);
			completeWrapper.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.GONE);
			
			this.winnerView = (ChildViewWinner)findViewById(R.id.winnerListWrapper);
			
			ContestWinnerTask task = new ContestWinnerTask();
			task.execute(contest.index);
		}
		
		TextView titleLabel = (TextView)findViewById(R.id.titleLabel);
		titleLabel.setText(contest.mainTitle);
		
		
		
		
	}
	
	private void refreshArtworks(String sortOption){
		ContestArtworksTask task = new ContestArtworksTask();
		task.execute(contest.index,sortOption);
	}
	
	

	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}

	@Override
	public void onDrawButtonClicked(TitleBar titleBar) {
		// TODO Auto-generated method stub
		
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
	public void onShowArtworkDetail(ArtworkEntity artwork) {
		ArrayList<ArtworkEntity> artworkList = new ArrayList<ArtworkEntity>();
		artworkList.add(artwork);
		
		Intent intent = new Intent(this,ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", 0);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
		
	}



	@Override
	public void onAddToCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onRemoveFromCollection(ArtworkThumbnail thumbnail,
			ArtworkEntity entity) {
		// TODO Auto-generated method stub
		
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
	public void onClickArtwork(ArtworkEntity artwork) {
		ArrayList<ArtworkEntity> artworkList = new ArrayList<ArtworkEntity>();
		artworkList.add(artwork);
		
		Intent intent = new Intent(this,ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", 0);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
		
		
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK){
//			ArtworkEntity entity = data.getParcelableExtra("selectedArtwork");
//		
//			WaitDialog.showWailtDialog(ContestDetailActivity.this, false);
//			SubmitContestTask task = new SubmitContestTask();
//			task.execute(entity.artworkId,contest.index);
//			
//		}
	}




	class ContestArtworksTask extends AsyncTask<String, Void, ArtworkPageWrapper>{

		@Override
		protected ArtworkPageWrapper doInBackground(String... params) {
			return ContestHandler.listContestArtwork(params[0], params[1], currentPage);
		}

		@Override
		protected void onCancelled(ArtworkPageWrapper result) {
			
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(ArtworkPageWrapper result) {
			super.onPostExecute(result);
			
			mPullRefreshListView.onRefreshComplete();
			adapter.setArtworks(result.artworks);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	class ContestWinnerTask extends AsyncTask<String, Void, ArrayList<ContestWinnerEntity>>{

		@Override
		protected ArrayList<ContestWinnerEntity> doInBackground(
				String... params) {
			return ContestHandler.listWinner(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<ContestWinnerEntity> result) {
			super.onPostExecute(result);
			winnerView.updateView(ContestDetailActivity.this, result);
			
		}
		
		
	}
	
	class SubmitContestTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			return ArtworkHandler.requestToContest(params[0], params[1]);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(ContestDetailActivity.this);
				builder.setTitle("Success");
				builder.setMessage(R.string.message_submit);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						refreshArtworks(currentSortOption);
					}
					
				});
				builder.create().show();
				
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(ContestDetailActivity.this);
				builder.setTitle("Error");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("Ok", null);
				builder.create().show();
			}
		}
		
		
	}

	@Override
	public void onAddClick() {
		// TODO Auto-generated method stub
		
	}
	
}
