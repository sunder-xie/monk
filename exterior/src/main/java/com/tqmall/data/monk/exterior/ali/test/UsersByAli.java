package com.tqmall.data.monk.exterior.ali.test;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Userinfos;
import com.taobao.api.request.OpenimUsersAddRequest;
import com.taobao.api.response.OpenimUsersAddResponse;
import com.tqmall.data.monk.common.bean.ImConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxg on 16/3/28.
 * 10:03
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class UsersByAli {
    private static Userinfos setUserInfo(String nick, String icon, String uId, String Password){
        Userinfos obj3 = new Userinfos();
        obj3.setNick(nick);
        obj3.setIconUrl(icon);
        obj3.setEmail(uId+"@taobao.com");
        obj3.setMobile("18600000000");
//        obj3.setTaobaoid("test1"); //淘宝账号 可选
        obj3.setUserid(uId);
        obj3.setPassword(Password);
        obj3.setRemark("remark-"+uId);
//        obj3.setExtra("{extra:extra-"+uId+"}");
        obj3.setCareer("ceo-"+uId);
//        obj3.setVip("{vip:vip-"+uId+"}");
        obj3.setAddress("虹桥路23号-"+uId);
        obj3.setName("name-"+uId);
        obj3.setAge(23L);
        obj3.setGender("M");
        obj3.setWechat("wechat-"+uId);
        obj3.setQq("qq-"+uId);
        obj3.setWeibo("weibo-"+uId);
        return obj3;
    }

    public static void main(String[] args) throws ApiException {
        String url = ImConstants.OnlineUrl;

        TaobaoClient client = new DefaultTaobaoClient(url, ImConstants.AppKey, ImConstants.AppSecret);
        OpenimUsersAddRequest req = new OpenimUsersAddRequest();

        List<Userinfos> list2 = new ArrayList<Userinfos>();
        Userinfos obj3 = setUserInfo("王辉","http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1305/30/c0/21447200_1369886146048.jpg","test1","123");
        list2.add(obj3);


        Userinfos obj4 = setUserInfo("刘1强","http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg","test2","123");
        list2.add(obj4);
        Userinfos obj5 = setUserInfo("刘2强","http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg","test3","123");
        list2.add(obj5);
        Userinfos obj6 = setUserInfo("刘3强","http://www.33lc.com/article/UploadPic/2012-8/201282413335761587.jpg","test4","123");
        list2.add(obj6);


        req.setUserinfos(list2);
        OpenimUsersAddResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }



}
