package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/12 下午2:05
 */
public enum CompanyEnum {

    /**
     * 公共企业
     */
    PUBLIC_COMPANY(1, "system"),

    /**
     * 其他
     */
    OTHER_COMPANY(2, "right");

    private Integer value;

    private String desc;

    CompanyEnum(Integer value, String desc) {
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
