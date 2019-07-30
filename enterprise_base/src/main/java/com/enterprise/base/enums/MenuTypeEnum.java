package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/29 上午10:21
 */
public enum MenuTypeEnum {

    /**
     * 卸载
     */
    MENU(1, "menu","菜单"),

    /**
     * 卸载
     */
    FUNC(2, "func","功能");

    private Integer value;

    private String desc;

    private String chiDesc;

    MenuTypeEnum(Integer value, String desc,String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static MenuTypeEnum getAgentStatusEnum(Integer value) {
        for (MenuTypeEnum agentStatusEnum : MenuTypeEnum.values()) {
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
