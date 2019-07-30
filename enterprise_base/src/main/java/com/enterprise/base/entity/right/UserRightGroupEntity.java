package com.enterprise.base.entity.right;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 用户角色表
 * Created by anson on 18/3/23.
 */
public class UserRightGroupEntity {

    private Integer id;

    private Integer companyId;

    //用户id
    private Integer userId;

    //角色id
    private Integer rightGroupId;

    //是否删除0/1 删除/正常
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

    public Integer getRightGroupId() {
        return rightGroupId;
    }

    public void setRightGroupId(Integer rightGroupId) {
        this.rightGroupId = rightGroupId;
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


    public UserRightGroupEntity() {
    }

    public UserRightGroupEntity(Integer userId) {
        this.userId = userId;
    }


    public UserRightGroupEntity(Integer id, Integer rightGroupId, Integer status, Date updateTime) {
        this.id = id;
        this.rightGroupId = rightGroupId;
        this.status = status;
        this.updateTime = updateTime;
    }

    public UserRightGroupEntity(Integer companyId,Integer userId, Integer status) {
        this.companyId = companyId;
        this.userId = userId;
        this.status = status;
    }

    public UserRightGroupEntity(Integer companyId,Integer userId,Integer rightGroupId, Integer status,Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.rightGroupId = rightGroupId;
        this.status = status;
        this.updateTime = updateTime;
    }

    public UserRightGroupEntity(Integer companyId, Integer userId, Integer rightGroupId, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.rightGroupId = rightGroupId;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
