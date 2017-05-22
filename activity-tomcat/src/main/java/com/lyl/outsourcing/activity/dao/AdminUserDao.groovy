package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.dao.mapper.AdminUserMapper
import com.lyl.outsourcing.activity.entity.AdminUser
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class AdminUserDao extends BaseDao<AdminUser> implements AdminUserMapper {

    private final static String NAMESPACE = "com.lyl.outsourcing.activity.dao.mapper.AdminUserMapper"

    @Autowired
    @Delegate
    private AdminUserMapper mapper

    @Autowired
    private SqlSession sqlSession

    AdminUserDao() {
        super(AdminUser.class)
    }

    List<String> selectRoleByUserId(long id) {
        sqlSession.selectList("${NAMESPACE}.selectRoleByUserId", id)
    }

    void setUserRoles(long userId, List<String> roles) {
        sqlSession.delete("${NAMESPACE}.deleteRoleByUserId", userId)
        if (roles != null && roles.size() > 0) {
            Map<String, Object> params = [userId:userId, list:roles]
            sqlSession.insert("${NAMESPACE}.insertRole", params)
        }
    }
}
