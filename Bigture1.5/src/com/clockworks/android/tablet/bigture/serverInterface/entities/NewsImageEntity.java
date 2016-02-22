package com.clockworks.android.tablet.bigture.serverInterface.entities;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;



import android.view.View;

public class NewsImageEntity
{
	public String tempIndex;
	public String imageIndex;
	public String imageURL;
	public String thumbURL;

	public String localImagePath;
	public View view;
	
	public void importData(JSONObject obj) throws Exception{
		imageIndex = obj.getString("id");
		imageURL = obj.getString("photo");
		thumbURL = obj.getString("thumbnail");
	}

	public String getThumbnailURL(){
		if (thumbURL != null && thumbURL.length() > 0)
			return ServerStaticVariable.NewsThumbURL + thumbURL;
		else
			return null;
	}

	public String getImageURL()
	{
		if (imageURL != null && imageURL.length() > 0)
			return ServerStaticVariable.NewsImageURL + imageURL;
		else
			return null;
	}
}
