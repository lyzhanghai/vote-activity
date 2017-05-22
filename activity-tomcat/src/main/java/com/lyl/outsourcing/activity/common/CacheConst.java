package com.lyl.outsourcing.activity.common;

/**
 * Created by liyilin on 2017/4/21.
 */
public class CacheConst {
    private static final String PREFIX = "platform:";

    public static final int RAFFLE_ITEM_NUMS_EXPIRE = 24 * 60 * 60;     // 抽奖记录，秒

    public static final int VOTE_RECORD_EXPIRE = 24 * 60 * 60;          // 投票记录，秒

    public static final int ACCESS_TOKEN_EXPIRE = 2 * 60 * 60;          // AccessToken, 秒

    /**
     * Hash
     * @return
     */
    public static String primaryKey() { return PREFIX + "PK"; }

    public static String menu() {
        return PREFIX + "menu";
    }

    public static String user(String key) {
        return PREFIX + "user:" + key;
    }

    public static String accessToken() { return PREFIX + "accessToken"; }

    public static String activityVote(long activityId) { return PREFIX + "activityVote:" + activityId; }

    /**
     * Hash类型
     * @param voteId
     * @return
     */
    public static String voteRecord(long voteId) { return PREFIX + "voteRecord:" + voteId; }

    /**
     * Hash类型
     * @param voteId
     * @return
     */
    public static String voteStatistics(long voteId) { return PREFIX + "voteStatistics:" + voteId; }

    /**
     * RaffleItem对象
     * @param raffleItemId
     * @return
     */
    public static String raffleItem(long raffleItemId) { return PREFIX + "raffleItem:" + raffleItemId; }

    /**
     * RaffleItem剩余数量
     * @param raffleItemid
     * @return
     */
    public static String raffleItemNum(long raffleItemid) { return PREFIX + "reffleItem:" + raffleItemid + ":num";}

    /**
     * Set
     * @param raffleId
     * @return
     */
    public static String raffleItemIdxByRaffleId(long raffleId) { return PREFIX + "raffleItemIdxByRaffleId:" + raffleId; }

    /**
     * Hash, raffleRecord:{raffleId}  --  [openId]:[raffleRecord]
     * @param raffleId
     * @return
     */
    public static String raffleRecord(long raffleId) { return PREFIX + "raffleRecord:" + raffleId; }
}