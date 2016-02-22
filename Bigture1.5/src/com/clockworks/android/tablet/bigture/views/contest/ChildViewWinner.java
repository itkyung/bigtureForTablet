package com.clockworks.android.tablet.bigture.views.contest;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestWinnerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class ChildViewWinner extends LinearLayout {
	Context context;

	ArtworkThumbnail.Callback callback;
	HorizontalScrollView winnerView;

	BitmapFactory.Options options = new BitmapFactory.Options();
	
	public ChildViewWinner(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_winner_list, this);
		
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
	}
	
	
	@Override
	protected void onFinishInflate() {
	
		super.onFinishInflate();
		
		winnerView = (HorizontalScrollView)findViewById(R.id.winnerView);
		
		winnerView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
			
				return false;
			}
			
		});
	}

	private int makeRankImg(int rank){
		int resource = 0;
		switch(rank){
		case 1:
			resource = R.drawable.contestpage_list_winner_badge01;
			break;
		case 2:
			resource = R.drawable.contestpage_list_winner_badge02;
			break;
		case 3:
			resource = R.drawable.contestpage_list_winner_badge03;
			break;
		case 4:
			resource = R.drawable.contestpage_list_winner_badge04;
			break;	
		case 5:
			resource = R.drawable.contestpage_list_winner_badge05;
			break;	
		case 6:
			resource = R.drawable.contestpage_list_winner_badge06;
			break;	
		case 7:
			resource = R.drawable.contestpage_list_winner_badge07;
			break;
		case 8:
			resource = R.drawable.contestpage_list_winner_badge08;
			break;
		case 9:
			resource = R.drawable.contestpage_list_winner_badge09;
			break;
		case 10:
			resource = R.drawable.contestpage_list_winner_badge10;
			break;
		default :
			resource = R.drawable.contestpage_list_winner_badge10;
			break;
		}
		return resource;
	}

	public void updateView(final ArtworkThumbnail.Callback callback, final ArrayList<ContestWinnerEntity> winnerList){
		this.callback = callback;
		LayoutInflater inflater = LayoutInflater.from(context);
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		
		for (int i = 0; i < winnerList.size(); i++){
			View view = inflater.inflate(R.layout.cell_contest_winner, null);
			final ContestWinnerEntity winner = winnerList.get(i);
			
			ImageView rankImg =(ImageView)view.findViewById(R.id.rankBadge);
			rankImg.setImageResource(makeRankImg(winner.rank));
			
			ImageView artworkImage = (ImageView)view.findViewById(R.id.imageArtwork);
			artworkImage.setTag(i);
			artworkImage.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View view){
					int index = (Integer)view.getTag();
//					callback.onShowArtworkDetail(artworkList, index);
				}
			});
			
			TextView winnerName = (TextView)view.findViewById(R.id.winnerName);
			winnerName.setText(winner.winnerName);
			
			String imageURL = winner.getThumbnailURL();
			BitmapDownloader.getInstance().displayImage(imageURL, options, artworkImage, null);
			
			
			layout.addView(view);
			
			artworkImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new ArtworkLoadTask().execute(winner.artworkId);
				}
			});
		}
		
		winnerView.removeAllViews();
		winnerView.addView(layout);
		winnerView.scrollTo(0, 0);
		
	}
	
	class ArtworkLoadTask extends AsyncTask<String, Void, ArtworkEntity>{

		@Override
		protected ArtworkEntity doInBackground(String... params) {
			return ArtworkHandler.loadArtwork(params[0]);
			
		}

		@Override
		protected void onPostExecute(ArtworkEntity result) {
			super.onPostExecute(result);
			callback.onShowArtworkDetail(result);
		}
		
	}
	
}
