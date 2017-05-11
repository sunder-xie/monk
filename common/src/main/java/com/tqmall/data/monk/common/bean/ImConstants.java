package com.tqmall.data.monk.common.bean;

/**
 * Created by zxg on 16/3/28.
 * 10:04
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class ImConstants {
    //本系统名，不可更改，uc获得用户信息的系统标识之一
    public static final String SysName = "MONK";

    //换行标识
    public static final String CHAT_NEW_LINE = "monk_<br>_on";
    /*==================阿里云旺==========================*/
    public static final String OnlineUrl = "http://gw.api.taobao.com/router/rest";
    //js中有冗余这个apkey
    public static String AppKey ;
    public static String AppSecret ;

    /*==================系统接入验证==========================*/
    //最大超时时间,60分钟，单位:ms
    public static Long outTime = 60*60*1000L;
    // 跟其他系统约定好的加密的盐
    public static final String AppSalt = "taOqI2016iM.";
    //其他用户系统名
    public static final String SYSNAME_UC = "uc";
    public static final String SYSNAME_LOP = "lop";
    public static final String SYSNAME_YUN_XIU = "yunxiu";
    public static final String SYSNAME_YUN_PEI = "yunpei";

    //官方客服
    public static final String SYSNAME_IM_SERVER = "imserver";






}