package com.tqmall.data.monk.client.service;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.client.bean.dto.TokenDto;

/**
 * Created by zxg on 16/9/9.
 * 16:02
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface MonkService {

    Result<TokenDto> getToken(String sysName);

}
