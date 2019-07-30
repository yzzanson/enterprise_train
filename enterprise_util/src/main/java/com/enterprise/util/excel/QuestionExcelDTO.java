package com.enterprise.util.excel;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/5/3 下午11:17
 */
public class QuestionExcelDTO {

    @ExcelVOAttribute(name = "题型", column = "A", isExport = true, prompt = "题型!")
    private String questionType;

    @ExcelVOAttribute(name = "题干", column = "B", isExport = true, prompt = "题干!")
    private String description;

    @ExcelVOAttribute(name = "标签", column = "C", isExport = true, prompt = "标签")
    private String label;

    @ExcelVOAttribute(name = "答案", column = "D", isExport = true, prompt = "答案")
    private String answer;

    @ExcelVOAttribute(name = "选项A", column = "E", isExport = true, prompt = "选项A")
    private String optionA;

    @ExcelVOAttribute(name = "选项B", column = "F", isExport = true, prompt = "选项B")
    private String optionB;

    @ExcelVOAttribute(name = "选项C", column = "G", isExport = true, prompt = "选项C")
    private String optionC;

    @ExcelVOAttribute(name = "选项D", column = "H", isExport = true, prompt = "选项D")
    private String optionD;

    @ExcelVOAttribute(name = "答案解析", column = "I", isExport = true, prompt = "答案解析")
    private String answerDesc;

    private Integer type;

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public QuestionExcelDTO() {
    }

    public QuestionExcelDTO(String questionType, String description, String label, String answer, String optionA, String optionB, String optionC, String optionD, String answerDesc) {
        this.questionType = questionType;
        this.description = description;
        this.label = label;
        this.answer = answer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answerDesc = answerDesc;
    }

    public QuestionExcelDTO(String description, String label, String answer, String optionA, String optionB, String optionC, String optionD, String answerDesc) {
        this.description = description;
        this.label = label;
        this.answer = answer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answerDesc = answerDesc;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
