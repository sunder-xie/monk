package com.tqmall.data.monk.exterior.ali;

import com.fasterxml.jackson.databind.JsonNode;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimImmsgPushRequest;
import com.taobao.api.request.OpenimImmsgPushRequest.ImMsg;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersGetRequest;
import com.taobao.api.response.OpenimImmsgPushResponse;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersGetResponse;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 16/4/12.
 * 16:10
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Slf4j
@Service
public class AliUserExt {

    //云旺 新增用户，返回password
    public  String insertUserToYunW(String uId, String password){
        String url = ImConstants.OnlineUrl;

        TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimUsersAddRequest req = new OpenimUsersAddRequest();

        List<Userinfos> list = new ArrayList<Userinfos>();
        Userinfos obj = new Userinfos();
        obj.setUserid(uId);
        obj.setPassword(password);
        list.add(obj);

        req.setUserinfos(list);
        try {
            OpenimUsersAddResponse rsp = client.execute(req);
            String resultString = rsp.getBody();
            log.info("ali add users result:{}",resultString);
            JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
            JsonNode response = jsonNode.get("openim_users_add_response");
            JsonNode uid_success = response.get("uid_succ");
            if(uid_success == null){
                return null;
            }
            JsonNode success_string = uid_success.get("string");
            if(success_string == null){
                //判断 是否是已有数据,密码是否一致
                String fail_message = response.get("fail_msg").get("string").get(0).toString().replace("\"","");
                if(fail_message.equals("data exist")){
                    String have_pass = getPassword(uId);
                    if(have_pass != null ){
                        log.info("ali add user uid:{},have password:{},want password:{}", uId, have_pass, password);
                        return have_pass;
                    }
                }
                return null;
            }
            Integer size = success_string.size();
            if(size > 0){
                String result_string =success_string.get(0).toString().replace("\"","");
                if(result_string.equals(uId)){
                    return password;
                }
            }

        } catch (Exception e) {
            log.error("ali add users wrong:",e);
            e.printStackTrace();
        }

        return null;
    }

    public Boolean sendMessageToOthers(String fromUid, List<String> toUidList, String message){
        String url = ImConstants.OnlineUrl;

        TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimImmsgPushRequest req = new OpenimImmsgPushRequest();

        ImMsg obj1 = new ImMsg();
        obj1.setFromUser(fromUid);
        obj1.setToUsers(toUidList);
        obj1.setMsgType(0L);
        obj1.setContext(message);

        req.setImmsg(obj1);
        OpenimImmsgPushResponse rsp = null;
        log.info("ali send message from:{},toUidList:{},message:{}", fromUid,toUidList.toString(),message);
        try {
            rsp = client.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }

        String resultString = rsp.getBody();
        log.info("ali send message to others result:{}", resultString);
        JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
        JsonNode response = jsonNode.get("openim_immsg_push_response");
        if(response != null){
            JsonNode msgid = response.get("msgid");
            if(msgid != null){
                return true;
            }
        }
        return false;
    }
    /*=====private==========*/
    private String getPassword(String uid) throws ApiException {
        TaobaoClient client = new DefaultTaobaoClient( ImConstants.OnlineUrl, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimUsersGetRequest req = new OpenimUsersGetRequest();
        req.setUserids(uid);
        OpenimUsersGetResponse rsp = client.execute(req);
        String resultString = rsp.getBody();

        log.info("ali find by aliUid：{} users result:{} ",uid,resultString);

        JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
        JsonNode response = jsonNode.get("openim_users_get_response");
        JsonNode userinfos = response.get("userinfos");
        JsonNode userinfosList = userinfos.get("userinfos");
        if(userinfosList == null){
            return null;
        }
        JsonNode userInfo = userinfosList.get(0);
        String password = userInfo.get("password").toString().replace("\"", "");;
        return password;
    }
}
