package com.tqmall.data.monk.web;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.OpenimUsersDeleteRequest;
import com.taobao.api.response.OpenimUsersDeleteResponse;
import com.tqmall.data.monk.bean.entity.ImUserDO;
import com.tqmall.data.monk.bean.test.TestDO;
import com.tqmall.data.monk.biz.im.ImUserBiz;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.ResultUtil;
import com.tqmall.data.monk.common.utils.TokenUtil;
import com.tqmall.data.monk.exterior.ali.AliUserExt;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by zxg on 16/3/30.
 * 10:25
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Controller
@RequestMapping("monk/test")
@Slf4j
public class TestController extends BaseController{

    @Autowired
    private ImUserBiz imUserBiz;
    @Autowired
    private AliUserExt aliUserExt;


    @RequestMapping("new_page")
    public ModelAndView aliDemo(){
        ModelAndView modelAndView = new ModelAndView(webVersion+"/monk/test_ftl/new");
        return modelAndView;
    }

    @RequestMapping("test")
    @ResponseBody
    public Result test(Integer id){
        id.toString();
        log.info("epc test");
        TestDO testDO = new TestDO();
        testDO.setId(1);
        testDO.setName("hzt");
        return ResultUtil.successResult(testDO);
    }

    @RequestMapping("test_more_data")
    @ResponseBody
    public Result test(String... datas){
        for(String data : datas){
            System.out.print(data);
        }
        return ResultUtil.successResult(datas);
    }



    @RequestMapping("testFtl")
    public ModelAndView test1(){
        return new ModelAndView(webVersion+"/monk/testFtl");
    }

    @RequestMapping("getTime")
    @ResponseBody
    public Result getTime(String sysName){
        Long now = new Date().getTime();

        String nowTime = now.toString();
        String token = TokenUtil.getToken(nowTime,sysName);

        Map<String,String> map = new HashMap<>();
        map.put("time",nowTime);
        map.put("token",token);
        map.put("sysname", ImConstants.SysName);
        return ResultUtil.successResult(map);
    }



//    @RequestMapping("user")
//    @ResponseBody
//    public Result user(Integer id,String sysName){
//        ImUserDO userDO = imUserBiz.getBySys(id, sysName);
//        return ResultUtil.successResult(userDO);
//    }

//    @RequestMapping("deleteAli")
//    @ResponseBody
//    public Result deleteAli(String uid) throws ApiException {
//
//        if(uid == null){
//            return ResultUtil.errorResult("001","uid not null");
//        }
//        TaobaoClient client = new DefaultTaobaoClient(ImConstants.OnlineUrl, ImConstants.AppKey, ImConstants.AppSecret);
//        OpenimUsersDeleteRequest req = new OpenimUsersDeleteRequest();
//        req.setUserids(uid);
//        OpenimUsersDeleteResponse rsp = client.execute(req);
//        String result = rsp.getBody();
//        System.out.println(result);
//        return ResultUtil.successResult(result);
//    }

//    @RequestMapping("sendMessage")
//    @ResponseBody
//    public Result sendMessage(String fromuid,String touid) throws ApiException {
//        List<String> list = new ArrayList<>();
//        list.add(touid);
//
//        Boolean isok = aliUserExt.sendMessageToOthers(fromuid, list, "首都师范222次." + ImConstants.CHAT_NEW_LINE + " 安师大~！");
//
//        return Result.wrapSuccessfulResult(isok);
//    }




}
