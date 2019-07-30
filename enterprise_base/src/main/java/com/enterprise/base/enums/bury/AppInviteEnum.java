package com.enterprise.base.enums.bury;

/**
 * 微应用使用状态枚举类
 *
 * @Author anson
 * @Date 2018/10/30 下午03:58
 */
public enum AppInviteEnum {

    /**
     * 分享
     */
    SHARE(1, "share","分享"),

    /**
     * 邀请
     */
    INVITE(2, "invite","邀请");

    private Integer value;

    private String desc;

    private String chiDesc;

    AppInviteEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static AppInviteEnum getAgentStatusEnum(Integer value) {
        for (AppInviteEnum agentStatusEnum : AppInviteEnum.values()) {
            if (agentStatusEnum.getValue().equals(value)) {
                return agentStatusEnum;
            }
        }

        return null;
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

    public String getChiDesc() {
        return chiDesc;
    }

    public void setChiDesc(String chiDesc) {
        this.chiDesc = chiDesc;
    }
}
