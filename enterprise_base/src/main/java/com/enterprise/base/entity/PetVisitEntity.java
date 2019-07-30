package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/11/9 下午1:50
 */
public class PetVisitEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    private Integer visitUserId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVisitUserId() {
        return visitUserId;
    }

    public void setVisitUserId(Integer visitUserId) {
        this.visitUserId = visitUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public PetVisitEntity() {
    }

    public PetVisitEntity(Integer companyId, Integer userId, Integer visitUserId, Date createTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.visitUserId = visitUserId;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
