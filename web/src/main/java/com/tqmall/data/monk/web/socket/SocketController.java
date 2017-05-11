package com.tqmall.data.monk.web.socket;

import com.tqmall.data.monk.bean.socket.SocketConstants;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.ResultUtil;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxg on 16/4/16.
 * 18:17
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@RestController
@RequestMapping("monk/socket")
@Slf4j
public class SocketController {
    @Value(value = "${js_socket_url}")
    protected String jsSocketUrl;
    @Value(value = "${socket.port}")
    protected String socketPort;

    @RequestMapping("getSocketProperties")
    public Result getSocketProperties(){
        Map<String,String> map = new HashMap<>();
        map.put("jsSocketUrl",jsSocketUrl);
        map.put("EVENT_NAME_GET", SocketConstants.EVENT_NAME_GET);
        map.put("EVENT_NAME_SEND",SocketConstants.EVENT_NAME_SEND);

        return ResultUtil.successResult(map);
    }
}
