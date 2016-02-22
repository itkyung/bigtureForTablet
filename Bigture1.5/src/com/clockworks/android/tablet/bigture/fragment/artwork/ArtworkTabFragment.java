package com.clockworks.android.tablet.bigture.fragment.artwork;

import android.app.Activity;
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


public class ArtworkTabFragment extends HorizontalTabFragment {
	PopupWindow sortOptionMenu;
	ArtworkTabListener listener;
	View allMenu;

	int currentPage;
	Context context;
	String[] menuStrs = {"RECENT","POPULAR","LOVE","AWESOME","WOW","FUN","FANTASTIC"};
	int selectedMenuIdx = 0;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (ArtworkTabListener)activity;
		context = activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.currentPage = 1;
	}

	

	@Override
	public void initView(View view) {
		super.initView(view);
		
		allMenu = view.findViewById(R.id.tabAction1);
		allMenu.setVisibility(View.VISIBLE);
		
		this.setMenuCount(3);
		
		this.setItemText(0, getResources().getString(R.string.artworks_tab_all));
		this.setItemText(1, getResources().getString(R.string.artworks_tab_likeus));
		this.setItemText(2, getResources().getString(R.string.common_tab_collections));
		
		TextView hiddenMenu = (TextView)view.findViewById(R.id.tabSortLabel1);
		hiddenMenu.setVisibility(View.GONE);
		ImageView hiddenImg = (ImageView)view.findViewById(R.id.imageDown2);
		hiddenImg.setVisibility(View.GONE);
		
		
		final TextView menuTxt = (TextView)view.findViewById(R.id.tabSortLabel2);
		menuTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(sortOptionMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_artwork_list_2, null);
					sortOptionMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					sortOptionMenu.setOutsideTouchable(true);
					sortOptionMenu.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				sortOptionMenu.dismiss();
				View mView = sortOptionMenu.getContentView();
				
				Button menuRecent = (Button)mView.findViewById(R.id.menuRecent);
				
				menuRecent.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 0;
						menuTxt.setText(getResources().getString(R.string.label_recent));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
					}
				});
				
				
				
				Button menuPopular = (Button)mView.findViewById(R.id.menuPopular);
				menuPopular.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 1;
						menuTxt.setText(getResources().getString(R.string.label_popular));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
					}
				});
				
				
				Button menuLove = (Button)mView.findViewById(R.id.menuLove);
				menuLove.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 2;
						menuTxt.setText(getResources().getString(R.string.common_emo_sticker_lovely));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
					}
				});
				
				Button menuAwesome = (Button)mView.findViewById(R.id.menuAwesome);
				menuAwesome.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 3;
						menuTxt.setText(getResources().getString(R.string.common_emo_sticker_awesome));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
						
						
					}
				});
				
				Button menuWow = (Button)mView.findViewById(R.id.menuWow);
				menuWow.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 4;
						menuTxt.setText("Wow");
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
						
					}
				});
				
				Button menuFun = (Button)mView.findViewById(R.id.menuFun);
				menuFun.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 5;
						menuTxt.setText(getResources().getString(R.string.common_emo_sticker_fun));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
					}
				});
				
				Button menuFantastic = (Button)mView.findViewById(R.id.menuFantastic);
				menuFantastic.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sortOptionMenu.dismiss();
						selectedMenuIdx = 6;
						menuTxt.setText(getResources().getString(R.string.common_emo_sticker_fantastic));
						listener.onSelectSortOption(menuStrs[selectedMenuIdx]);
					
					}
				});
				
				
				switch(selectedMenuIdx){
				case 0:
					menuRecent.setSelected(true);
					menuPopular.setSelected(false);
					menuLove.setSelected(false);
					menuAwesome.setSelected(false);
					menuWow.setSelected(false);
					menuFun.setSelected(false);
					menuFantastic.setSelected(false);
					
					break;
				case 1:
					menuRecent.setSelected(false);
					menuPopular.setSelected(true);
					menuLove.setSelected(false);
					menuAwesome.setSelected(false);
					menuWow.setSelected(false);
					menuFun.setSelected(false);
					menuFantastic.setSelected(false);
					break;
				case 2:
					menuRecent.setSelected(false);
					menuPopular.setSelected(false);
					menuLove.setSelected(true);
					menuAwesome.setSelected(false);
					menuWow.setSelected(false);
					menuFun.setSelected(false);
					menuFantastic.setSelected(false);
					break;
				case 3:
					menuRecent.setSelected(false);
					menuPopular.setSelected(false);
					menuLove.setSelected(false);
					menuAwesome.setSelected(true);
					menuWow.setSelected(false);
					menuFun.setSelected(false);
					menuFantastic.setSelected(false);
					break;
				case 4:
					menuRecent.setSelected(false);
					menuPopular.setSelected(false);
					menuLove.setSelected(false);
					menuAwesome.setSelected(false);
					menuWow.setSelected(true);
					menuFun.setSelected(false);
					menuFantastic.setSelected(false);
					break;	
				case 5:
					menuRecent.setSelected(false);
					menuPopular.setSelected(false);
					menuLove.setSelected(false);
					menuAwesome.setSelected(false);
					menuWow.setSelected(false);
					menuFun.setSelected(true);
					menuFantastic.setSelected(false);
					break;	
				case 6:
					menuRecent.setSelected(false);
					menuPopular.setSelected(false);
					menuLove.setSelected(false);
					menuAwesome.setSelected(false);
					menuWow.setSelected(false);
					menuFun.setSelected(false);
					menuFantastic.setSelected(true);
					break;		
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
			
				sortOptionMenu.setAnimationStyle(-1);
				sortOptionMenu.showAsDropDown(v, -30, 0);
			}
		});
		
		this.setSelection(0);
		
	}



	public interface ArtworkTabListener{
		public void onSelectSortOption(String sortOption);
	}
}
