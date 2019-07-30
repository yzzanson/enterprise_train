package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/6/6 下午6:52
 */
public class QuestionVO1 {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 所属题库Id
     */
    private Integer libraryId;

    /**题目创建人*/
    private Integer operator;

    /**
     * 题目类型
     */
    private Integer type;


    private String labelName;

    private Integer  labelId;

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目选项({"A":"","B":"",...})
     */
    private String options;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answer;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answerDesc;

    /**
     * 是否重新学习
     * */
    private Integer restudy;

    /**
     * 对应默认题库中的questionId,用于统计学习次数等
     * */
    private Integer parentId;

    private Integer blankIndex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Integer libraryId) {
        this.libraryId = libraryId;
    }

    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public Integer getRestudy() {
        return restudy;
    }

    public void setRestudy(Integer restudy) {
        this.restudy = restudy;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }


    public Integer getBlankIndex() {
        return blankIndex;
    }

    public void setBlankIndex(Integer blankIndex) {
        this.blankIndex = blankIndex;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
