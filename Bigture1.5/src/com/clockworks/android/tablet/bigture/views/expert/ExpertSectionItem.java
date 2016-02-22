package com.clockworks.android.tablet.bigture.views.expert;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.SimpleUserEntity;

public class ExpertSectionItem {
	private boolean section;
	private String sectionTitle;
	
	private SimpleUserEntity entity;
	private ArrayList<SimpleUserEntity> experts;

	public boolean isSection() {
		return section;
	}

	public void setSection(boolean section) {
		this.section = section;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public ArrayList<SimpleUserEntity> getExperts() {
		return experts;
	}

	public void setExperts(ArrayList<SimpleUserEntity> experts) {
		this.experts = experts;
	}

	public SimpleUserEntity getEntity() {
		return entity;
	}

	public void setEntity(SimpleUserEntity entity) {
		this.entity = entity;
	}
	
	
	
}
