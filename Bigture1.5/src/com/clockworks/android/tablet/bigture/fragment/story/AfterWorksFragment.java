package com.clockworks.android.tablet.bigture.fragment.story;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.ArtworkDetailActivity;
import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.ShowArtworkInterface;
import com.clockworks.android.tablet.bigture.adapter.story.AfterReadAdapter;
import com.clockworks.android.tablet.bigture.common.BigtureEnvironment;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AfterReadEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.StoryHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AfterWorksFragment extends Fragment implements ShowArtworkInterface {
	private ListView listView;
	private StoryEntity story;
	private AfterReadAdapter adapter;
	
	public static AfterWorksFragment newInstance(StoryEntity entity){
		AfterWorksFragment fragment = new AfterWorksFragment();
		
		Bundle args = new Bundle();
		args.putParcelable("story", entity);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		this.story = args.getParcelable("story");
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_afterread_list, container,false);
		
		this.listView = (ListView)view.findViewById(R.id.listView);
		this.adapter = new AfterReadAdapter(getActivity(), new ArrayList<AfterReadEntity>());
		listView.setAdapter(adapter);
		this.adapter.setArtworkInterface(this);
		
		refresh();
		return view;
	}
	
	public void refresh(){
		AfterReadTask task = new AfterReadTask();
		task.execute();
	}
	
	class AfterReadTask extends AsyncTask<Void, Void, ArrayList<AfterReadEntity>>{

		@Override
		protected ArrayList<AfterReadEntity> doInBackground(Void... params) {
			return StoryHandler.listAfterReading(story.storyId);
		}

		@Override
		protected void onPostExecute(ArrayList<AfterReadEntity> result) {
			
			super.onPostExecute(result);
			
			adapter.setArtworks(result);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
	}
	
	public void onShowArtworkDetail(ArtworkEntity artwork) {
		ArrayList<ArtworkEntity> artworkList = new ArrayList<ArtworkEntity>();
		artworkList.add(artwork);
		
		Intent intent = new Intent(getActivity(),ArtworkDetailActivity.class);
		intent.putExtra("fromMyBigture", false);
		intent.putParcelableArrayListExtra("artworkList", artworkList);
		intent.putExtra("currentIndex", 0);
		
		startActivityForResult(intent, BigtureEnvironment.REQ_CODE_ARTWORK_DETAIL);
	}
	
}
