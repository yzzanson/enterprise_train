package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/27.
 */
public enum QuestionLibraryTypeEnum {

    /**
     * 卸载
     */
    PUBLIC(0, "public"),

    /**
     * 正常使用
     */
    PRIVATE(1, "private");

    private Integer value;

    private String desc;

    QuestionLibraryTypeEnum(Integer value, String desc) {
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
