package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 公司数据每日汇总入库
 * @Author zezhouyang
 * @Date 18/4/13 上午10:06
 */
public class CompanyDaySummaryEntity {

    private Integer id;

    private Integer companyId;

    //成员进入应用数
    private Integer enterCount;

    //答题统计
    private Integer studyCount;

    //新用户数
    private Integer newUserCount;

    //日期
    private Date date;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getEnterCount() {
        return enterCount;
    }

    public void setEnterCount(Integer enterCount) {
        this.enterCount = enterCount;
    }

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }

    public Integer getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(Integer newUserCount) {
        this.newUserCount = newUserCount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public CompanyDaySummaryEntity() {
    }

    public CompanyDaySummaryEntity(Integer companyId, Integer enterCount, Integer studyCount, Integer newUserCount, Date date, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.enterCount = enterCount;
        this.studyCount = studyCount;
        this.newUserCount = newUserCount;
        this.date = date;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
