package com.clockworks.android.tablet.bigture.fragment.common;




import com.clockworks.android.tablet.bigture.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalTabFragment extends Fragment implements View.OnClickListener{
	protected TextView menuLabels[];
	protected View menuMarkers[];
	protected ImageView verticalLines[];
	
	protected int selection = 0;
	protected OnTabMenuSelectionListener listener;
	
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		
		this.listener = (OnTabMenuSelectionListener)activity;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_horizontal_tab, container, false);
		
		initView(view);
		
		return view;
	}

	public void initView(View view){
		int[] menuLabelIds  = {R.id.tvMenu1, R.id.tvMenu2, R.id.tvMenu3, R.id.tvMenu4, R.id.tvMenu5, R.id.tvMenu6};
		int[] menuMarkerIds = {R.id.ivMark1, R.id.ivMark2, R.id.ivMark3, R.id.ivMark4, R.id.ivMark5, R.id.ivMark6};
		int[] verticalLineIds = {R.id.ivSeperator1, R.id.ivSeperator2, R.id.ivSeperator3, R.id.ivSeperator4, R.id.ivSeperator5, R.id.ivSeperator6, R.id.ivSeperator7};
		
		menuLabels  = new TextView[menuLabelIds.length];
		menuMarkers = new View[menuMarkerIds.length];
		verticalLines = new ImageView[verticalLineIds.length];
		
		for (int i = 0; i < menuLabels.length; i++)
		{
			menuLabels[i] = (TextView)view.findViewById(menuLabelIds[i]);
			menuLabels[i].setOnClickListener(this);
		}

		for (int i = 0; i < menuMarkers.length; i++)
			menuMarkers[i] = view.findViewById(menuMarkerIds[i]);

		for (int i = 0; i < verticalLineIds.length; i++)
			verticalLines[i] = (ImageView)view.findViewById(verticalLineIds[i]);

	}
	
	public void setSelection(int position){
		menuLabels[selection].setTextColor(getResources().getColor(R.color.tab_text_n));
		menuLabels[position].setTextColor(getResources().getColor(R.color.tab_text_p));
		menuMarkers[selection].setVisibility(View.INVISIBLE);
		menuMarkers[position].setVisibility(View.VISIBLE);
		
		this.selection = position;
	}
	
	public int getSelection()
	{
		return selection;
	}
	
	public void setMenuCount(int count)
	{
		for (int i = menuLabels.length-1; i >= count; i--)
		{
			menuLabels[i].setVisibility(View.GONE);
			menuMarkers[i].setVisibility(View.GONE);
			verticalLines[i].setVisibility(View.GONE);
		}
	}
	
	public void setItemText(int position, String text)
	{
		menuLabels[position].setText(text);
	}
	
	
	
	@Override
	public void onClick(View v) {
		int sel = -1;
		for (int i = 0; i < menuLabels.length; i++)
		{
			if (menuLabels[i] == v)
			{
				sel = i;
				break;
			}
		}
		
		if (sel >= 0 && this.selection != sel)
		{
			menuLabels[selection].setTextColor(getResources().getColor(R.color.tab_text_n));
			menuLabels[sel].setTextColor(getResources().getColor(R.color.tab_text_p));
			menuMarkers[selection].setVisibility(View.INVISIBLE);
			menuMarkers[sel].setVisibility(View.VISIBLE);
			
			this.selection = sel;
			
			if (this.listener != null)
				this.listener.onMenuSelected(this, sel);
		}
		
	}



	public interface OnTabMenuSelectionListener{
		void onMenuSelected(HorizontalTabFragment tabMenu, int position);
	}
}
