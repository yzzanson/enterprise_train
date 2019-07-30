package com.enterprise.base.enums;

/**
 * ResultCodeEnum
 *
 * @author shisan
 * @create 2017-07-19 下午2:02
 **/
public enum ResultCodeEnum {
    /**
     * 失败
     */
    FAIL(1, "fail"),

    /**
     * 成功
     */
    SUCCESS(0, "success"),

    /**
     * 已被踢出
     */
    KICKED_OUT(2, " kicked_out"),

    /**
     * 暂无权限
     */
    NO_PERMISSION(3, "no_permission"),

    /**
     * 没有手机号
     */
    NO_MOBILE(1001, "no_mobile"),

    /**
     * 是否需要绑定手机号
     */
    NEED_BIND_MOBILE(1002, "need_bind_mobile"),

    /**
     * 未登录
     */
    NOT_LOGIN(1992, "not_login"),

    /**
     * 未激活应用
     */
    NOT_ACTIVE(2001, "not_active"),


    /**
     * 无响应
     */
    BUSY_RESPONSE(1003, "busy_response"),

    /**
     * 超过流量了
     */
    OVERLOAD(1004, "overload");

    private Integer value;

    private String desc;

    ResultCodeEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
