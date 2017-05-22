package com.lyl.outsourcing.activity.mvc;

import com.lyl.outsourcing.activity.mvc.filter.ContextFilter;
import com.lyl.outsourcing.activity.mvc.filter.RequestFilter;
import com.lyl.outsourcing.activity.mvc.resolver.WebHandlerExceptionResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.*;

/**
 * @author lyl
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${resources.url.mapping}")
    private String resourcesUrlMapping;

    @Value("${material.dir}")
    private String materialDir;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new DataTablePageableHandlerMethodArgumentResolver());
//        argumentResolvers.add(new SortHandlerMethodArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
//        converters.add( new MyGsonHttpMessageConverter());
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add( new WebHandlerExceptionResolver() );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + resourcesUrlMapping + "/**")
                .addResourceLocations("/", materialDir + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor( new WxRequestInterceptor() ).addPathPatterns("/*");
    }

    @Bean
    public FilterRegistrationBean threadContextFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        // 上下文过滤器
        ContextFilter contextFilter = new ContextFilter();
        registrationBean.setFilter(contextFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);

        // RequestFilter
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean requestFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        // 上下文过滤器
        RequestFilter requestFilter = new RequestFilter();
        registrationBean.setFilter(requestFilter);
        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);

        // RequestFilter
        return registrationBean;
    }

//    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding("UTF-8");
//        return multipartResolver;
//    }

    /**
     * 跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*")
                .allowedMethods("*")
                .allowedOrigins("*");
    }
}