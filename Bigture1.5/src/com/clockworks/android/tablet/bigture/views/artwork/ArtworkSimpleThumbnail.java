package com.clockworks.android.tablet.bigture.views.artwork;

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

public class ArtworkSimpleThumbnail extends RelativeLayout implements View.OnClickListener{
	Context context;

	Callback callback;
	ArtworkEntity artwork;
	

	BitmapFactory.Options bitmapOptions;
	
	TextView titleLabel;
	
	ImageView imageArtwork;
	
	ProgressBar progressBar;
	
	public ArtworkSimpleThumbnail(Context context, AttributeSet attrs){
		super(context, attrs);

		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		inflater.inflate(R.layout.child_view_artwork_simple_thumb, this);
		
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		
		
		imageArtwork  = (ImageView)findViewById(R.id.imageArtwork);
		imageArtwork.setOnClickListener(this);
		
	
		progressBar = (ProgressBar)findViewById(R.id.progressBar1);
		
	
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;

		int viewWidth  = context.getResources().getDimensionPixelSize(R.dimen.artwork_thumb_width);
		int viewHeight = context.getResources().getDimensionPixelSize(R.dimen.artwork_thumb_height);
		
		bitmapOptions.inSampleSize = 1 + ImageUtil.getMaxSampleSize(viewWidth, viewHeight, 568, 391);
	}

	public void update(ArtworkSimpleThumbnail.Callback callback, ArtworkEntity artwork){
		this.callback    = callback;
		this.artwork = artwork;

		showArtworkProperties(artwork, true);
	}

	public void showArtworkProperties(ArtworkEntity entity, boolean showImage){
		// Show artwork's title
		titleLabel.setText(entity.title);
		
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

	@Override
	public void onClick(View view){
		if (view.getId() == R.id.imageArtwork){
			callback.onSelectArtwork(artwork);
		}
	}

	
	public interface Callback{
		public void onSelectArtwork(ArtworkEntity artwork);
		
	}
}
