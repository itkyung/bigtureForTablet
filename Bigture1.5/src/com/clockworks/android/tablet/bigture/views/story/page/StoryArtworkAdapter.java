package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.SketchbookActivity;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class StoryArtworkAdapter extends PagerAdapter{
	ArrayList<StoryArtworkEntity> artworkList;
	boolean myBigture; //myBigture에서보는지 여부. myBigture에서 보면 artworks owner는 안나와야한다. 그리고 나의 artworks만 나와야한다.
	
	Context context;
	
	StoryEntity storyEntity;
	StoryPageEntity storyPageEntity;
	BitmapFactory.Options bitmapOptions;
	
	StoryThumbnail storyThumbs[];
//	ArrayList<Boolean> thumbsInitiated = new ArrayList<Boolean>();
	
	public StoryArtworkAdapter(Context context, StoryEntity storyEntity, StoryPageEntity storyPageEntity, ArrayList<StoryArtworkEntity> artworkList, boolean myBigture){
		this.context = context;
		this.storyEntity = storyEntity;
		this.storyPageEntity = storyPageEntity;
		this.myBigture = myBigture;
		if(myBigture){
			filteringMyArtworks(artworkList);
		}else{
			this.artworkList = artworkList;
		}
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 1;
		
		//storyThumbs = new StoryThumbnail[this.artworkList.size()];
		
		
	}

	//본인의 artworks만 필터링한다.
	private void filteringMyArtworks(ArrayList<StoryArtworkEntity> artworkList){
		this.artworkList = artworkList;
		
		String userId = AccountManager.getInstance().getUserIndex();
		
		Iterator<StoryArtworkEntity>  iter = this.artworkList.iterator();
		while(iter.hasNext()){
			StoryArtworkEntity artwork = (StoryArtworkEntity)iter.next();
			if(!userId.equals(artwork.userId)) iter.remove();
		}
	}
	
	public void setArtworkList(ArrayList<StoryArtworkEntity> artworkList){
		if(myBigture){
			filteringMyArtworks(artworkList);
		}else{
			this.artworkList = artworkList;
		}
	}
	
	@Override
	public int getCount(){
		return 1 + (artworkList == null ? 0 : artworkList.size());
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1){
		return (arg0 == arg1);
	}
	
	@Override
    public void destroyItem(View pager, int position, Object view){
		if (position < getCount()-1 && view != null && view instanceof StoryThumbnail){
			((StoryThumbnail)view).resetContent();
			//이것을 하면 memory leak이 난다.이유를 모르겠다. 그래서 해당 객체의 finalize에서 reset한다.
		}
        ((ViewPager)pager).removeView((View)view);
        view = null;
    }
	
	@Override
	public Object instantiateItem(View pager, int position){
		if (artworkList != null && position < artworkList.size()){
			StoryArtworkEntity entity = artworkList.get(position);

			LayoutInflater inflater = LayoutInflater.from(context);
			StoryThumbnail thumb = (StoryThumbnail)inflater.inflate(R.layout.child_view_story_artwork, null);
			thumb.updateView(entity, position, artworkList.size(), myBigture, true);
			
			((ViewPager)pager).addView(thumb, 0);
			//storyThumbs[position] = thumb;
		
			return thumb;
		}else{
			//마지막 페이지 
			LayoutInflater inflater = LayoutInflater.from(context);
			View nView = inflater.inflate(R.layout.child_view_story_artwork_blank, null);
			
			Button btnDrawHere = (Button)nView.findViewById(R.id.btnDrawHere);
			
			btnDrawHere.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					Activity activity = (Activity)context;
					Intent intent = new Intent(activity, SketchbookActivity.class);
					
					DrawingPurpose purpose = new DrawingPurpose();
					purpose.reason = DrawingPurpose.STORY;
					purpose.storyId = storyPageEntity.storyId;
					purpose.pageNo  = storyPageEntity.pageNo;
					purpose.pageId = storyPageEntity.pageId;
					
					intent.putExtra("drawingPurpose", purpose);
					
					activity.startActivityForResult(intent, BigtureEnvironment.REQ_CODE_SKETCHBOOK);
				}
			});

			((ViewPager)pager).addView(nView, 0);
			return nView;
		}		
	}
	
	
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void finish(){
//		for(StoryThumbnail th : storyThumbs){
//			if(th != null){
//				th.resetContent();
//			}
//		}
//		storyThumbs = null;
	}
}
