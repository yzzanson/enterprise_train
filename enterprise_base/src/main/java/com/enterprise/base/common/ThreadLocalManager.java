package com.enterprise.base.common;

import com.enterprise.base.exceptions.LoginException;

/**
 * 登录用户信息本地线程管理器
 *
 * @author shisan
 * @create 2017-09-13 下午4:36
 **/
public class ThreadLocalManager {

    /**
     * 客户端 登录用户信息本地线程
     */
    private static final ThreadLocal<MobileLoginUser> mobileLoginUserThreadLocal = new ThreadLocal<MobileLoginUser>();

    /**
     * 电脑端 登陆用户信息本地线程
     * */
    private static final ThreadLocal<LoginUser> pcLoginUserThreadLocal = new ThreadLocal<LoginUser>();

    public static MobileLoginUser getMobileLoginUser() {
        if (mobileLoginUserThreadLocal.get() == null) {
            throw new LoginException("请登录!");
        }
        return mobileLoginUserThreadLocal.get();
    }

    public static void setMobileLoginUser(MobileLoginUser mobileLoginUser) {
        if (mobileLoginUserThreadLocal.get() != null) {
            mobileLoginUserThreadLocal.remove();
        }
        mobileLoginUserThreadLocal.set(mobileLoginUser);
    }

    public static void clearMobileLoginUser() {
        mobileLoginUserThreadLocal.remove();
    }



    public static LoginUser getPCLoginUser() {
        if (pcLoginUserThreadLocal.get() == null) {
            //throw new LoginException("请登录!");
            return null;
        }
        return pcLoginUserThreadLocal.get();
    }

    public static void setPCLoginUser(LoginUser loginUser) {
        if (pcLoginUserThreadLocal.get() != null) {
            pcLoginUserThreadLocal.remove();
        }
        pcLoginUserThreadLocal.set(loginUser);
    }

    public static void clearPCLoginUser() {
        pcLoginUserThreadLocal.remove();
    }

}
