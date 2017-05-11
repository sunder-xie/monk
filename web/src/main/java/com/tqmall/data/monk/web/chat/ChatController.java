package com.tqmall.data.monk.web.chat;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.tqmall.data.monk.bean.entity.ImUserDO;
import com.tqmall.data.monk.biz.im.ImUserBiz;
import com.tqmall.data.monk.common.bean.ControllerResult;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.TokenUtil;
import com.tqmall.data.monk.web.BaseController;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zxg on 16/3/30.
 * 15:12
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Controller
@RequestMapping("/monk/chat")
public class ChatController extends BaseController {
    @Autowired
    private ImUserBiz imUserBiz;

    @RequestMapping("index")
    public ModelAndView index(ChatVO chatVO) {
        log.info("index chatVO btn_name:" + chatVO.getBottom_url_btn_name());
        ModelAndView modelAndView = new ModelAndView(webVersion + "/monk/chat");
        modelAndView.addObject("chatVO", chatVO);
        return modelAndView;
    }

    @RequestMapping("aliDemo")
    public ModelAndView aliDemo() {
        ModelAndView modelAndView = new ModelAndView(webVersion + "/monk/test_ftl/chat-ali-demo");

        return modelAndView;
    }

    @RequestMapping("demo")
    public ModelAndView demo() {
        ModelAndView modelAndView = new ModelAndView(webVersion + "/monk/test_ftl/chat-demo");

        return modelAndView;
    }

    @RequestMapping("out")
    public ModelAndView out() {
        String place = webVersion + "/monk/out";
        if (webVersion.contains("dev")) {
            place = webVersion + "/monk/test_ftl/out";
        }
        ModelAndView modelAndView = new ModelAndView(place);

        return modelAndView;
    }


    @RequestMapping("checkApp")
    @ResponseBody
    public Result checkApp(Integer sysId, String sysName, Long time, String token) {
        if (sysId == null || sysName == null || sysId.equals(0) || sysName.trim().equals("")) {
            return ControllerResult.wrapFailResult("缺少必要参数");
        }
        //判断token是否 通过，及时间是否一致
        Boolean timeIsOut = TokenUtil.isOutTime(time);
        if (timeIsOut) {
            log.info("app:{},id:{} init chat fail,timeStamp is {},time out", sysName, sysId, time.toString());
            return ControllerResult.wrapFailResult("time out");
        }
        Boolean tokenIsWrong = TokenUtil.isWrongToken(time, sysName, token);
        if (tokenIsWrong) {
            log.info("app:{},id:{} init chat fail,timeStamp is {},token wrong", sysName, sysId, time.toString());
            return ControllerResult.wrapFailResult("token wrong");
        }
        //判断token是否使用过,不进行一小时判断
//        Boolean isUsedToken = tokenBiz.isUsedToken(token);
//        if(isUsedToken){
//            log.info("app:{},id:{} init chat fail,timeStamp is {},token is used",sysName,sysId,time.toString());
//            return ControllerResult.wrapFailResult("token is used");
//        }

        //通过验证后，对用户进行处理
        return findUserBySys(sysId, sysName);
    }

    @RequestMapping("getUser")
    @ResponseBody
    public Result getUser(Integer sysId, String sysName) {
        if (sysId == null || sysId.equals(0) || StringUtils.isEmpty(sysName)) {
            return ControllerResult.wrapFailResult("缺少必要参数");
        }

        return findUserBySys(sysId, sysName);
    }

    //根据ali_uid获得两个用户名
    @RequestMapping("getUserNameByAliUid")
    @ResponseBody
    public Result getUserNameByAliUid(String uid) {
        if (StringUtils.isEmpty(uid)) {
            return ControllerResult.wrapFailResult("缺少必要参数");
        }
        ImUserDO resultDO = new ImUserDO();
        ImUserDO userDO = imUserBiz.getByUid(uid);
        if (userDO != null) {
            resultDO.setSysId(userDO.getSysId());
            resultDO.setSysName(userDO.getSysName());
            resultDO.setAliUid(userDO.getAliUid());
            resultDO.setUserName(userDO.getUserName());
            resultDO.setUserNickName(userDO.getUserNickName());
        }

        return Result.wrapSuccessfulResult(resultDO);
    }

    //剪切图片
    @RequestMapping("getPictureFromClipBoard")
    @ResponseBody
    public Result showPicture() {
        return Result.wrapSuccessfulResult(imUserBiz.getBase64StrFromClipBoard());
    }


    /*=========private============*/
    //查找用户，若不存在，则存入数据
    private Result findUserBySys(Integer sysId, String sysName) {
        ImUserDO userDO = imUserBiz.getBySysWithInsert(sysId, sysName);
        if (userDO == null) {
            //不存在且插入失败
            return ControllerResult.wrapFailResult("新用户注册失败，请联系开发~~~");
        }
        return Result.wrapSuccessfulResult(userDO);
    }

}
