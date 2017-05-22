package com.lyl.outsourcing.activity.exception;

/**
 * Created by lyl on 2016/11/8.
 */
public enum ErrorCode {
    /**
     * 没有错误
     */
    OK(0),

    /**
     * 权限不足
     */
    ACCESS_DENIED_ERROR(200),

    /**
     * DAO错误
     */
    DAO_ERROR(201),

    /**
     * 域错误
     */
    FIELD_ERROR(202),

    /**
     * 全局异常
     */
    GLOBAL_ERROR(203),

    /**
     * 权限错误
     */
    AUTHORIZE_ERROR(204),

    /**
     * 未知错误
     */
    UNKNOWN_ERROR(210),

    /**
     * 找不到对象错误
     */
    OBJECT_NOT_FOUND_ERROR(301),

    /**
     * 图片处理异常
     */
    IMAGE_PROCESS_ERROR(10001),

    /**
     * 视频处理异常
     */
    VIDEO_PROCESS_ERROR(10002),

    /**
     * 音频处理异常
     */
    VOICE_PROCESS_ERROR(10003),

    /**
     * 重复投票异常
     */
    REPEAT_VOTE_ERROR(20001),

    /**
     * 投票时间异常
     */
    VOTE_TIME_ERROR(20002),

    /**
     * 抽奖信息修改异常
     */
    RAFFLE_UPDATE_ERROR(30001),

    /**
     * 抽奖项数量修改异常
     */
    RAFFLE_ITEM_NUM_UPDATE_ERROR(30002),

    /**
     * 重复抽奖异常
     */
    REPEAT_RAFFLE_ERROR(30003),

    /**
     * 没有抽奖
     */
    NO_RAFFLE_RECORD_ERROR(30004),

    /**
     * 已经兑奖过
     */
    HAS_ACHIEVE_ERROR(30005),

    /**
     * 没有中奖
     */
    NO_WINNING_PRICE_ERROR(30006),

    /**
     * 与抽奖主题关联的活动中，用户还有票没投完
     */
    EXISTS_UNFINISH_VOTE(30007),

    /**
     * 微信认证错误
     */
    WX_AUTHORIZE_EXCEPTION(40001);

    private int code = 0;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
