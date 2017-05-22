package com.lyl.outsourcing.activity.config;

import com.github.pagehelper.PageHelper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Dao配置
 * 主要做MyBatis的基本配置
 * 注意: @EnableTransactionManagement 注解支持注解事务
 * Created by liyilin on 2017/4/18.
 */
@Configuration
@EnableTransactionManagement
public class DaoConfig implements TransactionManagementConfigurer {
    /**
     * C3P0连接池Bean
     * 配置属性在 classpath:application-db.properties 配置文件中
     * 主要配置连接url, 用户名, 密码等
     * @return C3P0连接池Bean
     */
    @Bean(destroyMethod = "close")
    @ConfigurationProperties(prefix="datasource.mysql")
    public DataSource dataSource() {
        return new ComboPooledDataSource();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate() {
        return new SqlSessionTemplate( sqlSessionFactoryBean() );
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource( dataSource() );
        bean.setTypeAliasesPackage("com.lyl.outsourcing.activity.entity");

        //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("reasonable", "true");
        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        properties.setProperty("params", "count=countSql");
        pageHelper.setProperties(properties);

        //添加插件
        bean.setPlugins(new Interceptor[]{pageHelper});

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] mapper = resolver.getResources("classpath:mapper/*.xml");
            Resource[] custom = resolver.getResources("classpath:mapper/custom/*.xml");
            Resource[] mapperLocations = new Resource[mapper.length + custom.length];
            System.arraycopy(mapper, 0, mapperLocations, 0, mapper.length);
            System.arraycopy(custom, 0, mapperLocations, mapper.length, custom.length);
            bean.setMapperLocations(mapperLocations);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager( dataSource() );
    }
}
