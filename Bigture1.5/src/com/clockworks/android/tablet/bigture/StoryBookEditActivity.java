package com.clockworks.android.tablet.bigture;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CommonCoverEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryPageEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;
import com.clockworks.android.tablet.bigture.views.story.page.BodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.EditBodyPage;
import com.clockworks.android.tablet.bigture.views.story.page.EditCoverPage;
import com.clockworks.android.tablet.bigture.views.story.page.ShareStoryButton;
import com.clockworks.android.tablet.bigture.views.story.page.EditBodyPage.EditBodyPageListener;
import com.clockworks.android.tablet.bigture.views.story.page.EditCoverPage.EditCoverPageListener;

public class StoryBookEditActivity extends AbstractStoryBookActivity implements EditCoverPageListener,EditBodyPageListener{
	private ShareStoryButton btnShare;
	private View btnDraft;
	private Button btnAddPage;
	
	@Override
	protected void initView() {
		this.currentMode = EDIT_MODE;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_story_book_edit);
		
		this.btnAddPage = (Button)findViewById(R.id.btnAddPage);
		this.btnAddPage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				totalPage = totalPage+1;
				seekbar.setMax(totalPage-1);
				
				pagerPageCount++;
				storyEntity.pageCount++;
				StoryPageEntity pageEntity = new StoryPageEntity();
				pageEntity.pageNo = storyEntity.pageCount;//1부터 시작함. 
				
				pageList.add(pageEntity);
				viewPager.getAdapter().notifyDataSetChanged();
				
				viewPager.setCurrentItem(getLastPosition(),true);
				
			}
		});
		
		this.btnShare = (ShareStoryButton)findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				syncStoryEntity();
				
				ShareStoryTask task = new ShareStoryTask();
				task.execute();
			}
		});
		
		this.btnDraft = findViewById(R.id.btnDraft);
		btnDraft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				syncStoryEntity();
				if(storyEntity.title == null || "".equals(storyEntity.title)){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("Notice	");
					builder.setMessage(R.string.story_ph_story_title);
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
					builder.create().show();
					
				}else{
					SaveStoryTask task = new SaveStoryTask();
					task.execute();
				}
			}
		});
		
		LayoutInflater inflater = LayoutInflater.from(this);
		coverPage = (EditCoverPage)inflater.inflate(R.layout.view_bigture_story_cover_edit, null);
		
		if(getIntent().hasExtra("storyEntity")){
			this.storyEntity  = getIntent().getParcelableExtra("storyEntity");
			
		}else{
			this.storyEntity = new StoryEntity();
			
		}
		((EditCoverPage)coverPage).initView(this.storyEntity,this);
		checkShareBtn();
	}
	
	public void checkShareBtn(){
		if(canShare()){
			btnShare.setEnabled(true);
		}else{
			btnShare.setEnabled(false);
		}
	}
	
	/**
	 * 아래 조건에 속하면 share불가.
	 * 1. title을 적지않음.
	 * 2. 페이지가 하나도 없음.
	 * 3. 페이지에 설명을 적지 않았음.
	 * @return
	 */
	public boolean canShare(){
		syncStoryEntity();
		
		if(storyEntity.title == null || "".equals(storyEntity.title)){
			return false;
		}
		
		if(storyEntity.pageCount == 0 || pageList == null)
			return false;
		
		for(StoryPageEntity p : pageList){
			if(p.description == null || p.description.length() == 0)
				return false;
		}
		
		
		return true;
	}
	
/**
 * 내부에 각각 입력한 내용을 기초로 해서 storyEntity에 값을 넣는다.
 */
	private void syncStoryEntity(){
		((EditCoverPage)coverPage).syncData();
		//page는 이미 데이타 입력시점에 자동으로 동기화됨.
	}
	
	
	
	@Override
	public void onPageInitCompleted() {
		super.onPageInitCompleted();
		checkShareBtn();
	}

	@Override
	protected BodyPage initBodyPageView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		EditBodyPage bodyPage  = (EditBodyPage)inflater.inflate(R.layout.view_bigture_story_body_edit, null);
		bodyPage.setListener(this);
		return bodyPage;
	}

	@Override
	protected void initAfterReadPage(StoryEntity storyEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickAddPage() {
		pagerPageCount++;
		storyEntity.pageCount++;
		StoryPageEntity pageEntity = new StoryPageEntity();
		pageEntity.pageNo = storyEntity.pageCount;//1부터 시작함. 
		
		pageList.add(pageEntity);
		viewPager.getAdapter().notifyDataSetChanged();
		
		viewPager.setCurrentItem(getLastPosition(),true);
		checkShareBtn();
	}
	
	private int getLastPosition(){
		int lastPosition = 1 + pageList.size() - 1;
		return lastPosition;
	}

	@Override
	public void onClickNextPage() {
		int currentItem = viewPager.getCurrentItem();
		if(currentItem < getLastPosition()){
			viewPager.setCurrentItem(currentItem+1,true);
		}
	}
	
	

	@Override
	public void onClickPrevPage() {
		int currentItem = viewPager.getCurrentItem();
		if(currentItem > 0)
			viewPager.setCurrentItem(currentItem-1,true);
	}
	
	

	@Override
	public void onDescChanged() {
		checkShareBtn();
	}

	@Override
	public void onTakePhoto() {
		String tempPath = FileUtil.getTempPath(context) + "/taken.image";
		File file = new File(tempPath);
		
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_TAKE_PICTURE);
	}

	@Override
	public void onSelectFromGallery() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_PICKUP_PICTURE);
	}

	@Override
	public void onSelectFromArtworks() {
		Intent intent = new Intent(context,PopupArtworkListActivity.class);
		startActivityForResult(intent, BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK);
	}

	@Override
	public void onSelectRecentArtworks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSelectFromStory() {
		// TODO Auto-generated method stub
		
	}

	public void showCoverImage(CommonCoverEntity coverEntity){
		((EditCoverPage)coverPage).showCoverImage(coverEntity);
	}
	
	public void uploadCoverImage(String filePath){
		((EditCoverPage)coverPage).uploadCoverImage(filePath);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Uri imageUri = null;
		Intent intent = null;
		if(resultCode == Activity.RESULT_OK){
			switch(requestCode){
			case BigtureEnvironment.EVENT_CODE_SELECT_ARTWORK:
				ArtworkEntity entity = data.getParcelableExtra("selectedArtwork");
				CommonCoverEntity cover = new CommonCoverEntity();
				cover.coverFromArtworks = true;
				cover.coverImageId = entity.artworkId;
				cover.coverImagePath = entity.imagePath;
				cover.coverThumbPath = entity.thumbPath;
				showCoverImage(cover);
				break;
			case BigtureEnvironment.REQ_CODE_TAKE_PICTURE:
				String tempPath = FileUtil.getTempPath(context) + "/taken.image";
				File file = new File(tempPath);
				imageUri = Uri.fromFile(file);

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				break;
			case BigtureEnvironment.REQ_CODE_CROP_PICTURE:
				final Bundle bundle = data.getExtras();
				
				String filePath = bundle.getString("imagePath");
				String newFilePath = FileUtil.getTempPath(context) + File.separator + System.currentTimeMillis() + ".image";
				try{
					FileUtil.moveFile(filePath, newFilePath);
					uploadCoverImage(newFilePath);
				}catch (Exception e){
					e.printStackTrace();
				}		
				
				break;
			case BigtureEnvironment.REQ_CODE_PICKUP_PICTURE:
				imageUri = data.getData();

				intent = new Intent(context, CropActivity.class);
				intent.putExtra("imageUri", imageUri);
				intent.putExtra("returnImageWidth", 1024);
				intent.putExtra("returnImageHeight", 768);
				intent.putExtra("aspectX", 1024);
				intent.putExtra("aspectY", 768);
				
				startActivityForResult(intent, BigtureEnvironment.REQ_CODE_CROP_PICTURE);
				
				break;
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	class SaveStoryTask extends AsyncTask<Void, Void, Boolean>{

		@Override
		protected Boolean doInBackground(Void... params) {
			return StoryHandler.saveStory(storyEntity);
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
				setResult(Activity.RESULT_OK);
				finish();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}
	}
	
	class ShareStoryTask extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected Boolean doInBackground(Void... params) {
			return StoryHandler.shareStory(storyEntity);
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
				setResult(Activity.RESULT_OK);
				finish();
			}else{
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Error	");
				builder.setMessage(R.string.error_common);
				builder.setPositiveButton("OK", null);
				builder.create().show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			WaitDialog.showWailtDialog(context, false);
		}
	}
	

}
