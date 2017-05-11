package com.tqmall.data.monk.common.bean;

import com.tqmall.zenith.errorcode.ErrorCode;
import com.tqmall.zenith.errorcode.ErrorCodeBuilder;
import com.tqmall.zenith.errorcode.PlatformErrorCode;
import com.tqmall.zenith.errorcode.support.CommonError;

/**
 * cloudepc项目异常定义
 * Created by huangzhangting on 16/1/26.
 */
public class EpcError extends CommonError {
    private static final String EPC_CODE = "01";

    public static final ErrorCode UNKNOW_EXCEPTION = ErrorCodeBuilder.newError(PlatformErrorCode.DATA, EPC_CODE)
            .setError()
            .setDetailCode("0001")
            .setName("未知异常")
            .setMessageFormat("系统发生未知异常")
            .genErrorCode();

}
