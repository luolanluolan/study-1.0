package com.study.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import com.study.util.ConfigDefinition;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author : luolan
 * @Date: 2022-05-26 9:19
 * @Description :
 */
@Slf4j
@WebFilter(urlPatterns = {"/*"}, filterName = "commonFilter")
public class CommonFilter implements Filter {


    @Override
    public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain chain) throws IOException, ServletException {
        log.info("===================doFilter=============");
        MDC.put(ConfigDefinition.REQUEST_MDC_KEY, String.valueOf(UUID.randomUUID()));
        HttpServletRequest request = (HttpServletRequest) paramServletRequest;
        HttpServletResponse response = (HttpServletResponse)paramServletResponse;
        String url = request.getServletPath();
        log.debug("请求URL：{}", url);
        RequestDispatcher requestDispatcher;
        requestDispatcher = request.getRequestDispatcher(url);
        requestDispatcher.forward(request, response);
    }
}
