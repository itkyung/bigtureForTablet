package com.clockworks.android.tablet.bigture.serverInterface.entities;


import java.util.Date;

import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;



import android.os.Parcel;
import android.os.Parcelable;

public class ArtworkEntity implements Parcelable{
	public String artworkId;
	public String title;
	public String comment;
	public String imagePath;
	public String thumbPath;
	public String drawingPath;
	public Date created;
	public String userId;
	public String ownerName;

	public String userJob;
	public String userRegion;
	public String userCountry;
	public String userCountryKr;
	public int friendStatus;
	
	public String referenceName;
	public String referenceType;
	public int    contestRank;

	public boolean    opend;
	
	public int    loveCount;
	public int    awesomeCount;
	public int    wowCount;
	public int    funCount;
	public int    fantasticCount;
	public int    commentsCount;
	public int    score;
	public int    createYear;
	public boolean spam;
	public boolean collected;	//이미 collection한것인지 여부..
	
	
	
	public String profileImagePath;
	public String profileText;
	
	public boolean selected;
	
	public ArtworkEntity(){
		
	}
	
	public ArtworkEntity(Parcel parcel){
		readFromParcel(parcel);
	}
	
	public void importData(JSONObject artworkObject) throws Exception{
		if (artworkObject.has("id") && !artworkObject.isNull("id"))
			this.artworkId     = artworkObject.getString("id");
		
		if (artworkObject.has("title"))
			this.title         = artworkObject.getString("title");
		if (artworkObject.has("comment"))
			this.comment       = artworkObject.getString("comment");
		if (artworkObject.has("photo"))
			this.imagePath     = artworkObject.getString("photo");
		if (artworkObject.has("thumbnail"))
			this.thumbPath     = artworkObject.getString("thumbnail");
		if (artworkObject.has("drawingPath") && !artworkObject.isNull("drawingPath"))
			this.drawingPath = artworkObject.getString("drawingPath");
		if (artworkObject.has("created")){
			Long date = artworkObject.getLong("created");
			this.created  = new Date(date);
		}
		if (artworkObject.has("ownerId"))
			this.userId        = artworkObject.getString("ownerId");
		if (artworkObject.has("ownerName"))
			this.ownerName = artworkObject.getString("ownerName");
		
		if (artworkObject.has("referenceName"))
			this.referenceName = artworkObject.getString("referenceName");
		
		if (artworkObject.has("referenceType"))
			this.referenceType = artworkObject.getString("referenceType");
		
		if (artworkObject.has("ownerJob") && !artworkObject.isNull("ownerJob"))
			this.userJob = artworkObject.getString("ownerJob");
		if (artworkObject.has("ownerRegion"))
			this.userRegion    = artworkObject.getString("ownerRegion");
		if (artworkObject.has("ownerCountry"))
			this.userCountry   = artworkObject.getString("ownerCountry");
		if (artworkObject.has("ownerCountryKr"))
			this.userCountryKr = artworkObject.getString("ownerCountryKr");
		
		if (artworkObject.has("ownerPhoto"))
			this.profileImagePath = artworkObject.getString("ownerPhoto");
		
		if (artworkObject.has("ownerComment"))
			this.profileText = artworkObject.getString("ownerComment");
		
		
		if (artworkObject.has("opend")){
			opend = artworkObject.getBoolean("opend");
		}else{
			opend = false;
		}

		
		if (artworkObject.has("rank"))
			this.contestRank  = artworkObject.getInt("rank");
		else
			this.contestRank = 0;

		if (artworkObject.has("loveCount") && !artworkObject.isNull("loveCount"))
			this.loveCount     = artworkObject.getInt("loveCount");
		else
			this.loveCount = 0;

		if (artworkObject.has("awesomeCount") && !artworkObject.isNull("awesomeCount"))
			this.awesomeCount     = artworkObject.getInt("awesomeCount");
		else
			this.awesomeCount = 0;
		
		if (artworkObject.has("wowCount") && !artworkObject.isNull("wowCount"))
			this.wowCount     = artworkObject.getInt("wowCount");
		else
			this.wowCount = 0;
		
		if (artworkObject.has("funCount") && !artworkObject.isNull("funCount"))
			this.funCount     = artworkObject.getInt("funCount");
		else
			this.funCount = 0;
		
		if (artworkObject.has("fantasticCount") && !artworkObject.isNull("fantasticCount"))
			this.fantasticCount     = artworkObject.getInt("fantasticCount");
		else
			this.fantasticCount = 0;	
		
		if (artworkObject.has("commentsCount") && !artworkObject.isNull("commentsCount"))
			this.commentsCount     = artworkObject.getInt("commentsCount");
		else
			this.commentsCount = 0;	
		
		if (artworkObject.has("score") && !artworkObject.isNull("score"))
			this.score     = artworkObject.getInt("score");
		else
			this.score = 0;	
		
		if (artworkObject.has("createYear") && !artworkObject.isNull("createYear"))
			this.createYear     = artworkObject.getInt("createYear");
		else
			this.createYear = 0;	
		
		if (artworkObject.has("spam") && !artworkObject.isNull("spam"))
			this.spam     = artworkObject.getBoolean("spam");
		else
			this.spam = false;			
		
		if (artworkObject.has("collected") && !artworkObject.isNull("collected"))
			this.collected     = artworkObject.getBoolean("collected");
		else
			this.collected = false;		
		
	}
	
	public String getArtworkImageURL(){
		return ServerStaticVariable.ImageURL + imagePath;		
	}
	
	public String getThumbnailURL(){
		return ServerStaticVariable.ThumbURL + thumbPath;
	}
	
	public String getDrawingFileURL(){
		if (drawingPath == null || drawingPath.length() == 0)
			return null;
		
		return ServerStaticVariable.ImageURL + drawingPath;		
	}
	
	public String getProfileImageURL(){
		if (profileImagePath != null && profileImagePath.length() > 0)
			return ServerStaticVariable.ProfileURL + profileImagePath;
		else
			return null;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		out.writeString(artworkId);
		out.writeString(title);
		out.writeString(comment);
		
		out.writeInt(opend ? 1 : 0);
		out.writeString(imagePath);
		out.writeString(thumbPath);
		out.writeString(drawingPath);
		out.writeLong(created.getTime());
		out.writeString(userId);
		out.writeString(ownerName);
		out.writeString(userJob);
		out.writeString(userRegion);
		out.writeString(userCountry);
		out.writeString(userCountryKr);
		out.writeString(profileImagePath);
		out.writeString(profileText);
		out.writeInt(friendStatus);
		
		out.writeString(referenceName);
		out.writeString(referenceType);
		out.writeInt(contestRank);
		out.writeInt(loveCount);
		out.writeInt(awesomeCount);
		out.writeInt(wowCount);
		out.writeInt(funCount);
		out.writeInt(fantasticCount);
		out.writeInt(commentsCount);
		out.writeInt(score);
		out.writeInt(createYear);
		out.writeInt(spam ? 1 : 0);
		out.writeInt(collected ? 1 : 0);
		
	
	}
	
	protected void readFromParcel(Parcel in)
	{
		artworkId      = in.readString();
		title      = in.readString();
		comment    = in.readString();
	
		opend    = in.readInt() == 1 ? true : false;
		imagePath  = in.readString();
		thumbPath  = in.readString();
		drawingPath = in.readString();
		created =  new Date(in.readLong());
		userId  = in.readString();
		ownerName  = in.readString();
	
		userJob        = in.readString();
		userRegion     = in.readString();
		userCountry    = in.readString();
		userCountryKr  = in.readString();
		profileImagePath = in.readString();
		profileText = in.readString();
		friendStatus = in.readInt();
		
		referenceName = in.readString();
		referenceType = in.readString();
		contestRank  = in.readInt();
		loveCount     = in.readInt();
		awesomeCount = in.readInt();
		wowCount = in.readInt();
		funCount = in.readInt();
		fantasticCount = in.readInt();
		commentsCount = in.readInt();
		score = in.readInt();
		createYear = in.readInt();
		spam = in.readInt() == 1 ? true : false;
		collected = in.readInt() == 1 ? true : false;
		
	
		
	}
	
	public static final Parcelable.Creator<ArtworkEntity> CREATOR = new Parcelable.Creator<ArtworkEntity>()
	{
		public ArtworkEntity createFromParcel(Parcel in)
		{
			return new ArtworkEntity(in);
		}
		
		public ArtworkEntity[] newArray(int size)
		{
			return new ArtworkEntity[size];
		}
	};
}
