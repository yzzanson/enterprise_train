package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description 用户的道具
 * @Author zezhouyang
 * @Date 18/9/5 下午4:22
 */
public class BagToolPeopleEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    //道具id
    private Integer toolId;

    //触发事件 BagToolGainTypeEnum
    private Integer eventType;

    //1,2  1未使用 2使用
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToolId() {
        return toolId;
    }

    public void setToolId(Integer toolId) {
        this.toolId = toolId;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
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

    public BagToolPeopleEntity() {
    }

    public BagToolPeopleEntity(Integer id, Integer status, Date updateTime) {
        this.id = id;
        this.status = status;
        this.updateTime = updateTime;
    }

    public BagToolPeopleEntity(Integer companyId, Integer userId,Integer toolId, Integer eventType, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.toolId = toolId;
        this.eventType = eventType;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


}
