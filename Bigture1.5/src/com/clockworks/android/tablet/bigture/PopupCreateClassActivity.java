package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.ClassShare;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassCreateFragment;
import com.clockworks.android.tablet.bigture.fragment.artclass.SelectMemberFragment;
import com.clockworks.android.tablet.bigture.fragment.artclass.ArtClassCreateFragment.ChangeShareOptionListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopupCreateClassActivity extends FragmentActivity implements ChangeShareOptionListener {
	
	private Context context;
	private Button btnCancel;
	private Button btnOk;
	private TextView titleLabel;
	private ClassShare currentOption;
	private int currentFragmentIdx;
	private ArtClassEntity classEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;
		this.currentOption = ClassShare.OPEN;
		
		setContentView(R.layout.popup_create_artclass);
		
		titleLabel = (TextView)findViewById(R.id.titleLabel);
		
		currentFragmentIdx = 1;
		
		btnCancel = (Button)findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				if( currentFragmentIdx == 1){
					setResult(Activity.RESULT_CANCELED);
					finish();
				}else if(currentFragmentIdx == 2){
					backToFirst();
				}
			}
		});
		
		btnOk = (Button)findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v){
				if( (currentOption.equals(ClassShare.OPEN) && currentFragmentIdx == 1) 
						|| (currentOption.equals(ClassShare.EXCLUSIVE) && currentFragmentIdx == 2)){
					//생성을 시도한다.
					submitClasss();
				}else if(currentFragmentIdx == 1){
					//다음으로 이동시킨다.
					moveToSelectFriends();
				}
				
			}
		});
		
		ArtClassCreateFragment fragment = new ArtClassCreateFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,fragment).commitAllowingStateLoss();
		
	}

	

	@Override
	public void onChangeShareOption(ClassShare option) {
		this.currentOption = option;
		
		switch (option) {
		case OPEN:
			btnOk.setText("OK");
			
			break;
		case EXCLUSIVE:
			btnOk.setText("Next");
		
			break;
		default:
			break;
		}
		
	}
	
	public void moveToSelectFriends(){
		
		//첫번째 fragment에서 classEntity를 가져와서 저장시킨다.
		ArtClassCreateFragment fragment = (ArtClassCreateFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		ArtClassEntity entity = fragment.makeClassEntity();
		if(entity == null){
			return;
		}
		
		this.classEntity = entity;
		ArrayList<FriendEntity> selectedFriends = entity.members;
		
		SelectMemberFragment memberFragment = SelectMemberFragment.newInstance(selectedFriends == null ? new ArrayList<FriendEntity>() : selectedFriends, 
				AccountManager.getInstance().entity.likeYous, AccountManager.getInstance().entity.friendGroups,false);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, memberFragment).commitAllowingStateLoss();		
		
		
		currentFragmentIdx = 2;
		btnCancel.setText("Previous");
		btnOk.setText("OK");
	}
	
	public void backToFirst(){
		
		SelectMemberFragment memberFragment =  (SelectMemberFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
		this.classEntity.members = memberFragment.getSelectedFriends();
		
		ArtClassCreateFragment fragment = new ArtClassCreateFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commitAllowingStateLoss();		
		
		
		currentFragmentIdx = 1;
		btnCancel.setText("Cancel");
		btnOk.setText("Next");
	}
	
	private void submitClasss(){
		if(currentFragmentIdx == 1){
			ArtClassCreateFragment fragment = (ArtClassCreateFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
			ArtClassEntity entity = fragment.makeClassEntity();
			if(entity == null){
				return;
			}
			this.classEntity = entity;
		}else{
			SelectMemberFragment memberFragment =  (SelectMemberFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
			this.classEntity.members = memberFragment.getSelectedFriends();
		}
		
		
		WaitDialog.showWailtDialog(this, false);
		CreateClassTask task = new CreateClassTask();
		task.execute(AccountManager.getUserIdx());
	}
	
	class CreateClassTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String userId = params[0];
			return ArtClassHandler.createArtClass(userId, classEntity);
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
		
		
		
	}
	
	
	public ArtClassEntity getClassEntity(){
		return this.classEntity;
	}
}
