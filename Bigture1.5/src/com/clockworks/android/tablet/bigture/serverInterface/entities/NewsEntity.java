package com.clockworks.android.tablet.bigture.serverInterface.entities;

import java.util.List;


import org.json.JSONObject;

import com.clockworks.android.tablet.bigture.serverInterface.ServerStaticVariable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {"idx":"90","member":"121","title":"\ucf54\ubfd4\uc18c, \uc774\uc815\uc6c5(10\uc138)",
 * "contents":"content","photo":"20121129\/121_20121129115345_image.tmp","thumbnail":"20121129\/121_20121129115345_image.tmp","create_time":"2012-11-29 11:53:45"}
 * 
 * @author mobility_macpro
 *
 */
public class NewsEntity implements Parcelable
{
	public String index;
	
	public String title;
	public String contents;
	public String photoPath;
	public String thumbPath;
	public String createTime;
	public int imageCount;
	
	public List<NewsImageEntity> imageList;
	
	public NewsEntity()
	{
		
	}
	
	public NewsEntity(Parcel parcel)
	{
		readFromParcel(parcel);
	}

	public void importData(JSONObject newsObjects) throws Exception
	{
		index = newsObjects.getString("id");
		
		title = newsObjects.getString("title");
		contents = newsObjects.getString("contents");
		contents = contents.replaceAll("\\\\n", "\\\n");
		if(newsObjects.has("photo")){
			photoPath = newsObjects.getString("photo");
		}
		if(newsObjects.has("thumbnail")){
			thumbPath = newsObjects.getString("thumbnail");
		}
		
		createTime = newsObjects.getString("created");
		imageCount = newsObjects.getInt("imageCount");
	}

	public String getThumbnailURL(){
		if (thumbPath != null && thumbPath.length() > 0)
			return ServerStaticVariable.NewsThumbURL + thumbPath;
		else
			return null;
	}

	public String getImageURL()	{
		if (photoPath != null && photoPath.length() > 0)
			return ServerStaticVariable.NewsImageURL + photoPath;
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
		out.writeString(index);
	
		out.writeString(title);
		out.writeString(contents);
		out.writeString(photoPath);
		out.writeString(thumbPath);
		out.writeString(createTime);
		out.writeInt(imageCount);		
	}
	
	protected void readFromParcel(Parcel in)
	{
		index      = in.readString();

		title      = in.readString();
		contents   = in.readString();
		photoPath  = in.readString();
		thumbPath  = in.readString();
		createTime = in.readString();
		imageCount = in.readInt();
	}
	
	public static final Parcelable.Creator<NewsEntity> CREATOR = new Parcelable.Creator<NewsEntity>()
	{
		public NewsEntity createFromParcel(Parcel in)
		{
			return new NewsEntity(in);
		}
		
		public NewsEntity[] newArray(int size)
		{
			return new NewsEntity[size];
		}
	};
}
