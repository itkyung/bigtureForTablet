package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;


import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.ColorPalette;
import com.clockworks.android.tablet.bigture.views.story.page.BodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.EditBodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.ViewBodyPage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class ColorPaletteDialog extends Dialog implements ViewPager.OnPageChangeListener{
	private Context context;
	
	private Button leftArrow;
	private Button rightArrow;
	private TextView colorSetName;
	private ViewPager palettePager;
	private PaletteView.SelectColorListener colorLisener;
	
	private ArrayList<ColorPalette> palettes = new ArrayList<ColorPalette>();
	
	public ColorPaletteDialog(Context context,PaletteView.SelectColorListener colorLisener) {
		super(context,android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		this.context = context;
		this.colorLisener = colorLisener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, context.getResources().getDisplayMetrics());
//		final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 255, context.getResources().getDisplayMetrics());
//		
//		LayoutParams params = getWindow().getAttributes();
//		params.width = width;
//		params.height = height;
//		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		
		setContentView(R.layout.popup_ske_color_palette);
		
		initPalette();
		
		leftArrow = (Button)findViewById(R.id.btnLeft);
		leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curIdx = palettePager.getCurrentItem();
				if(curIdx > 0){
					palettePager.setCurrentItem(curIdx-1, true);
				}else if(curIdx == 0){
					palettePager.setCurrentItem(4, true);
				}
				
			}
		});
		
		rightArrow = (Button)findViewById(R.id.btnRight);
		rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int curIdx = palettePager.getCurrentItem();
				int totalCount = palettePager.getAdapter().getCount();
				if(curIdx < totalCount-1){
					palettePager.setCurrentItem(curIdx+1, true);
				}else{
					palettePager.setCurrentItem(0,true);
				}
			}
		});
		
		colorSetName = (TextView)findViewById(R.id.colorSetName);
		
		palettePager = (ViewPager)findViewById(R.id.palettePager);
		palettePager.setOnPageChangeListener(this);
		
		ColorPageAdapter adapter = new ColorPageAdapter();
		palettePager.setAdapter(adapter);
		
		palettePager.setCurrentItem(2);
	}

	private void initPalette(){
		//Morden 
		ColorPalette modern = new ColorPalette(ColorPalette.MODERN,context);
		palettes.add(modern);
		
		ColorPalette popsicle = new ColorPalette(ColorPalette.POPSICLE,context);
		palettes.add(popsicle);
		
		ColorPalette basic = new ColorPalette(ColorPalette.BASIC,context);
		palettes.add(basic);
		
		ColorPalette friends = new ColorPalette(ColorPalette.FRIENDS,context);
		palettes.add(friends);
		
		ColorPalette day = new ColorPalette(ColorPalette.DAY,context);
		palettes.add(day);

		
	}
	
	@Override
	public void onPageScrollStateChanged(int position) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		ColorPalette p = palettes.get(position);
		colorSetName.setText(p.getGroupName());
		
	}

	
	class ColorPageAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return palettes.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return (view == obj);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object){
			container.removeView((View)object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position){
			
			LayoutInflater inflater = LayoutInflater.from(context);
			PaletteView pv  = (PaletteView)inflater.inflate(R.layout.view_color_palette, null);
			pv.initView(palettes.get(position), colorLisener,ColorPaletteDialog.this);
			
			pv.setTag(position);
			container.addView(pv, 0);
			return pv;
			
		}
		
	}
	
}
