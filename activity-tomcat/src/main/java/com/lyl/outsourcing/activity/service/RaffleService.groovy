package com.lyl.outsourcing.activity.service

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.ActivityDao
import com.lyl.outsourcing.activity.dao.RaffleDao
import com.lyl.outsourcing.activity.dao.RaffleItemDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.RaffleForm
import com.lyl.outsourcing.activity.dto.request.RaffleItemForm
import com.lyl.outsourcing.activity.dto.response.RaffleResp
import com.lyl.outsourcing.activity.entity.Activity
import com.lyl.outsourcing.activity.entity.Raffle
import com.lyl.outsourcing.activity.entity.RaffleExample
import com.lyl.outsourcing.activity.entity.RaffleItem
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.exception.FieldException
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by liyilin on 2017/5/8.
 */
@Service
class RaffleService {
    private static final Logger LOG = LoggerFactory.getLogger(RaffleService.class)

    @Autowired
    private ActivityDao activityDao
    @Autowired
    private RaffleDao raffleDao
    @Autowired
    private RaffleItemDao raffleItemDao
    @Autowired
    private RaffleItemService raffleItemService

    @Transactional
    Result save(RaffleForm raffleForm) {
        // 验证ActivityID正确性
        if ( raffleForm.activityId != null
                && null == activityDao.selectByPrimaryKey(raffleForm.activityId) )
            throw new ObjectNotFoundException(raffleForm.activityId, Activity.class)
        if ( raffleForm.activityId != null && getByActivityId(raffleForm.activityId).code == 0 )
            throw new FieldException("activityId", "该活动[ID=${raffleForm.activityId}]已绑定其它抽奖主题")

        Raffle raffle = new Raffle()
        raffle.activityId = raffleForm.activityId
        raffle.name = raffleForm.name
        raffle.role = raffleForm.role
        raffle.startTime = raffleForm.startTime
        raffle.endTime = raffleForm.endTime
        raffle.updateTime = new Date()

        assertTimeLegal(raffle)

        raffleDao.insert(raffle)

        // 生成默认未中奖人数
        RaffleItemForm raffleItemForm = new RaffleItemForm()
        raffleItemForm.price = false
        raffleItemForm.raffleId = raffle.id
        raffleItemForm.description = "没有中奖"
        raffleItemForm.name = "未中奖"
        raffleItemForm.totalNum = 500
        raffleItemService.save(raffleItemForm)

        new Result(0, null, parse(raffle) )
    }

    Result update(RaffleForm raffleForm) {
        Raffle raffle = raffleDao.selectByPrimaryKey(raffleForm.id)
        if (raffle == null)
            throw new ObjectNotFoundException(raffleForm.id, Raffle.class)

        if ( raffleForm.activityId != null
                && raffleForm.activityId != raffle.activityId
                && null == activityDao.selectByPrimaryKey(raffleForm.activityId) )
            throw new ObjectNotFoundException(raffleForm.activityId, Activity.class)

        if ( raffleForm.activityId != null
            && raffleForm.activityId != raffle.activityId
            && getByActivityId(raffleForm.activityId).code == 0 )
            throw new FieldException("activityId", "该活动[ID=${raffleForm.activityId}]已绑定其它抽奖主题")

        raffle.activityId = raffleForm.activityId
        raffle.name = raffleForm.name
        raffle.role = raffleForm.role
        raffle.startTime = raffleForm.startTime
        raffle.endTime = raffleForm.endTime
        raffle.updateTime = new Date()

        assertTimeLegal(raffle)

        raffleDao.updateByPrimaryKey(raffle)

        new Result(0, null, parse(raffle) )
    }

    /**
     * 检测抽奖主题时间合法性
     * @param raffle
     */
    private void assertTimeLegal(Raffle raffle) {
        if (raffle.startTime.time >= raffle.endTime.time)
            throw new FieldException("startTime", "开始时间必须早于结束时间")
    }

    private Result deleteById(long id) {
        // 删除抽奖主题
        raffleDao.deleteByPrimaryKey(id)

        // 删除抽奖项
        List<RaffleItem> list = raffleItemDao.selectByRaffleId(id)
        for (RaffleItem raffleItem : list) {
            raffleItemDao.deleteByPrimaryKey(raffleItem.raffleId)
        }
        new Result(0)
    }

    Result delete(Set<Long> idSet) {
        def ans = []
        for (Long id : idSet) {
            try {
                deleteById(id)
                ans << [id:id, msg:null]
            } catch (Exception e) {
                def errorMsg = "${e.class.name}: ${e.message}"
                LOG.error(errorMsg)
                ans << [id:id, msg:errorMsg]
            }
        }
        new Result(0, null, ans)
    }

    Result getById(long id) {
        Raffle raffle = raffleDao.selectByPrimaryKey(id)
        if (raffle == null)
            throw new ObjectNotFoundException(id, Raffle.class)
        new Result(0, null, parse(raffle) )
    }

    /**
     * 根据ActivityID查询抽奖主题
     * @param acitivityId
     * @return
     */
    Result getByActivityId(long acitivityId) {
        RaffleExample raffleExample = new RaffleExample()
        raffleExample.or().andActivityIdEqualTo(acitivityId)
        List<Raffle> list = raffleDao.selectByExample(raffleExample)

        if (list.size() > 0)
            return new Result(0, null, list.get(0))
        else
            return new Result(ErrorCode.OBJECT_NOT_FOUND_ERROR, "找不到ActivityId=${acitivityId}的抽奖主题")
    }

    Result page(int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize)
        RaffleExample example = new RaffleExample()
        // 按更新时间倒序排序
        example.orderByClause = "update_time desc"
        List<Raffle> list = raffleDao.selectByExample(example)
        PageInfo pageInfo = new PageInfo<>(list)

        List<RaffleResp> raffleRespList = new ArrayList<>(list.size())
        list.each { raffleRespList.add( parse( it ) ) }
        pageInfo.list = raffleRespList

        new Result(0, null, pageInfo)
    }

    private RaffleResp parse(Raffle raffle) {
        RaffleResp raffleResp = new RaffleResp()
        XUtil.copy(raffle, raffleResp)

        if (raffle.activityId != null) {
            Activity activity = activityDao.selectByPrimaryKey(raffle.activityId)
            raffleResp.activity = activity
        }
        raffleResp
    }
}
