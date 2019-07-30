package com.enterprise.base.enums;

/**
 * 微应用使用状态枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum AgentStatusEnum {

    /**
     * 卸载
     */
    UNINSTALL(0, "uninstall","已卸载"),

    /**
     * 正常使用
     */
    USEING(1, "useing","正常使用"),

    /**
     * 停用
     */
    STOP_USE(2, "stop_use","已停用");

    private Integer value;

    private String desc;

    private String chiDesc;

    AgentStatusEnum(Integer value, String desc,String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static AgentStatusEnum getAgentStatusEnum(Integer value) {
        for (AgentStatusEnum agentStatusEnum : AgentStatusEnum.values()) {
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
