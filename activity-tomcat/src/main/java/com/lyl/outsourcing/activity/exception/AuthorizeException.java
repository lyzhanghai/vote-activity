package com.lyl.outsourcing.activity.exception;

/**
 * Created by liyilin on 2017/5/18.
 */
public class AuthorizeException extends WebException {
    public AuthorizeException() {
        super(ErrorCode.AUTHORIZE_ERROR, "你没有权限进行该操作");
    }
}
