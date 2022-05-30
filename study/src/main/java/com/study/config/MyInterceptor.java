package com.study.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.study.util.ConfigDefinition;

/**
 * @Author : luolan
 * @Date: 2022-05-26 9:20
 * @Description :
 */
@Slf4j
@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class MyInterceptor extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration ir = registry.addInterceptor(new HandlerInterceptor () {

            //在请求处理之前进行调用（Controller方法调用之前
            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                log.info("preHandle被调用=====");
                return true;    //如果false，停止流程，api被拦截
            }

            //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
            @Override
            public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//                log.info("postHandle被调用被调用=====");
            }

            //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
            @Override
            public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
                log.info("afterCompletion被调用====");
                MDC.remove(ConfigDefinition.REQUEST_MDC_KEY);
            }
        });
        ir.addPathPatterns("/**");  //指定该类拦截的url
        ir.excludePathPatterns("/static/**"); //忽略拦截的url
    }
}