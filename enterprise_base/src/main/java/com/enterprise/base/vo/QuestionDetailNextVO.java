package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 问题详情
 * Created by anson on 18/3/29.
 */
public class QuestionDetailNextVO {

    private Integer id;

    private Integer type;

    private String description;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private String answer;

    private String answerDesc;

    private Integer restudy;

    private String label;

    private String labelId;

    private Integer totalCount;

    private Integer answeredCount;

    private Integer isDecay;

    private Integer sortType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getAnsweredCount() {
        return answeredCount;
    }

    public void setAnsweredCount(Integer answeredCount) {
        this.answeredCount = answeredCount;
    }

    public Integer getIsDecay() {
        return isDecay;
    }

    public void setIsDecay(Integer isDecay) {
        this.isDecay = isDecay;
    }

    public Integer getSortType() {
        return sortType;
    }

    public void setSortType(Integer sortType) {
        this.sortType = sortType;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
