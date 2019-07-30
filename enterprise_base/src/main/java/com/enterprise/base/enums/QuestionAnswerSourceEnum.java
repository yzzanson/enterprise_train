package com.enterprise.base.enums;

/**
 * 答题来源枚举类
 *
 * @Author shisan
 * @Date 2018/3/26 上午10:56
 */
public enum QuestionAnswerSourceEnum {

    /**
     * 卸载
     */
    AUTONOMOUS_ANSWER(0, "autonomous_answer","自主答题"),

    /**
     * 正常使用
     */
    ARENA_ANSWER(1, "arena_answer","擂台答题");

    private Integer value;

    private String desc;

    private String chiDesc;

    QuestionAnswerSourceEnum(Integer value, String desc, String chiDesc) {
        this.value = value;
        this.desc = desc;
        this.chiDesc = chiDesc;
    }

    public static QuestionAnswerSourceEnum getQuestionAnswerSourceEnum(Integer value) {
        for (QuestionAnswerSourceEnum questionAnswerSourceEnum : QuestionAnswerSourceEnum.values()) {
            if (questionAnswerSourceEnum.getValue().equals(value)) {
                return questionAnswerSourceEnum;
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
