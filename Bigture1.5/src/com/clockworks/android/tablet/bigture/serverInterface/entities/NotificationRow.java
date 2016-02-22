package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.Date;

public class NotificationRow {
	private boolean section;
	private Date createDate;
	private NotificationEntity notification;
	
	public boolean isSection() {
		return section;
	}
	public void setSection(boolean section) {
		this.section = section;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public NotificationEntity getNotification() {
		return notification;
	}
	public void setNotification(NotificationEntity notification) {
		this.notification = notification;
	}
	
	
}
