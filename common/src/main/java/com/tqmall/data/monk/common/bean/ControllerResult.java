package com.tqmall.data.monk.common.bean;

import com.tqmall.zenith.errorcode.support.Result;

/**
 * Created by zxg on 16/4/12.
 * 12:07
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public class ControllerResult {
    public static <T> Result<T> wrapFailResult(String message) {

        return new Result<T>().setSuccess(false).setMessage(message);
    }

    public static <T> Result<T> wrapSuccessfulResult(T data) {
        return Result.wrapSuccessfulResult(data);
    }
}
