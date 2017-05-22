package com.lyl.outsourcing.activity.common

import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Created by liyilin on 2017/5/18.
 */
@Component
class WxUtil {
    private static final Logger LOG = LoggerFactory.getLogger(WxUtil.class)

    private static String appId
    private static String appSecret
    private static String token

    public static final String STATE_AUTHORIZE = "authorize"

    static String getAccessToken() {
        CacheUtil.getCache(CacheConst.accessToken(), CacheConst.ACCESS_TOKEN_EXPIRE, String.class) {
            LOG.info("缓存中找不到AccessToken，尝试调用微信接口进行获取...")
            String jsonStr = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=${appId}&secret=${appSecret}".toURL().text
            LOG.info("获取结果：" + jsonStr)
            JsonSlurper jsonSlurper = new JsonSlurper()
            def json = jsonSlurper.parseText(jsonStr)
            json.access_token
        }
    }

    static String getOpenIdByCode(String code) {
        LOG.info("调用微信接口，通过Code获取OpenID...")
        String jsonStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appId}&secret=${appSecret}&code=${code}&grant_type=authorization_code".toURL().text
        LOG.info("获取结果：" + jsonStr)
        JsonSlurper jsonSlurper = new JsonSlurper()
        def json = jsonSlurper.parseText(jsonStr)
        json.openid
    }

    static String getMenu() {
        LOG.info("调用微信接口，通过菜单信息...")
        String jsonStr = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=${accessToken}".toURL().text
        LOG.info("获取结果：" + jsonStr)
        JsonSlurper jsonSlurper = new JsonSlurper()
        def json = jsonSlurper.parseText(jsonStr)
        json
    }

    static String getAppId() {
        return appId
    }

    static String getAppSecret() {
        return appSecret
    }

    static String getToken() {
        return token
    }

    @Value("\${wx.appId}")
    void setAppId(String appId) {
        WxUtil.appId = appId
    }

    @Value("\${wx.appSecret}")
    void setAppSecret(String appSecret) {
        WxUtil.appSecret = appSecret
    }

    @Value("\${wx.token}")
    void setToken(String token) {
        WxUtil.token = token
    }
}
