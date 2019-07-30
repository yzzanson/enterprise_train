package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/10/26 上午10:56
 */
public class PetDynamicEntity {

    private Integer id;

    private Integer companyId;

    private Integer userId;

    //DynamicEnum
    private Integer activeId;

    //当前事件在该表中的id
    private Integer dynamicId;

    private String dynamicContent;

    private Date createTime;

    private Integer isRead;

    private Integer status;

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

    public Integer getActiveId() {
        return activeId;
    }

    public void setActiveId(Integer activeId) {
        this.activeId = activeId;
    }

    public Integer getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(Integer dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public PetDynamicEntity() {
    }

    public PetDynamicEntity(Integer companyId, Integer userId, Integer activeId, Integer dynamicId, String dynamicContent, Date createTime, Integer status) {
        this.companyId = companyId;
        this.userId = userId;
        this.activeId = activeId;
        this.dynamicId = dynamicId;
        this.dynamicContent = dynamicContent;
        this.createTime = createTime;
        this.status = status;
    }

    public PetDynamicEntity(Integer companyId, Integer userId, Integer activeId, Integer dynamicId, String dynamicContent, Date createTime, Integer isRead, Integer status) {
        this.companyId = companyId;
        this.userId = userId;
        this.activeId = activeId;
        this.dynamicId = dynamicId;
        this.dynamicContent = dynamicContent;
        this.createTime = createTime;
        this.isRead = isRead;
        this.status = status;
    }

    public PetDynamicEntity(Integer id, Integer companyId, Integer userId, Integer activeId, Integer dynamicId, String dynamicContent, Date createTime, Integer isRead, Integer status) {
        this.id = id;
        this.companyId = companyId;
        this.userId = userId;
        this.activeId = activeId;
        this.dynamicId = dynamicId;
        this.dynamicContent = dynamicContent;
        this.createTime = createTime;
        this.isRead = isRead;
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
