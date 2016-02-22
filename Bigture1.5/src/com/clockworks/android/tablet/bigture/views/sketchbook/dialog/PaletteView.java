	package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.views.sketchbook.data.ColorPalette;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PaletteView extends LinearLayout implements View.OnClickListener{
	private Context context;
	private ColorPalette palette;
	private SelectColorListener listener;
	private ColorPaletteDialog dialog;
	
	public PaletteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		findViewById(R.id.color0).setOnClickListener(this);
		findViewById(R.id.color1).setOnClickListener(this);
		findViewById(R.id.color2).setOnClickListener(this);
		findViewById(R.id.color3).setOnClickListener(this);
		findViewById(R.id.color4).setOnClickListener(this);
		findViewById(R.id.color5).setOnClickListener(this);
		findViewById(R.id.color6).setOnClickListener(this);
		findViewById(R.id.color7).setOnClickListener(this);
		findViewById(R.id.color8).setOnClickListener(this);
		findViewById(R.id.color9).setOnClickListener(this);
		findViewById(R.id.color10).setOnClickListener(this);
		findViewById(R.id.color11).setOnClickListener(this);
		findViewById(R.id.color12).setOnClickListener(this);
		findViewById(R.id.color13).setOnClickListener(this);
		findViewById(R.id.color14).setOnClickListener(this);
		
			
	}



	public void initView(ColorPalette p,SelectColorListener l,ColorPaletteDialog dialog){
		this.palette = p;
		this.listener = l;
		this.dialog = dialog;
		
		ImageView img0 = (ImageView)findViewById(R.id.colorImg0);
		GradientDrawable drawable0 = (GradientDrawable) img0.getBackground();
		drawable0.setColor(getResources().getColor(palette.colors[0]));
		img0.setOnClickListener(this);
		
		
		
		ImageView img1 = (ImageView)findViewById(R.id.colorImg1);
		GradientDrawable drawable1 = (GradientDrawable) img1.getBackground();
		drawable1.setColor(getResources().getColor(palette.colors[1]));
		img1.setOnClickListener(this);
		
		ImageView img2 = (ImageView)findViewById(R.id.colorImg2);
		GradientDrawable drawable2 = (GradientDrawable) img2.getBackground();
		drawable2.setColor(getResources().getColor(palette.colors[2]));
		img2.setOnClickListener(this);
		
		ImageView img3 = (ImageView)findViewById(R.id.colorImg3);
		GradientDrawable drawable3 = (GradientDrawable) img3.getBackground();
		drawable3.setColor(getResources().getColor(palette.colors[3]));
		img3.setOnClickListener(this);
		
		ImageView img4 = (ImageView)findViewById(R.id.colorImg4);
		GradientDrawable drawable4 = (GradientDrawable) img4.getBackground();
		drawable4.setColor(getResources().getColor(palette.colors[4]));
		img4.setOnClickListener(this);
		
		ImageView img5 = (ImageView)findViewById(R.id.colorImg5);
		GradientDrawable drawable5 = (GradientDrawable) img5.getBackground();
		drawable5.setColor(getResources().getColor(palette.colors[5]));
		img5.setOnClickListener(this);
		
		ImageView img6 = (ImageView)findViewById(R.id.colorImg6);
		GradientDrawable drawable6 = (GradientDrawable) img6.getBackground();
		drawable6.setColor(getResources().getColor(palette.colors[6]));
		img6.setOnClickListener(this);
		
		ImageView img7 = (ImageView)findViewById(R.id.colorImg7);
		GradientDrawable drawable7 = (GradientDrawable) img7.getBackground();
		drawable7.setColor(getResources().getColor(palette.colors[7]));
		img7.setOnClickListener(this);
		
		ImageView img8 = (ImageView)findViewById(R.id.colorImg8);
		GradientDrawable drawable8 = (GradientDrawable) img8.getBackground();
		drawable8.setColor(getResources().getColor(palette.colors[8]));

		img8.setOnClickListener(this);
		
		ImageView img9 = (ImageView)findViewById(R.id.colorImg9);
		GradientDrawable drawable9 = (GradientDrawable) img9.getBackground();
		drawable9.setColor(getResources().getColor(palette.colors[9]));

		img9.setOnClickListener(this);
		
		ImageView img10 = (ImageView)findViewById(R.id.colorImg10);
		GradientDrawable drawable10 = (GradientDrawable) img10.getBackground();
		drawable10.setColor(getResources().getColor(palette.colors[10]));

		img10.setOnClickListener(this);
		
		ImageView img11 = (ImageView)findViewById(R.id.colorImg11);
		GradientDrawable drawable11 = (GradientDrawable) img11.getBackground();
		drawable11.setColor(getResources().getColor(palette.colors[11]));

		img11.setOnClickListener(this);
		
		ImageView img12 = (ImageView)findViewById(R.id.colorImg12);
		GradientDrawable drawable12 = (GradientDrawable) img12.getBackground();
		drawable12.setColor(getResources().getColor(palette.colors[12]));

		img12.setOnClickListener(this);
		
		
		ImageView img13 = (ImageView)findViewById(R.id.colorImg13);
		GradientDrawable drawable13 = (GradientDrawable) img13.getBackground();
		drawable13.setColor(getResources().getColor(palette.colors[13]));

		img13.setOnClickListener(this);
		
		ImageView img14 = (ImageView)findViewById(R.id.colorImg14);
		GradientDrawable drawable14 = (GradientDrawable) img14.getBackground();
		drawable14.setColor(getResources().getColor(palette.colors[14]));

		img14.setOnClickListener(this);
	}
	
	
	

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int color = -1;
		switch(id){
		case R.id.color0:
		case R.id.colorImg0:
			color = palette.colors[0];
			break;
		case R.id.color1:
		case R.id.colorImg1:
			color = palette.colors[1];
			break;
		case R.id.color2:
		case R.id.colorImg2:
			color = palette.colors[2];
			break;
		case R.id.color3:
		case R.id.colorImg3:
			color = palette.colors[3];
			break;
		case R.id.color4:
		case R.id.colorImg4:
			color = palette.colors[4];
			break;
		case R.id.color5:
		case R.id.colorImg5:
			color = palette.colors[5];
			break;
		case R.id.color6:
		case R.id.colorImg6:
			color = palette.colors[6];
			break;
		case R.id.color7:
		case R.id.colorImg7:
			color = palette.colors[7];
			break;
		case R.id.color8:
		case R.id.colorImg8:
			color = palette.colors[8];
			break;
		case R.id.color9:
		case R.id.colorImg9:	
			color = palette.colors[9];
			break;
		case R.id.color10:
		case R.id.colorImg10:
			color = palette.colors[10];
			break;
		case R.id.color11:
		case R.id.colorImg11:	
			color = palette.colors[11];
			break;
		case R.id.color12:
		case R.id.colorImg12:	
			color = palette.colors[12];
			break;
		case R.id.color13:
		case R.id.colorImg13:	
			color = palette.colors[13];
			break;
		case R.id.color14:
		case R.id.colorImg14:	
			color = palette.colors[14];
			break;		
		
		}
		
		listener.selectColor(getResources().getColor(color),dialog);
	}




	public interface SelectColorListener{
		void selectColor(int color,ColorPaletteDialog dialog);
	}
	
	
}
