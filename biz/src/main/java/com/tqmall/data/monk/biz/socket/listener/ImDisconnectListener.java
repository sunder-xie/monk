package com.tqmall.data.monk.biz.socket.listener;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.tqmall.data.monk.bean.socket.SocketConstants;
import com.tqmall.data.monk.bean.socket.SocketOutDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by zxg on 16/4/16.
 * 20:00
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class ImDisconnectListener implements DisconnectListener {
    @Override
    public void onDisconnect(SocketIOClient socketIOClient) {
        String sessionId = socketIOClient.getSessionId().toString();

        log.info("disconnect:"+sessionId);
        SocketConstants.SESSION_CLIENT_MAP.remove(sessionId);

        String source = SocketConstants.SESSION_TYPE_MAP.get(sessionId);
        SocketConstants.SESSION_TYPE_MAP.remove(sessionId);
        if(source == null){
            return;
        }

        //外部接入系统
        if(source.equals(SocketConstants.SOURCE_OUT)){
            String aliUid = SocketConstants.OUT_CHAT_MAP.get(sessionId);
            if(aliUid != null) {
                Set<String> valueSet = SocketConstants.CHAT_OUT_SET_MAP.get(aliUid);
                if (valueSet != null) {
                    valueSet.remove(sessionId);
                    //若外部页面全关闭了，则给聊天框发关闭消息
//                    if(valueSet.isEmpty()){
//                        closeChat(aliUid);
//                    }
                }

                SocketConstants.OUT_CHAT_MAP.remove(sessionId);
            }

            return;
        }
        //若为内部系统--聊天框
        if(source.equals(SocketConstants.SOURCE_CHAT)){
            String aliUid = SocketConstants.IN_SESSION_UID_MAP.get(sessionId);
            SocketConstants.IN_SESSION_UID_MAP.remove(sessionId);

            List<String> chatSessionList = SocketConstants.IN_UID_SESSION_MAP.get(aliUid);
            if(chatSessionList != null){
                chatSessionList.remove(sessionId);
                if(chatSessionList.isEmpty()){
                    SocketConstants.IN_UID_SESSION_MAP.remove(aliUid);
                    SocketConstants.CHAT_OUT_SET_MAP.remove(aliUid);
                    SocketConstants.ALI_UN_READ_MAP.remove(aliUid);
                }
            }

        }
    }

    //给聊天框发关闭的命令
    private void closeChat(String aliUid){
        List<String> chatSessionList = SocketConstants.IN_UID_SESSION_MAP.get(aliUid);
        if(chatSessionList == null){
            return;
        }
        for(String chatSessionId : chatSessionList) {
            SocketIOClient childClient = SocketConstants.SESSION_CLIENT_MAP.get(chatSessionId);
            if(childClient != null){
                SocketOutDO socketDO = new SocketOutDO();
                socketDO.setIsClosed(SocketConstants.IS_YES);
                //发送 数据
                childClient.sendEvent(SocketConstants.EVENT_NAME_GET, socketDO);
            }
        }
    }
}
