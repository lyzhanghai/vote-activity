package com.lyl.outsourcing.activity.exception;

/**
 * Created by liyilin on 2017/5/18.
 */
public class WxAuthorizeException extends WebException {
    public WxAuthorizeException() {
        super(ErrorCode.WX_AUTHORIZE_EXCEPTION, "未能获取微信用户信息，请用微信正确访问");
    }
}
