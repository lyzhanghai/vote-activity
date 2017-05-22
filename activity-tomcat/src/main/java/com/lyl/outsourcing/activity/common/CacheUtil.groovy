package com.lyl.outsourcing.activity.common

import com.fasterxml.jackson.core.type.TypeReference
import com.lyl.outsourcing.activity.dto.SessionUser
import com.lyl.outsourcing.activity.entity.AdminUser
import org.springframework.data.redis.core.StringRedisTemplate

import java.util.concurrent.TimeUnit

/**
 * Created by liyilin on 2017/4/23.
 */
class CacheUtil {
    static <T> T getCache(String key, int expire, Class<T> clazz, Closure<T> closure) {
        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        String json = redisTemplate.opsForValue().get(key)

        if (json) return XUtil.fromJson(json, clazz)
        else {
            T result = closure()
            if (result != null) {
                json = XUtil.toJson(result)
                redisTemplate.opsForValue().set(key, json, expire, TimeUnit.SECONDS)
                return result
            }
        }
        return null
    }

    static <T> T getCache(String key, Class<T> clazz, Closure<T> closure) {
        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        String json = redisTemplate.opsForValue().get(key)

        if (json) return XUtil.fromJson(json, clazz)
        else {
            T result = closure()
            if (result != null) {
                json = XUtil.toJson(result)
                redisTemplate.opsForValue().set(key, json)
                return result
            }
        }
        return null
    }

    static <T> List<T> getCacheList(String key, int expire, TypeReference<List<T>> type, Closure<List<T>> closure) {
        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        String json = redisTemplate.opsForValue().get(key)

        if (json) return XUtil.fromJson(json, type)
        else {
            List<T> result = closure()
            if (result != null) {
                json = XUtil.toJson(result)
                redisTemplate.opsForValue().set(key, json, expire, TimeUnit.SECONDS)
                return result
            }
        }
        return null
    }

    static void removeCache(String key) {
        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        redisTemplate.delete(key)
    }

    static String getValue(String key) {
        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        redisTemplate.opsForValue().get(key) as String
    }

    static SessionUser setCurrentUser(AdminUser user, List<String> roles) {
        String username = user.username
        String password = user.password
        SessionUser sessionUser = new SessionUser(user)
        sessionUser.setRoles(roles)

        String json = XUtil.toJson(sessionUser)
        String signature = CryptoUtil.hmacSha1(password, String.format("%d\n%s\n%d", sessionUser.getId(), username, new Date().getTime()))
        String key = CacheConst.user(signature)

        sessionUser.setSessionId(signature)

        StringRedisTemplate redisTemplate = Global.getRedisTemplate()
        redisTemplate.opsForValue().set(key, json, SessionConst.EXPIRE_USER_TOKEN, TimeUnit.SECONDS)

        sessionUser
    }

    static SessionUser getCurrentUser(String sessionId) {
        if (sessionId != null) {
            StringRedisTemplate redisTemplate = Global.getRedisTemplate()
            String json = redisTemplate.opsForValue().get(CacheConst.user(sessionId))

            if (json != null) {
                SessionUser user = XUtil.fromJson(json, SessionUser.class)
                // hit cache
                redisTemplate.expire(sessionId, SessionConst.EXPIRE_USER_TOKEN, TimeUnit.SECONDS)
                return user
            }
        }
        null
    }
}
