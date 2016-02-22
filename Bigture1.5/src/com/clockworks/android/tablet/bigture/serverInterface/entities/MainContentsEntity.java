package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;



public class MainContentsEntity {
	ArrayList<ArtworkEntity> artworks = new ArrayList<ArtworkEntity>();
	ArrayList<ContestEntity> contests = new ArrayList<ContestEntity>();
	ArrayList<StoryEntity> stories = new ArrayList<StoryEntity>();
	ArrayList<ArtClassEntity> classes = new ArrayList<ArtClassEntity>();
	ArrayList<SimpleUserEntity> experts = new ArrayList<SimpleUserEntity>();
	
	public MainContentsEntity(){
		
	}
	
	public void importData(JSONObject obj) throws Exception{
		JSONArray array1 = obj.getJSONArray("artworks");
		
		for(int i=0; i < array1.length(); i++){
			JSONObject artwork = array1.getJSONObject(i);
			ArtworkEntity entity = new ArtworkEntity();
			entity.importData(artwork);
			artworks.add(entity);
		}
		
		JSONArray array2 = obj.getJSONArray("contests");
		
		for(int i=0; i < array2.length(); i++){
			JSONObject contest = array2.getJSONObject(i);
			ContestEntity entity = new ContestEntity();
			entity.importData(contest);
			contests.add(entity);
		}
		
		JSONArray array3 = obj.getJSONArray("experts");
		
		for(int i=0; i < array3.length(); i++){
			JSONObject json = array3.getJSONObject(i);
			SimpleUserEntity entity = new SimpleUserEntity();
			entity.importData(json);
			experts.add(entity);
		}
		
		JSONArray array4 = obj.getJSONArray("stories");
		
		for(int i=0; i < array4.length(); i++){
			JSONObject json = array4.getJSONObject(i);
			StoryEntity entity = new StoryEntity();
			entity.importData(json);
			stories.add(entity);
		}
		
		if(obj.has("artClasses")){
		
			JSONArray array5 = obj.getJSONArray("artClasses");
			
			for(int i=0; i < array5.length(); i++){
				JSONObject json = array5.getJSONObject(i);
				ArtClassEntity entity = new ArtClassEntity();
				entity.importData(json);
				classes.add(entity);
			}
		}
	}

	public ArrayList<ArtworkEntity> getArtworks() {
		return artworks;
	}

	public void setArtworks(ArrayList<ArtworkEntity> artworks) {
		this.artworks = artworks;
	}

	public ArrayList<ContestEntity> getContests() {
		return contests;
	}

	public void setContests(ArrayList<ContestEntity> contests) {
		this.contests = contests;
	}

	public ArrayList<StoryEntity> getStories() {
		return stories;
	}

	public void setStories(ArrayList<StoryEntity> stories) {
		this.stories = stories;
	}

	public ArrayList<ArtClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<ArtClassEntity> classes) {
		this.classes = classes;
	}

	public ArrayList<SimpleUserEntity> getExperts() {
		return experts;
	}

	public void setExperts(ArrayList<SimpleUserEntity> experts) {
		this.experts = experts;
	}
	
	
	
}
