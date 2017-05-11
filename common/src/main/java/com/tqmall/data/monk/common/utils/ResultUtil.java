package com.tqmall.data.monk.common.utils;

import com.tqmall.zenith.errorcode.ErrorCode;
import com.tqmall.zenith.errorcode.support.Result;

/**
 * Created by huangzhangting on 16/1/26.
 */
public class ResultUtil {
    public static <T> Result<T> errorResult(String code, String message) {
        return new Result<T>().setSuccess(false).setCode(code).setMessage(message);
    }

    public static <T> Result<T> errorResult(ErrorCode errorCode) {
        return new Result<T>().setSuccess(false)
                .setCode(errorCode.getCode())
                .setMessage(errorCode.getErrorMessage());
    }

    public static <T> Result<T> successResult(T data) {
        return Result.wrapSuccessfulResult(data);
    }

}
