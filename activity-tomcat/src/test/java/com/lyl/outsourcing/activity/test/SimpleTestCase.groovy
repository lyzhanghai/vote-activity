package com.lyl.outsourcing.activity.test

import com.mchange.io.FileUtils

import java.util.regex.Matcher

/**
 * Created by liyilin on 2017/4/30.
 */
class SimpleTestCase extends GroovyTestCase {
    void testBuildMaterialId() {
        String uuid = UUID.randomUUID().toString().replace('-', '')
        println "${uuid}: ${uuid.length()}"
    }

    void testRegex() {
        def regexDuration = /Duration: (.*?), start: (.*?), bitrate: (\d*?) kb\/s/
        String regexVideo = /Video: (.*?), (.*?), (.*?)[,\s]/
        String content = FileUtils.getContentsAsString( new File("src/test/resources/1.txt") )

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

        println properties
    }
}
