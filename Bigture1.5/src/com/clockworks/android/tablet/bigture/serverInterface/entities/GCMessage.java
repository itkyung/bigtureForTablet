package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.clockworks.android.tablet.bigture.utils.DateUtil;




import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class GCMessage extends ProfileEntity
{
	public static final int ACTION_STICKER = 101;
	public static final int ACTION_COMMENT = 102;
	public static final int ACTION_ADD_FRIEND = 111;
	public static final int ACTION_DEL_FRIEND = 112;
	public static final int ACTION_UPLOAD_ARTWORK = 113;
	public static final int ACTION_NOTICE = 121;
	public static final int ACTION_NEWS = 122;
	public static final int ACTION_NEW_CONTEST = 131;
	public static final int ACTION_INVITED_CLASS = 141;
	public static final int ACTION_WAIT_CLASS_ENTRANCE = 142;
	public static final int ACTION_WAIT_GUIDE_COMMENT = 143;
	public static final int ACTION_PUZZLE_FINISHED = 144;
	public static final int ACTION_APPROVED_ENTRANCE = 145;
	
	public int index;
	public int action;
	public String message;
	
	public String artworkIdx;
	public String userIdx;
	//public String userName;
	
	public String classIdx;
	//public String className;
	
	public String noticeIdx;
	public String newsIdx;
	
	public String contestIdx;
	public String subjectIdx;
	public String layerIdx;
	//public String contestName;
	
	public Date regDate;
	
	
	public static GCMessage newInstance(Bundle bundle)
	{
		GCMessage msg = null;
		
		if (bundle == null)
			return null;
		
		long t1 = DateUtil.createDate(2000, 1, 1).getTime();
		long t2 = System.currentTimeMillis();
		
		int index = (int)(t2 - t1);
		String strAction = bundle.getString("action");
		String strDate = bundle.getString("date");
		
		int action = Integer.parseInt(strAction);
		
		if (action == GCMessage.ACTION_STICKER) // 자신의 그림에 스티커
		{
			msg = new GCMessage(index, action);
			msg.artworkIdx = bundle.getString("artworkIdx");
			msg.userIdx    = bundle.getString("userIdx");
			
			String userName = bundle.getString("userName");
			String message = userName + " has put the sticker on your artwork";
			
			msg.message = message;
		}
		else if (action == GCMessage.ACTION_COMMENT) // 자신의 그림에 댓글
		{
			msg = new GCMessage(index, action);
			msg.artworkIdx = bundle.getString("artworkIdx");
			msg.userIdx    = bundle.getString("userIdx");
			
			String userName = bundle.getString("userName");
			String message = userName + " has commented on your artwork";
			
			msg.message = message;
		}
		else if (action == GCMessage.ACTION_UPLOAD_ARTWORK) // 친구가 그림을 올릴 때
		{
			msg = new GCMessage(index, action);
			msg.artworkIdx = bundle.getString("artworkIdx");
			msg.userIdx  = bundle.getString("userIdx");

			String userName = bundle.getString("userName");
			String message = userName + " has uploaded his/her artwork";
			
			msg.message = message;
		}
		else if (action == GCMessage.ACTION_ADD_FRIEND)
		{
			msg = new GCMessage(index, action);
			msg.userIdx  = bundle.getString("userIdx");

			String userName = bundle.getString("userName");
			String message = "You become a friend of " + userName;
			
			msg.message = message;
		}
		else if (action == GCMessage.ACTION_DEL_FRIEND)
		{
			
		}
		else if (action == GCMessage.ACTION_INVITED_CLASS) // 전문가로부터 클래스 초청을 받을 때
		{
			msg = new GCMessage(index, action);
			msg.classIdx   = bundle.getString("classIdx");
			String className  = bundle.getString("className");
			msg.message = "Your are invited on special class('" + className + "')";
		}
		else if (action == GCMessage.ACTION_WAIT_CLASS_ENTRANCE)
		{
			msg = new GCMessage(index, action);
			msg.classIdx   = bundle.getString("classIdx");
			String userName = bundle.getString("userName");
			msg.message = userName + " is waiting for entrance to the class";
		}
		else if (action == GCMessage.ACTION_APPROVED_ENTRANCE)
		{
			msg = new GCMessage(index, action);
			msg.classIdx   = bundle.getString("classIdx");
			String className = bundle.getString("className");
			msg.message = "You were joined to the class '" + className + "'";			
		}
		else if (action == GCMessage.ACTION_WAIT_GUIDE_COMMENT)
		{
			msg = new GCMessage(index, action);
			msg.classIdx   = bundle.getString("classIdx");
			msg.subjectIdx = bundle.getString("subjectIdx");
			msg.message = "Artists are waiting for your guide comment";
		}
		else if (action == GCMessage.ACTION_PUZZLE_FINISHED)
		{
			msg = new GCMessage(index, action);
			msg.classIdx   = bundle.getString("classIdx");
			msg.subjectIdx = bundle.getString("subjectIdx");
			msg.message = "Puzzle type subject has been finished.";
		}
		else if (action == GCMessage.ACTION_NEW_CONTEST) // 새로운 콘테스트가 시작
		{
			msg = new GCMessage(index, action);
			msg.contestIdx  = bundle.getString("contestIdx");
			String contestName = bundle.getString("contestName");
			
			msg.message = "New contest(" + contestName + ") begins";
		}
		else if (action == GCMessage.ACTION_NOTICE)
		{
			msg = new GCMessage(index, action);
			msg.contestIdx  = bundle.getString("noticeIdx");
			msg.message = "There is a new notice";
		}
		else if (action == GCMessage.ACTION_NEWS)
		{
			msg = new GCMessage(index, action);
			msg.contestIdx  = bundle.getString("newsIdx");
			msg.message = "There is a news";				
		}

		if (msg != null)
		{
			if (strDate == null)
				msg.regDate = new Date();
			else
				msg.regDate = DateUtil.createDateFromString(strDate, "yyyy-MM-dd HH:mm:ss");
		}
		
		return msg;
	}
	
	public GCMessage()
	{
		
	}
	
	public GCMessage(int index, int action)
	{
		this.index = index;
		this.action = action;
	}
	
	public GCMessage(Parcel in)
	{
		readFromParcel(in);
	}
	
	public void read(ObjectInputStream ois) throws IOException
	{
		index = ois.readInt();
		action = ois.readInt();
		message = ois.readUTF();
		
		String strDate = ois.readUTF();
		if (strDate == null)
			regDate = new Date();
		else
			regDate = DateUtil.createDateFromString(strDate, "yyyy-MM-dd HH:mm:ss");
		
		if (action == ACTION_STICKER)
		{
			artworkIdx = ois.readUTF();
			userIdx = ois.readUTF();
		}
		else if (action == ACTION_COMMENT)
		{
			artworkIdx = ois.readUTF();
			userIdx = ois.readUTF();
		}
		else if (action == ACTION_ADD_FRIEND)
		{
			userIdx = ois.readUTF();
		}
		else if (action == ACTION_DEL_FRIEND)
		{
			userIdx = ois.readUTF();
		}
		else if (action == ACTION_UPLOAD_ARTWORK)
		{
			artworkIdx = ois.readUTF();
			userIdx = ois.readUTF();
		}
		else if (action == ACTION_NOTICE)
		{
			noticeIdx = ois.readUTF();
		}
		else if (action == ACTION_NEWS)
		{
			newsIdx = ois.readUTF();
		}
		
		else if (action == ACTION_NEW_CONTEST)
		{
			contestIdx = ois.readUTF();
		}
		else if (action == ACTION_WAIT_CLASS_ENTRANCE || action == ACTION_APPROVED_ENTRANCE
				|| action == ACTION_INVITED_CLASS)
		{
			classIdx = ois.readUTF();
		}
		else if (action == ACTION_WAIT_GUIDE_COMMENT)
		{
			classIdx = ois.readUTF();
			subjectIdx = ois.readUTF();
		}
		else if (action == ACTION_PUZZLE_FINISHED)
		{
			classIdx = ois.readUTF();
			subjectIdx = ois.readUTF();
		}
	}
	
	public void write(ObjectOutputStream oos) throws IOException
	{
		oos.writeInt(index);
		oos.writeInt(action);
		oos.writeUTF(message);
		
		String strDate = DateUtil.getDateFormatString(regDate, "yyyy-MM-dd HH:mm:ss");
		oos.writeUTF(strDate);

		if (action == ACTION_STICKER)
		{
			oos.writeUTF(artworkIdx);
			oos.writeUTF(userIdx);
		}
		else if (action == ACTION_COMMENT)
		{
			oos.writeUTF(artworkIdx);
			oos.writeUTF(userIdx);
		}
		else if (action == ACTION_ADD_FRIEND)
		{
			oos.writeUTF(userIdx);
		}
		else if (action == ACTION_DEL_FRIEND)
		{
			oos.writeUTF(userIdx);
		}
		else if (action == ACTION_UPLOAD_ARTWORK)
		{
			oos.writeUTF(artworkIdx);
			oos.writeUTF(userIdx);
		}
		else if (action == ACTION_NOTICE)
		{
			oos.writeUTF(noticeIdx);
		}
		else if (action == ACTION_NEWS)
		{
			oos.writeUTF(newsIdx);
		}
		
		else if (action == ACTION_NEW_CONTEST)
		{
			oos.writeUTF(contestIdx);
		}
		else if (action == ACTION_INVITED_CLASS || 
				action == ACTION_WAIT_CLASS_ENTRANCE || action == ACTION_APPROVED_ENTRANCE)
		{
			oos.writeUTF(classIdx);
		}
		else if (action == ACTION_WAIT_GUIDE_COMMENT)
		{
			oos.writeUTF(classIdx);
			oos.writeUTF(subjectIdx);
		}
		else if (action == ACTION_PUZZLE_FINISHED)
		{
			oos.writeUTF(classIdx);
			oos.writeUTF(subjectIdx);
		}
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags)
	{
		super.writeToParcel(out, flags);
		
		out.writeInt(index);
		out.writeInt(action);
		out.writeString(message);
		
		out.writeString(artworkIdx);
		out.writeString(userIdx);
		out.writeString(classIdx);
		out.writeString(noticeIdx);
		out.writeString(newsIdx);
		out.writeString(contestIdx);
		out.writeString(subjectIdx);
		out.writeString(layerIdx);
	}
	
	protected void readFromParcel(Parcel in)
	{
		super.readFromParcel(in);

		index = in.readInt();
		action = in.readInt();
		message = in.readString();
		
		artworkIdx = in.readString();
		userIdx = in.readString();
		classIdx = in.readString();
		noticeIdx = in.readString();
		newsIdx = in.readString();
		contestIdx = in.readString();
		subjectIdx = in.readString();
		layerIdx = in.readString();
	}

	public static final Parcelable.Creator<GCMessage> CREATOR = new Parcelable.Creator<GCMessage>()
	{
		public GCMessage createFromParcel(Parcel in)
		{
			return new GCMessage(in);
		}
		
		public GCMessage[] newArray(int size)
		{
			return new GCMessage[size];
		}
	};

}
