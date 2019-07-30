package com.enterprise.base.enums;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/13 下午2:29
 */
public enum UserSourceEnum {

    /**
     * 内训
     */
    NEIXUN(0, "neixun"),

    /**
     * 后台
     */
    BACKSTAGE(1, "backstage");

    private Integer value;

    private String desc;

    UserSourceEnum(Integer value, String desc) {
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
