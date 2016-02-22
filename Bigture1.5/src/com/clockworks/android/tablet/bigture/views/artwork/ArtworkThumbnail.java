package com.clockworks.android.tablet.bigture.views.artwork;

import java.util.List;





import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ArtworkThumbnail extends RelativeLayout implements View.OnClickListener{
	Context context;

	Callback callback;
	ArtworkEntity artwork;
	

	BitmapFactory.Options bitmapOptions;
	
	TextView titleLabel;
	TextView nameLabel;
	ImageView imageArtwork;
	ImageButton imageCollect;
	ImageButton imageOpen;
	
	TextView  winnerLabel;

	
	ProgressBar progressBar;
	
	boolean myPage;
	String myId;
	
	public ArtworkThumbnail(Context context, AttributeSet attrs){
		super(context, attrs);
		myId = AccountManager.getUserIdx();

		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_artwork_thumb, this);
		
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		nameLabel  = (TextView)findViewById(R.id.nameLabel);
		
		imageArtwork  = (ImageView)findViewById(R.id.imageArtwork);
		imageArtwork.setOnClickListener(this);
		
		imageCollect = (ImageButton)findViewById(R.id.imageCollect);
		imageCollect.setOnClickListener(this);
		
		imageOpen = (ImageButton)findViewById(R.id.imageOpen);
		imageOpen.setOnClickListener(this);
		
		
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		
		winnerLabel = (TextView)findViewById(R.id.winnerLabel);
	
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;

		int viewWidth  = context.getResources().getDimensionPixelSize(R.dimen.artwork_thumb_width);
		int viewHeight = context.getResources().getDimensionPixelSize(R.dimen.artwork_thumb_height);
		
		bitmapOptions.inSampleSize = 1 + ImageUtil.getMaxSampleSize(viewWidth, viewHeight, 568, 391);
	}

	public void update(ArtworkThumbnail.Callback callback, ArtworkEntity artwork, boolean myPage){
		this.callback    = callback;
		this.artwork = artwork;
		this.myPage = myPage;
		showArtworkProperties(artwork, true);
	}

	public void showArtworkProperties(ArtworkEntity entity, boolean showImage){
		// Show artwork's title
		titleLabel.setText(entity.title);
		
		// Show owner name and country
		nameLabel.setText(Html.fromHtml("<b>" + entity.ownerName + "</b>"));

		// Show contest info if this is contest entry.
		if (entity.contestRank != 0){
			winnerLabel.setVisibility(View.VISIBLE);
			winnerLabel.setText(entity.referenceName);
		}else{
			winnerLabel.setVisibility(View.GONE);
		
		}
		
		if(myId == null || myId.equals(artwork.userId)){
			imageCollect.setClickable(false);
			imageCollect.setVisibility(View.GONE);
			if(entity.opend){
				imageOpen.setVisibility(View.GONE);
			}else{
				imageOpen.setVisibility(View.VISIBLE);
			}
			
		}else{
		
			if(this.myPage){
				//나의 페이지이면 collect버튼을 사라지게 하고 비공개인지 아닌지만 나타나게한다.
				imageCollect.setClickable(false);
				imageCollect.setVisibility(View.GONE);
				
				if(entity.opend){
					imageOpen.setVisibility(View.GONE);
				}else{
					imageOpen.setVisibility(View.VISIBLE);
				}
			}else{
				imageCollect.setClickable(true);
				imageCollect.setVisibility(View.VISIBLE);
				imageOpen.setVisibility(View.GONE);
		
				//나의 페이지가 아니면 내가 collect한 artwork인지 여부에 따라서 collect버튼의 활성도를 바꾼다.
				if(entity.collected){
					imageCollect.setSelected(true);
				}else{
					imageCollect.setSelected(false);
				}
			}
		}
		if(entity.spam){
			imageCollect.setClickable(false);
			//TODO 여기에서 spam처리된 이미지를 보여줘야한다.
			
			
			
		}else{
			if (showImage){
				Drawable d = imageArtwork.getDrawable();
				
				if (d != null && d instanceof BitmapDrawable)
				{
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					if (bitmap != null)
						bitmap = null;
					
					d.setCallback(null);
				}
				imageArtwork.setImageBitmap(null);
				BitmapDownloader imageDownloader = BitmapDownloader.getInstance();
				imageDownloader.displayImage(entity.getThumbnailURL(), bitmapOptions, imageArtwork, progressBar);
			}
		}
	}

	@Override
	public void onClick(View view){
		if (view.getId() == R.id.imageArtwork){
			callback.onShowArtworkDetail(artwork);
		}else if (view.getId() == R.id.imageCollect){
			ArtworkEntity artworkEntity = artwork;
			if(this.myPage){
				//이경우이면 비공개 설정이다. 여기로 오는경우는 없어야한다.
				
				
			}else{
			
				// check login
				if (!AccountManager.isLogin()){
					BigtureEnvironment.showAlertMessage(context, 16);
					return;
				}
				
				if (artworkEntity.collected){
					callback.onRemoveFromCollection(this, artworkEntity);
				}else{
					callback.onAddToCollection(this, artworkEntity);
				}
				
			}
		}
	}

	
	public interface Callback
	{
		public void onShowArtworkDetail(ArtworkEntity artwork);
		
		public void onAddToCollection(ArtworkThumbnail thumbnail, ArtworkEntity entity);
		public void onRemoveFromCollection(ArtworkThumbnail thumbnail, ArtworkEntity entity);
		
		public void onNextPage(int crntPage);
		public void onPrevPage(int crntPage);
		public void onAddClick();
	}

}
