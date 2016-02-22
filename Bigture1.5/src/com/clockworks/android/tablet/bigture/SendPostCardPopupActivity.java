package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PostCardReceiverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.PostCardHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SendPostCardPopupActivity extends Activity {
	private Context context;
	private ArtworkEntity artworkEntity;
	
	private EditText editEmail;
	private EditText editComment;
	private Button btnFriend;
	private ArrayList<FriendEntity> receivers;
	private boolean reply;
	private ImageView artworkImg;
	BitmapFactory.Options bitmapOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		context = this;
		
		bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmapOptions.inSampleSize = 3;
		setContentView(R.layout.popup_send_card);
		
		
		Intent intent = getIntent();
		this.artworkEntity = (ArtworkEntity)intent.getParcelableExtra("selectedArtwork");
		this.reply = intent.getBooleanExtra("reply", false);
		
		
		this.artworkImg = (ImageView)findViewById(R.id.artworkImg);
		
		BitmapDownloader imageDownloader = BitmapDownloader.getInstance();
		imageDownloader.displayImage(artworkEntity.getArtworkImageURL(), bitmapOptions, artworkImg, null);
		
		
		TextView artworkTitleView = (TextView)findViewById(R.id.artworkTitle);
		if(artworkEntity.title == null){
			
		}else{
			artworkTitleView.setText(artworkEntity.title);
		}
		
		TextView artworkOwnerView = (TextView)findViewById(R.id.artworkOwner);
		artworkOwnerView.setText(artworkEntity.ownerName);
		
		TextView artworkDateView = (TextView)findViewById(R.id.artworkDate);
		artworkDateView.setText(artworkEntity.createYear+"");
		
		editEmail = (EditText)findViewById(R.id.editEmail);
		editComment = (EditText)findViewById(R.id.editComment);
		
		btnFriend = (Button)findViewById(R.id.btnFriend);
		btnFriend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(receivers == null){
					receivers = new ArrayList<FriendEntity>();
				}
				
				Intent intent = new Intent(SendPostCardPopupActivity.this,SelectFriendPopupActivity.class);
				intent.putParcelableArrayListExtra("selectedFriends", receivers);
				intent.putExtra("popupTitle", "Send a postcard");
				intent.putParcelableArrayListExtra("likeUList", AccountManager.getInstance().entity.likeYous);
				intent.putParcelableArrayListExtra("groups", AccountManager.getInstance().entity.friendGroups);
				
				startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_FRIENDS);
				
				
			}
		});
	
		if(this.reply){
			//receiver가 자동으로 한명 추가되고 artwork를 동일한걸로 이용하는 형태임.
			//기존 card의 owner를 receiver로 지정해야하므로 PostCardReceiverEntity를 하나 만들어야한다.
			PostCardEntity originEntity = (PostCardEntity)intent.getParcelableExtra("originCard");
			FriendEntity receiver = new FriendEntity();
			receiver.index = originEntity.ownerId;
			receiver.nickName = originEntity.ownerName;
			receiver.job = originEntity.ownerJob;
			receiver.photoPath = originEntity.ownerPhoto;
			receiver.country = originEntity.ownerCountry;
			receiver.regionId = 0L;
			if(originEntity.ownerJob != null){
				receiver.pro = true;
			}else{
				receiver.pro = false;
			}
			
			if(receivers == null){
				receivers = new ArrayList<FriendEntity>();
			}
			receivers.add(receiver);
			
			btnFriend.setBackgroundResource(R.drawable.selector_popup_btn_friend_bg);
			Drawable icon = context.getResources().getDrawable(R.drawable.mybigture_popup_postcard_btn_friends_number_n_icon);
			icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
			btnFriend.setCompoundDrawables(icon, null, null, null);
			btnFriend.setText("" + 1);
		}
		
		Button btnSend = (Button)findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(receivers.size() == 0 && (editEmail.getText().toString() == null || editEmail.getText().toString().length() == 0)){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Notice");
					builder.setMessage("수신인을 한명이상 지정하세요.");
					builder.setPositiveButton("OK",null);
					builder.create().show();
					
				}else{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Send card");
					builder.setMessage("이 Postcard를 전송하시겠습니까?");
					builder.setNegativeButton("Cancel", null);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							
							WaitDialog.showWailtDialog(context, false);
							
							PostCardSendTask task = new PostCardSendTask();
							task.execute();
						}
						
					});
					builder.create().show();
				}
			}
		});
		
		Button btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Close card");
				builder.setMessage("작성중인 내용이 지워집니다. 나가시겠습니까?");
				builder.setNegativeButton("Cancel", null);
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(
							DialogInterface dialog,
							int which) {
						setResult(RESULT_CANCELED);
						finish();
					}
					
				});
				builder.create().show();
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK){
			if(requestCode == BigtureEnvironment.EVENT_CODE_SELECT_FRIENDS){
				ArrayList<FriendEntity> friends = data.getParcelableArrayListExtra("selectedFriends");
				receivers.clear();
				receivers.addAll(friends);
				
				int count = receivers.size();
				
				btnFriend.setBackgroundResource(R.drawable.selector_popup_btn_friend_bg);
				Drawable icon = context.getResources().getDrawable(R.drawable.mybigture_popup_postcard_btn_friends_number_n_icon);
				icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
				btnFriend.setCompoundDrawables(icon, null, null, null);
				btnFriend.setText("" + count);
				
			}
		}
		
	}
	
	class PostCardSendTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			String comment = editComment.getText().toString();
			String emails = editEmail.getText().toString();
			
			ArrayList<String> receiverStrs = new ArrayList<String>();
			for(FriendEntity entity : receivers){
				receiverStrs.add(entity.index);
			}
			
			return PostCardHandler.sendPostCard(artworkEntity.artworkId, comment, reply, emails, receiverStrs);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			WaitDialog.hideWaitDialog();
			
			setResult(RESULT_OK);
			finish();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			WaitDialog.hideWaitDialog();
			
			super.onCancelled();
		}
		
		
	}
	
	
}
