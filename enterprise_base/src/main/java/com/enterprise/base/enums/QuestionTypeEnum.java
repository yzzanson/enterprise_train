package com.enterprise.base.enums;

/**
 * Created by anson on 18/3/27.
 */
public enum QuestionTypeEnum {


    /**
     * 单选题
     */
    SINGLE_CHOICE(1, "single_choice","单选题"),

    /**
     * 多选
     */
    MULTI_CHOICE(2, "multi_choice","多选题"),

    /**
     * 判断题
     */
    TRUE_FALSE(3, "true_false","判断题"),

    /**
     * 填空题
     */
    COMPLETION(4, "completion","填空题");

    private Integer type;

    private String desc;

    private String chineseDesc;

    QuestionTypeEnum(Integer type, String desc,String chineseDesc) {
        this.type = type;
        this.desc = desc;
        this.chineseDesc = chineseDesc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getChineseDesc() {
        return chineseDesc;
    }

    public void setChineseDesc(String chineseDesc) {
        this.chineseDesc = chineseDesc;
    }

    public static QuestionTypeEnum getQuestionTypeEnum(Integer value) {
        for (QuestionTypeEnum questionTypeEnum : QuestionTypeEnum.values()) {
            if (questionTypeEnum.getType().equals(value)) {
                return questionTypeEnum;
            }
        }

        return null;
    }

}
