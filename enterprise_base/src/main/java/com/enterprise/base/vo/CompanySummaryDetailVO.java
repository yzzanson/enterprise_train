package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * @Description 公司统计详情
 * @Author zezhouyang
 * @Date 18/4/14 下午8:30
 */
public class CompanySummaryDetailVO {

    private Integer companyId;

    private String companyName;

    private String installTime;

    private Integer answerCount;

    //人均答题数
    private String userPerAnswerCount;

    //查询日期间,不然则为前7天
    private List<CompanyAnswerPerDayVO> companyAnswerPerDayList;

    private List<NewUserVO> newUserList;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public String getUserPerAnswerCount() {
        return userPerAnswerCount;
    }

    public void setUserPerAnswerCount(String userPerAnswerCount) {
        this.userPerAnswerCount = userPerAnswerCount;
    }

    public List<CompanyAnswerPerDayVO> getCompanyAnswerPerDayList() {
        return companyAnswerPerDayList;
    }

    public void setCompanyAnswerPerDayList(List<CompanyAnswerPerDayVO> companyAnswerPerDayList) {
        this.companyAnswerPerDayList = companyAnswerPerDayList;
    }

    public List<NewUserVO> getNewUserList() {
        return newUserList;
    }

    public void setNewUserList(List<NewUserVO> newUserList) {
        this.newUserList = newUserList;
    }

    public CompanySummaryDetailVO() {
    }

    public CompanySummaryDetailVO(Integer companyId, String companyName, String installTime, Integer answerCount, String userPerAnswerCount) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.installTime = installTime;
        this.answerCount = answerCount;
        this.userPerAnswerCount = userPerAnswerCount;
    }

    public CompanySummaryDetailVO(Integer companyId, String companyName, String installTime, Integer answerCount, String userPerAnswerCount, List<CompanyAnswerPerDayVO> companyAnswerPerDayList, List<NewUserVO> newUserList) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.installTime = installTime;
        this.answerCount = answerCount;
        this.userPerAnswerCount = userPerAnswerCount;
        this.companyAnswerPerDayList = companyAnswerPerDayList;
        this.newUserList = newUserList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
