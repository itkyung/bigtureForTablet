package com.clockworks.android.tablet.bigture.common;

public enum NotificationType {
	COMMENT, //특정 artwork에 comment가 달림.
	SPAM, //신고되어서 숨김처리됨.
	NOTICE, //공지사항 
	NEW_VERSION, //신규버전 업 
	CONTEST, //컨테스트 관련
	CONTEST_DUE,//컨테스트 마감하루전.
	CONTEST_WINNER,
	ARTCLASS, //클래스 오픈,
	INVITE_CLASS,
	POSTCARD,//Card를 받음.
	LIKEU, //나를 like함. 
	
	REQ_PICTURE, //그림요청받음.
	PUZZLE // 퍼즐완성됨.
}
