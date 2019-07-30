package com.enterprise.base.enums.bury;

/**
 * 1版本更新推送，2邀请，3打扫，4投喂，5道具，6进入架构
 *
 * @Author anson
 * @Date 2018/10/30 下午03:56
 */
public enum OABuryEnum {

    /**
     * 新版本
     */
    NEW_VERSION(1, "new_version","新版本"),

    /**
     * 邀请
     */
    INVITE(2, "invite","邀请"),

    /**
     * 打扫
     */
    CLEAN(3, "clean","打扫"),

    /**
     * 投喂
     */
    FEED(4, "feed","投喂"),

    /**
     * 道具
     */
    TOOL(5, "tool","道具"),

    /**
     * 进入架构
     */
    ENTER(6, "enter","进入架构");

    private Integer value;

    private String desc;

    private String chiDesc;

    OABuryEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static OABuryEnum getAgentStatusEnum(Integer value) {
        for (OABuryEnum agentStatusEnum : OABuryEnum.values()) {
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
