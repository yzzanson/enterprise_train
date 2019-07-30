package com.enterprise.base.enums;

/**
 * 是否发送oa消息枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum SendOAEnum {

    /**
     * 不发送
     */
    NOT_SEND(0, "not_send"),

    /**
     * 发送
     */
    SEND(1, "send");

    private Integer value;

    private String desc;

    SendOAEnum(Integer value, String desc) {
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
