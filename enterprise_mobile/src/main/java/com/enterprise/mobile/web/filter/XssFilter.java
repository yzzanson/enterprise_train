package com.enterprise.mobile.web.filter;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/26 下午2:10
 */
@Configuration
public class XssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        filterChain.doFilter(new XssHttpServletRequestWrapper(request),servletResponse);
    }

    @Override
    public void destroy() {

    }
}
