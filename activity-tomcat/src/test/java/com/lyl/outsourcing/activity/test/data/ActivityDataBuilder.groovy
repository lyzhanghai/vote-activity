package com.lyl.outsourcing.activity.test.data

import com.lyl.outsourcing.activity.dto.request.ActivityForm
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.junit.Test

import java.text.SimpleDateFormat

/**
 * Created by liyilin on 2017/5/23.
 */
class ActivityDataBuilder {
//    final static URL = "http://39.108.6.235/activity/activity"
    final static URL = "http://localhost:8080/activity"
    final static dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    final int num = 100
    final int startDateMin = -10
    final int endDateMin = 10

    ActivityForm randomActivity() {
        Random random = new Random()
        int startDateDelta = random.nextInt(endDateMin - startDateMin) + startDateMin
        ActivityForm activity = new ActivityForm()
        activity.name = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        activity.startTime = new Date() + startDateDelta
        activity.endTime = activity.startTime + (random.nextInt(10) + 1)
        activity
    }

    @Test
    void build() {
        num.times {
            HTTPBuilder httpBuilder = new HTTPBuilder(URL)
            httpBuilder.request(Method.POST) { req ->
                requestContentType = ContentType.JSON
                ActivityForm activityForm = randomActivity()
                body = [name:activityForm.name, startTime:activityForm.startTime.time, endTime:activityForm.endTime.time]

                response.success = { resp, json ->
                    println json
                }
            }
        }
    }
}
