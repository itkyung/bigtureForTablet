package com.clockworks.android.tablet.bigture.adapter.artwork;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkListCell;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkSectionItem;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail.Callback;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ArtworkAdapter extends BaseAdapter {
	private Context context;
	private ArtworkThumbnail.Callback callback;
	private ArrayList<ArtworkSectionItem> listItem;
	private ArrayList<ArtworkEntity> artworkList;
	private int currentPage;
	private int totalPage;
	
	public ArtworkAdapter(Context context, Callback callback) {
		super();
		this.context = context;
		this.callback = callback;
		this.listItem = new ArrayList<ArtworkSectionItem>();
	}

	public void setArtworkList(ArrayList<ArtworkEntity> artworks,int totalPage,int currentPage){
		this.listItem.clear();
		this.currentPage = currentPage;
		this.totalPage = totalPage;
		if(artworks != null){
			this.artworkList = artworks;
			convertGroupItems(artworks);
		}
	}
	
	private void convertGroupItems(ArrayList<ArtworkEntity> artworkList){
		
		ArtworkSectionItem item = null;
		for(int i=0; i < artworkList.size(); i++){
			//4개마다 하나의 item을 만든다.
			if(i == 0 || i % 4 == 0){
				item = new ArtworkSectionItem();
				item.setArtworks(new ArrayList<ArtworkEntity>());
				item.setFooterView(false);
				this.listItem.add(item);
			}
			ArtworkEntity entity = artworkList.get(i);
			item.getArtworks().add(entity);
		}
		//page navigation이 필요한갯수이면.
		//맨 마지막에 footerView를 추가한다.
		if(totalPage > 1){
			ArtworkSectionItem footer = new ArtworkSectionItem();
			footer.setFooterView(true);
			this.listItem.add(footer);
		}
		
	}
	
	public ArrayList<ArtworkEntity> getArtworkList() {
		return artworkList;
	}

	@Override
	public int getCount() {
		return this.listItem.size();
	}

	@Override
	public Object getItem(int position) {
		
		return this.listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount(){
//		if (totalPage > 1)
//			return 2;
//		else
//			return 1;
		return 2;
	}

	@Override
	public int getItemViewType(int position){
		ArtworkSectionItem item = this.listItem.get(position);
		if(item.isFooterView()){
			return 1;
		}else{
			return 0;
		}
	}

	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ArtworkSectionItem item = this.listItem.get(position);
		
		if(getItemViewType(position) == 0){
			ArtworkListCell layout = (ArtworkListCell)convertView;
			
			if (layout == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				layout = (ArtworkListCell)inflater.inflate(R.layout.cell_artwork_list, null);
			}
			
			layout.updateView(callback, item.getArtworks(), false);
			return layout;
		}else{
			//Footer임.
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_artwork_list_pager, null);
				
				Button btnPrev = (Button)convertView.findViewById(R.id.btnPrev);
				Button btnNext = (Button)convertView.findViewById(R.id.btnNext);
				
				btnPrev.setOnClickListener(new View.OnClickListener(){	
					@Override
					public void onClick(View v)
					{
						callback.onPrevPage(currentPage);
					}
				});

				btnNext.setOnClickListener(new View.OnClickListener(){	
					@Override
					public void onClick(View v)
					{
						callback.onNextPage(currentPage);
					}
				});
			}

			Button btnPrev = (Button)convertView.findViewById(R.id.btnPrev);
			Button btnNext = (Button)convertView.findViewById(R.id.btnNext);
			
			if (currentPage == 1)
				btnPrev.setVisibility(View.INVISIBLE);
			else
				btnPrev.setVisibility(View.VISIBLE);
			
			if (currentPage == totalPage)
				btnNext.setVisibility(View.INVISIBLE);
			else
				btnNext.setVisibility(View.VISIBLE);
			
			return convertView;
		}
		
	}

}
