package com.tqmall.data.monk.web.chat;

import lombok.Data;

/**
 * Created by zxg on 16/4/27.
 * 18:04
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class ChatVO {
    private String sysid;
    private String sysname;
    private String time;
    private String token;


    private String tosysid;
    private String tosysname;
    private String top_url;
    private String bottom_url_btn_name;
    private String bottom_url;
}
