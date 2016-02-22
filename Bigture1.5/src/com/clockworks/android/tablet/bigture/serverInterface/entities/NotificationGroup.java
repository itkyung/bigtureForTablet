package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;



public class NotificationGroup {
	private Date createDate;
	private DateFormat fm = new SimpleDateFormat("MM-dd-yyyy");
	
	private ArrayList<NotificationEntity> notifications = new ArrayList<NotificationEntity>();

	public NotificationGroup(){
		
	}
	
	public void importDate(JSONObject json) throws Exception{
		this.createDate = fm.parse(json.getString("date"));
		
		JSONArray array = json.getJSONArray("notifications");
		for(int i = 0; i < array.length(); i++){
			JSONObject row = array.getJSONObject(i);
			NotificationEntity entity = new NotificationEntity();
			entity.importData(row);
			notifications.add(entity);
		}
		
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public ArrayList<NotificationEntity> getNotifications() {
		return notifications;
	}

	public void setNotifications(ArrayList<NotificationEntity> notifications) {
		this.notifications = notifications;
	}
	
	
}
