package com.lyl.outsourcing.activity.service

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.RaffleDao
import com.lyl.outsourcing.activity.dao.RaffleItemDao
import com.lyl.outsourcing.activity.dao.RaffleRecordDao
import com.lyl.outsourcing.activity.dao.VoteDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.RaffleItemForm
import com.lyl.outsourcing.activity.entity.Raffle
import com.lyl.outsourcing.activity.entity.RaffleItem
import com.lyl.outsourcing.activity.entity.RaffleItemWithNum
import com.lyl.outsourcing.activity.entity.RaffleRecord
import com.lyl.outsourcing.activity.entity.RaffleRecordExample
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import com.lyl.outsourcing.activity.exception.WebException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by liyilin on 2017/5/8.
 */
@Service
class RaffleItemService {
    private static final Logger LOG = LoggerFactory.getLogger(RaffleItemService.class)

    @Autowired
    private RaffleItemDao raffleItemDao
    @Autowired
    private RaffleDao raffleDao
    @Autowired
    private RaffleRecordDao raffleRecordDao
    @Autowired
    private VoteDao voteDao

    Result save(RaffleItemForm saveForm) {
        RaffleItem raffleItem = new RaffleItem()
        XUtil.copy(saveForm, raffleItem)
        raffleItem.id = null
        raffleItemDao.insert(raffleItem)
        new Result(0, null, raffleItem)
    }

    Result update(RaffleItemForm updateForm) {
        RaffleItem raffleItem = raffleItemDao.selectByPrimaryKey(updateForm.id)
        if (raffleItem == null)
            throw new ObjectNotFoundException(updateForm.id, RaffleItem.class)

        // 检测修改数量合法性
        int num = raffleItemDao.getItemNum(updateForm.id)
        if (updateForm.totalNum - raffleItem.totalNum + num < 0)
            throw new WebException(ErrorCode.RAFFLE_ITEM_NUM_UPDATE_ERROR, "修改的数目不能小于已排放奖品的数量")

        // raffleId禁止修改
        updateForm.raffleId = raffleItem.raffleId

        XUtil.copy(updateForm, raffleItem)
        raffleItemDao.updateByPrimaryKey(raffleItem)
        new Result(0, null, raffleItem)
    }

    private void deleteById(long id) {
        raffleItemDao.deleteByPrimaryKey(id)
    }

    Result delete(Set<Long> idSet) {
        def ans = []
        for (Long id : idSet) {
            try {
                deleteById(id)
                ans << [id: id, msg: null]
            } catch (Exception e) {
                def errorMsg = "${e.class.name}: ${e.message}"
                LOG.error(errorMsg)
                ans << [id:id, msg:errorMsg]
            }
        }
        new Result(0, null, ans)
    }

    Result getById(long id) {
        def raffleItem = raffleItemDao.selectByPrimaryKeyWithNum(id)
        if (raffleItem == null)
            throw new ObjectNotFoundException(id, RaffleItem.class)
        new Result(0, null, raffleItem)
    }

    /**
     * 获取一个抽奖主题中的所有抽奖项
     * @param raffleId
     * @return
     */
    Result getByRaffleId(long raffleId) {
        List<RaffleItemWithNum> list = raffleItemDao.selectByRaffleIdWithNum(raffleId)
        new Result(0, null, list)
    }

    /**
     * 抽奖，重复抽奖则直接返回抽奖记录
     * @param openId
     * @param raffleId
     * @return
     */
    Result raffleByRaffleId(String openId, long raffleId) {
        RaffleRecord raffleRecord = raffleRecordDao.selectByOpenIdAndRaffleId(openId, raffleId)
        if ( null != raffleRecord )     // 如果重复抽奖，则直接返回上一次抽奖结果
            return new Result( ErrorCode.REPEAT_RAFFLE_ERROR, "该用户已经进行过抽奖", raffleRecord )

        Raffle raffle = raffleDao.selectByPrimaryKey(raffleId)
        if (raffle == null)
            throw new ObjectNotFoundException(raffleId, Raffle.class)

        // 校验活动投票
        if (raffle.activityId != null) {
            def voteList = voteDao.selectUnfinishVote(openId, raffle.activityId)
            if (voteList.size() > 0)
                return new Result(ErrorCode.EXISTS_UNFINISH_VOTE, "与抽奖绑定的活动中，用户还有票没投完，请先投完票再来抽奖", voteList)
        }

        List<RaffleItemWithNum> list = raffleItemDao.selectByRaffleIdWithNum(raffleId)
        int sum = 0
        list.each { sum += it.num }
        Random random = new Random()
        int tmp = random.nextInt(sum)
        for (RaffleItemWithNum raffleItemWithNum : list) {
            if (tmp > raffleItemWithNum.num) {
                tmp -= raffleItemWithNum.num
            } else {
                // 抽中的奖项
                def ans = raffleItemDao.selectByPrimaryKey(raffleItemWithNum.id)
                raffleItemDao.decrItemNum(ans.id, 1)

                // 存储抽奖记录
                raffleRecord = new RaffleRecord()
                raffleRecord.raffleId = raffleId
                raffleRecord.openId = openId
                raffleRecord.raffleItemId = raffleItemWithNum.id
                raffleRecord.name = raffleItemWithNum.name
                raffleRecord.description = raffleItemWithNum.description
                raffleRecord.state = raffleItemWithNum.price ? 0 : -1       // -1 表示没中奖
                raffleRecord.raffleTime = new Date()
                raffleRecordDao.insert(raffleRecord)

                return new Result(0, null, raffleRecord)
            }
        }
        return new Result(0, "没有抽奖项可抽取")
    }

    /**
     * 获取抽奖记录
     * @param raffleId
     * @param openId
     * @return
     */
    Result getRaffleRecord(String openId, long raffleId) {
        RaffleRecord raffleRecord = raffleRecordDao.selectByOpenIdAndRaffleId(openId, raffleId)
        new Result(0, raffleRecord == null ? "尚未抽奖" : null, raffleRecord)
    }

    /**
     * 兑奖
     * @param raffleId
     * @param openId
     * @return
     */
    Result achieveRaffleItem(String openId, long raffleId) {
        RaffleRecord raffleRecord = raffleRecordDao.selectByOpenIdAndRaffleId(openId, raffleId)

        if (raffleRecord == null)
            return new Result(ErrorCode.NO_RAFFLE_RECORD_ERROR, "用户还未抽奖")
        if (raffleRecord.state == 1)
            return new Result(ErrorCode.HAS_ACHIEVE_ERROR, "已经兑奖过")
        else if (raffleRecord.state == -1)
            return new Result(ErrorCode.NO_WINNING_PRICE_ERROR, "没有中奖")
        else if (raffleRecord.state == 0) {
            // 更新状态
            raffleRecord.state = 1
            raffleRecord.achieveTime = new Date()
            RaffleRecordExample example = new RaffleRecordExample()
            example.or().andRaffleIdEqualTo(raffleId).andOpenIdEqualTo(openId)
            raffleRecordDao.updateByExample(raffleRecord, example)

            return new Result(0, "兑奖成功")
        }
        else
            throw new WebException(ErrorCode.UNKNOWN_ERROR, "抽奖记录有误: ${XUtil.toJson(raffleRecord)}")
    }
}
