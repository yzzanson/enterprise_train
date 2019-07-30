package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/28.
 */
public enum QuestionFeedbackEnum {

    /**
     * 卸载
     */
    DELETED(0, "deleted"),

    /**
     * 正常使用
     */
    NORMAL(1, "normal"),

    /**
     * 停用
     */
    ISREAD(3, "is_read"),

    /**
     * 正常使用
     */
    HANDLERED(4, "normal"),
    ;

    private Integer value;

    private String desc;

    QuestionFeedbackEnum(Integer value, String desc) {
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
