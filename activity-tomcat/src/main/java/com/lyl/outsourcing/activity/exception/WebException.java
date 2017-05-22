package com.lyl.outsourcing.activity.exception;

/**
 * Created by lyl on 2016/11/8.
 */
public class WebException extends RuntimeException {

    /**
     * 错误码
     */
    private ErrorCode errorCode;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 报错原因
     */
    private Throwable cause;

    public WebException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public WebException(ErrorCode errorCode, String message, Throwable cause) {
        this.errorCode = errorCode;
        this.message = message;
        this.cause = cause;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
