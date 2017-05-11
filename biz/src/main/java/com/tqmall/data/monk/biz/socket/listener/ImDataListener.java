package com.tqmall.data.monk.biz.socket.listener;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.tqmall.data.monk.bean.socket.SocketChatDO;
import com.tqmall.data.monk.bean.socket.SocketConstants;
import com.tqmall.data.monk.bean.socket.SocketOutDO;
import com.tqmall.data.monk.common.utils.BdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zxg on 16/4/20.
 * 17:32
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
@Slf4j
public class ImDataListener implements DataListener {
    @Override
    public void onData(SocketIOClient socketIOClient, Object o, AckRequest ackRequest) throws Exception {
        outDo(socketIOClient,o);
        chatDo(socketIOClient, o);
    }

    /*===对外部系统的处理=====*/
    private void outDo(SocketIOClient socketIOClient, Object object){
        SocketOutDO socketDO = new SocketOutDO();
        BdUtil.transMap2Bean2((Map<String, String>) object, socketDO);

        String source = socketDO.getSource();
        if(source == null || source.equals(SocketConstants.SOURCE_CHAT)){
            //没有来源或者来源是 内部系统传来的，则不处理
            return;
        }

        String sessionId = socketIOClient.getSessionId().toString();

        //判断是否初始化
        String isInit = socketDO.getIsInit();
        if(isInit != null && isInit.equals(SocketConstants.IS_YES)) {
            SocketConstants.SESSION_CLIENT_MAP.put(sessionId, socketIOClient);
            SocketConstants.SESSION_TYPE_MAP.put(sessionId, source);

            String toAliUid = socketDO.getToAliUid();
            if (toAliUid != null) {
                //初始化时 建立关系，初次接入socket，跟 chat 建立map关系
                SocketConstants.OUT_CHAT_MAP.put(sessionId, toAliUid);
                Set<String> outSessionList = SocketConstants.CHAT_OUT_SET_MAP.get(toAliUid);
                if (outSessionList == null) {
                    outSessionList = new ConcurrentHashSet<>();
                    SocketConstants.CHAT_OUT_SET_MAP.put(toAliUid,outSessionList);
                }
                outSessionList.add(sessionId);

                //若存在未读消息数，推送给外部用户未读消息数
                String unReadSum = SocketConstants.ALI_UN_READ_MAP.get(toAliUid);
                if(unReadSum != null){
                    SocketChatDO socketChatDO = new SocketChatDO();
                    socketChatDO.setUnReadMsgSum(unReadSum);
                    log.info("out to chat {}:{}",toAliUid,socketChatDO.toString());
                    socketIOClient.sendEvent(SocketConstants.EVENT_NAME_GET,socketChatDO);
                }
            }

            return;
        }

        //非初始化，获得对应的聊天框
        String aliUid = SocketConstants.OUT_CHAT_MAP.get(sessionId);
        if(aliUid == null){
            return;
        }
        List<String> chatSessionList = SocketConstants.IN_UID_SESSION_MAP.get(aliUid);
        if(chatSessionList != null){
            for(String chatSessionId : chatSessionList) {
                SocketIOClient childClient = SocketConstants.SESSION_CLIENT_MAP.get(chatSessionId);
                //发送 数据
                log.info("out to chat {}:{}",aliUid,socketDO.toString());
                childClient.sendEvent(SocketConstants.EVENT_NAME_GET, socketDO);
            }
        }

    }

    /*=====对内部系统处理=============*/
    private void chatDo(SocketIOClient socketIOClient, Object object){
        SocketChatDO socketDO = new SocketChatDO();
        BdUtil.transMap2Bean2((Map<String, String>) object, socketDO);

        String source = socketDO.getSource();
        if(source == null || source.equals(SocketConstants.SOURCE_OUT)){
            //没有来源或者来源是 外部系统传来的，则不处理
            return;
        }
        String aliUid = socketDO.getAliUid();
        if(aliUid == null){
            //必须有aliUid
            return;
        }
        String sessionId = socketIOClient.getSessionId().toString();

        //是否初始化
        String isInit = socketDO.getIsInit();
        if(isInit != null && isInit.equals(SocketConstants.IS_YES)) {
            SocketConstants.SESSION_CLIENT_MAP.put(sessionId, socketIOClient);
            SocketConstants.SESSION_TYPE_MAP.put(sessionId, source);

            SocketConstants.IN_SESSION_UID_MAP.put(sessionId, aliUid);

            List<String> chatSessionList = SocketConstants.IN_UID_SESSION_MAP.get(aliUid);
            if(chatSessionList == null) {
                chatSessionList = new ArrayList<>();
                SocketConstants.IN_UID_SESSION_MAP.put(aliUid,chatSessionList);
            }
            chatSessionList.add(sessionId);
            return;

        }
        Set<String> outSessionList = SocketConstants.CHAT_OUT_SET_MAP.get(aliUid);
        if(outSessionList != null){
            for(String session_id : outSessionList){
                SocketIOClient childClient = SocketConstants.SESSION_CLIENT_MAP.get(session_id);
                //发送 关闭或者未读数量
                //如果IN_UID_SESSION_MAP 仅剩一个，则 发关闭，否则不发:其他系统可进行消息通信
                List<String> chatSessionList = SocketConstants.IN_UID_SESSION_MAP.get(aliUid);
                if(chatSessionList != null && chatSessionList.size() > 1){
                    socketDO.setIsClosed(SocketConstants.IS_NO);
                }

                log.info("chat:{} to out -{}",aliUid,socketDO.toString());
                childClient.sendEvent(SocketConstants.EVENT_NAME_GET,socketDO);
            }
        }

        //若此次是发送未读消息数，进行记录
        String unReadSum = socketDO.getUnReadMsgSum();
        if(unReadSum != null){
            SocketConstants.ALI_UN_READ_MAP.put(aliUid,unReadSum);
        }
    }
}
