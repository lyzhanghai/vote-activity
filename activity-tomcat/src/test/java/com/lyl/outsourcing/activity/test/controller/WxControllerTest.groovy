package com.lyl.outsourcing.activity.test.controller

import com.lyl.outsourcing.activity.test.TestBase
import org.junit.Test
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by liyilin on 2017/5/17.
 */
class WxControllerTest extends TestBase {
    @Test
    void testValid() {
        final signature = "1700dc9c457ea4cb86ede1866288cbff29ae1e73"
        final echostr = "8069502750502579935"
        final timestamp = "1495017116"
        final nonce = "1670944093"

        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/wx/valid")
                        .param("signature", signature)
                        .param("echostr", echostr)
                        .param("timestamp", timestamp)
                        .param("nonce", nonce)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
    }
}
