package com.clockworks.android.tablet.bigture.views.postcard;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.views.story.listcell.BookThumbnail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NormalCell extends RelativeLayout {
	CardThumbnail.Callback callback;
	CardThumbnail[] cards;
	View viewAdd;
	PopupWindow sendMenu;
	Context context;
	static int ids[] = {R.id.card1, R.id.card2, R.id.card3};
	
	public NormalCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	
	
	@Override
	protected void onFinishInflate(){
		cards = new CardThumbnail[3];
		
		viewAdd = findViewById(R.id.vwAdd);
		
		ImageView vmAddImg = (ImageView)findViewById(R.id.vmAddImg);
		
		vmAddImg.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(sendMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_select_card_img, null);
					sendMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					sendMenu.setOutsideTouchable(true);
					sendMenu.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				sendMenu.dismiss();
				View mView = sendMenu.getContentView();
				
				ImageView arrow = (ImageView)mView.findViewById(R.id.bottomArrow);
				arrow.setVisibility(View.GONE);
				
				Button btnDraw = (Button)mView.findViewById(R.id.btnDraw);
				btnDraw.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sendMenu.dismiss();
						callback.onDrawSketchbook();
						
					}
				});
				
				Button btnSelectArtwork = (Button)mView.findViewById(R.id.btnSelectArtwork);
				btnSelectArtwork.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						sendMenu.dismiss();
						callback.onSelectArtwork();
						
					}
				});
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				sendMenu.setAnimationStyle(-1);
				sendMenu.showAsDropDown(v, -30, -30);
				
				
			}
		});

		for (int i = 0; i < ids.length; i++)
			cards[i] = (CardThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(CardThumbnail.Callback callback, ArrayList<PostCardEntity> cardList, int position, boolean myPage, boolean receivedCell){
		this.callback = callback;
		
		cards[0].setVisibility(View.INVISIBLE);
		cards[1].setVisibility(View.INVISIBLE);
		cards[2].setVisibility(View.INVISIBLE);
		
		if (position == 0 && cardList.get(0).id == null && myPage){
			cards[0].setVisibility(View.GONE);
			viewAdd.setVisibility(View.VISIBLE);
		}else{
			cards[0].setVisibility(View.VISIBLE);
			viewAdd.setVisibility(View.GONE);
		}
		
		for (int i = 0, idx = 0; i < cardList.size(); i++, idx++){
			if (i < cardList.size()){
				PostCardEntity entity = cardList.get(i);
				
				if(entity.id != null){
					cards[idx].setVisibility(View.VISIBLE);
					cards[idx].updateCell(callback, entity,receivedCell);
				}
			}else{
				cards[idx].setVisibility(View.INVISIBLE);
			}
		}
	}
	
}
