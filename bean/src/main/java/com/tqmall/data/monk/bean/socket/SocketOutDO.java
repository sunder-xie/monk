package com.tqmall.data.monk.bean.socket;

import lombok.Data;
import lombok.ToString;

/**
 * Created by zxg on 16/4/16.
 * 18:41
 * no bug,以后改代码的哥们，祝你好运~！！
 * socket 传递消息的实体类---外部向聊天框传递的内容
 */
@ToString
@Data
public class SocketOutDO {

    //消息来源类型
    //必传
    private String source;
    //是否初始化，
    // 选传
    private String isInit;

    /*=======外部系统向聊天页面 发送数据*/
    //chat聊天框的当前用户唯一标识,初次建立关系使用，若不为null，则代表初次建立关系
    private String toAliUid;
    //是否要关闭聊天框:
    private String isClosed;
    //是否要聚焦聊天框:
    private String isFocus;

    //即将要聊天的对象,阿里uid
    private String chatWithUid;

    //传入替换顶部的url对象
    private String topUrl;

}
