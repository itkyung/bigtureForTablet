package com.clockworks.android.tablet.bigture.serverInterface.task;

import java.io.File;
import java.util.List;


import com.clockworks.android.tablet.bigture.common.PuzzlePart;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtClassEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PuzzleArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.PuzzleEntity;
import com.clockworks.android.tablet.bigture.serverInterface.handler.ArtClassHandler;

import android.os.AsyncTask;

public class ArtClassHandleTask{
	Callback callback;

	public int value;

	public ArtClassEntity artClass;
	public PuzzleEntity puzzle;
	public List<PuzzleEntity> puzzleList;
	public List<PuzzleArtworkEntity> puzzleArtworkList;
	public PuzzleArtworkEntity puzzleArtworkEntity;
	

	public List<ArtworkEntity> artworkList;
	
	public ArtClassHandleTask(Callback callback){
		this.callback = callback;
	}

	public void getPuzzleList(String classId){
		InterfaceTask task = new InterfaceTask();
		task.execute("getPuzzleList", classId);
	}
	
	public void getPuzzleArtworkList(String classId,String puzzleId){
		InterfaceTask task = new InterfaceTask();
		task.execute("getPuzzleArtworkList", classId,puzzleId);
	}
	
//	public void addPuzzle(String classId, File sketchFile){
//		InterfaceTask task = new InterfaceTask();
//		task.execute("addPuzzle", classId, sketchFile);
//	}
	
	public void addPuzzleArtwork(String classId,String puzzleId, PuzzlePart part, String artworkId){
		InterfaceTask task = new InterfaceTask();
		task.execute("addPuzzleArtwork", classId, puzzleId, part, artworkId);		
	}
	
	public void reservePuzzleArtwork(String classId,String puzzleId, PuzzlePart part){
		InterfaceTask task = new InterfaceTask();
		task.execute("reservePuzzleArtwork",classId, puzzleId, part);		
	}
	
	public void cancelReservedPuzzleArtwork(String classId,String puzzleId, PuzzlePart part){
		InterfaceTask task = new InterfaceTask();
		task.execute("cancelReservedPuzzleArtwork", classId,puzzleId, part);
	}
	
	public void getPuzzleArtwork(String puzzleId, PuzzlePart part){
		InterfaceTask task = new InterfaceTask();
		task.execute("getPuzzleArtwork", puzzleId, part);		
	}
	
	public void getArtClass(String classId){
		InterfaceTask task = new InterfaceTask();
		task.execute("getArtClass", classId);			
	}
	
	public void getPuzzle(String puzzleId){
		InterfaceTask task = new InterfaceTask();
		task.execute("getPuzzle", puzzleId);			
	}
	
//	public void getReservedPuzzleArtworkList(String userIdx){
//		InterfaceTask task = new InterfaceTask();
//		task.execute("getReservedPuzzleArtworkList", userIdx);		
//	}
//	
	
	public boolean runMethod(Object[] objects){
		String methodName = (String)objects[0];
		
		if (methodName.equals("getPuzzleList")){
			String classId = (String)objects[1];
			
			puzzleList = ArtClassHandler.getPuzzleList(classId);
			
			if (puzzleList != null)
				return true;
		}else if (methodName.equals("getPuzzleArtworkList")){
			String classId = (String)objects[1];
			String puzzleId = (String)objects[2];
			puzzleArtworkList = ArtClassHandler.getPuzzleArtworkList(classId,puzzleId);
			
			if (puzzleArtworkList != null)
				return true;
		}else if (methodName.equals("getArtClass")){
			String classId = (String)objects[1];
			artClass = ArtClassHandler.getArtClassInfo(classId);
			if(artClass != null)
				return true;
		}else if (methodName.equals("getPuzzle")){
			String puzzleId = (String)objects[1];
			puzzle = ArtClassHandler.getPuzzle(puzzleId);
			if(puzzle != null)
				return true;
		}
//		else if (methodName.equals("addPuzzle")){
//			String classId = (String)objects[1];
//			String subjectId = (String)objects[2];
//			File sketchFile = (File)objects[3];
//			
//			return ClassSubjectHandler.addPuzzle(classId, subjectId, sketchFile);
//		}
		else if (methodName.equals("reservePuzzleArtwork")){
			String classId =  (String)objects[1];
			String puzzleId = (String)objects[2];
			PuzzlePart part = (PuzzlePart)objects[3];
	
			
			return ArtClassHandler.reservePuzzleArtwork(classId,puzzleId, part.name(), null);
		}else if (methodName.equals("cancelReservedPuzzleArtwork")){
			String classId =  (String)objects[1];
			String puzzleId = (String)objects[2];
			PuzzlePart part = (PuzzlePart)objects[3];
			
			return ArtClassHandler.cancelPuzzleArtwork(classId,puzzleId, part.name(), null);
		}else if (methodName.equals("addPuzzleArtwork")){
			String classId =  (String)objects[1];
			String puzzleId = (String)objects[2];
			PuzzlePart part = (PuzzlePart)objects[3];
			String artworkId = (String)objects[4];
			
			return ArtClassHandler.registPuzzleArtwork(classId,puzzleId, part.name(), artworkId);
		}else if (methodName.equals("getPuzzleArtwork")){
			String puzzleId = (String)objects[1];
			PuzzlePart part = (PuzzlePart)objects[2];
			
			puzzleArtworkEntity = null;
			puzzleArtworkEntity = ArtClassHandler.getPuzzleArtwork(puzzleId, part.name());
			
			if (puzzleArtworkEntity != null)
				return true;
		}
//		else if (methodName.equals("getReservedPuzzleArtworkList")){
//			String userIdx = (String)objects[1];
//			
//			puzzleArtworkList = null;
//			puzzleArtworkList = ClassSubjectHandler.getReservedPuzzleArtworkList(userIdx);
//			
//			if (puzzleArtworkList != null)
//				return true;
//		}
		
		return false;
	}
	
	class InterfaceTask extends AsyncTask<Object,Void,Boolean>
	{
		@Override
		protected Boolean doInBackground(Object... params)
		{
			return runMethod(params);
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			if (callback != null)
				callback.onComplete(ArtClassHandleTask.this, result);
				
			super.onPostExecute(result);
		}
	}
	
	public interface Callback
	{
		void onComplete(ArtClassHandleTask handler, boolean result);
	}
}
