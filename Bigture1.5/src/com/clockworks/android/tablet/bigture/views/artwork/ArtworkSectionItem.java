package com.clockworks.android.tablet.bigture.views.artwork;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;

public class ArtworkSectionItem {

	private boolean footerView;
	private boolean section;
	private String sectionTitle;
	
	private ArrayList<ArtworkEntity> artworks;

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

	public ArrayList<ArtworkEntity> getArtworks() {
		return artworks;
	}

	public void setArtworks(ArrayList<ArtworkEntity> artworks) {
		this.artworks = artworks;
	}

	public boolean isFooterView() {
		return footerView;
	}

	public void setFooterView(boolean footerView) {
		this.footerView = footerView;
	}

	
	
	
}
