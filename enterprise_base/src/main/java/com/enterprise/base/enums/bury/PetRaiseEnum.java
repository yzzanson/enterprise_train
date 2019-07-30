package com.enterprise.base.enums.bury;

/**
 * 微应用使用状态枚举类
 *
 * @Author anson
 * @Date 2018/10/30 下午03:58
 */
public enum PetRaiseEnum {

    /**
     * 打扫
     */
    CLEAN(1, "clean","打扫"),

    /**
     * 投食
     */
    FEEDOTHER(2, "feed_other","投食"),

    /**
     * 喂食
     */
    FEEDSELF(3, "feed_self","喂食");

    private Integer value;

    private String desc;

    private String chiDesc;

    PetRaiseEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static PetRaiseEnum getAgentStatusEnum(Integer value) {
        for (PetRaiseEnum agentStatusEnum : PetRaiseEnum.values()) {
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
