package com.tqmall.data.monk.biz.im;

import com.tqmall.data.monk.bean.entity.ImUserDO;

/**
 * Created by zxg on 16/4/12.
 * 15:10
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface ImUserBiz {
    //插入im用户
    Boolean insertUser(ImUserDO userDO);

    //根据接入系统名和id获得对象
    ImUserDO getBySys(Integer sysId,String sysName);

    //根据接入系统名和id获得对象,若不存在，则自动插入，异常情况返回null
    ImUserDO getBySysWithInsert(Integer sysId,String sysName);

    //根据uid 获得对象
    ImUserDO getByUid(String aliUid);

     //判断剪切板是否有数据
    String getBase64StrFromClipBoard();

}
