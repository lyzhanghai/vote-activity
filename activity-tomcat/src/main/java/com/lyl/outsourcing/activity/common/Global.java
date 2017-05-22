package com.lyl.outsourcing.activity.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by liyilin on 2017/4/22.
 */
@Component
public final class Global {
    private static Global instance;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Properties properties;

    public Global() {
        instance = this;
    }

    private static Global getInstance() { return instance; }

    public static StringRedisTemplate getRedisTemplate() {
        return instance.redisTemplate;
    }

    public static ApplicationContext getApplicationContext() {
        return instance.applicationContext;
    }

    public static Properties getProperties() { return instance.properties; }
}
