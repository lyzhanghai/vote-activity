package com.lyl.outsourcing.activity.mvc.filter

import com.lyl.outsourcing.activity.common.XUtil
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import sun.nio.ch.IOUtil

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Created by liyilin on 2017/5/17.
 */
class RequestFilter implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(RequestFilter.class)

    @Override
    void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request
        String method = httpServletRequest.getMethod()
        String url = httpServletRequest.requestURL
        String remoteAddr = httpServletRequest.remoteAddr
        String params = XUtil.toJson( httpServletRequest.parameterMap )
        String cookies = XUtil.toJson( httpServletRequest.cookies )
//        String body = getBodyContent(httpServletRequest)
        LOG.info( "访问记录：[remoteAddr:${remoteAddr}, URI:${url}, method:${method}, params:${params}, cookies:${cookies}']" )

        chain.doFilter(request, response)
    }

    @Override
    void destroy() {

    }

    private String getBodyContent(HttpServletRequest request, String charset="UTF-8") {
        InputStream inputStream = request.getInputStream()
        byte[] bytes = new byte[inputStream.available()]
        IOUtils.readFully(inputStream, bytes)
        inputStream.close()
        new String(bytes, charset)
    }
}
