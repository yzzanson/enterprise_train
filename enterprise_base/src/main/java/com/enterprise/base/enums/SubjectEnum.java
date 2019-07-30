package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/8 下午5:13
 */
public enum SubjectEnum {

    /**
     * 模板-既曾经的公共题库
     */
    MODEL(0, "题库模板"),

    /**
     * 企业题库
     */
    ENTERPRISE(1, "企业题库"),

    /**
     * 官方题库
     */
    PUBLIC(2,"官方题库");

    private Integer value;

    private String desc;

    SubjectEnum(Integer value, String desc) {
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
