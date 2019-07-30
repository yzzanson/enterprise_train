package com.enterprise.base.common;

import com.enterprise.base.convert.AES;
import com.enterprise.base.exceptions.LoginException;
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
public class MobileLoginUser {

    public static final String COMMUNITY_INVITE = "/community/saveInvite.json";
    /**
     * 用户id
     */
    private Integer userID;

    /**
     * 企业Id
     */
    private Integer companyID;

    private String companyName;

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

    private String appId;

    private String token;

    private Integer isManager;

    private static final Logger logger = LoggerFactory.getLogger(MobileLoginUser.class);

    public MobileLoginUser() {
    }

    public MobileLoginUser(Integer userID, Integer companyID, Integer role, String corpID, String dingID, String dingName, String avatar) {
        this.userID = userID;
        this.companyID = companyID;
        this.role = role;
        this.corpID = corpID;
        this.dingID = dingID;
        this.dingName = dingName;
        this.avatar = avatar;
    }

    /************************************************
     * 以下为getter/setter方法
     * ************************************************
     * <p/>
     * 获取登录用户信息
     *
     * @author shisan
     * @date 2017/11/20 上午10:48
     */
    public static MobileLoginUser getUser() {
        // TODO 上线时需要放开以下注释代码
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String sid = getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
        MobileLoginUser mobileLoginUser = (MobileLoginUser) request.getSession().getAttribute(sid);
        if (mobileLoginUser == null || mobileLoginUser.getUserID() == null) {
            String cookieUserStr = getCookieValue(request, DDConstant.USER_MOBILE);
            if (StringUtils.isEmpty(cookieUserStr)) {
                if(request.getRequestURI().contains(COMMUNITY_INVITE)){
                    return null;
                }
                throw new LoginException("请登录!");
            }
            mobileLoginUser = getUserFromCookieStr(cookieUserStr);
            if (mobileLoginUser == null) {
                throw new LoginException("请登录!");
            }
        }

//        MobileLoginUser mobileLoginUser = new MobileLoginUser();
//        mobileLoginUser.setDingID("$:LWCP_v1:$5dJ5Ory3050wiDcuTZcHdw==");
//        mobileLoginUser.setDingName("拾叁");
//        mobileLoginUser.setUserID(49);
////        mobileLoginUser.setUserID(30);
//        mobileLoginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        mobileLoginUser.setCompanyID(1);
//        mobileLoginUser.setRole(3);

//        mobileLoginUser.setDingID("$:LWCP_v1:$lULaZb0maU6z1siWYFl19Q==");
//        mobileLoginUser.setDingName("杨兴竹(文心兰)");
//        mobileLoginUser.setUserID(3);
//        mobileLoginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        mobileLoginUser.setCompanyID(1);
//        mobileLoginUser.setRole(2);

//        mobileLoginUser.setDingID("$:LWCP_v1:$MTn+5EMJ9N739NhsvZyELg==");
//        mobileLoginUser.setDingName("刘松");
//        mobileLoginUser.setUserID(46);
//        mobileLoginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        mobileLoginUser.setCompanyID(1);
//        mobileLoginUser.setRole(3);

//        mobileLoginUser.setDingID("$:LWCP_v1:$wQkfYhqJtJycSyW5XvM7bQ==");
//        mobileLoginUser.setDingName("无式");
//        mobileLoginUser.setUserID(5);
//        mobileLoginUser.setCorpID("ding440a3cea1d4e70b135c2f4657eb6378f");
//        mobileLoginUser.setCompanyID(3);
//        mobileLoginUser.setRole(3);

//        mobileLoginUser.setDingID("$:LWCP_v1:$I2B3Y2/854MCbALTO8JRWA==");//长老
//        mobileLoginUser.setDingName("长老");
//        mobileLoginUser.setUserID(34);//长老
//        mobileLoginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        mobileLoginUser.setCompanyID(1);
//        mobileLoginUser.setRole(2);

//云社区版本用
//        MobileLoginUser mobileLoginUser = new MobileLoginUser();
//        mobileLoginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        mobileLoginUser.setDingID("$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==");//蓝波
//        mobileLoginUser.setAvatar("https://static.dingtalk.com/media/lADPACOG8zv2Th3NAkTNAkQ_580_580.jpg");
//        mobileLoginUser.setDingName("蓝波");
//        mobileLoginUser.setUserID(2);//蓝波
//        mobileLoginUser.setCompanyID(11);
//        mobileLoginUser.setRole(2);

//        MobileLoginUser mobileLoginUser = new MobileLoginUser();
//        mobileLoginUser.setCorpID("ding614949de72a0b8a2");
//        mobileLoginUser.setDingID("$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==");//蓝波
//        mobileLoginUser.setAvatar("https://static.dingtalk.com/media/lADPACOG8zv2Th3NAkTNAkQ_580_580.jpg");
//        mobileLoginUser.setDingName("蓝波");
//        mobileLoginUser.setUserID(2);//蓝波
//        mobileLoginUser.setCompanyID(13);
//        mobileLoginUser.setRole(2);
//
//        logger.info("mobileLoginUser:"+mobileLoginUser.getUserID()+":"+mobileLoginUser.getCorpID()+":"+mobileLoginUser.getDingName());
        return mobileLoginUser;
    }

    /**
     * 将用户信息保存到cookie中
     */
    public static String getUserCookieStr(MobileLoginUser loginUser) {
        String userId = "";
        String companyId = "";
        String corpId = "";
        String role = "";
        String avatar = "";
        String dingId = "";
        String dingName = "";
        String isManage = "";
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
        if(loginUser.getIsManager()!=null && loginUser.getIsManager()>0){
            isManage = "1";
        }else{
            isManage = "0";
        }
        String userCookieString = userId + "|" + companyId + "|" + corpId
                + "|" + role + "|" + avatar + "|" + dingId + "|" + dingName + "|" + isManage;
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
    public static MobileLoginUser getUserFromCookieStr(String userAesStr) {
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
                String isManage = resultString[7] != null && StringUtils.isNotEmpty(resultString[7]) ? resultString[7] : null;
                MobileLoginUser mobileLoginUser = new MobileLoginUser();
                if (userId != null) {
                    mobileLoginUser.setUserID(userId);
                }
                if (companyId != null) {
                    mobileLoginUser.setCompanyID(companyId);
                }
                if (StringUtils.isNotEmpty(corpId) && !corpId.equals("null")) {
                    mobileLoginUser.setCorpID(corpId);
                }
                if (role != null) {
                    mobileLoginUser.setRole(role);
                }
                if (StringUtils.isNotEmpty(avatar) && !avatar.equals("null")) {
                    mobileLoginUser.setAvatar(avatar);
                }
                if (StringUtils.isNotEmpty(dingId) && !dingId.equals("null")) {
                    mobileLoginUser.setDingID(dingId);
                }
                if (StringUtils.isNotEmpty(dingName) && !dingName.equals("null")) {
                    mobileLoginUser.setDingName(dingName);
                }
                if (StringUtils.isNotEmpty(isManage) && !dingName.equals("null")) {
                    mobileLoginUser.setIsManager(1);
                }else{
                    mobileLoginUser.setIsManager(0);
                }
                return mobileLoginUser;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        /*
         sessionStr:com.enterprise.base.common.MobileLoginUser@20d0150d[
         userID=21,companyID=1,role=<null>,corpID=ding0d0be1f4df9f899a35c2f4657eb6378f,
         dingID=$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==,dingName=杨泽洲,
         avatar=https://static.dingtalk.com/media/lADOyYXMMM0E2s0E1w_1239_1242.jpg]
        * */
//        MobileLoginUser loginUser = new MobileLoginUser();
//        loginUser.setDingName("杨泽洲");
//        loginUser.setDingID("$:LWCP_v1:$VBnCXRdtIs/I0rSwC4JAdw==");
//        loginUser.setAvatar("https://static.dingtalk.com/media/lADOyYXMMM0E2s0E1w_1239_1242.jpg");
//        loginUser.setCompanyID(1);
//        loginUser.setCorpID("ding0d0be1f4df9f899a35c2f4657eb6378f");
//        loginUser.setRole(null);
//        loginUser.setUserID(21);
//        System.out.println(loginUser.toString());
//        String aesStr = getUserCookieStr(loginUser);
//        System.out.println(aesStr);
//        MobileLoginUser mobileLoginUser = getUserFromCookieStr(aesStr);
//        System.out.println(mobileLoginUser.toString());
        String cookieStr = ":UlJ0UHN9apOQqLBR7ATm22gjEblZAP+kHUrqU7HqApwMevmE85dj3iW3KPs0sEfT5+IPAOFRy8ErhYhvwl6WM3HG8xwcxOuA9bJDaNdnLxyr63AYuQ1vtwk/I/Y5ZuIH+fgzJEFJvbPhAvsXzN7y0fk+Q3lNIzhlPMLQXpY30nRCArdLYItOll0h+GYJtokC3289I3UaOKAJGUZUEfNZ0g==";
        MobileLoginUser mb = getUserFromCookieStr(cookieStr);
        System.out.println(mb.toString());
    }


    /**
     * 是否登录
     *
     * @author shisan
     * @date 2017/12/26 下午2:16
     */
    public static boolean hasLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String sid = getCookieValue(request, DDConstant.COOKIE_NAME_MOBILE);
        MobileLoginUser loginUser = (MobileLoginUser) request.getSession().getAttribute(sid);

        return loginUser == null ? false : true;
    }

//    private static String getCookieByName(Cookie[] cookies, String name) {
//        if (cookies == null || cookies.length <= 0) {
//            return null;
//        }
//        for (Cookie obj : cookies) {
//            if (obj.getName().equals(name)) {
//                return obj.getValue();
//            }
//        }
//        return null;
//    }

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
