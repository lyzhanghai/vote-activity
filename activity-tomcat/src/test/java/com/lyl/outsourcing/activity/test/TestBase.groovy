package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.mvc.filter.ContextFilter
import com.lyl.outsourcing.activity.mvc.filter.RequestFilter
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Created by liyilin on 2017/5/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class TestBase {
    @Autowired
    WebApplicationContext wac
    MockMvc mockMvc

    @Before
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new ContextFilter(), "/*")
                .addFilter(new RequestFilter(), "/*")
                .build()
    }

    Result getResult(MvcResult mvcResult) {
        String json = mvcResult.getResponse().getContentAsString()
        XUtil.fromJson(json, Result.class)
    }

    void assertResult(Result result, int code) {
        assert result.code == code : "${result.code}: ${result.msg}"
    }
}
