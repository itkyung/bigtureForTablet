package com.clockworks.android.tablet.bigture.views.story;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryEntity;

public class StorySectionItem {

	private boolean section;
	private String sectionTitle;
	
	
	private ArrayList<StoryEntity> stories;

	public ArrayList<StoryEntity> getStories() {
		return stories;
	}

	public void setStories(ArrayList<StoryEntity> stories) {
		this.stories = stories;
	}

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
	
	

}
