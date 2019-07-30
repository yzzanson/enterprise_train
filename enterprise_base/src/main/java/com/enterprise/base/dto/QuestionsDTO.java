package com.enterprise.base.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by anson on 18/3/27.
 */
public class QuestionsDTO implements Serializable {

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

    /**
     * 题目描述
     */
    private String description;

    /**
     * 题目选项({"A":"","B":"",...})
     */
    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answer;

    /**
     * 题目正确选项(多个用,隔开)
     */
    private String answerDesc;

    /**
     * 状态(0:删除，1:正常)
     */
    private Integer status = 1;

    private Integer restudy;

    private Integer labelId;

    //0顺序一样 1顺序不一样
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRestudy() {
        return restudy;
    }

    public void setRestudy(Integer restudy) {
        this.restudy = restudy;
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
