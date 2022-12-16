package com.tanhua.commons.utils;

//常量定义
public class Constants {

    //手机APP短信验证码CHECK_CODE_
    public static final String SMS_CODE = "CHECK:CODE:";

    // 验证码失效时间,5 minute
    public static final Long SMS_TIME = 5l;

	//推荐动态
	public static final String MOVEMENTS_RECOMMEND = "MOVEMENTS:RECOMMEND:";

    //推荐视频
    public static final String VIDEOS_RECOMMEND = "VIDEOS:RECOMMEND:";

	//圈子互动KEY
	public static final String MOVEMENTS_INTERACT_KEY = "MOVEMENTS:INTERACT:";

    //动态点赞用户HashKey
    public static final String MOVEMENT_LIKE_HASHKEY = "MOVEMENT:LIKE:";

    //动态喜欢用户HashKey
    public static final String MOVEMENT_LOVE_HASHKEY = "MOVEMENT:LOVE:";

    //视频点赞用户HashKey
    public static final String VIDEO_LIKE_HASHKEY = "VIDEO:LIKE";

    //访问用户
    public static final String VISITORS = "VISITORS";

    //关注用户
    public static final String FOCUS_USER = "FOCUS:USER:{}:{}";

	//初始化密码
    public static final String INIT_PASSWORD = "123456";

    //环信用户前缀
    public static final String HX_USER_PREFIX = "hx";

    //jwt加密盐
    public static final String JWT_SECRET = "itcast";

    //jwt超时时间
    public static final int JWT_TIME_OUT = 3_600;
}
