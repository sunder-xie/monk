package com.tqmall.data.monk.common.utils;

import com.tqmall.data.monk.common.bean.ImConstants;

import java.util.Date;

/**
 * Created by zxg on 16/4/12.
 * 11:22
 * token 生成和校验util
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class TokenUtil {

    public static String getToken(String time,String sysName) {
        //salt+系统名+ 时间戳
        return Sha1Util.getSha1(ImConstants.AppSalt+sysName+time);
    }

    //token 是否正确
    public static Boolean isWrongToken(Long time, String sysName, String oldToken){
        String newToken = Sha1Util.getSha1(ImConstants.AppSalt+sysName+time);
        return !newToken.equals(oldToken);
    }

    //时间是否超时
    public static Boolean isOutTime(Long time){
        Long diff = new Date().getTime() - time;
        if(diff > ImConstants.outTime){
            return true;
        }
        return false;
    }
}
