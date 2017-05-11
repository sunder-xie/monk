package test.yunwang;

import com.fasterxml.jackson.databind.JsonNode;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.OpenImUser;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.JsonUtil;
import org.junit.Test;

import java.util.*;

/**
 * Created by zxg on 16/5/23.
 * 09:25
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class aliTest {
    String url = ImConstants.OnlineUrl;

    TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
    OpenimUserserviceGetRequest req = new OpenimUserserviceGetRequest();


    @Test
    public void testAliyunWang() throws ApiException {
        System.out.println("=========="+new Date().toString());
        TaobaoClient client = new DefaultTaobaoClient( ImConstants.OnlineUrl, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimUsersGetRequest req = new OpenimUsersGetRequest();
        req.setUserids("devmonklop10116");
//        req.setUserids("devMONKuc37897");
        OpenimUsersGetResponse rsp = client.execute(req);
        String resultString = rsp.getBody();
        System.out.println(resultString);
        System.out.println("========="+new Date().toString());
    }


    @Test
    public void testAliAdd(){
        String uId = "devMONKuc1872";
        String password = "devMONKuc1872";
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

            System.out.println("ali add users result:" + resultString);
            JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
            JsonNode response = jsonNode.get("openim_users_add_response");
            JsonNode uid_success = response.get("uid_succ");
            if(uid_success == null){
                System.out.println("uid_success false");
            }
            JsonNode success_string = uid_success.get("string");
            if(success_string == null){
                System.out.println("success_string false");
            }
            String fail_message = response.get("fail_msg").get("string").get(0).toString().replace("\"","");
            System.out.println(""+fail_message);
//            Integer size = success_string.size();
//            if(size > 0){
//                String result_string =success_string.get(0).toString().replace("\"","");
//                if(result_string.equals(uId)){
//                    System.out.println("true");
//                }
//            }
            //说存在改对象

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void yunwangUpdate() throws ApiException {
        String url = ImConstants.OnlineUrl;

        TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimUsersUpdateRequest req = new OpenimUsersUpdateRequest();
        List<Userinfos> list2 = new ArrayList<Userinfos>();
        Userinfos obj3 = new Userinfos();
        list2.add(obj3);

        obj3.setUserid("devMONKuc37890");
        obj3.setPassword("1234561");

        req.setUserinfos(list2);
        OpenimUsersUpdateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }


    @Test
    public void testAliOther() throws ApiException {
        String url = ImConstants.OnlineUrl;

        TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimRelationsGetRequest req = new OpenimRelationsGetRequest();
        req.setBegDate("20160520");
        req.setEndDate("20150522");
        OpenImUser obj1 = new OpenImUser();
        obj1.setUid("onlineMONKuc72873");
        obj1.setTaobaoAccount(false);
        obj1.setAppKey("5b8af81e-52f0-4ac1-ac55-a17ab70fcc07");
        req.setUser(obj1);
        OpenimRelationsGetResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    @Test
    public void testUserService() throws ApiException {
        req.setDate(StringUtils.parseDateTime("2016-05-20 10:00:00"));
        req.setPageNo(1L);
        req.setPageSize(200L);
        OpenimUserserviceGetResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    @Test
    public void testChatHistory() throws ApiException {
        Long beginTime = StringUtils.parseDateTime("2016-05-20 00:00:00").getTime()/1000;
        Long endTime = StringUtils.parseDateTime("2016-05-30 23:59:59").getTime()/1000;
        String nextKey = "";
        //onlinemonklop10038 onlinemonkuc38590
        String fromUid = "onlinemonklop10038";
        String toUid = "onlinemonkuc38590";

        OpenimChatlogsGetRequest req = new OpenimChatlogsGetRequest();
        OpenImUser obj1 = new OpenImUser();
        obj1.setUid(fromUid);
        req.setUser1(obj1);
        OpenImUser obj2 = new OpenImUser();
        obj2.setUid(toUid);
        req.setUser2(obj2);
        req.setBegin(beginTime);
        req.setEnd(endTime);
        req.setCount(100L);
        req.setNextKey(nextKey);
        OpenimChatlogsGetResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }

    @Test
    public void testAllRecord() throws ApiException {
        Long beginTime = StringUtils.parseDateTime("2016-05-20 00:00:00").getTime()/1000;
        Long endTime = StringUtils.parseDateTime("2016-05-30 23:59:59").getTime()/1000;
        String nextKey = "";


        Set<String> testSet = new HashSet<>();
//        Set<String> fromChatSet = new HashSet<>();
        HashMap<String,Integer> fromMap = new HashMap<>();
        HashMap<String,Set<String>> fromTOMap = new HashMap<>();
        testSet.add("onlinemonklop10008");

        while (true) {
            OpenimAppChatlogsGetRequest req = new OpenimAppChatlogsGetRequest();
            req.setBeg(beginTime);
            req.setEnd(endTime);
            req.setCount(1000L);
            req.setNext(nextKey);
            OpenimAppChatlogsGetResponse rsp = client.execute(req);

            String resultString = rsp.getBody();
            JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
            JsonNode resultNode = jsonNode.get("openim_app_chatlogs_get_response").get("result");
            JsonNode messages = resultNode.get("messages").get("es_message");
            nextKey = resultNode.get("next_key").toString().replace("\"", "");


            for (JsonNode messageNode : messages) {
                String from_uid = messageNode.get("from_id").get("uid").toString().replace("\"", "");
                String to_uid = messageNode.get("to_id").get("uid").toString().replace("\"", "");
                if (from_uid.startsWith("online")) {
                    if (testSet.contains(from_uid)) {
                        testSet.add(to_uid);
                        continue;
                    }
                    Integer num = fromMap.get(from_uid);
                    if(num == null){
                        num = 0;
                    }
                    num ++;
                    fromMap.put(from_uid,num);

                    Set<String> toSet = fromTOMap.get(from_uid);
                    if(null == toSet){
                        toSet = new HashSet<>();
                        fromTOMap.put(from_uid,toSet);
                    }
                    toSet.add(to_uid);
                }
            }

            Iterator<String> iterator = fromMap.keySet().iterator();
            while (iterator.hasNext()){
                String it = iterator.next();
                if(testSet.contains(it)){
                    iterator.remove();
                    fromTOMap.remove(it);
                }
            }
            if(nextKey.equals("")){
                break;
            }
        }


        System.out.println("chat num:"+fromMap.size());
        System.out.println(fromMap.toString());
        System.out.println(fromTOMap.toString());
    }
}
