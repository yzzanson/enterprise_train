package com.enterprise.base.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

/**
 * @Description 数据统计-企业数据vo
 * @Author zezhouyang
 * @Date 18/4/13 下午4:14
 */
public class CompanySummaryVO {

    private Integer companyId;

    //公司名称
    private String companyName;

    //应用状态
    private String appStatus;

    //版本号
    private String version;

    //架构人数
    private Integer userCount;

    //有效用户数
    private Integer activeUserCount;

    //人均答题数
    private BigDecimal userPerAnswerCount;

    //答题总数
    private Integer answerCount;

    //今日答题数
    private Integer todayAnswerCount;

    //安装时间
    private String installTime;

    private Integer contactCall;

    private String availableDay;

    private String corpId;

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

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Integer activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public BigDecimal getUserPerAnswerCount() {
        return userPerAnswerCount;
    }

    public void setUserPerAnswerCount(BigDecimal userPerAnswerCount) {
        this.userPerAnswerCount = userPerAnswerCount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Integer getTodayAnswerCount() {
        return todayAnswerCount;
    }

    public void setTodayAnswerCount(Integer todayAnswerCount) {
        this.todayAnswerCount = todayAnswerCount;
    }

    public String getInstallTime() {
        return installTime;
    }

    public void setInstallTime(String installTime) {
        this.installTime = installTime;
    }

    public Integer getContactCall() {
        return contactCall;
    }

    public void setContactCall(Integer contactCall) {
        this.contactCall = contactCall;
    }

    public String getAvailableDay() {
        return availableDay;
    }

    public void setAvailableDay(String availableDay) {
        this.availableDay = availableDay;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public CompanySummaryVO() {
    }

    public CompanySummaryVO(Integer companyId, String companyName, String appStatus, String version, Integer userCount, Integer activeUserCount, BigDecimal userPerAnswerCount, Integer answerCount, Integer todayAnswerCount, String installTime) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.appStatus = appStatus;
        this.version = version;
        this.userCount = userCount;
        this.activeUserCount = activeUserCount;
        this.userPerAnswerCount = userPerAnswerCount;
        this.answerCount = answerCount;
        this.todayAnswerCount = todayAnswerCount;
        this.installTime = installTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
