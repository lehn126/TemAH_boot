package com.temah.ahfm.common.constant;

public class MsgConstant {

	//*****************************共通 0/1开头*******************************
	public static final String INTERNAL_SERVER_ERROR = "100100";
	public static final String BAD_REQUEST = "100101";
	public static final String KEY_ERROR = "100102";
	public static final String ENCODE_ERROR = "100103";
	public static final String DECODE_ERROR = "100104";

	public static final String FILE_DOWNLOAD_ERROR = "100105";
	public static final String FILE_NOT_FOUND = "100106";

	public static final String PDF_GENERATE_FAILED = "100107";

	public static final String SMS_CODE_LESS_60 = "100108";
	public static final String SMS_VERIFYCODE_INVALID = "100109";
	public static final String SMS_REACH_MAX_PERDAY = "100110";
	public static final String FILE_TYPE_NOT_ALLOWED = "100111";
	public static final String SMS_EMPTY_MOBILE = "100112";
	public static final String SMS_SEND_FAILED = "100113";

	public static final String PK_CODE_EXISTS = "100114";
	
	public static final String DEL_SUPER_ADMIN_USER = "100115";
	public static final String DUPLICATE_ADMIN_USERNAME = "100116";
	public static final String DEL_SUPER_ADMIN_ROLE = "100117";
	
	public static final String LOGIN_FAIL = "100118";
	
	public static final String LOGIN_ID_INVALID = "100119";
	public static final String ACCESS_DENIED = "100120";
	public static final String FILE_UPLOAD_FAILED = "100121";
	public static final String REQUEST_SIGN_INVALID = "100122";
	public static final String PW_INCORRECT = "100123";
	public static final String LOGIN_FAIL_ACCOUNT_LOCKED = "100124";
	public static final String UPDATE_FAILED = "100125";//更新失败，不能确定是不是版本并发问题
	public static final String CAPTCHA_VERIFY_CODE_INVALID = "100126";//图片验证码不正确
	public static final String PHONE_ERROR = "100127";//手机信息获取失败
	public static final String LOGIN_AT_OTHER_PLACE = "100128";//您已在其他地方登陆

}
