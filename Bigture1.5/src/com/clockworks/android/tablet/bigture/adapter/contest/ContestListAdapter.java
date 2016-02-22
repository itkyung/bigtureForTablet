package com.clockworks.android.tablet.bigture.adapter.contest;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ContestEntity;
import com.clockworks.android.tablet.bigture.views.contest.ContestListCell;
import com.clockworks.android.tablet.bigture.views.contest.ContestSmallCell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContestListAdapter extends BaseAdapter {
	private ArrayList<ContestEntity> contests;
	private Context context;
	private ContestListCell.ContestCallback callback;
	
	public ContestListAdapter(Context context,ContestListCell.ContestCallback callback){
		this.context = context;
		this.contests = new ArrayList<ContestEntity>();
		this.callback = callback;
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
		
		ContestListCell contestView = (ContestListCell)convertView;
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			contestView = (ContestListCell)inflater.inflate(R.layout.cell_contest_list, null);
		}
		
		contestView.updateCell(contest,callback);
		
		
		return contestView;
	}

}
