package com.lyl.outsourcing.activity.controller

import com.lyl.outsourcing.activity.common.CryptoUtil
import com.lyl.outsourcing.activity.common.Render
import com.lyl.outsourcing.activity.service.VoteItemService
import com.lyl.outsourcing.activity.service.VoteService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Created by liyilin on 2017/5/17.
 */
@Controller
@RequestMapping("/wx")
class WxController {
    private static final Logger LOG = LoggerFactory.getLogger(WxController.class)

    private final String token = "liyilin"

    @Autowired
    private VoteService voteService
    @Autowired
    private VoteItemService voteItemService

    /**
     * 微信认证接口
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @RequestMapping("/valid")
    ResponseEntity<String> valid(String signature, String timestamp, String nonce, String echostr) {
        // 排序
        String sortString = sort(token, timestamp, nonce)
        //加密
        def digest = CryptoUtil.sha1(sortString.bytes)

        StringBuffer hexstr = new StringBuffer()
        String shaHex
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF)
            if (shaHex.length() < 2) {
                hexstr.append(0)
            }
            hexstr.append(shaHex)
        }

        String myToken = hexstr.toString()

        //结果
        LOG.info("计算Token：" + myToken)

        //校验签名
        if (myToken != null && myToken != "" && myToken == signature) {
            LOG.info("签名校验通过")
            return Render.string(echostr)       // 如果校验成功，则返回回声字符串
        } else {
            LOG.info("签名校验失败。")
            return Render.string("")
        }
    }

    /**
     * 排序方法
     * @param token
     * @param timestamp
     * @param nonce
     * @return
     */
    private String sort(String token, String timestamp, String nonce) {
        def strArray = [ token, timestamp, nonce ]
        strArray = strArray.sort()

        StringBuilder sbuilder = new StringBuilder()
        for (String str : strArray) {
            sbuilder.append(str)
        }

        return sbuilder.toString()
    }
}
