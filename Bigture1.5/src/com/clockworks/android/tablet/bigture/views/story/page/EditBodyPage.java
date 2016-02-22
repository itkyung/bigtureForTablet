package com.clockworks.android.tablet.bigture.views.story.page;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditBodyPage extends BodyPage {
	private EditText descLabel;
	private Button addPageBtn;
	private Button rightArrowBtn;
	private Button leftArrowBtn;
	private EditBodyPageListener listener;
	
	public EditBodyPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.descLabel = (EditText)findViewById(R.id.descLabel);
		descLabel.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					syncData();
					listener.onDescChanged();
				}
			}
		});
		
		this.addPageBtn = (Button)findViewById(R.id.addPageBtn);
		addPageBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClickAddPage();
			}
		});
		
		this.leftArrowBtn = (Button)findViewById(R.id.leftArrowBtn);
		leftArrowBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClickPrevPage();
			}
		});
		
		this.rightArrowBtn = (Button)findViewById(R.id.rightArrowBtn);
		rightArrowBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClickNextPage();
			}
		});
	}
	
	
	
	public void setListener(EditBodyPageListener l){
		this.listener = l;
	}
	
	public void initView(Context context,StoryEntity storyEntity,StoryPageEntity pageEntity){
		super.initView(context, storyEntity, pageEntity);
		
		if(pageEntity != null){
			updateArrow(storyEntity, pageEntity);
			descLabel.setText(pageEntity.description);
		}
	}
	
	public void updateArrow(StoryEntity storyEntity,StoryPageEntity pageEntity){
		super.updateArrow(storyEntity, pageEntity);
		
		if(pageEntity != null){
			if(storyEntity.pageCount > pageEntity.pageNo){
				rightArrowBtn.setVisibility(View.VISIBLE);
			}else{
				rightArrowBtn.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 사용자가 입력한 description을 동기화한다.
	 */
	public void syncData(){
		this.pageEntity.description = descLabel.getText().toString();
	}
	
	public interface EditBodyPageListener{
		public void onClickAddPage();
		public void onClickNextPage();
		public void onClickPrevPage();
		public void onDescChanged();
	}
	
}
