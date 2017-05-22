package com.lyl.outsourcing.activity.annotation;

import java.lang.annotation.*;

/**
 * Created by liyilin on 2017/5/18.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AdminUserAuth {
}
