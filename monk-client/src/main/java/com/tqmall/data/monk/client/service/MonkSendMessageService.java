package com.tqmall.data.monk.client.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.client.bean.dto.ChatUserDto;

import java.util.List;

/**
 * Created by zxg on 16/9/8.
 * 10:49
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface MonkSendMessageService {
    /**
     * 某用户向另外一个用户发送message消息
     * @param fromUser 发送方用户
     * @param toUsers 接收方用户，可多个，组成list
     * @param messageList 发送的内容，list之间的message为换行信息
     * @return
     */
    Result<String> sendMessageToOther(ChatUserDto fromUser,List<ChatUserDto> toUsers,List<String> messageList);

}
