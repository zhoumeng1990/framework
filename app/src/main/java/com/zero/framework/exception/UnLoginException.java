package com.zero.framework.exception;

/**
 * 未登录造成的异常
 * Created by Zero on 2017/5/26.
 */

public class UnLoginException extends APIException {
    public UnLoginException(int code, String message) {
        super(code, message);
    }
}