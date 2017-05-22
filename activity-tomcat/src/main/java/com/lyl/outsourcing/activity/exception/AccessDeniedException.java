package com.lyl.outsourcing.activity.exception;

/**
 * 权限异常
 * Created by lyl on 2016/11/8.
 */
public class AccessDeniedException extends WebException {
    public AccessDeniedException(String message) {
        super(ErrorCode.ACCESS_DENIED_ERROR, message);
    }
}
