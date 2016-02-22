package com.clockworks.android.tablet.bigture;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.my.ExpandableFriendGroupAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.FindFriendAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.SelectedMemberAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.SelectedMemberAdapter.GroupMemberListener;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

public class EditGroupMemberActivity extends Activity implements ExpandableFriendGroupAdapter.GroupSelectListener, 
	FindFriendAdapter.FriendSelectListener, GroupMemberListener {
	
	private FriendGroupEntity groupEntity;
	private ArrayList<FriendEntity> members;
	private ArrayList<FriendEntity> likeUList;
	private ArrayList<FriendGroupEntity> groups;
	private TextView leftTitleView;
	private TextView rightTitleView;
	
	private ExpandableListView leftExpandableList;
	private ListView leftList;
	
	private ListView rightList;
	private BitmapFactory.Options options;
	private Context context;
	private int currentPage;
	private EditText keywordEdit;
	
	private ExpandableFriendGroupAdapter expandableAdapter;
	private FindFriendAdapter findFriendAdapter;
	private SelectedMemberAdapter memberAdapter;
	private ProgressDialog progressDlg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		currentPage = 1;
		this.context = this;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		
		Intent intent = getIntent();
		this.groupEntity = (FriendGroupEntity)intent.getParcelableExtra("groupEntity");
		this.members = intent.getParcelableArrayListExtra("members");
		this.likeUList = intent.getParcelableArrayListExtra("likeUList");
		this.groups = intent.getParcelableArrayListExtra("groups");
		
		//첫번째 dummy에 member를 추가한다.
		FriendGroupEntity dummy = this.groups.get(0);
		dummy.members = this.likeUList;
		
		setContentView(R.layout.popup_edit_member);
		
		this.leftTitleView = (TextView)findViewById(R.id.leftTitle);
		this.leftTitleView.setText("Like U list (" + this.likeUList.size() + ")");
		this.rightTitleView = (TextView)findViewById(R.id.rightTitle);
		this.rightTitleView.setText("Group member list (" + this.members.size() + ")");
		
		this.leftExpandableList = (ExpandableListView)findViewById(R.id.leftExpandableList);
		this.expandableAdapter = new ExpandableFriendGroupAdapter(this, new ArrayList<FriendGroupEntity>(), this);
		
		this.leftExpandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
//				if(expandableAdapter.getChildrenCount(groupPosition) > 0)
//					leftExpandableList.expandGroup(groupPosition);
//				leftExpandableList.expandGroup(1);
				return false;
			}
		});
		
	
		this.leftList = (ListView)findViewById(R.id.leftList);
		this.findFriendAdapter = new FindFriendAdapter(context,this);
		this.leftList.setAdapter(findFriendAdapter);
		
		this.rightList = (ListView)findViewById(R.id.rightList);
		this.memberAdapter = new SelectedMemberAdapter(this,this.members,this,false);
		this.rightList.setAdapter(memberAdapter);
		
		this.keywordEdit = (EditText)findViewById(R.id.editKeyword);
		keywordEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				currentPage = 1;
				if (s.toString().length() > 1){
					leftList.setVisibility(View.VISIBLE);
					leftExpandableList.setVisibility(View.GONE);
					searchUser(s.toString());
				}else{
					leftList.setVisibility(View.GONE);
					leftExpandableList.setVisibility(View.VISIBLE);
					searchUser(null);
					
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});
		
		Button btnClose = (Button)findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {
			
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

				
//				Intent result = new Intent();
//				
//				ArrayList<String> addedMemebers = new ArrayList<String>();
//				ArrayList<String> removedMembers = new ArrayList<String>();
//				
//				for(FriendEntity entity :  memberAdapter.getAddedMembers()){
//					addedMemebers.add(entity.index);
//				}
//				
//				for(FriendEntity entity : memberAdapter.getRemovedMembers()){
//					removedMembers.add(entity.index);
//					
//				}
//				
//				result.putStringArrayListExtra("addedMembers", addedMemebers);
//				result.putStringArrayListExtra("removedMembers", removedMembers);
//				
//				setResult(RESULT_OK,result);
				
				//서버에 그룹멤버를 수정하는 request를 보낸다.
				progressDlg = ProgressDialog.show(context, "", "Save members .....", true);
				progressDlg.setCancelable(false);
				
				EditMemberTask task = new EditMemberTask();
				task.execute(groupEntity.index);
				
			}
		});
		
		
		findGroupMember();
		searchUser(null);
		
	}

	public void findGroupMember(){
		//서버에서 각 group별 member정보를 얻어온다.
		GroupAndMemberTask task = new GroupAndMemberTask();
		task.execute();
	}
	
	public void searchUser(String keyword){
		if(keyword == null){
			//Group view를 보여준다.
			this.expandableAdapter.notifyDataSetChanged();
		}else{
			//서버에서 keyword로 검색해서 결과를 가져온다.
			FindFriendTask task = new FindFriendTask();
			task.execute(keyword);
		}
		
	}
	
	public void onSaveCompleted(){
		setResult(RESULT_OK);
		finish();
	}
	
	class EditMemberTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String groupId = params[0];
			
			ArrayList<String> addedMembers = new ArrayList<String>();
			ArrayList<String> removedMembers = new ArrayList<String>();
			
			for(FriendEntity entity :  memberAdapter.getAddedMembers()){
				addedMembers.add(entity.index);
			}
			
			for(FriendEntity entity : memberAdapter.getRemovedMembers()){
				removedMembers.add(entity.index);
				
			}
			
			return FriendHandler.editGroupMember(groupId, addedMembers, removedMembers);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDlg.dismiss();
			if(result)
				onSaveCompleted();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}
		
		
		
	}
	
	
	
	class FindFriendTask extends AsyncTask<String, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(String... params) {
			return FriendHandler.findInFriends(params[0], 1);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			super.onPostExecute(result);
			findFriendAdapter.setUsers(result);
			findFriendAdapter.notifyDataSetChanged();
		}
		
	}
	
	class GroupAndMemberTask extends AsyncTask<Void, Void, ArrayList<FriendGroupEntity>>{

		@Override
		protected ArrayList<FriendGroupEntity> doInBackground(Void... params) {
			return FriendHandler.findGroupAndMembers();
		}

		@Override
		protected void onPostExecute(ArrayList<FriendGroupEntity> result) {
			super.onPostExecute(result);
			
			
			FriendGroupEntity dummy = new FriendGroupEntity();
			dummy.index = "-1";
			dummy.groupName = "All";
			dummy.friendCount = likeUList.size();
			dummy.members = likeUList;
			
			ArrayList<FriendGroupEntity> entities = new ArrayList<FriendGroupEntity>();
			entities.add(dummy);
			entities.addAll(result);
			
			expandableAdapter.setGroups(entities);
			leftExpandableList.setAdapter(expandableAdapter);
			expandableAdapter.notifyDataSetChanged();
			
		}
		
	}

	@Override
	public void selectGroup(FriendGroupEntity group, boolean selected) {
		if(selected){
			boolean duplicated = memberAdapter.addMembers(group.members);
			if(duplicated == true){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Duplicated");
				builder.setMessage("Friends in selected group are duplicated!");
				builder.setPositiveButton("OK",null);
				builder.show();
			}
		}else{
			for(FriendEntity f : group.members){
				memberAdapter.removeMember(f, false);
			}
		}
		
	}

	@Override
	public boolean selectFriend(FriendEntity friend, boolean selected) {
		boolean duplicated = false;
		ArrayList<FriendEntity> f = new ArrayList<FriendEntity>();
		f.add(friend);
		if(selected){
			duplicated = memberAdapter.addMembers(f);
			if(duplicated == true){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Duplicated");
				builder.setMessage("Selected friend is duplicated!");
				builder.setPositiveButton("OK",null);
				builder.show();
			}
		}else{
			memberAdapter.removeMember(friend, false);
		}
		
		return duplicated;
		
	}

	
	
	@Override
	public void onChangeGroupMember(int memberCount) {
		this.rightTitleView.setText("Group member list (" + memberCount + ")");
	}

	@Override
	public void clickGroup(int groupPosition) {
		if(this.leftExpandableList.isGroupExpanded(groupPosition)){
			this.leftExpandableList.collapseGroup(groupPosition);
		}else{
			this.leftExpandableList.expandGroup(groupPosition);
		}
		
	}
	
	
}
