package com.clockworks.android.tablet.bigture.views.dialog;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.R;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.CountryHandler;

import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CountryListDialog extends DialogFragment implements OnItemClickListener{
	private OnSelectCountryListener mListener;
	private ListView listView;
	private ArrayList<CountryEntity> countries;
	private ArrayList<String> countryNames;
	private ArrayAdapter<String> adapter;
	
	public static CountryListDialog newInstance(){
		CountryListDialog dialog = new CountryListDialog();
		
		return dialog;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try{
			mListener = (OnSelectCountryListener)activity;
		}catch(ClassCastException e){
			throw new ClassCastException(activity.toString() + "must implement OnSelectCountryListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_country_list, container,false);
		
		getDialog().setTitle("Country");
		
		this.countryNames = new ArrayList<String>();
		this.countries = new ArrayList<CountryEntity>();
		this.listView = (ListView)v.findViewById(R.id.listCountry);
		
		this.adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,countryNames);
		this.listView.setAdapter(adapter);
		this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		this.listView.setOnItemClickListener(this);
		
		
		CountryListTask task = new CountryListTask();
		task.execute();
		
		return v;
	}

	public void setCountries(ArrayList<CountryEntity> datas){
		countries.clear();
		countries.addAll(datas);
		countryNames.clear();
		for(CountryEntity data : datas){
			countryNames.add(data.name);
		}
		
		this.adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
		CountryEntity country = this.countries.get(position);
		this.mListener.onCountrySelected(country);
		
	}

	class CountryListTask extends AsyncTask<Void,Void,ArrayList<CountryEntity>>{

		@Override
		protected ArrayList<CountryEntity> doInBackground(Void... params) {
			
			ArrayList<CountryEntity> datas = CountryHandler.getCountries(); 
			
			return datas;
		}

		@Override
		protected void onPostExecute(ArrayList<CountryEntity> datas) {
			setCountries(datas);
		}
		
		

	}


	public interface OnSelectCountryListener{
		
		public void onCountrySelected(CountryEntity country);
	}
}
