package com.enterprise.base.vo;

import java.util.List;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/1/15 下午3:51
 */
public class QuestionLibraryAccuracyVO {

    private Integer questionId;

    private String description;

    private Integer answerCount;

    private String accuracy;

    private Integer wrongCount;

    private List<UserVO3> wrongUserList;

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getWrongCount() {
        return wrongCount;
    }

    public void setWrongCount(Integer wrongCount) {
        this.wrongCount = wrongCount;
    }

    public List<UserVO3> getWrongUserList() {
        return wrongUserList;
    }

    public void setWrongUserList(List<UserVO3> wrongUserList) {
        this.wrongUserList = wrongUserList;
    }

    public QuestionLibraryAccuracyVO() {
    }

    public QuestionLibraryAccuracyVO(Integer questionId, String description, Integer answerCount, String accuracy, Integer wrongCount) {
        this.questionId = questionId;
        this.description = description;
        this.answerCount = answerCount;
        this.accuracy = accuracy;
        this.wrongCount = wrongCount;
    }
}
