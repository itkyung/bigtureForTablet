package com.clockworks.android.tablet.bigture.fragment.user;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.clockworks.android.tablet.bigture.EditGroupMemberActivity;
import com.clockworks.android.tablet.bigture.EditGroupPopupActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.adapter.my.LikeFriendAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.LikeGroupAdapter;
import com.clockworks.android.tablet.bigture.adapter.my.LikeGroupAdapter.LikeGroupListListener;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendGroupEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.FriendListTask;
import com.clockworks.android.tablet.bigture.views.like.MapIcon;




import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LikeListFragment extends Fragment implements LikeFriendAdapter.LikeFriendListListener,LikeGroupListListener {
	final int TAB_LIKEU = 0;
	final int TAB_LIKEME = 1;
	final int counterIds[] = {-1,R.id.counter001, R.id.counter002, R.id.counter003, R.id.counter004, R.id.counter005, 
			R.id.counter006, R.id.counter007, R.id.counter008, R.id.counter009, R.id.counter010, R.id.counter011, 
			R.id.counter012, R.id.counter013,	R.id.counter014, R.id.counter015, R.id.counter016, R.id.counter017 };
	
	final String region[] = {"","Western Europe", "Central & Eastern Europe", "Middle East", "Oceania", "North Africa",
			"Eastern Africa", "West Africa", "South Africa", "Central Asia", "East Asia", "South Asia", "Southeast Asia",
			"North America", "Middle America", "The Caribbean basin", "Western South America", "Eastern South America"}; 
	
	private ArrayList<FriendEntity> likeUList;
	private ArrayList<FriendEntity> likeMeList;
	private SparseArray<ArrayList<FriendEntity>> regionMap = new SparseArray<ArrayList<FriendEntity>>();
	private ArrayList<FriendGroupEntity> groups;
	private MapIcon[] mapIcons;

	private int currentTab;  //0 : likeU, 1 : likeMe
	private FriendGroupEntity currentGroup;
	private int groupCount;
	private String userId;
	private boolean myPage;
	private Context context;
	private ListView listView;
	private String selectedRegionId;
	private String selectedRegionName;
	private int selectedRegionCount;
	
	private TextView textAreaNameView;
	private TextView textCountView;
	
	private Button btnListAction;
	private Button btnLikeU;
	private Button btnLikeMe;
	private AccountManager accountManager;
	private Button btnAreaBackIcon;
	private Button btnEditGroup;
	
	private LikeFriendAdapter friendAdapter;
	private LikeGroupAdapter groupAdapter;
	private boolean isCurrentEditMode = false;
	private ProgressDialog progressDlg;
	
	public static LikeListFragment newInstance(String userId,boolean myPage,int groupCount){
		LikeListFragment fragment = new LikeListFragment();
		
		Bundle args = new Bundle();
		args.putString("userId", userId);
		args.putBoolean("myPage", myPage);
		args.putInt("groupCount", groupCount);
		fragment.setArguments(args);
		
		return fragment;
	}
	
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = activity;
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		currentTab = 0;
		selectedRegionId = null;
		selectedRegionName = null;
		Bundle args = getArguments();
		if(args != null){
			this.myPage = args.getBoolean("myPage");
			this.userId = args.getString("userId");
			this.groupCount = args.getInt("groupCount");
		}else{
			this.myPage = false;
			this.groupCount = 0;
		}
		this.accountManager = AccountManager.getInstance();
	}

	
	public void getFriendsData(){
		if(this.currentTab == 0){
			//LikeU
			//나의 페이지가 아닐경우에는 friend정보만 가져온다.
			//friend정보는 좌측 맵을 위해서 기본적으로 꼭 필요하고 group정보는 group이 존재할 경우에만 가져온다.
			if(this.likeUList == null){
				likeUList = new ArrayList<FriendEntity>();
				refreshData(this.currentTab);
			}else{
				displayFriendData();
			}
			//그룹정보를 얻어온다.
			if(this.groups == null){
				this.groups = new ArrayList<FriendGroupEntity>();
				GroupListTask task = new GroupListTask();
				if(Build.VERSION.SDK_INT >= 11){
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}else{
					task.execute();				
				}
				
			}
			
		}else{
			if(this.likeMeList == null){
				likeMeList = new ArrayList<FriendEntity>();
				refreshData(this.currentTab);
			}else{
				displayFriendData();
			}
		}
	}

	private void displayFriendData(){
		filterData();
		showMap();
		displayRightPanel();
		
	}
	
	private void displayRightPanel(){
		//우측에 현재 선택된 region정보를 보여주고 숫자도 보여준다.
		//만약에 group이 존재하면 group데이타를 보여준다.
		
		if(currentGroup != null){
			//선택된 그룹이 있으면 그 그룹에 존재하는 멤버를 보여준다.
			btnAreaBackIcon.setBackgroundResource(R.drawable.selector_btn_list_back);
			btnEditGroup.setVisibility(View.GONE);
			btnListAction.setVisibility(View.VISIBLE);
			btnListAction.setText("Edit member");
			
			textAreaNameView.setText(currentGroup.groupName);
			textCountView.setText("(" + currentGroup.friendCount + ")");
			
			
			this.listView.setAdapter(this.friendAdapter);
			
			this.friendAdapter.setFriends(currentGroup.members);
			this.friendAdapter.notifyDataSetChanged();
			
		}else if(selectedRegionId != null){
			//특정지역의 사람뷰 
			this.listView.setAdapter(this.friendAdapter);
			btnEditGroup.setVisibility(View.GONE);
			if(this.currentTab == TAB_LIKEU && myPage){
				btnListAction.setVisibility(View.VISIBLE);
				btnListAction.setText("Add a group");
			}else{
				btnListAction.setVisibility(View.GONE);
			}
			btnAreaBackIcon.setBackgroundResource(R.drawable.selector_btn_list_back);
			btnListAction.setVisibility(View.GONE);
			
			//특정 선택된 지역.
			textAreaNameView.setText(selectedRegionName);
			ArrayList<FriendEntity> friends = regionMap.get(Integer.parseInt(selectedRegionId));
			this.textCountView.setText("("+friends.size()+")");
			this.friendAdapter.setFriends(friends);
			this.friendAdapter.notifyDataSetChanged();
			
		}else{
//			if(groupCount > 0 && this.currentTab == TAB_LIKEU && myPage){
//				//그룹뷰 
//				btnListAction.setVisibility(View.VISIBLE);
//				btnListAction.setText("Add a group");
//				
//				
//				textAreaNameView.setText("All place");
//				this.textCountView.setText("(" + this.likeUList.size()+")");
//					
//				btnAreaBackIcon.setBackgroundResource(R.drawable.mybigture_listarea_like_list_location_icon);
//				btnEditGroup.setVisibility(View.VISIBLE);
//				this.listView.setAdapter(this.groupAdapter);
//				
//				this.groupAdapter.setGroups(this.groups);
//				this.groupAdapter.notifyDataSetChanged();
//			}else{
				this.listView.setAdapter(this.friendAdapter);
				btnEditGroup.setVisibility(View.GONE);
//				if(this.currentTab == TAB_LIKEU && myPage){
//					btnListAction.setVisibility(View.VISIBLE);
//					btnListAction.setText("Add a group");
//				}else{
					btnListAction.setVisibility(View.GONE);
			//	}
				//모든 지역.
				btnAreaBackIcon.setBackgroundResource(R.drawable.mybigture_listarea_like_list_location_icon);
				
				textAreaNameView.setText("All place");
				if(this.currentTab == TAB_LIKEU){
					this.textCountView.setText("(" + this.likeUList.size()+")");
					this.friendAdapter.setFriends(this.likeUList);
				}else{
					this.textCountView.setText("("+this.likeMeList.size()+")");
					this.friendAdapter.setFriends(this.likeMeList);
				}
					
			
				this.friendAdapter.notifyDataSetChanged();
		//	}
			
		}
		
		
	}
	
	/**
	 * Group 상세뷰나 region상세뷰에서 다시 root로 돌아가는 버튼을 클릭시 
	 */
	private void backToRoot(){
		this.selectedRegionId = null;
		this.currentGroup = null;
		
		displayRightPanel();
	}
	
	public void refreshData(final int _currentTab){
		FriendListTask task = new FriendListTask(_currentTab, new FriendListTask.FriendListListener() {
			
			@Override
			public void onCompleted(ArrayList<FriendEntity> friends) {
				if(_currentTab == 0){
					likeUList.clear();
					likeUList.addAll(friends);
					
				}else{
					likeMeList.clear();
					likeMeList.addAll(friends);
					
				}
				
				displayFriendData();
			}
		});
		
		if(_currentTab == 0){
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId,selectedRegionId,null);
			}else{
				task.execute(userId,selectedRegionId,null);				
			}
		}else{
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,userId,selectedRegionId);
			}else{
				task.execute(userId,selectedRegionId);				
			}
		}
		
		if(groupCount > 0){
			//그룹정보를 가져와야한다.
			GroupListTask task2 = new GroupListTask();
			if(Build.VERSION.SDK_INT >= 11){
				task2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}else{
				task2.execute();				
			}
			
		}
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_like_list, container,false);
		
		
		this.listView = (ListView)view.findViewById(R.id.lvFriends);
		this.friendAdapter = new LikeFriendAdapter(getActivity(), this);
		this.groupAdapter = new LikeGroupAdapter(getActivity(),this);
		if(groupCount > 0){
			this.listView.setAdapter(this.groupAdapter);
		}else{
			this.listView.setAdapter(this.friendAdapter);
		}
		this.textAreaNameView = (TextView)view.findViewById(R.id.textAreaName);
		this.textCountView = (TextView)view.findViewById(R.id.textMemberCount);
		
		this.btnListAction = (Button)view.findViewById(R.id.btnListAction);
		this.btnListAction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentGroup == null){
					//add Group
					Intent intent = new Intent(context, EditGroupPopupActivity.class);
					startActivityForResult(intent, BigtureEnvironment.EVENT_EDIT_GROUP);
					
				}else{
					//edit member
					Intent intent = new Intent(context, EditGroupMemberActivity.class);
					intent.putExtra("groupEntity", (Parcelable)currentGroup);
					intent.putParcelableArrayListExtra("members", currentGroup.members);
					intent.putParcelableArrayListExtra("likeUList", likeUList);
					intent.putParcelableArrayListExtra("groups", groups);
					
					startActivityForResult(intent, BigtureEnvironment.EVENT_EDIT_GROUP_MEMBER);
				}	
			}
		});
		
		this.btnLikeU = (Button)view.findViewById(R.id.btnLikeU);
		this.btnLikeU.setSelected(true);
		this.btnLikeU.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnLikeU.setSelected(true);
				btnLikeMe.setSelected(false);
				currentTab = 0;
				currentGroup = null;
				selectedRegionId = null;
				
				getFriendsData();
			}
		});
		
		this.btnLikeMe = (Button)view.findViewById(R.id.btnLikeMe);
		this.btnLikeMe.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnLikeU.setSelected(false);
				btnLikeMe.setSelected(true);
				currentTab = 1;
				currentGroup = null;
				selectedRegionId = null;
				getFriendsData();
			}
		});
		
		
		this.mapIcons = new MapIcon[18];
		for (int i = 0; i < mapIcons.length; i++){
			if(i == 0) continue;
			mapIcons[i] = (MapIcon)view.findViewById(counterIds[i]);
			mapIcons[i].setTag(i+1);
			mapIcons[i].setCallback(new MapIcon.Callback(){	
				@Override
				public void onIconClicked(MapIcon icon){
					//해당 지역의 friend정보를 우측에 표현한다.
					selectedRegionId = "" + icon.regionId;
					selectedRegionName = region[icon.regionId];
					ArrayList<FriendEntity> friends = regionMap.get(icon.regionId);
					selectedRegionCount = friends.size();
					
					displayRightPanel();
				}
			});
		}
		
		this.btnAreaBackIcon = (Button)view.findViewById(R.id.btnAreaBackIcon);
		this.btnAreaBackIcon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(currentGroup != null || selectedRegionId != null){
					backToRoot();
				}
			}
		});
		
		this.btnEditGroup = (Button)view.findViewById(R.id.btnEditGroup);
		this.btnEditGroup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleEditGroupMode(currentGroup);
			}
		});
		
		getFriendsData();
		return view;
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		
		if(requestCode == BigtureEnvironment.EVENT_EDIT_GROUP){
			//서버에서 group정보를 새롭게 가져와야한다.
			currentTab = 0;
			GroupListTask task = new GroupListTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}else{
				task.execute();				
			}
			
		}else if(requestCode == BigtureEnvironment.EVENT_EDIT_GROUP_MEMBER){
			//현재 그룹의 멤버정보를 서버에서 다시 가져온다.
			GroupMemberListTask task = new GroupMemberListTask();
			if(Build.VERSION.SDK_INT >= 11){
				task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,this.currentGroup.index);
			}else{
				task.execute(this.currentGroup.index);				
			}
			
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	

	private void filterData(){
		this.regionMap.clear();

		ArrayList<FriendEntity> friends = null;
		if(this.currentTab == TAB_LIKEU){
			friends = this.likeUList;
		}else{
			friends = this.likeMeList;
		}
		
		for (int i = 0; i < friends.size(); i++){
			FriendEntity entity = friends.get(i);
			
			ArrayList<FriendEntity> list = regionMap.get(entity.regionId.intValue());
			if(list == null){
				list = new ArrayList<FriendEntity>();
				regionMap.put(entity.regionId.intValue(), list);
			}
			list.add(entity);
		}
	}
	
	private void showMap(){
		
		for (int i = 0; i < 18; i++){
			if(i == 0) continue;
			MapIcon mapIcon = mapIcons[i];
			mapIcon.regionId = i;
			ArrayList<FriendEntity> friends = regionMap.get(i);
			if (friends != null && friends.size() > 0){
				mapIcon.setVisibility(View.VISIBLE);
				mapIcon.setCount(friends.size());
			}else{
				mapIcon.setVisibility(View.GONE);
			}
		}
	}



	@Override
	public void doUnlike(FriendEntity friend) {
		
		
	}



	@Override
	public void goDetailGroup(FriendGroupEntity group) {
		if(group.members == null){
			this.currentGroup = group;
			
			GroupMemberListTask task = new GroupMemberListTask();
			task.execute(group.index);
		}else{
			this.currentGroup = group;
			displayRightPanel();
		}
	}



	@Override
	public void deleteGroup(FriendGroupEntity group) {
		
		progressDlg = ProgressDialog.show(context, "", "Delete group .....", true);
		progressDlg.setCancelable(false);
		
		GroupDeleteTask task = new GroupDeleteTask();
		task.execute(group.index);
		
	}
	
	
	@Override
	public void editGroupName(FriendGroupEntity group) {
		Intent intent = new Intent(context, EditGroupPopupActivity.class);
		
		intent.putExtra("groupEntity", (Parcelable)group);
		startActivityForResult(intent, BigtureEnvironment.EVENT_EDIT_GROUP);
		
	}



	public void toggleEditGroupMode(FriendGroupEntity group){
		if(isCurrentEditMode){
			//edit mode를 해제함.
			btnEditGroup.setBackgroundResource(R.drawable.selector_btn_edit_group);
			groupAdapter.setEditMode(false);
			groupAdapter.notifyDataSetChanged();
			isCurrentEditMode = false;
		}else{
			//edit mode로 변경.
			btnEditGroup.setBackgroundResource(R.drawable.mybigture_listarea_btn_edit_c);
			groupAdapter.setEditMode(true);
			groupAdapter.notifyDataSetChanged();
			isCurrentEditMode = true;
		}
	}
	
	private class GroupMemberListTask extends AsyncTask<String, Void, ArrayList<FriendEntity>>{

		@Override
		protected ArrayList<FriendEntity> doInBackground(String... params) {
			String groupId = params[0];
			return FriendHandler.getFriendGroupMembers(groupId, 1);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			
			super.onPostExecute(result);
			currentGroup.members = result;
			currentGroup.friendCount = result.size();
			displayRightPanel();
		}
		
	}
	
	
	private class GroupListTask extends AsyncTask<Void, Void, ArrayList<FriendGroupEntity>>{
		
		
		@Override
		protected ArrayList<FriendGroupEntity> doInBackground(Void... params) {
			return FriendHandler.findGroups();
		}

		@Override
		protected void onPostExecute(ArrayList<FriendGroupEntity> result) {
			super.onPostExecute(result);
			if(groups == null)
				groups = new ArrayList<FriendGroupEntity>();
			else
				groups.clear();
			if(result.size() > 0){
				//맨 처음에 All값을 먼저 추가한다.
				
				FriendGroupEntity dummy = new FriendGroupEntity();
				dummy.index = "-1";
				dummy.groupName = "All";
				dummy.friendCount = likeUList.size();
				groups.add(dummy);
				
				groups.addAll(result);
			}
			groupCount = result.size();
			displayRightPanel();
		}
		
	}
	
	private class GroupDeleteTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String groupId = params[0];
			return FriendHandler.deleteGroup(groupId);
		}

		@Override
		protected void onCancelled() {
			progressDlg.dismiss();
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			progressDlg.dismiss();
			
			GroupListTask task2 = new GroupListTask();
			task2.execute();
			
			super.onPostExecute(result);
		}
		
		
	}
}
