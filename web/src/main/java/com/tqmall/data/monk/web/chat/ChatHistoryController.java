package com.tqmall.data.monk.web.chat;

import com.tqmall.data.monk.bean.test.TestDO;
import com.tqmall.data.monk.common.utils.ResultUtil;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zxg on 16/4/15.
 * 09:59
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@RestController
@RequestMapping("/monk/chatHis")
public class ChatHistoryController {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @RequestMapping("test")
    public Result test(Integer id){
        id.toString();
        log.info("epc test");
        TestDO testDO = new TestDO();
        testDO.setId(1);
        testDO.setName("zxg");
        return ResultUtil.successResult(testDO);
    }

    @RequestMapping(value = "insert_chat", method = RequestMethod.POST)
    public void insertChat(Integer id,Integer toid,String message,String time,String type){
        SaveChatHistoryProcess saveChatHistoryProcess = new SaveChatHistoryProcess(id,toid,message,time,type);
        taskExecutor.execute(saveChatHistoryProcess);
    }





    //内部线程类，开始记录聊天内容
    @NoArgsConstructor
    @AllArgsConstructor
    private class SaveChatHistoryProcess implements Runnable {
        private Integer id;
        private Integer toid;
        private String message;
        private String time;
        private String type;
        @Override
        public void run() {
            try {
                if(type.equals("text")){
                    //文字聊天
                    log.info("文本聊天-{} {} 对 {} 说:{}",time,id,toid,message);
                }else if(type.equals("image")){
                    //图片传输
                    log.info("图片聊天-{} {} 对 {} 发了张图:{}",time,id,toid,message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
