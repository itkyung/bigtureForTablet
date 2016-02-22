package com.clockworks.android.tablet.bigture.fragment.story;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;

public class StoryTabFragment extends HorizontalTabFragment {
	PopupWindow sortOptionMenuWindow;
	View sortMenu;
	boolean isPro;

	int currentPage;
	Context context;
	String[] sortOptionMenuStr = {"RECENT","STORYNAME","EXPERTNAME"};
	
	int selectedSortOptionIdx = 0;
	StoryTabListener listener;
	
	private View sView;

	public static StoryTabFragment newInstance(boolean isPro){
		StoryTabFragment fragment = new StoryTabFragment();
		Bundle args = new Bundle();
		args.putBoolean("isPro", isPro);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
		this.listener = (StoryTabListener)activity;
		
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.currentPage = 1;
		
		Bundle args = getArguments();
		if(args != null){
			this.isPro = args.getBoolean("isPro");
		}
		
	}
	
	
	@Override
	public void initView(View view) {
		super.initView(view);
		
		this.sView = view;
		
		sortMenu = view.findViewById(R.id.tabAction3);
		sortMenu.setVisibility(View.GONE);
		
		
		if(isPro){
			this.setMenuCount(4);
			
			this.setItemText(0, getResources().getString(R.string.story_tab_own));
			this.setItemText(1, getResources().getString(R.string.story_tab_joined));
			this.setItemText(2, getResources().getString(R.string.common_tab_collections));
			this.setItemText(3, getResources().getString(R.string.common_tab_all));
			
		}else{
			this.setMenuCount(3);
			
			this.setItemText(0, getResources().getString(R.string.story_tab_joined));
			this.setItemText(1, getResources().getString(R.string.common_tab_collections));
			this.setItemText(2, getResources().getString(R.string.common_tab_all));
			
		}
		
		
		//SortOption 메뉴 
		final TextView menuSortTxt = (TextView)view.findViewById(R.id.tabSortLabel4);
		menuSortTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(sortOptionMenuWindow == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_artclass_sortoption, null);
					sortOptionMenuWindow = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					sortOptionMenuWindow.setOutsideTouchable(true);
					sortOptionMenuWindow.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				sortOptionMenuWindow.dismiss();
				View mView = sortOptionMenuWindow.getContentView();
				
				Button menuRecent = (Button)mView.findViewById(R.id.menuRecent);
				
				menuRecent.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenuWindow.dismiss();
						selectedSortOptionIdx = 0;
						menuSortTxt.setText(getResources().getString(R.string.label_recent));
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				
				
				Button menuStoryName = (Button)mView.findViewById(R.id.menuClassName);
				menuStoryName.setText(getResources().getString(R.string.story_po_storyname));
				menuStoryName.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenuWindow.dismiss();
						selectedSortOptionIdx = 1;
						menuSortTxt.setText(getResources().getString(R.string.story_po_storyname));
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				
				Button menuExpertsName = (Button)mView.findViewById(R.id.menuExpertsName);
				menuExpertsName.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenuWindow.dismiss();
						selectedSortOptionIdx = 2;
						menuSortTxt.setText(getResources().getString(R.string.story_po_expertname));
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				switch(selectedSortOptionIdx){
				case 0:
					menuRecent.setSelected(true);
					menuStoryName.setSelected(false);
					menuExpertsName.setSelected(false);
				
					break;
				case 1:
					menuRecent.setSelected(false);
					menuStoryName.setSelected(true);
					menuExpertsName.setSelected(false);
				
					break;
				case 2:
					menuRecent.setSelected(false);
					menuStoryName.setSelected(false);
					menuExpertsName.setSelected(true);
				
					break;
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			
				sortOptionMenuWindow.setAnimationStyle(-1);
				sortOptionMenuWindow.showAsDropDown(v, -30, 0);
			}
		});
				
		
		
		this.setSelection(0);
		
		
	}

	public void changeSortMenu(String menu){
		final TextView menuSortTxt = (TextView)sView.findViewById(R.id.tabSortLabel4);
		menuSortTxt.setText(menu);
	}
	
	public void hideSortMenu(){
		sortMenu.setVisibility(View.GONE);
	}
	
	public void showSortMenu(){
		sortMenu.setVisibility(View.VISIBLE);
	}
	

	public interface StoryTabListener{
		public void onSelectSortOption(String sortOption);
	}
}
