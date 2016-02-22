package com.clockworks.android.tablet.bigture.views.sketchbook;



import com.clockworks.android.tablet.bigture.common.PuzzlePart;

import android.os.Parcel;
import android.os.Parcelable;

public class DrawingPurpose implements Parcelable{
	public static String storeKey = "Purpose";
	
	public static final int NORMAL = 0;
	public static final int STORY = 1;
	public static final int ARTCLASS = 2;
	public static final int CONTEST = 3;
	public static final int POSTCARD = 4;
	
	public int reason; 

	public String contestId;
	public String postcardId;
	public String storyId;
	public int pageNo; // pageNo = 0 : after reading
	public boolean afterReading;
	public String pageId;
	
	public String classId;
	public boolean puzzle;	//puzzle인지 여부.
	
	public String puzzleId;
	public PuzzlePart puzzlePart;
	
	
	// special class
	public String backgroundImageURL;
	
	public DrawingPurpose(){
		afterReading = false;
	}
	
	public DrawingPurpose(Parcel parcel)
	{
		readFromParcel(parcel);
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags){
		out.writeInt(reason);
		out.writeString(contestId);
		out.writeString(storyId);
		out.writeInt(pageNo);
		out.writeString(classId);
		out.writeString(puzzleId);
		out.writeString(puzzlePart == null ? null : puzzlePart.name());
		out.writeString(pageId);
		out.writeString(postcardId);
		out.writeInt(puzzle ? 1 : 0);
		out.writeInt(afterReading ? 1 : 0);
	}
	
	protected void readFromParcel(Parcel in){
		reason = in.readInt();
		
		contestId = in.readString();
		
		storyId = in.readString();
		pageNo  = in.readInt();

		classId = in.readString();
		
		puzzleId = in.readString();
		String p = in.readString();
		if(p != null)
			puzzlePart = PuzzlePart.valueOf(p);
		
		pageId   = in.readString();
		postcardId = in.readString();
		int i = in.readInt();
		puzzle = (i == 1) ? true : false;
		int a = in.readInt();
		afterReading = (a == 1) ? true : false;
	}

	public static final Parcelable.Creator<DrawingPurpose> CREATOR = new Parcelable.Creator<DrawingPurpose>(){
		public DrawingPurpose createFromParcel(Parcel in){
			return new DrawingPurpose(in);
		}
		
		public DrawingPurpose[] newArray(int size){
			return new DrawingPurpose[size];
		}
	};
}
