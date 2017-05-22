package com.lyl.outsourcing.activity.common;

import com.lyl.outsourcing.activity.dto.SessionUser;

import java.util.Map;

/**
 * Created by liyilin on 2017/4/22.
 */
public class ThreadContext {
    public static ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    public static final String SESSION_USER = "USER_TOKEN";
    public static final String OPENID = "OPENID";

    public static SessionUser getSessionUser() {
        Map<String, Object> map = context.get();
        return (SessionUser) map.get(SESSION_USER);
    }

    public static String getOpenId() {
        Map<String, Object> map = context.get();
        return (String) map.get(OPENID);
    }
}
