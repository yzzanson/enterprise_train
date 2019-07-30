package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/24.
 */
public enum StatusEnum {

    /**
     * 失败
     */
    DELETE(0, "delete"),

    /**
     * 正常
     */
    OK(1, "ok"),

    HOLD_ON(2,"hold_on"),

    TOOL(3,"tool");

    private Integer value;

    private String desc;

    StatusEnum(Integer value, String desc) {
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
