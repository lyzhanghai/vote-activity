package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.dao.mapper.RaffleMapper
import com.lyl.outsourcing.activity.entity.Raffle
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class RaffleDao extends BaseDao<Raffle> implements RaffleMapper {

    final NAMESPACE = "com.lyl.outsourcing.activity.dao.mapper.RaffleMapper"

    @Autowired
    @Delegate
    private RaffleMapper mapper

    @Autowired
    private SqlSession sqlSession

    RaffleDao() {
        super(Raffle.class)
    }
}
