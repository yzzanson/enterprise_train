package com.enterprise.base.entity;

/**
 * @Description
 * @Author zezhouyang
 * @Date 18/4/12 下午8:11
 */
public class UserXOpenEntity {

    private Integer id;

    private Integer userId;

    private Integer companyId;

    private Integer status;

    private Integer createTime;

    private Integer updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Integer updateTime) {
        this.updateTime = updateTime;
    }

    public UserXOpenEntity() {
    }

    public UserXOpenEntity(Integer userId, Integer companyId, Integer status) {
        this.userId = userId;
        this.companyId = companyId;
        this.status = status;
    }
}
