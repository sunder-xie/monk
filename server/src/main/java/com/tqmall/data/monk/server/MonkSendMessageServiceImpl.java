package com.tqmall.data.monk.server;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.bean.entity.ImUserDO;
import com.tqmall.data.monk.biz.im.ImMessageBiz;
import com.tqmall.data.monk.client.bean.dto.ChatUserDto;
import com.tqmall.data.monk.client.service.MonkSendMessageService;
import com.tqmall.data.monk.common.utils.BdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by zxg on 16/9/8.
 * 10:57
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class MonkSendMessageServiceImpl implements MonkSendMessageService {
    @Autowired
    private ImMessageBiz imMessageBiz;

    @Override
    public Result<String> sendMessageToOther(ChatUserDto fromUser, List<ChatUserDto> toUsers, List<String> messageList) {
        if(null == fromUser || CollectionUtils.isEmpty(toUsers)){
            return Result.wrapErrorResult("000","入参为空");
        }
        ImUserDO fromDo = BdUtil.do2bo(fromUser,ImUserDO.class);
        List<ImUserDO> toList = BdUtil.do2bo4List(toUsers,ImUserDO.class);

        return imMessageBiz.sendMessageToOther(fromDo,toList,messageList);
    }

}
