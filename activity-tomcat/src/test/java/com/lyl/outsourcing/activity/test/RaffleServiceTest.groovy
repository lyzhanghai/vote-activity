package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.RaffleForm
import com.lyl.outsourcing.activity.service.RaffleService
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by liyilin on 2017/5/12.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RaffleServiceTest extends TestBase {
    @Autowired
    RaffleService raffleService

    static Set<Long> idSet = new HashSet<>()

    @Test
    void test1_save() {
        Date now = new Date()
        RaffleForm raffleForm = new RaffleForm()
        raffleForm.name = "测试抽奖"
        raffleForm.activityId = 1L
        raffleForm.role = "1.遵循绝对随机规则<br/>2.一人只能抽一次<br/>3.抽到奖品后请与管理员兑奖"
        raffleForm.startTime = now + 1
        raffleForm.endTime = now + 3

        println XUtil.toJson(raffleForm)

        Result result = raffleService.save(raffleForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
        idSet.add( result.data.id as long )
    }

    @Test
    void test2_update() {
        Date now = new Date()
        final id = idSet[0]
        RaffleForm raffleForm = new RaffleForm()
        raffleForm.id = 1
        raffleForm.activityId = 1
        raffleForm.name = "测试抽奖(修改)"
        raffleForm.role = "1.遵循绝对随机规则<br/>2.一人只能抽一次<br/>3.抽到奖品后请与管理员兑奖"
        raffleForm.startTime = now + 1
        raffleForm.endTime = now + 3
        println XUtil.toJson(raffleForm)
        
        Result result = raffleService.update(raffleForm)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test3_getById() {
        final id = idSet[0]
        def result = raffleService.getById(id)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test4_page() {
//        5.times { test1_save() }
        def result = raffleService.page(1, 5)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void test5_delete() {
        def result = raffleService.delete(idSet)
        assertResult(result, 0)
        result.data.each { Map it ->
            println XUtil.toJson(it)
            assert it.msg == null
        }
    }
}
