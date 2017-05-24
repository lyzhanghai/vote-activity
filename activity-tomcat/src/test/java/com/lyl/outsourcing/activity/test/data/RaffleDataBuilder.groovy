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
class RaffleDataBuilder {
    final activityNum = 100
    final raffleNum = 1000
    final int startDateMin = -10
    final int endDateMin = 10

    def randomDateRange(Date start, Date end) {
        Random random = new Random()
        long startTime = 0, endTime = 0
        while(startTime < start.time || endTime > end.time || startTime >= endTime) {
            startTime = random.nextLong() % (end.time - start.time) + start.time
            endTime = random.nextLong() % (end.time - startTime) + startTime + 1000L
        }
        [startTime:new Date(startTime), endTime:new Date(endTime)]
    }

    Map randomRaffleForm(Activity activity) {
        Map raffleForm = new HashMap()
        raffleForm.name = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        raffleForm.activityId = activity != null ? activity.id : null
        raffleForm.role = "1.遵循绝对随机规则<br/>2.一人只能抽一次<br/>3.抽到奖品后请与管理员兑奖"

        def range
        if (activity) range = randomDateRange(activity.startTime, activity.endTime)
        else {
            Random random = new Random()
            int startDateDelta = random.nextInt(endDateMin - startDateMin) + startDateMin
            Date startTime = new Date() + startDateDelta
            Date endTime = startTime + (random.nextInt(10) + 1)
            range = [startTime:startTime, endTime:endTime]
        }
        raffleForm.startTime = range.startTime.time
        raffleForm.endTime = range.endTime.time

        raffleForm
    }



    Activity getActivityById(long id) {
        def jsonStr = "http://localhost:8080/activity/${id}".toURL().text
        Result result = XUtil.fromJson(jsonStr, Result)
        assert result.code == 0: "${result.code}: ${result.msg}"
        Map map = result.data as Map
        def activity = new Activity()
        activity.id = map.id as long
        activity.startTime = new Date(map.startTime as long)
        activity.endTime = new Date(map.endTime as long)
        activity.name = map.name as String
        activity
    }

    def initActivityBindRaffle(int raffleNum, int activityNum) {
        Set<Long> set = new HashSet<>(activityNum)
        Random random = new Random()
        activityNum.times {
            long id = -1
            while(id == -1 || set.contains(id)) {
                id = random.nextInt(raffleNum) + 1
            }
            set << id
        }
        List<Long> list = new ArrayList<>(set)
        list.sort()
        return list
    }

    @Test
    void build() {
        Random random = new Random()
        List<Long> activityBindRaffleId = initActivityBindRaffle(raffleNum, activityNum)
        long id = 0
        int idx = 0

        raffleNum.times {
            HTTPBuilder httpBuilder = new HTTPBuilder("http://localhost:8080/raffle")
            httpBuilder.request(Method.POST) { req ->
                requestContentType = ContentType.JSON
                id++
                Activity activity = null
                if (id == activityBindRaffleId[idx])
                    activity = getActivityById(++idx)
                body = randomRaffleForm( activity )

                response.success = { resp, json ->
                    println json
                }
            }
        }
    }
}
