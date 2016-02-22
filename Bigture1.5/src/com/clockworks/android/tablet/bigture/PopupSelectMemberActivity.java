package com.clockworks.android.tablet.bigture;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.fragment.artclass.SelectMemberFragment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


public class PopupSelectMemberActivity extends AbstractBigtureActivity {
	private Context context;
	private SelectMemberFragment fragment;
	private String classId;
	
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		
		this.context = this;
		
		setContentView(R.layout.popup_select_members);
		
		Intent intent = getIntent();
		this.classId = intent.getStringExtra("classId");
		ArrayList<SimpleUserEntity> members = intent.getParcelableArrayListExtra("members");
		
		ArrayList<FriendEntity> friends = new ArrayList<FriendEntity>();
		for(SimpleUserEntity member : members){
			FriendEntity friend = new FriendEntity();
			friend.index = member.index;
			friend.nickName = member.name;
			friend.job = member.jobName;
			friend.country = member.country;
			friend.photoPath = member.photo;
			if(friend.job != null){
				friend.pro = true;
			}else{
				friend.pro = false;
			}
			friends.add(friend);
		}
		
		Button btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		
		Button btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				ArrayList<FriendEntity> addedMembers = fragment.getAddedMembers();
				if(addedMembers != null && addedMembers.size() > 0){
					WaitDialog.showWailtDialog(context, false);
					SaveEditedMemberTask task = new SaveEditedMemberTask();
					task.execute(addedMembers);
					
				}else{
					setResult(RESULT_CANCELED);
					finish();
				}
				
			}
		});
		
		this.fragment = SelectMemberFragment.newInstance(friends == null ? new ArrayList<FriendEntity>() : friends, 
				AccountManager.getInstance().entity.likeYous, AccountManager.getInstance().entity.friendGroups,true);
		
		getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragment).commitAllowingStateLoss();	
		
	}
	
	
	
	class SaveEditedMemberTask extends AsyncTask<ArrayList<FriendEntity>, Void, Boolean>{

		@Override
		protected Boolean doInBackground(ArrayList<FriendEntity>... params) {
			return ArtClassHandler.addClassMembers(classId,params[0]);
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
			setResult(RESULT_OK);
			finish();
		}
		
	}
	
}
