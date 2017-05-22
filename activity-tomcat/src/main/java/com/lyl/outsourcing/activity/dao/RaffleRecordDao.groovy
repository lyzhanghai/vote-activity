package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.dao.mapper.RaffleRecordMapper
import com.lyl.outsourcing.activity.entity.RaffleRecord
import com.lyl.outsourcing.activity.entity.RaffleRecordExample
import org.apache.ibatis.session.SqlSession
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/5/11.
 */
@Repository
class RaffleRecordDao extends BaseDao<RaffleRecord> implements RaffleRecordMapper {
    private static Logger LOG = LoggerFactory.getLogger(RaffleRecordDao.class)

    @Autowired
    @Delegate
    private RaffleRecordMapper raffleRecordMapper

    @Autowired
    private SqlSession sqlSession

    RaffleRecordDao() {
        super(RaffleRecord.class)
    }

    RaffleRecord selectByOpenIdAndRaffleId(String openId, long raffleId) {
        RaffleRecordExample example = new RaffleRecordExample()
        example.or().andOpenIdEqualTo(openId).andRaffleIdEqualTo(raffleId)
        List<RaffleRecord> list = selectByExample(example)
        if (list.size() > 0) return list[0]
        else return null
    }
}
