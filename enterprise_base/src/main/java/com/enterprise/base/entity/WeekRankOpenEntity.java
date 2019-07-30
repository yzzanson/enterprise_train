package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/17 上午9:54
 */
public class WeekRankOpenEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    private Date weekTime;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(Date weekTime) {
        this.weekTime = weekTime;
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

    public WeekRankOpenEntity() {
    }

    public WeekRankOpenEntity(Integer id, Date weekTime, Date updateTime) {
        this.id = id;
        this.weekTime = weekTime;
        this.updateTime = updateTime;
    }

    public WeekRankOpenEntity(Integer companyId, Integer userId, Date weekTime, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.weekTime = weekTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public WeekRankOpenEntity(Integer id, Integer companyId, Integer userId, Date weekTime, Date createTime, Date updateTime) {
        this.id = id;
        this.companyId = companyId;
        this.userId = userId;
        this.weekTime = weekTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
