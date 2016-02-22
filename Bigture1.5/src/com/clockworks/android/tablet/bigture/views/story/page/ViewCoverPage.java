package com.clockworks.android.tablet.bigture.views.story.page;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewCoverPage extends CoverPage {
	private TextView titleLabel;
	private TextView nameLabel;
	private ImageView ivCover;
	private Button collectBtn;
	private Context context;
	
	public ViewCoverPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		this.titleLabel = (TextView)findViewById(R.id.titleLabel);
		this.nameLabel = (TextView)findViewById(R.id.nameLabel);
		this.ivCover = (ImageView)findViewById(R.id.ivCover);
		this.collectBtn = (Button)findViewById(R.id.collectBtn);
		collectBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void initView(StoryEntity entity) {
		super.initView(entity);
		AccountEntity account = AccountManager.getInstance().getAccountEntity();
		nameLabel.setText(entity.ownerName);
		titleLabel.setText(entity.title);
		collectBtn.setSelected(entity.collected);
		
		String imagePath = entity.getCoverImageURL();
		if(imagePath != null){
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			bitmapOptions.inSampleSize = 3;
			
			BitmapDownloader.getInstance().displayImage(imagePath, bitmapOptions, ivCover, null);
		}
	}
	
	
	
}
