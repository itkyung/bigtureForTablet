package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

public class PuzzleEntity {
	public String classId;
	public String puzzleId;
	public String baseSketch;
	public boolean completed;
	
	public ArrayList<PuzzleArtworkEntity> fragments = new ArrayList<PuzzleArtworkEntity>();
	
	public void importData(JSONObject json) throws Exception{
		this.classId = json.getString("classId");
		this.puzzleId = json.getString("puzzleId");
		this.baseSketch = json.getString("baseSketch");
		this.completed = json.getBoolean("completed");
		
	}
	
	
	public String getSketchImageURL(){
		return ServerStaticVariable.ClassPuzzleSketchURL + baseSketch;
	}
	
}
