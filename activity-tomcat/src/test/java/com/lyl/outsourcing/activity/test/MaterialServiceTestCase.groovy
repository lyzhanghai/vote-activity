package com.lyl.outsourcing.activity.test

import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.MaterialSaveReq
import com.lyl.outsourcing.activity.dto.request.MaterialUpdateReq
import com.lyl.outsourcing.activity.entity.Material
import com.lyl.outsourcing.activity.service.MaterialService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

/**
 * Created by liyilin on 2017/4/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
@Rollback
class MaterialServiceTestCase {
    @Autowired
    MaterialService materialService

    @Test
    void testSaveImage() {
        final filepath = "/Users/liyilin/Downloads/tmp/test.jpg"
        MaterialSaveReq materialSaveReq = new MaterialSaveReq()
        materialSaveReq.name = "1.jpg"
        materialSaveReq.type = MaterialService.MATERIAL_TYPE_IMAGE
        InputStream inputStream = new FileInputStream(filepath)
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", inputStream)

        Result result = materialService.save(materialSaveReq, multipartFile)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson(result)
    }

    private batchSaveRandom(int num) {
        Random random = new Random()
        final ans = []
        num.each {
            int idx = random.nextInt(3)
            switch (idx) {
                case 0:
                    ans << batchSaveImage(1)
                    break
                case 1:
                    ans << batchSaveVoice(1)
                    break
                case 2:
                    ans << batchSaveVideo(1)
                    break
            }
        }
        return ans
    }

    private batchSaveImage(int num) {
        def ans = []
        num.each {
            final filepath = "/Users/liyilin/Downloads/tmp/test.jpg"
            MaterialSaveReq materialSaveReq = new MaterialSaveReq()
            materialSaveReq.name = "1.jpg"
            materialSaveReq.type = MaterialService.MATERIAL_TYPE_IMAGE
            InputStream inputStream = new FileInputStream(filepath)
            MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", inputStream)

            Result result = materialService.save(materialSaveReq, multipartFile)
            assert result.code == 0 : "${result.code}: ${result.msg}"
            ans << (result.data as Material).id
        }
        ans
    }

    private batchSaveVideo(int num) {
        def ans = []
        num.each {
            final filepath = "/Users/liyilin/Downloads/tmp/test.ogg"
            MaterialSaveReq materialSaveReq = new MaterialSaveReq()
            materialSaveReq.name = "1.ogg"
            materialSaveReq.type = MaterialService.MATERIAL_TYPE_VIDEO
            InputStream inputStream = new FileInputStream(filepath)
            MockMultipartFile multipartFile = new MockMultipartFile("file", "test.ogg", "application/ogg", inputStream)

            Result result = materialService.save(materialSaveReq, multipartFile)
            assert result.code == 0 : "${result.code}: ${result.msg}"
            ans << (result.data as Material).id
        }
        ans
    }

    private batchSaveVoice(int num) {
        def ans = []
        num.each {
            final filepath = "/Users/liyilin/Downloads/tmp/test.mp3"
            MaterialSaveReq materialSaveReq = new MaterialSaveReq()
            materialSaveReq.name = "1.mp3"
            materialSaveReq.type = MaterialService.MATERIAL_TYPE_VOICE
            InputStream inputStream = new FileInputStream(filepath)
            MockMultipartFile multipartFile = new MockMultipartFile("file", "test.mp3", "audio/mp3", inputStream)

            Result result = materialService.save(materialSaveReq, multipartFile)
            assert result.code == 0 : "${result.code}: ${result.msg}"
            ans << (result.data as Material).id
        }
        ans
    }

    @Test
    void testSaveVideo() {
        batchSaveVideo(10)
    }

    @Test
    void testSaveVoice() {
        batchSaveVoice(10)
    }

    @Test
    void testUpdate() {
        MaterialUpdateReq materialUpdateReq = new MaterialUpdateReq()
        materialUpdateReq.id = "dbfdc7b953fe4d7b8e7be2184160b0fd"
        materialUpdateReq.name = "ccccccc"
        Result result = materialService.update(materialUpdateReq)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson(result.data)
    }

    @Test
    void testDelete() {
        Set<String> idSet = batchSaveRandom(10)
        materialService.delete(idSet)
    }

    @Test
    void testPage() {
//        batchSaveVideo(10)
        String type = MaterialService.MATERIAL_TYPE_IMAGE
        def result = materialService.page(type, 1, 5)
        assert result.code == 0 : "${result.code}: ${result.msg}"
        println XUtil.toJson(result.data)
    }

    @Test
    void testGetById() {
        def list = batchSaveVideo(1)
        def id = list[0]
        def result = materialService.getById(id)
        assert result.code == 0: "${result.code}: ${result.msg}"
        println XUtil.toJson(result)
    }
}
