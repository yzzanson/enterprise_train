package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/23.
 */
public enum RoleEnum {

    /**
     * 运营
     * */
    MANAGE(1, "超级管理员"),
    /**
     * 无需登录
     * */
    NOLOGIN(3, "无需登录")
    ;

    private Integer value;

    private String desc;

    RoleEnum(Integer value, String desc) {
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
