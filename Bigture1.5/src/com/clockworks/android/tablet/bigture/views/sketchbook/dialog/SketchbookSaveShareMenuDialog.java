package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.R.id;
import com.clockworks.android.tablet.bigture.R.layout;
import com.clockworks.android.tablet.bigture.views.story.page.ShareStoryButton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public class SketchbookSaveShareMenuDialog extends Dialog  implements View.OnClickListener{
	public final static int MENU_SAVE_TEMP=0;
	public final static int MENU_ALBUM=1;
	public final static int MENU_SHARE_ARTWROK=2;
	public final static int MENU_CARD=3;
	public final static int MENU_SHARE_OTHER=4;
	public final static int MENU_PRINT=5;
	
	private Context context;
	private int type;
	private SelectMenuListener selectMenuListener;
	
	public SketchbookSaveShareMenuDialog(Context context,int type){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.setCanceledOnTouchOutside(false);
		
		this.context = context;
		this.type = type;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
//		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int screenWidth = (int)metrics.widthPixels;
//        
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_sketchbook_menu);
		
//		getWindow().setLayout(screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//		getWindow().setGravity(Gravity.CENTER);
		
		
		View saveMenu = findViewById(R.id.saveMenu);
		View shareMenu = findViewById(R.id.shareMenu);
		
		final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 376, context.getResources().getDisplayMetrics());
		
		
		if(type == 1){
			//save
			final int leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
			final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
			
			LayoutParams lp = getWindow().getAttributes();
			//lp.y = Gravity.TOP + height;
			//lp.x = Gravity.LEFT + leftMargin;
		//	lp.width = width;
		//	getWindow().setAttributes(lp);
			
			
			saveMenu.setVisibility(View.VISIBLE);
			shareMenu.setVisibility(View.GONE);
			
			findViewById(R.id.btnSaveTemp).setOnClickListener(this);
			findViewById(R.id.btnSaveAlbum).setOnClickListener(this);
			
		}else{
			final int leftMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
			final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
			LayoutParams lp = getWindow().getAttributes();
		//	lp.y = Gravity.TOP + height;
		//	lp.x = Gravity.LEFT + leftMargin;
		//	lp.width = width;
		//	getWindow().setAttributes(lp);
			
			
			//share
			saveMenu.setVisibility(View.GONE);
			shareMenu.setVisibility(View.VISIBLE);
			
			findViewById(R.id.btnShareArtwork).setOnClickListener(this);
			findViewById(R.id.btnSendCard).setOnClickListener(this);
			findViewById(R.id.btnShareOther).setOnClickListener(this);
			findViewById(R.id.btnPrint).setOnClickListener(this);
		}
		
	}

	public void setSelectMenuListener(SelectMenuListener selectMenuListener) {
		this.selectMenuListener = selectMenuListener;
	}

	@Override
	public void onClick(View v) {
		int menu = 0;
		switch (v.getId()) {
		case R.id.btnSaveTemp:
			menu = MENU_SAVE_TEMP;
			break;
		case R.id.btnSaveAlbum:
			menu = MENU_ALBUM;
			break;
		case R.id.btnShareArtwork:
			menu = MENU_SHARE_ARTWROK;
			break;
		case R.id.btnSendCard:
			menu = MENU_CARD;
			break;
		case R.id.btnShareOther:
			menu = MENU_SHARE_OTHER;
			break;			
		case R.id.btnPrint:
			menu = MENU_PRINT;
			break;
		default:
			break;
		}
		finishWithAction(menu);
	}

	public interface SelectMenuListener{
		public void selectMenu(int action);
	}
	
	private void finishWithAction(int action){
		selectMenuListener.selectMenu(action);
	}
	
}
