package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.WxUtil
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * Created by liyilin on 2017/4/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class WxTestCase {
    @Test
    void testAccessToken() {
        println WxUtil.getAccessToken()
        println WxUtil.getAccessToken()
        println WxUtil.getAccessToken()
    }

//    @Test
//    void testGetMaterialList() {
//        println WxUtil.getMaterialList("image", 0, 20)
//    }
//
//    @Test
//    void testUploadMaterial() {
//        println WxUtil.uploadMaterial( WxUtil.MATERIAL_TYPE_IMAGE, new File("/Users/liyilin/Downloads/tmp/test.jpg") )
//    }
//
//    @Test
//    void testDeleteMaterial() {
//        def json = WxUtil.uploadMaterial( WxUtil.MATERIAL_TYPE_IMAGE, new File("/Users/liyilin/Downloads/tmp/test.jpg") )
//        println json
//        assert json != null && json.media_id != null
//        json = WxUtil.deleteMaterial(json.media_id)
//        println json
//        assert json.errcode == 0
//    }
//
//    @Test
//    void testMenuCreate() {
//        def menu = [
//                button: [
//                    [
//                        type: "view",
//                        name: "主页",
//                        url: "https://open.weixin.qq.com/connect/oauth2/authorize?" +
//                                "appid=${WxUtil.appId}" +
//                                "&redirect_uri=http%3A%2F%2F39.108.6.235%2Factivity%2Factivity" +
//                                "&response_type=code&scope=snsapi_base" +
//                                "&state=${WxUtil.STATE_AUTHORIZE}#wechat_redirect"
//                    ]
//                ]
//        ]
//
//        def json = WxUtil.menuCreate( menu )
//        println json
//        assert json != null && json.errcode == 0
//    }

    @Test
    void testGetOpenIdByCode() {
        println WxUtil.getOpenIdByCode("001vL8KK1CIOM700SqJK1gs6KK1vL8KQ")
    }

    @Test
    void testGetMenu() {
        println WxUtil.getMenu()
    }
}
