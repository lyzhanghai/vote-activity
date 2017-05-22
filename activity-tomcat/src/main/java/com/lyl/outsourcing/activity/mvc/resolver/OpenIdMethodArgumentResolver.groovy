package com.lyl.outsourcing.activity.mvc.resolver

import com.lyl.outsourcing.activity.annotation.OpenID
import com.lyl.outsourcing.activity.common.ThreadContext
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

/**
 * Created by liyilin on 2017/5/18.
 */
class OpenIdMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OpenID.class)
    }

    @Override
    Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return ThreadContext.openId
    }
}
