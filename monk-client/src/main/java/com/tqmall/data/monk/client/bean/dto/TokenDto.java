package com.tqmall.data.monk.client.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by zxg on 16/9/9.
 * 16:02
 * no bug,以后改代码的哥们，祝你好运~！！
 */
@Data
public class TokenDto implements Serializable {

    private String sysName;

    private String time;

    private String token;

}
