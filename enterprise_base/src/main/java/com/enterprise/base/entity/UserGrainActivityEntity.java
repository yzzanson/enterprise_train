package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 19/2/12 上午10:53
 */
public class UserGrainActivityEntity {

    private Integer id;

    private Integer companyId;

    private Integer activityId;

    private Integer userId;

    private Integer grainCost;

    //证书编号
    private String certificateNo;

    private Date createTime;

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

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGrainCost() {
        return grainCost;
    }

    public void setGrainCost(Integer grainCost) {
        this.grainCost = grainCost;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserGrainActivityEntity() {
    }

    public UserGrainActivityEntity(Integer companyId, Integer activityId, Integer userId, Integer grainCost, String certificateNo, Date createTime) {
        this.companyId = companyId;
        this.activityId = activityId;
        this.userId = userId;
        this.grainCost = grainCost;
        this.certificateNo = certificateNo;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
