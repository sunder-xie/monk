package com.tqmall.data.monk.bean.socket;

import lombok.Data;
import lombok.ToString;

/**
 * Created by zxg on 16/4/16.
 * 18:41
 * no bug,以后改代码的哥们，祝你好运~！！
 * socket 传递消息的实体类---聊天框向外部传递的内容
 */
@ToString
@Data
public class SocketChatDO {

    //消息来源类型，
    // 必传
    private String source;

    //是否初始化，
    // 选传
    private String isInit;


    //当chat内部往外部发消息时，根据唯一标识，确定所有外围系统，
    // 必传
    private String aliUid;
    /*=======单向发送信息：chat聊天框 向外部系统发送数据*/

    //未读消息数的数量，
    // 选传
    private String unReadMsgSum;

    //是否要关闭聊天框:，
    // 选传
    private String isClosed;
    
}
