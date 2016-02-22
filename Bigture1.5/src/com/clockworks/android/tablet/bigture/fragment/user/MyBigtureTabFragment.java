package com.clockworks.android.tablet.bigture.fragment.user;

import java.util.ArrayList;

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

public class MyBigtureTabFragment extends HorizontalTabFragment {
	boolean isPro;
	boolean myPage;
	View artworksMenu;
	View storyMenu;
	View likeMenu;
	View cardMenu;
	TextView cardMenuTxt;
	MyBigtureTabListener listener;
	
	int selectedArtworkMenu1Idx = 0;
	int selectedArtworkMenu2Idx = 0;
	int selectedStoryMenuIdx = 0;
	int selectedCardMenuIdx = 0;
	
	String[] artworkMenuStr1 = {null,"CONTEST"};
	String[] artworkMenuStr2 = {"RECENT","POPULAR","LOVE","AWESOME","WOW","FUN","FANTASTIC"};
	String[] storyMenuStr = {null,"DOING","DONE"};
	String[] cardMenuStr = {"RECEIVED","SENDED"};
	
	PopupWindow artworksMenu1Popup;
	PopupWindow artworksMenu2Popup;
	PopupWindow storyMenuPopup;
	PopupWindow cardMenuPopup;
	
	Context context;
	
	public static MyBigtureTabFragment newInstance(boolean _isPro,boolean _myPage){
		MyBigtureTabFragment fragment = new MyBigtureTabFragment();
		
		Bundle args = new Bundle();
		args.putBoolean("isPro", _isPro);
		args.putBoolean("myPage", _myPage);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		if(args != null){
			this.isPro = args.getBoolean("isPro");
			this.myPage = args.getBoolean("myPage");
		}
		
	}



	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		listener = (MyBigtureTabListener)activity;
		context = activity;
	}



	@Override
	public void initView(View view) {
		super.initView(view);
		
		artworksMenu = view.findViewById(R.id.tabAction1);
		storyMenu = view.findViewById(R.id.tabAction2);
		likeMenu = view.findViewById(R.id.tabFindFriend);
		cardMenu = view.findViewById(R.id.tabCardMenu);
		
		final TextView artworkMenuTxt1 = (TextView)view.findViewById(R.id.tabSortLabel1);
		artworkMenuTxt1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// All 또는 Contest
				if(artworksMenu1Popup == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_artwork_list_1, null);
					artworksMenu1Popup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					artworksMenu1Popup.setOutsideTouchable(true);
					artworksMenu1Popup.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				artworksMenu1Popup.dismiss();
				View mView = artworksMenu1Popup.getContentView();
				
				Button menuAll = (Button)mView.findViewById(R.id.menuAll);
				
				menuAll.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu1Popup.dismiss();
						artworkMenuTxt1.setText("All");
						selectedArtworkMenu1Idx = 0;
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				
				
				Button menuContest = (Button)mView.findViewById(R.id.menuContest);
				menuContest.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu1Popup.dismiss();
						artworkMenuTxt1.setText("Contest");
						selectedArtworkMenu1Idx = 1;
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				if(selectedArtworkMenu1Idx == 0){
					menuAll.setSelected(true);
					menuContest.setSelected(false);
				}else{
					menuAll.setSelected(false);
					menuContest.setSelected(true);
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				artworksMenu1Popup.setAnimationStyle(-1);
				artworksMenu1Popup.showAsDropDown(v, -30, 0);
				
				
			}
		});
		
		final TextView artworkMenuTxt2 = (TextView)view.findViewById(R.id.tabSortLabel2);
		artworkMenuTxt2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 스티커 메뉴를 표현한다.
				if(artworksMenu2Popup == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_artwork_list_2, null);
					artworksMenu2Popup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					artworksMenu2Popup.setOutsideTouchable(true);
					artworksMenu2Popup.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				artworksMenu2Popup.dismiss();
				View mView = artworksMenu2Popup.getContentView();
				
				Button menuRecent = (Button)mView.findViewById(R.id.menuRecent);
				
				menuRecent.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 0;
						artworkMenuTxt2.setText("Recent");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				
				
				Button menuPopular = (Button)mView.findViewById(R.id.menuPopular);
				menuPopular.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 1;
						artworkMenuTxt2.setText("Popular");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				
				Button menuLove = (Button)mView.findViewById(R.id.menuLove);
				menuLove.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 2;
						artworkMenuTxt2.setText("Love");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				Button menuAwesome = (Button)mView.findViewById(R.id.menuAwesome);
				menuAwesome.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 3;
						artworkMenuTxt2.setText("Awesome");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				Button menuWow = (Button)mView.findViewById(R.id.menuWow);
				menuWow.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 4;
						artworkMenuTxt2.setText("Wow");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				Button menuFun = (Button)mView.findViewById(R.id.menuFun);
				menuFun.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 5;
						artworkMenuTxt2.setText("Fun");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				Button menuFantastic = (Button)mView.findViewById(R.id.menuFantastic);
				menuFantastic.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						artworksMenu2Popup.dismiss();
						selectedArtworkMenu2Idx = 6;
						artworkMenuTxt2.setText("Fantastic");
						listener.selectArtworksPopupMenu(artworkMenuStr1[selectedArtworkMenu1Idx], artworkMenuStr2[selectedArtworkMenu2Idx]);
					}
				});
				
				
				switch(selectedArtworkMenu2Idx){
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
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				artworksMenu2Popup.setAnimationStyle(-1);
				artworksMenu2Popup.showAsDropDown(v, -30, 0);
			}
		});
		
		final TextView storyMenuTxt = (TextView)view.findViewById(R.id.tabSortLabel3);
		storyMenuTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// All, Doing, Done 
				if(storyMenuPopup == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_story_list, null);
					storyMenuPopup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					storyMenuPopup.setOutsideTouchable(true);
					storyMenuPopup.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				storyMenuPopup.dismiss();
				View mView = storyMenuPopup.getContentView();
				
				Button menuAll = (Button)mView.findViewById(R.id.menuAll);
				
				menuAll.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						storyMenuPopup.dismiss();
						selectedStoryMenuIdx = 0;
						storyMenuTxt.setText("All");
						listener.selectStoryPopupMenu(null);
					}
				});
				
				
				
				Button menuDoing = (Button)mView.findViewById(R.id.menuDoing);
				menuDoing.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						storyMenuPopup.dismiss();
						selectedStoryMenuIdx = 1;
						storyMenuTxt.setText("Doing");
						listener.selectStoryPopupMenu(storyMenuStr[selectedStoryMenuIdx]);
					}
				});
				
				
				Button menuDone = (Button)mView.findViewById(R.id.menuDone);
				menuDone.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						storyMenuPopup.dismiss();
						selectedStoryMenuIdx = 2;
						storyMenuTxt.setText("Done");
						listener.selectStoryPopupMenu(storyMenuStr[selectedStoryMenuIdx]);
					}
				});
				
				
				switch(selectedStoryMenuIdx){
				case 0:
					menuAll.setSelected(true);
					menuDoing.setSelected(false);
					menuDone.setSelected(false);
					
					
					break;
				case 1:
					menuAll.setSelected(false);
					menuDoing.setSelected(true);
					menuDone.setSelected(false);
				
					break;
				case 2:
					menuAll.setSelected(false);
					menuDoing.setSelected(false);
					menuDone.setSelected(true);
				
					break;
				
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				storyMenuPopup.setAnimationStyle(-1);
				storyMenuPopup.showAsDropDown(v, -30, 0);
				
			}
		});
		
		cardMenuTxt = (TextView)view.findViewById(R.id.tabCardLabel);
		cardMenuTxt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Received, Sended
				
				if(cardMenuPopup == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_card_list, null);
					cardMenuPopup = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					cardMenuPopup.setOutsideTouchable(true);
					cardMenuPopup.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				cardMenuPopup.dismiss();
				View mView = cardMenuPopup.getContentView();
				
				Button menuReceived = (Button)mView.findViewById(R.id.menuReceived);
				
				menuReceived.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cardMenuPopup.dismiss();
						selectedCardMenuIdx = 0;
						cardMenuTxt.setText("Received");
						listener.selectCardPopupMenu(cardMenuStr[selectedCardMenuIdx]);
					}
				});
				
				
				
				Button menuSended = (Button)mView.findViewById(R.id.menuSended);
				menuSended.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						cardMenuPopup.dismiss();
						selectedCardMenuIdx = 1;
						cardMenuTxt.setText("Sended");
						listener.selectCardPopupMenu(cardMenuStr[selectedCardMenuIdx]);
					}
				});
				
				
				
				switch(selectedCardMenuIdx){
				case 0:
					menuReceived.setSelected(true);
					menuSended.setSelected(false);
					
					
					break;
				case 1:
					menuReceived.setSelected(false);
					menuSended.setSelected(true);
				
					break;
			
				}
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				cardMenuPopup.setAnimationStyle(-1);
				cardMenuPopup.showAsDropDown(v, -30, 0);
				
				
				
			}
		});
		
		
		Button btnFindFriend = (Button)view.findViewById(R.id.btnFindFriend);
		btnFindFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findFriendClick();
			}
		});
		
		
		
		
		if(isPro){
			this.setMenuCount(4);
			
			this.setItemText(0, getResources().getString(R.string.mybigture_tab_carrer));
			this.setItemText(1, getResources().getString(R.string.mybigture_tab_artworks));
			this.setItemText(2, getResources().getString(R.string.mybigture_tab_story));
			//this.setItemText(3, "Class");
			this.setItemText(3, getResources().getString(R.string.mybigture_tab_like));
			//this.setItemText(4, getResources().getString(R.string.mybigture_tab_postbox));
			hideAllMenu();
		}else{
			this.setMenuCount(3);

			this.setItemText(0, getResources().getString(R.string.mybigture_tab_artworks));
			this.setItemText(1, getResources().getString(R.string.mybigture_tab_story));
			//this.setItemText(2, "Class");
			this.setItemText(2, getResources().getString(R.string.mybigture_tab_like));
			//this.setItemText(3, getResources().getString(R.string.mybigture_tab_postbox));
			showArtworksMenu();
		}
		
		this.setSelection(0);
	}
	
	public void changeCardMenuText(String txt,int idx){
		cardMenuTxt.setText(txt);
		selectedCardMenuIdx = idx;
	}
	
	public void hideAllMenu(){
		artworksMenu.setVisibility(View.GONE);
		storyMenu.setVisibility(View.GONE);
		likeMenu.setVisibility(View.GONE);
		cardMenu.setVisibility(View.GONE);
	}

	public void showArtworksMenu(){
		artworksMenu.setVisibility(View.VISIBLE);
		storyMenu.setVisibility(View.GONE);
		likeMenu.setVisibility(View.GONE);
		cardMenu.setVisibility(View.GONE);
	}
	
	public void showStoryMenu(){
		artworksMenu.setVisibility(View.GONE);
		storyMenu.setVisibility(View.VISIBLE);
		likeMenu.setVisibility(View.GONE);
		cardMenu.setVisibility(View.GONE);
	}
	
	public void showLikeMenu(){
		artworksMenu.setVisibility(View.GONE);
		storyMenu.setVisibility(View.GONE);
		likeMenu.setVisibility(View.VISIBLE);
		cardMenu.setVisibility(View.GONE);
	}
	
	public void showCardMenu(){
		
		artworksMenu.setVisibility(View.GONE);
		storyMenu.setVisibility(View.GONE);
		likeMenu.setVisibility(View.GONE);
		if(myPage)
			cardMenu.setVisibility(View.VISIBLE);
		else
			cardMenu.setVisibility(View.GONE);
	}
	
	public void findFriendClick(){
		this.listener.openFindFriendPopup();
	}
	
	public interface MyBigtureTabListener{
		public void openFindFriendPopup();
		public void selectArtworksPopupMenu(String type,String sortOption);
		public void selectStoryPopupMenu(String status);
		public void selectCardPopupMenu(String listType);
	}
}

