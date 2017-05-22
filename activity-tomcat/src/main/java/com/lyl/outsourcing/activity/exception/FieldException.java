package com.lyl.outsourcing.activity.exception;

/**
 * Created by lyl on 2016/11/10.
 */
public class FieldException extends WebException {
    private String field;

    public FieldException(String field, String message) {
        super(ErrorCode.FIELD_ERROR, message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}