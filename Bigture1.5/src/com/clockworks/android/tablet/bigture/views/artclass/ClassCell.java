package com.clockworks.android.tablet.bigture.views.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;



import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class ClassCell extends RelativeLayout {
	ClassThumbnail.Callback callback;
	ClassThumbnail[] classes;
	View viewAdd;
	
	
	static int ids[] = {R.id.class1, R.id.class2, R.id.class3, R.id.class4};
	
	public ClassCell(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate(){
		classes = new ClassThumbnail[4];
		
		viewAdd = findViewById(R.id.vwAdd);
		viewAdd.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				callback.onAddArtClass();
			}
		});

		for (int i = 0; i < ids.length; i++)
			classes[i] = (ClassThumbnail)findViewById(ids[i]);
		
		super.onFinishInflate();
	}
	
	public void updateView(ClassThumbnail.Callback callback, ArrayList<ArtClassEntity> classList, int position, boolean isOwn){
		this.callback = callback;
		
		classes[0].setVisibility(View.INVISIBLE);
		classes[1].setVisibility(View.INVISIBLE);
		classes[2].setVisibility(View.INVISIBLE);
		classes[3].setVisibility(View.INVISIBLE);
		
		if (position == 0 && classList.get(0).index == null && isOwn){
			classes[0].setVisibility(View.GONE);
			viewAdd.setVisibility(View.VISIBLE);
		}else{
			classes[0].setVisibility(View.VISIBLE);
			viewAdd.setVisibility(View.GONE);
		}
		
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
