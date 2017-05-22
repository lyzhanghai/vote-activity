package com.lyl.outsourcing.activity.mvc.resolver;

import com.lyl.outsourcing.activity.common.XUtil;
import com.lyl.outsourcing.activity.dto.Result;
import com.lyl.outsourcing.activity.exception.WebException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyilin on 2017/4/21.
 */
public class WebHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(WebHandlerExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        LOG.error( ex.getClass().getName() + ": " + ex.getMessage() );

        if (ex instanceof WebException) {
            write( new Result(((WebException) ex).getErrorCode(), ex.toString()), response);
        } else if (ex instanceof MethodArgumentNotValidException) {
            write( buildErrorResult((MethodArgumentNotValidException) ex) , response);
        } else {
            // 未处理错误
            ex.printStackTrace();
            write( new Result(-1, ex.getClass().getName() + ": " + ex.getMessage()), response );
        }
        return new ModelAndView();
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private void write(Object obj, HttpServletResponse response) {
        write(XUtil.toBytes(obj), response);
    }

    private void write(byte[] bytes, HttpServletResponse response) {
        try {
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    private Result buildErrorResult(MethodArgumentNotValidException ex) {
        Map<String, String> map = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String field = error.getField();
            String messgae = error.getDefaultMessage();
            map.put(field, messgae);
        }
        return new Result(-1, "表单参数验证不通过", map);
    }
}
