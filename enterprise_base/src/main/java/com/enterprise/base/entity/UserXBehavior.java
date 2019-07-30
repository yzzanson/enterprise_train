package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by anson on 18/3/26.
 */
public class UserXBehavior {

    /**
     * Id
     */
    private Integer id;

    private Integer companyId;

    /**
     * user表中的
     */
    private Integer userId;

    /**
     * 0,1 未登录,pk
     * */
    private Integer type;

    /**
     * 状态
     */
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public UserXBehavior() {
    }

    public UserXBehavior(Integer companyId, Integer userId, Integer type, Integer status, Date createTime, Date updateTime) {
        this.companyId = companyId;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public UserXBehavior(Integer id, Integer companyId, Integer userId, Integer type, Integer status, Date createTime, Date updateTime) {
        this.id = id;
        this.companyId = companyId;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
