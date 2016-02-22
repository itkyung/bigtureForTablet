package com.clockworks.android.tablet.bigture.serverInterface;

public interface ServerStaticVariable {
	public static final String BASE_URL = "http://www.bigture.co.kr:8080/bigture";
	public static final String IMG_BASE_URL = "http://www.bigture.co.kr/";
	public static final String TERMS_BASE_URL = "http://www.bigture.co.kr/terms/";
	//public static final String IMG_BASE_URL = "http://192.168.0.48/";
	//public static final String BASE_URL = "http://192.168.219.155:8080/bigture";
	public static final String BASE_MOBILE_URL = BASE_URL + "/mobile";
	public static final String ProfileURL   = IMG_BASE_URL + "/upload/profile/";
	public static final String ImageDownloadURL = BASE_URL + "/image_download";
	public static final String ImageURL     = IMG_BASE_URL + "/upload/picture/";
	public static final String ThumbURL     = IMG_BASE_URL + "/upload/thumbnail/";
	public static final String NewsThumbURL = IMG_BASE_URL + "/upload/news/thumbnail/";
	public static final String NewsImageURL = IMG_BASE_URL + "/upload/news/picture/";
	public static final String StoryCoverURL = IMG_BASE_URL + "/upload/story/picture/";
	public static final String ClassCoverURL = IMG_BASE_URL + "/upload/class/picture/";
	public static final String ClassPuzzleSketchURL = IMG_BASE_URL + "/upload/class/puzzle/";
	
	
	public static final String SplashURL = BASE_MOBILE_URL + "/splashArtwork";
	public static final String MainURL = BASE_MOBILE_URL + "/getMainContents";
	public static final String ListNotificationURL = BASE_MOBILE_URL + "/getNotifications";
	public static final String DeleteNotificationURL = BASE_MOBILE_URL + "/deleteNotifications";
	public static final String FindSettingURL = BASE_MOBILE_URL + "/findPushSettings";
	public static final String ListNoticeURL = BASE_MOBILE_URL + "/listNotice";
	
	
	/**
	 * Login
	 */
	public static final String LoginURL = BASE_URL + "/loginAction";
	public static final String LoginByTokenURL = BASE_URL + "/loginByToken";
	public static final String LoginBySocialURL = BASE_URL + "/loginBySocial";
	public static final String LoginOutURL = BASE_URL + "/logout";
	public static final String CheckSnsAccountURL = BASE_URL + "/login/checkSnsAccount";
	
	public static final String GcmRegistURL = BASE_URL + "/login/updateGCMRegistrationId";
	
	/**
	 * Friends
	 */
	public static final String GetLikeYouURL = BASE_MOBILE_URL + "/friends/getLikeYouList";
	public static final String GetLikeMeURL = BASE_MOBILE_URL + "/friends/getLikeMeList";
	public static final String LikeYouURL = BASE_MOBILE_URL + "/friends/likeYou";
	public static final String UnLikeURL = BASE_MOBILE_URL + "/friends/unlike";
	public static final String GetGroupURL = BASE_MOBILE_URL + "/friends/getFriendGroups";
	public static final String SaveGroupURL = BASE_MOBILE_URL + "/friends/saveFriendGroup";
	public static final String DeleteGroupURL = BASE_MOBILE_URL + "/friends/deleteFriendGroup";
	public static final String GetFriendMemberURL = BASE_MOBILE_URL + "/friends/getFriendGroupMembers";
	public static final String FindUserURL = BASE_MOBILE_URL + "/friends/searchUsers";
	public static final String FindInFriendURL = BASE_MOBILE_URL + "/friends/searchLikeYous";
	public static final String GetGroupAndMembersURL = BASE_MOBILE_URL + "/friends/getGroupAndMembers";
	public static final String EditGroupMemberURL = BASE_MOBILE_URL + "/friends/editGroupMember";
	
	/**
	 * Account
	 */
	public static final String RegistAccountURL = BASE_MOBILE_URL + "/account/registAccount";
	public static final String CountryListURL = BASE_MOBILE_URL + "/account/getCountries";
	public static final String ResendActivateURL = BASE_MOBILE_URL + "/account/resendActivateLink";
	public static final String ResetPasswordURL = BASE_MOBILE_URL + "/account/resetPassword";
	public static final String UpdateAccountURL = BASE_MOBILE_URL + "/account/updateAccount";
	public static final String ChangePassworkdURL = BASE_MOBILE_URL + "/account/changePasswd";
	public static final String UploadProfileImageURL = BASE_MOBILE_URL + "/account/uploadProfileImage";
	
	/**
	 * Profile
	 */
	public static final String GetProfileURL = BASE_MOBILE_URL + "/profile/getProfile";
	public static final String SaveCareerURL = BASE_MOBILE_URL + "/profile/saveCareer";
	public static final String DeleteCareerURL = BASE_MOBILE_URL + "/profile/deleteCareer";
	public static final String ListCareerURL = BASE_MOBILE_URL + "/profile/listCareer";
	public static final String ListNewsURL = BASE_MOBILE_URL + "/profile/listNews";
	public static final String ListNewsImagesURL = BASE_MOBILE_URL + "/profile/listNewsImages";
	public static final String UploadNewsImageURL = BASE_MOBILE_URL + "/profile/uploadNewsImage";
	public static final String SaveNewsURL = BASE_MOBILE_URL + "/profile/saveNews";
	public static final String DeleteNewsURL = BASE_MOBILE_URL + "/profile/deleteNews";
	
	
	/**
	 * PostCards
	 */
	public static final String ListSendedPostCardsURL = BASE_MOBILE_URL + "/postcard/getSendedPostCards";
	public static final String ListReceivedPostCardsURL = BASE_MOBILE_URL + "/postcard/getReceivedPostCards";
	public static final String ListAllPostCardsURL = BASE_MOBILE_URL + "/postcard/getAllPostCards";
	public static final String SendPostCardURL = BASE_MOBILE_URL + "/postcard/sendPostCard";
	public static final String ChangeCardShareModeURL = BASE_MOBILE_URL + "/postcard/changeShareMode";
	public static final String RemoveFromListURL = BASE_MOBILE_URL + "/postcard/removeFromList";
	public static final String ViewPostCardURL = BASE_MOBILE_URL + "/postcard/viewPostCard";
	
	
	/**
	 * Artworks
	 */
	public static final String ListUserArtworksURL = BASE_MOBILE_URL + "/artwork/findUserArtworks";
	public static final String ListAllArtworksURL = BASE_MOBILE_URL + "/artwork/findAllArtworks";
	public static final String ListFriendsArtworksURL = BASE_MOBILE_URL + "/artwork/findFriendsArtworks";
	public static final String ListCollectedArtworksURL = BASE_MOBILE_URL + "/artwork/findCollectedArtworks";
	public static final String GetArtworkCommentsURL = BASE_MOBILE_URL + "/artwork/getArtworkComments";
	public static final String SendStickerURL = BASE_MOBILE_URL + "/artwork/sendSticker";
	public static final String SendCommentURL = BASE_MOBILE_URL + "/artwork/sendComment";
	public static final String AddArtworkCollectionURL = BASE_MOBILE_URL + "/artwork/addArtworkCollection";
	public static final String RemoveArtworkCollectionURL = BASE_MOBILE_URL + "/artwork/removeArtworkCollection";
	public static final String EditArtworkURL = BASE_MOBILE_URL + "/artwork/editArtwork";
	public static final String DeleteArtworkURL = BASE_MOBILE_URL + "/artwork/deleteArtwork";
	public static final String ReportSpamURL = BASE_MOBILE_URL + "/artwork/reportSpam";
	public static final String RequestToContestURL = BASE_MOBILE_URL + "/artwork/requestToContest";
	public static final String RegistArtworkURL = BASE_MOBILE_URL + "/artwork/registArtwork";
	public static final String LoadArtworkURL = BASE_MOBILE_URL + "/artwork/loadArtwork";
	public static final String DeleteCommentOrStickerURL = BASE_MOBILE_URL + "/artwork/deleteCommentOrSticker";
	public static final String GetTalkmapDataURL = BASE_MOBILE_URL + "/artwork/getTalkMapData";
	
	/**
	 * Contest
	 */
	public static final String ListActiveContestURL = BASE_MOBILE_URL + "/contest/listActiveContest";
	public static final String ListAllContestURL = BASE_MOBILE_URL + "/contest/listAllContest";
	public static final String ListContestArtworkURL = BASE_MOBILE_URL + "/contest/listContestArtworks";
	public static final String listWinnerURL = BASE_MOBILE_URL + "/contest/listWinner";
	public static final String GetContestURL = BASE_MOBILE_URL + "/contest/getContestInfo";
	
	/**
	 * Story
	 */
	public static final String ListUserStoryURL = BASE_MOBILE_URL + "/story/findUserStory";
	public static final String ListOwnStoryURL = BASE_MOBILE_URL + "/story/findOwnStory";
	public static final String ListJoinedStoryURL = BASE_MOBILE_URL + "/story/findJoinedStory";
	public static final String ListCollectedStoryURL = BASE_MOBILE_URL + "/story/findCollectedStory";
	public static final String ListAllStoryURL = BASE_MOBILE_URL + "/story/findAllStory";
	public static final String ListStoryByExpertsURL = BASE_MOBILE_URL + "/story/findStoryByExperts";
	public static final String AddStoryCollectionURL = BASE_MOBILE_URL + "/story/addStoryCollection";
	public static final String RemoveStoryCollectionURL = BASE_MOBILE_URL + "/story/removeStoryCollection";
	public static final String DeleteStoryURL = BASE_MOBILE_URL + "/story/deleteStory";
	public static final String ListStoryPageURL = BASE_MOBILE_URL + "/story/listPages";
	public static final String ListStoryArtworkURL = BASE_MOBILE_URL + "/story/listStoryArtworks";
	public static final String ListStoryArtworkByPageURL = BASE_MOBILE_URL + "/story/listStoryArtworksByPage";
	public static final String SaveStoryURL = BASE_MOBILE_URL + "/story/saveOrUpdateStory";
	public static final String ShareStoryURL = BASE_MOBILE_URL + "/story/shareStory";
	public static final String RegistStoryArtworkURL = BASE_MOBILE_URL + "/story/registArtwork";
	public static final String RegistAfterReadingURL = BASE_MOBILE_URL + "/story/registAfterReading";
	public static final String GetStoryInfoURL = BASE_MOBILE_URL + "/story/getStoryInfo";
	public static final String ListAfterReadingURL = BASE_MOBILE_URL + "/story/listAfterRead";
	
	/**
	 * Artclass
	 */
	public static final String ListUserArtClassURL = BASE_MOBILE_URL + "/artclass/findUserClass";
	public static final String ListOwnArtClassURL = BASE_MOBILE_URL + "/artclass/findOwnClass";
	public static final String ListJoinedClassURL = BASE_MOBILE_URL + "/artclass/findJoinedClass";
	public static final String ListCollectedClassURL = BASE_MOBILE_URL + "/artclass/findCollectedClass";
	public static final String ListAllClassURL = BASE_MOBILE_URL + "/artclass/findAllClass";
	public static final String ListClassByExpertURL = BASE_MOBILE_URL + "/artclass/findClassByExperts";
	public static final String CreateClassURL = BASE_MOBILE_URL + "/artclass/createArtClass";
	public static final String UploadClassCoverURL = BASE_MOBILE_URL + "/artclass/uploadCoverImage";
	public static final String GetClassInfoURL = BASE_MOBILE_URL + "/artclass/getArtClassInfo";
	public static final String GetJoinedMembersURL = BASE_MOBILE_URL + "/artclass/getJoinedMembers";
	public static final String ChangeCoverImageURL = BASE_MOBILE_URL + "/artclass/changeCoverImage";
	public static final String ListClassArtworksURL = BASE_MOBILE_URL + "/artclass/listClassArtworks";
	public static final String AddClassCollectionURL = BASE_MOBILE_URL + "/artclass/addClassCollection";
	public static final String RemoveClassCollectionURL = BASE_MOBILE_URL + "/artclass/removeClassCollection";
	public static final String DeleteArtClassURL = BASE_MOBILE_URL + "/artclass/deleteArtClass";
	public static final String EditArtClassURL = BASE_MOBILE_URL + "/artclass/editArtClass";
	public static final String AddClassMembersURL = BASE_MOBILE_URL + "/artclass/editClassMembers";
	public static final String RegistClassArtworkURL = BASE_MOBILE_URL + "/artclass/registArtwork";
	public static final String RegistPuzzleArtworkURL = BASE_MOBILE_URL + "/artclass/registPuzzleArtwork";
	public static final String ReservePuzzleArtworkURL = BASE_MOBILE_URL + "/artclass/reservePuzzleArtwork";
	public static final String CancelPuzzleArtworkURL = BASE_MOBILE_URL + "/artclass/cancelPuzzleArtwork";
	public static final String GetPuzzleListURL = BASE_MOBILE_URL + "/artclass/getPuzzleList";
	public static final String ListPuzzleArtworksURL = BASE_MOBILE_URL + "/artclass/listPuzzleArtworks";
	public static final String GetPuzzleInfoURL = BASE_MOBILE_URL + "/artclass/getPuzzleInfo";
	public static final String GetPuzzleArtworkURL = BASE_MOBILE_URL + "/artclass/getPuzzleArtwork";
	
	
	/**
	 * Experts
	 */
	public static final String ListExpertsByNameURL = BASE_MOBILE_URL + "/expert/listByName";
	public static final String ListExpertsByJobURL = BASE_MOBILE_URL + "/expert/findByJob";
	public static final String ListJobsURL = BASE_MOBILE_URL + "/expert/listJobs";
	public static final String FindExpertURL = BASE_MOBILE_URL + "/expert/findByNameCountry";
	
	
	/**
	 * Error Code
	 */
	public static final int SUCCESS = 0;
	public static final int LOGIN_COMPLETE = 1000;
	
	// -1번은 알려지지 않은 에러.
	public static final int ERROR_UNKNOWN = -1;
	
	//100번대는 로그인 에러
	public static final int ERROR_NOT_ACTIVE = 101;
	public static final int ERROR_USER_NOT_EXIST = 102;
	public static final int ERROR_PASSWORD = 103;
	
	
	// 200번대는 account에러.
	public static final int ERROR_DUPLICATE = 201;
	public static final int ERROR_MANDATORY = 202;
}
