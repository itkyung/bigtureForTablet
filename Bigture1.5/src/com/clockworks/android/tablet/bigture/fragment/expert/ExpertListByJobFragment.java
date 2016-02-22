package com.clockworks.android.tablet.bigture.fragment.expert;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.MyBigtureActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertShortListAdapter;
import com.clockworks.android.tablet.bigture.adapter.expert.JobListAdapter;
import com.clockworks.android.tablet.bigture.adapter.expert.JobListAdapter.JobListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ExpertHandler;
import com.clockworks.android.tablet.bigture.views.expert.ExpertThumbnail;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ExpertListByJobFragment extends Fragment implements JobListener,View.OnClickListener,ExpertThumbnail.Callback {
	private Context context;
	private PullToRefreshListView jobListView;
	private PullToRefreshListView expertListView;
	private JobListAdapter jobAdapter;
	private ExpertShortListAdapter listAdpater;
	private String currentSelectedJob;
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		this.context = activity;
		this.currentSelectedJob = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_expert_job_list, container,false);
		
		this.jobListView = (PullToRefreshListView)view.findViewById(R.id.jobList);
		this.jobListView.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshJobList();
			}
		});
		
		this.expertListView = (PullToRefreshListView)view.findViewById(R.id.expertList);
		this.expertListView.setOnRefreshListener(new OnRefreshListener<ListView>(){

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshExperts();
			}
		});
		
		this.jobAdapter = new JobListAdapter(context, this);
		this.jobListView.setAdapter(jobAdapter);
		
		this.listAdpater = new ExpertShortListAdapter(context,this);
		this.expertListView.setAdapter(listAdpater);
		
		LinearLayout sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		String[] sections = (String[])this.jobAdapter.getSections();
		
		TextView tmpTV = null;
		for (String section : sections) {
			tmpTV = new TextView(context);
			tmpTV.setText(section);
			tmpTV.setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
			tmpTV.setOnClickListener(this);
		}
		
		refreshJobList();
		
		return view;
	}

	
	private void refreshJobList(){
		JobListTask task = new JobListTask();
		task.execute();
	}
	
	private void refreshExperts(){
		if(this.currentSelectedJob != null){
			ExpertListTask task = new ExpertListTask();
			task.execute(this.currentSelectedJob);
		}
	}
	
	
	@Override
	public void onClickJob(String jobName) {
		this.currentSelectedJob = jobName;
		refreshExperts();
	}

	
	@Override
	public void onClick(View v) {
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			String text = tv.getText().toString();
			int position = this.jobAdapter.getPositionForSection(text);
			if(position != -1){
				this.jobListView.getRefreshableView().setSelection(position);
			}
		}
		
	}


	@Override
	public void onShowDetailExpert(SimpleUserEntity expert) {
		Intent intent = new Intent(getActivity(), MyBigtureActivity.class);
		intent.putExtra("userId", expert.index);
		intent.putExtra("viewIndex", 0);
		startActivity(intent);
	}




	class JobListTask extends AsyncTask<Void, Void, ArrayList<String>>{

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			return ExpertHandler.listJobs();
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);
			jobListView.onRefreshComplete();
			
			jobAdapter.setJobs(result);
			jobAdapter.notifyDataSetChanged();
			
			if(result.size() > 0){
				currentSelectedJob = result.get(0);
				refreshExperts();
				jobListView.getRefreshableView().setSelection(1); //section을 제외한 첫번째 row를 선택상태로 만든다.
			}
		}
		
		
	}
	
	class ExpertListTask extends AsyncTask<String, Void, ArrayList<SimpleUserEntity>>{

		@Override
		protected ArrayList<SimpleUserEntity> doInBackground(String... params) {
			String jobName = currentSelectedJob;
			return ExpertHandler.findByJob(jobName);
		}

		@Override
		protected void onPostExecute(ArrayList<SimpleUserEntity> result) {
			super.onPostExecute(result);
			expertListView.onRefreshComplete();
			
			listAdpater.setExperts(result);
			listAdpater.notifyDataSetChanged();
			
		}
		
		
		
	}
	
}
