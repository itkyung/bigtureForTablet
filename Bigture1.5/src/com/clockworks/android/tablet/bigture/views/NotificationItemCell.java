package com.clockworks.android.tablet.bigture.views;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.NotificationClickListener;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationEntity;
import com.clockworks.android.tablet.bigture.utils.DateUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class NotificationItemCell extends RelativeLayout {
	private Context context;
	private NotificationClickListener listener;
	private NotificationEntity entity;
	private ImageView notiImage;
	private TextView notiTitle;
	private TextView notiMsg;
	private TextView dateText;
	BitmapFactory.Options bitmapOptions;
	
	
	public NotificationItemCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
	}
	
	@Override
	protected void onFinishInflate() {
		this.notiImage = (ImageView)findViewById(R.id.notiImage);
		this.notiTitle = (TextView)findViewById(R.id.notiTitle);
		this.notiMsg = (TextView)findViewById(R.id.notiMsg);
		this.dateText = (TextView)findViewById(R.id.dateText);
		
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null)
					listener.clickNotification(entity);
			}
		});
	}

	
	public void updateView(NotificationEntity entity,NotificationClickListener listener){
		this.entity = entity;
		this.listener = listener;
		
		String imageUrl = null;
		if(entity.isReadFlag()){
			this.notiTitle.setTextColor(getResources().getColor(R.color.text_read_color));
			this.notiMsg.setTextColor(getResources().getColor(R.color.text_read_color));
		}else{
			this.notiTitle.setTextColor(getResources().getColor(R.color.text_unread_color));
			this.notiMsg.setTextColor(getResources().getColor(R.color.text_unread_color));
		}
		
		String date = DateUtil.getTimeString(entity.getCreated());
		this.dateText.setText(date);
		
		switch (entity.getType()) {
		case COMMENT:
		case LIKEU:
		case POSTCARD:
		case REQ_PICTURE:
			imageUrl = entity.getActorImage();
			if(imageUrl != null){
				BitmapDownloader.getInstance().displayImage(imageUrl, bitmapOptions, this.notiImage, null);
			}else{
				notiImage.setImageDrawable(getResources().getDrawable(R.drawable.common_designpopup_notification_icon_profile));
			}
			notiTitle.setVisibility(View.GONE);
			notiMsg.setText(entity.getMsg());
			break;
		case CONTEST:
		case CONTEST_DUE:
		case CONTEST_WINNER:
			notiImage.setImageDrawable(getResources().getDrawable(R.drawable.common_designpopup_notification_icon_contest));
			notiTitle.setVisibility(View.VISIBLE);
			notiTitle.setText("<"+entity.getTitle()+">");
			notiMsg.setText(entity.getMsg());
			break;
		case ARTCLASS:
		case INVITE_CLASS:
			notiImage.setImageDrawable(getResources().getDrawable(R.drawable.common_designpopup_notification_icon_class));
			notiTitle.setVisibility(View.VISIBLE);
			notiTitle.setText("<"+entity.getTitle()+">");
			notiMsg.setText(entity.getMsg());
			break;
		case NOTICE:
		case NEW_VERSION:
		case SPAM:
			notiImage.setImageDrawable(getResources().getDrawable(R.drawable.common_designpopup_notification_icon_bigture));
			notiTitle.setVisibility(View.VISIBLE);
			notiTitle.setText("<Bigture's Notice>");
			notiMsg.setText(entity.getMsg());
			break;
			
		default:
			break;
		}
		
		
	}
}
