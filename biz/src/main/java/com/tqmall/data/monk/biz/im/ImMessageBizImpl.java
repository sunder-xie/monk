package com.tqmall.data.monk.biz.im;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.bean.entity.ImUserDO;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.exterior.ali.AliUserExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 16/9/8.
 * 11:20
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class ImMessageBizImpl implements ImMessageBiz {


    @Autowired
    private ImUserBiz imUserBiz;
    @Autowired
    private AliUserExt aliUserExt;

    @Override
    public Result<String> sendMessageToOther(ImUserDO fromUser,List<ImUserDO> toUsers,List<String> messageList) {
        if(fromUser == null || CollectionUtils.isEmpty(toUsers) || CollectionUtils.isEmpty(messageList)){
            return Result.wrapErrorResult("001","参数错误，为null 或空");
        }
        //处理来源的id
        Integer sysId = fromUser.getSysId();
        String sysName = fromUser.getSysName();
        Result fromResult = getUidBySys(sysId, sysName);
        if(!fromResult.isSuccess()){
            return fromResult;
        }
        String fromUid = (String) fromResult.getData();

        //处理要发送的帐号
        List<String> toUidList = new ArrayList<>();
        for(ImUserDO imUserDO : toUsers){
            Integer toSysId = imUserDO.getSysId();
            String toSysName = imUserDO.getSysName();
            Result toResult = getUidBySys(toSysId, toSysName);
            if(!toResult.isSuccess()){
                return toResult;
            }
            String toUid = (String) toResult.getData();
            toUidList.add(toUid);
        }
        //要发送的message
        StringBuilder sendMessage = new StringBuilder();
        Integer messageListSize = messageList.size();
        for(int i=0;i<messageListSize;i++){
            String message = messageList.get(i);
            //换行符替换成 im 能够识别的换行符号
            message = message.replace("\r","").replace("\n",ImConstants.CHAT_NEW_LINE);
            sendMessage.append(message);
            if(i < messageListSize -1){
                sendMessage.append(ImConstants.CHAT_NEW_LINE);
            }
        }
        System.out.println(sendMessage.length());
        if(sendMessage.length() > 1000){
            return Result.wrapErrorResult("009","message字数过长");
        }

        //实际发送
        Boolean isSendOk = aliUserExt.sendMessageToOthers(fromUid,toUidList,sendMessage.toString());
        if (isSendOk){
            return Result.wrapSuccessfulResult(messageListSize.toString());
        }

        return Result.wrapErrorResult("008","send fail by 云旺");
    }



    private Result<String> getUidBySys(Integer sysId,String sysName){
        if(sysId ==null || sysId < 0 || StringUtils.isEmpty(sysName) ){
            return Result.wrapErrorResult("001","sysId、sysName参数错误");
        }
        if (!sysName.equals(ImConstants.SYSNAME_YUN_XIU)&&!sysName.equals(ImConstants.SYSNAME_YUN_PEI)&&
                !sysName.equals(ImConstants.SYSNAME_UC)&&!sysName.equals(ImConstants.SYSNAME_LOP)&& !sysName.equals(ImConstants.SYSNAME_IM_SERVER)){
            return Result.wrapErrorResult("002","sysName 不在限定来源名称之中");
        }
        ImUserDO fromDO = imUserBiz.getBySysWithInsert(sysId,sysName);
        if(fromDO == null){
            return Result.wrapErrorResult("003","新建云旺账户失败");
        }
        return Result.wrapSuccessfulResult(fromDO.getAliUid());
    }

}
