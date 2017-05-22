package com.lyl.outsourcing.activity.exception;

/**
 * Created by lyl on 2016/11/9.
 */
public class DaoException extends WebException {

    public DaoException(String message) {
        super(ErrorCode.DAO_ERROR, message);
    }
}
