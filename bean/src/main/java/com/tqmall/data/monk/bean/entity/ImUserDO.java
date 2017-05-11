package com.tqmall.data.monk.bean.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ImUserDO {
    private Integer id;

    private String isDeleted;

    private Date gmtCreate;

    private Date gmtModified;

    private Integer sysId;

    private String sysName;

    private String aliUid;

    private String aliPassword;

    private String userName;

    private String userNickName;

    private String userPic;

    private String userPhone;

    private Integer userType;

    //冗余 appKey
    private String appKey;

}