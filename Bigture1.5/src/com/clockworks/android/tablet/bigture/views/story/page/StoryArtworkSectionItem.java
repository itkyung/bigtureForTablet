package com.clockworks.android.tablet.bigture.views.story.page;

import java.util.ArrayList;

import com.clockworks.android.tablet.bigture.serverInterface.entities.StoryArtworkEntity;

public class StoryArtworkSectionItem {
	private int pageNo;
	private String pageId;
	private boolean viewPage;
	private boolean viewDivider;
	private ArrayList<StoryArtworkEntity> artworks;
	
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public boolean isViewPage() {
		return viewPage;
	}
	public void setViewPage(boolean viewPage) {
		this.viewPage = viewPage;
	}
	public ArrayList<StoryArtworkEntity> getArtworks() {
		return artworks;
	}
	public void setArtworks(ArrayList<StoryArtworkEntity> artworks) {
		this.artworks = artworks;
	}
	public boolean isViewDivider() {
		return viewDivider;
	}
	public void setViewDivider(boolean viewDivider) {
		this.viewDivider = viewDivider;
	}
	
	
}
