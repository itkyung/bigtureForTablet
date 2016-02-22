package com.clockworks.android.tablet.bigture.views.artwork;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ArtworkListCell extends LinearLayout{
	ArtworkThumbnail[] thumbs;

	public ArtworkListCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		thumbs = new ArtworkThumbnail[4];
		
		thumbs[0] = (ArtworkThumbnail)findViewById(R.id.cell1);
		thumbs[1] = (ArtworkThumbnail)findViewById(R.id.cell2);
		thumbs[2] = (ArtworkThumbnail)findViewById(R.id.cell3);
		thumbs[3] = (ArtworkThumbnail)findViewById(R.id.cell4);

		super.onFinishInflate();
	}

	public void updateView(ArtworkThumbnail.Callback callback, List<ArtworkEntity> artworkList, boolean myPage){
		for (int i = 0; i < thumbs.length; i++){
			if (i >= artworkList.size()){
				thumbs[i].setVisibility(View.INVISIBLE);				
				continue;
			}
			
			thumbs[i].setVisibility(View.VISIBLE);
			thumbs[i].update(callback, artworkList.get(i), myPage);
		}
	}
}
