package com.clockworks.android.tablet.bigture.common;

import com.clockworks.android.tablet.bigture.serverInterface.entities.NotificationEntity;

public interface NotificationClickListener {
	public void refreshNotifications();
	public void clickNotification(NotificationEntity noti);
}
