package com.clockworks.android.tablet.bigture.adapter.my;

import java.util.List;


import com.clockworks.android.tablet.bigture.NewsImageViewerActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CareerEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExpertNewsAdapter extends BaseAdapter {

	Context context;
	PopupWindow editMenu;
	List<NewsEntity> newsList;
	NewsMenuListener listener;
	String myUserIndex;
	boolean myNews;

	public ExpertNewsAdapter(Context context, List<NewsEntity> newsList, boolean myNews, NewsMenuListener listener){
		this.context = context;
		this.newsList = newsList;
		this.listener = listener;
		this.myNews   = myNews;
		
		//myUserIndex = AccountManager.getInstance().getUserIndex();
	}

	@Override
	public int getCount()
	{
		return (newsList == null ? 0 : newsList.size());
	}

	@Override
	public Object getItem(int position)
	{
		if (newsList == null)
			return null;
		
		return newsList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public int getItemViewType(int position)
	{
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){	
		final NewsEntity newsEntity = (NewsEntity)getItem(position);
		
		ViewHolder viewHolder = null;
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = (LinearLayout)inflater.inflate(R.layout.cell_expert_news, null);
			viewHolder = new ViewHolder();
			viewHolder.init(convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}

		viewHolder.showData(newsEntity);
		
		if(!myNews){
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
						listener.clickEdit(newsEntity);
					}
				});
				
				Button deleteText = (Button)mView.findViewById(R.id.menuDelete);
				deleteText.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						editMenu.dismiss();
						
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Delete news");
						builder.setMessage("This news will be deleted, Continue?");
						builder.setNegativeButton("No", null);
						builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								listener.clickDelete(newsEntity);
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
			
		});
		
		String imageURL = newsEntity.getThumbnailURL();
		
		if (imageURL != null){
			viewHolder.newsImage.setVisibility(View.VISIBLE);
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inSampleSize = 1;
	
			BitmapDownloader.getInstance().displayImage(imageURL, options, viewHolder.newsImage, null);
		}else{
			viewHolder.newsImage.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	class ViewHolder{
		public String newsIdx;
		public TextView titleLabel;
		public TextView contentLabel;
		public ImageView newsImage;
		
		public TextView countLabel;
		public RelativeLayout imageHolder;
		public Button editBtn;
		
		public void init(View view)
		{
			titleLabel  = (TextView)view.findViewById(R.id.titleLabel);
			contentLabel  = (TextView)view.findViewById(R.id.contentLabel);
			newsImage  = (ImageView)view.findViewById(R.id.imageNews);
			
			countLabel = (TextView)view.findViewById(R.id.countLabel);
			imageHolder = (RelativeLayout)view.findViewById(R.id.imageHolder);
			editBtn = (Button)view.findViewById(R.id.btnNewsMenu);
			
			newsImage.setOnClickListener(new View.OnClickListener()
			{				
				@Override
				public void onClick(View v)
				{
					Activity activity = (Activity)context;
					Intent intent = new Intent(activity, NewsImageViewerActivity.class);
					intent.putExtra("newsIdx", newsIdx);
					activity.startActivity(intent);
				}
			});
		}
		
		public void showData(NewsEntity entity){
			newsIdx = entity.index;
			
			titleLabel.setText(entity.title);
			contentLabel.setText(entity.contents);
			countLabel.setText("" + entity.imageCount);
			
			if (entity.imageCount == 0)
				imageHolder.setVisibility(View.GONE);
			else
			{
				if (entity.imageCount > 1){
					//backImage.setImageResource(R.drawable.expertpage_careernews_news_list_image_01);
					countLabel.setVisibility(View.VISIBLE);
				}else{
					//backImage.setImageResource(R.drawable.expertpage_careernews_news_list_image_02);
					countLabel.setVisibility(View.INVISIBLE);
				}
				
				imageHolder.setVisibility(View.VISIBLE);
			}
		}
	}	
	
	public interface NewsMenuListener{
		public void clickEdit(NewsEntity entity);
		public void clickDelete(NewsEntity entity);
	}
}
