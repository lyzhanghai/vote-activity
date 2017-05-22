package com.lyl.outsourcing.activity.annotation;

import java.lang.annotation.*;

/**
 * 标记Controller中的参数，标明这个参数是获取OpenID
 * Created by liyilin on 2017/5/18.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenID {
}
