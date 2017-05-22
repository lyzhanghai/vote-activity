package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.VoteItemSaveReq
import com.lyl.outsourcing.activity.dto.request.VoteItemUpdateReq
import com.lyl.outsourcing.activity.entity.VoteItem
import com.lyl.outsourcing.activity.exception.WebException
import com.lyl.outsourcing.activity.service.VoteItemService
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

/**
 * Created by liyilin on 2017/5/3.
 */
@Transactional
@Rollback
class VoteItemServiceTest extends TestBase {
    @Autowired
    private VoteItemService voteItemService

    long voteId = 19L

    @Test
    void testSave() {
        VoteItemSaveReq saveReq = new VoteItemSaveReq()
        saveReq.voteId = voteId
        saveReq.title = "主标题"
        saveReq.subTitle = "副标题"
        saveReq.description = "iodsajfoaisdjfoidsajfoisa"
        saveReq.imageUrl = ""
        println XUtil.toJson(saveReq)

        Result result = voteItemService.save(saveReq)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    private saveBatch(int num) {
        final ans = []
        num.times {
            VoteItemSaveReq saveReq = new VoteItemSaveReq()
            saveReq.voteId = voteId
            saveReq.title = "主标题"
            saveReq.subTitle = "副标题"
            saveReq.description = "iodsajfoaisdjfoidsajfoisa"
            saveReq.imageUrl = "http://p1.music.126.net/HqWhglwt4wyAYbmkCkKYnw==/18828037115920685.jpg?param=200y200"

            Result result = voteItemService.save(saveReq)
            assertResult(result, 0)
            ans << (result.data as VoteItem).id
        }
        ans
    }

    @Test
    void testUpdate() {
        final id = saveBatch(1)[0] as long
        VoteItemUpdateReq updateReq = new VoteItemUpdateReq()
        updateReq.id = id
        updateReq.title = "主标题"
        updateReq.subTitle = "副标题"
        updateReq.description = "iodsajfoaisdjfoidsajfoisa"
        updateReq.imageUrl = "http://www.baidu.com/"

        println XUtil.toJson(updateReq)

        Result result = voteItemService.update(updateReq)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void testDelete() {
        Set<Long> idSet = saveBatch(5)
        Result result = voteItemService.delete(idSet)
        assertResult(result, 0)
        println XUtil.toJson(result)
        result.data.each {
            println it
            assert it.msg == null: "${it.id}: ${it.msg}"
        }
    }

    @Test
    void testGetById() {
        final id = saveBatch(1)[0] as long
        Result result = voteItemService.getById(id)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void testVote() {
        Random random = new Random()
        final batchNum = 20
        final openId = UUID.randomUUID().toString().replace("-", "")
        List<Long> idSet = saveBatch(batchNum)
        def voteItemId = idSet[random.nextInt(batchNum)]

        // 第一次投票保证通过
        Result result = voteItemService.vote(openId, voteItemId)
        assertResult(result, 0)
        println XUtil.toJson(result)

        // 再投票同一个投票主题中的任何投票项都会报错
        try {
            voteItemId = idSet[random.nextInt(batchNum)]
            result = voteItemService.vote(openId, voteItemId)
            assert false: "再投票同一个投票主题中的任何投票项都会报错"
        } catch (WebException e) {
            println "${e.getClass().getName()}: ${e.getMessage()}"
        }
    }

    @Test
    void testPage() {
        saveBatch(100)
        Result result = voteItemService.page(voteId, 1, 20)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }
}
