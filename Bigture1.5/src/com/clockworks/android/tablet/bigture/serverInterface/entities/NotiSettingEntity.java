package com.clockworks.android.tablet.bigture.serverInterface.entities;

import org.json.JSONObject;

public class NotiSettingEntity {
	
	public boolean talkCommentPush; //댓글 달렸을때 
	
	public boolean likePush; //like할때 
	
	public boolean picMePush; //pic me 할때 
	
	public boolean contestPush; //컨테스트가 있을때 
	
	public boolean myClassPush; //내가 참여한 클래스 소식 
	
	public boolean cardPush; //카드 수신 알림 
	
	public boolean storyPush; //스토리 오픈소식 
	
	public boolean classPush;  //클래스 오픈소식 
	
	public boolean pushAlert;
	
	public NotiSettingEntity(){
		
	}
	
	public void importData(JSONObject json) throws Exception{
		if(json.has("talkCommentPush")){
			this.talkCommentPush = json.getBoolean("talkCommentPush");
		}else{
			this.talkCommentPush = false;
		}
		if(json.has("likePush")){
			this.likePush = json.getBoolean("likePush");
		}else{
			this.likePush = false;
		}
		if(json.has("picMePush")){
			this.picMePush = json.getBoolean("picMePush");
		}else{
			this.picMePush = false;
		}
		if(json.has("contestPush")){
			this.contestPush = json.getBoolean("contestPush");
		}else{
			this.contestPush = false;
		}
		if(json.has("myClassPush")){
			this.myClassPush = json.getBoolean("myClassPush");
		}else{
			this.myClassPush = false;
		}
		if(json.has("cardPush")){
			this.cardPush = json.getBoolean("cardPush");
		}else{
			this.cardPush = false;
		}
		if(json.has("storyPush")){
			this.storyPush = json.getBoolean("storyPush");
		}else{
			this.storyPush = false;
		}
		if(json.has("classPush")){
			this.classPush = json.getBoolean("classPush");
		}else{
			this.classPush = false;
		}
		
		if(json.has("pushAlert")){
			this.pushAlert = json.getBoolean("pushAlert");
		}else{
			this.pushAlert = true;
		}
	}
	
}
