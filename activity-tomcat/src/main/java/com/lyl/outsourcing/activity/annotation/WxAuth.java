package com.lyl.outsourcing.activity.annotation;

import java.lang.annotation.*;

/**
 * 记录控制器类或控制器中的方法为微信请求接口
 * · 若标记在控制器类上，则控制器中所有方法都属于微信请求接口
 * · 若标记在方法上，则只标明该方法属于微信请求接口
 * 微信请求接口即：必须带有微信用户信息才能调用
 * Created by liyilin on 2017/5/18.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WxAuth {
}
