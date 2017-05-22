package com.lyl.outsourcing.activity.service

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import com.lyl.outsourcing.activity.common.XUtil
import com.lyl.outsourcing.activity.dao.MaterialDao
import com.lyl.outsourcing.activity.dto.Result
import com.lyl.outsourcing.activity.dto.request.MaterialSaveReq
import com.lyl.outsourcing.activity.dto.request.MaterialUpdateReq
import com.lyl.outsourcing.activity.entity.Material
import com.lyl.outsourcing.activity.entity.MaterialExample
import com.lyl.outsourcing.activity.exception.ErrorCode
import com.lyl.outsourcing.activity.exception.ObjectNotFoundException
import com.lyl.outsourcing.activity.exception.WebException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.ImageInputStream
import java.text.SimpleDateFormat

@Service
class MaterialService {
    private static final Logger LOG = LoggerFactory.getLogger(MaterialService.class)

    static final String MATERIAL_TYPE_IMAGE = "image"
    static final String MATERIAL_TYPE_VIDEO = "video"
    static final String MATERIAL_TYPE_VOICE = "voice"

    @Value("\${material.dir}")
    private String MATERIAL_DIR

    @Value("\${material.ffmpeg}")
    private String ffmpeg

    @Value("\${resources.url.mapping}")
    private String resourcesUrlPrefix

    @Autowired
    private MaterialDao materialDao

    /**
     * 存储素材
     * @param materialSaveReq
     * @param file
     * @return
     * @throws IOException
     */
    Result save(MaterialSaveReq materialSaveReq, MultipartFile file) throws IOException {
        String type = materialSaveReq.type
        String name = materialSaveReq.name

        String uuid = UUID.randomUUID().toString().replace("-", "")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM")
        String dateStr = simpleDateFormat.format( new Date() )
        File dir = new File( "${MATERIAL_DIR}/${dateStr}" )
        File localFile = new File("${MATERIAL_DIR}/${dateStr}/${uuid}")

        Material material = new Material()
        material.setName(name)
        material.setType(type)
        material.setCreateTime(new Date())
        material.setContentType(file.getContentType())
        material.setFileLength(file.getSize())
        material.setPath( "/${resourcesUrlPrefix}/${dateStr}/${uuid}" )
        material.setId(uuid)
        if (!dir.exists()) dir.mkdir()
        file.transferTo(localFile)
        String properties = null

        switch (type) {
            case MATERIAL_TYPE_IMAGE:
                properties = buildImageProperties(localFile)
                break
            case MATERIAL_TYPE_VIDEO:
                properties = buildVideoProperties(localFile)
                break
            case MATERIAL_TYPE_VOICE:
                properties = buildVoiceProperties(localFile)
                break
        }
        material.setProperties(properties)

        materialDao.insert(material)
        return new Result(0, null, material)
    }

    /**
     * 更新
     * @param materialUpdateReq
     * @return
     */
    Result update(MaterialUpdateReq materialUpdateReq) {
        String id = materialUpdateReq.id
        String name = materialUpdateReq.name
        def material = materialDao.selectByPrimaryKey(id)
        material.setName(name)
        materialDao.updateByPrimaryKey(material)
        new Result(0, null, material)
    }

    /**
     * 批量删除
     * @param materialIdSet id集合
     * @return
     */
    Result delete(Set<String> materialIdSet) {
        def ans = []
        materialIdSet.each {
            try {
                def material = materialDao.selectByPrimaryKey(it)
                if (material == null)
                    throw new ObjectNotFoundException(it, Material.class)
                def type = material.getType()

                try {
                    // 删除文件
                    def file = new File(MATERIAL_DIR + material.getPath())
                    if (file.exists()) file.delete()

                    // 如果是视频，连同视频封面图也删除
                    if (type == MATERIAL_TYPE_VIDEO) {
                        file = new File(MATERIAL_DIR + material.getPath() + ".png")
                        if (file.exists()) file.delete()
                    }
                } catch (Exception e) {
                    LOG.warn("删除素材文件失败, 但依然会继续删除素材记录\n${e.getClass().getName()}: ${e.getMessage()}")
                }

                materialDao.deleteByPrimaryKey(it)
                ans << [id:it, success: true]
            } catch (Throwable e) {
                ans << [id: it, success:false, errorMsg: "${e.getClass().getName()}: ${e.getMessage()}"]
            }
        }
        new Result(0, null, ans)
    }

    /**
     * 分页获取资源列表
     * @param type
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Result page(String type, int pageIndex, int pageSize) {
        PageHelper.startPage(pageIndex, pageSize)
        MaterialExample example = new MaterialExample()
        example.or().andTypeEqualTo(type)
        example.setOrderByClause("create_time desc")
        List<Material> list = materialDao.selectByExample(example)
        def pageInfo = new PageInfo<Material>(list)
        new Result(0, null, pageInfo)
    }

    Result getById(String id) {
        def material = materialDao.selectByPrimaryKey(id)
        if (material == null)
            throw new ObjectNotFoundException(id, Material.class)
        new Result(0, null, material)
    }

    /**
     * 获取图片属性
     * @param file 图片文件
     * @return 图片属性
     */
    private String buildImageProperties(File file) {
        ImageInputStream iis
        try {
            iis = ImageIO.createImageInputStream(file)
            def readers = ImageIO.getImageReaders(iis)
            ImageReader reader = ++readers

            String format = reader.getFormatName()
            LOG.debug("获取图片格式为：${format}")

            def image = ImageIO.read(file)
            int width = image.getWidth(null)
            int height = image.getHeight(null)

            LOG.debug("获取图片宽高为：${width}x${height}")

            return XUtil.toJson([width: width, height: height, format:format])
        } catch (Throwable e) {
            throw new WebException(ErrorCode.IMAGE_PROCESS_ERROR, "${e.getClass().getName()}: ${e.getMessage()}")
        } finally {
            if (iis) iis.close()
        }
    }

    /**
     * 获取视频属性
     * 生成视频第一帧作为封面图
     * @param videoFile   视频文件
     * @param surfaceFile 导出封面图路径
     * @throws InterruptedException
     * @throws IOException
     */
    private String buildVideoProperties(File videoFile) throws InterruptedException, IOException {
        File surfaceFile = new File( videoFile.getAbsolutePath() + ".png" )

        def result
        try {
            result = exec([ffmpeg, "-y", "-i", videoFile.getAbsolutePath(), "-vframes", "1", "-ss", "0:0:0", "-an",
                  "-vcodec", "png", "-f", "rawvideo", "-s", "200*200", surfaceFile.getAbsolutePath()])
            if (result.exitValue != 0)
                throw new WebException(ErrorCode.VIDEO_PROCESS_ERROR, "${result}")
        } catch (Exception e) {
            throw new WebException(ErrorCode.VIDEO_PROCESS_ERROR, "${e.getClass().getName()}: ${e.getMessage()}")
        }

        final regexDuration = /Duration: (.*?), start: (.*?), bitrate: (\d*?) kb\/s/
        final regexVideo = /Video: (.*?), (.*?), (.*?)[,\s]/
        String content = result.msg

        def properties = new HashMap()
        content.find(regexDuration) {group ->
            properties.duration = group[1]         // 持续时长
            properties.bitrate = group[3]          // 比特率
        }

        content.find(regexVideo) {group ->
            properties.encodeFormat = group[1]     // 编码格式
            properties.videoFormat = group[2]      // 视频格式
            properties.resolution = group[3]       // 分辨率
        }

        XUtil.toJson(properties)
    }

    /**
     * 获取音频属性
     * @param file 音频文件
     * @return 音频属性
     */
    private String buildVoiceProperties(File file) {
        final regexDuration = /Duration: (.*?), start: (.*?), bitrate: (\d*?) kb\/s/
        final regexAudio = /Audio: (\w*), (\d*) Hz/

        def result
        try {
            result = exec([ffmpeg, "-i", file.getAbsolutePath()])
            if (result.exitValue != 1)
                throw new WebException(ErrorCode.VIDEO_PROCESS_ERROR, "${result}")
        } catch (Exception e) {
            throw new WebException(ErrorCode.VIDEO_PROCESS_ERROR, "${e.getClass().getName()}: ${e.getMessage()}")
        }

        String content = result.msg

        def properties = new HashMap()
        content.find(regexDuration) {group ->
            properties.duration = group[1]         // 持续时长
            properties.bitrate = group[3]          // 比特率
        }

        content.find(regexAudio) {group ->
            properties.encodeFormat = group[1]      // 音频编码
            properties.frequency = group[2]         // 采样频率
        }

        XUtil.toJson(properties)
    }

    private exec(List<String> cmd) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd)

        Process process = processBuilder.start()
        process.waitFor()

        int exitValue = process.exitValue()

        StringBuffer sb = new StringBuffer()
        InputStream errorStream = process.getErrorStream()
        InputStreamReader isr = new InputStreamReader(errorStream)
        BufferedReader br = new BufferedReader(isr)
        try {
            String line
            while( (line = br.readLine()) != null ) {
                sb.append(line + "\n")
            }
        } catch (Exception e) {
            throw e
        } finally {
            br.close()
            isr.close()
            errorStream.close()
        }

        [exitValue: exitValue, msg: sb.toString()]
    }
}
