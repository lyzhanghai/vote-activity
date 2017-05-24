package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.VoteDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.VoteForm
import com.lyl.outsourcing.activity.dto.request.VoteItemSaveReq
import com.lyl.outsourcing.activity.dto.response.VoteResp
import com.lyl.outsourcing.activity.entity.Vote
import com.lyl.outsourcing.activity.entity.VoteExample
import com.lyl.outsourcing.activity.entity.VoteItem
import com.lyl.outsourcing.activity.service.VoteItemService
import com.lyl.outsourcing.activity.service.VoteService
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Created by liyilin on 2017/4/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class VoteServiceTestCase extends TestBase {
    @Autowired
    private VoteDao voteDao
    @Autowired
    private VoteService voteService
    @Autowired
    private VoteItemService voteItemService

    static activityId = 1
    static Set<Long> idSet = new HashSet<>()

    @Test
    void test1_save() {
        VoteForm saveReq = new VoteForm()
        saveReq.activityId = activityId
        saveReq.name = "测试投票主题"
        saveReq.role = "1.我的地盘我就是规则<br/>2.请叫我老大就行了<br/>3.我TM说了没规则，听我的就行了！"
        saveReq.introduction = "这是一个测试主题的介绍内容，今天晴空万里，空气清新，甚至有一丢丢睡意，但也不能阻止我写代码的欲望，" +
                "拿起键盘，手起X落，一段没bug的代码随意挥出，如天上划过一条清流，如海上掠过一只狗。" +
                "说这么多，我不是想炫耀自己写代码怎么牛逼，只是想说明一下我TM是全世界最帅气的，没有之一。"
        Date now = new Date()
        saveReq.startTime = now
        saveReq.endTime = now + 2

        Result result = voteService.save(saveReq)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson(result.data)
        idSet << (result.data as VoteResp).id
    }

    @Test
    void test2_update() {
        final id = idSet[0] as long
        Date now = new Date()
        VoteForm voteForm = new VoteForm()
        voteForm.id = id
        voteForm.activityId = 1L
        voteForm.name = "ccccccc"
        voteForm.role = "sdfadfs"
        voteForm.introduction = "dsoajfoisadjfoisa"
        voteForm.startTime = now
        voteForm.endTime = now + 2

        Result result = voteService.update(voteForm)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson( result )
    }

    @Test
    void test3_page() {
        5.times { test1_save() }
        Result result = voteService.page(null, null, 1, 5)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson(result)
        result.data.list.each {
            println XUtil.toJson(it)
        }
    }

    @Test
    void test4_getById() {
        final id = idSet[0] as long
        Result result = voteService.getById(id)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson( result.data )
    }

    /**
     * 先测试完 VoteItemService.voteRecord() 再来测试这个
     */
    @Test
    void test5_getUnfinishVote() {
        final voteId = idSet[0] as long
        final openId = UUID.randomUUID().toString().replace("-", "").substring(0, 30)
        final voteItemIdSet = new HashSet()
        Result result

        VoteItemSaveReq voteItemSaveReq = new VoteItemSaveReq()
        voteItemSaveReq.title = "1号选手"
        voteItemSaveReq.subTitle = "班长"
        voteItemSaveReq.description = "这是1号选手的描述内容"
        voteItemSaveReq.voteId = voteId
        result = voteItemService.save(voteItemSaveReq)
        assertResult(result, 0)
        voteItemIdSet << (result.data as VoteItem).id

        voteItemSaveReq = new VoteItemSaveReq()
        voteItemSaveReq.title = "2号选手"
        voteItemSaveReq.subTitle = "副班长"
        voteItemSaveReq.description = "这是2号选手的描述内容"
        voteItemSaveReq.voteId = voteId
        result = voteItemService.save(voteItemSaveReq)
        assertResult(result, 0)
        voteItemIdSet << (result.data as VoteItem).id

        voteItemSaveReq = new VoteItemSaveReq()
        voteItemSaveReq.title = "3号选手"
        voteItemSaveReq.subTitle = "团长"
        voteItemSaveReq.description = "这是3号选手的描述内容"
        voteItemSaveReq.voteId = voteId
        result = voteItemService.save(voteItemSaveReq)
        assertResult(result, 0)
        voteItemIdSet << (result.data as VoteItem).id


        final voteItemId = voteItemIdSet[0] as long
        result = voteItemService.vote(openId, voteItemId)
        assertResult(result, 0)
        println XUtil.toJson(result)

        VoteExample voteExample = new VoteExample()
        voteExample.or().andActivityIdEqualTo(activityId)
        List<Vote> listByActivityId = voteDao.selectByExample(voteExample)
        println "ID=${activityId}的活动中所有的投票主题分别有："
        listByActivityId.each {
            println XUtil.toJson(it)
        }
        println ""

        println "获取尚未投票的投票主题："
        result = voteService.getUnfinishVote(openId, activityId)
        assertResult(result, 0)
        result.data.each {
            println XUtil.toJson(it)
        }
    }

    @Test
    void test6_delete() {
        Result result = voteService.delete(idSet)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        result.data.each {Map it ->
            assert it.msg == null : "${it.id}: ${it.msg}"
        }
    }
}
