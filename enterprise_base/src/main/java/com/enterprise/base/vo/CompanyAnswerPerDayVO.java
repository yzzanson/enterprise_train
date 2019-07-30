package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * @Description 公司每日答题数,做统计用
 * @Author zezhouyang
 * @Date 18/4/14 下午8:40
 */
public class CompanyAnswerPerDayVO {

    private String date;

    private String dayAnswerCount;

    private BigDecimal userPerAnswerCount;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayAnswerCount() {
        return dayAnswerCount;
    }

    public void setDayAnswerCount(String dayAnswerCount) {
        this.dayAnswerCount = dayAnswerCount;
    }

    public BigDecimal getUserPerAnswerCount() {
        return userPerAnswerCount;
    }

    public void setUserPerAnswerCount(BigDecimal userPerAnswerCount) {
        this.userPerAnswerCount = userPerAnswerCount;
    }

    public CompanyAnswerPerDayVO() {
    }

    public CompanyAnswerPerDayVO(String date, String dayAnswerCount, BigDecimal userPerAnswerCount) {
        this.date = date;
        this.dayAnswerCount = dayAnswerCount;
        this.userPerAnswerCount = userPerAnswerCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
