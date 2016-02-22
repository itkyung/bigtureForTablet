package com.clockworks.android.tablet.bigture;


import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtworkHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class PopupEditArtworkActivity extends Activity implements View.OnClickListener {
	private Context context;
	private ArtworkEntity artwork;
	BitmapFactory.Options options = null;
	private EditText titleEdit;
	private EditText commentEdit;
	private View artworkPrivate;
	private View artworkPublic;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		this.context = this;
		
		setContentView(R.layout.popup_edit_artwork);
		
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 1;
		
		
		Intent intent = getIntent();
		this.artwork = (ArtworkEntity)intent.getParcelableExtra("artwork");
		
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(titleEdit.getText().toString().length() == 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Alert");
					builder.setMessage(R.string.label_error_title);
					builder.setPositiveButton("Ok", null);
					builder.create().show();
					return;
				}else if(commentEdit.getText().toString().length() == 0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Alert");
					builder.setMessage(R.string.label_error_comment);
					builder.setPositiveButton("Ok", null);
					builder.create().show();
					return;
				}
				
				
				WaitDialog.showWailtDialog(context, false);
				SubmitTask task = new SubmitTask();
				task.execute();
			}
		});
		
		ImageView artworkImg = (ImageView)findViewById(R.id.artworkImg);
		String imageUrl = artwork.getThumbnailURL();
		if(imageUrl != null){
			BitmapDownloader.getInstance().displayImage(imageUrl, options, artworkImg, null);
		}
		
		
		titleEdit = (EditText)findViewById(R.id.editArtworkTitle);
		commentEdit = (EditText)findViewById(R.id.editArtworkComment);
		
		
		artworkPrivate = findViewById(R.id.artworkPrivate);
		artworkPrivate.setOnClickListener(this);
		
		artworkPublic = findViewById(R.id.artworkPublic);
		artworkPublic.setOnClickListener(this);
	
		titleEdit.setText(artwork.title);
		commentEdit.setText(artwork.comment);
		
		updateView();
	}
	
	private void updateView(){
		
		
		if(artwork.opend){
			artworkPrivate.setSelected(false);
			artworkPublic.setSelected(true);
		}else{
			artworkPrivate.setSelected(true);
			artworkPublic.setSelected(false);
		}
		
	}

	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.artworkPrivate){
			artwork.opend = false;
		}else{
			artwork.opend = true;
		}
		
		updateView();
	}
	
	class SubmitTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			String title = titleEdit.getText().toString();
			String comment = commentEdit.getText().toString();
			String shareType = artwork.opend ? "PUBLIC" : "PRIVATE";
			
			artwork.title = title;
			artwork.comment = comment;
			
			return ArtworkHandler.editArtworks(artwork.artworkId, title, comment, shareType);
		}

		@Override
		protected void onCancelled() {
			
			super.onCancelled();
			
			WaitDialog.hideWaitDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
		
			super.onPostExecute(result);
			WaitDialog.hideWaitDialog();
			
			if(result){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Success");
				builder.setMessage(R.string.message_submit);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent newIntent = new Intent();
						newIntent.putExtra("artwork", artwork);
						setResult(RESULT_OK,newIntent);
						finish();
					}
					
				});
				builder.create().show();
				
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("Ok", null);
				builder.create().show();
			}
		}
		
		
		
	}
	
}
