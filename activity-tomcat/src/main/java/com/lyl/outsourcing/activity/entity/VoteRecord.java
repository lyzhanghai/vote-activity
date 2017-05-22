package com.lyl.outsourcing.activity.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liyilin on 2017/5/3.
 */
public class VoteRecord implements Serializable {
    private String openId;
    private long voteId;
    private long voteItemId;
    private Date voteTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public long getVoteItemId() {
        return voteItemId;
    }

    public void setVoteItemId(long voteItemId) {
        this.voteItemId = voteItemId;
    }

    public Date getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Date voteTime) {
        this.voteTime = voteTime;
    }
}
