package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/3 下午7:43
 */
public class QuestionAnswerVO {

    private Integer questionId;

    private Integer questionType;

    private String answer;

    private Integer answerStatus;

    private String message;

    private Integer isArenaFinished;

    private String answerDesc;

    private Integer totalCount;

    private Integer answeredCount;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(Integer answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public QuestionAnswerVO(Integer questionId, String answer, Integer answerStatus, String message) {
        this.questionId = questionId;
        this.answer = answer;
        this.answerStatus = answerStatus;
        this.message = message;
    }

    public QuestionAnswerVO(Integer questionId, String answer, Integer answerStatus, String message, String answerDesc) {
        this.questionId = questionId;
        this.answer = answer;
        this.answerStatus = answerStatus;
        this.message = message;
        this.answerDesc = answerDesc;
    }

    public QuestionAnswerVO(Integer questionId, Integer questionType, String answer, Integer answerStatus, String message, String answerDesc) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.answer = answer;
        this.answerStatus = answerStatus;
        this.message = message;
        this.answerDesc = answerDesc;
    }



    public QuestionAnswerVO(Integer questionId, Integer questionType, String answer, Integer answerStatus, String message, String answerDesc, Integer totalCount, Integer answeredCount) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.answer = answer;
        this.answerStatus = answerStatus;
        this.message = message;
        this.answerDesc = answerDesc;
        this.totalCount = totalCount;
        this.answeredCount = answeredCount;
    }

    public QuestionAnswerVO() {
    }

    public Integer getIsArenaFinished() {
        return isArenaFinished;
    }

    public void setIsArenaFinished(Integer isArenaFinished) {
        this.isArenaFinished = isArenaFinished;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
