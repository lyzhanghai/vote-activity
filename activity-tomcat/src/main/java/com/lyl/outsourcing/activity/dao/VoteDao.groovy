package com.lyl.outsourcing.activity.dao

import com.lyl.outsourcing.activity.common.CacheConst
import com.lyl.outsourcing.activity.common.Global
import com.lyl.outsourcing.activity.dao.mapper.VoteMapper
import com.lyl.outsourcing.activity.entity.Vote
import com.lyl.outsourcing.activity.entity.VoteExample
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import org.apache.ibatis.session.SqlSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

/**
 * Created by liyilin on 2017/4/21.
 */
@Repository
class VoteDao extends BaseDao<Vote> implements VoteMapper {

    final NAMESPACE = "com.lyl.outsourcing.activity.dao.mapper.VoteMapper"

    @Autowired
    @Delegate
    private VoteMapper mapper

    @Autowired
    private SqlSession sqlSession

    VoteDao() {
        super(Vote.class)
    }

    void assertExists(long id) {
        VoteExample voteExample = new VoteExample()
        voteExample.or().andIdEqualTo(id)
        long count = mapper.countByExample(voteExample)
        if (!count)
            throw new ObjectNotFoundException(id, Vote.class)
    }

    List<Vote> list(Long activityId) {
        sqlSession.selectList("${NAMESPACE}.list", [activityId: activityId])
    }

    Long countByActivityId(long activityId) {
        VoteExample example = new VoteExample()
        example.or().andActivityIdEqualTo(activityId)
        mapper.countByExample(example)
    }

    List<Vote> selectUnfinishVote(String openId, long activityId) {
        VoteExample voteExample = new VoteExample()
        voteExample.or().andActivityIdEqualTo(activityId)
        List<Vote> byActivityId = this.selectByExample(voteExample)
        List<Vote> ans = new ArrayList<>()

        for (Vote vote : byActivityId) {
            if (!isVoted(openId, vote.id))
                ans << vote
        }

        ans
    }

    boolean isVoted(String openId, long voteId) {
        def redisTemplate = Global.redisTemplate
        redisTemplate.opsForHash().hasKey(CacheConst.voteRecord(voteId), openId)
    }

    long countWithIlegalStartTime(long activityId, Date startTime) {
        VoteExample example = new VoteExample()
        example.or().andActivityIdEqualTo(activityId).andStartTimeLessThan(startTime)
        mapper.countByExample(example)
    }

    long countWithIlegalEndTime(long activityId, Date endTime) {
        VoteExample example = new VoteExample()
        example.or().andActivityIdEqualTo(activityId).andEndTimeGreaterThan(endTime)
        mapper.countByExample(example)
    }
}
