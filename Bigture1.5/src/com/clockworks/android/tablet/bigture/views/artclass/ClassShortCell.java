package com.clockworks.android.tablet.bigture.views.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ClassShortCell extends RelativeLayout {
	ClassThumbnail.Callback callback;
	ClassThumbnail[] classes;
	static int ids[] = {R.id.class1, R.id.class2, R.id.class3};
	
	
	public ClassShortCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate(){
		classes = new ClassThumbnail[3];
		
		for (int i = 0; i < ids.length; i++)
			classes[i] = (ClassThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(ClassThumbnail.Callback callback, ArrayList<ArtClassEntity> classList, int position){
		this.callback = callback;
		
		classes[0].setVisibility(View.INVISIBLE);
		classes[1].setVisibility(View.INVISIBLE);
		classes[2].setVisibility(View.INVISIBLE);
		
		
		for (int i = 0, idx = 0; i < classList.size(); i++, idx++){
			if (i < classList.size()){
				ArtClassEntity entity = classList.get(i);
				
				if(entity.index != null){
					classes[idx].setVisibility(View.VISIBLE);
					classes[idx].updateCell(callback, entity);
				}
			}else{
				classes[idx].setVisibility(View.INVISIBLE);
			}
		}
	}
	
}
