package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.views.common.PostCardSectionItem;
import com.clockworks.android.tablet.bigture.views.postcard.CardThumbnail;
import com.clockworks.android.tablet.bigture.views.postcard.NormalCell;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PostCardAdapter extends BaseAdapter {
	private Context context;
	private boolean myPage;
	private ArrayList<PostCardSectionItem> listItem;
	private CardThumbnail.Callback callback;
	private boolean receivedCell;
	
	public PostCardAdapter(Context context, boolean myPage,CardThumbnail.Callback callback,boolean receivedCell) {
		super();
		this.context = context;
		this.myPage = myPage;
		this.callback = callback;
		this.receivedCell = receivedCell;
		this.listItem = new ArrayList<PostCardSectionItem>();
	}
	
	
	
	public boolean isReceivedCell() {
		return receivedCell;
	}



	public void setReceivedCell(boolean receivedCell) {
		this.receivedCell = receivedCell;
	}



	public void setCardItem(ArrayList<PostCardEntity> cards){
		this.listItem.clear();
		
		ArrayList<PostCardEntity> realCards = new ArrayList<PostCardEntity>();
		
		if(myPage){
			PostCardEntity dummy = new PostCardEntity();
			dummy.id = null;
			realCards.add(dummy);
		}
		
		realCards.addAll(cards);
		PostCardSectionItem item = null;
		for(int i=0; i < realCards.size(); i++){
			//3개마다 하나의 item을 만든다.
			if(i == 0 || i % 3 == 0){
				item = new PostCardSectionItem();
				item.setCards(new ArrayList<PostCardEntity>());
				this.listItem.add(item);
			}
			PostCardEntity entity = realCards.get(i);
			item.getCards().add(entity);
		}
	}



	@Override
	public int getCount() {
		return this.listItem.size();
	}

	@Override
	public Object getItem(int position) {
		PostCardSectionItem item = this.listItem.get(position);
		return item;
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NormalCell layout = (NormalCell)convertView;
		
		if (layout == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			layout = (NormalCell)inflater.inflate(R.layout.cell_postcard_list, null);
		}
		PostCardSectionItem item = (PostCardSectionItem)getItem(position);
		layout.updateView(callback, item.getCards(), position, myPage, receivedCell);
		return layout;
		
	}

}
