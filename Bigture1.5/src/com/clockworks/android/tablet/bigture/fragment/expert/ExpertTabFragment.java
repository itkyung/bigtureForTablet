package com.clockworks.android.tablet.bigture.fragment.expert;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.fragment.common.HorizontalTabFragment;

public class ExpertTabFragment extends HorizontalTabFragment {

	int currentPage;
	Context context;

	int selectedMenuIdx = 0;
	ExpertTabListener eListener;

	@Override
	public void onAttach(Activity activity) {
	
		super.onAttach(activity);
		context = activity;
		this.eListener = (ExpertTabListener)activity;
	}

	@Override
	public void initView(View view) {
		super.initView(view);
		
		View allMenu = view.findViewById(R.id.tabAction1);
		allMenu.setVisibility(View.INVISIBLE);
		
		this.setMenuCount(2);
		
		this.setItemText(0, getResources().getString(R.string.expert_tab_name));
		this.setItemText(1, getResources().getString(R.string.expert_tab_job));
		
		TextView hiddenMenu = (TextView)view.findViewById(R.id.tabSortLabel1);
		hiddenMenu.setVisibility(View.GONE);
		ImageView hiddenImg = (ImageView)view.findViewById(R.id.imageDown2);
		hiddenImg.setVisibility(View.GONE);
		
		
		View tabFindFriend = view.findViewById(R.id.tabFindFriend);
		tabFindFriend.setVisibility(View.VISIBLE);
		
		Button btnFindFriend = (Button)view.findViewById(R.id.btnFindFriend);
		btnFindFriend.setText(getResources().getString(R.string.expert_lb_find_experts));
		btnFindFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				eListener.openFindExperts();
			}
		});
		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.currentPage = 1;
	}
	
	public interface ExpertTabListener{
		public void openFindExperts();
	}
	
	
}
