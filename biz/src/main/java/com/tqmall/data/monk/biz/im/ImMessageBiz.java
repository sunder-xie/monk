package com.tqmall.data.monk.biz.im;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.bean.entity.ImUserDO;

import java.util.List;

/**
 * Created by zxg on 16/9/8.
 * 11:20
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface ImMessageBiz {

//    某用户向另外一个用户发送message消息
    Result<String> sendMessageToOther(ImUserDO fromUser, List<ImUserDO> toUsers, List<String> messageList);
}
