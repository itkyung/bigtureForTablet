package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.my.ExpertCareerAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.ExpertNewsAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.ExpertCareerAdapter.CareerMenuListener;
import com.clockworks.android.tablet.bigture.adapter.my.ExpertNewsAdapter.NewsMenuListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.CareerHandleTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.CareerReadTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.NewsHandleTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.NewsReadTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.CareerHandleTask.CareerHandleTaskListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CareerNewsListFragment extends Fragment implements View.OnClickListener,CareerMenuListener,NewsMenuListener{
	private CareerNewsListener listener;
	private PullToRefreshListView careerListView;
	private PullToRefreshListView newsListView;
	private String userId;
	private boolean myPage;
	private Context context;
	
	public static CareerNewsListFragment newInstance(String userId,boolean myPage){
		CareerNewsListFragment fragment = new CareerNewsListFragment();
		
		Bundle args = new Bundle();
		args.putString("userId", userId);
		args.putBoolean("myPage", myPage);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if(args != null){
			this.userId = args.getString("userId");
			this.myPage = args.getBoolean("myPage");
		}else{
			myPage = false;
		}
	}



	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		this.context = activity;
		this.listener = (CareerNewsListener)activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_career_news_list, container,false);
		
		ImageView addCareerImg = (ImageView)view.findViewById(R.id.imgAddCareer);
		addCareerImg.setOnClickListener(this);
		
		TextView addCareerText = (TextView)view.findViewById(R.id.txtAddCareer);
		
		
		ImageView addNewsImg = (ImageView)view.findViewById(R.id.imgAddNews);
		addNewsImg.setOnClickListener(this);
		
		TextView addNewsText = (TextView)view.findViewById(R.id.txtAddNews);
		
		
		if(!myPage){
			addCareerImg.setVisibility(View.GONE);
			addNewsImg.setVisibility(View.GONE);
			addCareerText.setText("Career");
			addNewsText.setText("News");
		}else{
			addCareerText.setOnClickListener(this);
			addNewsText.setOnClickListener(this);
		}
		
		careerListView = (PullToRefreshListView)view.findViewById(R.id.careerList);
		
		careerListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			
				refreshCareer();
			}
			
		});
		
		
		newsListView = (PullToRefreshListView)view.findViewById(R.id.newsList);
		newsListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				refreshNews();
			}
		});
		
		
		registerForContextMenu(careerListView);
		registerForContextMenu(newsListView);
		
		refreshCareer();
		refreshNews();
		
		return view;
	}

	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		if (info.position > 0){
			menu.setHeaderTitle("Select Action");
			
			menu.add(1, 1, 1, "Edit");
			menu.add(1, 2, 2, "Delete");
		}
		
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	public void refreshCareer(){
		CareerReadTask task1 = new CareerReadTask(new CareerReadTask.CareerReadTaskListener(){
			@Override
			public void onComplete(CareerReadTask task, List<CareerEntity> careerList){
				careerListView.onRefreshComplete();
				ExpertCareerAdapter adapter = new ExpertCareerAdapter(context, careerList, myPage,CareerNewsListFragment.this);
				careerListView.setAdapter(adapter);
			}
		});
		if(Build.VERSION.SDK_INT >= 11){
			task1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId);
		}else{
			task1.execute(userId);				
		}
		
	}
	
	public void refreshNews(){
		NewsReadTask task = new NewsReadTask(new NewsReadTask.NewsReadTaskListener() {
			
			@Override
			public void onComplete(NewsReadTask task, List<NewsEntity> newsList) {
				newsListView.onRefreshComplete();
				ExpertNewsAdapter adpater = new ExpertNewsAdapter(context, newsList, myPage,CareerNewsListFragment.this);
				newsListView.setAdapter(adpater);
			}
		});
		if(Build.VERSION.SDK_INT >= 11){
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId);
		}else{
			task.execute(userId);				
		}
		
	}
	
	
	@Override
	public void clickEdit(CareerEntity entity) {
		this.listener.editCareer(entity);
	}



	@Override
	public void clickDelete(CareerEntity entity) {
		CareerHandleTask task = new CareerHandleTask(CareerHandleTask.ACTION_DELETE, new CareerHandleTaskListener() {
			
			@Override
			public void onComplete(CareerHandleTask task, boolean result) {
				refreshCareer();
			}
		});
		task.execute(entity.index);
	}

	@Override
	public void clickEdit(NewsEntity entity) {
		this.listener.editNews(entity);
	}



	@Override
	public void clickDelete(NewsEntity entity) {
		NewsHandleTask task = new NewsHandleTask(new NewsHandleTask.NewsHandleTaskListener() {
			
			@Override
			public void onComplete(NewsHandleTask task, boolean result) {
				refreshNews();
			}
		});
		
		task.execute(entity.index);
	}



	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.imgAddCareer || 
				v.getId() == R.id.txtAddCareer){
			this.listener.clickAddCareer();
		}else if(v.getId() == R.id.imgAddNews ||
				v.getId() == R.id.txtAddNews){
			this.listener.clickAddNews();
		}
		
	}



	public interface CareerNewsListener{
		void clickAddCareer();
		void editCareer(CareerEntity entity);
		void clickAddNews();
		void editNews(NewsEntity entity);
		
	}
}
