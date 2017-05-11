package com.tqmall.data.monk.web.rest;

import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.ResultUtil;
import com.tqmall.data.monk.common.utils.TokenUtil;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxg on 16/4/29.
 * 12:58
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@RequestMapping("monk/rest")
@Slf4j
public class restController {

    @RequestMapping("getToken")
    @ResponseBody
    public Result getToken(String sysname){
        if(sysname == null){
            return ResultUtil.errorResult("001","sysname is null");
        }
        if(!sysname.equals(ImConstants.SYSNAME_YUN_XIU) && !sysname.equals(ImConstants.SYSNAME_LOP) && !sysname.equals(ImConstants.SYSNAME_UC)
                && !sysname.equals(ImConstants.SYSNAME_YUN_PEI) && !sysname.equals(ImConstants.SYSNAME_IM_SERVER) ){
            return ResultUtil.errorResult("002","sysname must be one of yuniu/lop/uc/yunpei/imserver");
        }
        Long now = new Date().getTime();

        String nowTime = now.toString();
        String token = TokenUtil.getToken(nowTime, sysname);

        Map<String,String> map = new HashMap<>();
        map.put("sysname",sysname);
        map.put("time",nowTime);
        map.put("token",token);
        return ResultUtil.successResult(map);
    }
}
