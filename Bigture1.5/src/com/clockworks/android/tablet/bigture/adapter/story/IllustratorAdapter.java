package com.clockworks.android.tablet.bigture.adapter.story;

import java.util.ArrayList;
import java.util.List;




import com.clockworks.android.tablet.bigture.StoryPageChanger;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.views.story.page.IllustratorCell;
import com.clockworks.android.tablet.bigture.views.story.page.IllustratorMyCell;
import com.clockworks.android.tablet.bigture.views.story.page.StoryArtworkSectionItem;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class IllustratorAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<StoryPageEntity> pages;
	private boolean myBigture;
	private ArrayList<StoryArtworkSectionItem> listItem;
	private ArrayList<StoryArtworkEntity> myArtworks;
	private StoryPageChanger changer;
	
	public IllustratorAdapter(Context context,
			ArrayList<StoryPageEntity> pages, boolean myBigture,StoryPageChanger changer) {
		super();
		this.context = context;
		this.pages = pages;
		this.myBigture = myBigture;
		this.changer = changer;
		if(myBigture){
			myArtworks = new ArrayList<StoryArtworkEntity>();
			filterMyArtworks();
		}else{
			listItem = new ArrayList<StoryArtworkSectionItem>();
			makePageSection();
		}
	}

	private void filterMyArtworks(){
		for(StoryPageEntity page : pages){
			if(page.artworkList == null) continue;
			for(StoryArtworkEntity entity : page.artworkList){
				entity.pageNo = page.pageNo;
				if(entity.userId.equals(AccountManager.getUserIdx())){
					myArtworks.add(entity);
				}
			}
		}
	}
	
	/*
	 * 한 row에 3개씩 만든다.
	 */
	private void makePageSection(){
		
		StoryArtworkSectionItem item = null;
		boolean viewPage = false;
		for(int i=0; i < pages.size(); i++){
			viewPage = true;
			StoryPageEntity page = pages.get(i);
			if(page.artworkList == null) continue;
			for(int j=0; j < page.artworkList.size(); j++){
				//3개마다 하나의 item을 만든다.
				if(j == 0 || j % 3 == 0){
					item = new StoryArtworkSectionItem();
					item.setViewDivider(false);
					item.setViewPage(viewPage);
					item.setArtworks(new ArrayList<StoryArtworkEntity>());
					this.listItem.add(item);
				}
				
				item.setPageId(page.pageId);
				item.setPageNo(page.pageNo);
				StoryArtworkEntity entity = page.artworkList.get(j);
				item.getArtworks().add(entity);
				viewPage = false;
			}
			//listItem의 마지막 요소에 업데이트.
			StoryArtworkSectionItem lastItem = listItem.get(listItem.size()-1);
			lastItem.setViewDivider(true);
			
		}
	}
	
	@Override
	public int getCount() {
		return myBigture ? myArtworks.size() : listItem.size();
	}

	@Override
	public Object getItem(int position) {
		if(myBigture){
			return myArtworks.get(position);
		}else{
			return listItem.get(position);
		}
		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(myBigture){
			IllustratorMyCell cell = (IllustratorMyCell)convertView;
			if (cell == null){
				cell = new IllustratorMyCell(context);
			}
			StoryArtworkEntity entity = (StoryArtworkEntity)getItem(position);
			cell.updateCell(entity, entity.pageNo,position);
			
			return cell;
		}else{
			IllustratorCell cell = (IllustratorCell)convertView;
			if (cell == null){
				cell = new IllustratorCell(context);
			}
			
			cell.updateCell((StoryArtworkSectionItem)getItem(position), position,this.changer);
			
			return cell;
		}
		
	}

}
