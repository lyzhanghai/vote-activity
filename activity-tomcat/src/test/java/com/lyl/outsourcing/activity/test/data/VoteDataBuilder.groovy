package com.lyl.outsourcing.activity.test.data

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.entity.Activity
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.junit.Test

/**
 * Created by liyilin on 2017/5/23.
 */
class VoteDataBuilder {
    final activityNum = 100
    final eachActivityVoteNum = 5

    def randomDateRange(Date start, Date end) {
        Random random = new Random()
        long startTime = random.nextLong() % (end.time - start.time) + start.time
        long endTime = random.nextLong() % (end.time - startTime) + startTime + 1000L
        [startTime:new Date(startTime), endTime:new Date(endTime)]
    }

    Map randomVoteForm(Activity activity) {
        Map voteForm = new HashMap()
        voteForm.name = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        voteForm.activityId = activity.id
        voteForm.role = "1.我的地盘我就是规则<br/>2.请叫我老大就行了<br/>3.我TM说了没规则，听我的就行了！"
        voteForm.introduction = "这是一个测试主题的介绍内容，今天晴空万里，空气清新，甚至有一丢丢睡意，但也不能阻止我写代码的欲望，" +
                "拿起键盘，手起X落，一段没bug的代码随意挥出，如天上划过一条清流，如海上掠过一只狗。" +
                "说这么多，我不是想炫耀自己写代码怎么牛逼，只是想说明一下我TM是全世界最帅气的，没有之一。"

        def range = randomDateRange(activity.startTime, activity.endTime)
        voteForm.startTime = range.startTime.time
        voteForm.endTime = range.endTime.time

        voteForm
    }



    Activity getActivityById(long id) {
        def jsonStr = "http://localhost:8080/activity/${id}".toURL().text
        Result result = XUtil.fromJson(jsonStr, Result)
        assert result.code == 0: "${result.code}: ${result.msg}"
        Map map = result.data
        def activity = new Activity()
        activity.id = map.id
        activity.startTime = new Date(map.startTime)
        activity.endTime = new Date(map.endTime)
        activity.name = map.name
        activity
    }

    @Test
    void build() {
        for (i in 1..activityNum) {
            Activity activity = getActivityById(i as long)
            eachActivityVoteNum.times {
                HTTPBuilder httpBuilder = new HTTPBuilder("http://localhost:8080/vote")
                httpBuilder.request(Method.POST) { req ->
                    requestContentType = ContentType.JSON
                    body = randomVoteForm(activity)

                    response.success = { resp, json ->
                        println json
                    }
                }
            }
        }
    }
}
