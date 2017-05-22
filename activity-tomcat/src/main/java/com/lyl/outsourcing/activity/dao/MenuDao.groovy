package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.common.CacheConst
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.mapper.MenuMapper
import com.lyl.outsourcing.activity.entity.Menu
import com.lyl.outsourcing.activity.entity.MenuExample
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository

import java.util.concurrent.TimeUnit

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class MenuDao extends BaseDao<Menu> implements MenuMapper {

    private final static String NAMESPACE = "com.lyl.outsourcing.activity.dao.mapper.MenuMapper"

    @Autowired
    @Delegate
    private MenuMapper mapper

    @Autowired
    private SqlSession sqlSession

    @Autowired
    StringRedisTemplate redisTemplate

    MenuDao() {
        super(Menu.class)
    }

    List<Menu> selectFullTree() {
        String json = redisTemplate.opsForValue().get(CacheConst.menu())
        if (json == null) {
            MenuExample example = new MenuExample()
            example.or().andParentIdIsNull()
            List<Menu> list = sqlSession.selectList("${NAMESPACE}.selectByExampleWithChildren", example)
            json = XUtil.toJson(list)
            redisTemplate.opsForValue().set( CacheConst.menu(), json, 2, TimeUnit.HOURS )       // 缓存2小时
        } else {
            return XUtil.fromJsonArray(json, Menu.class)
        }
    }

    List<Menu> selectByExampleWithChildren(MenuExample example) {
        sqlSession.selectList("${NAMESPACE}.selectByExampleWithChildren", example)
    }
}
