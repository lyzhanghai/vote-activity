package com.lyl.outsourcing.activity.dto.response

/**
 * Created by liyilin on 2017/5/5.
 */
class VoteResp implements Serializable {
    long id
    long activityId
    String name
    String role
    String introduction
    Date startTime
    Date endTime
    Date createTime
    Date updateTime

    /**
     * 投票主题状态
     * 0: 未投票
     * 1：已投票
     * -1：已过期
     * null: 没有用户信息
     */
    Integer state
}
