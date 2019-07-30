package com.enterprise.mobile.web.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description 拦截器适配器,或者实现HandlerInterceptor,加密验证
 * @Author zezhouyang
 * @Date 18/12/4 下午5:36
 */
public class SignInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (handler instanceof HandlerMethod) {
//            Sign signAnnotation = ((HandlerMethod) handler).getMethodAnnotation(Sign.class);
//            if(signAnnotation!=null){
//                SortedMap<String, String> allParams = RequestUtil.getAllParams(request, null);
//                if(allParams==null || allParams!=null&&allParams.size()==1){
//                    allParams.put("userid", String.valueOf(MobileLoginUser.getUser().getUserID()));
//                }
//                boolean isSigned = SignUtil.verifySign(allParams);
//                return isSigned;
//            }
//        }
        return true;
    }
}
