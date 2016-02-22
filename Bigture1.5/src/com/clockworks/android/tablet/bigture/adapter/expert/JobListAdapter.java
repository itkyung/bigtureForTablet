package com.clockworks.android.tablet.bigture.adapter.expert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.AlphabetIdxWrapper;
import com.clockworks.android.tablet.bigture.utils.StringUtils;
import com.clockworks.android.tablet.bigture.views.expert.ExpertListCell;
import com.clockworks.android.tablet.bigture.views.expert.ExpertSectionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JobListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> jobs;
	private ArrayList<AlphabetIdxWrapper> datas;
	private HashMap<String, Integer> sectionMap = new HashMap<String, Integer>();
	private String[] sections = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private JobListener listener;
	
	public JobListAdapter(Context context,JobListener listener){
		this.context = context;
		this.listener = listener;
		this.datas = new ArrayList<AlphabetIdxWrapper>();
	}
	
	
	public void setJobs(ArrayList<String> jobs){
		this.jobs = jobs;
		this.datas.clear();
		
		convertToSection();
	}
	
	private void convertToSection(){
		Collections.sort(jobs);
		char lastFirstChar = '\u0000';
		int i=0;
		for(String job : jobs){
			
			char firstChar = StringUtils.getFirstElement(job);
			if(firstChar == lastFirstChar){
				AlphabetIdxWrapper row = new AlphabetIdxWrapper();
				row.setSection(false);
				row.setValue(job);
				datas.add(row);
				i++;
			}else{
				AlphabetIdxWrapper row = new AlphabetIdxWrapper();
				row.setSection(true);
				row.setValue(""+firstChar);
				sectionMap.put(""+firstChar, i);
				i++;
				datas.add(row);
				AlphabetIdxWrapper row1 = new AlphabetIdxWrapper();
				row1.setSection(false);
				row1.setValue(job);
				datas.add(row1);
				i++;
				lastFirstChar = firstChar;
			}
		}
		
	}
	
	@Override
	public int getItemViewType(int position) {
		AlphabetIdxWrapper item = this.datas.get(position);
		return item.isSection() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	
	
	@Override
	public int getCount() {
		return this.datas.size();
	}

	@Override
	public Object getItem(int position) {
		return this.datas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AlphabetIdxWrapper item = (AlphabetIdxWrapper)getItem(position);
		if(item.isSection()){
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_job_section, null);
			}
			TextView sectionTitle = (TextView)convertView.findViewById(R.id.sectionTitle);
			sectionTitle.setText(item.getValue());
			return convertView;
		}else{
			
			if (convertView == null){
				LayoutInflater inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.cell_job, null);
			}
			TextView jobTitle = (TextView)convertView.findViewById(R.id.jobTitle);
			jobTitle.setText(item.getValue());
			jobTitle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.onClickJob(item.getValue());
				}
			});
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.onClickJob(item.getValue());
				}
			});
			return convertView;
		}
		
	}

	public int getPositionForSection(String section){
		String key = section.toLowerCase();
		if(sectionMap.containsKey(key)){
			return sectionMap.get(key);
		}else{
			return -1;
		}
	}

	public Object[] getSections() {
		return sections;
	}
	
	public interface JobListener{
		public void onClickJob(String jobName);
	}
	
}
