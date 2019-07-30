package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 用户答题详情
 * @Author zezhouyang
 * @Date 18/4/8 下午5:17
 */
public class UserQuestionVO {

    private Integer questionId;

    private String description;

    private Integer myRight;//当前人正确

    private Integer oppRight;//对手正确

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

    public Integer getMyRight() {
        return myRight;
    }

    public void setMyRight(Integer myRight) {
        this.myRight = myRight;
    }

    public Integer getOppRight() {
        return oppRight;
    }

    public void setOppRight(Integer oppRight) {
        this.oppRight = oppRight;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
