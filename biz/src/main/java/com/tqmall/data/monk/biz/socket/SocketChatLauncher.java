package com.tqmall.data.monk.biz.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.tqmall.data.monk.bean.socket.SocketConstants;
import com.tqmall.data.monk.biz.socket.listener.ImDisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 项目核心启动类
 */
@Service
@Slf4j
public class SocketChatLauncher {

    @Autowired
    private SocketIOServer socketIOServer;
    @Autowired
    private DataListener dataListener;
    @Autowired
    private ImDisconnectListener imDisconnectListener;

    //初始化--暂时不启动，去netty，使用浏览器自身的缓存做消息同步
//    @PostConstruct
    public void postConstruct() throws InterruptedException, UnknownHostException {
//        InetAddress netAddress = InetAddress.getLocalHost();
//        socketIOServer.getConfiguration().setHostname(socketHost);

        //监听--发送过来的事件
        socketIOServer.addEventListener(SocketConstants.EVENT_NAME_SEND, Object.class, dataListener);
        //监听--生成连接
        socketIOServer.addConnectListener(new ConnectListener() {//添加客户端连接监听器
            @Override
            public void onConnect(SocketIOClient client) {
                log.info("connected:" + client.getSessionId());
            }
        });
        //监听--注销socket连接
        socketIOServer.addDisconnectListener(imDisconnectListener);

        while(true) {
            try {
                socketIOServer.start();
                break;
            } catch (Exception e) {
                Thread.sleep(1000);
            }
        }
        log.info("Socket-client: postConstruct 初始化启动成功");
    }


//    @PreDestroy
    public void preDestroy()  {
        log.info("Socket-client: preDestroy 销毁");
        socketIOServer.stop();
    }
}
