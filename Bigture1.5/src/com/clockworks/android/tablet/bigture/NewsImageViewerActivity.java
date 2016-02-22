package com.clockworks.android.tablet.bigture;

import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NewsImageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.NewsHandler;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class NewsImageViewerActivity extends Activity
{
	TextView pageLabel;
	ViewPager viewPager;
	List<NewsImageEntity> imageList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_news_image);
		
		String newsIdx = getIntent().getExtras().getString("newsIdx");
		loadNewsImages(newsIdx);
		
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View arg0)
			{
				finish();
			}
		});

		pageLabel = (TextView)findViewById(R.id.pageLabel);
		pageLabel.setVisibility(View.INVISIBLE);
		
		viewPager = (ViewPager)findViewById(R.id.viewPager1);
	}
	
	private void loadNewsImages(String newsIdx)
	{
		new AsyncTask<String,Void,List<NewsImageEntity>>()
		{
			@Override
			protected List<NewsImageEntity> doInBackground(String... params)
			{
				return NewsHandler.readNewsImageList(params[0]);
			}

			@Override
			protected void onPostExecute(List<NewsImageEntity> result)
			{				
				if (result != null)
				{
					imageList = result;
					viewPager.setAdapter(new ImageAdapter());
					
					pageLabel.setText("1/" + imageList.size());
					pageLabel.setVisibility(View.VISIBLE);
				}					
				
				super.onPostExecute(result);
			}
			
		}.execute(newsIdx);
	}

	class ImageAdapter extends PagerAdapter
	{
		@Override
		public int getCount()
		{
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return (arg0 == arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			ImageView imageView = (ImageView)object;
			imageView.setImageBitmap(null);
			
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = new ImageView(NewsImageViewerActivity.this);
			imageView.setScaleType(ScaleType.FIT_CENTER);
			container.addView(imageView, 0);
			
			String imageURL = imageList.get(position).getImageURL();
			BitmapDownloader.getInstance().displayImage(imageURL, null, imageView, null);
			
			ViewPager.LayoutParams params = new ViewPager.LayoutParams();
			params.width = 1024;
			params.height = 768;
			
			imageView.setLayoutParams(params);
			
			return imageView;
		}
	}
}
