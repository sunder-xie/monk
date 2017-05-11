package com.tqmall.data.monk.client.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxg on 16/9/8.
 * 11:25
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class ChatUserDto implements Serializable {
    //sysId 来源的id
    private Integer sysId;

    //sysName 来源的名称，必须在 MonkClientConstants 的SYSNAME_中选择一个
    private String sysName;
}
