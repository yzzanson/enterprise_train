package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @Description 某日新增企业/用户数
 * @Author zezhouyang
 * @Date 18/4/15 上午10:55
 */
public class NewUserVO {

    private String date;

    private Integer newUserCount;

    private Integer newCompanyCount;

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Integer getNewCompanyCount() {
        return newCompanyCount;
    }

    public void setNewCompanyCount(Integer newCompanyCount) {
        this.newCompanyCount = newCompanyCount;
    }

    public NewUserVO() {
    }

    public NewUserVO(String date, Integer newUserCount, Integer newCompanyCount) {
        this.date = date;
        this.newUserCount = newUserCount;
        this.newCompanyCount = newCompanyCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}


