package com.lyl.outsourcing.activity.service

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lyl.outsourcing.activity.dao.VoteDao
import com.lyl.outsourcing.activity.dao.VoteItemDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.VoteItemSaveReq
import com.lyl.outsourcing.activity.dto.request.VoteItemUpdateReq
import com.lyl.outsourcing.activity.dto.response.VoteItemResp
import com.lyl.outsourcing.activity.entity.Vote
import com.lyl.outsourcing.activity.entity.VoteItem
import com.lyl.outsourcing.activity.entity.VoteItemExample
import com.lyl.outsourcing.activity.entity.VoteRecord
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import com.lyl.outsourcing.activity.exception.WebException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by liyilin on 2017/5/3.
 */
@Service
class VoteItemService {
    @Autowired
    private VoteItemDao voteItemDao
    @Autowired
    private VoteDao voteDao

    Result save(VoteItemSaveReq saveReq) {
        voteDao.assertExists(saveReq.voteId)

        VoteItem voteItem = new VoteItem()
        voteItem.voteId = saveReq.voteId
        voteItem.title = saveReq.title
        voteItem.subTitle = saveReq.subTitle
        voteItem.description = saveReq.description
        voteItem.imageUrl = saveReq.imageUrl

        voteItemDao.insert(voteItem)

        new Result(0, null ,voteItem)
    }

    Result update(VoteItemUpdateReq updateReq) {
        VoteItem voteItem = voteItemDao.selectByPrimaryKey(updateReq.id)
        if (voteItem == null)
            throw new ObjectNotFoundException(updateReq.id, VoteItem.class)

        voteItem.title = updateReq.title
        voteItem.subTitle = updateReq.subTitle
        voteItem.description = updateReq.description
        voteItem.imageUrl = updateReq.imageUrl

        voteItemDao.updateByPrimaryKey(voteItem)

        new Result(0, null, voteItem)
    }

    Result delete(Set<Long> idSet) {
        def list = []
        idSet.each {
            try {
                int row = voteItemDao.deleteByPrimaryKey(it)
                if (row == 1) list << [id:it, msg:null]
                else throw new ObjectNotFoundException(it, VoteItem.class)
            } catch (Exception e) {
                list << [id:it, msg:"${e.getClass().getName()}: ${e.getMessage()}"]
            }
        }
        new Result(0, null, list)
    }

    /**
     * 获取单个投票项信息
     * @param voteId
     * @param pageRequest
     * @return
     */
    Result page(long voteId, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize)
        List<VoteItem> list = voteItemDao.selectPageByVoteId(voteId)
        PageInfo pageInfo = new PageInfo(list)
        new Result(0, null, pageInfo)
    }

    /**
     * 获取单个投票项信息
     * @param id
     * @return
     */
    Result getById(long id) {
        VoteItem voteItem = voteItemDao.selectByPrimaryKey(id)
        if (voteItem == null)
            throw new ObjectNotFoundException(id, VoteItem.class)
        new Result(0, null, voteItem)
    }

    /**
     * 投票操作
     * @param openId
     * @param voteItemId
     * @return
     */
    Result vote(String openId, long voteItemId) {
        VoteItem voteItem = voteItemDao.selectByPrimaryKey(voteItemId)
        if (voteItem == null)
            throw new ObjectNotFoundException(voteItemId, VoteItem.class)
        Vote vote = voteDao.selectByPrimaryKey(voteItem.voteId)
        Date now = new Date()
        if (vote.startTime.time > now.time || vote.endTime.time < now.time)
            throw new WebException(ErrorCode.VOTE_TIME_ERROR, "不能在投票主题规定时间以外进行投票")
        if (voteDao.isVoted(openId, voteItem.voteId))
            throw new WebException(ErrorCode.REPEAT_VOTE_ERROR, "你已进行过投票，每个投票主题只能投一次票")

        VoteRecord voteRecord = new VoteRecord()
        voteRecord.voteId = voteItem.voteId
        voteRecord.openId = openId
        voteRecord.voteItemId = voteItemId
        voteRecord.voteTime = new Date()
        voteItemDao.vote(voteRecord)

        new Result(0, "投票成功")
    }

    /**
     * 获取投票主题下所有投票项的票数统计
     * @param voteId
     * @return
     */
    Result getStatisticsByVoteId(long voteId) {
        VoteItemExample voteItemExample = new VoteItemExample()
        voteItemExample.or().andVoteIdEqualTo(voteId)
        def voteItems = voteItemDao.selectByExample(voteItemExample)
        def list = new ArrayList<Map>(voteItems.size())

        for (VoteItem voteItem : voteItems) {
            def obj = new HashMap(3)
            obj.voteItemId = voteItem.id
            obj.voteItemName = voteItem.title
            obj.voteNum = voteItemDao.selectVoteNum(voteItem.voteId, voteItem.id)
            list << obj
        }

        new Result(0, null, list)
    }

    private VoteItemResp parse(VoteItem voteItem) {
        VoteItemResp voteItemResp = new VoteItemResp()
        voteItem.properties.each {key, value ->
            voteItemResp[key as String] = value
        }
        // 获取实时投票数
        long count = voteItemDao.selectVoteNum(voteItem.voteId, voteItem.id)
        voteItemResp.voteNum = count
        voteItemResp
    }
}
