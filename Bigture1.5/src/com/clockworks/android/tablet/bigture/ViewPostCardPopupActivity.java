package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;




import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardReceiverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.PostCardHandler;
import com.clockworks.android.tablet.bigture.utils.DateUtil;
import com.clockworks.android.tablet.bigture.views.common.ExpandableTextView;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewPostCardPopupActivity extends Activity {
	
	private Context context;
	private PostCardEntity cardEntity;
	private Button btnReply;
	private boolean isSendCard;
	private boolean isMyPage;
	private ImageView artworkImg;
	BitmapFactory.Options bitmapOptions;
	private PopupWindow sendMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		context = this;
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		setContentView(R.layout.popup_view_card);
		
		
		Intent intent = getIntent();
		String cardId = intent.getStringExtra("cardId");
		this.isSendCard = intent.getBooleanExtra("sendFlag", false);
		this.isMyPage = intent.getBooleanExtra("myPage", false);
		
		PostCardReadTask task = new PostCardReadTask();
		task.execute(cardId);
		
	}
	
	private void updateView(PostCardEntity entity){
		this.cardEntity = entity;

		this.artworkImg = (ImageView)findViewById(R.id.artworkImg);
		
		Button btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		BitmapDownloader imageDownloader = BitmapDownloader.getInstance();
		imageDownloader.displayImage(entity.getArtworkImageURL(), bitmapOptions, artworkImg, null);
		
		
		TextView artworkTitleView = (TextView)findViewById(R.id.artworkTitle);
		artworkTitleView.setText(cardEntity.photoTitle);
		
		TextView artworkOwnerView = (TextView)findViewById(R.id.artworkOwner);
		artworkOwnerView.setText(cardEntity.photoOwnerName);
		
		TextView artworkDateView = (TextView)findViewById(R.id.artworkDate);
		artworkDateView.setText(DateUtil.getDateFormatString(cardEntity.photoDate,"yyyy"));
		
		TextView commentView = (TextView)findViewById(R.id.commentText);
		commentView.setText(entity.comment);
		
		TextView sendDateText = (TextView)findViewById(R.id.sendDateText);
		sendDateText.setText(DateUtil.getDateFormatString(entity.created, "MM.dd.yyyy hh24:mm"));
		
		TextView targetNameText = (TextView)findViewById(R.id.targetName);
		TextView targetJobText = (TextView)findViewById(R.id.targetJob);
		TextView targetCountryText = (TextView)findViewById(R.id.targetCountry);
		ImageView targetProfile = (ImageView)findViewById(R.id.targetProfile);
		
		if(isSendCard){
			RelativeLayout singleTargetContainer = (RelativeLayout)findViewById(R.id.singleTargetContainer);
			RelativeLayout multiTargetContainer =  (RelativeLayout)findViewById(R.id.multiTargetContainer);
			
			//Receiver들의 이름을 나열해준다.
			if(cardEntity.receivers != null && cardEntity.receivers.size() > 0){
				if(cardEntity.receivers.size() == 1){
					PostCardReceiverEntity receiverEntity = cardEntity.receivers.get(0);
					
					targetNameText.setText(receiverEntity.receiverName);
					targetCountryText.setText(receiverEntity.receiverCountry);
					if(receiverEntity.receiverJob != null){
						targetJobText.setVisibility(View.VISIBLE);
						targetJobText.setText(receiverEntity.receiverJob);
					}else{
						targetJobText.setVisibility(View.GONE);
					}
					if(receiverEntity.receiverPhotoPath != null){
						imageDownloader.displayImage(cardEntity.getOwnerProfileURL(), bitmapOptions, targetProfile, null);
					}
				}else{
					//여러명일 경우.
					singleTargetContainer.setVisibility(View.GONE);
					multiTargetContainer.setVisibility(View.VISIBLE);
					
					ArrayList<String> names = new ArrayList<String>();
					for(PostCardReceiverEntity receiver : cardEntity.receivers){
						names.add(receiver.receiverName);
					}
					
					ExpandableTextView multiName = (ExpandableTextView)findViewById(R.id.multiTargetName);
					multiName.setText(StringUtils.join(names.toArray(), ","));
				}
			}else{
				//Email만 있을경우에는 Email의 리스트를 그대로 보여줌.
				singleTargetContainer.setVisibility(View.GONE);
				multiTargetContainer.setVisibility(View.VISIBLE);
				ExpandableTextView multiName = (ExpandableTextView)findViewById(R.id.multiTargetName);
				multiName.setText(cardEntity.receiverEmails);
			}
			
		}else{
			//보낸사람의 정보를 보여준다.
			targetNameText.setText(cardEntity.ownerName);
			targetCountryText.setText(cardEntity.ownerCountry);
			if(cardEntity.ownerJob != null){
				targetJobText.setVisibility(View.VISIBLE);
				targetJobText.setText(cardEntity.ownerJob);
			}else{
				targetJobText.setVisibility(View.GONE);
			}
			if(cardEntity.ownerPhoto != null){
				imageDownloader.displayImage(cardEntity.getOwnerProfileURL(), bitmapOptions, targetProfile, null);
			}
		}
		
		Button btnReply = (Button)findViewById(R.id.btnReply);
		if(isSendCard || !isMyPage){
			btnReply.setVisibility(View.GONE);
		}else{
			btnReply.setVisibility(View.VISIBLE);
			
			btnReply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(sendMenu == null){
						LayoutInflater inflater = LayoutInflater.from(context);
						View menuView = inflater.inflate(R.layout.menu_select_card_img, null);
						sendMenu = new PopupWindow(menuView,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
						sendMenu.setOutsideTouchable(true);
						sendMenu.setBackgroundDrawable(new BitmapDrawable()) ;
					}
					
					sendMenu.dismiss();
					View mView = sendMenu.getContentView();
					
					ImageView arrow = (ImageView)mView.findViewById(R.id.topArrow);
					arrow.setVisibility(View.GONE);
					
					Button btnDraw = (Button)mView.findViewById(R.id.btnDraw);
					btnDraw.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							sendMenu.dismiss();
							
						
							
						}
					});
					
					Button btnSelectArtwork = (Button)mView.findViewById(R.id.btnSelectArtwork);
					btnSelectArtwork.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							sendMenu.dismiss();
							
							Intent intent = new Intent(context,PopupArtworkListActivity.class);
							startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
							
							
						}
					});
					
					mView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
					
					//int xOffset = (v.getWidth() + 20 - v.getMeasuredWidth());
					//int yOffset = (-clickView.getHeight() / 2 - view.getMeasuredHeight() - 20);

					sendMenu.setAnimationStyle(-1);
					sendMenu.showAsDropDown(v, -140, -130);
				}
			});
		}
		
		Button btnDelete = (Button)findViewById(R.id.btnDelete);
		final Button btnOpen = (Button)findViewById(R.id.btnOpen);
		if(isMyPage){
			btnDelete.setVisibility(View.VISIBLE);
			btnDelete.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Delete card");
					builder.setMessage(R.string.Remove_card_confirm);
					builder.setNegativeButton("Cancel", null);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							WaitDialog.showWailtDialog(context, false);
							PostCardDeleteTask task = new PostCardDeleteTask();
							task.execute(cardEntity.id,isSendCard ? "SENDED":"RECEIVED");
						}
						
					});
					builder.create().show();
					
				}
			});
			btnOpen.setVisibility(View.VISIBLE);
			if(cardEntity.opened){
				btnOpen.setSelected(true);
			}else{
				btnOpen.setSelected(false);
			}
			btnOpen.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					WaitDialog.showWailtDialog(context, false);
					if(cardEntity.opened){
						btnOpen.setSelected(false);
					}else{
						btnOpen.setSelected(true);
					}
					PostCardChangeTask task = new PostCardChangeTask();
					task.execute(cardEntity.id,cardEntity.opened ? "false" : "true");
				}
			});
			
		}else{
			btnDelete.setVisibility(View.GONE);
			btnOpen.setVisibility(View.GONE);
		}
		
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != RESULT_OK) return;
		
		if(requestCode == BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK){
			ArtworkEntity artwork = data.getParcelableExtra("selectedArtwork");
			Intent intent = new Intent();
			intent.putExtra("cardEntity", cardEntity);
			intent.putExtra("reply", true);
			intent.putExtra("selectedArtwork", artwork);
			setResult(RESULT_OK,intent);
			finish();
		}
	}



	class PostCardReadTask extends AsyncTask<String, Void, PostCardEntity>{

		@Override
		protected PostCardEntity doInBackground(String... params) {
			String cardId = params[0];
			
			return PostCardHandler.viewPostCard(cardId);
		}

		@Override
		protected void onPostExecute(PostCardEntity result) {
			super.onPostExecute(result);
			
			updateView(result);
		}
		
	}
	
	class PostCardDeleteTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String cardId = params[0];
			String listType = params[1];
			
			return PostCardHandler.removeFromList(cardId, listType);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			WaitDialog.hideWaitDialog();
			super.onPostExecute(result);
			
			Intent intent = new Intent();
			intent.putExtra("reload", true);
			setResult(RESULT_OK,intent);
			finish();
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}
	}
	
	class PostCardChangeTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String cardId = params[0];
			boolean open = Boolean.getBoolean(params[1]);
			
			return PostCardHandler.changeShareMode(cardId, open);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			cardEntity.opened = cardEntity.opened ? false : true;
			WaitDialog.hideWaitDialog();
			super.onPostExecute(result);
		}
	}
	
	
}
