package com.lyl.outsourcing.activity.test.controller

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.MaterialSaveReq
import com.lyl.outsourcing.activity.test.TestBase
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by liyilin on 2017/5/20.
 */
class MaterialControllerTest extends TestBase {
    @Test
    void testSaveImage() {
        File file = new File("/Users/liyilin/Downloads/tmp/test.jpg")
        FileInputStream fileInputStream = new FileInputStream(file)
        MaterialSaveReq saveReq = new MaterialSaveReq()
        saveReq.name = file.name
        saveReq.type = "image"

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.name, "image/jpeg", fileInputStream)
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload("/material")
                .file(multipartFile)
                .contentType("application/json;charset=utf8")
                .content(XUtil.toJson(saveReq))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()

        Result result = getResult(mvcResult)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void testSaveVideo() {
        File file = new File("/Users/liyilin/Downloads/tmp/test.ogg")
        FileInputStream fileInputStream = new FileInputStream(file)
        MaterialSaveReq saveReq = new MaterialSaveReq()
        saveReq.name = file.name
        saveReq.type = "video"

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.name, "application/ogg", fileInputStream)
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload("/material")
                        .file(multipartFile)
                        .contentType("application/json;charset=utf8")
                        .content(XUtil.toJson(saveReq))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()

        Result result = getResult(mvcResult)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }

    @Test
    void testSaveVoice() {
        File file = new File("/Users/liyilin/Downloads/tmp/test.mp3")
        FileInputStream fileInputStream = new FileInputStream(file)
        MaterialSaveReq saveReq = new MaterialSaveReq()
        saveReq.name = file.name
        saveReq.type = "voice"

        MockMultipartFile multipartFile = new MockMultipartFile("file", file.name, "audio/mp3", fileInputStream)
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.fileUpload("/material")
                        .file(multipartFile)
                        .contentType("application/json;charset=utf8")
                        .content(XUtil.toJson(saveReq))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()

        Result result = getResult(mvcResult)
        assertResult(result, 0)
        println XUtil.toJson(result)
    }
}
