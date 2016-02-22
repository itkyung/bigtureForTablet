package com.clockworks.android.tablet.bigture.views.contest;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContestListCell extends LinearLayout {
	private Context context;
	private ContestEntity contest;
	
	private View contestImgWrapper;
	private ImageView imageContest;
	private TextView contestNowOnMsg;
	private TextView titleLabel;
	private ImageView nowonImg;
	private TextView rangeLabel;
	private TextView contestDesc;
	private Button btnSubmit;
	private View winnerWrapper;
	private ImageView winnerProfile;
	private TextView winnerName;
	private ContestCallback callback;
	BitmapFactory.Options options = null;
	
	
	public ContestListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 1;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.contestImgWrapper = findViewById(R.id.contestImgWrapper);
		this.imageContest =(ImageView)findViewById(R.id.imageContest);
		this.contestNowOnMsg = (TextView)findViewById(R.id.contestNowOnMsg);
		this.titleLabel = (TextView)findViewById(R.id.titleLabel);
		this.nowonImg = (ImageView)findViewById(R.id.nowonImg);
		this.rangeLabel = (TextView)findViewById(R.id.rangeLabel);
		this.contestDesc = (TextView)findViewById(R.id.contestDesc);
		this.btnSubmit = (Button)findViewById(R.id.btnSubmit);
		this.winnerWrapper = findViewById(R.id.winnerWrapper);
		this.winnerProfile = (ImageView)findViewById(R.id.winnerProfile);
		this.winnerName = (TextView)findViewById(R.id.winnerName);
		
		this.btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.requestToContest(contest);
			}
		});
		
		this.imageContest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.viewDetail(contest);
				
			}
		});
		
		this.titleLabel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callback.viewDetail(contest);
				
			}
		});
		
	}
	
	public void updateCell(ContestEntity entity,ContestCallback callback){
		this.contest = entity;
		this.callback = callback;
		
		String imageURL = contest.getThumbnailURL();
		if(imageURL != null){
			BitmapDownloader.getInstance().displayImage(imageURL, options, imageContest, null);
		}
		
		titleLabel.setText(contest.mainTitle);
		String startTime = DateUtil.getDateFormatString(contest.startTime, "MM.dd.yyyy");
		String endTime = DateUtil.getDateFormatString(contest.endTime, "MM.dd.yyyy");
		rangeLabel.setText(startTime + " to " + endTime);
		contestDesc.setText(contest.contents);
		
		if(contest.status.equals("ING")){
			//진행중.
			contestImgWrapper.setBackgroundResource(R.drawable.contest_list_frame_bg_nowon);
			contestNowOnMsg.setVisibility(View.VISIBLE);
			nowonImg.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.VISIBLE);
			winnerWrapper.setVisibility(View.GONE);
			
		}else{
			contestImgWrapper.setBackgroundResource(R.drawable.contest_list_frame_bg_ended);
			contestNowOnMsg.setVisibility(View.GONE);
			nowonImg.setVisibility(View.GONE);
			btnSubmit.setVisibility(View.GONE);
			winnerWrapper.setVisibility(View.VISIBLE);
			
			String profileUrl = contest.getWinnerProfileURL();
			if(profileUrl != null){
				BitmapDownloader.getInstance().displayImage(profileUrl, options, winnerProfile, null);
			}
			winnerName.setText(contest.winnerName);
		}
		
		
	}
	
	public interface ContestCallback{
		public void requestToContest(ContestEntity contest);
		public void viewDetail(ContestEntity contest);
	}
	
}
