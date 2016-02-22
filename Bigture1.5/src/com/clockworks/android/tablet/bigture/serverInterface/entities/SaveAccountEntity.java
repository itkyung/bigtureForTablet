package com.clockworks.android.tablet.bigture.serverInterface.entities;

public class SaveAccountEntity {
	private String nickName;
	private String comment;
	private String birthday;
	private String gender;
	private String countryCode;
	private String tempImageId;
	private NotiSettingEntity setting;
	private boolean valid;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTempImageId() {
		return tempImageId;
	}
	public void setTempImageId(String tempImageId) {
		this.tempImageId = tempImageId;
	}
	public NotiSettingEntity getSetting() {
		return setting;
	}
	public void setSetting(NotiSettingEntity setting) {
		this.setting = setting;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	
}
