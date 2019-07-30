package com.enterprise.base.vo.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by anson on 18/4/2.
 */
public class QuestionAnswerDto {

    private Integer questionId;

    private String answer;

    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
