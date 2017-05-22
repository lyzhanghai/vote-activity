package com.lyl.outsourcing.activity.exception;

import java.io.Serializable;

/**
 * Created by liyilin on 2017/5/1.
 */
public class ObjectNotFoundException extends WebException {
    public ObjectNotFoundException(Serializable id, Class<?> clazz) {
        super(ErrorCode.OBJECT_NOT_FOUND_ERROR,
                "找不到ID=" + id + ", 类型为" + clazz.getName() + "的对象" );
    }
}
