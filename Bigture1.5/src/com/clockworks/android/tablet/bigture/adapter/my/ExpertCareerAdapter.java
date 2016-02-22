package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class ExpertCareerAdapter extends BaseAdapter
{
	Context context;
	List<CareerEntity> careerList;
	boolean myCareer;
	CareerMenuListener listener;
	PopupWindow editMenu;

	public ExpertCareerAdapter(Context context, List<CareerEntity> careerList, boolean myCareer,CareerMenuListener l){
		this.context = context;
		this.careerList = careerList;
		this.myCareer = myCareer;
		this.listener = l;
	}

	@Override
	public int getCount()
	{
		return careerList.size();
	}

	@Override
	public Object getItem(int position){	
		return careerList.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}

	@Override
	public int getItemViewType(int position){
		return 0;
	}

	@Override
	public int getViewTypeCount(){
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		final CareerEntity careerEntity = (CareerEntity)getItem(position);
		
		LinearLayout cell = (LinearLayout)convertView;
		ViewHolder viewHolder = null;
		
		if (cell == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			cell = (LinearLayout)inflater.inflate(R.layout.cell_expert_career, null);
			viewHolder = new ViewHolder();
			viewHolder.init(cell);
			cell.setTag(viewHolder);

			convertView = cell;
		}else{
			viewHolder = (ViewHolder)cell.getTag();
		}
		
		viewHolder.showData(careerEntity);
		
		if(!myCareer){
			viewHolder.editBtn.setVisibility(View.GONE);
		}
		
		viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//PopupWindow를 이용해서 뷰를 만든다.
				if(editMenu == null){
					LayoutInflater inflater = LayoutInflater.from(context);
					View menuView = inflater.inflate(R.layout.menu_edit_class_up, null);
					editMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
					editMenu.setOutsideTouchable(true);
					editMenu.setBackgroundDrawable(new BitmapDrawable()) ;
				}
				
				editMenu.dismiss();
				View mView = editMenu.getContentView();
				
				Button editText = (Button)mView.findViewById(R.id.menuEdit);
				editText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						listener.clickEdit(careerEntity);
					}
				});
				
				Button deleteText = (Button)mView.findViewById(R.id.menuDelete);
				deleteText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Delete career");
						builder.setMessage("This career will be deleted, Continue?");
						builder.setNegativeButton("No", null);
						builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								listener.clickDelete(careerEntity);
							}
							
						});
						builder.create().show();
						
						
						
						
					}
				});
				
				mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
				
				//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
				//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

				editMenu.setAnimationStyle(-1);
				editMenu.showAsDropDown(v, -100, 0);
				
			}
		} );
		
		return convertView;
	}

	static class ViewHolder{
		public TextView periodLabel;
		public TextView contentLabel;
		public Button editBtn;
		
		public void init(View view){
			periodLabel  = (TextView)view.findViewById(R.id.periodLabel);
			contentLabel  = (TextView)view.findViewById(R.id.contentLabel);
			editBtn = (Button)view.findViewById(R.id.btnCareerMenu);
			
			
		}
		
		public void showData(CareerEntity entity){
			String startTime = entity.startTime.substring(0, 10);
			String endTime = (entity.endTime != null ? entity.endTime.substring(0, 10) : "Present");
			
			if (startTime.equals(endTime))
				endTime = "Present";
			
			periodLabel.setText(startTime + " to " + endTime);
			contentLabel.setText(entity.career);
			
			
		}
	}
	
	public interface CareerMenuListener{
		public void clickEdit(CareerEntity entity);
		public void clickDelete(CareerEntity entity);
	}
}
