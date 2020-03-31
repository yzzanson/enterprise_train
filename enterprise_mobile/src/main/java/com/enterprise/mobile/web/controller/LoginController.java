package com.enterprise.mobile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.enterprise.base.common.*;
import com.enterprise.base.entity.*;
import com.enterprise.base.enums.StatusEnum;
import com.enterprise.base.enums.UserSourceEnum;
import com.enterprise.mobile.web.session.SessionFilter;
import com.enterprise.mobile.web.util.CSRFTokenUtil;
import com.enterprise.service.company.CompanyInfoService;
import com.enterprise.service.isvTickets.IsvTicketsService;
import com.enterprise.service.redis.RedisService;
import com.enterprise.service.report.UserXOpenService;
import com.enterprise.service.right.UserRightGroupService;
import com.enterprise.service.user.UserService;
import com.enterprise.service.user.UserXCompanyService;
import com.enterprise.thread.ModifyUserLibraryThread;
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

//import com.enterprise.util.jwt.JwtUtil;

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
    private UserXOpenService userXOpenService;

    @Resource
    private CompanyInfoService companyInfoService;

    @Resource
    private RedisService redisService;

    @Resource
    private UserRightGroupService userRightGroupService;

    @Resource
    private CSRFTokenUtil csrfTokenUtil;

    private String APPID = GlobalConstant.getAppId();


    /**
     * 退出
     *
     * @author shisan
     * @date 2017/12/26 上午11:32
     */
    @ResponseBody
    @RequestMapping("/logOut")
    public JSONObject logOut(HttpServletRequest request, HttpServletResponse response) {
        if (MobileLoginUser.hasLogin()) {
            String sessionKey = AES.Encrypt(MobileLoginUser.getUser().getUserID() + "", DDConstant.AES_KEY);
            request.getSession().removeAttribute(sessionKey);

            Cookie cookie = new Cookie(DDConstant.COOKIE_NAME_MOBILE, "");
            cookie.setPath("/");
            cookie.setDomain(GlobalConstant.getDomain());
//            cookie.setDomain(DDConstant.DOMAIN);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
//            ThreadLocalManager.clearMobileLoginUser();
        }

        return ResultJson.succResultJson("SUCCESS");
    }

    /**
     * 首页(获取钉钉免登陆所需要的信息)
     *corp
     * @param corpid     企业corpid
     * @param currentUrl 当前url
     * @return
     */
    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getConfig(HttpServletRequest request, String corpid, String currentUrl) {
        AssertUtil.isTrue(!StringUtils.isEmpty(corpid), "企业corpid不能为空!");
        AssertUtil.isTrue(!StringUtils.isEmpty(currentUrl), "currentUrl不能为空!");

        IsvTicketsEntity entity = isvTicketsService.getIsvTicketByCorpId(corpid);
        AssertUtil.notNull(entity, "请先安装微应用!");

        String configValue = AuthHelper.getConfig(request, corpid, entity.getCorpAgentId(), entity.getCorpTicket(), currentUrl);
        Map<String, Object> map = Maps.newHashMap();
        map.put("configValue", configValue);
        map.put("tickets", entity);

        return ResultJson.succResultJson(map);
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
        try {
            AssertUtil.isTrue(!StringUtils.isEmpty(request.getParameter(DDConstant.CODE)), "code不能为空!");
            AssertUtil.isTrue(!StringUtils.isEmpty(request.getParameter(DDConstant.CORP_ID)), "corpId不能为空!");

            logger.info("login >>>>>>> CORP_ID = " + request.getParameter(DDConstant.CORP_ID) + ", CODE = " + request.getParameter(DDConstant.CODE));

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
            UserXCompany userXCompanyInDB = userXCompanyService.createOrUpdateUserXCompany(new UserXCompany(request.getParameter(DDConstant.CORP_ID), dingUserId, UserSourceEnum.NEIXUN.getValue(), null));
            //根据userId获取用户信息
            UserEntity userInDB = userService.getUserById(userXCompanyInDB.getUserId());
            AssertUtil.notNull(userInDB, "登录失败!");

            CompanyInfoEntity companyInfoEntity = companyInfoService.getCompanyById(isvTickets.getCompanyId());
            String companyName = companyInfoEntity.getName();
            Integer companyId = companyInfoEntity.getId();

            Integer isSuperManage = userRightGroupService.checkIsSuperManage(companyId,userInDB.getId());
            MobileLoginUser loginUser = new MobileLoginUser();
            loginUser.setCorpID(userXCompanyInDB.getCorpId());
            loginUser.setUserID(userInDB.getId());
            loginUser.setAvatar(userInDB.getAvatar());
            loginUser.setDingName(userInDB.getName());
            loginUser.setDingID(userInDB.getDingId());
            loginUser.setCompanyID(isvTickets.getCompanyId());
            loginUser.setCompanyName(companyName);
            loginUser.setAppId(APPID);
            loginUser.setIsManager(isSuperManage);


            logger.info("sessionStr:" + loginUser.toString());
            //设置session
            setSession(loginUser, request, response);

//        String token = csrfTokenUtil.generate();
//        loginUser.setToken(token);

//        response.setHeader("token", token);
            //记录用户打开次数
            loginStorage(userXCompanyInDB.getUserId(), isvTickets.getCompanyId());
            new Thread(new ModifyUserLibraryThread(isvTickets.getCompanyId(),userXCompanyInDB.getUserId())).start();
            return ResultJson.succResultJson("loginUser", loginUser);
        }catch (Exception e){
            return ResultJson.errorResultJson(e.getMessage());
        }
    }

    private void loginStorage(Integer userId,Integer companyId){
        UserXOpenEntity userXOpenEntity = new UserXOpenEntity(userId,companyId, StatusEnum.OK.getValue());
        userXOpenService.createOrUpdateUserXOpen(userXOpenEntity);
    }

//  	private String genToken(Integer userId){
//        String token = jwtUtil.generToken(userId.toString(),null,null);
//        return token;
//    }

    /**
     * 免登陆
     * 放入session cookie
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/testLogin")
    @ResponseBody
    public JSONObject testLogin(HttpServletRequest request,HttpServletResponse response) {
        IsvTicketsEntity isvTickets = isvTicketsService.getIsvTicketByCorpId(request.getParameter(DDConstant.CORP_ID));
        AssertUtil.notNull(isvTickets, "该企业尚未接入应用,请先扫码授权接入!");

        //员工在企业内的UserID
        String dingUserId = request.getParameter("userid").toString();
        //根据corpId 和 dingUserId
//        UserXCompany userXCompanyInDB = userXCompanyService.createOrUpdateUserXCompany(new UserXCompany(request.getParameter(DDConstant.CORP_ID), dingUserId, UserSourceEnum.NEIXUN.getValue(),null));
        //从库读,不做从钉钉api查询
        UserXCompany userXCompanyInDB = userXCompanyService.getUserXCompany(new UserXCompany(request.getParameter(DDConstant.CORP_ID), dingUserId, UserSourceEnum.NEIXUN.getValue(), null));
        //根据userId获取用户信息
        UserEntity userInDB = userService.getUserById(userXCompanyInDB.getUserId());
        AssertUtil.notNull(userInDB, "登录失败!");

        MobileLoginUser loginUser = new MobileLoginUser();
        loginUser.setCorpID(userXCompanyInDB.getCorpId());
        loginUser.setUserID(userInDB.getId());
        loginUser.setAvatar(userInDB.getAvatar());
        loginUser.setDingName(userInDB.getName());
        loginUser.setDingID(userInDB.getDingId());
        loginUser.setCompanyID(isvTickets.getCompanyId());
        loginUser.setAppId(APPID);

        //设置session
        setSession(loginUser, request, response);

        //设置session
        setSession(loginUser, request, response);
        //记录用户打开次数
        loginStorage(userXCompanyInDB.getUserId(),isvTickets.getCompanyId());

        return ResultJson.succResultJson("loginUser", loginUser);
    }

    @RequestMapping(value = "/getFromRedis")
    @ResponseBody
    public JSONObject getFromRedis(HttpServletRequest request) {
        String sid = CookieUtil.getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
        String userSessionString = (String) redisService.hget(RedisConstant.USER_SESSION_KEY + "_" + sid, sid);
        MobileLoginUser loginUser = (MobileLoginUser) JSONObject.parseObject(userSessionString, MobileLoginUser.class);
        return ResultJson.succResultJson(loginUser);
    }

    /**
     * 免登陆
     * 放入session cookie
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFromCookie")
    @ResponseBody
    public JSONObject getFromCookie(HttpServletRequest request) {
        String cookieUserStr = MobileLoginUser.getCookieValue(request, DDConstant.USER_MOBILE);
        MobileLoginUser mobileLoginUser = MobileLoginUser.getUserFromCookieStr(cookieUserStr);
        //设置session
        return ResultJson.succResultJson(mobileLoginUser);
    }


    /**
     * 设置session
     *
     * @param loginUser 用户登录信息
     * @author shisan
     * @date 2017/11/20 上午10:23
     */
    private void setSession(MobileLoginUser loginUser, HttpServletRequest request,HttpServletResponse response) {
        try {
            String sid = CookieUtil.getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
            logger.info("cookie_sid值初始化:" + sid);
            if(org.apache.commons.lang3.StringUtils.isEmpty(sid) || sid.length()==0){
                sid = SessionFilter.getUuid();
                CookieUtil.setCookie(request, response, DDConstant.COOKIE_NAME_MOBILE, sid, 2*60*60);
            }
            request.getSession().setAttribute(sid, loginUser);
            CookieUtil.setCookie(request, response, DDConstant.USER_MOBILE, MobileLoginUser.getUserCookieStr(loginUser), 24 * 60 * 60);
//            CookieUtil.setCookie(request, response, DDConstant.USER_REQUEST_TOKEN, token, 20 * 60);
            logger.warn("sid:" + sid + "用户:" + loginUser.toString());
//            ThreadLocalManager.setMobileLoginUser(loginUser);
        } catch (Exception e) {
            logger.error("[免登陆]设置session 失败!", e);
        }
    }

    public static void main(String[] args) {
//        MobileLoginUser loginUser = new MobileLoginUser();
//        loginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        loginUser.setUserID(49);
//        loginUser.setAvatar("https://static.dingtalk.com/media/lADOxqIK2s0C7s0C7g_750_750.jpg");
//        loginUser.setDingName("拾叁");
//        loginUser.setDingID("$:LWCP_v1:$5dJ5Ory3050wiDcuTZcHdw==");
//        loginUser.setCompanyID(1);
//        loginUser.setAppId("5372");
//        System.out.println(ResultJson.succResultJson(loginUser));
    }

}
