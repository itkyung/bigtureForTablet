package com.clockworks.android.tablet.bigture;

import com.clockworks.android.tablet.bigture.views.common.PageIndicator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LookActivity extends AbstractBigtureActivity {
	private PageIndicator indicator;
	private ViewPager pager;
	private boolean fromFirst;
	Button btnClose;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		setContentView(R.layout.activity_lookaround);
		
		Intent intent = getIntent();
		fromFirst = intent.getBooleanExtra("fromFirst", false);
		
		btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(fromFirst){
					finish();
				}else{
					finish();
				}
			}
		});
		
		this.pager = (ViewPager)findViewById(R.id.pager);
		PagerAdapterClass adapter = new PagerAdapterClass(getApplicationContext());
		this.pager.setAdapter(adapter);
		this.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				changePage(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.indicator = (PageIndicator)findViewById(R.id.pageIndicator);
		this.indicator.setPageCount(7);
		this.indicator.setCurrentPage(0);
		
		SharedPreferences preferences = getApplicationContext().getSharedPreferences("Intro", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("alreadyViewIntro", true);
		editor.commit();
		
	}
	
	public void clickStart(){
		if(fromFirst){
			finish();
		}else{
			finish();
		}
	}
	
	public void changePage(int page){
		indicator.setCurrentPage(page);
		if(page == 6){
			btnClose.setVisibility(View.GONE);
		}else{
			btnClose.setVisibility(View.VISIBLE);
		}
	}
	
	private class PagerAdapterClass extends PagerAdapter{
		private LayoutInflater mInflater;

		public PagerAdapterClass(Context c){
			super();
			mInflater = LayoutInflater.from(c);
		}
		
		@Override
		public int getCount() {
		
			return 7;
		}

		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = null;
			
			switch(position){
			case 0:
				view = mInflater.inflate(R.layout.child_view_look_0, null);
				String msg = getResources().getString(R.string.what_is_bigture);
				TextView label1 = (TextView)view.findViewById(R.id.lookText);
				label1.setText(Html.fromHtml(msg));
				break;
			case 1:
				view = mInflater.inflate(R.layout.child_view_look_1, null);
				break;
			case 2:
				view = mInflater.inflate(R.layout.child_view_look_2, null);
				break;
			case 3:
				view = mInflater.inflate(R.layout.child_view_look_3, null);
				break;	
//			case 4:
//				view = mInflater.inflate(R.layout.child_view_look_4, null);
//				break;	
			case 4:
				view = mInflater.inflate(R.layout.child_view_look_5, null);
				break;
			case 5:
				view = mInflater.inflate(R.layout.child_view_look_6, null);
				break;	
			case 6:
				view = mInflater.inflate(R.layout.child_view_look_7, null);
				view.findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						clickStart();
					}
				});
				break;	
			
			}
			
			
			view.setTag(position);
			container.addView(view, 0);
			return view;
		}
		
		
	}
}
