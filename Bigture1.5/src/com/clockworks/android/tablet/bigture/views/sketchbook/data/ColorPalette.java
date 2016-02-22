package com.clockworks.android.tablet.bigture.views.sketchbook.data;

import android.content.Context;

import com.clockworks.android.tablet.bigture.R;

public class ColorPalette {
	public static final int MODERN = 0;
	public static final int POPSICLE = 1;
	public static final int BASIC = 2;
	public static final int FRIENDS = 3;
	public static final int DAY = 4;
	
	public int[] modernColor = {R.color.morden_pop_0,R.color.morden_pop_1,R.color.morden_pop_2,
			R.color.morden_pop_3,R.color.morden_pop_4,R.color.morden_pop_5,R.color.morden_pop_6,
			R.color.morden_pop_7,R.color.morden_pop_8,R.color.morden_pop_9,R.color.morden_pop_10,
			R.color.morden_pop_11,R.color.morden_pop_12,R.color.morden_pop_13,R.color.morden_pop_14};
	
	public int[] popsicleColor = {R.color.posicle_0,R.color.posicle_1,R.color.posicle_2,
			R.color.posicle_3,R.color.posicle_4,R.color.posicle_5,R.color.posicle_6,
			R.color.posicle_7,R.color.posicle_8,R.color.posicle_9,R.color.posicle_10,
			R.color.posicle_11,R.color.posicle_12,R.color.posicle_13,R.color.posicle_14};
	
	public int[] basicColor = {R.color.basic_0,R.color.basic_1,R.color.basic_2,
			R.color.basic_3,R.color.basic_4,R.color.basic_5,R.color.basic_6,
			R.color.basic_7,R.color.basic_8,R.color.basic_9,R.color.basic_10,
			R.color.basic_11,R.color.basic_12,R.color.basic_13,R.color.basic_14};
	
	public int[] friendsColor = {R.color.friends_0,R.color.friends_1,R.color.friends_2,
			R.color.friends_3,R.color.friends_4,R.color.friends_5,R.color.friends_6,
			R.color.friends_7,R.color.friends_8,R.color.friends_9,R.color.friends_10,
			R.color.friends_11,R.color.friends_12,R.color.friends_13,R.color.friends_14};
	
	public int[] dayColor = {R.color.day_0,R.color.day_1,R.color.day_2,
			R.color.day_3,R.color.day_4,R.color.day_5,R.color.day_6,
			R.color.day_7,R.color.day_8,R.color.day_9,R.color.day_10,
			R.color.day_11,R.color.day_12,R.color.day_13,R.color.day_14};
	
	
	private String groupName;
	public int[] colors;
	
	public ColorPalette(int type,Context context){
		switch(type){
		case MODERN:
			this.groupName = context.getResources().getString(R.string.sketchbook_lb_color_modern);
			this.colors = modernColor;
			break;
		case POPSICLE:
			this.groupName = context.getResources().getString(R.string.sketchbook_lb_color_popsicle);
			this.colors = popsicleColor;
			break;
		case BASIC:
			this.groupName = context.getResources().getString(R.string.sketchbook_lb_color_basic);
			this.colors = basicColor;
			break;
		case FRIENDS:
			this.groupName = context.getResources().getString(R.string.sketchbook_lb_color_face);
			this.colors = friendsColor;
			break;
		case DAY:
			this.groupName = context.getResources().getString(R.string.sketchbook_lb_color_dream);
			this.colors = dayColor;			
			break;
		}
		 
		
	}
	
	public String getGroupName() {
		return groupName;
	}


	
	
}
