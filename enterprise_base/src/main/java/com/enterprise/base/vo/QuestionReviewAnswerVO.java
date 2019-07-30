package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/3 下午7:43
 */
public class QuestionReviewAnswerVO {

    private Integer questionId;

    private Integer questionType;

    private String answer;

    private String rightAnswer;

    private Integer answerStatus;

    private String answerDesc;

    private String message;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public Integer getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(Integer answerStatus) {
        this.answerStatus = answerStatus;
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

    public QuestionReviewAnswerVO() {
    }

    public QuestionReviewAnswerVO(Integer questionId, Integer questionType, String answer, String rightAnswer, Integer answerStatus, String answerDesc, String message) {
        this.questionId = questionId;
        this.questionType = questionType;
        this.answer = answer;
        this.rightAnswer = rightAnswer;
        this.answerStatus = answerStatus;
        this.answerDesc = answerDesc;
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
