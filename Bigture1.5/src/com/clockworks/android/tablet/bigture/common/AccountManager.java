package com.clockworks.android.tablet.bigture.common;

import java.util.ArrayList;
import java.util.List;

import com.clockworks.android.tablet.bigture.serverInterface.entities.AccountEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.FriendEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ProfileEntity;
import com.clockworks.android.tablet.bigture.utils.DeviceUtil;


import android.content.Context;

public class AccountManager
{
	public static String osName = "Android";
	public static int osVersion;
	public static boolean isTablet;
	public static String appVersion;
	
	protected static AccountManager manager;
	protected Context context;
	
	private boolean login;
	public AccountEntity entity;
	
	private List<OnLoginChangeListener> listenerList = new ArrayList<OnLoginChangeListener>();

	protected AccountManager(Context context)
	{
		this.context = context;
		entity = new AccountEntity();
		entity.load(context);
		
		isTablet = DeviceUtil.isTablet(context);
		osVersion = DeviceUtil.getOSVersionNumber();
		appVersion = DeviceUtil.getAppVersion(context);
	}
	
	public static AccountManager createInstance(Context context)
	{
		if (manager == null)
			manager = new AccountManager(context);
		
		return manager;
	}
	
	public static AccountManager getInstance()
	{
		return manager;
	}
	
	public boolean isKorean(){
		if(entity != null){
			return "KO".equalsIgnoreCase(entity.countryCode) || "KOR".equalsIgnoreCase(entity.countryCode) ? true : false;
		}
		return false;
	}
	
	public static boolean isLogin()
	{
		if (manager == null)
			return false;
		
		return manager.login;
	}
	
	public static boolean isExpert()
	{
		if (manager == null || manager.login == false)
			return false;
		
		if (manager.entity != null && manager.entity.isPro)
			return true;
		
		return false;
	}
	
	public static boolean isMyUserIndex(String userIndex)
	{
		if (manager == null || manager.login == false || userIndex == null)
			return false;
		
		return userIndex.equals(manager.getAccountEntity().index);
	}
	
	public static boolean isFriend(String userIndex)
	{
		if (manager == null || manager.login == false || userIndex == null)
			return false;

		List<FriendEntity> friendList = manager.entity.likeYous;
		
		if (friendList != null)
		{
			for (FriendEntity entity : friendList)
			{
				if (entity.index.equals(userIndex))
					return true;
			}
		}
		
		return false;
	}
	
	public static String getUserIdx()
	{
		if (manager == null || manager.login == false || manager.entity == null)
			return null;
		
		return manager.entity.index;
	}
	
	public String getUserIndex()
	{
		if (login == false || entity == null)
			return null;
		
		return entity.index;
	}
	
	public AccountEntity getAccountEntity()
	{
		return this.entity;
	}
	
	public void setAccountEntity(AccountEntity entity){
		this.entity = entity;
		entity.store(context);
	}
	
	public void setLikeYous(ArrayList<FriendEntity> friendList){
		if (this.entity != null)
			this.entity.likeYous = friendList;
	}
	
	public void addOnLoginChangeListener(OnLoginChangeListener listener)
	{
		listenerList.remove(listener);
		listenerList.add(listener);
	}
	public void removeOnLoginChangeListener(OnLoginChangeListener listener){
		listenerList.remove(listener);
	}
	
	public void setLogin(boolean value){
		this.login = value;
		
		if (value == false){
			this.entity = null;
		}else{
			this.entity = new AccountEntity();
			this.entity.load(context);
		}
		
		for (OnLoginChangeListener listener : listenerList){
			listener.onLoginChanged(value);
		}
	}
	
	public void setAutoLogin(boolean value)
	{
		entity.keepLogin = value;
		entity.store(context);
	}
	
	public void store()
	{
		entity = new AccountEntity();
		entity.store(context);
	}
	
	public void load()
	{
		entity = new AccountEntity();
		entity.load(context);
	}
	
	public interface OnLoginChangeListener
	{
		void onLoginChanged(boolean login);
	}
	
	public int getCountLikeYous(){
		return entity.likeYous == null ? 0 : entity.likeYous.size();
	}
	
	public int getCountLikeMes(){
		return entity.likeMes == null ? 0 : entity.likeMes.size();
	}
	
}
