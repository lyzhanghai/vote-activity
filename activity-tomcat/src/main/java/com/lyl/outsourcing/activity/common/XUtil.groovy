package com.lyl.outsourcing.activity.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper

import java.text.SimpleDateFormat

/**
 * Created by liyilin on 2017/4/23.
 */
final class XUtil {
    static ObjectMapper objectMapper

    static {
        objectMapper  = new ObjectMapper()
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    static ObjectMapper getObjectMapper() {
        return objectMapper
    }

    static String getProperties(ResourceBundle bundle, String key, boolean throwIfExpcetion=false) {
        try {
            return bundle.getString(key)
        } catch (Throwable e) {
            if (throwIfExpcetion) throw e
        }
        null
    }

    static <T> List<T> fromJsonArray(String json, Class<T> elementClass) {
        JavaType javaType = getObjectMapper().getTypeFactory().constructParametricType(ArrayList.class, elementClass)
        return getObjectMapper().readValue(json, javaType)
    }

    static <T> T fromJson(String json, Class<T> clazz) {
        return getObjectMapper().readValue(json, clazz)
    }

    static <T> T fromBytes(byte[] bytes, Class<T> clazz) {
        return getObjectMapper().readValue(bytes, clazz)
    }

    static <T> T fromJson(String json, TypeReference<T> type) {
        return getObjectMapper().readValue(json, type)
    }

    static String toJson(Object src) {
        return objectMapper.writeValueAsString(src)
    }

    static byte[] toBytes(Object src) {
        return objectMapper.writeValueAsBytes(src)
    }

    static <T> T clone(Object src, Class<T> clazz) {
        def dest = clazz.newInstance()
        src.properties.each {key, value ->
            try {
                if (dest.hasProperty(key))
                    dest[key] = value
            } catch (Exception e) {}
        }
        dest
    }

    static void copy(Object src, Object dest) {
        src.properties.each {key, value ->
            try {
                if (dest.hasProperty(key))
                    dest[key] = value
            } catch (Exception e) {}
        }
    }

    static Date parse(String dateStr, String pattern="yyyy-MM-dd HH:mm:ss") {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern)
        simpleDateFormat.parse(dateStr)
    }

    static String format(Date date, String pattern="yyyy-MM-dd HH:mm:ss") {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern)
        simpleDateFormat.format(date)
    }
}
