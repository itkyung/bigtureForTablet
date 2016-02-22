package com.clockworks.android.tablet.bigture.views.contest;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkDetailCell.Callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContestSmallCell extends LinearLayout implements Checkable {
	Context context;
	ContestEntity contest;
	BitmapFactory.Options options = null;
	ImageView contestImg;
	TextView titleLabel;
	TextView rangeLabel;
	boolean checked;
	
	public ContestSmallCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.context = context;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 1;
		this.checked = false;
	}

	public void updateCell(ContestEntity entity){
		this.contest = entity;
		String imageURL = contest.getThumbnailURL();
		if(imageURL != null){
			BitmapDownloader.getInstance().displayImage(imageURL, options, contestImg, null);
		}
		
		titleLabel.setText(contest.mainTitle);
		
		String startTime = DateUtil.getDateFormatString(contest.startTime, "MM.dd.yyyy");
		String endTime = DateUtil.getDateFormatString(contest.endTime, "MM.dd.yyyy");
		rangeLabel.setText(startTime + " to " + endTime);
		
	}
	
	@Override
	protected void onFinishInflate() {
		
		super.onFinishInflate();
		
		contestImg = (ImageView)findViewById(R.id.imageContest);
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		rangeLabel = (TextView)findViewById(R.id.rangeLabel);
		if(checked){
			setBackgroundColor(getResources().getColor(R.color.popup_list_selected));
		}else{
			setBackgroundColor(getResources().getColor(R.color.white));
		}
	}



	@Override
	public boolean isChecked() {
		return checked;
	}

	@Override
	public void setChecked(boolean checked) {
		this.checked = checked;
		if(checked){
			setBackgroundColor(getResources().getColor(R.color.popup_list_selected));
		}else{
			setBackgroundColor(getResources().getColor(R.color.white));
		}
	}

	@Override
	public void toggle() {
		if(checked){
			setChecked(false);
		}else{
			setChecked(true);
		}

	}

}
