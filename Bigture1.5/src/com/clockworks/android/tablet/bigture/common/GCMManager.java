package com.clockworks.android.tablet.bigture.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.clockworks.android.tablet.bigture.serverInterface.entities.ArtworkEntity;
import com.clockworks.android.tablet.bigture.serverInterface.entities.GCMessage;
import com.clockworks.android.tablet.bigture.serverInterface.handler.GCMServiceHandler;
import com.clockworks.android.tablet.bigture.utils.file.FileUtil;

import com.google.android.gcm.GCMRegistrar;

public class GCMManager
{
	public static final String SENDER_ID = "184879146181";
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;

	public static String getRegistrationId(Context context)
	{
		return GCMRegistrar.getRegistrationId(context);
	}
	
	/**
	 * GCM Server에 디바이스를 등록한다.
	 * @param context
	 */
	public static void registerGCM(final Context context)
	{
		GCMRegistrar.checkDevice(context);
		GCMRegistrar.checkManifest(context);
		
		String registrationId = GCMRegistrar.getRegistrationId(context);
		
		if (registrationId.equals(""))
			GCMRegistrar.register(context, GCMManager.SENDER_ID);
	}

	/**
	 * 3rd-party 메시지 서버에 registration id를 등록한다. 
	 * @param context
	 * @param registrationId
	 */
	public static void register(Context context, String registrationId)
	{
		if (GCMServiceHandler.updateGCMRegistrationId(AccountManager.getUserIdx(), registrationId))
			GCMRegistrar.setRegisteredOnServer(context, true);
	}
	
	/**
	 * 3rd-party 메시지 서버에 registration id를 삭제한다.
	 * @param context
	 */
	public static void unregister(Context context)
	{
		if (AccountManager.isLogin())
		{
			if (GCMServiceHandler.updateGCMRegistrationId(AccountManager.getUserIdx(), null))
				GCMRegistrar.setRegisteredOnServer(context, false);
		}
	}
	
	public static List<GCMessage> loadMessage(Context context)
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		List<GCMessage> messageList = new ArrayList<GCMessage>();
		
		try
		{
			String filePath = FileUtil.getMessagePath(context) + File.separator + "gcm_message.msg";
			File file = new File(filePath);
			if (!file.isFile())
				return null;
			
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			
			int count = ois.readInt();
			
			for (int i = 0; i < count; i++)
			{
				GCMessage message = new GCMessage();
				message.read(ois);
				messageList.add(message);
			}
		}
		catch(Exception e)
		{
			messageList = null;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				ois.close();
				fis.close();
			}
			catch(Exception e)
			{
			}
		}
		
		return messageList;
	}
	
	public static void saveMessage(Context context, List<GCMessage> messageList)
	{
		String filePath = FileUtil.getMessagePath(context) + File.separator + "gcm_message.msg";
		
		if (messageList == null || messageList.size() == 0)
		{
			FileUtil.removeFile(filePath);
			return;
		}
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try
		{
			File file = new File(filePath);
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			oos.writeInt(messageList.size());
			
			for (int i = 0; i < messageList.size(); i++)
			{
				GCMessage message = messageList.get(i);
				message.write(oos);
			}
		}
		catch(Exception e)
		{
			messageList = null;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				oos.close();
				fos.close();
			}
			catch(Exception e)
			{
			}
		}		
	}
	
	public static void handleGCMessage(final Context context, long messageIndex)
	{
		if (!AccountManager.isLogin())
		{
			BigtureEnvironment.showAlertMessage(context, 16);
			return;
		}
		
		GCMessage msg2 = null;
		List<GCMessage> messageList = GCMManager.loadMessage(context);
		
		if (messageList != null)
		{
			for (int i = 0; i < messageList.size(); i++)
			{
				GCMessage msg = messageList.get(i);
				if (msg.index == messageIndex)
				{
					msg2 = messageList.remove(i);
					GCMManager.saveMessage(context, messageList);
					break;
				}
			}
		}

		final GCMessage message = msg2;

		if (message != null)
		{
			
		//FIXME 임시로 주석처리함.
//			if (message.action == GCMessage.ACTION_STICKER || message.action == GCMessage.ACTION_COMMENT)
//			{
//				ArtworkIndexReader reader = new ArtworkIndexReader(new ArtworkIndexReader.ArtworkIndexReaderListener()
//				{	
//					@Override
//					public void onSuccess(ArtworkIndexReader reader, List<ArtworkEntity> entityList, int artworkCount)
//					{
//						int position = 0;
//						for (int i = 0; i < entityList.size(); i++)
//						{
//							if (message.artworkIdx.equals(entityList.get(i).artworkId))
//							{
//								position = i;
//								break;
//							}
//						}
//						
//						Intent intent = new Intent((Activity)context, ArtworkDetailActivity.class);
//						intent.putParcelableArrayListExtra("artworkList", (ArrayList<ArtworkEntity>)entityList);
//						intent.putExtra("currentIndex", position);
//						intent.putExtra("title", "My Artworks");
//						context.startActivity(intent);
//					}
//					
//					@Override
//					public void onFail(ArtworkIndexReader reader)
//					{
//						BigtureEnvironment.showAlertMessage(context, 99);
//					}
//				});
//				
//				reader.readMyArtworkIndex(AccountManager.getUserIdx(), InterfaceProtocol.SORT_RECENT, 0, -1);				
//			}
//			else if (message.action == GCMessage.ACTION_UPLOAD_ARTWORK)
//			{
//				ArtworkIndexReader reader = new ArtworkIndexReader(new ArtworkIndexReader.ArtworkIndexReaderListener()
//				{	
//					@Override
//					public void onSuccess(ArtworkIndexReader reader, List<ArtworkEntity> entityList, int artworkCount)
//					{
//						int position = 0;
//						for (int i = 0; i < entityList.size(); i++)
//						{
//							if (message.artworkIdx.equals(entityList.get(i).artworkId))
//							{
//								position = i;
//								break;
//							}
//						}
//						
//						Intent intent = new Intent(context, ArtworkDetailActivity.class);
//						intent.putParcelableArrayListExtra("artworkList", (ArrayList<ArtworkEntity>)entityList);
//						intent.putExtra("currentIndex", position);
//						intent.putExtra("title", "Artworks");
//						context.startActivity(intent);
//					}
//					
//					@Override
//					public void onFail(ArtworkIndexReader reader)
//					{
//						BigtureEnvironment.showAlertMessage(context, 99);
//					}
//				});
//				
//				reader.readUserArtworkIndex(message.userIdx, InterfaceProtocol.SORT_RECENT, 0, -1);				
//			}
//			else if (message.action == GCMessage.ACTION_WAIT_CLASS_ENTRANCE ||
//					message.action == GCMessage.ACTION_APPROVED_ENTRANCE ||
//					message.action == GCMessage.ACTION_INVITED_CLASS)
//			{
//				ClassHandleTask task = new ClassHandleTask(new ClassHandleTask.Callback()
//				{
//					@Override
//					public void onComplete(ClassHandleTask task, boolean result)
//					{
//						if (result)
//						{
//							Intent intent = new Intent(context, ClassMainActivity.class);
//							intent.putExtra("classEntity", task.classEntity);
//							context.startActivity(intent);
//						}
//						else
//						{
//							BigtureEnvironment.showAlertMessage(context, 99);
//						}
//					}
//				});
//				
//				task.readClass(message.classIdx);				
//			}
//			else if (message.action == GCMessage.ACTION_WAIT_GUIDE_COMMENT || message.action == GCMessage.ACTION_PUZZLE_FINISHED)
//			{
//				ClassHandleTask task = new ClassHandleTask(new ClassHandleTask.Callback()
//				{
//					@Override
//					public void onComplete(ClassHandleTask task, boolean result)
//					{
//						if (result)
//						{
//							Intent intent = new Intent(context, ClassSubjectActivity.class);
//							intent.putExtra("classEntity", task.classEntity);
//							intent.putExtra("currentSubjectIdx", message.subjectIdx);
//							intent.putExtra("joinStatus", 1);
//							context.startActivity(intent);
//						}
//						else
//						{
//							BigtureEnvironment.showAlertMessage(context, 99);
//						}
//					}
//				});
//				
//				task.readClass(message.classIdx);
//			}
//			else if (message.action == GCMessage.ACTION_NOTICE)
//			{
//				InfoDialog dialog = new InfoDialog(context);
//				dialog.show();
//			}
//			else if (message.action == GCMessage.ACTION_NEWS)
//			{
//				
//			}
//			else if (message.action == GCMessage.ACTION_ADD_FRIEND)
//			{
//				Intent intent = new Intent(context, MyFriendActivity.class);
//				context.startActivity(intent);				
//			}
		}
	}
}
