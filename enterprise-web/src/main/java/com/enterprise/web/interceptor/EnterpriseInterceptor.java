package com.enterprise.web.interceptor;

import com.enterprise.base.common.LoginUser;
import com.enterprise.base.entity.right.RightResourceUrlEntity;
import com.enterprise.base.entity.right.UserRightGroupEntity;
import com.enterprise.service.right.RightResourceService;
import com.enterprise.service.right.RightResourceUrlService;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.util.AssertUtil;
import com.enterprise.web.constants.UrlConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 权限控制拦截器
 *
 * @author shisan
 * @create 2017-11-09 上午10:53
 **/
@Component
public class EnterpriseInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(EnterpriseInterceptor.class);

    @Resource
    private RightResourceService rightResourceService;

    @Resource
    private UserRightGroupService userRightGroupService;

    @Resource
    private RightResourceUrlService rightResourceUrlService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用于计算请求处理时间
        request.setAttribute("startTime", System.currentTimeMillis());

        //获取当前url
        String currentUrl = request.getRequestURI();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("requestUrl:[").append(currentUrl).append("],");
        stringBuffer.append("parameterMap:[");
        Enumeration<?> parameterNames = request.getParameterNames();
       // String resourceId = "";
        while (parameterNames.hasMoreElements()) {
            String pName = parameterNames.nextElement().toString();
            stringBuffer.append(pName);
            stringBuffer.append("=");
            stringBuffer.append(request.getParameter(pName));
            stringBuffer.append(parameterNames.hasMoreElements() ? "," : "");
            //if(pName.equals("resourceId")){
           //    resourceId = request.getParameter(pName);
            //}
        }
        stringBuffer.append("]");

        logger.info("当前请求URL = " + stringBuffer.toString());

        if(!(currentUrl.contains("/login/logOut")) && !(currentUrl.contains("/login/getConfig"))  && !(currentUrl.contains("/login/login")) && !(currentUrl.contains("/login/open_web_login")) && !(currentUrl.contains("/login/testLogin"))
                && !(currentUrl.contains("/system/getMarketVersionMessage.json"))  && !(currentUrl.contains("/right_group/getManageUser.json"))
                ) {
            List<RightResourceUrlEntity> resourcesList = null;
            if (LoginUser.getUser() != null && LoginUser.getUser().getUserID() != null) {
                resourcesList = (List) request.getSession().getAttribute(LoginUser.getUser().getUserID() + "_" + LoginUser.getUser().getCompanyID() + UrlConstant.USER_LOGIN);
                if (CollectionUtils.isEmpty(resourcesList)) {
                    //根据用户Id获取当前登录用户的角色
                    if (LoginUser.getUser() != null) {
                        logger.info("interceptor:" + LoginUser.getUser().toString());
                    }
                    List<UserRightGroupEntity> roleList = userRightGroupService.findUserRightGroups(LoginUser.getUser().getCompanyID(), LoginUser.getUser().getUserID());
                    AssertUtil.isTrue(!CollectionUtils.isEmpty(roleList), "当前登录用户无任何角色,请联系系统管理员!");

                    Set<Integer> roleIdSet = new HashSet<>();
                    for (UserRightGroupEntity groupUser : roleList) {
                        roleIdSet.add(groupUser.getRightGroupId());
                    }
                    List<Integer> roleIdList = new ArrayList<>(roleIdSet);
                    resourcesList = rightResourceUrlService.findRightResourcesByGroupIds(roleIdList);
                    //设置session 有效期为1小时
                    request.getSession().setMaxInactiveInterval(7200);
                    request.getSession().setAttribute(LoginUser.getUser().getUserID() + "_" + LoginUser.getUser().getCompanyID() + UrlConstant.USER_LOGIN, resourcesList);
                }
            }
        }

        if(!(currentUrl.contains("/login/logOut")||currentUrl.contains("/login/getConfig")||currentUrl.contains("/login/login")||currentUrl.contains("/login/open_web_login")||currentUrl.contains("/login/testLogin")
                ||currentUrl.contains("/ding") || currentUrl.contains("/system") || currentUrl.contains("/sync")|| currentUrl.contains("/user_right_group/getUserResource.json")||currentUrl.contains("/suite")
                ||currentUrl.contains("/isvreceive")||currentUrl.contains("/dingcall"))) {
            AssertUtil.isTrue(LoginUser.getUser() != null, "登陆超时,请重新登录!");
        }else{
            return true;
        }


        boolean hasPermission = true;

        AssertUtil.isTrue(hasPermission, "您对当前操作没有权限，如有疑问请与管理员联系!");
        return hasPermission;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        long startTime = (Long) httpServletRequest.getAttribute("startTime");
        long endTime = System.currentTimeMillis();

        logger.info("[" + handler + "], Controller的方法执行完毕之后, DispatcherServlet进行视图的渲染之前 耗时: " + (endTime - startTime) + "ms");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public static void main(String[] args) {
//        List<Integer> s = new ArrayList<>();
////        s.add(1);
//        AssertUtil.isTrue(!CollectionUtils.isEmpty(s), "当前登录用户无任何角色,请联系系统管理员!");

//        boolean hasPermission = true;
//        AssertUtil.isTrue(hasPermission, "您对当前操作没有权限，如有疑问请与管理员联系!");
        List<UserRightGroupEntity> roleList = new ArrayList<>();
        roleList.add(new UserRightGroupEntity(1));
        AssertUtil.isTrue(!CollectionUtils.isEmpty(roleList), "当前登录用户无任何角色,请联系系统管理员!");
    }
}
