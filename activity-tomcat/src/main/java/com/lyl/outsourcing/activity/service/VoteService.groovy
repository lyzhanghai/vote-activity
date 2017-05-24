package com.lyl.outsourcing.activity.service

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.ActivityDao
import com.lyl.outsourcing.activity.dao.RaffleDao
import com.lyl.outsourcing.activity.dao.VoteDao
import com.lyl.outsourcing.activity.dao.VoteItemDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.VoteForm
import com.lyl.outsourcing.activity.dto.response.VoteResp
import com.lyl.outsourcing.activity.entity.Activity
import com.lyl.outsourcing.activity.entity.Raffle
import com.lyl.outsourcing.activity.entity.RaffleExample
import com.lyl.outsourcing.activity.entity.Vote
import com.lyl.outsourcing.activity.entity.VoteExample
import com.lyl.outsourcing.activity.entity.VoteItemExample
import com.lyl.outsourcing.activity.exception.FieldException
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by liyilin on 2017/5/3.
 */
@Service
class VoteService {
    @Autowired
    private ActivityDao activityDao
    @Autowired
    private VoteDao voteDao
    @Autowired
    private VoteItemDao voteItemDao
    @Autowired
    private RaffleDao raffleDao

    /**
     * 保存
     * @param voteForm
     * @return
     */
    Result save(VoteForm voteForm) {
        Vote vote = new Vote()
        vote.activityId = voteForm.activityId
        vote.name = voteForm.name
        vote.role = voteForm.role
        vote.introduction = voteForm.introduction
        vote.startTime = voteForm.startTime
        vote.endTime = voteForm.endTime

        assertTimeLegal(vote)

        Date now = new Date()
        vote.createTime = now
        vote.updateTime = now

        voteDao.insert(vote)

        new Result(0, null, parse(vote))
    }

    /**
     * 更新
     * @param updateForm
     * @return
     */
    Result update(VoteForm voteForm) {
        Vote vote = voteDao.selectByPrimaryKey(voteForm.id)
        if (vote == null)
            throw new ObjectNotFoundException(voteForm.id, Vote.class)
        vote.name = voteForm.name
        vote.role = voteForm.role
        vote.introduction = voteForm.introduction
        vote.startTime = voteForm.startTime
        vote.endTime = voteForm.endTime

        assertTimeLegal(vote)

        vote.updateTime = new Date()

        voteDao.updateByPrimaryKey(vote)

        new Result(0, null, parse(vote))
    }

    /**
     * 批量删除投票主题
     * @param idSet
     * @return
     */
    Result delete(Set<Long> idSet) {
        def list = []
        idSet.each {
            try {
                deleteById(it)
                list << [id:it, msg:null]
            } catch (Exception e) {
                list << [id:it, msg:"${e.getClass().getName()}: ${e.getMessage()}"]
            }
        }
        new Result(0, null, list)
    }

    /**
     * 删除单个投票主题
     * @param id
     */
    @Transactional
    private void deleteById(long id) {
        int row = voteDao.deleteByPrimaryKey(id)
        if (row == 0) throw new ObjectNotFoundException(id, Vote.class)

        // 级联删除投票项
        VoteItemExample example = new VoteItemExample()
        example.or().andVoteIdEqualTo(id)
        voteItemDao.deleteByExample(example)
    }

    /**
     * 分页获取投票主题
     * @param openId    用于追踪用户投票状态，可选
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Result page(String openId, Long activityId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize)
        List<Vote> list = voteDao.list(activityId)
        PageInfo pageInfo = new PageInfo(list)

        List<VoteResp> voteRespList = new ArrayList<>(list.size())
        list.each { voteRespList << parse(it, openId) }

        pageInfo.list = voteRespList

        new Result(0, null, pageInfo)
    }

    /**
     * 获取单个投票主题
     * @param id
     * @return
     */
    Result getById(long id) {
        Vote vote = voteDao.selectByPrimaryKey(id)
        if (vote == null)
            throw new ObjectNotFoundException(id, Vote.class)
        new Result(0, null, parse(vote))
    }

    /**
     * 查询用户在活动中所有未投票的投票主题
     * @param openId
     * @param activityId
     * @return
     */
    Result getUnfinishVote(String openId, long activityId) {
        RaffleExample example = new RaffleExample()
        example.or().andActivityIdEqualTo(activityId)
        List<Raffle> raffleList = raffleDao.selectByExample(example)
        List<Vote> voteList = voteDao.selectUnfinishVote(openId, activityId)
        new Result(0, null, [raffleId: raffleList.size() > 0 ? raffleList[0].id : null, unvoteList: voteList])
    }

    /**
     * 检测投票主题时间合法性
     * @param vote
     */
    private void assertTimeLegal(Vote vote) {
        if (vote.startTime.time >= vote.endTime.time)
            throw new FieldException("startTime", "开始时间必须早于结束时间")

        if (vote.activityId != null) {
            def activity = activityDao.selectByPrimaryKey(vote.activityId)
            if (activity == null)
                throw new ObjectNotFoundException(vote.activityId, Activity)
            if (vote.startTime.time < activity.startTime.time)
                throw new FieldException("startTime", "投票主题开始时间必须等于或迟于活动开始时间")
            if (vote.endTime.time > activity.endTime.time)
                throw new FieldException("endTime", "投票主题结束时间必须等于或早于活动开始时间")
        }
    }

    private VoteResp parse(Vote vote, String openId=null) {
        Date now = new Date()
        VoteResp voteResp = new VoteResp()
        XUtil.copy(vote, voteResp)

        if (vote.activityId != null) {
            Activity activity = activityDao.selectByPrimaryKey(vote.activityId)
            voteResp.activity = activity
        }

        if (openId != null) {
            boolean voted = voteDao.isVoted(openId, vote.id)
            if (voted) voteResp.state = 1
            else if (now.time > vote.endTime.time) voteResp.state = -1
            else voteResp.state = 0
        }

        voteResp
    }
}
