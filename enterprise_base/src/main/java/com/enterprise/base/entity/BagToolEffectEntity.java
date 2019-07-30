package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/9/7 上午11:32
 */
public class BagToolEffectEntity {

    private Integer id;

    private Integer companyId;

    //道具类型
    private Integer toolId;

    //道具具体编号
    private Integer toolPeopleId;

    //使用道具用户
    private Integer userId;

    //被道具作用用户
    private Integer effectUserId;

    //消除的作用id
    private Integer elimateId;

    //1作用中 0已消除
    private Integer status;

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

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getToolPeopleId() {
        return toolPeopleId;
    }

    public void setToolPeopleId(Integer toolPeopleId) {
        this.toolPeopleId = toolPeopleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getEffectUserId() {
        return effectUserId;
    }

    public void setEffectUserId(Integer effectUserId) {
        this.effectUserId = effectUserId;
    }

    public Integer getElimateId() {
        return elimateId;
    }

    public void setElimateId(Integer elimateId) {
        this.elimateId = elimateId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public BagToolEffectEntity() {
    }

    public BagToolEffectEntity(Integer companyId, Integer toolId, Integer toolPeopleId, Integer userId, Integer effectUserId, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.toolId = toolId;
        this.toolPeopleId = toolPeopleId;
        this.userId = userId;
        this.effectUserId = effectUserId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
