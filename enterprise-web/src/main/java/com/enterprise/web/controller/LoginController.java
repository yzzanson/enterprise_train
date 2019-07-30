package com.enterprise.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.IsvTicketsEntity;
import com.enterprise.base.entity.UserEntity;
import com.enterprise.base.entity.UserXCompany;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.UserSourceEnum;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.util.*;
import com.enterprise.util.dingtalk.AuthHelper;
import com.enterprise.util.dingtalk.DDConfig;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author anson
 * @create 2018-03-24 下午11:31
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IsvTicketsService isvTicketsService;

    @Resource
    private UserXCompanyService userXCompanyService;

    @Resource
    private UserService userService;

    @Resource
    private UserRightGroupService userRightGroupService;

    @Resource
    private RedisService redisService;

    /**
     * 退出
     *
     * @author shisan
     * @date 2017/12/26 上午11:32
     */
    @ResponseBody
    @RequestMapping("/logOut")
    public JSONObject logOut(HttpServletRequest request, HttpServletResponse response) {
        String cookeiName = getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME);
        if (!StringUtils.isEmpty(cookeiName)) {
            request.getSession().removeAttribute(cookeiName);
        }
        Cookie cookie = new Cookie(DDConstant.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setDomain(GlobalConstant.getDomain());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
//        }
        return ResultJson.succResultJson("SUCCESS");
    }

    /**
     * 首页(获取钉钉免登陆所需要的信息)
     *
     * @param corpid     企业corpid
     * @param currentUrl 当前url
     * @return
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getConfig(HttpServletRequest request, String corpid, String currentUrl) {
        AssertUtil.isTrue(!StringUtils.isEmpty(corpid), "企业corpid不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(currentUrl), "currentUrl不能为空!");
        logger.info(String.format("corpid = %s, currentUrl = %s", corpid, currentUrl));

        IsvTicketsEntity entity = isvTicketsService.getIsvTicketByCorpId(corpid);
        AssertUtil.notNull(entity, "请先安装微应用!");

        String configValue = AuthHelper.getConfig(request, corpid, entity.getCorpAgentId(), entity.getCorpTicket(), currentUrl);
        Map<String, Object> map = Maps.newHashMap();
        map.put("configValue", configValue);
        map.put("tickets", entity);

        return ResultJson.succResultJson(map);
    }

    /**web新打开页面免登*/
    @RequestMapping(value = "/open_web_login")
    @ResponseBody
    public JSONObject open_web_login(String corpId,Integer userId,HttpServletRequest request,HttpServletResponse response) {
        AssertUtil.isTrue(corpId!=null,"请输入corpId");
        AssertUtil.isTrue(userId != null, "请输入userId");
        UserEntity userEntity = userService.getUserById(userId);
        UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(corpId, userId, StatusEnum.OK.getValue()));
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        AssertUtil.isTrue(companyId != null, "companyId为空");
        LoginUser loginUser = new LoginUser();
        loginUser.setCorpID(corpId);
        loginUser.setUserID(userId);
        loginUser.setAvatar(userEntity.getAvatar());
        loginUser.setDingName(userEntity.getName());
        loginUser.setDingID(userXCompany.getDingUserId());
        loginUser.setCompanyID(companyId);
        Integer role = userRightGroupService.getUserGroupByCompanyIdAndUserId(companyId,userId);
        loginUser.setRole(role);
        setSession(loginUser, request, response);
        return ResultJson.succResultJson("loginUser", loginUser);
    }



    /**web新打开页面免登*/
    @RequestMapping(value = "/testLogin")
    @ResponseBody
    public JSONObject testLogin(String corpId,Integer userId,HttpServletRequest request,HttpServletResponse response) {
        AssertUtil.isTrue(corpId!=null,"请输入corpId");
        AssertUtil.isTrue(userId != null, "请输入userId");
        UserEntity userEntity = userService.getUserById(userId);
        UserXCompany userXCompany = userXCompanyService.getUserXCompany(new UserXCompany(corpId, userId, StatusEnum.OK.getValue()));
        IsvTicketsEntity isvTicketsEntity = isvTicketsService.getIsvTicketByCorpId(corpId);
        Integer companyId = isvTicketsEntity.getCompanyId();
        AssertUtil.isTrue(companyId != null, "companyId为空");
        LoginUser loginUser = new LoginUser();
        loginUser.setCorpID(corpId);
        loginUser.setUserID(userId);
        loginUser.setAvatar(userEntity.getAvatar());
        loginUser.setDingName(userEntity.getName());
        loginUser.setDingID(userXCompany.getDingUserId());
        loginUser.setCompanyID(isvTicketsEntity.getCompanyId());
        Integer role = userRightGroupService.getUserGroupByCompanyIdAndUserId(companyId,userId);
        loginUser.setRole(role);
        setSession(loginUser, request, response);
        return ResultJson.succResultJson("loginUser", loginUser);
    }

    /**
     * 免登陆
     * 放入session cookie
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/login")
    @ResponseBody
    public JSONObject login(HttpServletRequest request, HttpServletResponse response) {
        // code 只能有效调用一次
        AssertUtil.isTrue(!StringUtils.isEmpty(request.getParameter(DDConstant.CODE)), "code不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(request.getParameter(DDConstant.CORP_ID)), "corpId不能为空!");

        logger.info("login >>>>>>> CORP_ID = " + request.getParameter(DDConstant.CORP_ID) + ", companyId = " + request.getParameter("companyId") + ", CODE = " + request.getParameter(DDConstant.CODE));

        IsvTicketsEntity isvTickets = isvTicketsService.getIsvTicketByCorpId(request.getParameter(DDConstant.CORP_ID));
        AssertUtil.notNull(isvTickets, "该企业尚未接入应用,请先扫码授权接入!");

        String accessToken = isvTickets.getCorpAccessToken();
        //获取用户在钉钉中的 userID
        JSONObject object = new JSONObject();
        object.put(DDConstant.ACCESS_TOKEN, accessToken);
        object.put(DDConstant.CODE, request.getParameter(DDConstant.CODE));
        String url = JSONUtil.appendJsonParamsToUrl(DDConfig.GET_USER_INFO_URL, object);
        JSONObject result = HttpUtil.get(url);
        AssertUtil.isTrue(result != null && result.get(DDConstant.USER_ID) != null, "获取用户信息失败!");
        //员工在企业内的UserID
        String dingUserId = result.get("userid").toString();

        //根据corpId 和 dingUserId
        UserXCompany userXCompanyInDB = userXCompanyService.createOrUpdateUserXCompany(new UserXCompany(request.getParameter(DDConstant.CORP_ID), dingUserId, UserSourceEnum.BACKSTAGE.getValue(), StatusEnum.OK.getValue()));

        Integer companyId = isvTickets.getCompanyId();
        AssertUtil.isTrue(companyId != null, "companyId为空");

        //根据userId获取用户信息
        UserEntity userInDB = userService.getUserById(userXCompanyInDB.getUserId());
        AssertUtil.notNull(userInDB, "登录失败!");
        LoginUser loginUser = new LoginUser();
        loginUser.setCorpID(userXCompanyInDB.getCorpId());
        loginUser.setUserID(userInDB.getId());
        loginUser.setAvatar(userInDB.getAvatar());
        loginUser.setDingName(userInDB.getName());
        loginUser.setDingID(userInDB.getDingId());
        loginUser.setCompanyID(companyId);
        Integer role = userRightGroupService.getUserGroupByCompanyIdAndUserId(companyId,userInDB.getId());
        loginUser.setRole(role);
        LoginUser.setUser(loginUser);
        logger.info("sessionStr:" + loginUser.toString());
        //设置session
        setSession(loginUser, request, response);

        return ResultJson.succResultJson("loginUser", loginUser);
    }

    /**
     * 设置session
     *
     * @param loginUser 用户登录信息
     * @author shisan
     * @date 2017/11/20 上午10:23
     */
    private void setSession(LoginUser loginUser, HttpServletRequest request, HttpServletResponse response) {
        try {
            String sessionKey = AES.Encrypt(loginUser.getUserID()+"" , DDConstant.AES_KEY);
            //实例化一个cookie, name=cookie_name_userId  value= 加密后的userId
//            Cookie cookie = new Cookie(DDConstant.COOKIE_NAME, sessionKey);
//            cookie.setPath("/");
//            cookie.setDomain(GlobalConstant.getDomain());
//            response.addCookie(cookie);
            request.getSession().setAttribute(sessionKey, loginUser);
            CookieUtil.setCookie(request, response, DDConstant.COOKIE_NAME, sessionKey, 24 * 60 * 60);
            CookieUtil.setCookie(request, response, DDConstant.USER_WEB, loginUser.getUserCookieStr(loginUser), 24 * 60 * 60);

//            Object json = JSONObject.toJSON(loginUser);
//            String redisKey = RedisConstant.USER_SESSION_KEY + "_" + sessionKey;
//            redisService.hset(redisKey, sessionKey, json.toString());
//            logger.info(sessionKey + ":" + request.getSession().getAttribute(sessionKey));


//            HttpServletRequest request1 = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String cookeiName = getCookieByName(request1.getCookies(), DDConstant.COOKIE_NAME);
//            System.out.println("cookeiName:"+cookeiName);
        } catch (Exception e) {
            logger.error("[免登陆]设置session 失败!", e);
        }
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

    public static void main(String[] args) {
        Integer role =null;
        LoginUser loginUser = new LoginUser();
        loginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
        loginUser.setUserID(2);
        loginUser.setAvatar("http://sss/a.jpg");
        loginUser.setDingName("牛逼哥");
        loginUser.setDingID("niubility");
        loginUser.setCompanyID(1);
        loginUser.setRole(role);
        System.out.println(ResultJson.succResultJson(loginUser));
    }

}
