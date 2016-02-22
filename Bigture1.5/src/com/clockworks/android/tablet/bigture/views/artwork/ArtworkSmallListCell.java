package com.clockworks.android.tablet.bigture.views.artwork;

import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ArtworkSmallListCell extends LinearLayout {
	ArtworkSimpleThumbnail[] thumbs;

	public ArtworkSmallListCell(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		thumbs = new ArtworkSimpleThumbnail[3];
		
		thumbs[0] = (ArtworkSimpleThumbnail)findViewById(R.id.cell1);
		thumbs[1] = (ArtworkSimpleThumbnail)findViewById(R.id.cell2);
		thumbs[2] = (ArtworkSimpleThumbnail)findViewById(R.id.cell3);
		
		super.onFinishInflate();
	}

	public void updateView(ArtworkSimpleThumbnail.Callback callback, List<ArtworkEntity> artworkList){
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
