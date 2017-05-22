package com.lyl.outsourcing.activity.dto;

import com.lyl.outsourcing.activity.exception.ErrorCode;

/**
 * Created by liyilin on 2017/4/21.
 */
public class Result {
    private int code;
    private String msg;
    private Object data;

    public Result() {
    }

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code) {
        this.code = code;
    }

    public Result(ErrorCode errorCode, String msg, Object data) {
        this.code = errorCode.getCode();
        this.msg = msg;
        this.data = data;
    }

    public Result(ErrorCode errorCode, String msg) {
        this.code = errorCode.getCode();
        this.msg = msg;
    }

    public Result(ErrorCode errorCode) {
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
