package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/27.
 */
public enum QuestionAnswerTypeEnum {


    /**
     * 普通答题
     */
    LEARN(0, "learn"),

    /**
     * 擂台
     */
    ARENA(1, "arean");

    private Integer value;

    private String desc;

    QuestionAnswerTypeEnum(Integer value, String desc) {
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
