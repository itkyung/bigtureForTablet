package com.clockworks.android.tablet.bigture.adapter.contest;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.contest.ContestSmallCell;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleContestAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ContestEntity> contests;
	private int selectedIdx;
	
	
	
	public SimpleContestAdapter(Context context){
		this.context = context;
		this.contests = new ArrayList<ContestEntity>();
		this.selectedIdx = 0;
		
	
	}
	
	
	
	public void setContests(ArrayList<ContestEntity> contests) {
		this.contests.clear();
		this.contests.addAll(contests);
	}

	

	@Override
	public int getCount() {
		return this.contests.size();
	}

	@Override
	public Object getItem(int position) {
		return this.contests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ContestEntity contest = (ContestEntity)getItem(position);
		final int idx = position;
		
		ContestSmallCell contestView = (ContestSmallCell)convertView;
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			contestView = (ContestSmallCell)inflater.inflate(R.layout.cell_contest_small, null);
		}
		
		contestView.updateCell(contest);
		
		
		return contestView;
	}

	public String getSelectedContestId(){
		if(this.contests.size() > selectedIdx){
			ContestEntity e = this.contests.get(selectedIdx);
			return e.index;
		}else{
			return null;
		}
	}
	
	
}
