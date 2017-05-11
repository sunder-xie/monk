package com.tqmall.data.monk.biz.processor;

import com.tqmall.data.monk.common.bean.ImConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by zxg on 16/6/2.
 * 10:13
 * no bug,以后改代码的哥们，祝你好运~！！
 * 容器初始化时的操作
 */
@Service
public class ContainerProcessor {
    @Value(value = "${ali.app.key}")
    private String trueAppKey;
    @Value(value = "${ali.app.secret}")
    private String trueAppSecret;


    @PostConstruct
    public void postConstruct(){
        //给appkey 赋值
        ImConstants.AppKey = trueAppKey;
        ImConstants.AppSecret = trueAppSecret;
    }
}
