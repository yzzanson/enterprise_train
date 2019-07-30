package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/27.
 */
public enum QuestionAnswerEnum {


    /**
     * 未答题
     */
    NO_ANSWER(0, "no_answer"),

    /**
     * 正确
     */
    RIGHT(1, "right"),

    /**
     * 错误
     */
    WRONG(2, "wrong");

    private Integer value;

    private String desc;

    QuestionAnswerEnum(Integer value, String desc) {
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
