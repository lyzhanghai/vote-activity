package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.RaffleForm
import com.lyl.outsourcing.activity.dto.request.RaffleItemForm
import com.lyl.outsourcing.activity.entity.RaffleItem
import com.lyl.outsourcing.activity.entity.RaffleRecord
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.service.RaffleItemService
import com.lyl.outsourcing.activity.service.RaffleService
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by liyilin on 2017/5/12.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RaffleItemServiceTest extends TestBase {
    @Autowired
    RaffleItemService raffleItemService
    @Autowired
    RaffleService raffleService

    static idList = []
    static long RAFFLE_ID
    static String OPEN_ID
    static boolean PRICE = false

    @Test
    void setUp() {
        OPEN_ID = UUID.randomUUID().toString().replace("-", "")

        Date now = new Date()
        RaffleForm raffleForm = new RaffleForm()
        raffleForm.name = "测试抽奖"
        raffleForm.role = "1.遵循绝对随机规则<br/>2.一人只能抽一次<br/>3.抽到奖品后请与管理员兑奖"
        raffleForm.startTime = now + 1
        raffleForm.endTime = now + 3

        Result result = raffleService.save(raffleForm)
        assertResult(result, 0)
        RAFFLE_ID = result.data.id as long
    }

    @Test
    void test1_save() {
        RaffleItemForm raffleItemForm = new RaffleItemForm()
        raffleItemForm.name = "一等奖"
        raffleItemForm.description = "iPad mini 4 一台"
        raffleItemForm.totalNum = 1
        raffleItemForm.raffleId = RAFFLE_ID
        println XUtil.toJson(raffleItemForm)

        Result result = raffleItemService.save(raffleItemForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
        idList << (result.data as RaffleItem).id

        raffleItemForm.name = "二等奖"
        raffleItemForm.description = "iWatch Serial2 一枚"
        raffleItemForm.totalNum = 3
        raffleItemForm.raffleId = RAFFLE_ID

        result = raffleItemService.save(raffleItemForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
        idList << (result.data as RaffleItem).id

        raffleItemForm.name = "三等奖"
        raffleItemForm.description = "500元奖金"
        raffleItemForm.totalNum = 10
        raffleItemForm.raffleId = RAFFLE_ID

        result = raffleItemService.save(raffleItemForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
        idList << (result.data as RaffleItem).id
    }

    @Test
    void test2_update() {
        final id = idList[0] as long
        RaffleItemForm raffleItemForm = new RaffleItemForm()
        raffleItemForm.id = id
        raffleItemForm.name = "一等奖"
        raffleItemForm.description = "iPhone7Plus 一台"
        raffleItemForm.totalNum = 1
        raffleItemForm.raffleId = RAFFLE_ID

        println XUtil.toJson(raffleItemForm)

        Result result = raffleItemService.update(raffleItemForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test3_getById() {
        final id = idList[0] as long
        Result result = raffleItemService.getById(id)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test4_raffleByRaffleId() {
        Result result = raffleItemService.raffleByRaffleId(OPEN_ID, RAFFLE_ID)
        assertResult(result, 0)
        println XUtil.toJson(result)
        PRICE = ( (result.data as RaffleRecord).state != -1 )

        // 重复抽奖
        result = raffleItemService.raffleByRaffleId(OPEN_ID, RAFFLE_ID)
        assertResult(result, ErrorCode.REPEAT_RAFFLE_ERROR.getCode())
        println XUtil.toJson(result)
    }

    @Test
    void test5_getRaffleRecord() {
        Result result = raffleItemService.getRaffleRecord(OPEN_ID, RAFFLE_ID)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test6_achieveRaffleItem() {
        Result result
        if (PRICE) {
            // 正常兑奖
            result = raffleItemService.achieveRaffleItem(OPEN_ID, RAFFLE_ID)
            assertResult(result, 0)
            println XUtil.toJson(result)

            // 重复兑奖
            result = raffleItemService.achieveRaffleItem(OPEN_ID, RAFFLE_ID)
            assertResult(result, ErrorCode.HAS_ACHIEVE_ERROR.getCode())
            println XUtil.toJson(result)
        } else {
            // 没有中奖
            result = raffleItemService.achieveRaffleItem(OPEN_ID, RAFFLE_ID)
            assertResult(result, ErrorCode.NO_WINNING_PRICE_ERROR.getCode())
            println XUtil.toJson(result)
        }

        // 还没抽奖
        result = raffleItemService.achieveRaffleItem(OPEN_ID, -1)
        assertResult(result, ErrorCode.NO_RAFFLE_RECORD_ERROR.getCode())
        println XUtil.toJson(result)
    }

    @Test
    void test7_getByRaffleId() {
        Result result = raffleItemService.getByRaffleId(RAFFLE_ID)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test8_delete() {
        Result result = raffleItemService.delete(new HashSet<Long>(idList))
        assertResult(result, 0)
        println XUtil.toJson(result)
        result.data.each { Map it ->
            println XUtil.toJson(it)
            assert it.msg == null
        }
    }
}
