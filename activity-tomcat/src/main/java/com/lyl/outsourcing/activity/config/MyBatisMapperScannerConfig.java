package com.lyl.outsourcing.activity.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis扫描器配置
 * 注意，由于MapperScannerConfigurer执行的比较早，所以必须有 @AutoConfigureAfter 注解
 * Created by liyilin on 2017/4/18.
 */
@Configuration
@AutoConfigureAfter(DaoConfig.class)
public class MyBatisMapperScannerConfig {
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.lyl.outsourcing.activity.dao.mapper");
        return mapperScannerConfigurer;
    }
}
