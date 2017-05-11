package com.tqmall.data.monk.server;

import com.tqmall.core.common.entity.Result;
import com.tqmall.data.monk.client.bean.dto.TokenDto;
import com.tqmall.data.monk.client.service.MonkService;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.TokenUtil;

import java.util.Date;

/**
 * Created by zxg on 16/9/9.
 * 16:04
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class MonkServiceImpl implements MonkService {

    @Override
    public Result<TokenDto> getToken(String sysName) {
        if(sysName == null){
            return Result.wrapErrorResult("001", "sysname is null");
        }
        if(!sysName.equals(ImConstants.SYSNAME_YUN_XIU) && !sysName.equals(ImConstants.SYSNAME_LOP) && !sysName.equals(ImConstants.SYSNAME_UC)&&
                !sysName.equals(ImConstants.SYSNAME_YUN_PEI) && !sysName.equals(ImConstants.SYSNAME_IM_SERVER) ){
            return Result.wrapErrorResult("002", "sysname must be one of yuniu/lop/uc/yunpei/imserver");
        }
        Long now = new Date().getTime();

        String nowTime = now.toString();
        String token = TokenUtil.getToken(nowTime, sysName);

        TokenDto tokenDto = new TokenDto();
        tokenDto.setSysName(sysName);
        tokenDto.setTime(nowTime);
        tokenDto.setToken(token);
        return Result.wrapSuccessfulResult(tokenDto);
    }
}
