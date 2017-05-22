package com.lyl.outsourcing.activity.dto;

import com.lyl.outsourcing.activity.entity.AdminUser;

import java.util.List;

/**
 * 存放在Session中的User类
 * Created by liyilin on 2017/4/21.
 */
public class SessionUser {
    private long id;
    private String username;
    private String sessionId;
    private List<String> roles;

    public SessionUser() {
    }

    public SessionUser(AdminUser adminUser) {
        this.id = adminUser.getId();
        this.username = adminUser.getUsername();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
