package com.enterprise.base.common;

import com.enterprise.base.convert.AES;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * LoginUser
 *
 * @author shisan
 * @create 2017-10-17 下午3:48
 **/
public class LoginUser {
    /**
     * 用户id
     */
    private Integer userID;

    /**
     * 企业Id
     */
    private Integer companyID;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 企业corpID
     */
    private String corpID;

    /**
     * dingID
     */
    private String dingID;

    /**
     * dingName
     */
    private String dingName;

    /**
     * 头像
     */
    private String avatar;

    public static Logger logger = LoggerFactory.getLogger(LoginUser.class);

    /************************************************
     * 以下为getter/setter方法
     * ************************************************
     * <p/>
     * 获取登录用户信息
     *
     * @author shisan
     * @date 2017/11/20 上午10:48
     */
    public static LoginUser getUser() {
        try {
//            // TODO 上线时需要放开以下注释代码
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String currentUrl = request.getRequestURI();
            logger.info("currentUrl:" + currentUrl);
            String cookeiName = getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME);
            LoginUser loginUser = (LoginUser) request.getSession().getAttribute(cookeiName);
            if (loginUser == null || (loginUser != null && loginUser.getUserID() == null)) {
                String cookieStr = getCookieByName(request.getCookies(), DDConstant.USER_WEB);
                if (StringUtils.isNotEmpty(cookieStr)) {
                    loginUser = getUserFromCookieStr(cookieStr);
                    return loginUser;
                }
                return null;
            } else {
                logger.info("getUserFromSession:loginUser:" + loginUser.toString());
            }


//            LoginUser loginUser = new LoginUser();
////            loginUser.setDingID("$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==");
//            loginUser.setDingID("$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==");
//            loginUser.setDingName("杨泽洲");
//            loginUser.setUserID(2);
//            loginUser.setCorpID("dingf115ac3395a05876");
//            loginUser.setCorpID("ding440a3cea1d4e70b135c2f4657eb6378f");
////        loginUser.setCompanyID(1);
//            loginUser.setCompanyID(11);
//            loginUser.setRole(3);

//        LoginUser loginUser = new LoginUser();
//        loginUser.setDingID("$:LWCP_v1:$o9/h/XindAoQkgtuXZVqEg==");
//        loginUser.setDingName("金瑶");
//        loginUser.setUserID(1);
//        loginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
////        loginUser.setCorpID("ding440a3cea1d4e70b135c2f4657eb6378f");
//        loginUser.setCompanyID(2);
//        loginUser.setRole(1);


//        LoginUser loginUser = new LoginUser();
//        loginUser.setDingID("$:LWCP_v1:$D1J6dlLhdt2w+bpetry2RQ==");
//        loginUser.setDingName("杨泽洲");
//        loginUser.setUserID(2);
//        loginUser.setCorpID("ding440a3cea1d4e70b135c2f4657eb6378f");
//        loginUser.setCompanyID(1);
//        loginUser.setRole(3);

            return loginUser;
        } catch (Exception e) {
            return null;
        }
    }

    public static void setUser(LoginUser loginUser) {
        try {
            // TODO 上线时需要放开以下注释代码
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String currentUrl = request.getRequestURI();
            String cookeiName = getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME);
            if (StringUtils.isNotEmpty(cookeiName)) {
                request.getSession().setAttribute(cookeiName, loginUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否登录
     *
     * @author shisan
     * @date 2017/12/26 下午2:16
     */
    public static boolean hasLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(getCookieByName(request.getCookies(), DDConstant.COOKIE_NAME));

        return loginUser == null ? false : true;
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

    /************************************************
     * 以下为getter/setter方法
     * ************************************************
     */
    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getCorpID() {
        return corpID;
    }

    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    public String getDingID() {
        return dingID;
    }

    public void setDingID(String dingID) {
        this.dingID = dingID;
    }

    public String getDingName() {
        return dingName;
    }

    public void setDingName(String dingName) {
        this.dingName = dingName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public static String getUserCookieStr(LoginUser loginUser) {
        String userId = "";
        String companyId = "";
        String corpId = "";
        String role = "";
        String avatar = "";
        String dingId = "";
        String dingName = "";
        if (loginUser.getUserID() != null) {
            userId = String.valueOf(loginUser.getUserID());
        }
        if (loginUser.getCompanyID() != null) {
            companyId = String.valueOf(loginUser.getCompanyID());
        }
        if (loginUser.getCorpID() != null) {
            corpId = String.valueOf(loginUser.getCorpID());
        }
        if (loginUser.getRole() != null) {
            role = String.valueOf(loginUser.getRole());
        }
        if (loginUser.getAvatar() != null) {
            avatar = String.valueOf(loginUser.getAvatar());
        }
        if (loginUser.getDingID() != null) {
            dingId = String.valueOf(loginUser.getDingID());
        }
        if (loginUser.getDingName() != null) {
            dingName = String.valueOf(loginUser.getDingName());
        }
        String userCookieString = userId + "|" + companyId + "|" + corpId
                + "|" + role + "|" + avatar + "|" + dingId + "|" + dingName;
        try {
//            String md5Str =  MD5Utils.encrypt(MD5Utils.base64encode(MD5Utils.base64encode(userCookieString)), MD5Utils.KEY);
            String aesStr = AES.Encrypt(userCookieString, AES.AESKEY);
            return aesStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将用户信息保存到cookie中
     */
    public static LoginUser getUserFromCookieStr(String userAesStr) {
        try {
            if (StringUtils.isNotEmpty(userAesStr)) {
//            String[] resultString = MD5Utils.base64Decode(MD5Utils.base64Decode(MD5Utils.decrypt(userMd5Str, MD5Utils.KEY))).split("\\|");
                String[] resultString = AES.Decrypt(userAesStr, AES.AESKEY).split("\\|");
                Integer userId = resultString[0] != null && StringUtils.isNotEmpty(resultString[0]) ? Integer.valueOf(resultString[0]) : null;
                Integer companyId = resultString[1] != null && StringUtils.isNotEmpty(resultString[1]) ? Integer.valueOf(resultString[1]) : null;
                String corpId = resultString[2] != null && StringUtils.isNotEmpty(resultString[2]) ? resultString[2] : null;
                Integer role = resultString[3] != null && StringUtils.isNotEmpty(resultString[3]) ? Integer.valueOf(resultString[3]) : null;
                String avatar = resultString[4] != null && StringUtils.isNotEmpty(resultString[4]) ? resultString[4] : null;
                String dingId = resultString[5] != null && StringUtils.isNotEmpty(resultString[5]) ? resultString[5] : null;
                String dingName = resultString[6] != null && StringUtils.isNotEmpty(resultString[6]) ? resultString[6] : null;
                LoginUser loginUser = new LoginUser();
                if (userId != null) {
                    loginUser.setUserID(userId);
                }
                if (companyId != null) {
                    loginUser.setCompanyID(companyId);
                }
                if (StringUtils.isNotEmpty(corpId) && !corpId.equals("null")) {
                    loginUser.setCorpID(corpId);
                }
                if (role != null) {
                    loginUser.setRole(role);
                }
                if (StringUtils.isNotEmpty(avatar) && !avatar.equals("null")) {
                    loginUser.setAvatar(avatar);
                }
                if (StringUtils.isNotEmpty(dingId) && !dingId.equals("null")) {
                    loginUser.setDingID(dingId);
                }
                if (StringUtils.isNotEmpty(dingName) && !dingName.equals("null")) {
                    loginUser.setDingName(dingName);
                }
                return loginUser;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    public static void main(String[] args) {
        String str = "wqB7D6O5p7iv7x5fj67Pcsbqt8vuNyr+ttJcaHFU8jzGtleW1CFeLfcXlv928JDMYZ/HKOaKBcWPZ51Aij1mQUcjS7QtrGV4I3zOgrV9mSGJPJy5bY43NF0PVUxLAam2X+2nGBT9U1gvnCjHNdGw5nWB0LMStN0rkszE9WtDxiQythdkgtODnu5Snq32h+zD";
        LoginUser loginUser = getUserFromCookieStr(str);
        System.out.println(loginUser.toString());
    }
}
