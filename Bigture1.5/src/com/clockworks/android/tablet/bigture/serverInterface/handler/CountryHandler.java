package com.clockworks.android.tablet.bigture.serverInterface.handler;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;



import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;
import com.clockworks.android.tablet.bigture.serverInterface.entities.CountryEntity;
import com.clockworks.android.tablet.bigture.utils.http.HttpRequestParameters;
import com.clockworks.android.tablet.bigture.utils.http.HttpUtil;

public class CountryHandler {

	public static ArrayList<CountryEntity> getCountries(){
		ArrayList<CountryEntity> countries = new ArrayList<CountryEntity>();
		HttpRequestParameters params = new HttpRequestParameters();
		
		HttpUtil util = new HttpUtil();
		String resultString = util.execute(ServerStaticVariable.CountryListURL, params);

		try
		{
			JSONObject object = new JSONObject(resultString);
			boolean success = object.getBoolean("success");
			int errorNo = object.getInt("errno");

			if (success){
				JSONArray datas = (JSONArray)object.get("datas");
				for(int i=0; i < datas.length(); i++){
					JSONObject data = datas.getJSONObject(i);
					CountryEntity entity = new CountryEntity();
					entity.importData(data);
					countries.add(entity);
				}
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return countries;
	}
}
