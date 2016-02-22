package com.clockworks.android.tablet.bigture.views.expert;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;




import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ExpertListCell extends LinearLayout {
	private ExpertThumbnail[] thumbs; 
	
	
	public ExpertListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	protected void onFinishInflate(){
		thumbs = new ExpertThumbnail[7];
		
		thumbs[0] = (ExpertThumbnail)findViewById(R.id.cell1);
		thumbs[1] = (ExpertThumbnail)findViewById(R.id.cell2);
		thumbs[2] = (ExpertThumbnail)findViewById(R.id.cell3);
		thumbs[3] = (ExpertThumbnail)findViewById(R.id.cell4);
		thumbs[4] = (ExpertThumbnail)findViewById(R.id.cell5);
		thumbs[5] = (ExpertThumbnail)findViewById(R.id.cell6);
		thumbs[6] = (ExpertThumbnail)findViewById(R.id.cell7);
		
		
		super.onFinishInflate();
	}
	
	public void updateView(ExpertThumbnail.Callback callback, ArrayList<SimpleUserEntity> expertList){
		for (int i = 0; i < thumbs.length; i++){
			if (i >= expertList.size()){
				thumbs[i].setVisibility(View.INVISIBLE);				
				continue;
			}
			
			thumbs[i].setVisibility(View.VISIBLE);
			thumbs[i].update(callback, expertList.get(i));
		}
	}
	
}
