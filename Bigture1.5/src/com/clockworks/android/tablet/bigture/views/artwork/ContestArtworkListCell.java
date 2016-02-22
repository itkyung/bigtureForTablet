package com.clockworks.android.tablet.bigture.views.artwork;

import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ContestArtworkListCell extends LinearLayout {
	ContestArtworkThumbnail[] thumbs;

	public ContestArtworkListCell(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		thumbs = new ContestArtworkThumbnail[3];
		
		thumbs[0] = (ContestArtworkThumbnail)findViewById(R.id.cell1);
		thumbs[1] = (ContestArtworkThumbnail)findViewById(R.id.cell2);
		thumbs[2] = (ContestArtworkThumbnail)findViewById(R.id.cell3);
		
		super.onFinishInflate();
	}

	public void updateView(ContestArtworkThumbnail.Callback callback, List<ArtworkEntity> artworkList){
		for (int i = 0; i < thumbs.length; i++){
			if (i >= artworkList.size()){
				thumbs[i].setVisibility(View.INVISIBLE);				
				continue;
			}
			
			thumbs[i].setVisibility(View.VISIBLE);
			thumbs[i].update(callback, artworkList.get(i));
		}
	}
}
