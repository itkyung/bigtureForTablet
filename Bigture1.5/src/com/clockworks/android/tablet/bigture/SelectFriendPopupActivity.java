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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 친구들중에 몇명을 고르는 activity popup
 * @author bizwave
 *
 */
public class SelectFriendPopupActivity extends Activity implements ExpandableFriendGroupAdapter.GroupSelectListener, 
FindFriendAdapter.FriendSelectListener, GroupMemberListener{
	private ArrayList<FriendEntity> selectedFriends;
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
	private SelectedMemberAdapter selectedFriendsAdapter;
	private ProgressDialog progressDlg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		this.context = this;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		
		Intent intent = getIntent();
		this.selectedFriends = new ArrayList<FriendEntity>();
		ArrayList<FriendEntity> friends = intent.getParcelableArrayListExtra("selectedFriends");
		this.selectedFriends.addAll(friends);
				
		this.likeUList = intent.getParcelableArrayListExtra("likeUList");
		if(this.likeUList == null)
			this.likeUList = new ArrayList<FriendEntity>();
		
		ArrayList<FriendGroupEntity> gs = intent.getParcelableArrayListExtra("groups");
		
		this.groups = new ArrayList<FriendGroupEntity>();
		FriendGroupEntity dummy = new FriendGroupEntity();
		dummy.index = "-1";
		dummy.groupName = "All";
		dummy.friendCount = likeUList.size();
		dummy.members = this.likeUList;
		this.groups.add(dummy);
		if(gs != null){
			this.groups.addAll(gs);
		}
		
		String titleText = intent.getStringExtra("popupTitle");
		
		
		setContentView(R.layout.popup_edit_member);
		
		this.leftTitleView = (TextView)findViewById(R.id.leftTitle);
		this.leftTitleView.setText("Like U list (" + this.likeUList.size() + ")");
		this.rightTitleView = (TextView)findViewById(R.id.rightTitle);
		this.rightTitleView.setText("The list of recipients (" + this.selectedFriends.size() + ")");
		TextView titleView = (TextView)findViewById(R.id.titleLabel);
		titleView.setText( titleText);
		
		this.leftExpandableList = (ExpandableListView)findViewById(R.id.leftExpandableList);
		this.expandableAdapter = new ExpandableFriendGroupAdapter(this, new ArrayList<FriendGroupEntity>(), this);
		
		this.leftExpandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				return false;
			}
		});
		
	
		this.leftList = (ListView)findViewById(R.id.leftList);
		this.findFriendAdapter = new FindFriendAdapter(context,this);
		this.leftList.setAdapter(findFriendAdapter);
		
		this.rightList = (ListView)findViewById(R.id.rightList);
		this.selectedFriendsAdapter = new SelectedMemberAdapter(this,this.selectedFriends,this,false);
		this.rightList.setAdapter(selectedFriendsAdapter);
		
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
		btnClose.setText("Previous");
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
				Intent intent = new Intent();
				intent.putParcelableArrayListExtra("selectedFriends", selectedFriendsAdapter.getSelectedMembers());
				setResult(RESULT_OK,intent);
				finish();
			}
		});
		
		findGroupMember();
		searchUser(null);
		
		
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
			boolean duplicated = selectedFriendsAdapter.addMembers(group.members);
			if(duplicated == true){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Duplicated");
				builder.setMessage("Friends in selected group are duplicated!");
				builder.setPositiveButton("OK",null);
				builder.show();
			}
		}else{
			for(FriendEntity f : group.members){
				selectedFriendsAdapter.removeMember(f, false);
			}
		}
		
	}

	@Override
	public boolean selectFriend(FriendEntity friend, boolean selected) {
		boolean duplicated = false;
		ArrayList<FriendEntity> f = new ArrayList<FriendEntity>();
		f.add(friend);
		if(selected){
			duplicated = selectedFriendsAdapter.addMembers(f);
			if(duplicated == true){
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Duplicated");
				builder.setMessage("Selected friend is duplicated!");
				builder.setPositiveButton("OK",null);
				builder.show();
			}
		}else{
			selectedFriendsAdapter.removeMember(friend, false);
		}
		
		return duplicated;
		
	}

	
	
	@Override
	public void onChangeGroupMember(int memberCount) {
		this.rightTitleView.setText("The list of recipients (" + memberCount + ")");
	}

	@Override
	public void clickGroup(int groupPosition) {
		if(this.leftExpandableList.isGroupExpanded(groupPosition)){
			this.leftExpandableList.collapseGroup(groupPosition);
		}else{
			this.leftExpandableList.expandGroup(groupPosition);
		}
		
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
	
	
}
