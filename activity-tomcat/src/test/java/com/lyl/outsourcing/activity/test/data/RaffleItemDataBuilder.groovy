package com.lyl.outsourcing.activity.test.data

import com.lyl.outsourcing.activity.dto.request.RaffleItemForm
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.junit.Test

/**
 * Created by liyilin on 2017/5/23.
 */
class RaffleItemDataBuilder {
    final raffleNum = 1000
    final itemNumForEachRaffle = 5
    final raffleItemNums = [1, 5, 25, 125, 500]

    List<Map> buildRaffleItemForms(long raffleId) {
        List<Map> list = new ArrayList<>()
        Map raffleItemForm

        raffleItemForm = new HashMap()
        raffleItemForm.name = "一等奖"
        raffleItemForm.description = "iPad4 128GB"
        raffleItemForm.totalNum = raffleItemNums[0]
        raffleItemForm.raffleId = raffleId
        list.add(raffleItemForm)

        raffleItemForm = new HashMap()
        raffleItemForm.name = "二等奖"
        raffleItemForm.description = "iPad mini2 64GB"
        raffleItemForm.totalNum = raffleItemNums[1]
        raffleItemForm.raffleId = raffleId
        list.add(raffleItemForm)

        raffleItemForm = new HashMap()
        raffleItemForm.name = "三等奖"
        raffleItemForm.description = "1000元奖金"
        raffleItemForm.totalNum = raffleItemNums[2]
        raffleItemForm.raffleId = raffleId
        list.add(raffleItemForm)

        raffleItemForm = new HashMap()
        raffleItemForm.name = "四等奖"
        raffleItemForm.description = "500元奖金"
        raffleItemForm.totalNum = raffleItemNums[3]
        raffleItemForm.raffleId = raffleId
        list.add(raffleItemForm)

        raffleItemForm = new HashMap()
        raffleItemForm.name = "五等奖"
        raffleItemForm.description = "250元奖金"
        raffleItemForm.totalNum = raffleItemNums[4]
        raffleItemForm.raffleId = raffleId
        list.add(raffleItemForm)

        list
    }

    @Test
    void build() {
        for (i in 1..raffleNum) {
            List<Map> forms = buildRaffleItemForms(i)

            itemNumForEachRaffle.times {
                int idx = it
                HTTPBuilder httpBuilder = new HTTPBuilder("http://localhost:8080/raffleItem")
                httpBuilder.request(Method.POST) { req ->
                    requestContentType = ContentType.JSON
                    body = forms[idx]

                    response.success = { resp, json ->
                        println json
                    }
                }
            }
        }
    }
}
