package com.tqmall.data.monk.bean.socket;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zxg on 16/4/20.
 * 16:37
 * no bug,以后改代码的哥们，祝你好运~！！
 * socket 的常量池
 */
public interface SocketConstants {

    /*==================netty-socket==========================*/
    //外部系统为1，实际聊天框为2
    String SOURCE_OUT = "1";
    String SOURCE_CHAT = "2";
    //是否关闭聊天框，是否聚焦
    // 1.聊天框给外部时，外部系统断开连接，聊天框关闭
    // 2.外部给聊天框时，聊天框触发close事件
    String IS_YES = "yes";
    String IS_NO = "no";

    //lop的连接事件名,js中有冗余-socket.io
    String EVENT_NAME_GET = "socket_get_fr_server ";
    String EVENT_NAME_SEND = "socket_send_to_server";

    /*======common=========*/
    //新打开的im聊天框页面 SessionId 对应的 SocketIOClient
    Map<String, SocketIOClient> SESSION_CLIENT_MAP = new ConcurrentHashMap<>();
    //session_id 对应的类型
    Map<String, String> SESSION_TYPE_MAP = new ConcurrentHashMap<>();

    /*========chat in 内部===============*/
    //新打开的im聊天框页面n内部 SessionId 对应的 aliUid
    Map<String, String> IN_SESSION_UID_MAP = new ConcurrentHashMap<>();
    //内部 aliUid 对应的 list sessionId
    Map<String, List<String>> IN_UID_SESSION_MAP = new ConcurrentHashMap<>();
    //新打开的im聊天框页面 ali-uid 对应的来源老页面的session，1:n
    Map<String, Set<String>> CHAT_OUT_SET_MAP = new ConcurrentHashMap<>();
    //ali uid 当前的未读消息数
    Map<String,String> ALI_UN_READ_MAP = new ConcurrentHashMap<>();


    /*==========out =================*/
    //外部系统跟聊天界面的对应关系，外部对应唯一一个内部:out_session_id-aliUid
    Map<String, String> OUT_CHAT_MAP = new ConcurrentHashMap<>();





}
