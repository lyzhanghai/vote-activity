package com.lyl.outsourcing.activity.test.data

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.MaterialSaveReq
import com.lyl.outsourcing.activity.test.TestBase
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.entity.mime.content.InputStreamBody
import org.apache.http.entity.mime.content.StringBody
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * Created by liyilin on 2017/5/20.
 */
class MaterialDataBuilder {
    @Test
    void testSaveImage() {
        File file = new File("/Users/liyilin/Downloads/tmp/test.jpg")
        FileInputStream fileInputStream = new FileInputStream(file)
        MaterialSaveReq saveReq = new MaterialSaveReq()
        saveReq.name = file.name
        saveReq.type = "image"

        def http = new HTTPBuilder("http://39.108.6.235/activity/material")

        http.request(Method.POST) { req ->
            headers.'Content-Type' = "application/json"
            MultipartEntity multiPartContent = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE)
            multiPartContent.addPart("file", new InputStreamBody(fileInputStream, "image/jpeg", file.absolutePath))
            multiPartContent.addPart("json", new StringBody( XUtil.toJson(saveReq) ))
            req.setEntity(multiPartContent)

            response.success = { resp, json ->
                println json
            }
        }
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
