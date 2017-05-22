package com.lyl.outsourcing.activity.aspect

import com.lyl.outsourcing.activity.common.ThreadContext
import com.lyl.outsourcing.activity.exception.WxAuthorizeException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Created by liyilin on 2017/5/18.
 */
@Aspect
@Component
class WxAuthAspect {
    private static final Logger LOG = LoggerFactory.getLogger(WxAuthAspect.class)

    @Pointcut("execution(@com.lyl.outsourcing.activity.annotation.WxAuth * com.lyl.outsourcing.activity.controller..*.*(..)) || within(@com.lyl.outsourcing.activity.annotation.WxAuth com.lyl.outsourcing.activity.controller..*)")
    void controllerMethodPointcut() {}

    @Before("controllerMethodPointcut()")
    void doBefore(JoinPoint joinPoint) throws Throwable {
        println joinPoint.target
        LOG.info( "微信登录认证切片" )
        if (ThreadContext.getOpenId() == null)
            throw new WxAuthorizeException()
    }
}
