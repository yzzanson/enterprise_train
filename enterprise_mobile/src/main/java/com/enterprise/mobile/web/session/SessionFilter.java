package com.enterprise.mobile.web.session;

import com.enterprise.base.common.DDConstant;
import com.enterprise.mobile.web.session.wrapper.HttpServletRequestWrapper;
import com.enterprise.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by anson on 17/9/12.
 */
public class SessionFilter extends OncePerRequestFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        String sid = CookieUtil.getCookieValue(request, GlobalConstant.JSESSIONID);
        String sid = CookieUtil.getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
        logger.info("cookie_sid值初始化:"+sid);
        if(StringUtils.isEmpty(sid) || sid.length()==0){
            sid = getUuid();
//            CookieUtil.setCookie(request, response, GlobalConstant.JSESSIONID, sid, 60 * 60);
            CookieUtil.setCookie(request, response, DDConstant.COOKIE_NAME_MOBILE, sid, 60 * 60);
        }
        HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper(sid,request);
        chain.doFilter(httpServletRequestWrapper,response);
    }

    public static String getUuid() {
        return UUID.randomUUID().toString();
    }

}
