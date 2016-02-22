package com.clockworks.android.tablet.bigture;


import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.common.AccountManager;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.common.bitmapDownloader.BitmapDownloader;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.FriendHandler;
import com.clockworks.android.tablet.bigture.serverInterface.task.FriendHandleTask;
import com.clockworks.android.tablet.bigture.views.common.WaitDialog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 사용자를 검색해서 친구추가를 할수있는 팝업 
 * @author bizwave
 *
 */
public class FindUserPopupActivity extends Activity {
	
	private ListView listView;
	private EditText keywordEdit;
	private BitmapFactory.Options options;
	private FindUserTask findTask;
	private int currentPage;
	
	private Context context;
	private FindUserAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		context = this;
		currentPage = 1;
		options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 3;
		
		setContentView(R.layout.popup_find_user);
		
		this.keywordEdit = (EditText)findViewById(R.id.editKeyword);
		keywordEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s){
				currentPage = 1;
				if (s.toString().length() > 1){
					searchUser(s.toString());
				}else{
					searchUser(null);
					//listView.setAdapter(null);
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
		
		findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener(){	
			@Override
			public void onClick(View v){
				setResult(Activity.RESULT_OK);
				finish();
			}
		});
		
		this.listView = (ListView)findViewById(R.id.listView1);
		this.adapter = new FindUserAdapter(this);
		this.listView.setAdapter(adapter);
		
		searchUser(null);
	}

	private void searchUser(String keyword){

		if (findTask != null)
			findTask.cancelTask();
		
		findTask = new FindUserTask();
		findTask.execute(keyword);
		
	}
	
	public void refreshData(ArrayList<FriendEntity> users){
		this.adapter.setUsers(users);
		this.adapter.notifyDataSetChanged();
	}
	
	private class FindUserTask extends AsyncTask<String, Void, ArrayList<FriendEntity>>{
		boolean canceled = false;
		
		@Override
		protected ArrayList<FriendEntity> doInBackground(String... params) {
			String keyword = params[0];
			
			return FriendHandler.findUsers(keyword, currentPage);
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEntity> result) {
			
			super.onPostExecute(result);
			if(!canceled)
				refreshData(result);
		}
		
		public void cancelTask(){
			this.cancel(true);
			canceled = true;
		}
	}
	
	class FindUserAdapter extends BaseAdapter{
		List<FriendEntity> users;
		Context context;
		
		public FindUserAdapter(Context context){
			this.context = context;
			this.users = new ArrayList<FriendEntity>();
		}
		
		public List<FriendEntity> getUsers() {
			return users;
		}

		public void setUsers(List<FriendEntity> users) {
			this.users = users;
		}



		@Override
		public int getCount(){
			return users.size();
		}

		@Override
		public Object getItem(int position){
			return users.get(position);
		}

		@Override
		public long getItemId(int arg0){
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(this.context);
				convertView = inflater.inflate(R.layout.cell_find_user, null);
				ViewHolder viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}
			
			FriendEntity entity = users.get(position);

			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			viewHolder.updateView(entity);
			
			return convertView;
		}		
	}
	
	class ViewHolder{
		FriendEntity entity;
		public TextView nameLabel;
		public TextView countryLabel;
		public ImageView imageProfile;
		public TextView jobLabel;
		public Button btnLike;
		public Button btnUnlike;
		public Button btnWait;
		
		public ViewHolder(View parent){
			nameLabel = (TextView)parent.findViewById(R.id.nameLabel);
			countryLabel = (TextView)parent.findViewById(R.id.countryLabel);
			imageProfile = (ImageView)parent.findViewById(R.id.imageProfile);
			btnLike  = (Button)parent.findViewById(R.id.btnLike);
			btnUnlike = (Button)parent.findViewById(R.id.btnUnlike);
			jobLabel = (TextView)parent.findViewById(R.id.jobLabel);
			
			imageProfile.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){
//					if (entity.pro){
//						Intent intent = new Intent(getContext(), ExpertPageActivity.class);
//						intent.putExtra("userIndex", profileEntity.index);
//						getContext().startActivity(intent);
//					}
//					else
//					{
//						Intent intent = new Intent(getContext(), MyArtworkActivity.class);
//						intent.putExtra("userIndex", profileEntity.index);
//						getContext().startActivity(intent);						
//					}
				}
			});
			
			btnLike.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){
					FriendHandleTask task = new FriendHandleTask(new FriendHandleTask.Callback(){	
						@Override
						public void onComplete(FriendHandleTask task, boolean result){
							WaitDialog.hideWaitDialog();
							if (result){
								entity.alreadyLike = true;
								FindUserAdapter adapter = (FindUserAdapter)listView.getAdapter();
								adapter.notifyDataSetChanged();								
							}else{
								BigtureEnvironment.showAlertMessage(context, 99);
							}
						}
					});
					
					WaitDialog.showWailtDialog(context, false);
					task.likeYou(entity.index);
				}
			});
			
			btnUnlike.setOnClickListener(new View.OnClickListener(){	
				@Override
				public void onClick(View v){
					FriendHandleTask task = new FriendHandleTask(new FriendHandleTask.Callback(){	
						@Override
						public void onComplete(FriendHandleTask task, boolean result){
							WaitDialog.hideWaitDialog();
							if (result){
								entity.alreadyLike = false;
								FindUserAdapter adapter = (FindUserAdapter)listView.getAdapter();
								adapter.notifyDataSetChanged();								
							}else{
								BigtureEnvironment.showAlertMessage(context, 99);
							}
						}
					});
					
					WaitDialog.showWailtDialog(context, false);
					task.unlike(entity.index);
				}
			});
		}
		
		public void updateView(FriendEntity entity){
			this.entity = entity;
			nameLabel.setText(entity.nickName);
			countryLabel.setText(entity.country);
			
			if(entity.pro){
				jobLabel.setVisibility(View.VISIBLE);
				jobLabel.setText(entity.job);
			}else{
				jobLabel.setVisibility(View.GONE);
			}
			
			String imageURL = entity.photoPath;
			if (imageURL != null)
				BitmapDownloader.getInstance().displayImage(imageURL, options, imageProfile, null);
			else
				imageProfile.setImageResource(R.drawable.common_pofile_60x60);
			
			if (entity.alreadyLike){
				btnLike.setVisibility(View.GONE);
				btnUnlike.setVisibility(View.VISIBLE);
			}else{
				btnLike.setVisibility(View.VISIBLE);
				btnUnlike.setVisibility(View.GONE);				
			}
		}
	}
}
