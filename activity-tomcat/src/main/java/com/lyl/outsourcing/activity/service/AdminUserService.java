package com.lyl.outsourcing.activity.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.outsourcing.activity.common.CacheUtil;
import com.lyl.outsourcing.activity.common.CryptoUtil;
import com.lyl.outsourcing.activity.dao.AdminUserDao;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.dto.SessionUser;
import com.lyl.outsourcing.activity.entity.AdminUser;
import com.lyl.outsourcing.activity.entity.AdminUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AdminUserService {
    @Autowired
    private AdminUserDao userDao;

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     */
    public Result login(String username, String password) {
        AdminUserExample adminUserExample = new AdminUserExample();
        byte[] pwdBytes = password.getBytes();
        String pwdMd5 = CryptoUtil.md5String(pwdBytes);
        adminUserExample.or().andUsernameEqualTo(username).andPasswordEqualTo(pwdMd5);
        List<AdminUser> adminUserList = userDao.selectByExample(adminUserExample);

        if (adminUserList.size() > 0) {
            AdminUser persist = adminUserList.get(0);
            List<String> roles = userDao.selectRoleByUserId(persist.getId());
            SessionUser user = CacheUtil.setCurrentUser( persist, roles );

            return new Result(0, "登录成功", user);
        } else {
            return new Result(-1, "用户名或密码错误");
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     */
    @Transactional
    public Result register(String username, String password, List<String> roles) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(password);
        userDao.insert(adminUser);

        userDao.setUserRoles(adminUser.getId(), roles);

        return new Result(0, "注册成功");
    }

    /**
     * 根据ID批量删除用户
     * @param idSet 用户ID集
     */
    public Result delete(Set<Long> idSet) {
        Map<Long, String> map = new HashMap<>(idSet.size());
        for (Long id : idSet) {
            if (id == null) continue;
            try {
                deleteById(id);
                map.put(id, null);
            } catch (Exception e) {
                map.put(id, e.getMessage());
            }
        }
        return new Result(0, null, map);
    }

    @Transactional
    private void deleteById(long id) {
        userDao.deleteByPrimaryKey(id);
        userDao.setUserRoles(id, null);
    }

    public Result page(String username, int pageIndex, int pageSize) {
        AdminUserExample example = new AdminUserExample();
        example.or().andUsernameLike(username);
        PageHelper.startPage(pageIndex, pageSize);
        List<AdminUser> adminUsers = userDao.selectByExample(example);
        PageInfo<AdminUser> pageInfo = new PageInfo<>(adminUsers);
        return new Result(0, null, pageInfo);
    }
}
