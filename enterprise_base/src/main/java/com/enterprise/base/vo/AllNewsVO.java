package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/15 下午12:48
 */
public class AllNewsVO {

    private String date;

    private Integer userAddCount;

    private Integer compAddCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getUserAddCount() {
        return userAddCount;
    }

    public void setUserAddCount(Integer userAddCount) {
        this.userAddCount = userAddCount;
    }

    public Integer getCompAddCount() {
        return compAddCount;
    }

    public void setCompAddCount(Integer compAddCount) {
        this.compAddCount = compAddCount;
    }

    public AllNewsVO() {
    }

    public AllNewsVO(String date, Integer userAddCount, Integer compAddCount) {
        this.date = date;
        this.userAddCount = userAddCount;
        this.compAddCount = compAddCount;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
