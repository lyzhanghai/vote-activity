package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.common.CacheConst
import com.lyl.outsourcing.activity.common.Global
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.mapper.VoteItemMapper
import com.lyl.outsourcing.activity.entity.VoteItem
import com.lyl.outsourcing.activity.entity.VoteItemExample
import com.lyl.outsourcing.activity.entity.VoteRecord
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class VoteItemDao extends BaseDao<VoteItem> implements VoteItemMapper {

    final NAMESPACE = "com.lyl.outsourcing.activity.dao.mapper.VoteItemMapper"

    @Autowired
    @Delegate
    private VoteItemMapper mapper

    @Autowired
    private SqlSession sqlSession

    VoteItemDao() {
        super(VoteItem.class)
    }

    void assertExists(long id) {
        VoteItemExample voteItemExample = new VoteItemExample()
        voteItemExample.or().andIdEqualTo(id)
        long count = mapper.countByExample(voteItemExample)
        if (!count)
            throw new ObjectNotFoundException(id, VoteItem.class)
    }

    def selectPageByVoteId(long voteId) {
        sqlSession.selectList("${NAMESPACE}.selectPageByVoteId", voteId)
    }

    void vote(VoteRecord voteRecord) {
        Global.redisTemplate.opsForHash().put(
                CacheConst.voteRecord(voteRecord.voteId),
                voteRecord.openId,
                XUtil.toJson(voteRecord)
        )
        Global.redisTemplate.opsForHash().increment(CacheConst.voteStatistics(voteRecord.voteId), ""+voteRecord.voteItemId, 1)
    }

    long selectVoteNum(long voteId, long voteItemId) {
        String num = Global.redisTemplate.opsForHash().get(CacheConst.voteStatistics(voteId), voteItemId)
        num == null ? 0L : Long.parseLong(num)
    }
}
