package com.temah.common.constant;

public class MsgConstant {

	//*****************************共通 0/1开头*******************************
	public static final String INTERNAL_SERVER_ERROR = "100100";
	public static final String BAD_REQUEST = "100101";
	public static final String TOKEN_MISS_ERROR = "100102";
	public static final String TOKEN_VALID_ERROR = "100103";
	public static final String ENCODE_ERROR = "100104";
	public static final String DECODE_ERROR = "100105";
	public static final String LOGIN_FAIL = "100106";
	public static final String LOGIN_ID_INVALID = "100107";
	public static final String PW_INCORRECT = "100108";
	public static final String ACCESS_DENIED = "100109";
	public static final String LOGIN_FAIL_ACCOUNT_LOCKED = "100110";
	public static final String CAPTCHA_VERIFY_CODE_INVALID = "100111";//图片验证码不正确
	public static final String LOGIN_AT_OTHER_PLACE = "100112";//您已在其他地方登陆

	public static final String UPDATE_FAILED = "100201";//更新失败，不能确定是不是版本并发问题

}
