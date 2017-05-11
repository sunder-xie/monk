package com.tqmall.data.monk.common.redis;

/**
 * Created by zxg on 15/9/10.
 * redis key 的管理
 */

public interface RedisKeyBean {
    /*===========变量自动为 final static*/
    //redis变量key的系统前缀
    String SYSTEM_PREFIX = "MONK_";

    String NULL_OBJECT = "None";
    /*=====================自定义的各种key=========================================================================*/
    //token 数据的记录 失效时间：一分钟
    String TOKEN_KEY = SYSTEM_PREFIX+"token_%s";
    //im用户数据,根据sys，失效时间：10分钟
    String IM_USER_SYS_KEY = SYSTEM_PREFIX+"im_user_by_sys_%s_%s_%d";
    //根据uid获得用户数据，失效时间10分钟
    String IM_USER_UID_KEY = SYSTEM_PREFIX+"im_user_by_uid_%s";



    /*=============失效时间=======================================================================*/
    /**
     * 缓存时效 1分钟
     */
    public static int RREDIS_EXP_MINUTE = 60;

    /**
     * 缓存时效 10分钟
     */
    public static int RREDIS_EXP_MINUTES = 60 * 10;

    /**
     * 缓存时效 60分钟
     */
    public static int RREDIS_EXP_HOURS = 60 * 60;

    /**
     * 缓存时效 1天
     */
    public static int RREDIS_EXP_DAY = 3600 * 24;

    /**
     * 缓存时效 1周
     */
    public static int RREDIS_EXP_WEEK = 3600 * 24 * 7;

    /**
     * 缓存时效 1月
     */
    public static int RREDIS_EXP_MONTH = 3600 * 24 * 30 * 7;
}
