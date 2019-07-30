package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * @Description 数据总览 蓝色字下面一行统计
 * @Author zezhouyang
 * @Date 18/4/14 下午8:30
 */
public class AllSummaryDetailVO {

    //企业总数
    private Integer companyCount;

    //架构总人数
    private Integer userCount;

    //有效用户数
    private Integer validateUserCount;

    //激活率,1位小数
    private BigDecimal activeRate;

    //答题总数
    private Integer answerCount;

    //人均答题
    private BigDecimal averageAnswerCount;

    public Integer getCompanyCount() {
        return companyCount;
    }

    public void setCompanyCount(Integer companyCount) {
        this.companyCount = companyCount;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getValidateUserCount() {
        return validateUserCount;
    }

    public void setValidateUserCount(Integer validateUserCount) {
        this.validateUserCount = validateUserCount;
    }

    public BigDecimal getActiveRate() {
        return activeRate;
    }

    public void setActiveRate(BigDecimal activeRate) {
        this.activeRate = activeRate;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public BigDecimal getAverageAnswerCount() {
        return averageAnswerCount;
    }

    public void setAverageAnswerCount(BigDecimal averageAnswerCount) {
        this.averageAnswerCount = averageAnswerCount;
    }

    public AllSummaryDetailVO() {
    }

    public AllSummaryDetailVO(Integer companyCount, Integer userCount, Integer validateUserCount, BigDecimal activeRate, Integer answerCount, BigDecimal averageAnswerCount) {
        this.companyCount = companyCount;
        this.userCount = userCount;
        this.validateUserCount = validateUserCount;
        this.activeRate = activeRate;
        this.answerCount = answerCount;
        this.averageAnswerCount = averageAnswerCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
