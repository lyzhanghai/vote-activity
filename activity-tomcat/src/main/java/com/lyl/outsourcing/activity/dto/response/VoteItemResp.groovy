package com.lyl.outsourcing.activity.dto.response

/**
 * Created by liyilin on 2017/5/3.
 */
class VoteItemResp implements Serializable {
    long id
    long voteId
    String title
    String subTitle
    String description
    String imageUrl
    long voteNum        // 票数
}
