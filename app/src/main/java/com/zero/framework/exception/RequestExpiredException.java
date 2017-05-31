package com.zero.framework.exception;

/**
 * Created by Zero on 2017/5/26.
 */

public class RequestExpiredException extends APIException {
    public RequestExpiredException(int code, String message) {
        super(code, message);
    }
}