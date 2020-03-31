package com.enterprise.mobile.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description 通过对请求Head的refer信息进行校验来判断是否存在crfs漏洞
 * @Author zezhouyang
 * @Date 18/11/28 下午4:48
 */
@Deprecated
public class TokenInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);

//    @Resource
//    private CSRFTokenUtil csrfTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // token验证方式
        /*
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        TokenRequired methodAnnotation = method.getAnnotation(TokenRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            String requstCSRFToken = httpServletRequest.getHeader("csrf-token");
            if(csrfTokenUtil.verifyToken(requstCSRFToken)){
                csrfTokenUtil.deleteToken(requstCSRFToken);//验证通过后，立即删除token，可以表单防止重复提交。
                return true;
            }
            return false;
        }
        */

//        if (!(handler instanceof HandlerMethod)) {
//            return true;
//        }
//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Method method = handlerMethod.getMethod();
//        // 判断接口是否需要登录
//        TokenRequired methodAnnotation = method.getAnnotation(TokenRequired.class);
//        // 有 @LoginRequired 注解，需要认证
//        if (methodAnnotation != null) {
//            String referrer = httpServletRequest.getHeader("referer");
////            logger.debug("referrer:{}", referrer);
//            StringBuffer stringBuffer = new StringBuffer();
//            stringBuffer.append(httpServletRequest.getScheme()).append("://").append(httpServletRequest.getServerName());
////            logger.debug("basePath:{}", stringBuffer);
//            if (StringUtils.isNotEmpty(referrer) && String.valueOf(stringBuffer).indexOf(referrer) >= 0) {
//                return true;
//            } else {
//                return false;
//            }
//        }




        /**
         * refer验证方式 暂时先去掉
         * */
        String currentUrl = httpServletRequest.getRequestURI();
        if (currentUrl.contains("/login") || currentUrl.contains("/test")) {
            return true;
        } else {
            String referrer = httpServletRequest.getHeader("referer");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(httpServletRequest.getServerName());
            if (StringUtils.isNotEmpty(referrer) && String.valueOf(referrer).indexOf(stringBuffer.toString()) >= 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        /*
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 判断接口是否需要登录
        TokenRequired methodAnnotation = method.getAnnotation(TokenRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            String new_token = csrfTokenUtil.generate();
            httpServletRequest.setAttribute("csrf-token", new_token);
        }
        */
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    // 检测到没有token，直接返回不验证
    public void dealErrorReturn(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object obj) {
        String json = (String) obj;
        PrintWriter writer = null;
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html; charset=utf-8");
        try {
            writer = httpServletResponse.getWriter();
            writer.print(json);

        } catch (IOException ex) {
            logger.error("response error", ex);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
