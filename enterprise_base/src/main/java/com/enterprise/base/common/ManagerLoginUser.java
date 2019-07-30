package com.enterprise.base.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * LoginUser
 *
 * @author shisan
 * @create 2017-09-13 下午4:37
 **/
public class ManagerLoginUser implements Serializable {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 登录者角色
     */
    private Integer role;

    public ManagerLoginUser() {
    }

    public ManagerLoginUser(Integer userId, String name, String mobile, Integer role) {
        this.userId = userId;
        this.name = name;
        this.mobile = mobile;
        this.role = role;
    }

    public static String parseLoginUserToCookieStr(ManagerLoginUser managerLoginUser){
        StringBuffer stringBuffer = new StringBuffer();
        Integer userId = managerLoginUser.getUserId();
        String name = managerLoginUser.getName();
        String mobile = managerLoginUser.getMobile();
        Integer role= managerLoginUser.getRole();
        if(userId!=null){stringBuffer.append(userId.toString());} else stringBuffer.append("");
        if(StringUtils.isNotEmpty(name)) stringBuffer.append("|").append(name); else stringBuffer.append("|");
        if(StringUtils.isNotEmpty(mobile)) stringBuffer.append("|").append(mobile); else stringBuffer.append("|");
        if(role>0) stringBuffer.append("|").append(role); else stringBuffer.append("|");
        return stringBuffer.toString();
    }

    public static ManagerLoginUser parseCookieStringToManagerLoginUser(String cookieStr){
        if(StringUtils.isNotEmpty(cookieStr)) {
            ManagerLoginUser managerLoginUser = new ManagerLoginUser();
            String[] loginUserStr = cookieStr.split("\\|");
            if (StringUtils.isNotEmpty(loginUserStr[0])) managerLoginUser.setUserId(Integer.parseInt(loginUserStr[0]));
            if (StringUtils.isNotEmpty(loginUserStr[1])) managerLoginUser.setName(loginUserStr[1]);
            if (StringUtils.isNotEmpty(loginUserStr[2])) managerLoginUser.setMobile(loginUserStr[2]);
            if (StringUtils.isNotEmpty(loginUserStr[3])) managerLoginUser.setRole(Integer.parseInt(loginUserStr[3]));
            return managerLoginUser;
        }
        return null;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
