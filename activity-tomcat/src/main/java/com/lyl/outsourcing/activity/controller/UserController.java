package com.lyl.outsourcing.activity.controller;

import com.lyl.outsourcing.activity.annotation.AdminUserAuth;
import com.lyl.outsourcing.activity.common.CacheUtil;
import com.lyl.outsourcing.activity.common.SessionConst;
import com.lyl.outsourcing.activity.common.ThreadContext;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SessionUser;
import com.lyl.outsourcing.activity.exception.ErrorCode;
import com.lyl.outsourcing.activity.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * Created by liyilin on 2017/4/23.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/login")
    public Object login(String username, String password, HttpServletResponse response) {
        Result result = adminUserService.login(username, password);
        if (result.getCode() == 0) {
            // 登录成功则把 SessionID 存到Cookie
            SessionUser user = (SessionUser) result.getData();
            Cookie cookie = new Cookie(SessionConst.USER_TOKEN, user.getSessionId());
            cookie.setMaxAge((int) SessionConst.EXPIRE_USER_TOKEN);
            response.addCookie(cookie);
        }
        return result;
    }

    @AdminUserAuth
    @PostMapping("/register")
    public Object register(String username, String password, List<String> roles) {
        Result result = adminUserService.register(username, password, roles);
        return result;
    }

    @GetMapping("/currentUser")
    public Object getCurrentUser() {
        SessionUser user = ThreadContext.getSessionUser();
        if (user != null)
            return new Result(0, null, user);
        else
            return new Result(ErrorCode.AUTHORIZE_ERROR, "你还未登录管理员账号");
    }

    @PostMapping("logout")
    public Object logout() {
        SessionUser user = ThreadContext.getSessionUser();
        if (user != null) {
            CacheUtil.removeCache(user.getSessionId());
        }
        return new Result(0);
    }

    @AdminUserAuth
    @PostMapping("/delete")
    public Object delete(Set<Long> idSet) {
        Result result = adminUserService.delete(idSet);
        return result;
    }

    @AdminUserAuth
    @GetMapping
    public Object page(@RequestParam(defaultValue = "") String username,
                       @RequestParam(defaultValue = "1") Integer pageIndex,
                       @RequestParam(defaultValue = "20") Integer pageSize) {
        Result result = adminUserService.page(username, pageIndex, pageSize);
        return result;
    }
}
