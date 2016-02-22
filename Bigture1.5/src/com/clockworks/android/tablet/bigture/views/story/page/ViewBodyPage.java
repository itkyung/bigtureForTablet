package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;


public class ViewBodyPage extends BodyPage {
	public int currentPage = 0;
	private StoryArtworkEntity currentArtworkEntity;
	private ViewPager viewPager;
	
	private TextView loveCountLabel;
	private TextView awesomeCountLabel;
	private TextView wowCountLabel;
	private TextView funCountLabel;
	private TextView fantasticCountLabel;
	
	private Button btnTalkMap;
	private Button btnMore;
	private TextView descLabel;
	private Button btnPrev;
	private Button btnNext;
	private ViewBodyPageListener listener;
	private boolean myBigture;
	private StoryArtworkAdapter adpater = null;
	
	public ViewBodyPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.viewPager = (ViewPager)findViewById(R.id.viewPager1);
		this.loveCountLabel = (TextView)findViewById(R.id.loveCountLabel);
		this.awesomeCountLabel = (TextView)findViewById(R.id.awesomeCountLabel);
		this.wowCountLabel = (TextView)findViewById(R.id.wowCountLabel);
		this.funCountLabel = (TextView)findViewById(R.id.funCountLabel);
		this.fantasticCountLabel =  (TextView)findViewById(R.id.fantasticCountLabel);
		
		
		
		showSticker(null);
		this.btnTalkMap = (Button)findViewById(R.id.btnTalkMap);
		btnTalkMap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.btnMore = (Button)findViewById(R.id.btnMore);
		btnMore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		this.btnPrev = (Button)findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int nPage = currentPage-1;
				viewPager.setCurrentItem(nPage);
			}
		});
		
		this.btnNext = (Button)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int nPage = currentPage+1;
				viewPager.setCurrentItem(nPage);
			}
		});
		
		this.descLabel = (TextView)findViewById(R.id.descLabel);
	
		
		
		try{
			DisplayMetrics metrics = new DisplayMetrics();
			WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(metrics);
	
			viewPager.setPageMargin((int)(20 * metrics.density));
		}catch(Exception e){
			e.printStackTrace();
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int page) {
				List<StoryArtworkEntity> artworkList = pageEntity.artworkList;
				
				if (artworkList != null && page < artworkList.size()){
					if(page == 0){
						togglePrevBtn(false);
						toggleNextBtn(true);
					}
					currentArtworkEntity = artworkList.get(page);
					
					listener.onPageArtworksChanged(currentArtworkEntity);
					showSticker(currentArtworkEntity);
				}else{
					//마지막 페이지이다. 여기에서는 그림을 그릴수 있다.
					listener.onPageArtworksChanged(null);
					currentArtworkEntity = null;
					showSticker(null);
					togglePrevBtn(true);
					toggleNextBtn(false);
				}
				currentPage = page;
				toggleArrowButton();
			}	
		});
		
		
	}
	
		
		
	public void setListener(ViewBodyPageListener listener,boolean myBigture) {
		this.listener = listener;
		this.myBigture = myBigture;
	}

	private void showSticker(StoryArtworkEntity artwork){
		if(artwork == null){
			this.loveCountLabel.setText(0+"");
			this.awesomeCountLabel.setText(0+"");
			this.fantasticCountLabel.setText(0+"");
			this.funCountLabel.setText(0+"");
			this.wowCountLabel.setText(0+"");
		}else{
			this.loveCountLabel.setText(artwork.loveCount+"");
			this.awesomeCountLabel.setText(artwork.awesomeCount+"");
			this.fantasticCountLabel.setText(artwork.fantasticCount+"");
			this.funCountLabel.setText(artwork.funCount+"");
			this.wowCountLabel.setText(artwork.wowCount+"");		
			
		}
	}
	
	private void togglePrevBtn(boolean show){
		this.btnPrev.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	
	private void toggleNextBtn(boolean show){
		this.btnNext.setVisibility(show ? View.VISIBLE : View.GONE);
	}
		
	public void initView(Context context, StoryEntity storyEntity,
			StoryPageEntity pageEntity) {
		super.initView(context, storyEntity, pageEntity);
		
		this.descLabel.setText(pageEntity.description);
		
		//Pager Adapter를 만든다.
		this.adpater = new StoryArtworkAdapter(context, storyEntity, pageEntity, pageEntity.artworkList, myBigture);
		viewPager.setAdapter(adpater);
		viewPager.setCurrentItem(0);
		toggleArrowButton();
//		if(pageEntity.artworkList != null && pageEntity.artworkList.size() > 0){
//			listener.onPageArtworksChanged(pageEntity.artworkList.get(0));
//		}else{
//			listener.onPageArtworksChanged(null);
//		}
	}

	public void destorySubView(){
		this.adpater.finish();
	}
	
	public int getArtworksCount(){
		int artworkCount =  pageEntity.artworkList == null ? 0 : pageEntity.artworkList.size();
		return artworkCount;
	}
	
	private int getTotalPageCount(){
		return getArtworksCount() + 1;
	}
	
	public StoryArtworkEntity getFirstArtwork(){
		if(getArtworksCount() == 0) return null;
		return pageEntity.artworkList.get(0);
	}
	
	public void setFirstArtwork(){
		viewPager.setCurrentItem(0);
	}
	
	private void toggleArrowButton(){
		int totalPage = getTotalPageCount();
		
		if(currentPage == 0 && totalPage == 1){
			btnPrev.setVisibility(View.GONE);
			btnNext.setVisibility(View.GONE);
		}else if(currentPage == 0){
			btnPrev.setVisibility(View.GONE);
			btnNext.setVisibility(View.VISIBLE);
		}else if(currentPage == totalPage-1){
			//마지막페이지임.
			btnPrev.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.GONE);
		}else{
			btnPrev.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
		}
		
	}
	
	public interface ViewBodyPageListener{
		public void onPageArtworksChanged(StoryArtworkEntity artwork);
		
	}
	
	public void addArtworkList(ArrayList<StoryArtworkEntity> artworks){
		if(pageEntity.artworkList == null){
			pageEntity.artworkList = new ArrayList<StoryArtworkEntity>();
		}else{
			pageEntity.artworkList.clear();
		}
		pageEntity.artworkList.addAll(artworks);
		this.adpater.setArtworkList(pageEntity.artworkList);
		this.adpater.notifyDataSetChanged();
		viewPager.setCurrentItem(pageEntity.artworkList.size()-1, true);
	}
	
	/**
	 * 새로운 artworkEntity를 추가한다.
	 * @param artwork
	 */
	public void addArtwork(StoryArtworkEntity artwork){
		if(pageEntity != null){
			pageEntity.addArtwork(artwork);
			this.adpater.notifyDataSetChanged();
		}
	}
	
	public StoryPageEntity getPageEntity(){
		return this.pageEntity;
	}
}
