package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 后台数据答题总数/每次答题数
 * @Author zezhouyang
 * @Date 18/4/15 下午12:48
 */
public class AllPerAnswerVO {

    private String date;

    //答题总数
    private Integer allAnswerCount;

    //打开总次数
    private Integer allUserCount;

    //每次打开答题数
    private String perUserAnswerCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAllAnswerCount() {
        return allAnswerCount;
    }

    public void setAllAnswerCount(Integer allAnswerCount) {
        this.allAnswerCount = allAnswerCount;
    }

    public Integer getAllUserCount() {
        return allUserCount;
    }

    public void setAllUserCount(Integer allUserCount) {
        this.allUserCount = allUserCount;
    }

    public String getPerUserAnswerCount() {
        return perUserAnswerCount;
    }

    public void setPerUserAnswerCount(String perUserAnswerCount) {
        this.perUserAnswerCount = perUserAnswerCount;
    }

    public AllPerAnswerVO() {
    }

    public AllPerAnswerVO(String date, Integer allAnswerCount, Integer allUserCount, String perUserAnswerCount) {
        this.date = date;
        this.allAnswerCount = allAnswerCount;
        this.allUserCount = allUserCount;
        this.perUserAnswerCount = perUserAnswerCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
