package com.clockworks.android.tablet.bigture.views.sketchbook.dialog;

import java.io.File;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.ArtworkType;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.PuzzlePart;
import com.clockworks.android.tablet.bigture.common.ShareType;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.ArtClassHandleTask;
import com.clockworks.android.tablet.bigture.serverInterface.task.StoryHandleTask;
import com.clockworks.android.tablet.bigture.utils.image.ImageUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.sketchbook.DrawingPurpose;




import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Biture Story와 Class에 저장하는 dialog 
 * @author bizwave
 *
 */
public class ShareBigtureDialog2 extends Dialog{
	Context context;
	BigtureEnvironment.DialogListener listener;

	DrawingPurpose purpose;
	String imagePath;
	String drawPath;
	
	EditText editTitle;
	EditText editDesc;

	ImageView imageArtwork;
	
	
	Button btnShare;
	
	StoryEntity storyEntity;
	StoryPageEntity storyPageEntity;
	int type;
	boolean puzzle;
	
	public ShareBigtureDialog2(Context context, BigtureEnvironment.DialogListener listener, DrawingPurpose purpose, String imagePath, String drawPath, boolean puzzle){
		super(context, android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		
		this.setCanceledOnTouchOutside(false);
		
		this.context = context;
		this.listener = listener;
		this.purpose  = purpose;
		
		this.imagePath = imagePath;
		this.drawPath  = drawPath;
		this.type = purpose.reason;
		this.puzzle = puzzle;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_ske_share_bigture2);
		
		LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.MATCH_PARENT;
		getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
		
		editTitle = (EditText)findViewById(R.id.editTitle);
		editTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				if (s.toString().length() > 20)
				{
					String text = s.toString().substring(0, 20);
					editTitle.setText(text);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				String text = s.toString();
				try{
//					int length = text.length();
//					textBytes.setText("" + length + " / 20 chars");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});

		editDesc  = (EditText)findViewById(R.id.editDesc);

		imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		options.inDither = true;
		Bitmap bitmap = ImageUtil.loadFromFile(imagePath);
		
		ImageView imageArtwork = (ImageView)findViewById(R.id.imageArtwork);
		imageArtwork.setImageBitmap(bitmap);

		
		btnShare = (Button)findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
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

				v.setEnabled(false);
				ArtworkRegisterTask task = new ArtworkRegisterTask();
				task.execute(title, desc);
			}
		});
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		// Bigture story
		if (purpose.reason == DrawingPurpose.STORY){
			ImageView imageIcon = (ImageView)findViewById(R.id.imageShareIcon);
			imageIcon.setImageResource(R.drawable.sketchbook_func_share_popup_icon_story);
			
			StoryHandleTask task = new StoryHandleTask(new StoryHandleTask.Callback(){
				@Override
				public void onComplete(StoryHandleTask handler, boolean result){
					TextView textView = (TextView)findViewById(R.id.shareTargetLabel);
					if (result){
						textView.setText(handler.storyEntity.title);
					}
				}
			});

			task.readStory(purpose.storyId);
		}
		// Class
		else if (purpose.reason == DrawingPurpose.ARTCLASS){
			ImageView imageIcon = (ImageView)findViewById(R.id.imageShareIcon);
			imageIcon.setImageResource(R.drawable.sketchbook_func_share_popup_icon_class);
			
			if(purpose.puzzle){
				//해당 Puzzle이 속한 class의 정보를 얻는다.
				ArtClassHandleTask task = new ArtClassHandleTask(new ArtClassHandleTask.Callback(){
					@Override
					public void onComplete(ArtClassHandleTask task, boolean result){
						if (result){
							TextView textView = (TextView)findViewById(R.id.shareTargetLabel);
							textView.setText(task.artClass.className);
							
							String title = task.artClass.className;
							if (title.length() > 20)
								title.substring(0, 20);
							
							editTitle.setText(title);
						}
					}
				});
				
				task.getArtClass(purpose.classId);
				
			}else{
				//해당 Class의 정보를 얻는다.
				ArtClassHandleTask task = new ArtClassHandleTask(new ArtClassHandleTask.Callback(){
					@Override
					public void onComplete(ArtClassHandleTask task, boolean result){
						if (result){
							TextView textView = (TextView)findViewById(R.id.shareTargetLabel);
							textView.setText(task.artClass.className);
							
							String title = task.artClass.className;
							if (title.length() > 20)
								title.substring(0, 20);
							
							editTitle.setText(title);
							
						}
					}
				});
				
				task.getArtClass(purpose.classId);
			}
		}
	}
	
	class ArtworkRegisterTask extends AsyncTask<String,Void,Boolean>{
		@Override
		protected Boolean doInBackground(String... params){
			String title = params[0];
			String comment = params[1];
			
			String userIdx = AccountManager.getInstance().getUserIndex();
			File imageFile = new File(imagePath);
			File drawFile = (drawPath == null ? null : new File(drawPath));
			
			int reason = 0;
			String refId = null;
			
			ArtworkType aType = null;
			switch(reason){
			case DrawingPurpose.NORMAL:
				aType = ArtworkType.NORMAL;
				break;
			case DrawingPurpose.ARTCLASS:
				if(puzzle){
					aType = ArtworkType.PUZZLE;
				}else{
					aType = ArtworkType.CLASS;
				}
				refId = purpose.classId;
				break;
			case DrawingPurpose.CONTEST:
				aType = ArtworkType.CONTEST;
				refId = purpose.contestId;
				break;
			case DrawingPurpose.STORY:
				aType = ArtworkType.STORY;
				refId = purpose.storyId;
				break;
			case DrawingPurpose.POSTCARD:
				aType = ArtworkType.POSTCARD;
				refId = purpose.postcardId;
				break;
			}
			
			
			String artworkId = ArtworkHandler.registerArtwork(userIdx, title, comment, refId, aType, ShareType.PUBLIC, imageFile, drawFile);

			if (artworkId == null)
				return false;

			if (purpose.reason == DrawingPurpose.STORY){
				String storyId = purpose.storyId;
				int page = purpose.pageNo;
				String pageId = purpose.pageId;
	
				if (purpose.afterReading)
					return StoryHandler.registerAfterReading(userIdx, storyId, artworkId);
				else
					return StoryHandler.registerArtwork(userIdx, storyId, pageId, artworkId);
			}else if (purpose.reason == DrawingPurpose.ARTCLASS){
				String classId = purpose.classId;
				String puzzleId = purpose.puzzleId;
				PuzzlePart part = purpose.puzzlePart;
				
				if(purpose.puzzle){
					return ArtClassHandler.registPuzzleArtwork(classId, puzzleId, part.name(),artworkId);
				}else{
					return ArtClassHandler.registArtwork(classId, artworkId);
				}
			}
			
			return false;
		}

		@Override
		protected void onPreExecute()
		{
			WaitDialog.showWailtDialog(context, false);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			btnShare.setEnabled(true);
			WaitDialog.hideWaitDialog();

			if (result)
			{
				if (listener != null)
					listener.onCompleted();

				dismiss();
				Toast.makeText(context, context.getResources().getString(R.string.sketchbook_tp_shared_artworks), Toast.LENGTH_LONG).show();
			}
			else
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Register");
				builder.setMessage("Fail to register.");
				builder.setNegativeButton("Ok", null);
				builder.create().show();
			}
			
			super.onPostExecute(result);
		}
		
	}
}
