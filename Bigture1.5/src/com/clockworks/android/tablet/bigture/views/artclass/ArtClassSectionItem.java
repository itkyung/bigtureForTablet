package com.clockworks.android.tablet.bigture.views.artclass;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;

public class ArtClassSectionItem {
	private boolean section;
	private String sectionTitle;
	
	private ArrayList<ArtClassEntity> classes;

	
	
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

	public ArrayList<ArtClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<ArtClassEntity> classes) {
		this.classes = classes;
	}
	
	
}
