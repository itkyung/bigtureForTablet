package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.common.NotificationType;
import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class NotificationEntity {
	private String id;
	
	private NotificationType type;
	
	private String title;
	
	private String refId;
	
	private String msg;
	
	private String actorName;
	
	private String actorJob;
	
	private String actorId;
	
	private Date created;
	
	private Date startDate;
	
	private Date endDate;
	
	private boolean readFlag;
	
	private String actorImage;

	
	public NotificationEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		this.id = json.getString("id");
		this.type = NotificationType.valueOf(json.getString("type"));
		if(json.has("title"))
			this.title = json.getString("title");
		this.msg = json.getString("msg");
		this.created = new Date(json.getLong("createdTime"));
		if(json.has("refId")){
			this.refId = json.getString("refId");
		}
		if(json.has("actorName")){
			this.actorName = json.getString("actorName");
		}
		if(json.has("actorId")){
			this.actorId = json.getString("actorId");
		}
		if(json.has("actorJob")){
			this.actorJob = json.getString("actorJob");
		}
		if(json.has("contestStartDate")){
			this.startDate = new Date(json.getLong("contestStartDate"));
		}
		if(json.has("contestEndDate")){
			this.endDate = new Date(json.getLong("contestEndDate"));
		}
		if(json.has("readFlag")){
			this.readFlag = json.getBoolean("readFlag");
		}else{
			this.readFlag = false;
		}
		
		if(json.has("actorImage")){
			this.actorImage = json.getString("actorImage");
		}
		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public String getActorJob() {
		return actorJob;
	}

	public void setActorJob(String actorJob) {
		this.actorJob = actorJob;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isReadFlag() {
		return readFlag;
	}

	public void setReadFlag(boolean readFlag) {
		this.readFlag = readFlag;
	}

	public String getActorImage() {
		
		if (actorImage != null && actorImage.length() > 0)
			return ServerStaticVariable.ProfileURL + actorImage;
		else
			return null;
		
	}

	public void setActorImage(String actorImage) {
		this.actorImage = actorImage;
	}
	
	
}
