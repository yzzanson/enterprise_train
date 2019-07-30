package com.enterprise.util.oa.enums;

/**
 * 消息类型
 *
 * @author shisan
 * @create 2017-07-19 下午2:02
 **/
public enum MsgTypeEnum {
    /**
     * text消息
     */
    TEXT("text", "text消息"),

    /**
     * oa消息
     */
    OA("oa", "oa消息"),

    /**
     * oa消息
     */
    LINK("link", "oa消息")
    ,

    /**
     * markDown
     */
    ACTIONCARD("action_card", "oa消息");

    private String value;

    private String desc;

    MsgTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
