package com.lyl.outsourcing.activity.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Controller结果渲染类
 * 把Controller的返回结果转换成SpringMVC View对象可以接受的形式
 * Created by liyilin on 2017/4/18.
 */
public class Render {
    public static ResponseEntity<String> string(String body, String type, String subtype, String encoding) {
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(new MediaType(type, subtype, Charset.forName(encoding)));
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    public static ResponseEntity<String> string(String body) {
        return string(body, "text", "plain", "utf-8");
    }

    public static ResponseEntity<String> string(String callBack,String body){
        StringBuffer sb = new StringBuffer();
        if(callBack != null && callBack.trim().length() > 0){
            sb.append(callBack).append("(").append(body).append(")");
        }else {
            sb.append(body);
        }
        return string(sb.toString(), "text", "plain", "utf-8");
    }

    static ResponseEntity<String> html(String body) {
        return string(body, "text", "html", "utf-8");
    }

    static ResponseEntity<String> redirect(String redirectUrl) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "html", Charset.forName("utf-8")));
        headers.setLocation(new URI(redirectUrl));
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    /**
     * 返回一个 json 响应
     * @param obj
     * @return
     */
    static ResponseEntity<String> json(Object obj) {
        String str=XUtil.toJson(obj);
        return string(str, "application", "json", "utf-8");
    }


    static ModelAndView modelView(Map map) {
        return modelView(map, null);
    }

    static ModelAndView modelView(Map map, String viewName) {
        ModelAndView mv=new ModelAndView();
        if (viewName != null) mv.setViewName(viewName);

        mv.getModel().putAll(map);
        return mv;
    }
}
