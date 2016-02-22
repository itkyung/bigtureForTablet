package com.clockworks.android.tablet.bigture.fragment.artclass;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;


public class ArtClassTabFragment extends HorizontalTabFragment   {
	PopupWindow sortOptionMenuWindow;
	PopupWindow statusMenuWindow;
	ArtClassTabListener listener;
	View statusMenu;
	View sortMenu;
	
	boolean isPro;

	int currentPage;
	Context context;
	String[] sortOptionMenuStr = {"RECENT","CLASSNAME","EXPERTNAME"};
	String[] statusMenuStr = {"ALL","DOING","DONE"};
	
	int selectedSortOptionIdx = 0;
	int selectedStatusIdx = 0;
	
	public static ArtClassTabFragment newInstance(boolean isPro){
		ArtClassTabFragment fragment = new ArtClassTabFragment();
		Bundle args = new Bundle();
		args.putBoolean("isPro", isPro);
		fragment.setArguments(args);
		return fragment;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (ArtClassTabListener)activity;
		context = activity;
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
		
		statusMenu = view.findViewById(R.id.tabAction2);
		statusMenu.setVisibility(View.VISIBLE);
		
		sortMenu = view.findViewById(R.id.tabAction3);
		sortMenu.setVisibility(View.GONE);
		
		
		if(isPro){
			this.setMenuCount(4);
			
			this.setItemText(0, "Own class");
			this.setItemText(1, "Joined class");
			this.setItemText(2, "Collection");
			this.setItemText(3, "All");
			
		}else{
			this.setMenuCount(3);
			
			this.setItemText(0, "Joined class");
			this.setItemText(1, "Collection");
			this.setItemText(2, "All");
			
		}
		
		//Status 메뉴 
		final TextView menuTxt = (TextView)view.findViewById(R.id.tabSortLabel3);
		menuTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(statusMenuWindow == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_artclass_status, null);
					statusMenuWindow = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					statusMenuWindow.setOutsideTouchable(true);
					statusMenuWindow.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				statusMenuWindow.dismiss();
				View mView = statusMenuWindow.getContentView();
				
				Button menuAll = (Button)mView.findViewById(R.id.menuAll);
				
				menuAll.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						statusMenuWindow.dismiss();
						selectedStatusIdx = 0;
						menuTxt.setText("All");
						listener.onSelectStatus(statusMenuStr[selectedStatusIdx]);
					}
				});
				
				
				
				Button menuNowOn = (Button)mView.findViewById(R.id.menuNowOn);
				menuNowOn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						statusMenuWindow.dismiss();
						selectedStatusIdx = 1;
						menuTxt.setText("Now on");
						listener.onSelectStatus(statusMenuStr[selectedStatusIdx]);
					}
				});
				
				
				Button menuEnd = (Button)mView.findViewById(R.id.menuEnd);
				menuEnd.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						statusMenuWindow.dismiss();
						selectedStatusIdx = 2;
						menuTxt.setText("End");
						listener.onSelectStatus(statusMenuStr[selectedStatusIdx]);
					}
				});
				
				switch(selectedStatusIdx){
				case 0:
					menuAll.setSelected(true);
					menuNowOn.setSelected(false);
					menuEnd.setSelected(false);
				
					break;
				case 1:
					menuAll.setSelected(false);
					menuNowOn.setSelected(true);
					menuEnd.setSelected(false);
				
					break;
				case 2:
					menuAll.setSelected(false);
					menuNowOn.setSelected(false);
					menuEnd.setSelected(true);
				
					break;
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			
				statusMenuWindow.setAnimationStyle(-1);
				statusMenuWindow.showAsDropDown(v, -30, 0);
			}
		});
		
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
						menuSortTxt.setText("Recent");
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				
				
				Button menuClassName = (Button)mView.findViewById(R.id.menuClassName);
				menuClassName.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenuWindow.dismiss();
						selectedSortOptionIdx = 1;
						menuSortTxt.setText("Class name");
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				
				Button menuExpertsName = (Button)mView.findViewById(R.id.menuExpertsName);
				menuExpertsName.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenuWindow.dismiss();
						selectedSortOptionIdx = 2;
						menuSortTxt.setText("Expert's name");
						listener.onSelectSortOption(sortOptionMenuStr[selectedSortOptionIdx]);
					}
				});
				
				switch(selectedSortOptionIdx){
				case 0:
					menuRecent.setSelected(true);
					menuClassName.setSelected(false);
					menuExpertsName.setSelected(false);
				
					break;
				case 1:
					menuRecent.setSelected(false);
					menuClassName.setSelected(true);
					menuExpertsName.setSelected(false);
				
					break;
				case 2:
					menuRecent.setSelected(false);
					menuClassName.setSelected(false);
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

	public void hideSortMenu(){
		sortMenu.setVisibility(View.GONE);
	}
	
	public void showSortMenu(){
		sortMenu.setVisibility(View.VISIBLE);
	}
	
	public void hideStatusMenu(){
		statusMenu.setVisibility(View.GONE);
	}
	
	public void showStatusMenu(){
		statusMenu.setVisibility(View.VISIBLE);
	}
	
	
	public interface ArtClassTabListener{
		public void onSelectSortOption(String sortOption);
		public void onSelectStatus(String status);
	}
	
	
}
