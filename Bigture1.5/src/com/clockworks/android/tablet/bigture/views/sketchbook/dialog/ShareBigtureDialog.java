package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.io.File;

import twitter4j.examples.tweets.GetRetweets;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.ArtworkType;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtworkRegisterTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtworkRegisterTask.ArtworkRegisterListener;
import com.clockworks.android.tablet.bigture.serverInterface.task.ContestHandleTask;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;





import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShareBigtureDialog extends Dialog  implements View.OnClickListener{
	Context context;
	String imagePath;
	String drawPath;

	
	
	EditText editTitle;
	EditText editDesc;
	
	Callback callback;
	String artworkIdx;
	String refId;
	
	int type;
	boolean puzzle;
	
	public ShareBigtureDialog(Context context, int type, String refId, String imagePath, String drawPath, Callback callback, boolean puzzle){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		
		this.setCanceledOnTouchOutside(false);
		
		this.type = type;
		this.context    = context;
		this.refId = refId;
		this.imagePath  = imagePath;
		this.drawPath   = drawPath;
		this.callback   = callback;
		this.puzzle = puzzle;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_ske_share_bigture);
		
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		findViewById(R.id.btnShare).setOnClickListener(this);
		
		
		editTitle = (EditText)findViewById(R.id.editTitle);
		editDesc  = (EditText)findViewById(R.id.editDesc);
		editTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				if (s.toString().length() > 20){
					String text = s.toString().substring(0, 20);
					editTitle.setText(text);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				String text = s.toString();
				try{

//					int length = text.length();
//					textBytes.setText("" + length + " / 20 chars");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v)
			{
				dismiss();
			}
		});
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 3;
		options.inDither = true;
		Bitmap bitmap = ImageUtil.loadFromFile(imagePath);
		
		ImageView imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		imageArtwork.setImageBitmap(bitmap);
		
		if (type == DrawingPurpose.CONTEST && refId != null){
			ImageView imageIcon = (ImageView)findViewById(R.id.imageShareIcon);
			imageIcon.setImageResource(R.drawable.sketchbook_func_share_popup_icon_contest);
			showContestTitle(refId);
		}
	}

	@Override
	public void onClick(View v){
		if (v.getId() == R.id.btnShare){
			String title = editTitle.getText().toString();
			String desc  = editDesc.getText().toString();

			if (title.trim().length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Register");
				builder.setMessage(context.getResources().getString(R.string.common_ph_title));
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				
				return;
			}else if (title.length() > 20){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Register");
				builder.setMessage("Title is too long");
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				
				return;
			}
			
			if (desc.trim().length() == 0){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Register");
				builder.setMessage(context.getResources().getString(R.string.sketchbook_ph_tell_about_artwork));
				builder.setNegativeButton("Ok", null);
				builder.create().show();
				
				return;
			}
			
			WaitDialog.showWailtDialog(context, false);
			
			ArtworkRegisterTask task = new ArtworkRegisterTask(new ArtworkRegisterListener(){
				@Override
				public void onComplete(String result){
					WaitDialog.hideWaitDialog();
					findViewById(R.id.btnShare).setEnabled(true);
					if (result != null){
						dismiss();
						Toast.makeText(context, context.getResources().getString(R.string.sketchbook_tp_shared_artworks), Toast.LENGTH_LONG).show();
						if (callback != null)
							callback.onFinish(ShareBigtureDialog.this, result);
					}else{
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Register");
						builder.setMessage("Fail to register.");
						builder.setNegativeButton("Ok", null);
						builder.create().show();
					}
				}
			});

			v.setEnabled(false);

			File bitmapFile = new File(imagePath);
			File drawFile = (drawPath == null ? null : new File(drawPath));
			String userIndex = AccountManager.getInstance().getUserIndex();
			int privacy = 0;

			ArtworkType aType = null;
			switch(type){
			case DrawingPurpose.NORMAL:
				aType = ArtworkType.NORMAL;
				break;
			case DrawingPurpose.ARTCLASS:
				if(puzzle){
					aType = ArtworkType.PUZZLE;
				}else{
					aType = ArtworkType.CLASS;
				}
				break;
			case DrawingPurpose.CONTEST:
				aType = ArtworkType.CONTEST;
				
				break;
			case DrawingPurpose.STORY:
				aType = ArtworkType.STORY;
				break;
			case DrawingPurpose.POSTCARD:
				aType = ArtworkType.POSTCARD;
				break;
			}
			
			task.register(userIndex, title, desc, refId, aType, privacy == 1 ? ShareType.PRIVATE : ShareType.PUBLIC, bitmapFile, drawFile);
		}
	}
	
	private void showContestTitle(String contestId){
		ContestHandleTask task = new ContestHandleTask(new ContestHandleTask.Callback(){	
			@Override
			public void onComplete(ContestHandleTask task, boolean result){
				WaitDialog.hideWaitDialog();
				
				if (result){
					TextView textView = (TextView)findViewById(R.id.shareTargetLabel);
					textView.setText(task.contestEntity.mainTitle);
				}else{
					BigtureEnvironment.showAlertMessage(context, 99);
					dismiss();
				}
			}
		});

		task.getContest(contestId);
	}
	
	public interface Callback{
		public void onFinish(ShareBigtureDialog dialog, String artwork);
	}
}
