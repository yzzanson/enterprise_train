package com.enterprise.mobile.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.exceptions.LoginException;
import com.enterprise.service.redis.RedisService;
import com.enterprise.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;


/**
 * 权限控制拦截器
 *
 * @author shisan
 * @create 2017-11-09 上午10:53
 **/
@Component
public class EnterpriseMobileInterceptor extends BaseController implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(EnterpriseMobileInterceptor.class);

    private final String PKRESULT = "/arena/getArenaPKResult.json";
    private final String WEEKRANKSHARE = "/rank/getWeekAnswerRankShare.json";
    private final String USERRANK = "/rank/getMyRank.json";
    private final String TOOLRANK = "/bag_tool_statics/getEffectTotalSummary.json";

    private final String COMPANYRANKGEN = "/rank/getWeekRank.json";
    private final String COMPANYRANK = "/rank/genRank.json";
    private final String INIT_PET_WEIGHT = "/mypet/initWeight.json";
    private final String CERTIFICATE_DETAIL = "/grain/getCertificateDetailNoLogin.json";
    private final String COMMUNITY_INVITE = "/community/saveInvite.json";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用于计算请求处理时间
        request.setAttribute("startTime", System.currentTimeMillis());

        //获取当前url
        String currentUrl = request.getRequestURI();

        StringBuffer sb = new StringBuffer();
        sb.append("requestUrl:[").append(currentUrl).append("],");
        sb.append("parameterMap:[");
        Enumeration<?> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String pName = parameterNames.nextElement().toString();
            sb.append(pName);
            sb.append("=");
            sb.append(request.getParameter(pName));
            sb.append(parameterNames.hasMoreElements() ? "," : "");
        }
        sb.append("]");
        if (currentUrl.contains("/login/logOut") || currentUrl.contains("/login/getConfig") || currentUrl.contains("/login/login") || currentUrl.contains("/login/testLogin") || currentUrl.contains("/login/getFromRedis") || currentUrl.contains("/login/getFromCookie")
                || currentUrl.contains(PKRESULT) || currentUrl.contains(WEEKRANKSHARE) || currentUrl.contains(USERRANK) || currentUrl.contains(TOOLRANK) || currentUrl.contains(COMPANYRANKGEN) || currentUrl.contains(COMPANYRANK)
                || currentUrl.contains("test") || currentUrl.contains("pressure") || currentUrl.contains(CERTIFICATE_DETAIL)) {
            return true;
        } else {
//            return true;

            if (request.getSession().getAttribute(CookieUtil.getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE)) != null) {
                String sid = CookieUtil.getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
                String userCookie = CookieUtil.getCookieValue(request, DDConstant.USER_MOBILE);
                MobileLoginUser loginUser = null;
                RedisService redisService = SpringContextHolder.getBean(RedisService.class);
                String userSessionString = (String) redisService.hget(RedisConstant.USER_SESSION_KEY + "_" + sid, sid);
                if (null != userSessionString) {
                    loginUser = (MobileLoginUser) JSONObject.parseObject(userSessionString, MobileLoginUser.class);
                    redisService.hset(RedisConstant.USER_SESSION_KEY + "_" + sid, sid, userSessionString, RedisConstant.EXPIRE_TIME_COMMON);
                    request.getSession().setAttribute(getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME_MOBILE), loginUser);
                } else {
                    loginUser = (MobileLoginUser) request.getSession().getAttribute(getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME_MOBILE));
                    if (loginUser != null) {
                        request.getSession().setAttribute(getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME_MOBILE), loginUser);
                    } else if (StringUtils.isNotEmpty(userCookie)) {
                        loginUser = MobileLoginUser.getUserFromCookieStr(userCookie);
                        request.getSession().setAttribute(getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME_MOBILE), loginUser);
                    } else {
                        throw new LoginException("请登录!");
                    }
                }
            } else {
                String sid = getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
                MobileLoginUser mobileLoginUser = (MobileLoginUser) request.getSession().getAttribute(sid);
                if (mobileLoginUser == null || mobileLoginUser.getUserID() == null) {
                    String cookieUserStr = getCookieValue(request, DDConstant.USER_MOBILE);
                    if (StringUtils.isEmpty(cookieUserStr)) {
                        if(currentUrl.contains(COMMUNITY_INVITE)){
                            if(request.getParameter("userId")!=null && (request.getParameter("companyId")!=null)){
                                return true;
                            }
                        }
                        throw new LoginException("请登录!");
                    }
                    mobileLoginUser = MobileLoginUser.getUserFromCookieStr(cookieUserStr);
                    if (mobileLoginUser != null) {
                        return true;
                    }
                }
                throw new LoginException("请登录!");
            }
            return true;

        }


    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        try {
            Cookie cookies[] = request.getCookies();
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    if (name.equalsIgnoreCase(cookies[i].getName())) {
                        return cookies[i].getValue();
                    }
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    private static String getCookieByName(Cookie[] cookies, String name) {
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie obj : cookies) {
            if (obj.getName().equals(name)) {
                return obj.getValue();
            }
        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) httpServletRequest.getAttribute("startTime");
        long endTime = System.currentTimeMillis();

//        logger.info("[" + handler + "], Controller的方法执行完毕之后, DispatcherServlet进行视图的渲染之前 耗时: " + (endTime - startTime) + "ms");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }


    public static void main(String[] args) {
        //Integer userID, Integer companyID, Integer role, String corpID, String dingID, String dingName, String avatar
        MobileLoginUser mobileLoginUser = new MobileLoginUser(24, 1, 3, "ding1231", "aifjasodasdasd", "拾叁", "http://a.jpg");
        Object json = JSONObject.toJSON(mobileLoginUser);
        System.out.println(json.toString());
    }
}
