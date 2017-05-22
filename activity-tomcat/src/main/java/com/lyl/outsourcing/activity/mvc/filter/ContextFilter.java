package com.lyl.outsourcing.activity.mvc.filter;

import com.lyl.outsourcing.activity.common.*;
import com.lyl.outsourcing.activity.dto.SessionUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyilin on 2017/4/22.
 */
public class ContextFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(ContextFilter.class);

    private static final String SESSION_ID = SessionConst.USER_TOKEN;

    private static final String COOKIE_OPENID = "OpenID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();

        // 将当前用户信息存到线程上下文中
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        String sessionId = getUserToken(httpServletRequest);
        SessionUser currentUser = CacheUtil.getCurrentUser(sessionId);
        map.put( ThreadContext.SESSION_USER, currentUser );
        LOG.info("当前用户：" + XUtil.toJson(currentUser));

        String openId = getOpenId(httpServletRequest, (HttpServletResponse) response);
        LOG.info("当前访问者OpenID：" + openId);
        map.put( ThreadContext.OPENID, openId );

        ThreadLocal<Map<String, Object>> ctx = ThreadContext.context;
        ctx.set( map );

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private String getOpenId(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String openId = null;

        // 先从Cookie中找OpenID
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_OPENID)) {
                    LOG.info("使用Cookie中记录的OpenID");
                    openId = cookie.getValue();
                }
            }
        }

        // 找不到的话，通过Wx接口查询
        if (openId == null && state != null && state.equals(WxUtil.STATE_AUTHORIZE)) {
             openId = WxUtil.getOpenIdByCode(code);

             // 如果获取OpenID成功，则保存到Cookie
             if (openId != null) {
                 Cookie cookie = new Cookie(COOKIE_OPENID, openId);
                 final int oneDay = 24 * 60 * 60;
                 cookie.setMaxAge( oneDay );
                 response.addCookie( cookie );
             }
        }
        return openId;
    }

    private String getUserToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_ID)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
