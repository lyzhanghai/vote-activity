package com.lyl.outsourcing.activity.aspect

import com.lyl.outsourcing.activity.common.ThreadContext
import com.lyl.outsourcing.activity.exception.AuthorizeException
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
//@Component
class AdminUserAuthAspect {
    private static final Logger LOG = LoggerFactory.getLogger(AdminUserAuthAspect.class)

    @Pointcut("execution(@com.lyl.outsourcing.activity.annotation.AdminUserAuth * com.lyl.outsourcing.activity.controller..*.*(..)) || within(@com.lyl.outsourcing.activity.annotation.AdminUserAuth com.lyl.outsourcing.activity.controller..*)")
    void controllerMethodPointcut() {}

    @Before("controllerMethodPointcut()")
    void doBefore() throws Throwable {
        // 必须登录后台管理员
        if (ThreadContext.getSessionUser() == null)
            throw new AuthorizeException()
    }
}
