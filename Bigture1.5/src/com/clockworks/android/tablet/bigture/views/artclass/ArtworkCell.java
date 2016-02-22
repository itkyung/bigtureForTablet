package com.clockworks.android.tablet.bigture.views.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.views.artwork.ArtworkThumbnail;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ArtworkCell extends RelativeLayout {
	View viewAdd;
	ArtworkThumbnail[] artworks;
	ArtworkThumbnail.Callback callback;
	
	static int ids[] = {R.id.cell1, R.id.cell2, R.id.cell3};
	
	public ArtworkCell(Context context, AttributeSet attrs) {
		super(context, attrs);
	}


	@Override
	protected void onFinishInflate(){
		artworks = new ArtworkThumbnail[3];
		
		viewAdd = findViewById(R.id.vwAdd);
		viewAdd.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onAddClick();
			}
		});

		for (int i = 0; i < ids.length; i++)
			artworks[i] = (ArtworkThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(ArtworkThumbnail.Callback callback, ArrayList<ArtworkEntity> artworkList, int position, boolean canWrite){
		this.callback = callback;
		
		artworks[0].setVisibility(View.INVISIBLE);
		artworks[1].setVisibility(View.INVISIBLE);
		artworks[2].setVisibility(View.INVISIBLE);
	
		
		if (position == 0 && artworkList.get(0).artworkId == null && canWrite){
			artworks[0].setVisibility(View.GONE);
			viewAdd.setVisibility(View.VISIBLE);
		}else{
			artworks[0].setVisibility(View.VISIBLE);
			viewAdd.setVisibility(View.GONE);
		}
		
		for (int i = 0, idx = 0; i < artworkList.size(); i++, idx++){
			if (i < artworkList.size()){
				ArtworkEntity entity = artworkList.get(i);
				
				if(entity.artworkId != null){
					artworks[idx].setVisibility(View.VISIBLE);
					artworks[idx].update(callback, entity,false);
				}
			}else{
				artworks[idx].setVisibility(View.INVISIBLE);
			}
		}
	}
}
