package com.tqmall.data.monk.biz;

import com.tqmall.data.monk.common.redis.RedisClientTemplate;
import com.tqmall.data.monk.common.redis.RedisKeyBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by zxg on 16/4/12.
 * 11:49
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Service
public class TokenBiz {

    @Value(value = "${project.version}")
    private String projectVersion;

    @Autowired
    private RedisClientTemplate redisClientTemplate;

    public Boolean isUsedToken(String token){
        String key = String.format(RedisKeyBean.TOKEN_KEY,projectVersion+token);
        String result = redisClientTemplate.lazyGet(key,String.class);
        if(result == null){
            //没有出现过,保存，一分钟后失效
            redisClientTemplate.lazySet(key,"used",RedisKeyBean.RREDIS_EXP_MINUTE);
            return false;
        }

        return true;
    }

}
