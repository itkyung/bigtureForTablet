package com.clockworks.android.tablet.bigture.fragment.expert;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.MyBigtureActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.expert.ExpertListAdapter;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ExpertWordWrapper;
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

public class ExpertListFragment extends Fragment implements ExpertThumbnail.Callback,View.OnClickListener {
	private Context context;
	private PullToRefreshListView mPullRefreshListView;
	private ExpertListAdapter adapter;
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"ㄱ","ㄴ","ㄷ","ㄹ","ㅁ","ㅂ","ㅅ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
	
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		this.context = activity;
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_expert_list, container,false);
		
		this.mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.expertList);
		
		this.mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshExperts();
			}
			
		});
		
		this.adapter = new ExpertListAdapter(context, this);
		this.mPullRefreshListView.setAdapter(adapter);
		
		//this.mPullRefreshListView.getRefreshableView().setFastScrollEnabled(true);
		
		refreshExperts();
		
		LinearLayout sideIndex = (LinearLayout)view.findViewById(R.id.sideIndex);
		String[] sections = (String[])this.adapter.getSections();
		
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
		
		
		return view;
	}

	
	private void refreshExperts(){
		ListExpertTask task = new ListExpertTask();
		task.execute();
		
	}
	
	public class ListExpertTask extends AsyncTask<Void, Void, ArrayList<ExpertWordWrapper>>{

		@Override
		protected ArrayList<ExpertWordWrapper> doInBackground(Void... params) {
		
			return ExpertHandler.listExpertsByName();
		}

		@Override
		protected void onPostExecute(ArrayList<ExpertWordWrapper> result) {
			
			super.onPostExecute(result);
			mPullRefreshListView.onRefreshComplete();
			
			adapter.setExpertList(result);
			adapter.notifyDataSetChanged();
			
		}
		
		
		
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		if(v instanceof TextView){
			TextView tv = (TextView)v;
			String text = tv.getText().toString();
			int position = this.adapter.getPositionForSection(text);
			if(position != -1){
				this.mPullRefreshListView.getRefreshableView().setSelection(position);
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

	
	
	
}
