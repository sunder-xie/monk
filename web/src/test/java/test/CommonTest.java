package test;

import com.fasterxml.jackson.databind.JsonNode;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.OpenImUser;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimRelationsGetRequest;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.request.OpenimUsersGetRequest;
import com.taobao.api.request.OpenimUsersUpdateRequest;
import com.taobao.api.response.OpenimRelationsGetResponse;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.taobao.api.response.OpenimUsersGetResponse;
import com.taobao.api.response.OpenimUsersUpdateResponse;
import com.tqmall.data.monk.common.bean.ImConstants;
import com.tqmall.data.monk.common.utils.JsonUtil;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zxg on 16/4/6.
 * 16:10
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class CommonTest {

    @Test
    public void testDis(){
        Integer test = -4;

        Integer test1 = test >> 2;
        Integer test2 = test << 2;
        Integer test3 = test >>> 2;

        System.out.println(test1);
        System.out.println(test2);
        System.out.println(test3);
    }


    @Test
    public void testString(){
        String test = "abcdefg";
        System.out.println(test.charAt(1));
        System.out.println(test.codePointAt(1));
        System.out.println(test.codePointBefore(1));
        System.out.println(test.codePointCount(0, 3));
        System.out.println(test.indexOf("ere"));

    }


    @Test
    public void testUUid(){
        System.out.printf(UUID.randomUUID().toString());
    }


    @Test
    public void json(){
        String resultString = "{\"openim_users_add_response\":{\"fail_msg\":{\"string\":[\"data exist\",\"data exist\",\"data exist\",\"data exist\"]},\"uid_fail\":{\"string\":[\"test1\",\"test2\",\"test3\",\"test4\"]},\"uid_succ\":{}}}";
        JsonNode jsonNode = JsonUtil.getJsonNode(resultString);
        JsonNode response = jsonNode.get("openim_users_add_response");
        JsonNode uid_success = response.get("uid_succ");
        if(uid_success != null ){
            JsonNode succe_string = uid_success.get("string");
            if(succe_string != null){
                Integer size = succe_string.size();
                if(size > 0){
                    String result_string =succe_string.get(0).toString();
                }
            }
            System.out.printf("success_string is null");
        }
        JsonNode uid_fail = response.get("uid_fail");
        JsonNode string_fail = uid_fail.get("string");

        Integer size = string_fail.size();
        if(size > 0){
            String result_string =string_fail.get(0).toString();
            System.out.printf(result_string);
        }
        System.out.printf("hhh");
    }


    @Test
    public void testMapList(){
        Map<String,List<String>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        map.put(null,new ArrayList<String>());
        map.put("test",list);

        List<String> list1 = map.get("test");
        list1.add("sda");
        list1.add("sda");

        System.out.printf(""+map.get("test").size());
    }

    @Test
    public void time() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String dateString = "2014/10/11 14:50:11";
        Date date = df.parse(dateString);
        long s=date.getTime();
        System.out.println(s / 1000);
    }


    @Test
    public void testToLowerCase(){
        System.out.println("ADSA231".toLowerCase());
    }



}
