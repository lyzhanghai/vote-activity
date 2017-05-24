package com.lyl.outsourcing.activity.test.data

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.junit.Test

/**
 * Created by liyilin on 2017/5/23.
 */
class VoteItemDataBuilder {
    final static int voteIdBound = 256
    final static int eachVoteItemNum = 50
    final static images = ["00bc288f9a3243b1b60998d16dbdb49e" ,"7b96af5216db4605ba71fb8fe50c15be",
                            "35e07be9d1cb42298d50b385b00de323" ,"7c5f48f70f8c47f98159f22a4d11d884",
                            "47f9993fc9534bf5873912c5c33961d5" ,"83cc7d38905d40a786cab1557a392b9c",
                            "521a30a4a90d4144a0b68c0a7a6655a3" ,"96827e8ce8d94de1ba5ec361278997eb",
                            "71ae9f361f79474c93ada31ed57d23de" ,"f80a7b900f9c4886b496809189e5a529"]

    Map randomVoteItemForm(long voteId) {
        Map voteItemForm = new HashMap()
        Random random = new Random()
        voteItemForm.voteId = voteId
        voteItemForm.title = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        voteItemForm.subTitle = UUID.randomUUID().toString().replace("-", "").substring(0, 15)
        voteItemForm.description = "iodsajfoaisdjfoidsajfoisa"
        voteItemForm.imageUrl = "http://39.108.6.235/activity/201705/${images[random.nextInt(images.size())]}".toString()
        voteItemForm
    }

    @Test
    void build() {
        for (i in 1..voteIdBound) {
            eachVoteItemNum.times {
                HTTPBuilder httpBuilder = new HTTPBuilder("http://localhost:8080/voteItem")
                httpBuilder.request(Method.POST) { req ->
                    requestContentType = ContentType.JSON
                    body = randomVoteItemForm(i as long)

                    response.success = { resp, json ->
                        println json
                    }
                }
            }
        }
    }
}
